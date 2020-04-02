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
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JointDistributionTest {
  private static JointDistribution jd;

  @BeforeClass
  public static void init() {
    int[] partition = {0, 0, 1, 1, 2, 2, 3, 4, 4, 5};
    CommunityStructure cs = new CommunityStructure(partition);
    HardClustering hc = new HardClustering(cs);
    HardClustering h2 = new HardClustering(cs);
    jd = new JointDistribution(hc, h2);
  }

  @Test
  public void checkNonzeroDists() {
    assertEquals(jd.distribution(0, 0), 0.2d, 0d);
    assertEquals(jd.distribution(1, 1), 0.2d, 0d);
    assertEquals(jd.distribution(2, 2), 0.2d, 0d);
    assertEquals(jd.distribution(3, 3), 0.1d, 0d);
    assertEquals(jd.distribution(4, 4), 0.2d, 0d);
    assertEquals(jd.distribution(5, 5), 0.1d, 0d);

  }

  @Test
  public void checkZeroDists() {
    assertEquals(jd.distribution(0, 1), 0d, 0d);
    assertEquals(jd.distribution(5, 4), 0d, 0d);
  }

}