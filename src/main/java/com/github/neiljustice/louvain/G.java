package com.github.neiljustice.louvain;

import com.github.neiljustice.louvain.graph.*;
import com.github.neiljustice.louvain.clustering.*;
import com.github.neiljustice.louvain.nmi.*;

public class G {
  public static void main(String[] args) {
    G prog = new G();
    
    prog.run();
  }
  
  public void run() {
    
    Graph g = new GraphBuilder().fromFile("graphs/twitter.csv", true);
    LouvainDetector ld = new LouvainDetector(g);
    LayeredCommunityStructure cs = new LayeredCommunityStructure(ld.run());

    InfomapResultsReader irr = new InfomapResultsReader("graphs/infomap.tree");
    LayeredCommunityStructure cs2 = new LayeredCommunityStructure(irr.run());
    
    for (int i = 0; i < cs.layers(); i++) {
      for (int j = 0; j < cs2.layers(); j++) {
        System.out.printf("%.03f ", NMI.NMI(cs.layer(i), cs2.layer(j)));
      }
      System.out.println();
    }
  }
}