package aoc2023;

import aoc2023.tools.Input;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Day10 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day10.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    @RequiredArgsConstructor
    static class Tile {
        private final int x;
        private final int y;
        private final boolean east;
        private final boolean west;
        private final boolean north;
        private final boolean south;
        private int distance = -1;
    }

    public static long getPart1(List<String> lines) {
        List<List<Tile>> tiles = parse(lines);
        return tiles.stream().flatMap(List::stream).mapToInt(tile -> tile.distance).max().orElseThrow();
    }

    private static List<List<Tile>> parse(List<String> lines) {
        List<List<String>> grid = lines.stream().filter(Predicate.not(String::isBlank)).map(string -> Arrays.asList(string.split(""))).toList();
        int height = grid.size();
        int width = grid.getFirst().size();
        Tile start = null;
        for (int y = 0; y < height; y++) {
            int index = grid.get(y).indexOf("S");
            if (index >= 0) {
                // hard coded directions of start to solve the puzzle faster
                if (height < 10) {
                    start = new Tile(index, y, true, false, false, true);
                } else {
                    start = new Tile(index, y, true, true, false, false);
                }
                start.distance = 0;
                break;
            }
        }
        List<List<Tile>> tiles = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            ArrayList<Tile> row = new ArrayList<>();
            tiles.add(row);
            for (int x = 0; x < width; x++) {
                String symbol = grid.get(y).get(x);
                if ("S".equals(symbol)) {
                    row.add(start);
                } else {
                    row.add(new Tile(x, y, east(symbol), west(symbol), north(symbol), south(symbol)));
                }
            }
        }
        List<Tile> todo = new LinkedList<>();
        todo.add(start);
        while (!todo.isEmpty()) {
            Tile next = todo.removeFirst();
            List<Tile> neighbors = new ArrayList<>();
            if (next.west) {
                neighbors.add(tiles.get(next.y).get(next.x - 1));
            }
            if (next.east) {
                neighbors.add(tiles.get(next.y).get(next.x + 1));
            }
            if (next.north) {
                neighbors.add(tiles.get(next.y - 1).get(next.x));
            }
            if (next.south) {
                neighbors.add(tiles.get(next.y + 1).get(next.x));
            }
            for (Tile neighbor : neighbors) {
                if (neighbor.distance < 0) {
                    todo.add(neighbor);
                    neighbor.distance = next.distance + 1;
                }
            }
        }
        return tiles;
    }

    private static boolean south(String symbol) {
        return "|".equals(symbol) || "F".equals(symbol) || "7".equals(symbol);
    }

    private static boolean north(String symbol) {
        return "|".equals(symbol) || "J".equals(symbol) || "L".equals(symbol);
    }

    private static boolean west(String symbol) {
        return "-".equals(symbol) || "J".equals(symbol) || "7".equals(symbol);
    }

    private static boolean east(String symbol) {
        return "-".equals(symbol) || "L".equals(symbol) || "F".equals(symbol);
    }

    public static long getPart2(List<String> lines) {
        List<List<Tile>> tiles = parse(lines);
        int count = 0;
        for (List<Tile> row : tiles) {
            boolean inside = false;
            boolean onPipeFromSouth = false;
            boolean onPipeFromNorth = false;
            for (Tile tile : row) {
                if (tile.distance < 0) {
                    // not on pipe
                    onPipeFromSouth = false;
                    onPipeFromNorth = false;
                    if (inside) {
                        count++;
                    }
                    continue;
                }
                if (tile.south) {
                    onPipeFromSouth = !onPipeFromSouth;
                }
                if (tile.north) {
                    onPipeFromNorth = !onPipeFromNorth;
                }
                if (onPipeFromNorth && onPipeFromSouth) {
                    onPipeFromSouth = false;
                    onPipeFromNorth = false;
                    inside = !inside;
                }
            }
        }
        return count;
    }
}
