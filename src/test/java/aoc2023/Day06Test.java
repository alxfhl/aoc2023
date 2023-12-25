package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day06Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            Time:      7  15   30
            Distance:  9  40  200
            """);

    @Test
    public void part1() {
        assertThat(Day06.getPart1(EXAMPLE1)).isEqualTo(288);
        assertThat(Day06.getPart1(Input.fromFile("input06"))).isEqualTo(227850);
    }

    @Test
    public void part2() {
        assertThat(Day06.getPart2(EXAMPLE1)).isEqualTo(71503);
        assertThat(Day06.getPart2(Input.fromFile("input06"))).isEqualTo(42948149);
    }
}