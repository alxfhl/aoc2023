package aoc2023;

import aoc2023.tools.Coord2D;
import aoc2023.tools.Direction;
import aoc2023.tools.Input;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Disclaimer: This is the cleaned up version after introducing Coord2D and Direction classes and some refactoring.
 */
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

    record Tile(char ch, EnumSet<Direction> exits) {
        public Tile(char ch) {
            this(ch, EnumSet.noneOf(Direction.class));
        }

        public void addExit(Coord2D from, Direction direction, List<Todo> todos) {
            if (exits.add(direction)) {
                todos.add(new Todo(from.go(direction), direction));
            }
        }
    }

    record Todo(Coord2D position, Direction direction) {

    }

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day16.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        return evaluate(parse(lines), new Coord2D(0, 0), Direction.RIGHT);
    }

    private static long getPart2(List<String> lines) {
        List<List<Tile>> grid = parse(lines);
        int width = grid.getFirst().size();
        int height = grid.size();
        long max = 0;
        for (int x = 0; x < width; x++) {
            max = Math.max(max, evaluate(grid, new Coord2D(x, 0), Direction.DOWN));
            max = Math.max(max, evaluate(grid, new Coord2D(x, height - 1), Direction.UP));
        }
        for (int y = 0; y < height; y++) {
            max = Math.max(max, evaluate(grid, new Coord2D(0, y), Direction.RIGHT));
            max = Math.max(max, evaluate(grid, new Coord2D(width - 1, y), Direction.LEFT));
        }
        return max;
    }

    private static long evaluate(List<List<Tile>> grid, Coord2D initialPosition, Direction initialDirection) {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo(initialPosition, initialDirection));
        int width = grid.getFirst().size();
        int height = grid.size();
        while (!todos.isEmpty()) {
            Todo todo = todos.removeLast();
            Coord2D from = todo.position();
            Direction direction = todo.direction;
            if (!from.isInGrid(width, height)) {
                continue;
            }
            Tile tile = grid.get((int) from.y()).get((int) from.x());
            char ch = tile.ch;
            if (ch == '.' || ch == (direction.isHorizontal() ? '-' : '|')) {
                tile.addExit(from, direction, todos);
            } else if (ch == '|' || ch == '-') {
                tile.addExit(from, direction.turnLeft(), todos);
                tile.addExit(from, direction.turnRight(), todos);
            } else if (ch == '/' && direction.isHorizontal() || ch == '\\' && direction.isVertical()) {
                tile.addExit(from, direction.turnLeft(), todos);
            } else {
                tile.addExit(from, direction.turnRight(), todos);
            }
        }
        long count = grid.stream().flatMap(List::stream).filter(t -> !t.exits.isEmpty()).count();
        for (List<Tile> row : grid) {
            for (Tile tile : row) {
                tile.exits.clear();
            }
        }
        return count;
    }

    private static List<List<Tile>> parse(List<String> lines) {
        return lines.stream().map(line -> line.chars().mapToObj(ch -> new Tile((char) ch)).toList()).toList();
    }
}
