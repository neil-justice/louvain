package com.github.neiljustice.louvain.nmi;

/**
 * Interface which defines a clustering of nodes probabilistically.
 */
public interface Clustering {
  public double distribution(int index);
  public double entropy();
  public int length();
}