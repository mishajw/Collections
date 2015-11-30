package com.mishawagner.util.collections.graph;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiFunction;

public abstract class AbstractGraph<T> {
    class Link<U> {
        U start;
        U end;
        double weight;

        public Link(U start, U end, double weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return String.format("%s -> %s (%f)", start, end, weight);
        }
    }

    public abstract void add(T t);

    public abstract List<T> getAllNodes();

    public abstract void addLink(T start, T end, double weight);

    public abstract List<Link<T>> getLinks(T t);

    public void addAll(List<T> t) {
        t.forEach(this::add);
    }

    public List<T> bfs(T start, T end) {
        return search(start, end, new FronteerAbstraction<T>() {
            Queue<T> list = new LinkedBlockingQueue<>();

            @Override
            public boolean isEmpty() {
                return list.isEmpty();
            }

            @Override
            public T get() {
                return list.poll();
            }

            @Override
            public void add(T t) {
                list.add(t);
            }
        });
    }

    public List<T> dfs(T start, T end) {
        return search(start, end, new FronteerAbstraction<T>() {
            Stack<T> list = new Stack<>();

            @Override
            public boolean isEmpty() {
                return list.isEmpty();
            }

            @Override
            public T get() {
                return list.pop();
            }

            @Override
            public void add(T t) {
                list.add(t);
            }
        });
    }

    public List<T> dijstra(T start, T end) {
        return search(start, end, new FronteerAbstraction<T>() {
            Map<T, Double> costs;
            PriorityQueue<T> list = new PriorityQueue<>(getComparator());

            @Override
            protected double getWeight(T t) {
                return costs.get(t);
            }

            @Override
            public boolean isEmpty() {
                return list.isEmpty();
            }

            @Override
            public T get() {
                return list.poll();
            }

            @Override
            public void add(T t) {
                list.add(t);
            }

            @Override
            public void registerCosts(Map<T, Double> costs) {
                this.costs = costs;
            }
        });
    }

    public List<T> aStar(T start, T end, BiFunction<T, T, Double> heuristic) {
        return search(start, end, new FronteerAbstraction<T>() {
            Map<T, Double> costs;
            T end;

            PriorityQueue<T> list = new PriorityQueue<>(getComparator());

            @Override
            protected double getWeight(T t) {
                return costs.get(t) + heuristic.apply(t, end);
            }

            @Override
            public boolean isEmpty() {
                return list.isEmpty();
            }

            @Override
            public T get() {
                return list.poll();
            }

            @Override
            public void add(T t) {
                list.add(t);
            }

            @Override
            public void registerCosts(Map<T, Double> costs) {
                this.costs = costs;
            }

            @Override
            public void registerEnd(T end) {
                this.end = end;
            }
        });
    }

    private List<T> search(T start, T end, FronteerAbstraction<T> fronteer) {
        long startTime = System.currentTimeMillis();

        Set<T> explored = new HashSet<>();
        Map<T, T> parents = new HashMap<>();
        Map<T, Double> costs = new HashMap<>();

        fronteer.registerCosts(costs);
        fronteer.registerEnd(end);

        fronteer.add(start);
        costs.put(start, 0d);

        boolean found = false;

        while (!fronteer.isEmpty()) {
            T currentNode = fronteer.get();
            double currentCost = costs.get(currentNode);

            if (currentNode == end) {
                found = true;
                break;
            }

            for (Link<T> link : getLinks(currentNode)) {
                T n = link.end;

                if (explored.contains(n)) {
                    if (costs.get(n) > currentCost + link.weight) {
                        costs.put(n, currentCost + link.weight);
                        parents.put(n, currentNode);
                    }

                    continue;
                }

                costs.put(n, currentCost + link.weight);
                fronteer.add(n);
                explored.add(n);
                parents.put(n, currentNode);
            }
        }

        System.out.println("Total time taken: " + (System.currentTimeMillis() - startTime));

        if (found) {
            return getPathFromParentMap(start, end, parents);
        } else {
            System.out.println("No such path.");
            return new ArrayList<>();
        }
    }

    private List<T> getPathFromParentMap(T start, T end, Map<T, T> parents) {
        T currentNode = end;
        Stack<T> reversedPath = new Stack<>();
        reversedPath.add(currentNode);

        do {
            currentNode = parents.get(currentNode);
            reversedPath.push(currentNode);
        } while (currentNode != start);

        List<T> path = new ArrayList<>();

        while (!reversedPath.isEmpty()) {
            path.add(reversedPath.pop());
        }

        return path;
    }
}
