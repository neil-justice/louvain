package com.github.neiljustice.louvain.nmi;

import com.github.neiljustice.louvain.clustering.CommunityStructure;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;



public class HardClustering implements Clustering {
  private final int N; // no. of nodes
  private final double[] dist;
  private final int numComms;
  private final TIntArrayList[] members;
  private final int[] community;
  
  public HardClustering(CommunityStructure structure) {
    members = structure.communityMembers();
    community = structure.communities();
    N = structure.order();
    numComms = structure.numComms();
    
    dist = new double[numComms];
    
    for (int comm = 0; comm < numComms; comm++) {
      dist[comm] = (double) structure.communitySize(comm) / N;
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
  
  public int N() { return N; }
  
  public int community(int comm) { 
    if (comm >= numComms) throw new Error("index out of bounds");
    return community[comm];
  }
  
  public TIntArrayList members(int comm) {
    if (comm >= numComms) throw new Error("index out of bounds");
    return members[comm];
  } 
}