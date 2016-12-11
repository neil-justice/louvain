package com.github.neiljustice.louvain.clustering;

import java.util.*;
import com.github.neiljustice.louvain.util.*;

/**
 *  Assigns each node to a randomly generated set.  Can be run in two
 * different ways - either generates layers of a set size, or given
 * a real community partition generates random communities of exactly the
 * same size.
 *
 */
public class RandomCommunityAssigner implements Clusterer {
  private final List<int[]> randomCommunities = new ArrayList<int[]>();
  private final List<int[]> actualCommunities;
  private final int layers;
  private final int order;
    
  public RandomCommunityAssigner(List<int[]> actualCommunities) {
    order = actualCommunities.get(0).length;
    layers = actualCommunities.size();
    this.actualCommunities = actualCommunities;

    for (int layer = 0; layer < layers; layer++) {
      int[] comm = new int[order];
      randomCommunities.add(comm);
      for (int node = 0; node < order; node++) {
        comm[node] = actualCommunities.get(layer)[node];
      }
      ArrayUtils.shuffle(comm);
    }  
  }
  
  @Override
  public List<int[]> run() {
    return randomCommunities;
  }
  
  public List<int[]> reshuffle() {
    for (int layer = 0; layer < layers; layer++) {
      ArrayUtils.shuffle(randomCommunities.get(layer));
    }
    return randomCommunities;
  }
  
}