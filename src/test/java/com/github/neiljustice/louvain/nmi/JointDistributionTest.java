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