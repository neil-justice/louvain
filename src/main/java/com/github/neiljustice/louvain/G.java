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

package com.github.neiljustice.louvain;

import com.github.neiljustice.louvain.clustering.InfomapResultsReader;
import com.github.neiljustice.louvain.clustering.LayeredCommunityStructure;
import com.github.neiljustice.louvain.clustering.LouvainDetector;
import com.github.neiljustice.louvain.graph.Graph;
import com.github.neiljustice.louvain.graph.GraphBuilder;

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

    // for (int i = 0; i < cs.layers(); i++) {
    //   for (int j = 0; j < cs2.layers(); j++) {
    //     System.out.printf("%.03f ", NMI.NMI(cs.layer(i), cs2.layer(j)));
    //   }
    //   System.out.println();
    // }
  }
}