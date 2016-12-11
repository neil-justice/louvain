package com.github.neiljustice.louvain.nmi;

import java.util.*;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.iterator.TIntObjectIterator;

public class Entropy {

  /**
   * measures the shannon entropy H of a prob. dist.
   */
  public static double entropy(double[] dist) {
    return entropy(dist, 2);
  }
  
  /**
   * if base == length of dist, this normalised entropy to within the range 0-1.
   */
  public static double entropy(double[] dist, int base) {
    if (base <= 0) throw new Error("Base cannot be zero or negative");
    
    double H = 0d;
    for (int i = 0; i < dist.length; i++) {
      if (dist[i] != 0d) H -= dist[i] * Math.log(dist[i]);
    }
    return H / Math.log(base);
  }
}