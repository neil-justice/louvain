public class G {
  public static void main(String[] args) {
    G prog = new G();
    
    Graph.main(null);
    CommunityStructure.main(null);
    HardClustering.main(null);
    JointDistribution.main(null);
    prog.run();
  }
  
  public void run() {
    
    Graph g = new GraphBuilder().fromFile("graphs/arxiv.txt");
    LouvainDetector ld = new LouvainDetector(g);
    LayeredCommunityStructure cs = new LayeredCommunityStructure(ld.run());
    
    Graph g2 = new GraphBuilder().fromFile("graphs/arxiv.txt");
    LouvainDetector ld2 = new LouvainDetector(g2);
    LayeredCommunityStructure cs2 = new LayeredCommunityStructure(ld2.run());
    
    for (int i = 0; i < cs.layers(); i++) {
      for (int j = 0; j < cs2.layers(); j++) {
        System.out.printf("%.03f ", NMI.NMI(cs.layer(i), cs2.layer(j)));
      }
      System.out.println();
    }
  }
}