package aoc2023;

import aoc2023.tools.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Day05Test {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            seeds: 79 14 55 13
                        
            seed-to-soil map:
            50 98 2
            52 50 48
                        
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                        
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                        
            water-to-light map:
            88 18 7
            18 25 70
                        
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                        
            temperature-to-humidity map:
            0 69 1
            1 0 69
                        
            humidity-to-location map:
            60 56 37
            56 93 4""");

    @Test
    public void part1() {
        assertThat(Day05.getPart1(EXAMPLE1)).isEqualTo(35);
        assertThat(Day05.getPart1(Input.fromFile("input05"))).isEqualTo(331445006);
    }

    @Test
    public void part2() {
        assertThat(Day05.getPart2(EXAMPLE1)).isEqualTo(46);
        assertThat(Day05.getPart2(Input.fromFile("input05"))).isEqualTo(6472060);
    }
}