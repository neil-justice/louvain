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
    if (index >= numComms) {
      throw new Error("index out of bounds");
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

  public double fuzzyPartitioning(int comm, int node) {
    if (comm >= numComms) {
      throw new Error("index out of bounds");
    }
    if (node >= N) {
      throw new Error("node out of bounds");
    }
    return fuzzyPartitioning[comm][node];
  }

  public int N() {
    return N;
  }
}