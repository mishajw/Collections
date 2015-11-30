package com.mishawagner.util.collections.graph;

import java.util.Comparator;
import java.util.Map;

public abstract class FronteerAbstraction<T> {
    public abstract boolean isEmpty();
    public abstract T get();
    public abstract void add(T t);
    public void registerCosts(Map<T, Double> costs) {}
    public void registerEnd(T end) {};

    public Comparator<T> getComparator() {
        return (t1, t2) -> {
            double comparison = getWeight(t1) - getWeight(t2);
            if (comparison == 0) {
                return 0;
            } else if (comparison > 0) {
                return 1;
            } else {
                return -1;
            }
        };
    }

    protected double getWeight(T t) {
        return 0;
    }
}
