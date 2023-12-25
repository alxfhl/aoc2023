package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day08Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """);
    public static final List<String> EXAMPLE2 = Input.fromString("""
            LR
                        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """);

    @Test
    public void part1() {
        assertThat(Day08.getPart1(EXAMPLE1)).isEqualTo(2);
        assertThat(Day08.getPart1(Input.fromFile("input08"))).isEqualTo(21883);
    }

    @Test
    public void part2() {
        assertThat(Day08.getPart2(EXAMPLE2)).isEqualTo(6);
        assertThat(Day08.getPart2(Input.fromFile("input08"))).isEqualTo(12833235391111L);
    }
}