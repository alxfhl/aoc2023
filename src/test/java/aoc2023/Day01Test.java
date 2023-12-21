package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day01Test {

    public static final List<String> EXAMPLE1 = List.of("1abc2", "pqr3stu8vwx", "a1b2c3d4e5f", "treb7uchet");

    public static final List<String> EXAMPLE2 = List.of("two1nine", "eightwothree", "abcone2threexyz", "xtwone3four",
            "4nineeightseven2", "zoneight234", "7pqrstsixteen");

    @Test
    public void part1() {
        assertThat(Day01.getPart1(EXAMPLE1)).isEqualTo(142);
        assertThat(Day01.getPart1(Input.fromFile("input01"))).isEqualTo(55477);

    }

    @Test
    public void part2() {
        assertThat(Day01.getPart2(EXAMPLE2)).isEqualTo(281);
        assertThat(Day01.getPart2(Input.fromFile("input01"))).isEqualTo(54431);
    }
}