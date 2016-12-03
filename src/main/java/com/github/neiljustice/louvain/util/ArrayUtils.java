package com.github.neiljustice.louvain.util;

import java.util.*;

public class ArrayUtils {
  
  public static int lastIndexOf(double[] a, double n) {
    if (a == null) return -1;
    int lastIndex = -1;
    for (int i = 0; i < a.length; i++) {
      if (a[i] == n) lastIndex = i;
    }
    return lastIndex;
  }
  
  public static void shuffle(int[] a) {
    final Random rnd = new Random();
    int count = a.length;
    for (int i = count; i > 1; i--) {
      int r = rnd.nextInt(i);
      swap(a , i - 1, r);
    }
  }
  
  public static void fillRandomly(int[] a) {
    int count = a.length;
    
    for (int i = 0; i < count; i++) {
      a[i] = i;
    }
    
    shuffle(a);
  }

  private static void swap(int[] a, int i, int j) {
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }  
}