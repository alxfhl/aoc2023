package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day12 {

    public static final List<String> EXAMPLE1 = List.of(
            "???.### 1,1,3",
            ".??..??...?##. 1,1,3",
            "?#?#?#?#?#?#?#? 1,3,1,6",
            "????.#...#... 4,1,1",
            "????.######..#####. 1,6,5",
            "?###???????? 3,2,1");

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day12.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    enum Symbol {
        UNKNOWN, OPERATIONAL, DAMAGED
    }

    record Line(List<Symbol> symbols, List<Integer> runs) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Symbol symbol : symbols) {
                sb.append(symbol == Symbol.UNKNOWN ? "?" : symbol == Symbol.OPERATIONAL ? "." : "#");
            }
            return sb.append(" ").append(runs).toString();
        }
    }

    private static long getPart1(List<String> lines) {
        List<Line> input = parse(lines);
        long sum = 0;
        for (Line line : input) {
            long possibilities = possibilities(line.symbols, line.runs);
            sum += possibilities;
        }
        return sum;
    }

    private static long getPart2(List<String> lines) {
        List<Line> input = unfold(parse(lines));
        long sum = 0;
        for (Line line : input) {
            long possibilities = possibilities(line.symbols, line.runs);
            sum += possibilities;
        }
        return sum;
    }

    private static List<Line> parse(List<String> lines) {
        List<Line> parsed = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            List<Symbol> symbols = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (ch == '?') {
                    symbols.add(Symbol.UNKNOWN);
                } else if (ch == '.') {
                    if (i == 0 || symbols.getLast() != Symbol.OPERATIONAL) {
                        symbols.add(Symbol.OPERATIONAL);
                    }
                } else if (ch == '#') {
                    symbols.add(Symbol.DAMAGED);
                } else if (ch == ' ') {
                    break;
                }
            }
            parsed.add(new Line(symbols, Parse.getIntegers(line)));
        }
        return parsed;
    }

    private static List<Line> unfold(List<Line> parse) {
        List<Line> result = new ArrayList<>();
        for (Line line : parse) {
            List<Symbol> symbols = new ArrayList<>();
            List<Integer> runs = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                if (i > 0) {
                    symbols.add(Symbol.UNKNOWN);
                }
                symbols.addAll(line.symbols);
                runs.addAll(line.runs);
            }
            result.add(new Line(symbols, runs));
        }
        return result;
    }

    private static long possibilities(List<Symbol> symbols, List<Integer> runs) {
        if (runs.isEmpty()) {
            for (Symbol symbol : symbols) {
                if (symbol == Symbol.DAMAGED) {
                    return 0;
                }
            }
            return 1;
        }
        if (symbols.isEmpty()) {
            return 0;
        }
        symbols = trim(symbols);

        if (runs.size() <= 3) {
            return bruteForce(symbols, runs);
        }
        int middle = runs.size() / 2;
        int middleRun = runs.get(middle);
        List<Integer> leftRuns = runs.subList(0, middle);
        List<Integer> rightRuns = runs.subList(middle + 1, runs.size());
        int minLengthLeft = getMinLength(symbols, leftRuns);
        int minLengthRight = getMinLength(symbols.reversed(), rightRuns.reversed());
        int length = symbols.size();
        long sum = 0;
        for (int position : getPossiblePositions(symbols, middleRun)) {
            if (position > minLengthLeft && position < length - minLengthRight - 1) {
                long leftPossibilities = possibilities(symbols.subList(0, position - 1), leftRuns);
                long rightPossibilities = possibilities(symbols.subList(position + middleRun + 1, length), rightRuns);
                sum += leftPossibilities * rightPossibilities;
            }
        }
        return sum;
    }

    private static List<Symbol> trim(List<Symbol> symbols) {
        int start = 0;
        int end = symbols.size();
        while (symbols.get(start) == Symbol.OPERATIONAL) {
            start++;
        }
        while (start < end && symbols.get(end - 1) == Symbol.OPERATIONAL) {
            end--;
        }
        if (start > 0 || end < symbols.size()) {
            return new ArrayList<>(symbols.subList(start, end));
        }
        return symbols;
    }

    private static List<Integer> getPossiblePositions(List<Symbol> symbols, int middleRun) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i < symbols.size() - middleRun; i++) {
            if (symbols.get(i - 1) != Symbol.DAMAGED) {
                if (symbols.get(i + middleRun) != Symbol.DAMAGED) {
                    if (IntStream.range(i, i + middleRun).mapToObj(symbols::get).allMatch(symbol -> symbol != Symbol.OPERATIONAL)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    private static long bruteForce(List<Symbol> symbols, List<Integer> runs) {
        if (runs.isEmpty()) {
            for (Symbol symbol : symbols) {
                if (symbol == Symbol.DAMAGED) {
                    return 0;
                }
            }
            return 1;
        }
        int next = runs.getFirst();
        long minRest = getMinLength(symbols.reversed(), runs.subList(1, runs.size()).reversed());
        long sum = 0;
        for (int start = 0; start <= symbols.size() - minRest - next; start++) {
            if (start > 0) {
                if (symbols.get(start - 1) == Symbol.DAMAGED) {
                    break;
                }
            }

            boolean possible = IntStream.range(start, start + next).mapToObj(symbols::get).allMatch(symbol -> symbol != Symbol.OPERATIONAL)
                    && (start + next == symbols.size() || symbols.get(start + next) != Symbol.DAMAGED);
            if (possible) {
                if (start + next == symbols.size()) {
                    sum += runs.size() == 1 ? 1 : 0;
                } else {
                    sum += bruteForce(symbols.subList(start + next + 1, symbols.size()), runs.subList(1, runs.size()));
                }
            }
        }
        return sum;
    }

    private static int getMinLength(List<Symbol> symbols, List<Integer> runs) {
        if (runs.isEmpty()) {
            return 0;
        }
        try {
            int pos = 0;
            int runIndex = 0;
            int run = runs.getFirst();
            while (true) {
                if (symbols.get(pos) == Symbol.OPERATIONAL) {
                    pos++;
                    continue;
                }
                if (IntStream.range(pos, pos + run).mapToObj(symbols::get).allMatch(symbol -> symbol != Symbol.OPERATIONAL)
                && symbols.get(pos + run) != Symbol.DAMAGED) {
                    // fits at pos
                    pos += run;
                    runIndex++;
                    if (runIndex == runs.size()) {
                        return pos;
                    }
                    pos++;
                    run = runs.get(runIndex);
                } else {
                    pos++;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return 1000000;
        }
    }
}
