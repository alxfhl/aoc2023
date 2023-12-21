package aoc2023;

import aoc2023.tools.Input;
import aoc2023.tools.MathUtils;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

public class Day20 {

    public static final List<String> RELEVANT_CONJUNCTIONS = List.of("tx", "dd", "nz", "ph");

    public static final List<String> EXAMPLE1 = Input.fromString("""
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a""");

    @RequiredArgsConstructor
    static class Module {
        final String name;
        List<Module> destinations = new ArrayList<>();

        public String name() {
            return name;
        }

        @Override
        public String toString() {
            return name + " -> " + destinations.stream().map(Module::name).collect(joining(", "));
        }

        public List<Pulse> receive(Pulse pulse, long tick) {
            return List.of();
        }
    }

    static class FlipFlop extends Module {
        boolean on;

        public FlipFlop(String name) {
            super(name);
        }

        @Override
        public List<Pulse> receive(Pulse pulse, long tick) {
            List<Pulse> pulses = new ArrayList<>();
            if (!pulse.high()) {
                on = !on;
                for (Module destination : destinations) {
                    pulses.add(new Pulse(this, destination, on));
                }
            }
            return pulses;
        }

        @Override
        public String toString() {
            return "%" + super.toString();
        }
    }

    static class Conjunction extends Module {
        Map<String, Boolean> memory = new TreeMap<>();
        boolean relevant;
        List<Long> ticks = new LinkedList<>();

        public Conjunction(String name) {
            super(name);
            this.relevant = RELEVANT_CONJUNCTIONS.contains(name);
        }

        @Override
        public List<Pulse> receive(Pulse pulse, long tick) {
            List<Pulse> pulses = new ArrayList<>();
            memory.put(pulse.from().name(), pulse.high());
            boolean send = !memory.values().stream().allMatch(b -> b);
            if (relevant && send) {
                ticks.add(tick);
                if (ticks.size() > 10) {
                    ticks.removeFirst();
                }
            }
            for (Module destination : destinations) {
                pulses.add(new Pulse(this, destination, send));
            }
            return pulses;
        }

        @Override
        public String toString() {
            return "&" + super.toString();
        }
    }

    static class Broadcaster extends Module {

        public Broadcaster(String name) {
            super(name);
        }

        @Override
        public List<Pulse> receive(Pulse pulse, long tick) {
            List<Pulse> pulses = new ArrayList<>();
            for (Module destination : destinations) {
                pulses.add(new Pulse(this, destination, pulse.high()));
            }
            return pulses;
        }
    }

    static class Output extends Module {

        long lowPulses = 0;
        long highPulses = 0;

        public Output(String name) {
            super(name);
        }

        @Override
        public List<Pulse> receive(Pulse pulse, long tick) {
            if (pulse.high) {
                highPulses++;
            } else {
                lowPulses++;
            }
            return List.of();
        }
    }

    record Pulse(Module from, Module to, boolean high) {
        @Override
        public String toString() {
            return (from == null ? "button" : from.name) + (high ? " -high-> " : " -low-> ") + to.name;
        }
    }

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day20.class);
        for (var lines : List.of(EXAMPLE1, input)) {
            System.out.println("part 1: " + getPart1(lines));
        }
        System.out.println("part 2: " + getPart2(input));
    }

    private static long getPart1(List<String> lines) {
        Map<String, Module> modules = parseModules(lines);
        Module broadcaster = modules.get("broadcaster");
        List<Pulse> pulses = new LinkedList<>();
        long lowPulses = 0;
        long highPulses = 0;
        for (int i = 0; i < 1000; i++) {
            pulses.addLast(new Pulse(null, broadcaster, false));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.removeFirst();
                if (pulse.high) {
                    highPulses++;
                } else {
                    lowPulses++;
                }
                pulses.addAll(pulse.to.receive(pulse, i));
            }
        }
        return highPulses * lowPulses;
    }

    private static Map<String, Module> parseModules(List<String> lines) {
        Map<String, Module> modules = new HashMap<>();
        Pattern pattern = Pattern.compile("([&%]?)(\\w+) -> ((\\w+,? ?)+)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                System.out.println("failed to match " + line);
            }
            String name = matcher.group(2);
            Module module = switch (matcher.group(1)) {
                case "" -> new Broadcaster(name);
                case "%" -> new FlipFlop(name);
                case "&" -> new Conjunction(name);
                default -> throw new IllegalArgumentException(line);
            };
            modules.put(name, module);
        }
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                System.out.println("failed to match " + line);
            }
            List<String> destinations = Arrays.stream(matcher.group(3).split(",")).map(String::trim).filter(not(String::isEmpty)).toList();
            Module module = modules.get(matcher.group(2));
            for (String destination : destinations) {
                Module destinationModule = modules.get(destination);
                if (destinationModule == null) {
                    // System.out.println("Output: " + destination);
                    destinationModule = new Output(destination);
                    modules.put(destination, destinationModule);
                }
                module.destinations.add(destinationModule);
                if (destinationModule instanceof Conjunction conjunction) {
                    conjunction.memory.put(module.name(), false);
                }
            }
        }
        return modules;
    }

    private static long getPart2(List<String> lines) {
        Map<String, Module> modules = parseModules(lines);
        Module broadcaster = modules.get("broadcaster");
        List<Pulse> pulses = new LinkedList<>();
        int tick = 0;
        outer:
        while (true) {
            tick++;
            pulses.addLast(new Pulse(null, broadcaster, false));
            while (!pulses.isEmpty()) {
                Pulse pulse = pulses.removeFirst();
                pulses.addAll(pulse.to.receive(pulse, tick));
            }
            for (String relevantConjunction : RELEVANT_CONJUNCTIONS) {
                Conjunction con = (Conjunction) modules.get(relevantConjunction);
                if (con.ticks.size() < 10) {
                    continue outer;
                }
            }
            Map<String, Long> delta = new HashMap<>();
            Map<String, Long> offset = new HashMap<>();
            for (String relevantConjunction : RELEVANT_CONJUNCTIONS) {
                Conjunction con = (Conjunction) modules.get(relevantConjunction);
                Set<Long> deltas = new HashSet<>();
                for (int i = 0; i < 9; i++) {
                    deltas.add(con.ticks.get(i + 1) - con.ticks.get(i));
                }
                if (deltas.size() != 1) {
                    System.out.println("ERROR with " + con.name + " : " + deltas);
                }
                delta.put(con.name, deltas.stream().findFirst().orElseThrow());
                offset.put(con.name, con.ticks.getFirst() % delta.get(con.name));
            }
            // System.out.println(delta);
            // offsets are all 0 thankfully
            if (offset.values().stream().anyMatch(o -> o != 0)) {
                System.out.println("unexpected offsets: " + offset);
            }
            long result = 1;
            for (Long value : delta.values()) {
                result = MathUtils.lcm(result, value);
            }
            return result;
        }
    }

}
