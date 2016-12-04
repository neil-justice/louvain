package com.github.neiljustice.louvain.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CommunityStructureTest {
  private static CommunityStructure cs;
  
  @BeforeClass
  public static void init() {
    int[] partition = {1,8,8,5,1,5,2,9,0,9};
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
