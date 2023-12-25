package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day09Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """);

    @Test
    public void part1() {
        assertThat(Day09.getPart1(EXAMPLE1)).isEqualTo(114);
        assertThat(Day09.getPart1(Input.fromFile("input09"))).isEqualTo(1939607039);
    }

    @Test
    public void part2() {
        assertThat(Day09.getPart2(EXAMPLE1)).isEqualTo(2);
        assertThat(Day09.getPart2(Input.fromFile("input09"))).isEqualTo(1041);
    }
}