package com.github.neiljustice.louvain.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class GraphTest {
  private static Graph g;
  
  @BeforeClass
  public static void init() {
    g = new GraphBuilder().setSize(7)
                          .addEdge(0,1,12)
                          .addEdge(1,2,14)
                          .addEdge(0,2,5)
                          .addEdge(3,4,10)
                          .addEdge(4,4,10)
                          .addEdge(3,5,10)
                          .addEdge(4,6,11)
                          .addEdge(5,6,17)
                          .build();
    
    g.partitioning().moveToComm(1,0);
    g.partitioning().moveToComm(2,0);
    g.partitioning().moveToComm(4,3);
    g.partitioning().moveToComm(5,3);
  }

  @Test
  public void checkBasics() {
    assertEquals(g.order(),7);
    assertEquals(g.degree(1),26);
    assertEquals(g.size(),84);
    assertEquals(g.degree(0),17);
    assertEquals(g.weight(0,1), g.weight(1,0));
  }

  @Test
  public void checkDnodecomm() {
    assertEquals(g.partitioning().dnodecomm(6,3),28);
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
    assertEquals(g.partitioning().numComms(),3);
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