package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day15Test {

    public static final String EXAMPLE1 = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

    @Test
    public void testHash() {
        assertThat(Day15.hash("HASH")).isEqualTo(52);
    }

    @Test
    public void part1() {
        assertThat(Day15.getPart1(EXAMPLE1)).isEqualTo(1320);
        assertThat(Day15.getPart1(Input.fromFile("input15").getFirst())).isEqualTo(513643);
    }

    @Test
    public void part2() {
        assertThat(Day15.getPart2(EXAMPLE1)).isEqualTo(145);
        assertThat(Day15.getPart2(Input.fromFile("input15").getFirst())).isEqualTo(265345);
    }
}