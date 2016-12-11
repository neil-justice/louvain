import java.util.*;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;

public class LouvainDetector implements Clusterer {
  private int totalMoves = 0;
  private int layer = 0; // current community layer
  private final List<Graph> graphs = new ArrayList<Graph>();
  private final Maximiser m = new Maximiser();
  private final Random rnd;
  private final LayerMapper mapper = new LayerMapper(graphs);
  
  private LouvainDetector() {
    rnd = new Random();
  }
  
  public LouvainDetector(Graph g, long seed) {
    this();
    graphs.add(g);
    rnd.setSeed(seed);
    System.out.println("Using seed " + seed);
  }
  
  public LouvainDetector(Graph g) {
    this();
    graphs.add(g);
    long seed = rnd.nextLong();
    rnd.setSeed(seed);
    System.out.println("Using seed " + seed);
  }
  
  @Override
  public List<int[]> run() { return run(9999); }
  
  public List<int[]> run(int maxLayers) {
    if (maxLayers <= 0) return null;
    System.out.printf("Detecting graph communities...");
    
    do {
      System.out.printf("Round %d:%n", layer);
      totalMoves = m.run(graphs.get(layer));
      if (totalMoves > 0 && maxLayers >= layer) addNewLayer();
    }
    while (totalMoves > 0 && maxLayers >= layer);
    
    return mapper.mapAll();
  }
  
  public double modularity() { return graphs.get(layer).modularity(); }
  public List<int[]> communities() { return mapper.mapAll(); }

  private void addNewLayer() {
    Graph last = graphs.get(layer);
    TIntIntHashMap map = mapper.map(last);
    layer++;
    Graph coarse = new GraphBuilder().coarseGrain(last, map);
    graphs.add(coarse);
  }

  
  class Maximiser {
    private final double precision = 0.000001;
    private Graph g;
    private int[] shuffledNodes;
    
    private int run(Graph g) {
      this.g = g;
      shuffledNodes = new int[g.order()];
      ArrayUtils.fillRandomly(shuffledNodes);
      totalMoves = 0;
      
      long s1 = System.nanoTime();
      reassignCommunities();
      long e1 = System.nanoTime();
      double time = (e1 - s1) / 1000000000d;
      System.out.println("seconds taken: " + time );
      
      return totalMoves;
    }
    
    private void reassignCommunities() {
      double mod = g.modularity();
      double oldMod;
      int moves;
      boolean hasChanged;
      
      do {
        hasChanged = true;
        oldMod = mod;
        moves = maximiseLocalModularity();
        totalMoves += moves;
        mod = g.modularity();
        if (mod - oldMod <= precision) hasChanged = false;
        if (moves == 0) hasChanged = false;
      } while (hasChanged);
      System.out.printf("Mod: %5f  Comms: %d Moves:  %d%n", 
                          mod , g.numComms(), totalMoves);
    } 
    
    private int maximiseLocalModularity() {
      int moves = 0;
      for (int i = 0; i < g.order(); i++) {
        int node = shuffledNodes[i];
        if (makeBestMove(node)) moves++;
      }
      return moves;
    } 
    
    private boolean makeBestMove(int node) {
      double max = 0d;
      int best = -1;
      
      for (int i = 0; i < g.neighbours(node).size(); i++) {
        int community = g.community(g.neighbours(node).get(i));
        double inc = deltaModularity(node, community);
        if (inc > max) {
          max = inc;
          best = community;
        }
      }
      
      if (best >= 0 && best != g.community(node)) {
        g.moveToComm(node, best);
        return true;
      }
      else return false;
    }

    // change in modularity if node is moved to community
    private double deltaModularity(int node, int community) {
      double dnodecomm = (double) g.dnodecomm(node, community);
      double ctot      = (double) g.totDegree(community);
      double wdeg      = (double) g.degree(node);
      return dnodecomm - ((ctot * wdeg) / g.m2());
    }
  }
}