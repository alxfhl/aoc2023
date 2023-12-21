package aoc2023;

import aoc2023.tools.Input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

/**
 * <a href="https://adventofcode.com/2023/day/1">Day 1</a>
 */
public class Day01 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day01.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    private static final Map<String, Integer> DIGITS = Map.of(
            "1", 1,
            "2", 2,
            "3", 3,
            "4", 4,
            "5", 5,
            "6", 6,
            "7", 7,
            "8", 8,
            "9", 9);
    private static final Map<String, Integer> WORDS = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9);

    record Hit(int value, int firstIndex, int lastIndex) {
    }

    public static long getPart1(List<String> lines) {
        return commonPart(lines, DIGITS);
    }

    public static long getPart2(List<String> lines) {
        Map<String, Integer> map = new HashMap<>(DIGITS);
        map.putAll(WORDS);
        return commonPart(lines, map);
    }

    private static long commonPart(List<String> lines, Map<String, Integer> map) {
        long sum = 0;
        for (String line : lines) {
            if (!line.isBlank()) {
                List<Hit> hits = map.keySet().stream()
                        .filter(line::contains)
                        .map(s -> new Hit(map.get(s), line.indexOf(s), line.lastIndexOf(s)))
                        .toList();
                long first = min(hits, comparing(Hit::firstIndex)).value();
                long last = max(hits, comparing(Hit::lastIndex)).value();
                sum += first * 10 + last;
            }
        }
        return sum;
    }

}
