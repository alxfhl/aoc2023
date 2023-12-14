package aoc2023;

import aoc2023.tools.CharMatrix;
import aoc2023.tools.Input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day14 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day14.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        CharMatrix matrix = CharMatrix.valueOf(lines);
        tiltNorth(matrix);
        return getLoad(matrix);
    }

    private static long getLoad(CharMatrix matrix) {
        long load = 0;
        long value = matrix.getHeight();
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                if (matrix.get(x, y) == 'O') {
                    load += value;
                }
            }
            value--;
        }
        return load;
    }

    private static void tiltNorth(CharMatrix matrix) {
        tilt(matrix, 0, 1);
    }

    private static void tiltSouth(CharMatrix matrix) {
        tilt(matrix, 0, -1);
    }

    private static void tiltWest(CharMatrix matrix) {
        tilt(matrix, 1, 0);
    }

    private static void tiltEast(CharMatrix matrix) {
        tilt(matrix, -1, 0);
    }

    private static void tilt(CharMatrix matrix, int dx, int dy) {
        while (true) {
            boolean moved = false;
            for (int y = 0; y < matrix.getHeight(); y++) {
                int fromY = y + dy;
                for (int x = 0; x < matrix.getWidth(); x++) {
                    int fromX = x + dx;
                    if (matrix.isInside(fromX, fromY) && matrix.get(x, y) == '.' && matrix.get(fromX, fromY) == 'O') {
                        matrix.set(x, y, 'O');
                        matrix.set(fromX, fromY, '.');
                        moved = true;
                    }
                }
            }
            if (!moved) {
                break;
            }
        }
    }

    private static long getPart2(List<String> lines) {
        CharMatrix matrix = CharMatrix.valueOf(lines);
        List<CharMatrix> afterCycle = new ArrayList<>();
        afterCycle.add(new CharMatrix(matrix));
        int maxCycles = 1000000000;
        for (int cycle = 1; cycle < maxCycles; cycle++) {
            tiltNorth(matrix);
            tiltWest(matrix);
            tiltSouth(matrix);
            tiltEast(matrix);
            afterCycle.add(new CharMatrix(matrix));
            for (int before = 1; before < cycle; before++) {
                int oldCycle = cycle - before;
                if (afterCycle.get(oldCycle).equals(matrix)) {
                    int loopLength = cycle - oldCycle;
                    if ((maxCycles - cycle) % loopLength == 0) {
                        return getLoad(matrix);
                    }
                }
            }
        }
        return getLoad(matrix);
    }
}
