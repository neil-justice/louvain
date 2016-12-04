package com.github.neiljustice.louvain.nmi;

import com.github.neiljustice.louvain.clustering.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class NMITest {
  private static CommunityStructure cs;
  private static CommunityStructure c2;
  private static int[] p1 = {0, 0, 1, 1, 2, 2, 3, 4, 4, 5};
  private static int[] p2 = {5, 5, 4, 4, 3, 3, 2, 1, 1, 0};
  
  @BeforeClass
  public static void init() {
    cs = new CommunityStructure(p1);
    c2 = new CommunityStructure(p2);
  }

  @Test
  public void checkSelfNMI() {
    assertEquals(NMI.NMI(cs, c2), 1d, 0d);
    assertEquals(NMI.NMI(cs, cs), 1d, 0d);
    assertEquals(NMI.NMI(c2, c2), 1d, 0d);
    assertEquals(NMI.NMI(p1, p2), 1d, 0d);
  }
}