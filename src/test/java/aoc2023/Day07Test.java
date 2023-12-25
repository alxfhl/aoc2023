package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day07Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """);

    @Test
    public void part1() {
        assertThat(Day07.getPart1(EXAMPLE1)).isEqualTo(6440);
        assertThat(Day07.getPart1(Input.fromFile("input07"))).isEqualTo(253603890);
    }

    @Test
    public void part2() {
        assertThat(Day07.getPart2(EXAMPLE1)).isEqualTo(5905);
        assertThat(Day07.getPart2(Input.fromFile("input07"))).isEqualTo(253630098);
    }
}