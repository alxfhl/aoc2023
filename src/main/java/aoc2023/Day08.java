package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.MathUtils;

import java.io.IOException;
import java.util.*;

public class Day08 {
    public static final List<String> EXAMPLE1 = Input.fromString("""
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)""");
    public static final List<String> EXAMPLE2 = Input.fromString("""
            LLR
                        
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day08.class);
        for (var lines : List.of(EXAMPLE1, EXAMPLE2, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2 (fast): " + getPart2Fast(lines));
            System.out.println("part 2 (initial solution): " + getPart2(lines));
        }
    }

    record Node(String left, String right) {
    }

    private static long getPart1(List<String> lines) {
        List<String> instructions = List.of(lines.getFirst().split(""));
        Map<String, Node> nodes = parseNodes(lines);
        String position = "AAA";
        int steps = 0;
        while (!position.equals("ZZZ")) {
            String instruction = instructions.get(steps % instructions.size());
            steps++;
            Node node = nodes.get(position);
            position = instruction.equals("L") ? node.left : node.right;
        }
        return steps;
    }

    record Position(String name, long first, long round) {
    }

    private static long getPart2(List<String> lines) {
        List<String> instructions = List.of(lines.getFirst().split(""));
        Map<String, Node> nodes = parseNodes(lines);
        List<String> startingNodes = nodes.keySet().stream().filter(name -> name.endsWith("A")).toList();
        List<Position> list = new ArrayList<>();
        for (String position : startingNodes) {
            String start = position;
            long steps = 0;
            long first = 0;
            while (true) {
                String instruction = instructions.get((int) steps % instructions.size());
                steps++;
                Node node = nodes.get(position);
                position = instruction.equals("L") ? node.left : node.right;
                if (position.endsWith("Z")) {
                    if (first == 0) {
                        first = steps;
                    } else {
                        list.add(new Position(start, first, steps - first));
                        break;
                    }
                }
            }
        }
        long steps = list.stream().map(Position::first).max(Comparator.naturalOrder()).orElseThrow();
        List<Long> stepsOnRoundtrip = new ArrayList<>();
        for (Position position : list) {
            stepsOnRoundtrip.add((steps - position.first) % position.round);
        }
        while (!stepsOnRoundtrip.stream().allMatch(x -> x == 0)) {
            long required = 0;
            for (int i = 0; i < list.size(); i++) {
                required = Math.max(required, list.get(i).round - stepsOnRoundtrip.get(i));
            }
            steps += required;
            for (int i = 0; i < list.size(); i++) {
                stepsOnRoundtrip.set(i, (stepsOnRoundtrip.get(i) + required) % list.get(i).round);
            }
        }
        return steps;
    }

    private static long getPart2Fast(List<String> lines) {
        List<String> instructions = List.of(lines.getFirst().split(""));
        Map<String, Node> nodes = parseNodes(lines);
        List<String> startingNodes = nodes.keySet().stream().filter(name -> name.endsWith("A")).toList();
        List<Position> list = new ArrayList<>();
        for (String position : startingNodes) {
            String start = position;
            long steps = 0;
            long first = 0;
            while (true) {
                String instruction = instructions.get((int) steps % instructions.size());
                steps++;
                Node node = nodes.get(position);
                position = instruction.equals("L") ? node.left : node.right;
                if (position.endsWith("Z")) {
                    if (first == 0) {
                        first = steps;
                    } else {
                        list.add(new Position(start, first, steps - first));
                        break;
                    }
                }
            }
        }
        if (list.stream().anyMatch(pos -> pos.first != pos.round)) {
            // this algorithm only works if "first" equals "round" as in the real problem statement
            return -42;
        }
        long steps = 1;
        for (Position position : list) {
            steps = MathUtils.lcm(steps, position.round);
        }
        return steps;
    }

    private static Map<String, Node> parseNodes(List<String> lines) {
        Map<String, Node> nodes = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.isBlank()) {
                nodes.put(line.substring(0, 3), new Node(line.substring(7, 10), line.substring(12, 15)));
            }
        }
        return nodes;
    }
}
