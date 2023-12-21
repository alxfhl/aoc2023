package aoc2023;

import aoc2023.tools.CharMatrix;
import aoc2023.tools.Coord2D;
import aoc2023.tools.Direction;
import aoc2023.tools.Input;

import java.io.IOException;
import java.util.*;

public class Day21 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
            """);

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day21.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
        }
        for (var lines : List.of(input)) {
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        CharMatrix matrix = CharMatrix.valueOf(lines);
        for (int step = 1; step <= 64; step++) {
            CharMatrix newMatrix = new CharMatrix(matrix);
            for (int y = 0; y < matrix.getHeight(); y++) {
                for (int x = 0; x < matrix.getWidth(); x++) {
                    char now = matrix.get(x, y);
                    if (now == 'S' || now == 'O') {
                        newMatrix.set(x, y, '.');
                        for (Direction dir : Direction.values()) {
                            int nx = x + dir.dx();
                            int ny = y + dir.dy();
                            if (matrix.isInside(nx, ny) && matrix.get(nx, ny) == '.') {
                                newMatrix.set(nx, ny, 'O');
                            }
                        }
                    }
                }
            }
            matrix = newMatrix;
        }
        return matrix.count('O');
    }

    record SubMatrix(int firstContact, long deltaX, long deltaY, CharMatrix matrix) {
        List<Coord2D> doStep() {
            List<Coord2D> positions = new ArrayList<>();
            for (int y = 0; y < matrix.getHeight(); y++) {
                for (int x = 0; x < matrix.getWidth(); x++) {
                    char ch = matrix.get(x, y);
                    if (ch == 'S' || ch == 'O') {
                        positions.add(new Coord2D(x, y));
                        matrix.set(x, y, '.');
                    }
                }
            }
            List<Coord2D> outside = new ArrayList<>();
            for (Direction dir : Direction.values()) {
                for (Coord2D position : positions) {
                    Coord2D newPosition = position.go(dir);
                    if (matrix.isInside(newPosition)) {
                        if (matrix.get(newPosition) == '.') {
                            matrix.set(newPosition, 'O');
                        }
                    } else {
                        outside.add(newPosition.go(deltaX, deltaY));
                    }
                }
            }
            return outside;
        }
    }

    private static long getPart2(List<String> lines) {
        CharMatrix original = CharMatrix.valueOf(lines);
        CharMatrix empty = new CharMatrix(original);
        int width = original.getWidth();
        int height = original.getHeight();
        empty.replace('S', '.');
        Map<Coord2D, SubMatrix> matrixes = new HashMap<>();
        SubMatrix subMatrix = new SubMatrix(1, 0, 0, new CharMatrix(original));
        matrixes.put(new Coord2D(0, 0), subMatrix);


        for (int step = 1; step <= 65 + 131 * 5; step++) {
            List<Coord2D> outside = new ArrayList<>();
            for (var matrix : matrixes.values()) {
                outside.addAll(matrix.doStep());
            }
            for (Coord2D coord2D : outside) {
                long x = coord2D.x();
                long y = coord2D.y();
                long relX = ((x % width) + width) % width;
                long relY = ((y % height) + height) % height;
                Coord2D newMatrixPosition = new Coord2D((x - relX) / width, (y - relY) / height);
                int finalStep1 = step;
                matrixes.computeIfAbsent(newMatrixPosition, k -> new SubMatrix(finalStep1, x - relX, y - relY, new CharMatrix(empty)))
                        .matrix.set((int) relX, (int) relY, 'O');
            }
            // 65 = 26501365 % 131 (the width of the matrix)
            if (step % 131 == 65) {
                long sum = 0;
                for (SubMatrix value : matrixes.values()) {
                    sum += value.matrix.count('O');
                }
                System.out.println("step " + step + ": " + sum);
            }
        }

        // (obviously) very poor mathematical skills and Excel lead to this algorithm deduced by the previous results.
        // I am just a little bit ashamed.
        long sum = 188951;
        long steps = 458;
        long delta = 92480; // delta to the sum 131 steps ago.
        while (steps < 26501365) {
            steps += 131;
            delta += 30794;
            sum += delta;
        }
        return sum;
    }

}
