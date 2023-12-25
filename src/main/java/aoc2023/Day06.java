package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;

import java.util.ArrayList;
import java.util.List;

public class Day06 {

    public record Race(long time, long distance) {

    }

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day06.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    public static long getPart1(List<String> lines) {
        long product = 1;
        for (Race race : parseRaces(lines)) {
            product *= getWaysToWin(race.time, race.distance);
        }
        return product;
    }

    public static List<Race> parseRaces(List<String> lines) {
        List<Long> times = Parse.getLongs(lines.get(0));
        List<Long> distances = Parse.getLongs(lines.get(1));
        List<Race> races = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            races.add(new Race(times.get(i), distances.get(i)));
        }
        return races;
    }

    private static long getWaysToWin(long time, long distance) {
        for (long press = 1; press < time; press++) {
            long me = (time - press) * press;
            if (me > distance) {
                return (time - 1) - 2 * (press - 1);
            }
        }
        return 0;
    }

    public static long getPart2(List<String> lines) {
        return getWaysToWin(getOneNumber(lines.get(0)), getOneNumber(lines.get(1)));
    }

    private static long getOneNumber(String s) {
        int colonIndex = s.indexOf(':');
        return Long.parseLong(s.substring(colonIndex + 1).replace(" ", ""));
    }
}
