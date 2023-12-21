package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day03Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..""");

    @Test
    public void part1() {
        assertThat(Day03.getPart1(EXAMPLE1)).isEqualTo(4361);
        assertThat(Day03.getPart1(Input.fromFile("input03"))).isEqualTo(525119);

    }

    @Test
    public void part2() {
        assertThat(Day03.getPart2(EXAMPLE1)).isEqualTo(467835);
        assertThat(Day03.getPart2(Input.fromFile("input03"))).isEqualTo(76504829);
    }
}