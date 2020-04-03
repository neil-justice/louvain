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

package com.github.neiljustice.louvain.nmi;

import com.github.neiljustice.louvain.clustering.CommunityStructure;
import gnu.trove.list.array.TIntArrayList;


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
    if (index >= numComms) {
      throw new IndexOutOfBoundsException("index out of bounds");
    }
    return dist[index];
  }

  @Override
  public double entropy() {
    return Entropy.entropy(dist);
  }

  @Override
  public int length() {
    return numComms;
  }

  public int N() {
    return N;
  }

  public int community(int comm) {
    if (comm >= numComms) {
      throw new IndexOutOfBoundsException("index out of bounds");
    }
    return community[comm];
  }

  public TIntArrayList members(int comm) {
    if (comm >= numComms) {
      throw new IndexOutOfBoundsException("index out of bounds");
    }
    return members[comm];
  }
}