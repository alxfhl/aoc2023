package aoc2023;

import aoc2023.tools.Input;

import java.util.*;

import static java.util.function.Predicate.not;

/**
 * This algorithm is quite compute-intensive.
 * The key figure I use is the sum of all longest distances for all nodes of the graph. So for all nodes, the longest
 * distance to any other node is determined and those numbers summed up.
 * Then all possible edges are tested: The edge is temporarily removed and the above figure is updated. The edge for
 * which this number increases the most, is selected and permanently removed.
 * This is done twice to remove the first two edges.
 * Then all edges are tested whether their removal partitions the graph into to subgraphs to find the final edge.
 */
public class Day25 {

    public static void main(String[] args) {
        final List<String> input = Input.forDay(Day25.class);
        System.out.println("part 1: " + getPart1(input));
    }

    record Edge(String n1, String n2) {

    }

    public static long getPart1(List<String> lines) {
        Map<String, Set<String>> mesh = parse(lines);
        List<Edge> edges = getEdges(mesh);
        Edge result = getWeakestEdge(mesh, edges);
        System.out.println("First edge to remove: " + result); // cnr-hcd
        mesh.get(result.n1()).remove(result.n2());
        mesh.get(result.n2()).remove(result.n1());
        edges.remove(result);
        result = getWeakestEdge(mesh, edges);
        System.out.println("Second edge to remove: " + result); // fhv-zsp
        mesh.get(result.n1()).remove(result.n2());
        mesh.get(result.n2()).remove(result.n1());
        edges.remove(result);
        for (Edge edge : edges) {
            mesh.get(edge.n1()).remove(edge.n2());
            mesh.get(edge.n2()).remove(edge.n1());
            List<Set<String>> partitions = getPartitions(mesh);
            if (partitions.size() > 1) {
                System.out.println("Final edge to remove: " + edge); // bqp-fqr
                return (long) partitions.get(0).size() * partitions.get(1).size();
            }
            mesh.get(edge.n1()).add(edge.n2());
            mesh.get(edge.n2()).add(edge.n1());
        }
        List<Set<String>> partitions = getPartitions(mesh);
        return partitions.size() != 2 ? -1 : (long) partitions.get(0).size() * partitions.get(1).size();
    }

    private static Map<String, Set<String>> meshWithoutEdge(Map<String, Set<String>> mesh, Edge edge) {
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : mesh.entrySet()) {
            if (entry.getKey().equals(edge.n1()) || entry.getKey().equals(edge.n2())) {
                Set<String> newSet = new HashSet<>();
                for (String neighbor : entry.getValue()) {
                    if (!neighbor.equals(edge.n1()) && !neighbor.equals(edge.n2())) {
                        newSet.add(neighbor);
                    }
                }
                result.put(entry.getKey(), newSet);
            } else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private static Edge getWeakestEdge(Map<String, Set<String>> mesh, List<Edge> edges) {
        Map<String, Integer> longestDistances = getLongestDistances(mesh);
        int worst = longestDistances.values().stream().mapToInt(i1 -> i1).sum();
        Edge result = null;
        for (Edge edge : edges) {
            int newSum = getLongestDistances(mesh, longestDistances, edge).values().stream().mapToInt(i -> i).sum();
            if (newSum > worst) {
                worst = newSum;
                result = edge;
            }
        }
        return result;
    }

    private static Map<String, Integer> getLongestDistances(Map<String, Set<String>> mesh) {
        Map<String, Integer> result = new HashMap<>();
        for (String node : mesh.keySet()) {
            result.put(node, getLongestDistance(mesh, node));
        }
        return result;
    }

    private static Map<String, Integer> getLongestDistances(Map<String, Set<String>> mesh,
                                                            Map<String, Integer> oldValues, Edge edgeToRemove) {
        // copy the mesh before we change it
        mesh = meshWithoutEdge(mesh, edgeToRemove);

        // only recalculate required values
        Map<String, Integer> result = new HashMap<>(oldValues);
        List<String> toCheck = new ArrayList<>();
        toCheck.add(edgeToRemove.n1());
        toCheck.add(edgeToRemove.n2());
        Set<String> checked = new HashSet<>(toCheck);
        while (!toCheck.isEmpty()) {
            String node = toCheck.removeLast();
            int distance = getLongestDistance(mesh, node);
            if (distance > result.get(node)) {
                result.put(node, distance);
                for (String neighbor : mesh.get(node)) {
                    if (checked.add(neighbor)) {
                        toCheck.add(neighbor);
                    }
                }
            }
        }
        return result;
    }

    private static int getLongestDistance(Map<String, Set<String>> mesh, String node) {
        int distance = 0;
        Set<String> visited = new HashSet<>();
        visited.add(node);
        List<String> todo = new ArrayList<>(mesh.get(node));
        while (!todo.isEmpty()) {
            distance++;
            List<String> newTodo = new ArrayList<>();
            for (String node2 : todo) {
                if (visited.add(node2)) {
                    newTodo.addAll(mesh.get(node2));
                }
            }
            todo = newTodo;
        }
        return distance;
    }

    private static List<Edge> getEdges(Map<String, Set<String>> mesh) {
        List<Edge> edges = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : mesh.entrySet()) {
            String n1 = entry.getKey();
            for (String n2 : entry.getValue()) {
                if (n1.compareTo(n2) < 0) {
                    edges.add(new Edge(n1, n2));
                }
            }
        }
        return edges;
    }

    private static List<Set<String>> getPartitions(Map<String, Set<String>> mesh) {
        List<Set<String>> partitions = new ArrayList<>();
        Set<String> done = new HashSet<>();
        while (done.size() < mesh.size()) {
            Set<String> partition = new HashSet<>();
            String node = mesh.keySet().stream().filter(not(done::contains)).findFirst().orElseThrow();
            List<String> todo = new LinkedList<>();
            todo.addLast(node);
            while (!todo.isEmpty()) {
                String n = todo.removeLast();
                if (done.add(n)) {
                    partition.add(n);
                    todo.addAll(mesh.get(n));
                }
            }
            partitions.add(partition);
        }
        return partitions;
    }

    private static Map<String, Set<String>> parse(List<String> lines) {
        Map<String, Set<String>> mesh = new HashMap<>();
        for (String line : lines) {
            String node = line.substring(0, 3);
            for (String node2 : line.substring(5).split(" ")) {
                mesh.computeIfAbsent(node, n -> new HashSet<>()).add(node2);
                mesh.computeIfAbsent(node2, n -> new HashSet<>()).add(node);
            }
        }
        return mesh;
    }

}
