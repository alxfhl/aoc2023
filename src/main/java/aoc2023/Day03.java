package aoc2023;

import aoc2023.tools.Input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day03 {
    public static final List<String> EXAMPLE1 = Input.fromString("""
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..""");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.fromFile("input03");
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    record Symbol(int x, int y, String symbol, List<PartNumber> partNumbers) {

    }

    record PartNumber(int x, int y, long value, int length, List<Symbol> symbols) {

    }

    private static long getPart1(List<String> lines) {
        List<Symbol> symbols = parseSymbols(lines);
        List<PartNumber> partNumbers = parsePartNumbers(lines, symbols);
        return partNumbers.stream()
                .filter(partNumber -> !partNumber.symbols.isEmpty())
                .mapToLong(partNumber -> partNumber.value)
                .sum();
    }

    private static long getPart2(List<String> lines) {
        List<Symbol> symbols = parseSymbols(lines);
        parsePartNumbers(lines, symbols);
        return symbols.stream()
                .filter(symbol -> symbol.symbol.equals("*") && symbol.partNumbers.size() == 2)
                .mapToLong(symbol -> symbol.partNumbers.get(0).value * symbol.partNumbers.get(1).value)
                .sum();
    }

    private static List<Symbol> parseSymbols(List<String> lines) {
        List<Symbol> symbols = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (!Character.isDigit(c) && '.' != c) {
                    symbols.add(new Symbol(x, y, String.valueOf(c), new ArrayList<>()));
                }
            }
        }
        return symbols;
    }

    private static List<PartNumber> parsePartNumbers(List<String> lines, List<Symbol> symbols) {
        List<PartNumber> partNumbers = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            int x = 0;
            for (String part : line.splitWithDelimiters("\\d+", -1)) {
                int end = x + part.length();
                if (part.matches("\\d+")) {
                    long value = Long.parseLong(part);
                    partNumbers.add(new PartNumber(x, y, value, part.length(), new ArrayList<>()));
                }
                x = end;
            }
        }
        for (PartNumber partNumber : partNumbers) {
            for (Symbol sym : symbols) {
                if (sym.y() >= partNumber.y() - 1 && sym.y() <= partNumber.y() + 1 && sym.x() >= partNumber.x() - 1 && sym.x() <= partNumber.x() + partNumber.length()) {
                    sym.partNumbers.add(partNumber);
                    partNumber.symbols.add(sym);
                }
            }
        }
        return partNumbers;
    }

}
