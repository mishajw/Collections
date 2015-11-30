package com.mishawagner.util.collections.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PointerGraph<T> extends AbstractGraph<T> {
    public class Node<U> {
        U item;
        List<Link<U>> neighbours;

        public Node(U item) {
            this.item = item;
            neighbours = new ArrayList<>();
        }

        public void addNeighbour(Node<U> newNeighbour, double weight) {
            neighbours.add(new Link<>(this.item, newNeighbour.item, weight));
        }
    }

    public PointerGraph() {
        nodes = new HashMap<>();
    }

    private final Map<T, Node<T>> nodes;

    @Override
    public void add(T t) {
        getNodeForItem(t);
    }

    @Override
    public List<T> getAllNodes() {
        return nodes.values()
                .stream()
                .map(n -> n.item)
                .collect(Collectors.toList());
    }

    @Override
    public void addLink(T start, T end, double weight) {
        Node<T> startNode = getNodeForItem(start);
        Node<T> endNode = getNodeForItem(end);

        startNode.addNeighbour(endNode, weight);
    }

    @Override
    public List<Link<T>> getLinks(T t) {
        return getNodeForItem(t).neighbours;
    }

    private Node<T> getNodeForItem(T item) {
        if (nodes.containsKey(item)) {
            return nodes.get(item);
        }

        Node<T> newNode = new Node<>(item);
        nodes.put(item, newNode);
        return newNode;
    }
}
