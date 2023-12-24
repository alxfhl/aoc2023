package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day24Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
            """);

    @Test
    public void part1() {
        assertThat(Day24.getPart1(EXAMPLE1, new BigDecimal(7), new BigDecimal(27))).isEqualTo(2);
        assertThat(Day24.getPart1(Input.fromFile("input24"), new BigDecimal("200000000000000"), new BigDecimal("400000000000000"))).isEqualTo(20361);
    }

    @Test
    public void part2() {
        assertThat(Day24.getPart2(EXAMPLE1)).isEqualTo(47);
        // assertThat(Day24.getPart2(Input.fromFile("input24"))).isEqualTo(0);
    }
}