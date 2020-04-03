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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommunityStructureTest {
  private CommunityStructure cs;

  @Before
  public void init() {
    final int[] partition = {1, 8, 8, 5, 1, 5, 2, 9, 0, 9};
    cs = new CommunityStructure(partition);
  }

  @Test
  public void checkIndexing() {
    assertEquals(cs.community(0), 0);
    assertEquals(cs.community(1), 1);
    assertEquals(cs.community(2), 1);
    assertEquals(cs.community(3), 2);
    assertEquals(cs.community(4), 0);
    assertEquals(cs.community(5), 2);
    assertEquals(cs.community(6), 3);
    assertEquals(cs.community(7), 4);
    assertEquals(cs.community(8), 5);
    assertEquals(cs.community(9), 4);
  }

  @Test
  public void checkSizes() {
    assertEquals(cs.communitySize(0), cs.communityMembers(0).size());
    assertEquals(cs.communitySize(1), cs.communityMembers(1).size());
    assertEquals(cs.communitySize(2), cs.communityMembers(2).size());
    assertEquals(cs.communitySize(3), cs.communityMembers(3).size());
    assertEquals(cs.communitySize(4), cs.communityMembers(4).size());
    assertEquals(cs.communitySize(5), cs.communityMembers(5).size());
  }

  @Test
  public void checkNumComms() {
    assertEquals(cs.numComms(), 6);
  }
}
