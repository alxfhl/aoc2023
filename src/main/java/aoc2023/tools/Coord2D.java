package aoc2023.tools;

public record Coord2D(int x, int y) {
    /**
     * @return true if this coord is inside an area that is width x height in size and starts at 0/0.
     */
    public boolean isInGrid(int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
