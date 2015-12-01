package com.mishawagner.util.collections.tree;

import com.mishawagner.util.collections.Collection;

import java.util.List;

public class TrieTree<T> extends Collection<List<T>> {
  private class Node<U> {
    U item;
    List<U> children;

    public Node(U item) {
      this.item = item;
    }
  }

  @Override
  public void insert(List<T> ts) {

  }

  @Override
  public boolean remove(List<T> ts) {
    return false;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean contains(List<T> ts) {
    return false;
  }
}
