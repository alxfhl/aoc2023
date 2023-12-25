package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;

import java.util.ArrayList;
import java.util.List;

public class Day09 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day09.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    public static long getPart1(List<String> lines) {
        return lines.stream().mapToLong(line -> predict(Parse.getLongs(line))).sum();
    }

    public static long getPart2(List<String> lines) {
        return lines.stream().mapToLong(line -> predict(Parse.getLongs(line).reversed())).sum();
    }

    private static long predict(List<Long> longs) {
        if (longs.stream().allMatch(l -> l == 0)) {
            return 0;
        }
        List<Long> differences = new ArrayList<>();
        for (int i = 1; i < longs.size(); i++) {
            differences.add(longs.get(i) - longs.get(i - 1));
        }
        return longs.getLast() + predict(differences);
    }
}
