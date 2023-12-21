package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;
import aoc2023.tools.Range;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Comparator.comparing;

public class Day05 {

    record MappingEntry(Range sourceRange, long delta) {
    }

    record Mapping(String from, String to, List<MappingEntry> entries) {
    }

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day05.class);
        System.out.println("lowest location: " + getPart2(input));
    }

    public static long getPart1(List<String> input) {
        List<Long> seeds = getSeeds1(input);
        List<Mapping> mappings = getMappings(input);
        List<Long> ranges = map(seeds, mappings);
        return ranges.stream().mapToLong(Long::longValue).min().orElseThrow();
    }

    private static List<Long> getSeeds1(List<String> lines) {
        return Parse.getLongs(lines.getFirst());
    }

    private static List<Long> map(List<Long> seeds, List<Mapping> mappings) {
        List<Long> values = new ArrayList<>(seeds);
        for (Mapping mapping : mappings) {
            values = values.stream().map(value -> map(value, mapping.entries)).toList();
        }
        return values;
    }

    public static long getPart2(List<String> input) {
        List<Range> seeds = getSeeds2(input);
        List<Mapping> mappings = getMappings(input);
        List<Range> ranges = mapEverything(seeds, mappings);
        return ranges.stream().mapToLong(Range::start).min().orElseThrow();
    }

    private static List<Range> mapEverything(List<Range> seeds, List<Mapping> mappings) {
        List<Range> ranges = new ArrayList<>(seeds);
        for (Mapping mapping : mappings) {
            List<Range> newRanges = new ArrayList<>();
            for (Range range : ranges) {
                map(range, mapping.entries, newRanges);
            }
            ranges = newRanges;
        }
        return ranges;
    }

    private static long map(long value, List<MappingEntry> entries) {
        for (MappingEntry entry : entries) {
            if (entry.sourceRange.contains(value)) {
                return value + entry.delta;
            }
        }
        return value;
    }

    private static void map(Range range, List<MappingEntry> entries, List<Range> result) {
        List<Range> todos = new LinkedList<>();
        todos.add(range);
        for (MappingEntry entry : entries) {
            Iterator<Range> iterator = todos.iterator();
            List<Range> newTodos = new LinkedList<>();
            while (iterator.hasNext()) {
                Range inputRange = iterator.next();
                Range overlap = entry.sourceRange.and(inputRange);
                if (overlap != null) {
                    result.add(overlap.plus(entry.delta));
                    iterator.remove();
                    newTodos.addAll(inputRange.minus(entry.sourceRange));
                }
            }
            todos.addAll(newTodos);
        }
        result.addAll(todos);
    }

    private static List<Range> getSeeds2(List<String> lines) {
        List<Long> longs = Parse.getLongs(lines.getFirst());
        List<Range> seeds = new ArrayList<>();
        for (int i = 0; i < longs.size(); i += 2) {
            seeds.add(new Range(longs.get(i), longs.get(i) + longs.get(i + 1)));
        }
        return seeds;
    }

    private static List<Mapping> getMappings(List<String> lines) {
        Pattern header = Pattern.compile("([^\\-]+)-to-([^\\-]+) map:");
        List<Mapping> mappings = new ArrayList<>();
        Mapping mapping = null;
        for (String line : lines) {
            Matcher matcher = header.matcher(line);
            if (matcher.matches()) {
                mapping = new Mapping(matcher.group(1), matcher.group(2), new ArrayList<>());
                mappings.add(mapping);
                continue;
            }
            if (mapping == null || line.isBlank()) {
                continue;
            }
            List<Long> longs = Parse.getLongs(line);
            long destinationStart = longs.get(0);
            long sourceStart = longs.get(1);
            long length = longs.get(2);
            mapping.entries.add(new MappingEntry(new Range(sourceStart, sourceStart + length), destinationStart - sourceStart));
        }
        for (Mapping aMapping : mappings) {
            aMapping.entries.sort(comparing(MappingEntry::sourceRange));
        }
        return mappings;
    }
}