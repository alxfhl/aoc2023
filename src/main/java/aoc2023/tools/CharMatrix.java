package aoc2023.tools;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CharMatrix {
    @Getter
    private final int width;
    @Getter
    private final int height;
    private final char[] chars;

    public CharMatrix(CharMatrix original) {
        this.width = original.width;
        this.height = original.height;
        this.chars = new char[original.chars.length];
        System.arraycopy(original.chars, 0, this.chars, 0, this.chars.length);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public CharMatrix transposed() {
        CharMatrix newMatrix = new CharMatrix(height, width, new char[this.chars.length]);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newMatrix.set(y, x, get(x, y));
            }
        }
        return newMatrix;
    }

    public static CharMatrix valueOf(List<String> lines) {
        if (lines.isEmpty()) {
            return new CharMatrix(0, 0, new char[0]);
        }
        int width = lines.getFirst().length();
        int height = lines.size();
        char[] chars = new char[width * height];
        CharMatrix matrix = new CharMatrix(width, height, chars);
        int y = 0;
        for (String line : lines) {
            if (line.length() != width) {
                throw new IllegalArgumentException("All lines must have the same length!");
            }
            int x = 0;
            for (char ch : line.toCharArray()) {
                matrix.set(x, y, ch);
                x++;
            }
            y++;
        }
        return matrix;
    }

    public void set(int x, int y, char ch) {
        chars[getIndex(x, y)] = ch;
    }

    public char get(int x, int y) {
        return chars[getIndex(x, y)];
    }

    public char[] getRow(int y) {
        char[] result = new char[width];
        for (int x = 0; x < width; x++) {
            result[x] = get(x, y);
        }
        return result;
    }

    public char[] getColumn(int x) {
        char[] result = new char[height];
        for (int y = 0; y < height; y++) {
            result[y] = get(x, y);
        }
        return result;
    }

    private int getIndex(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new ArrayIndexOutOfBoundsException(
                    "(" + x + "," + y + ") is outside of (0.." + (width - 1) + ",0.." + (height - 1) + ")");
        }
        return y * width + x;
    }
}