package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day06 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            Time:      7  15   30
            Distance:  9  40  200""");

    record Race(long time, long distance) {

    }

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day06.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        long product = 1;
        for (Race race : parseRaces(lines)) {
            product *= getWaysToWin(race.time, race.distance);
        }
        return product;
    }

    private static List<Race> parseRaces(List<String> lines) {
        List<Long> times = Parse.getLongs(lines.get(0));
        List<Long> distances = Parse.getLongs(lines.get(1));
        List<Race> races = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            races.add(new Race(times.get(i), distances.get(i)));
        }
        return races;
    }

    private static long getWaysToWin(long time, long distance) {
        long count = 0;
        for (long press = 1; press < time; press++) {
            long me = (time - press) * press;
            if (me > distance) {
                count++;
            }
        }
        return count;
    }

    private static long getPart2(List<String> lines) {
        return getWaysToWin(getOneNumber(lines.get(0)), getOneNumber(lines.get(1)));
    }

    private static long getOneNumber(String s) {
        int colonIndex = s.indexOf(':');
        return Long.parseLong(s.substring(colonIndex + 1).replace(" ", ""));
    }
}
