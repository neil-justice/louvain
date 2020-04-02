/* MIT License

Copyright (c) 2018 Neil Justice

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.neiljustice.louvain.clustering;

import com.github.neiljustice.louvain.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Assigns each node to a randomly generated set.  Can be run in two
 * different ways - either generates layers of a set size, or given
 * a real community partition generates random communities of exactly the
 * same size.
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