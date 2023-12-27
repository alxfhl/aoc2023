package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day19.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    record Rule(String attribute, boolean greater, int than, String result) {

        public boolean test(int value) {
            return greater ? value > than : value < than;
        }
    }

    public static long getPart1(List<String> lines) {
        Map<String, List<Rule>> workflows = parseWorkflows(lines);
        long sum = 0;
        for (String line : lines) {
            if (!line.startsWith("{")) {
                continue;
            }
            Map<String, Integer> part = parsePart(line);
            boolean test = test(workflows, part);
            if (test) {
                for (Integer value : part.values()) {
                    sum += value;
                }
            }
        }
        return sum;
    }

    private static boolean test(Map<String, List<Rule>> workflows, Map<String, Integer> part) {
        String state = "in";
        while (true) {
            List<Rule> workflow = workflows.get(state);
            for (Rule rule : workflow) {
                if (rule.attribute == null || rule.test(part.get(rule.attribute))) {
                    state = rule.result;
                    break;
                }
            }
            if ("A".equals(state)) {
                return true;
            }
            if ("R".equals(state)) {
                return false;
            }
        }
    }

    private static Map<String, Integer> parsePart(String line) {
        int start = line.indexOf("{");
        int end = line.indexOf("}");
        Map<String, Integer> result = new HashMap<>();
        for (String part : line.substring(start + 1, end).split(",")) {
            int eq = part.indexOf("=");
            result.put(part.substring(0, eq), Integer.valueOf(part.substring(eq + 1)));
        }
        return result;
    }

    private static Map<String, List<Rule>> parseWorkflows(List<String> lines) {
        Map<String, List<Rule>> result = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\w+)([<>])(\\d+):(\\w+)");
        Pattern pattern2 = Pattern.compile("(\\w+)");
        for (String line : lines) {
            if (line.isBlank()) {
                return result;
            }
            int start = line.indexOf("{");
            int end = line.indexOf("}");
            String name = line.substring(0, start);
            List<Rule> rules = new ArrayList<>();
            for (String part : line.substring(start + 1, end).split(",")) {
                Matcher matcher = pattern.matcher(part);
                if (!matcher.matches()) {
                    matcher = pattern2.matcher(part);
                    if (!matcher.matches()) {
                        System.out.println("error on line " + line);
                    }
                    rules.add(new Rule(null, false, 0, part));
                    continue;
                }
                int value = Integer.parseInt(matcher.group(3));
                rules.add(new Rule(matcher.group(1), ">".equals(matcher.group(2)), value, matcher.group(4)));
            }
            result.put(name, rules);
        }
        throw new IllegalStateException();
    }

    public static long getPart2(List<String> lines) {
        Map<String, List<Rule>> workflows = parseWorkflows(lines);
        return getSum(workflows, "in", new Range(1, 4001), new Range(1, 4001), new Range(1, 4001), new Range(1, 4001));
    }

    private static long getSum(Map<String, List<Rule>> workflows, String state, Range x, Range m, Range a, Range s) {
        if ("A".equals(state)) {
            return x.size() * m.size() * a.size() * s.size();
        }
        if ("R".equals(state)) {
            return 0;
        }
        List<Rule> rules = workflows.get(state);
        long sum = 0;
        for (Rule rule : rules) {
            if (rule.attribute == null) {
                return sum + getSum(workflows, rule.result, x, m, a, s);
            }
            Range ruleRange = rule.greater ? new Range(rule.than + 1, 4001) : new Range(1, rule.than);

            if ("x".equals(rule.attribute)) {
                if (ruleRange.contains(x)) {
                    return sum + getSum(workflows, rule.result, x, m, a, s);
                }
                if (ruleRange.overlaps(x)) {
                    sum += getSum(workflows, rule.result, x.and(ruleRange), m, a, s);
                    x = x.minus(ruleRange).getFirst();
                }
            }

            if ("m".equals(rule.attribute)) {
                if (ruleRange.contains(m)) {
                    return sum + getSum(workflows, rule.result, x, m, a, s);
                }
                if (ruleRange.overlaps(m)) {
                    sum += getSum(workflows, rule.result, x, m.and(ruleRange), a, s);
                    m = m.minus(ruleRange).getFirst();
                }
            }

            if ("a".equals(rule.attribute)) {
                if (ruleRange.contains(a)) {
                    return sum + getSum(workflows, rule.result, x, m, a, s);
                }
                if (ruleRange.overlaps(a)) {
                    sum += getSum(workflows, rule.result, x, m, a.and(ruleRange), s);
                    a = a.minus(ruleRange).getFirst();
                }
            }

            if ("s".equals(rule.attribute)) {
                if (ruleRange.contains(s)) {
                    return sum + getSum(workflows, rule.result, x, m, a, s);
                }
                if (ruleRange.overlaps(s)) {
                    sum += getSum(workflows, rule.result, x, m, a, s.and(ruleRange));
                    s = s.minus(ruleRange).getFirst();
                }
            }
        }
        throw new IllegalStateException();
    }
}
