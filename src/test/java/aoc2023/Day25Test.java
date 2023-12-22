package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day25Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            """);

    @Test
    public void part1() {
        assertThat(Day25.getPart1(EXAMPLE1)).isEqualTo(0);
        // assertThat(Day25.getPart1(Input.fromFile("input25"))).isEqualTo(0);
    }

    @Test
    public void part2() {
        // assertThat(Day25.getPart2(EXAMPLE1)).isEqualTo(0);
        // assertThat(Day25.getPart2(Input.fromFile("input25"))).isEqualTo(0);
    }
}