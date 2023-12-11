package aoc2023;

import aoc2023.tools.Input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Day11 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day11.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2 (10): " + getPart2(lines, 10));
            System.out.println("part 2 (100): " + getPart2(lines, 100));
            System.out.println("part 2 (1000000): " + getPart2(lines, 1000000));
        }
    }

    record Galaxy(long x, long y) {

    }

    private static Long getPart1(List<String> lines) {
        List<Galaxy> galaxies = parse(lines);
        galaxies = expand(galaxies, 1);
        return shortestPaths(galaxies);
    }

    private static Long getPart2(List<String> lines, long factor) {
        List<Galaxy> galaxies = parse(lines);
        galaxies = expand(galaxies, factor - 1);
        return shortestPaths(galaxies);
    }

    private static long shortestPaths(List<Galaxy> galaxies) {
        long sum = 0;
        for (int g1 = 0; g1 < galaxies.size() - 1; g1++) {
            Galaxy gal1 = galaxies.get(g1);
            for (int g2 = g1 + 1; g2 < galaxies.size(); g2++) {
                Galaxy gal2 = galaxies.get(g2);
                sum += Math.abs(gal2.x - gal1.x) + Math.abs(gal2.y - gal1.y);
            }
        }
        return sum;
    }

    private static List<Galaxy> expand(List<Galaxy> galaxies, long factor) {
        long width = galaxies.stream().mapToLong(Galaxy::x).max().orElseThrow();
        long height = galaxies.stream().mapToLong(Galaxy::y).max().orElseThrow();
        Set<Long> emptyColumns = new LinkedHashSet<>();
        for (long x = 0; x < width; x++) {
            long finalX = x;
            if (galaxies.stream().noneMatch(g -> g.x == finalX)) {
                emptyColumns.add(x);
            }
        }
        Set<Long> emptyRows = new LinkedHashSet<>();
        for (long y = 0; y < height; y++) {
            long finalY = y;
            if (galaxies.stream().noneMatch(g -> g.y == finalY)) {
                emptyRows.add(y);
            }
        }
        List<Galaxy> result = new ArrayList<>();
        for (Galaxy galaxy : galaxies) {
            long addX = emptyColumns.stream().filter(col -> col < galaxy.x).count();
            long addY = emptyRows.stream().filter(row -> row < galaxy.y).count();
            result.add(new Galaxy(galaxy.x + addX * factor, galaxy.y + addY * factor));
        }
        return result;
    }

    private static List<Galaxy> parse(List<String> lines) {
        List<Galaxy> galaxies = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    galaxies.add(new Galaxy(x, y));
                }
            }
        }
        return galaxies;
    }
}
