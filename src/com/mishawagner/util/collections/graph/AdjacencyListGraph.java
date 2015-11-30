package com.mishawagner.util.collections.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyListGraph<T> extends AbstractGraph<T> {
    Map<T, List<Link<T>>> adjacencyList;

    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void add(T t) {
        adjacencyList.put(t, new ArrayList<>());
    }

    @Override
    public List<T> getAllNodes() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    @Override
    public void addLink(T start, T end, double weight) {
        adjacencyList.get(start).add(new Link<>(start, end, weight));
    }

    @Override
    public List<Link<T>> getLinks(T t) {
        return adjacencyList.get(t);
    }
}
