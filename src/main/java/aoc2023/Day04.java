package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

    public static final List<String> EXAMPLE1 = Input.fromString("""
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11""");

    private static final Pattern PATTERN = Pattern.compile("Card\\s+(\\d+):\\s*([^|]+)\\|([^|]+)");

    public static void main(String[] args) throws IOException {
        final List<String> input = Input.forDay(Day04.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    record ScratchCard(int number, List<Long> winningNumber, List<Long> yourNumbers, int matching) {
    }

    private static long getPart1(List<String> lines) {
        long result = 0;
        List<ScratchCard> cards = parseLines(lines);
        for (ScratchCard card : cards) {
            if (card.matching > 0) {
                result += Math.round(Math.pow(2, card.matching - 1));
            }
        }
        return result;
    }

    private static long getPart2(List<String> lines) {
        List<ScratchCard> cards = parseLines(lines);
        ArrayList<Integer> copies = new ArrayList<>(cards.stream().map(c -> 1).toList());
        for (int i = 0; i < cards.size(); i++) {
            ScratchCard card = cards.get(i);
            for (int j = i + 1; j < cards.size() && j < i + card.matching + 1; j++) {
                copies.set(j, copies.get(j) + copies.get(i));
            }
        }
        return copies.stream().mapToInt(i -> i).sum();
    }

    private static List<ScratchCard> parseLines(List<String> lines) {
        List<ScratchCard> cards = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.matches()) {
                continue;
            }
            int cardNumber = Integer.parseInt(matcher.group(1));
            List<Long> winning = Parse.getLongs(matcher.group(2));
            List<Long> your = Parse.getLongs(matcher.group(3));
            int matching = (int) your.stream().filter(winning::contains).count();
            cards.add(new ScratchCard(cardNumber, winning, your, matching));
        }
        return cards;
    }

}
