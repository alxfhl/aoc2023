package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day09 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day09.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        return lines.stream().mapToLong(line -> predict(Parse.getLongs(line))).sum();
    }

    private static long getPart2(List<String> lines) {
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
