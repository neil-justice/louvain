package com.github.neiljustice.louvain.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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
    assertEquals(m2.isSymmetric(), true);
  }
}

    
