package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day18Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
            """);

    @Test
    public void part1() {
        assertThat(Day18.getPart1(EXAMPLE1)).isEqualTo(62);
        assertThat(Day18.getPart1(Input.fromFile("input18"))).isEqualTo(49061);
    }

    @Test
    public void part2() {
        assertThat(Day18.getPart2(EXAMPLE1)).isEqualTo(952408144115L);
        assertThat(Day18.getPart2(Input.fromFile("input18"))).isEqualTo(92556825427032L);
    }
}