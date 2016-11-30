import gnu.trove.list.array.TIntArrayList;

import tester.Tester;

public class JointDistribution {
  private final double[][] dist;
  private final int N;
  
  public JointDistribution(SoftClustering soft, HardClustering hard) {
    dist = new double[soft.length()][hard.length()];
    N = hard.N();
    
    for (int node = 0; node < N; node++) {
      int comm = hard.community(node);
      for (int topic = 0; topic < soft.length(); topic++) {
        dist[topic][comm] += soft.theta(topic, node) / N;
      }
    }
  }

  public JointDistribution(HardClustering hard1, HardClustering hard2) {
    int n1 = hard1.length();
    int n2 = hard2.length();
    dist = new double[n1][n2];
    N = hard1.N();

    for (int i = 0; i < n1; i++) {
      for (int j = 0; j < n2; j++) {
        dist[i][j] = intersection(hard1, i, hard2, j) / N;
      }
    }
  }
  
  // returns the no. of nodes which are in both communities
  private double intersection(HardClustering hard1, int comm1, 
                              HardClustering hard2, int comm2) {
    TIntArrayList duplicate = new TIntArrayList(hard1.members(comm1));
    duplicate.retainAll(hard2.members(comm2));

    return (double) duplicate.size();
  }
  
  public double distribution(int i, int j) { 
    if (i >= dist.length || j >= dist[i].length) throw new Error("index out of bounds");
    return dist[i][j]; 
  }
  
  public static void main(String[] args) {
    Tester t = new Tester();
    // int[] partition = {1,8,8,5,1,5,2,9,0,9};
    int[] partition = {0, 0, 1, 1, 2, 2, 3, 4, 4, 5};
    CommunityStructure cs = new CommunityStructure(partition);
    HardClustering hc = new HardClustering(cs);
    HardClustering h2 = new HardClustering(cs);
    JointDistribution jd = new JointDistribution(hc, h2);
    
    t.is(jd.distribution(0, 0), 0.2d);
    t.is(jd.distribution(1, 1), 0.2d);
    t.is(jd.distribution(2, 2), 0.2d);
    t.is(jd.distribution(3, 3), 0.1d);
    t.is(jd.distribution(4, 4), 0.2d);
    t.is(jd.distribution(5, 5), 0.1d);
    
    t.is(jd.distribution(0, 1), 0d);
    t.is(jd.distribution(5, 4), 0d);
    
    t.results();
  }    
}