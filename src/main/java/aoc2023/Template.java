package aoc2023;

import aoc2023.tools.Input;

import java.io.IOException;
import java.util.List;

public class Template {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            abc
            abc
            abc
            abc""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Template.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static Long getPart1(List<String> lines) {
        return null;
    }

    private static Long getPart2(List<String> lines) {
        return null;
    }
}
