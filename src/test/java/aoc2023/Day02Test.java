package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day02Test {
    public static final List<String> EXAMPLE1 = Input.fromString("""
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green""");

    @Test
    public void part1() {
        assertThat(Day02.getPart1(EXAMPLE1)).isEqualTo(8);
        assertThat(Day02.getPart1(Input.fromFile("input02"))).isEqualTo(2268);
    }

    @Test
    public void part2() {
        assertThat(Day02.getPart2(EXAMPLE1)).isEqualTo(2286);
        assertThat(Day02.getPart2(Input.fromFile("input02"))).isEqualTo(63542);
    }
}