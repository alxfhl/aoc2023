package aoc2023;

import aoc2023.tools.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Day02 {

    private static final Logger LOG = LoggerFactory.getLogger(Day02.class);

    public static final List<String> EXAMPLE1 = Input.fromString("""
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green""");

    record Draw(Map<String, Integer> cubes) {

    }

    record Game(int number, List<Draw> draws) {

    }

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.fromFile("input02");
        for (var lines : List.of(EXAMPLE1, input)) {
            try {
                System.out.println("part 1: " + getValue(lines));
                System.out.println("part 2: " + getValue2(lines));
            } catch (RuntimeException e) {
                LOG.error("Unexpected error: ", e);
            }
        }
    }

    private static int getValue(List<String> lines) {
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

    private static long getValue2(List<String> lines) {
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
