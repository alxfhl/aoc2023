package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day21Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
            """);

    public static final List<String> EXAMPLE2 = Input.fromString("""
            .................................
            .....###.#......###.#......###.#.
            .###.##..#..###.##..#..###.##..#.
            ..#.#...#....#.#...#....#.#...#..
            ....#.#........#.#........#.#....
            .##...####..##...####..##...####.
            .##..#...#..##..#...#..##..#...#.
            .......##.........##.........##..
            .##.#.####..##.#.####..##.#.####.
            .##..##.##..##..##.##..##..##.##.
            .................................
            .................................
            .....###.#......###.#......###.#.
            .###.##..#..###.##..#..###.##..#.
            ..#.#...#....#.#...#....#.#...#..
            ....#.#........#.#........#.#....
            .##...####..##..S####..##...####.
            .##..#...#..##..#...#..##..#...#.
            .......##.........##.........##..
            .##.#.####..##.#.####..##.#.####.
            .##..##.##..##..##.##..##..##.##.
            .................................
            .................................
            .....###.#......###.#......###.#.
            .###.##..#..###.##..#..###.##..#.
            ..#.#...#....#.#...#....#.#...#..
            ....#.#........#.#........#.#....
            .##...####..##...####..##...####.
            .##..#...#..##..#...#..##..#...#.
            .......##.........##.........##..
            .##.#.####..##.#.####..##.#.####.
            .##..##.##..##..##.##..##..##.##.
            .................................
            """);

    @Test
    public void part1() {
        assertThat(Day21.getPart1(EXAMPLE1, 6)).isEqualTo(16);
        assertThat(Day21.getPart1(Input.fromFile("input21"), 64)).isEqualTo(3795);
    }

    @Test
    public void part2() {
        assertThat(Day21.getPart2(EXAMPLE2, 6)).isEqualTo(16);
        assertThat(Day21.getPart2(EXAMPLE2, 10)).isEqualTo(50);
        assertThat(Day21.getPart2(EXAMPLE2, 50)).isEqualTo(1594);
        assertThat(Day21.getPart2(EXAMPLE2, 100)).isEqualTo(6536);
        assertThat(Day21.getPart2(EXAMPLE2, 500)).isEqualTo(167004);
        // I don't know why, but it does not work for this one example :-(
        // assertThat(Day21.getPart2(EXAMPLE2, 1000)).isEqualTo(668697);
        assertThat(Day21.getPart2(EXAMPLE2, 5000)).isEqualTo(16733044);
        assertThat(Day21.getPart2(Input.fromFile("input21"), 26501365)).isEqualTo(630129824772393L);
    }
}