package aoc2023;

import aoc2023.tools.Coord2D;
import aoc2023.tools.Direction;
import aoc2023.tools.Input;

import java.util.*;

public class Day17 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day17.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    record State(Coord2D pos, Direction lastDirection, int loss) {

    }

    record Tile(int loss, Map<Boolean, Integer> minLoss) {

    }

    public static long getPart1(List<String> lines) {
        return solve(lines, 1, 3);
    }

    public static long getPart2(List<String> lines) {
        return solve(lines, 4, 10);
    }

    private static int solve(List<String> lines, int minDistance, int maxDistance) {
        List<List<Tile>> grid = new ArrayList<>();
        for (String line : lines) {
            grid.add(Arrays.stream(line.split("")).map(s -> new Tile(Integer.parseInt(s), new HashMap<>())).toList());
        }
        List<State> todos = new ArrayList<>();
        State start = new State(new Coord2D(0, 0), null, 0);
        addTodo2(grid, todos, start, Direction.RIGHT, minDistance, maxDistance);
        addTodo2(grid, todos, start, Direction.DOWN, minDistance, maxDistance);
        while (!todos.isEmpty()) {
            State state = todos.removeLast();
            switch (state.lastDirection) {
                case UP, DOWN -> {
                    addTodo2(grid, todos, state, Direction.LEFT, minDistance, maxDistance);
                    addTodo2(grid, todos, state, Direction.RIGHT, minDistance, maxDistance);
                }
                case LEFT, RIGHT -> {
                    addTodo2(grid, todos, state, Direction.UP, minDistance, maxDistance);
                    addTodo2(grid, todos, state, Direction.DOWN, minDistance, maxDistance);
                }
            }
            grid.getLast().getLast().minLoss().values().stream().mapToInt(l -> l).min()
                    .ifPresent(best -> todos.removeIf(todo -> todo.loss() > best));
        }
        return grid.getLast().getLast().minLoss().values().stream().mapToInt(l -> l).min().orElseThrow();
    }

    private static void addTodo2(List<List<Tile>> grid, List<State> todos, State start, Direction direction, int minDistance, int maxDistance) {
        Coord2D pos = start.pos();
        int loss = start.loss();
        for (int i = 1; i <= maxDistance; i++) {
            pos = pos.go(direction);
            if (!pos.isInGrid(grid.getFirst().size(), grid.size())) {
                return;
            }
            Tile tile = grid.get((int) pos.y()).get((int) pos.x());
            loss += tile.loss();
            if (i >= minDistance && (tile.minLoss.get(direction.isHorizontal()) == null
                    || tile.minLoss.get(direction.isHorizontal()) > loss)) {
                tile.minLoss.put(direction.isHorizontal(), loss);
                todos.add(new State(pos, direction, loss));
            }
        }
    }
}
