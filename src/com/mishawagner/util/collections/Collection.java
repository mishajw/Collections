package com.mishawagner.util.collections;

public abstract class Collection<T> {
  public abstract void insert(T t);
  public abstract boolean remove(T t);
  public abstract int size();
  public abstract boolean contains(T t);
}
