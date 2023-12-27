package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day12Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """);

    @Test
    public void part1() {
        assertThat(Day12.getPart1(EXAMPLE1)).isEqualTo(21);
        assertThat(Day12.getPart1(Input.fromFile("input12"))).isEqualTo(7169);
    }

    @Test
    public void part2() {
        assertThat(Day12.getPart2(EXAMPLE1)).isEqualTo(525152);
        assertThat(Day12.getPart2(Input.fromFile("input12"))).isEqualTo(1738259948652L);
    }
}