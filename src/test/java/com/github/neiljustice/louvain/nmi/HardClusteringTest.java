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

public class HardClusteringTest {
  private static HardClustering hc;
  
  @BeforeClass
  public static void init() {
    int[] partition = {1,8,8,5,1,5,2,9,0,9};
    CommunityStructure cs = new CommunityStructure(partition);
    hc = new HardClustering(cs);
  }

  @Test
  public void checkLength() {
    assertEquals(hc.length(), 6);
    assertEquals(hc.N(), 10);
  } 
  
  @Test
  public void checkDistribution() {
    assertEquals(hc.distribution(0), 0.2d, 0d);
    assertEquals(hc.distribution(1), 0.2d, 0d);
    assertEquals(hc.distribution(2), 0.2d, 0d);
    assertEquals(hc.distribution(3), 0.1d, 0d);
    assertEquals(hc.distribution(4), 0.2d, 0d);
    assertEquals(hc.distribution(5), 0.1d, 0d);
  }

  @Test
  public void checkNumMembers() {
    assertEquals(hc.members(0).size(), 2);
    assertEquals(hc.members(1).size(), 2);
    assertEquals(hc.members(2).size(), 2);
    assertEquals(hc.members(3).size(), 1);
    assertEquals(hc.members(4).size(), 2);
    assertEquals(hc.members(5).size(), 1);
  }
}
    
    
