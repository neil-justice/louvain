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

package com.github.neiljustice.louvain.graph;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphTest {
  private static Graph g;

  @BeforeClass
  public static void init() {
    g = new GraphBuilder().setSize(7)
        .addEdge(0, 1, 12)
        .addEdge(1, 2, 14)
        .addEdge(0, 2, 5)
        .addEdge(3, 4, 10)
        .addEdge(4, 4, 10)
        .addEdge(3, 5, 10)
        .addEdge(4, 6, 11)
        .addEdge(5, 6, 17)
        .build();

    g.partitioning().moveToComm(1, 0);
    g.partitioning().moveToComm(2, 0);
    g.partitioning().moveToComm(4, 3);
    g.partitioning().moveToComm(5, 3);
  }

  @Test
  public void checkBasics() {
    assertEquals(g.order(), 7);
    assertEquals(g.degree(1), 26);
    assertEquals(g.size(), 84);
    assertEquals(g.degree(0), 17);
    assertEquals(g.weight(0, 1), g.weight(1, 0));
  }

  @Test
  public void checkDnodecomm() {
    assertEquals(g.partitioning().dnodecomm(6, 3), 28);
  }

  @Test
  public void checkCommDegrees() {
    assertEquals(g.partitioning().totDegree(3), g.degree(3) + g.degree(4) + g.degree(5)); // 10 + 41 + 27
    assertEquals(g.partitioning().totDegree(1), 0);
    assertEquals(g.partitioning().totDegree(5), 0);
    assertEquals(g.partitioning().intDegree(0), 62);
    assertEquals(g.partitioning().intDegree(3), 50);
    assertEquals(g.partitioning().intDegree(1), 0);
    assertEquals(g.partitioning().intDegree(2), 0);
    assertEquals(g.partitioning().intDegree(6), 0);
  }

  @Test
  public void checkNumComms() {
    assertEquals(g.partitioning().numComms(), 3);
  }

  @Test
  public void checkCommWeights() {
    assertEquals(g.partitioning().communityWeight(0, 0), 62);
    assertEquals(g.partitioning().communityWeight(3, 0), 0);
    assertEquals(g.partitioning().communityWeight(3, 6), 28);
    assertEquals(g.partitioning().communityWeight(3, 3), 50);
    assertEquals(g.partitioning().communityWeight(6, 6), 0);
  }
}