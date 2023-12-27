package aoc2023;

import aoc2023.tools.CharMatrix;
import aoc2023.tools.Coord2D;
import aoc2023.tools.Direction;
import aoc2023.tools.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Comparator.comparing;

public class Day18 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day18.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    record Dig(Direction direction, int length, String color) {

    }

    record Trench(int startX, int startY, int endX, int endY, Direction direction, int length) {

    }

    public static long getPart1(List<String> lines) {
        List<Dig> plan = parse(lines);
        int x = 0;
        int y = 0;
        int minX = x;
        int minY = y;
        int maxX = x;
        int maxY = y;
        for (Dig dig : plan) {
            x += dig.direction.dx() * dig.length;
            y += dig.direction.dy() * dig.length;
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
        System.out.println("x: " + minX + ":" + maxX + "   y: " + minY + ":" + maxY);
        int width = maxX - minX + 3;
        int height = maxY - minY + 3;
        CharMatrix matrix = new CharMatrix(width, height, '.');
        x = -minX + 1;
        y = -minY + 1;
        matrix.set(x, y, '#');
        for (Dig dig : plan) {
            for (int i = 0; i < dig.length; i++) {
                x += dig.direction.dx();
                y += dig.direction.dy();
                matrix.set(x, y, '#');
            }
        }
        floodfill(matrix);
        return matrix.count('#') + matrix.count('.');
    }

    private static void floodfill(CharMatrix matrix) {
        List<Coord2D> todo = new ArrayList<>();
        todo.add(new Coord2D(0, 0));
        char target = matrix.get(0, 0);
        while (!todo.isEmpty()) {
            Coord2D coord = todo.removeLast();
            if (matrix.get(coord) == target) {
                matrix.set(coord, 'O');
                for (Coord2D neighbor : coord.getNeighbors()) {
                    if (matrix.isInside(neighbor)) {
                        todo.add(neighbor);
                    }
                }
            }
        }
    }

    private static List<Dig> parse(List<String> lines) {
        List<Dig> plan = new ArrayList<>();
        Pattern regex = Pattern.compile("([ULRD]) (\\d+) \\((.*)\\)");
        for (String line : lines) {
            Matcher matcher = regex.matcher(line);
            if (!matcher.matches()) {
                System.out.println("invalid input: " + line);
            } else {
                Direction direction = switch (matcher.group(1)) {
                    case "L" -> Direction.LEFT;
                    case "R" -> Direction.RIGHT;
                    case "U" -> Direction.UP;
                    case "D" -> Direction.DOWN;
                    default -> throw new IllegalStateException();
                };
                plan.add(new Dig(direction, Integer.parseInt(matcher.group(2)), matcher.group(3)));
            }
        }
        return plan;
    }

    public static long getPart2(List<String> lines) {
        List<Dig> plan = parse(lines);
        plan = convert(plan);
        List<Trench> trenches = toTrenches(plan);
        int minY = trenches.stream().mapToInt(Trench::startY).min().orElseThrow();
        int maxY = trenches.stream().mapToInt(Trench::startY).max().orElseThrow();
        long sum = 0;
        int y = minY;
        while (y <= maxY) {
            int finalY = y;
            List<Trench> filtered = trenches.stream().filter(t -> t.startY <= finalY && t.endY >= finalY).toList();
            long rowSum = getRowSum(filtered, y);
            if (filtered.stream().noneMatch(trench -> trench.direction().isHorizontal())) {
                int sameY = trenches.stream()
                        .filter(t -> t.direction.isHorizontal() && t.endY > finalY)
                        .mapToInt(Trench::endY)
                        .min().orElseThrow()
                        - y;
                sum += rowSum * sameY;
                y += sameY;
            } else {
                sum += rowSum;
                y++;
            }
        }
        return sum;
    }

    private static int getRowSum(List<Trench> filtered, int y) {
        boolean inside = false;
        boolean onPipeFromSouth = false;
        boolean onPipeFromNorth = false;
        int rowSum = 0;
        int x = filtered.getFirst().startX();
        rowSum++;
        for (Trench trench : filtered) {
            if (inside || trench.direction.isHorizontal()) {
                rowSum += trench.endX - x;
            } else if (x < trench.endX) {
                rowSum += 1;
            }
            x = trench.endX;
            if (trench.startY < y) {
                onPipeFromNorth = !onPipeFromNorth;
            }
            if (trench.endY > y) {
                onPipeFromSouth = !onPipeFromSouth;
            }
            if (onPipeFromNorth && onPipeFromSouth) {
                onPipeFromSouth = false;
                onPipeFromNorth = false;
                inside = !inside;
            }
        }
        return rowSum;
    }

    private static List<Dig> convert(List<Dig> plan) {
        List<Dig> result = new ArrayList<>();
        for (Dig dig : plan) {
            int distance = Integer.parseInt(dig.color().substring(1, 6), 16);
            Direction direction = switch (dig.color().substring(6)) {
                case "0" -> Direction.RIGHT;
                case "1" -> Direction.DOWN;
                case "2" -> Direction.LEFT;
                case "3" -> Direction.UP;
                default -> throw new IllegalStateException();
            };
            result.add(new Dig(direction, distance, dig.color));
        }
        return result;
    }

    private static List<Trench> toTrenches(List<Dig> plan) {
        List<Trench> result = new ArrayList<>();
        int x = 0;
        int y = 0;
        for (Dig dig : plan) {
            int newX = x + dig.direction.dx() * dig.length;
            int newY = y + dig.direction.dy() * dig.length;
            result.add(new Trench(Math.min(x, newX), Math.min(y, newY), Math.max(x, newX), Math.max(y, newY), dig.direction, dig.length));
            x = newX;
            y = newY;
        }
        result.sort(comparing(Trench::startX).thenComparing(Trench::endX));
        return result;
    }
}
