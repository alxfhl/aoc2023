package aoc2023;

import aoc2023.tools.CharMatrix;
import aoc2023.tools.Coord2D;
import aoc2023.tools.Direction;
import aoc2023.tools.Input;

import java.util.*;

/**
 * The 2nd part can actually be brute-forced just like part 1.
 * I implemented the solution using junctions retrospectively to speed things up.
 */
public class Day23 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day23.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    record Step(Coord2D pos, Direction direction) {
    }

    public static long getPart1(List<String> lines) {
        CharMatrix original = CharMatrix.valueOf(lines);
        List<Step> path = new ArrayList<>();
        path.add(new Step(new Coord2D(1, 1), Direction.DOWN));
        CharMatrix maze = new CharMatrix(original);
        maze.set(1, 0, 'S');
        maze.set(1, 1, 'O');
        return getLongestSolution(maze, path);
    }

    private static long getLongestSolution(CharMatrix maze, List<Step> path) {
        while (true) {
            Step last = path.getLast();
            Coord2D pos = last.pos();
            if (pos.y() == maze.getHeight() - 1) {
                return path.size();
            }
            List<Step> nextSteps = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                Coord2D next = pos.go(direction);
                if (maze.isInside(next)) {
                    char ch = maze.get(next);
                    if (ch == '.' || (ch == '>' && direction == Direction.RIGHT)
                            || (ch == '<' && direction == Direction.LEFT)
                            || (ch == 'v' && direction == Direction.DOWN)
                            || (ch == '^' && direction == Direction.UP)) {
                        nextSteps.add(new Step(next, direction));
                    }
                }
            }
            if (nextSteps.isEmpty()) {
                return 0;
            }
            if (nextSteps.size() == 1) {
                Step step = nextSteps.getFirst();
                maze.set(step.pos(), 'O');
                path.add(step);
            } else {
                return nextSteps.parallelStream().mapToLong(step -> {
                    CharMatrix mazeCopy = new CharMatrix(maze);
                    List<Step> pathCopy = new ArrayList<>(path);
                    mazeCopy.set(step.pos(), 'O');
                    pathCopy.add(step);
                    return getLongestSolution(mazeCopy, pathCopy);
                }).max().orElseThrow();
            }
        }
    }

    record Junction(Coord2D pos, Map<Direction, Junction> connections, Map<Direction, Integer> distances) {
        public Junction(Coord2D pos) {
            this(pos, new HashMap<>(), new HashMap<>());
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o != null && getClass() == o.getClass() && Objects.equals(pos, ((Junction) o).pos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos);
        }

        @Override
        public String toString() {
            return "Junction{pos=" + pos + ", distances=" + distances + '}';
        }
    }

    public static long getPart2(List<String> lines) {
        CharMatrix maze = CharMatrix.valueOf(lines);
        maze.replace('>', '.');
        maze.replace('<', '.');
        maze.replace('^', '.');
        maze.replace('v', '.');
        Junction start = new Junction(new Coord2D(1, 0));
        Junction exit = new Junction(new Coord2D(maze.getWidth() - 2, maze.getHeight() - 1));
        List<Junction> junctions = findJunctions(maze, start, exit);
        mapJunctions(junctions, maze);
        SequencedSet<Junction> visited = new LinkedHashSet<>();
        visited.add(start);
        return getLongestSolution(visited, 0, exit);
    }

    private static long getLongestSolution(SequencedSet<Junction> visited, long steps, Junction exit) {
        Junction last = visited.getLast();
        if (last == exit) {
            return steps;
        }
        long max = 0;
        for (Direction direction : last.connections().keySet()) {
            Junction junction = last.connections().get(direction);
            long distance = last.distances().get(direction);
            if (!visited.contains(junction)) {
                visited.add(junction);
                max = Math.max(max, getLongestSolution(visited, steps + distance, exit));
                visited.remove(junction);
            }
        }
        return max;
    }

    private static List<Junction> findJunctions(CharMatrix maze, Junction start, Junction exit) {
        List<Junction> junctions = new ArrayList<>();
        junctions.add(start);
        for (int y = 1; y < maze.getHeight() - 1; y++) {
            for (int x = 1; x < maze.getWidth() - 1; x++) {
                if (maze.get(x, y) != '.') {
                    continue;
                }
                int ways = 0;
                for (Direction dir : Direction.values()) {
                    if (maze.get(x + dir.dx(), y + dir.dy()) == '.') {
                        ways++;
                    }
                }
                if (ways > 2) {
                    junctions.add(new Junction(new Coord2D(x, y)));
                }
            }
        }
        junctions.add(exit);
        return junctions;
    }

    private static void mapJunctions(List<Junction> junctions, CharMatrix maze) {
        Map<Coord2D, Junction> junctionsByLocation = new HashMap<>();
        junctions.forEach(junction -> junctionsByLocation.put(junction.pos(), junction));
        for (Junction junction : junctions) {
            for (Direction direction : Direction.values()) {
                Coord2D neighbor = junction.pos().go(direction);
                if (maze.isInside(neighbor) && maze.get(neighbor) == '.') {
                    Coord2D pos = neighbor;
                    Direction dir = direction;
                    int distance = 1;
                    while (true) {
                        if (junctionsByLocation.get(pos) != null) {
                            junction.connections().put(direction, junctionsByLocation.get(pos));
                            junction.distances().put(direction, distance);
                            break;
                        }
                        Direction nextDir = null;
                        for (Direction d : Direction.values()) {
                            if (d != dir.turnAround()) {
                                if (maze.get(pos.go(d)) == '.') {
                                    nextDir = d;
                                    break;
                                }
                            }
                        }
                        if (nextDir == null) {
                            break;
                        }
                        dir = nextDir;
                        pos = pos.go(dir);
                        distance++;
                    }
                }
            }
        }
    }
}
