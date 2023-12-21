package aoc2023;

import aoc2023.tools.CharMatrix;
import aoc2023.tools.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is not my original solution. I extracted the CharMatrix class from it and encapsulated some logic there.
 */
public class Day13 {

    public static final List<String> EXAMPLE1 = List.of(
            "#.##..##.",
            "..#.##.#.",
            "##......#",
            "##......#",
            "..#.##.#.",
            "..##..##.",
            "#.#.##.#.",
            "",
            "#...##..#",
            "#....#..#",
            "..##..###",
            "#####.##.",
            "#####.##.",
            "..##..###",
            "#....#..#");

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day13.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        long sumHorizontal = 0;
        long sumVertical = 0;
        for (CharMatrix pattern : parse(lines)) {
            sumHorizontal += getUniqueHorizontalMirror(pattern);
            sumVertical += getUniqueHorizontalMirror(pattern.transposed());
        }
        return sumVertical + 100L * sumHorizontal;
    }

    private static int getUniqueHorizontalMirror(CharMatrix pattern) {
        return getHorizontalMirror(pattern).stream().findFirst().orElse(0);
    }

    private static List<Integer> getHorizontalMirror(CharMatrix pattern) {
        List<Integer> results = new ArrayList<>();
        for (int i = 1; i < pattern.getHeight(); i++) {
            boolean matches = true;
            for (int j = 0; j < Math.min(i, pattern.getHeight() - i); j++) {
                if (!Arrays.equals(pattern.getRow(i - j - 1), pattern.getRow(i + j))) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                results.add(i);
            }
        }
        return results;
    }

    private static List<CharMatrix> parse(List<String> lines) {
        List<CharMatrix> patterns = new ArrayList<>();
        List<String> pattern = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                if (!pattern.isEmpty()) {
                    patterns.add(CharMatrix.valueOf(pattern));
                    pattern = new ArrayList<>();
                }
            } else {
                pattern.add(line);
            }
        }
        if (!pattern.isEmpty()) {
            patterns.add(CharMatrix.valueOf(pattern));
        }
        return patterns;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static long getPart2(List<String> lines) {
        long sumHorizontal = 0;
        long sumVertical = 0;
        for (CharMatrix pattern : parse(lines)) {
            CharMatrix transposed = pattern.transposed();
            int width = pattern.getWidth();
            int height = pattern.getHeight();
            Integer originalHorizontalMirror = getUniqueHorizontalMirror(pattern);
            Integer originalVerticalMirror = getUniqueHorizontalMirror(transposed);
            outer:
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    List<Integer> newHorizontals = getMirrorsWithSmudge(pattern, x, y);
                    List<Integer> newVerticals = getMirrorsWithSmudge(transposed, y, x);

                    newHorizontals.remove(originalHorizontalMirror);
                    newVerticals.remove(originalVerticalMirror);
                    if (!newHorizontals.isEmpty() || !newVerticals.isEmpty()) {
                        sumHorizontal += newHorizontals.isEmpty() ? 0 : newHorizontals.getFirst();
                        sumVertical += newVerticals.isEmpty() ? 0 : newVerticals.getFirst();
                        break outer;
                    }
                }
            }
        }
        return sumVertical + 100L * sumHorizontal;
    }

    private static List<Integer> getMirrorsWithSmudge(CharMatrix pattern, int x, int y) {
        pattern.set(x, y, pattern.get(x, y) == '.' ? '#' : '.');
        List<Integer> newHorizontals = getHorizontalMirror(pattern);
        pattern.set(x, y, pattern.get(x, y) == '.' ? '#' : '.');
        return newHorizontals;
    }

}
