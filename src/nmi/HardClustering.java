import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;

import tester.Tester;

public class HardClustering implements Clustering {
  private final int N; // no. of nodes
  private final double[] dist;
  private final int numComms;
  private final TIntArrayList[] members;
  private final int[] community;
  
  public HardClustering(CommunityStructure structure) {
    members = structure.communityMembers();
    community = structure.communities();
    N = structure.order();
    numComms = structure.numComms();
    
    dist = new double[numComms];
    
    for (int comm = 0; comm < numComms; comm++) {
      dist[comm] = (double) structure.communitySize(comm) / N;
    }
  }
  
  @Override
  public double distribution(int index) { 
    if (index >= numComms) throw new Error("index out of bounds");
    return dist[index]; 
  }
  
  @Override
  public double entropy() { return Entropy.entropy(dist); }
  
  @Override
  public int length() { return numComms; }
  
  public int N() { return N; }
  
  public int community(int comm) { 
    if (comm >= numComms) throw new Error("index out of bounds");
    return community[comm];
  }
  
  public TIntArrayList members(int comm) {
    if (comm >= numComms) throw new Error("index out of bounds");
    return members[comm];
  }
  
  public static void main(String[] args) {
    Tester t = new Tester();
    int[] partition = {1,8,8,5,1,5,2,9,0,9};
    CommunityStructure cs = new CommunityStructure(partition);
    HardClustering hc = new HardClustering(cs);
    
    t.is(hc.length(), 6);
    t.is(hc.N(), 10);
    t.is(hc.distribution(0), 0.2d);
    t.is(hc.distribution(1), 0.2d);
    t.is(hc.distribution(2), 0.2d);
    t.is(hc.distribution(3), 0.1d);
    t.is(hc.distribution(4), 0.2d);
    t.is(hc.distribution(5), 0.1d);
    
    t.is(hc.members(0).size(), 2);
    t.is(hc.members(1).size(), 2);
    t.is(hc.members(2).size(), 2);
    t.is(hc.members(3).size(), 1);
    t.is(hc.members(4).size(), 2);
    t.is(hc.members(5).size(), 1);
    
    t.results();
  }  
}