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

    return duplicate.size();
  }

  public double distribution(int i, int j) {
    if (i >= dist.length || j >= dist[i].length) {
      throw new Error("index out of bounds");
    }
    return dist[i][j];
  }
}