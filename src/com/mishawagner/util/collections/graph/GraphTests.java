package com.mishawagner.util.collections.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class GraphTests {
    private static final BiFunction<Point, Point, Double> distanceFunction =
            (Point p1, Point p2) -> Math.sqrt(
                    Math.pow(p2.x - p1.x, 2) +
                    Math.pow(p2.y - p1.y, 2));

    private static final class Point {
        final int x;
        final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== POINTERS ===");
        testGraph(new PointerGraph<>());
        System.out.println("\n=== MATRICES ===");
        testGraph(new MatrixGraph<>());
        System.out.println("\n=== ADJACENCY LIST ===");
        testGraph(new AdjacencyListGraph<>());
    }

    private static void testGraph(AbstractGraph<Point> graph) {
        // Create items
        List<Point> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                items.add(new Point(i, j));
            }
        }

        // Log start time for adding elements to the list
        long addStart = System.currentTimeMillis();

        // Add them to the graph
        graph.addAll(items);

        // Add random links
        Random rand = new Random(1234);
        for (int i = 1; i < items.size() * 5; i++) {
            Point p1 = items.get(rand.nextInt(items.size()));
            Point p2 = items.get(rand.nextInt(items.size()));
            graph.addLink(p1, p2, distanceFunction.apply(p1, p2));
        }

        // Print stats
        System.out.println("Time to create: " + (System.currentTimeMillis() - addStart));
        System.out.print("BFS: ");
        graph.bfs(items.get(0), items.get(items.size() - 1));
        System.out.print("DFS: ");
        graph.dfs(items.get(0), items.get(items.size() - 1));
        System.out.print("Dijstra: ");
        graph.dijstra(items.get(0), items.get(items.size() - 1));
        System.out.print("A*: ");
        System.out.println(graph.aStar(items.get(0), items.get(items.size() - 1), distanceFunction));
    }
}
