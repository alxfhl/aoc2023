package aoc2023;

import aoc2023.tools.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Template {

    private static final Logger LOG = LoggerFactory.getLogger(Template.class);

    public static final List<String> EXAMPLE1 = Input.fromString("""
            abc
            abc
            abc
            abc""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.fromFile("input04");
        for (var lines : List.of(EXAMPLE1, input)) {
            try {
                System.out.println("part 1: " + getPart1(lines));
                System.out.println("part 2: " + getPart2(lines));
            } catch (RuntimeException e) {
                LOG.error("Unexpected error: ", e);
            }
        }
    }

    private static long getPart1(List<String> lines) {
        return -lines.size();
    }

    private static long getPart2(List<String> lines) {
        return -lines.size();
    }
}
