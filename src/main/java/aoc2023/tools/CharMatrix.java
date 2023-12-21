package aoc2023.tools;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SuspiciousNameCombination")
@EqualsAndHashCode
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

    public CharMatrix(int width, int height, char fill) {
        this.width = width;
        this.height = height;
        this.chars = new char[width * height];
        Arrays.fill(this.chars, fill);
    }

    public CharMatrix transposed() {
        CharMatrix newMatrix = new CharMatrix(height, width, new char[this.chars.length]);
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newMatrix.chars[x * height + y] = chars[index++];
            }
        }
        return newMatrix;
    }

    public CharMatrix rotatedLeft() {
        CharMatrix newMatrix = new CharMatrix(height, width, new char[this.chars.length]);
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newMatrix.chars[(width - 1 - x) * height + y] = chars[index++];
            }
        }
        return newMatrix;
    }

    public CharMatrix rotatedRight() {
        CharMatrix newMatrix = new CharMatrix(height, width, new char[this.chars.length]);
        int index = 0;
        for (int y = 0; y < height; y++) {
            int newY = height - 1 - y;
            for (int x = 0; x < width; x++) {
                newMatrix.chars[x * height + newY] = chars[index++];
            }
        }
        return newMatrix;
    }

    public CharMatrix rotated180() {
        CharMatrix newMatrix = new CharMatrix(width, height, new char[this.chars.length]);
        int index = 0;
        for (int y = 0; y < height; y++) {
            int newY = height - y;
            for (int x = 0; x < width; x++) {
                newMatrix.chars[newY * width - 1 - x] = chars[index++];
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

    public void set(Coord2D coord2D, char ch) {
        set((int) coord2D.x(), (int) coord2D.y(), ch);
    }

    public void set(int x, int y, char ch) {
        chars[getIndex(x, y)] = ch;
    }

    public char get(Coord2D coord2D) {
        return get((int) coord2D.x(), (int) coord2D.y());
    }

    public char get(int x, int y) {
        return chars[getIndex(x, y)];
    }

    public char[] getRow(int y) {
        char[] result = new char[width];
        System.arraycopy(chars, y * width, result, 0, width);
        return result;
    }

    public char[] getColumn(int x) {
        char[] result = new char[height];
        for (int y = 0; y < height; y++) {
            result[y] = chars[y * width + x];
        }
        return result;
    }

    private int getIndex(int x, int y) {
        if (isOutside(x, y)) {
            throw new ArrayIndexOutOfBoundsException("(" + x + "," + y + ") is outside of (0.." + (width - 1) + ",0.." + (height - 1) + ")");
        }
        return y * width + x;
    }

    public boolean isInside(Coord2D coord2D) {
        return isInside((int) coord2D.x(), (int) coord2D.y());
    }

    public boolean isOutside(Coord2D coord2D) {
        return isOutside((int) coord2D.x(), (int) coord2D.y());
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isOutside(int x, int y) {
        return x < 0 || x >= width || y < 0 || y >= height;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("Matrix ").append(width).append(":").append(height).append("\n");
        for (int y = 0; y < height; y++) {
            sb.append(chars, y * width, width).append("\n");
        }
        return sb.toString();
    }

    public long count(char ch) {
        long count = 0;
        for (char achar : chars) {
            if (achar == ch) {
                count++;
            }
        }
        return count;
    }

    public void replace(char search, char replacement) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == search) {
                chars[i] = replacement;
            }
        }
    }
}
