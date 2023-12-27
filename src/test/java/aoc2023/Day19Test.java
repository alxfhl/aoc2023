package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day19Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}
                        
            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
            """);

    @Test
    public void part1() {
        assertThat(Day19.getPart1(EXAMPLE1)).isEqualTo(19114);
        assertThat(Day19.getPart1(Input.fromFile("input19"))).isEqualTo(382440);
    }

    @Test
    public void part2() {
        assertThat(Day19.getPart2(EXAMPLE1)).isEqualTo(167409079868000L);
        assertThat(Day19.getPart2(Input.fromFile("input19"))).isEqualTo(136394217540123L);
    }
}