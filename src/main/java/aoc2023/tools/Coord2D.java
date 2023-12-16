package aoc2023.tools;

public record Coord2D(long x, long y) {
    /**
     * @return true if this coord is inside an area that is width x height in size and starts at 0/0.
     */
    public boolean isInGrid(long width, long height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Coord2D go(Direction direction) {
        return new Coord2D(x + direction.dx(), y + direction.dy());
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
