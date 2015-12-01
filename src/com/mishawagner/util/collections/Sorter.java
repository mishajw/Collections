package com.mishawagner.util.collections;

import java.util.Arrays;
import java.util.Random;

public class Sorter {
  public static int[] mergeSort(int[] xs) {
    if (xs.length <= 1) {
      return xs;
    }

    int middle = xs.length / 2;

    int[] first = Arrays.copyOfRange(xs, 0, middle);
    int[] second = Arrays.copyOfRange(xs, middle, xs.length);

    int[] firstSorted = mergeSort(first);
    int[] secondSorted = mergeSort(second);

    return merge(firstSorted, secondSorted);
  }

  private static int[] merge(int[] xs, int[] ys) {
    int[] merged = new int[xs.length + ys.length];

    int ix = 0, iy = 0;

    while (ix + iy < merged.length) {
      if (ix >= xs.length) {
        merged[ix + iy] = ys[iy];
        iy ++;
      } else if (iy >= ys.length) {
        merged[ix + iy] = xs[ix];
        ix ++;
      } else if (ys[iy] < xs[ix]) {
        merged[ix + iy] = ys[iy];
        iy ++;
      } else {
        merged[ix + iy] = xs[ix];
        ix ++;
      }
    }

    return merged;
  }

  public static int[] quickSort(int[] xs) {
    return quickSort(xs, 0, xs.length - 1);
  }

  private static int[] quickSort(int[] xs, int start, int end) {
    if (end - start <= 1) {
      return xs;
    }

    int pivotIndex = start + (end - start) / 2;
    int pivot = xs[pivotIndex];
    xs = swap(xs, pivotIndex, end);

    int iLeft = start;
    int iRight = end - 1;

    while (iLeft <= iRight) {
      while (iLeft <= iRight && xs[iLeft] <= pivot) {
        iLeft ++;
      }

      while (iLeft <= iRight && xs[iRight] >= pivot) {
        iRight --;
      }

      if (iLeft < iRight) {
        xs = swap(xs, iLeft, iRight);
        iLeft ++;
        iRight ++;
      }
    }

    xs = swap(xs, iLeft, end);

    xs = quickSort(xs, start, iLeft - 1);
    xs = quickSort(xs, iLeft + 1, end);

    return xs;
  }

  private static int[] swap(int[] xs, int i1, int i2) {
    int temp = xs[i1];
    xs[i1] = xs[i2];
    xs[i2] = temp;
    return xs;
  }

  public static void main(String[] args) {
    for (int i = 1; i < 10000000; i *= 2) {
      System.out.println(i + ":");
      testTimes(i);
    }
  }

  public static void testTimes(int sampleSize) {
    Random rand = new Random();
    int[] sample = new int[sampleSize];

    for (int i = 0; i < sampleSize; i++) {
      sample[i] = rand.nextInt(sampleSize);
    }

    long startMerge = System.currentTimeMillis();
    mergeSort(sample);
    System.out.println("Merge sort: " + (System.currentTimeMillis() - startMerge));

    long startQuick = System.currentTimeMillis();
    quickSort(sample);
    System.out.println("Quick sort: " + (System.currentTimeMillis() - startQuick));
  }
}
