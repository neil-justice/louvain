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

    final TIntIntHashMap commToIndex = reIndexCommunities(partitioning);
    numComms = commToIndex.size();

    communitySize = new int[numComms];
    communityMembers = new TIntArrayList[numComms];

    for (int comm = 0; comm < numComms; comm++) {
      communityMembers[comm] = new TIntArrayList();
    }

    for (int node = 0; node < order; node++) {
      final int comm = commToIndex.get(partitioning[node]);
      community[node] = comm;
      communitySize[comm]++;
      communityMembers[comm].add(node);
    }
  }

  // Re-index the community IDs consecutively from 0:
  private TIntIntHashMap reIndexCommunities(int[] partitioning) {
    final TIntIntHashMap commToIndex = new TIntIntHashMap();
    final boolean[] commExists = new boolean[order];
    int index = 0;

    for (int node = 0; node < order; node++) {
      final int comm = partitioning[node];
      if (!commExists[comm]) {
        commExists[comm] = true;
        commToIndex.put(comm, index);
        index++;
      }
    }

    return commToIndex;
  }

  public int order() {
    return order;
  }

  public int numComms() {
    return numComms;
  }

  public int[] communities() {
    return community;
  }

  public int community(int node) {
    if (node >= order) {
      throw new IndexOutOfBoundsException("node index out of bounds");
    }
    return community[node];
  }

  public int[] communitySizes() {
    return communitySize;
  }

  public int communitySize(int comm) {
    if (comm >= numComms) {
      throw new IndexOutOfBoundsException("node index out of bounds");
    }
    return communitySize[comm];
  }

  public TIntArrayList[] communityMembers() {
    return communityMembers;
  }

  public TIntArrayList communityMembers(int comm) {
    if (comm >= numComms) {
      throw new IndexOutOfBoundsException("node index out of bounds");
    }
    return communityMembers[comm];
  }
}
