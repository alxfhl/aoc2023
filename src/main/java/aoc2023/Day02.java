package aoc2023;

import aoc2023.tools.Input;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Day02 {

    record Draw(Map<String, Integer> cubes) {

    }

    record Game(int number, List<Draw> draws) {

    }

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day02.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    public static int getPart1(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            var game = parse(line);
            if (possible(game, 12, "red")
                    && possible(game, 13, "green")
                    && possible(game, 14, "blue")) {
                sum += game.number;
            }
        }
        return sum;
    }

    public static long getPart2(List<String> lines) {
        long sum = 0;
        for (String line : lines) {
            var game = parse(line);
            Map<String, Integer> minimums = new LinkedHashMap<>();
            for (Draw draw : game.draws) {
                for (Map.Entry<String, Integer> entry : draw.cubes.entrySet()) {
                    if (!minimums.containsKey(entry.getKey()) || minimums.get(entry.getKey()) < entry.getValue()) {
                        minimums.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            long power = 1;
            for (Integer value : minimums.values()) {
                power *= value;
            }
            sum += power;
        }
        return sum;
    }

    private static boolean possible(Game game, int count, String color) {
        return game.draws.stream().noneMatch(draw -> draw.cubes.get(color) != null && draw.cubes.get(color) > count);
    }

    private static Game parse(String line) {
        line = line.substring(5);
        int colonIndex = line.indexOf(':');
        int number = Integer.parseInt(line.substring(0, colonIndex));
        List<Draw> draws = Arrays.stream(line.substring(colonIndex + 2).split("; ")).map(Day02::parseDraw).toList();
        return new Game(number, draws);
    }

    private static Draw parseDraw(String s) {
        Map<String, Integer> cubes = new LinkedHashMap<>();
        for (String part : s.split(", ")) {
            int index = part.indexOf(" ");
            cubes.put(part.substring(index + 1), Integer.valueOf(part.substring(0, index)));
        }
        return new Draw(cubes);
    }
}
