package aoc2023;

import aoc2023.tools.Input;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;

public class Day01 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet""");

    public static final List<String> EXAMPLE2 = Input.fromString("""
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day01.class);
        for (var lines : List.of(EXAMPLE1, EXAMPLE2, input)) {
            System.out.println(getValue(lines));
        }
    }

    private static final Map<String, Integer> VALUES = new HashMap<>();

    static {
        VALUES.putAll(Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8, "nine", 9));
        VALUES.putAll(Map.of("1", 1, "2", 2, "3", 3, "4", 4, "5", 5, "6", 6, "7", 7, "8", 8, "9", 9));
    }

    record Hit(int value, int firstIndex, int lastIndex) {
    }

    private static int getValue(List<String> lines) {
        return lines.stream().filter(not(String::isBlank)).mapToInt(Day01::getValue).sum();
    }

    private static int getValue(String line) {
        List<Hit> hits = VALUES.keySet().stream()
                .filter(line::contains)
                .map(s -> new Hit(VALUES.get(s), line.indexOf(s), line.lastIndexOf(s)))
                .toList();
        int first = min(hits, comparing(Hit::firstIndex)).value();
        int last = max(hits, comparing(Hit::lastIndex)).value();
        return first * 10 + last;
    }
}
