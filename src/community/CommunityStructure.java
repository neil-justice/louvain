import java.util.*;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;

import tester.Tester;

public class CommunityStructure {
  
  private final int order;
  // community of each node
  private final int[] community;
  // size of each comm
  private final int[] communitySize;
  // keeps track of the members of each comm:
  private final TIntArrayList[] communityMembers;
  
  private final int numComms;

  public CommunityStructure(int[] partitioning) {
    order = partitioning.length;
    community = new int[order];
    
    TIntIntHashMap commToIndex = reIndexCommunities(partitioning);
    numComms = commToIndex.size();
    
    communitySize = new int[numComms];
    communityMembers = new TIntArrayList[numComms];
    
    for (int comm = 0; comm < numComms; comm++) {
      communityMembers[comm] = new TIntArrayList();
    }
    
    for (int node = 0; node < order; node++) {
      int comm = commToIndex.get(partitioning[node]);
      community[node] = comm;
      communitySize[comm]++;
      communityMembers[comm].add(node);
    }
  }
  
  // Re-index the community IDs consecutively from 0:
  private TIntIntHashMap reIndexCommunities(int[] partitioning) {
    TIntIntHashMap commToIndex = new TIntIntHashMap();
    boolean[] commExists = new boolean[order];
    int index = 0;
    
    for (int node = 0; node < order; node++) {
      int comm = partitioning[node];
      if (commExists[comm] == false) {
        commExists[comm] = true;
        commToIndex.put(comm, index);
        index++;
      } 
    }
    
    return commToIndex;
  }

  public int order() { return order; }
  
  public int numComms() { return numComms; }
  
  public int[] communities() { return community; }
  public int community(int node) { 
    if (node >= order) throw new Error("node index out of bounds");
    return community[node]; 
  }
  
  public int[] communitySizes() { return communitySize; }
  public int communitySize(int comm) { 
    if (comm >= numComms) throw new Error("node index out of bounds");
    return communitySize[comm]; 
  }
  
  public TIntArrayList[] communityMembers() { return communityMembers; }
  public TIntArrayList communityMembers(int comm) {
    if (comm >= numComms) throw new Error("node index out of bounds");
    return communityMembers[comm];
  }
  
  public static void main(String[] args) {
    Tester t = new Tester();
    int[] partition = {1,8,8,5,1,5,2,9,0,9};
    CommunityStructure cs = new CommunityStructure(partition);
    CommunityStructure c2 = new CommunityStructure(partition);
    
    t.is(cs.community(0), 0);
    t.is(cs.community(1), 1);
    t.is(cs.community(2), 1);
    t.is(cs.community(3), 2);
    t.is(cs.community(4), 0);
    t.is(cs.community(5), 2);
    t.is(cs.community(6), 3);
    t.is(cs.community(7), 4);
    t.is(cs.community(8), 5);
    t.is(cs.community(9), 4);
    
    t.is(cs.communitySize(0), cs.communityMembers(0).size());
    t.is(cs.communitySize(1), cs.communityMembers(1).size());
    t.is(cs.communitySize(2), cs.communityMembers(2).size());
    t.is(cs.communitySize(3), cs.communityMembers(3).size());
    t.is(cs.communitySize(4), cs.communityMembers(4).size());
    t.is(cs.communitySize(5), cs.communityMembers(5).size());
    
    t.is(cs.numComms(), 6);
    t.is(NMI.NMI(cs, c2), 1d);
    
    t.results();
  }
}
