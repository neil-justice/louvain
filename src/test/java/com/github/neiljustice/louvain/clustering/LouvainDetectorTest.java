package com.github.neiljustice.louvain.clustering;

import com.github.neiljustice.louvain.graph.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class LouvainDetectorTest {

  @Test
  public void checkArxivGraphMod() {
    Graph g = new GraphBuilder().fromFile("graphs/arxiv.txt");
    LouvainDetector ld = new LouvainDetector(g);
    ld.run();
    assertTrue(ld.modularity() > 0.81);
  }

  @Test
  public void checkCavemanGraph() {
    Graph g = new GraphBuilder().fromFile("graphs/connected-caveman-graph.csv");
    LouvainDetector ld = new LouvainDetector(g);
    List<int[]> res = ld.run();
    assertEquals(res.size(), 1);
    assertEquals(g.partitioning().numComms(), 6);
    assertTrue(ld.modularity() > 0.7);
  }  
}
