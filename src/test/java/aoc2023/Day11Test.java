package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day11Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """);

    @Test
    public void part1() {
        assertThat(Day11.getPart1(EXAMPLE1)).isEqualTo(374);
        assertThat(Day11.getPart1(Input.fromFile("input11"))).isEqualTo(10292708);
    }

    @Test
    public void part2() {
        assertThat(Day11.getPart2(EXAMPLE1, 10)).isEqualTo(1030);
        assertThat(Day11.getPart2(EXAMPLE1, 100)).isEqualTo(8410);
        assertThat(Day11.getPart2(Input.fromFile("input11"), 1000000)).isEqualTo(790194712336L);
    }
}