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

package com.github.neiljustice.louvain.util;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SparseIntMatrixTest {
  private static int[][] m1;
  private static SparseIntMatrix m2;

  @BeforeClass
  public static void init() {
    m1 = new int[3][3];
    m2 = new SparseIntMatrix(3);

    m1[1][2] = 3;
    m1[1][2] += 1101;
    m1[2][1] = 1104;
    m1[1][1] = 5;
    m1[2][2] = 100203;
    m2.set(1, 2, 3);
    m2.add(1, 2, 1101);
    m2.set(2, 1, 1104);
    m2.set(1, 1, 5);
    m2.set(2, 2, 100203);
  }

  @Test
  public void checkMatrixEquality() {
    assertEquals(m1[1][2], m2.get(1, 2));
    assertEquals(m1[1][1], m2.get(1, 1));
    assertEquals(m1[2][2], m2.get(2, 2));

  }

  @Test
  public void checkSymmetry() {
    assertTrue(m2.isSymmetric());
  }
}

    
