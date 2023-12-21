package aoc2023;

import aoc2023.tools.Input;

import java.util.ArrayList;
import java.util.List;

public class Day03 {
    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day03.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    record Symbol(int x, int y, String symbol, List<PartNumber> partNumbers) {

    }

    record PartNumber(int x, int y, long value, int length, List<Symbol> symbols) {

    }

    public static long getPart1(List<String> lines) {
        List<Symbol> symbols = parseSymbols(lines);
        List<PartNumber> partNumbers = parsePartNumbers(lines, symbols);
        return partNumbers.stream()
                .filter(partNumber -> !partNumber.symbols.isEmpty())
                .mapToLong(partNumber -> partNumber.value)
                .sum();
    }

    public static long getPart2(List<String> lines) {
        List<Symbol> symbols = parseSymbols(lines);
        parsePartNumbers(lines, symbols);
        return symbols.stream()
                .filter(symbol -> symbol.symbol.equals("*") && symbol.partNumbers.size() == 2)
                .mapToLong(symbol -> symbol.partNumbers.getFirst().value * symbol.partNumbers.get(1).value)
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
        List<PartNumber> partNumbers = getPartNumbers(lines);
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

    private static List<PartNumber> getPartNumbers(List<String> lines) {
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
        return partNumbers;
    }

}
