package aoc2023;

import aoc2023.tools.Input;

import java.util.*;

import static java.util.Comparator.comparing;

public class Day07 {

    private static final List<String> ORDER1 = List.of("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A");
    private static final List<String> ORDER2 = List.of("J", "2", "3", "4", "5", "6", "7", "8", "9", "T", "Q", "K", "A");
    public static final List<String> EXAMPLE1 = Input.fromString("""
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483""");

    private enum Kind {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE, FULL_HOUSE, FOUR, FIVE;

        static Kind of(List<String> cards) {
            Map<String, Integer> counts = getCounts(cards);
            return getKind(counts.size(), getMaxCount(counts));
        }

        private static Integer getMaxCount(Map<String, Integer> counts) {
            return counts.values().stream().max(Comparator.naturalOrder()).orElseThrow();
        }

        private static Kind getKind(int size, int maxCount) {
            return switch (maxCount) {
                case 5 -> FIVE;
                case 4 -> FOUR;
                case 3 -> size == 3 ? THREE : FULL_HOUSE;
                case 2 -> size == 3 ? TWO_PAIR : ONE_PAIR;
                default -> HIGH_CARD;
            };
        }

        static Kind of2(List<String> cards) {
            Map<String, Integer> counts = getCounts(cards);
            int maxCount = getMaxCount(counts);
            if (maxCount == 5) {
                return FIVE;
            }
            if (counts.containsKey("J")) {
                int jokers = counts.remove("J");
                maxCount = getMaxCount(counts);
                for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                    if (entry.getValue() == maxCount) {
                        counts.put(entry.getKey(), entry.getValue() + jokers);
                        break;
                    }
                }
            }
            return getKind(counts.size(), getMaxCount(counts));
        }

        private static Map<String, Integer> getCounts(List<String> cards) {
            Map<String, Integer> counts = new HashMap<>();
            for (String card : cards) {
                if (counts.containsKey(card)) {
                    counts.put(card, counts.get(card) + 1);
                } else {
                    counts.put(card, 1);
                }
            }
            return counts;
        }
    }

    record Hand(List<String> cards, long bid, Kind kind) {

    }

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day07.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
            System.out.println("part 2: " + getPart2(lines));
        }
    }

    private static long getPart1(List<String> lines) {
        List<Hand> hands = new ArrayList<>();
        for (String line : lines) {
            List<String> cards = getCardsFromLine(line);
            hands.add(new Hand(cards, Long.parseLong(line.substring(6)), Kind.of(cards)));
        }
        hands.sort(comparing(Hand::kind)
                .thenComparing(hand -> ORDER1.indexOf(hand.cards.getFirst()))
                .thenComparing(hand -> ORDER1.indexOf(hand.cards.get(1)))
                .thenComparing(hand -> ORDER1.indexOf(hand.cards.get(2)))
                .thenComparing(hand -> ORDER1.indexOf(hand.cards.get(3)))
                .thenComparing(hand -> ORDER1.indexOf(hand.cards.getLast())));
        return evaluateHands(hands);
    }

    private static List<String> getCardsFromLine(String line) {
        List<String> cards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cards.add(line.substring(i, i + 1));
        }
        return cards;
    }

    private static long evaluateHands(List<Hand> hands) {
        long sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * hands.get(i).bid();
        }
        return sum;
    }

    private static long getPart2(List<String> lines) {
        List<Hand> hands = new ArrayList<>();
        for (String line : lines) {
            List<String> cards = getCardsFromLine(line);
            hands.add(new Hand(cards, Long.parseLong(line.substring(6)), Kind.of2(cards)));
        }
        hands.sort(comparing(Hand::kind)
                .thenComparing(hand -> ORDER2.indexOf(hand.cards.getFirst()))
                .thenComparing(hand -> ORDER2.indexOf(hand.cards.get(1)))
                .thenComparing(hand -> ORDER2.indexOf(hand.cards.get(2)))
                .thenComparing(hand -> ORDER2.indexOf(hand.cards.get(3)))
                .thenComparing(hand -> ORDER2.indexOf(hand.cards.getLast())));
        return evaluateHands(hands);
    }
}