package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;
import aoc2023.tools.Range;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Comparator.comparing;

/**
 * Disclaimer: I worked on this after I had today's solution, to make it more object-oriented and easier to read.
 * In the end it might be worse than before, but at least I extracted a well tested class Range from this that may be
 * of use in the coming days.
 * And somewhere on the way I lost the solution to part 1 of today's task.
 */
public class Day05 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            seeds: 79 14 55 13
                        
            seed-to-soil map:
            50 98 2
            52 50 48
                        
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                        
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                        
            water-to-light map:
            88 18 7
            18 25 70
                        
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                        
            temperature-to-humidity map:
            0 69 1
            1 0 69
                        
            humidity-to-location map:
            60 56 37
            56 93 4""");

    record MappingEntry(Range sourceRange, long delta) {
    }

    record Mapping(String from, String to, List<MappingEntry> entries) {
    }

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.fromFile("input05");
        for (var lines : List.of(EXAMPLE1, input)) {
            List<Range> seeds = getSeeds(lines);
            List<Mapping> mappings = getMappings(lines);
            List<Range> ranges = mapEverything(seeds, mappings);
            System.out.println("lowest location: " + getMinStart(ranges));
        }
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


    private static long getMinStart(List<Range> ranges) {
        return ranges.stream().mapToLong(Range::start).min().orElseThrow();
    }

    private static List<Range> getSeeds(List<String> lines) {
        List<Long> longs = Parse.getLongs(lines.get(0));
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