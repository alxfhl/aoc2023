package aoc2023;

import aoc2023.tools.CharMatrix;
import aoc2023.tools.Coord2D;
import aoc2023.tools.Direction;
import aoc2023.tools.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day21.class);
        System.out.println("part 1: " + getPart1(input, 64));
        System.out.println("part 2: " + getPart2(input, 26501365));
    }

    public static long getPart1(List<String> lines, int steps) {
        CharMatrix matrix = CharMatrix.valueOf(lines);
        for (int step = 1; step <= steps; step++) {
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

    public static long getPart2(List<String> lines, int steps) {
        CharMatrix original = CharMatrix.valueOf(lines);
        CharMatrix empty = new CharMatrix(original);
        int width = original.getWidth();
        int height = original.getHeight();
        int blockSize = width * 2;
        empty.replace('S', '.');
        Map<Coord2D, SubMatrix> matrixes = new HashMap<>();
        SubMatrix subMatrix = new SubMatrix(1, 0, 0, new CharMatrix(original));
        matrixes.put(new Coord2D(0, 0), subMatrix);

        int remainder = steps % blockSize;
        List<Long> values = new ArrayList<>();
        for (int step = 1; step <= remainder + blockSize * 2; step++) {
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
            if (step % blockSize == remainder) {
                long sum = 0;
                for (SubMatrix value : matrixes.values()) {
                    sum += value.matrix.count('O');
                }
                values.add(sum);
            }
        }

        long offset = values.get(0);
        long quadratic = values.get(2) + values.get(0) - values.get(1) * 2;
        long linear = values.get(1) - offset - quadratic;
        long fullWidths = (steps - remainder) / blockSize;
        return offset + linear * fullWidths + fullWidths * (fullWidths + 1) / 2 * quadratic;
    }

}
