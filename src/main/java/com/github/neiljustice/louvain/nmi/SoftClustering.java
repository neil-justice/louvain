package com.github.neiljustice.louvain.nmi;

public class SoftClustering implements Clustering {
  private final int N; // no. of nodes
  private final double[] dist;
  private final int numComms;
  private final double[][] fuzzyPartitioning;
  
  public SoftClustering(double[][] fuzzyPartitioning) {
    this.fuzzyPartitioning = fuzzyPartitioning;
    N = fuzzyPartitioning[0].length;
    numComms = fuzzyPartitioning.length;
    dist = new double[numComms];

    for (int comm = 0; comm < numComms; comm++) {
      for (int doc = 0; doc < N; doc++) {
        dist[comm] += fuzzyPartitioning[comm][doc];
      }
      dist[comm] /= N;
    }
  }
  
  @Override
  public double distribution(int index) { 
    if (index >= numComms) throw new Error("index out of bounds");
    return dist[index];
  }  
  
  @Override
  public double entropy() { return Entropy.entropy(dist); }
  
  @Override
  public int length() { return numComms; }
  
  public double fuzzyPartitioning(int comm, int node) { 
    if (comm >= numComms) throw new Error("index out of bounds");
    if (node >= N) throw new Error("node out of bounds");
    return fuzzyPartitioning[comm][node];
  }
  
  public int N() { return N; }
}