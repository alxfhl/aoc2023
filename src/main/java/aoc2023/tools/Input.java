package aoc2023.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Input {
    public static List<String> fromFile(String fileName) {
        try {
            return Files.readAllLines(Path.of("src/main/resources/aoc2023/" + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> fromString(String s) {
        StringTokenizer stringTokenizer = new StringTokenizer(s, "\r?\n", false);
        final List<String> result = new ArrayList<>();
        while (stringTokenizer.hasMoreTokens()) {
            result.add(stringTokenizer.nextToken());
        }
        return result;
    }

    public static List<String> forDay(Class<?> clazz) {
        return fromFile("input" + clazz.getSimpleName().substring(3));
    }
}
