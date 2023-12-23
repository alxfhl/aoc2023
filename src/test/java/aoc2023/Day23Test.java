package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day23Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
            """);

    @Test
    public void part1() {
        assertThat(Day23.getPart1(EXAMPLE1)).isEqualTo(94);
        assertThat(Day23.getPart1(Input.fromFile("input23"))).isEqualTo(2370);
    }

    @Test
    public void part2() {
        assertThat(Day23.getPart2(EXAMPLE1)).isEqualTo(154);
        assertThat(Day23.getPart2(Input.fromFile("input23"))).isEqualTo(6546);
    }
}