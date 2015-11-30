package com.mishawagner.util.collections.graph;

import java.util.*;

public class MatrixGraph<T> extends AbstractGraph<T> {
    private Double[][] relations;
    private Map<T, Integer> indexes;
    private Map<Integer, T> elements;
    private int nodeCount = 0;

    public MatrixGraph() {
        relations = new Double[0][0];
        indexes = new HashMap<>();
        elements = new HashMap<>();
    }

    @Override
    public void add(T t) {
        nodeCount ++;

        indexes.put(t, nodeCount - 1);
        elements.put(nodeCount - 1, t);

        Double[][] newRelations = new Double[nodeCount][nodeCount];

        relations = copy2DArrays(relations, newRelations);
    }

    private Double[][] copy2DArrays(Double[][] from, Double[][] to) {
        for (int i = 0; i < from.length; i++) {
            System.arraycopy(from[i], 0, to[i], 0, from[i].length);
        }

        return to;
    }

    @Override
    public List<T> getAllNodes() {
        return new ArrayList<>(indexes.keySet());
    }

    @Override
    public void addLink(T start, T end, double weight) {
        relations[indexes.get(start)][indexes.get(end)] = weight;
    }

    @Override
    public List<Link<T>> getLinks(T t) {
        Double[] neighbours = relations[indexes.get(t)];
        List<Link<T>> links = new ArrayList<>();

        for (int i = 0; i < neighbours.length; i++) {
            if (neighbours[i] != null) {
                links.add(new Link<>(t, elements.get(i), neighbours[i]));
            }
        }

        return links;
    }

    @Override
    public void addAll(List<T> t) {
        for (int i = 0; i < t.size(); i++) {
            indexes.put(t.get(i), nodeCount + i);
            elements.put(nodeCount + i, t.get(i));
        }

        nodeCount += t.size();

        Double[][] newRelations = new Double[nodeCount][nodeCount];

        relations = copy2DArrays(relations, newRelations);
    }
}
