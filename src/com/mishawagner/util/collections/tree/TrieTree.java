package com.mishawagner.util.collections.tree;

import com.mishawagner.util.collections.Collection;
import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrieTree<T> extends Collection<List<T>> {
  private T item;
  private Set<TrieTree<T>> children;
  private int terminatedHere = 0;

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
      terminatedHere++;
      return;
    }

    T head = ts.get(0);
    if (head == null) {
      throw new IllegalArgumentException("Can't add null elements.");
    }
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
    if (ts.size() == 0) {
      if (terminatedHere > 0) {
        terminatedHere--;
        return true;
      } else {
        return false;
      }
    }

    T head = ts.get(0);
    List<T> tail = ts.subList(1, ts.size());

    TrieTree<T> existingNode = getNodeForHead(head);

    if (existingNode == null) {
      return false;
    }

    boolean rest = existingNode.remove(tail);

    if (rest && existingNode.isEmpty()) {
      children.remove(existingNode);
    }

    return rest;
  }

  @Override
  public int size() {
    return terminatedHere + children.stream()
        .mapToInt(TrieTree::size)
        .sum();
  }

  @Override
  public boolean contains(List<T> ts) {
    if (ts.size() == 0 && terminatedHere > 0) {
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
    List<List<T>> all = new ArrayList<>();

    for (int i = 0; i < terminatedHere; i++) {
      List<T> one = new ArrayList<>();
      one.add(item);
      all.add(one);
    }

    for (TrieTree<T> n : children) {
      List<List<T>> currentAll = n.getAll();

      for (List<T> l : currentAll) {
        if (item != null) l.add(0, item);
      }

      all.addAll(currentAll);
    }

    return all;
  }

  public boolean isEmpty() {
    if (terminatedHere > 0) {
      return false;
    }

    for (TrieTree<T> t : children) {
      if (!t.isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString() {
    String s = item + "[" + terminatedHere + "]" + "(";

    for (TrieTree<T> n : children) {
      s += n.toString() + ", ";
    }

    s += ")";

    return s;
  }

  public static void main(String[] args) {
    TrieTree<Character> t = new TrieTree<>();

//    String[] elements = ("Shall I compare thee to a summer's day?\n" +
//        "Thou art more lovely and more temperate:\n" +
//        "Rough winds do shake the darling buds of May,\n" +
//        "And summer's lease hath all too short a date").split(" ");

    String[] elements = "abc abc abcd abc abcd".split(" ");

    for (String s : elements) {
      t.insert(stringToCharList(s));
    }

    System.out.println(t);
    System.out.println(t.getAll());
    System.out.println(t.remove(stringToCharList("abcd")));
    System.out.println(t);
    System.out.println(t.getAll());
    System.out.println(t.remove(stringToCharList("abcd")));
    System.out.println(t);
    System.out.println(t.getAll());
    System.out.println(t.remove(stringToCharList("abc")));
    System.out.println(t);
    System.out.println(t.getAll());
    System.out.println(t.remove(stringToCharList("abc")));
    System.out.println(t);
    System.out.println(t.getAll());
    System.out.println(t.remove(stringToCharList("abc")));
    System.out.println(t);
    System.out.println(t.getAll());
    System.out.println(t.remove(stringToCharList("abc")));
    System.out.println(t);
    System.out.println(t.getAll());

    System.out.println(t);
    System.out.println(t.getAll());
  }

  private static List<Character> stringToCharList(String s) {
    List<Character> list = new ArrayList<>();

    for (Character c : s.toCharArray()) {
      list.add(c);
    }

    return list;
  }
}
