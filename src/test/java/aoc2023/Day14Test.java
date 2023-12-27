package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
            """);

    @Test
    public void part1() {
        assertThat(Day14.getPart1(EXAMPLE1)).isEqualTo(136);
        assertThat(Day14.getPart1(Input.fromFile("input14"))).isEqualTo(108840);
    }

    @Test
    public void part2() {
        assertThat(Day14.getPart2(EXAMPLE1)).isEqualTo(64);
        assertThat(Day14.getPart2(Input.fromFile("input14"))).isEqualTo(103445);
    }
}