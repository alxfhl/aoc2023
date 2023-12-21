package aoc2023;

import aoc2023.tools.Input;

import java.util.List;

public class Template {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            abc
            abc
            abc
            abc""");

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Template.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    public static long getPart1(List<String> lines) {
        return 0;
    }

    public static long getPart2(List<String> lines) {
        return 0;
    }
}
