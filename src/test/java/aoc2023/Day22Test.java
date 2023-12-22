package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day22Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """);

    @Test
    public void part1() {
        assertThat(Day22.getPart1(EXAMPLE1)).isEqualTo(5);
        assertThat(Day22.getPart1(Input.fromFile("input22"))).isEqualTo(428);
    }

    @Test
    public void part2() {
        assertThat(Day22.getPart2(EXAMPLE1)).isEqualTo(7);
        assertThat(Day22.getPart2(Input.fromFile("input22"))).isEqualTo(35654);
    }
}