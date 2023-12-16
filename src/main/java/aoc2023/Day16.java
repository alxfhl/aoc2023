package aoc2023;

import aoc2023.tools.Coord2D;
import aoc2023.tools.Input;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day16 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....""");

    @Data
    static class Tile {
        private char ch;
        private boolean left;
        private boolean right;
        private boolean up;
        private boolean down;

        public Tile(char ch) {
            this.ch = ch;
        }
    }

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day16.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        List<Coord2D> fromLeft = new ArrayList<>();
        fromLeft.add(new Coord2D(0, 0));
        return evaluate(parse(lines), fromLeft, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private static long getPart2(List<String> lines) {
        List<List<Tile>> grid = parse(lines);
        int width = grid.getFirst().size();
        int height = grid.size();
        long max = 0;
        for (int x = 0; x < width; x++) {
            List<Coord2D> fromUp = new ArrayList<>();
            fromUp.add(new Coord2D(x, 0));
            long energized = evaluate(parse(lines), new ArrayList<>(), new ArrayList<>(), fromUp, new ArrayList<>());
            max = Math.max(max, energized);
        }
        for (int x = 0; x < width; x++) {
            List<Coord2D> fromDown = new ArrayList<>();
            fromDown.add(new Coord2D(x, height - 1));
            long energized = evaluate(parse(lines), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), fromDown);
            max = Math.max(max, energized);
        }
        for (int y = 0; y < height; y++) {
            List<Coord2D> fromLeft = new ArrayList<>();
            fromLeft.add(new Coord2D(0, y));
            long energized = evaluate(parse(lines), fromLeft, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            max = Math.max(max, energized);
        }
        for (int y = 0; y < height; y++) {
            List<Coord2D> fromRight = new ArrayList<>();
            fromRight.add(new Coord2D(width - 1, y));
            long energized = evaluate(parse(lines), new ArrayList<>(), fromRight, new ArrayList<>(), new ArrayList<>());
            max = Math.max(max, energized);
        }
        return max;
    }

    private static long evaluate(List<List<Tile>> grid, List<Coord2D> fromLeft, List<Coord2D> fromRight,
                                 List<Coord2D> fromUp, List<Coord2D> fromDown) {
        int width = grid.getFirst().size();
        int height = grid.size();
        while (!fromLeft.isEmpty() || !fromRight.isEmpty() || !fromUp.isEmpty() || !fromDown.isEmpty()) {
            while (!fromLeft.isEmpty()) {
                Coord2D from = fromLeft.removeLast();
                if (from.isInGrid(width, height)) {
                    Tile tile = grid.get(from.y()).get(from.x());
                    if (tile.ch == '.' || tile.ch == '-') {
                        addRight(fromLeft, tile, from);
                    }
                    if (tile.ch == '/' || tile.ch == '|') {
                        addUp(fromDown, tile, from);
                    }
                    if (tile.ch == '\\' || tile.ch == '|') {
                        addDown(fromUp, tile, from);
                    }
                }
            }
            while (!fromRight.isEmpty()) {
                Coord2D from = fromRight.removeLast();
                if (from.isInGrid(width, height)) {
                    Tile tile = grid.get(from.y()).get(from.x());
                    if (tile.ch == '.' || tile.ch == '-') {
                        addLeft(fromRight, tile, from);
                    }
                    if (tile.ch == '\\' || tile.ch == '|') {
                        addUp(fromDown, tile, from);
                    }
                    if (tile.ch == '/' || tile.ch == '|') {
                        addDown(fromUp, tile, from);
                    }
                }
            }
            while (!fromUp.isEmpty()) {
                Coord2D from = fromUp.removeLast();
                if (from.isInGrid(width, height)) {
                    Tile tile = grid.get(from.y()).get(from.x());
                    if (tile.ch == '.' || tile.ch == '|') {
                        addDown(fromUp, tile, from);
                    }
                    if (tile.ch == '/' || tile.ch == '-') {
                        addLeft(fromRight, tile, from);
                    }
                    if (tile.ch == '\\' || tile.ch == '-') {
                        addRight(fromLeft, tile, from);
                    }
                }
            }
            while (!fromDown.isEmpty()) {
                Coord2D from = fromDown.removeLast();
                if (from.isInGrid(width, height)) {
                    Tile tile = grid.get(from.y()).get(from.x());
                    if (tile.ch == '.' || tile.ch == '|') {
                        addUp(fromDown, tile, from);
                    }
                    if (tile.ch == '\\' || tile.ch == '-') {
                        addLeft(fromRight, tile, from);
                    }
                    if (tile.ch == '/' || tile.ch == '-') {
                        addRight(fromLeft, tile, from);
                    }
                }
            }
        }
        return grid.stream().flatMap(List::stream).filter(t -> t.left || t.right || t.up || t.down).count();
    }

    private static List<List<Tile>> parse(List<String> lines) {
        List<List<Tile>> grid = new ArrayList<>();
        for (String line : lines) {
            ArrayList<Tile> row = new ArrayList<>();
            grid.add(row);
            for (char ch : line.toCharArray()) {
                row.add(new Tile(ch));
            }
        }
        return grid;
    }

    private static void addLeft(List<Coord2D> fromRight, Tile tile, Coord2D from) {
        if (!tile.left) {
            tile.left = true;
            fromRight.add(new Coord2D(from.x() - 1, from.y()));
        }
    }

    private static void addDown(List<Coord2D> fromUp, Tile tile, Coord2D from) {
        if (!tile.down) {
            tile.down = true;
            fromUp.add(new Coord2D(from.x(), from.y() + 1));
        }
    }

    private static void addUp(List<Coord2D> fromDown, Tile tile, Coord2D from) {
        if (!tile.up) {
            tile.up = true;
            fromDown.add(new Coord2D(from.x(), from.y() - 1));
        }
    }

    private static void addRight(List<Coord2D> fromLeft, Tile tile, Coord2D from) {
        if (!tile.right) {
            tile.right = true;
            fromLeft.add(new Coord2D(from.x() + 1, from.y()));
        }
    }
}
