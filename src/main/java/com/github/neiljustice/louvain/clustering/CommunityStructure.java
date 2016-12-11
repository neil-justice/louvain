package com.github.neiljustice.louvain.clustering;

import java.util.*;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;

public class CommunityStructure {
  
  private final int order;
  // community of each node
  private final int[] community;
  // size of each comm
  private final int[] communitySize;
  // keeps track of the members of each comm:
  private final TIntArrayList[] communityMembers;
  
  private final int numComms;

  public CommunityStructure(int[] partitioning) {
    order = partitioning.length;
    community = new int[order];
    
    TIntIntHashMap commToIndex = reIndexCommunities(partitioning);
    numComms = commToIndex.size();
    
    communitySize = new int[numComms];
    communityMembers = new TIntArrayList[numComms];
    
    for (int comm = 0; comm < numComms; comm++) {
      communityMembers[comm] = new TIntArrayList();
    }
    
    for (int node = 0; node < order; node++) {
      int comm = commToIndex.get(partitioning[node]);
      community[node] = comm;
      communitySize[comm]++;
      communityMembers[comm].add(node);
    }
  }
  
  // Re-index the community IDs consecutively from 0:
  private TIntIntHashMap reIndexCommunities(int[] partitioning) {
    TIntIntHashMap commToIndex = new TIntIntHashMap();
    boolean[] commExists = new boolean[order];
    int index = 0;
    
    for (int node = 0; node < order; node++) {
      int comm = partitioning[node];
      if (commExists[comm] == false) {
        commExists[comm] = true;
        commToIndex.put(comm, index);
        index++;
      } 
    }
    
    return commToIndex;
  }

  public int order() { return order; }
  
  public int numComms() { return numComms; }
  
  public int[] communities() { return community; }
  public int community(int node) { 
    if (node >= order) throw new Error("node index out of bounds");
    return community[node]; 
  }
  
  public int[] communitySizes() { return communitySize; }
  public int communitySize(int comm) { 
    if (comm >= numComms) throw new Error("node index out of bounds");
    return communitySize[comm]; 
  }
  
  public TIntArrayList[] communityMembers() { return communityMembers; }
  public TIntArrayList communityMembers(int comm) {
    if (comm >= numComms) throw new Error("node index out of bounds");
    return communityMembers[comm];
  }
}
