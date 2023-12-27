package aoc2023;

import aoc2023.tools.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15 {

    public static void main(String[] args) {
        String input = Input.forDay(Day15.class).getFirst();
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    public static long getPart1(String line) {
        return Arrays.stream(line.split(",")).mapToInt(Day15::hash).sum();
    }

    public static int hash(String line) {
        int currentValue = 0;
        for (char ch : line.toCharArray()) {
            currentValue = (currentValue + ch) * 17 % 256;
        }
        return currentValue;
    }

    record Lens(String label, int focus) {

    }

    public static long getPart2(String line) {
        List<String> commands = List.of(line.split(","));
        List<List<Lens>> boxes = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            boxes.add(new ArrayList<>());
        }
        for (String command : commands) {
            char op = command.contains("-") ? '-' : '=';
            String label = command.substring(0, command.indexOf(op));
            List<Lens> box = boxes.get(hash(label));
            if (op == '-') {
                box.removeIf(l -> l.label.equals(label));
            } else {
                Lens lens = new Lens(label, Integer.parseInt(command.substring(command.indexOf(op) + 1)));
                boolean found = false;
                for (int i = 0; i < box.size(); i++) {
                    if (box.get(i).label.equals(label)) {
                        found = true;
                        box.set(i, lens);
                    }
                }
                if (!found) {
                    box.add(lens);
                }
            }
        }
        return focusingPower(boxes);
    }

    private static long focusingPower(List<List<Lens>> boxes) {
        long sum = 0;
        int boxNumber = 1;
        for (List<Lens> box : boxes) {
            int slot = 1;
            for (Lens lens : box) {
                sum += (long) boxNumber * slot * lens.focus;
                slot++;
            }
            boxNumber++;
        }
        return sum;
    }
}
