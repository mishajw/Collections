package com.mishawagner.util.collections.tree;

import com.mishawagner.util.collections.Collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrieTree<T> extends Collection<List<T>> {
  private T item;
  private Set<TrieTree<T>> children;

  public TrieTree(T item) {
    this();
    this.item = item;
  }

  public TrieTree() {
    children = new HashSet<>();
  }

  @Override
  public void insert(List<T> ts) {
    if (ts.size() == 0) {
      return;
    }

    T head = ts.get(0);
    List<T> tail = ts.subList(1, ts.size());

    TrieTree<T> existingNode = getNodeForHead(head);

    if (existingNode != null) {
      existingNode.insert(tail);
      return;
    }

    TrieTree<T> newChild = new TrieTree<>(head);
    children.add(newChild);
    newChild.insert(tail);
  }

  @Override
  public boolean remove(List<T> ts) {
    return false;
  }

  @Override
  public int size() {
    return 1 + children.stream()
        .mapToInt(TrieTree::size)
        .sum();
  }

  @Override
  public boolean contains(List<T> ts) {
    if (ts.size() == 0) {
      return true;
    }

    T head = ts.get(0);
    List<T> tail = ts.subList(1, ts.size());

    TrieTree<T> existingNode = getNodeForHead(head);

    return existingNode != null && existingNode.contains(tail);
  }

  private TrieTree<T> getNodeForHead(T head) {
    for (TrieTree<T> n : children) {
      if (head == n.item) return n;
    }

    return null;
  }

  public List<List<T>> getAll() {
    if (children.size() == 0) {
      List<List<T>> all = new ArrayList<>();
      List<T>       one = new ArrayList<>();
      one.add(item);
      all.add(one);
      return all;
    }

    List<List<T>> all = new ArrayList<>();

    for (TrieTree<T> n : children) {
      List<List<T>> currentAll = n.getAll();

      for (List<T> l : currentAll) {
        l.add(0, item);
      }

      all.addAll(currentAll);
    }

    return all;
  }

  @Override
  public String toString() {
    String s = item + "(";

    for (TrieTree<T> n : children) {
      s += n.toString() + ", ";
    }

    s += ")";

    return s;
  }

  public static void main(String[] args) {
    TrieTree<Character> t = new TrieTree<>();

    String[] elements = ("Shall I compare thee to a summer's day?\n" +
        "Thou art more lovely and more temperate:\n" +
        "Rough winds do shake the darling buds of May,\n" +
        "And summer's lease hath all too short a date").split(" ");

    for (String s : elements) {
      t.insert(stringToCharList(s));
    }

    System.out.println(t.contains(stringToCharList(elements[0])));
    System.out.println(t.contains(stringToCharList("world")));

    System.out.println(t.getAll());

    System.out.println(t);
  }

  private static List<Character> stringToCharList(String s) {
    List<Character> list = new ArrayList<>();

    for (Character c : s.toCharArray()) {
      list.add(c);
    }

    return list;
  }
}
