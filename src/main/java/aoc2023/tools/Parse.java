package aoc2023.tools;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Parse {
    public static List<Long> getLongs(String s) {
        List<Long> result = new ArrayList<>();
        for (String part : s.split("[^0-9]+")) {
            if (!part.isBlank()) {
                result.add(Long.valueOf(part));
            }
        }
        return result;
    }

    public static List<Integer> getIntegers(String s) {
        List<Integer> result = new ArrayList<>();
        for (String part : s.split("[^0-9]+")) {
            if (!part.isBlank()) {
                result.add(Integer.valueOf(part));
            }
        }
        return result;
    }
}
