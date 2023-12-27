package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day17Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
            """);

    @Test
    public void part1() {
        assertThat(Day17.getPart1(EXAMPLE1)).isEqualTo(102);
        assertThat(Day17.getPart1(Input.fromFile("input17"))).isEqualTo(694);
    }

    @Test
    public void part2() {
        assertThat(Day17.getPart2(EXAMPLE1)).isEqualTo(94);
        assertThat(Day17.getPart2(Input.fromFile("input17"))).isEqualTo(829);
    }
}