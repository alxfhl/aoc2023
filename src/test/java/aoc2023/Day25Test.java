package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day25Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
            """);

    @Test
    public void part1() {
        assertThat(Day25.getPart1(EXAMPLE1)).isEqualTo(54);
        assertThat(Day25.getPart1(Input.fromFile("input25"))).isEqualTo(568214);
    }

}