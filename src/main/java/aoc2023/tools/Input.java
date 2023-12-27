package aoc2023.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Input {
    public static List<String> fromFile(String fileName) {
        try {
            return Files.readAllLines(Path.of("src/main/resources/aoc2023/" + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> fromString(String s) {
        return s.lines().toList();
    }

    public static List<String> forDay(Class<?> clazz) {
        return fromFile("input" + clazz.getSimpleName().substring(3));
    }
}
