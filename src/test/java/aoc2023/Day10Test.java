package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day10Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
            """);
    public static final List<String> EXAMPLE2 = Input.fromString("""
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
            """);

    @Test
    public void part1() {
        assertThat(Day10.getPart1(EXAMPLE1)).isEqualTo(8);
        assertThat(Day10.getPart1(Input.fromFile("input10"))).isEqualTo(6823);
    }

    @Test
    public void part2() {
        assertThat(Day10.getPart2(EXAMPLE2)).isEqualTo(8);
        assertThat(Day10.getPart2(Input.fromFile("input10"))).isEqualTo(415);
    }
}