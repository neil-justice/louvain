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
 * Assigns each node to a randomly generated set. Given a real community partition
 * generates random communities of exactly the same size.
 */
public class RandomCommunityAssigner implements Clusterer {
  private final List<int[]> randomCommunities = new ArrayList<>();
  private final int layers;
  private final int order;

  public RandomCommunityAssigner(LayeredCommunityStructure lcs) {
    order = lcs.layer(0).order();
    layers = lcs.layers();

    for (int layer = 0; layer < layers; layer++) {
      final int[] comm = new int[order];
      randomCommunities.add(comm);
      System.arraycopy(lcs.layer(layer).communities(), 0, comm, 0, order);
      ArrayUtils.shuffle(comm);
    }
  }

  public RandomCommunityAssigner(List<int[]> actualCommunities) {
    order = actualCommunities.get(0).length;
    layers = actualCommunities.size();

    for (int layer = 0; layer < layers; layer++) {
      final int[] comm = new int[order];
      randomCommunities.add(comm);
      System.arraycopy(actualCommunities.get(layer), 0, comm, 0, order);
      ArrayUtils.shuffle(comm);
    }
  }

  @Override
  public LayeredCommunityStructure cluster() {
    return new LayeredCommunityStructure(randomCommunities);
  }

  public LayeredCommunityStructure reshuffle() {
    for (int layer = 0; layer < layers; layer++) {
      ArrayUtils.shuffle(randomCommunities.get(layer));
    }
    return new LayeredCommunityStructure(randomCommunities);
  }

}