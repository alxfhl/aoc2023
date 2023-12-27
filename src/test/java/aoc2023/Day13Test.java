package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day13Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
                        
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
            """);

    @Test
    public void part1() {
        assertThat(Day13.getPart1(EXAMPLE1)).isEqualTo(405);
        assertThat(Day13.getPart1(Input.fromFile("input13"))).isEqualTo(26957);
    }

    @Test
    public void part2() {
        assertThat(Day13.getPart2(EXAMPLE1)).isEqualTo(400);
        assertThat(Day13.getPart2(Input.fromFile("input13"))).isEqualTo(42695);
    }
}