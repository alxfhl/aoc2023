package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.Parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

    private static final Pattern PATTERN = Pattern.compile("Card\\s+(\\d+):\\s*([^|]+)\\|([^|]+)");

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day04.class);
        System.out.println("part 1: " + getPart1(input));
        System.out.println("part 2: " + getPart2(input));
    }

    record ScratchCard(int number, List<Long> winningNumber, List<Long> yourNumbers, int matching) {
    }

    public static long getPart1(List<String> lines) {
        long result = 0;
        List<ScratchCard> cards = parseLines(lines);
        for (ScratchCard card : cards) {
            if (card.matching > 0) {
                result += Math.round(Math.pow(2, card.matching - 1));
            }
        }
        return result;
    }

    public static long getPart2(List<String> lines) {
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
