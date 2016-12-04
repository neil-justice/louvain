package com.github.neiljustice.louvain.nmi;

import gnu.trove.list.array.TIntArrayList;



public class JointDistribution {
  private final double[][] dist;
  private final int N;
  
  public JointDistribution(SoftClustering soft, HardClustering hard) {
    dist = new double[soft.length()][hard.length()];
    N = hard.N();
    
    for (int node = 0; node < N; node++) {
      int comm = hard.community(node);
      for (int comm2 = 0; comm2 < soft.length(); comm2++) {
        dist[comm2][comm] += soft.fuzzyPartitioning(comm2, node) / N;
      }
    }
  }

  public JointDistribution(HardClustering hard1, HardClustering hard2) {
    int n1 = hard1.length();
    int n2 = hard2.length();
    dist = new double[n1][n2];
    N = hard1.N();

    for (int i = 0; i < n1; i++) {
      for (int j = 0; j < n2; j++) {
        dist[i][j] = intersection(hard1, i, hard2, j) / N;
      }
    }
  }
  
  // returns the no. of nodes which are in both communities
  private double intersection(HardClustering hard1, int comm1, 
                              HardClustering hard2, int comm2) {
    TIntArrayList duplicate = new TIntArrayList(hard1.members(comm1));
    duplicate.retainAll(hard2.members(comm2));

    return (double) duplicate.size();
  }
  
  public double distribution(int i, int j) { 
    if (i >= dist.length || j >= dist[i].length) throw new Error("index out of bounds");
    return dist[i][j]; 
  } 
}