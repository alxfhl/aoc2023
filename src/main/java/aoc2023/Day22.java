package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

public class Day22 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day22.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    @AllArgsConstructor
    @Getter
    static class Brick {
        int x1, y1, z1;
        int x2, y2, z2;
        final List<Brick> supportedBy = new ArrayList<>();
        final List<Brick> supporting = new ArrayList<>();

        public boolean overlaps(Brick brick) {
            return x1 <= brick.x2 && brick.x1 <= x2 && y1 <= brick.y2 && brick.y1 <= y2;
        }
    }

    public static long getPart1(List<String> lines) {
        List<Brick> bricks = parse(lines);
        settle(bricks);
        analyze(bricks);
        return bricks.stream()
                .filter(brick -> brick.supporting.stream().allMatch(supp -> supp.supportedBy.size() > 1))
                .count();
    }

    public static long getPart2(List<String> lines) {
        List<Brick> bricks = parse(lines);
        settle(bricks);
        analyze(bricks);
        return bricks.stream().mapToLong(Day22::getChainReactionSize).sum();
    }

    private static long getChainReactionSize(Brick brick) {
        Set<Brick> falling = new HashSet<>();
        falling.add(brick);
        PriorityQueue<Brick> todo = new PriorityQueue<>(Comparator.comparing(Brick::getZ1));
        todo.add(brick);
        while (!todo.isEmpty()) {
            Brick next = todo.poll();
            for (Brick supported : next.supporting) {
                if (falling.containsAll(supported.supportedBy)) {
                    falling.add(supported);
                    todo.add(supported);
                }
            }
        }
        return falling.size() - 1;
    }

    private static List<Brick> parse(List<String> lines) {
        List<Brick> result = new ArrayList<>();
        for (String line : lines) {
            List<Integer> ints = Parse.getIntegers(line);
            result.add(new Brick(ints.get(0), ints.get(1), ints.get(2), ints.get(3), ints.get(4), ints.get(5)));
        }
        return result;
    }

    private static void settle(List<Brick> bricks) {
        bricks.sort(Comparator.comparing(Brick::getZ1));
        for (int a = 0; a < bricks.size(); a++) {
            Brick brick = bricks.get(a);
            int minZ = 1;
            for (int b = 0; b < a; b++) {
                Brick below = bricks.get(b);
                if (below.overlaps(brick)) {
                    minZ = Math.max(minZ, below.z2 + 1);
                }
            }
            if (minZ < brick.z1) {
                int height = brick.z2 - brick.z1;
                brick.z1 = minZ;
                brick.z2 = minZ + height;
            }
        }
    }

    private static void analyze(List<Brick> bricks) {
        bricks.sort(Comparator.comparing(Brick::getZ1));
        for (int a = 0; a < bricks.size(); a++) {
            Brick brick = bricks.get(a);
            for (int b = 0; b < a; b++) {
                Brick below = bricks.get(b);
                if (below.z2 + 1 == brick.z1 && below.overlaps(brick)) {
                    below.supporting.add(brick);
                    brick.supportedBy.add(below);
                }
            }
        }
    }
}
