package com.mishawagner.util.collections;

class HashTable {
  private final int size;
  private String[] table;

  public HashTable(int _size) {
    this.size = _size;
    this.table = new String[this.size];
  }

  public boolean put(String s) {
    int hashCode = s.hashCode();
    int index = hashCode % this.size;
    int incrCount = 0;

    while (this.table[index] != null) {
      if (incrCount >= this.size) {
        return false;
      }

      index ++;
      incrCount ++;
    }

    this.table[index] = s;
    return true;
  }

  public boolean contains(String s) {
    int hashCode = s.hashCode();
    int index = hashCode % this.size;
    int incrCount = 0;

    while (incrCount <= this.size) {
      String currentItem = this.table[index];

      if (currentItem == null) {
        return false;
      } else if (currentItem.equals(s)) {
        return true;
      } else {
        index ++;
        incrCount ++;
      }
    }

    return (this.table[index] == s);
  }

  public void printAll() {
    System.out.println("Table:");
    for (int i = 0; i < this.size; i++) {
      System.out.println(i + ": " + this.table[i]);
    }
  }

  public static void main(String[] args) {
    HashTable ht = new HashTable(10);

    String[] sentance = "Hello world its Misha".split(" ");

    for (String word : sentance) {
      ht.put(word);
    }

    ht.printAll();

    for (String word : sentance) {
      System.out.println(ht.contains(word));
    }

    System.out.println(ht.contains("Nope"));
  }
}

