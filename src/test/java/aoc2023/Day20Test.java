package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day20Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
            """);
    public static final List<String> EXAMPLE2 = Input.fromString("""
            broadcaster -> a
            %a -> inv, con
            &inv -> b
            %b -> con
            &con -> output
            """);

    @Test
    public void part1() {
        assertThat(Day20.getPart1(EXAMPLE1)).isEqualTo(32000000);
        assertThat(Day20.getPart1(EXAMPLE2)).isEqualTo(11687500);
        assertThat(Day20.getPart1(Input.fromFile("input20"))).isEqualTo(869395600);
    }

    @Test
    public void part2() {
        assertThat(Day20.getPart2(Input.fromFile("input20"))).isEqualTo(232605773145467L);
    }
}