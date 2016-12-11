import java.util.*;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;

public class LayerMapper {
  private final List<Graph> graphs;
  // maps between communities on L and nodes on L + 1:
  private final List<TIntIntHashMap> layerMaps = new ArrayList<>();
  private int layer = 0;
  
  public LayerMapper(List<Graph> graphs) {
    this.graphs = graphs;
  }
  
  // map from community -> node on layer above
  public TIntIntHashMap map(Graph g) {
    int count = 0;
    layer++;
    boolean[] isFound = new boolean[g.order()];
    TIntIntHashMap map = new TIntIntHashMap();
    // Arrays.sort(communities);
    
    for (int node = 0; node < g.order(); node++) {
      int comm = g.community(node);
      if (!isFound[comm]) {
        map.put(comm, count);
        isFound[comm] = true;
        count++;
      }
    }
    if (map.size() != g.numComms()) throw new Error("Map creation failed: " + 
                                                    g.numComms() + " != " + 
                                                    map.size());
    layerMaps.add(map);
    return map;
  }
  
  // uses the layer maps to assign a community from each layer to the base layer
  // graph.
  public List<int[]> mapAll() {
    List<int[]> rawComms = new ArrayList<int[]>();
    List<int[]> communities = new ArrayList<int[]>();
    communities.add(graphs.get(0).communities());
    
    for (int i = 0; i < layer; i++) {
      rawComms.add(graphs.get(i).communities());
    }
    
    for (int i = 0; i < layer - 1; i++) {
      communities.add(mapToBaseLayer(i , rawComms));
    }
    
    return communities;
  }
  
  // maps layers to each other until the specified layer has been mapped to the
  // base layer
  private int[] mapToBaseLayer(int layer, List<int[]> rawComms) {
    int[] a = mapToNextLayer(graphs.get(layer), layerMaps.get(layer), 
                             rawComms.get(layer + 1));
    layer--;
    
    while (layer >= 0) {
      a = mapToNextLayer(graphs.get(layer), layerMaps.get(layer), a);
      layer--;
    }
    
    return a;
  }
  
  // maps each node in a layer to its community on the layer above it
  private int[] mapToNextLayer(Graph g, TIntIntHashMap map, int[] commsL2) {
    int[] commsL1 = g.communities();
    int[] NL1toCL2 = new int[g.order()];

    for (int nodeL1 = 0; nodeL1 < g.order(); nodeL1++) {
      int commL1 = commsL1[nodeL1];
      int nodeL2 = map.get(commL1);
      int commL2 = commsL2[nodeL2];
      NL1toCL2[nodeL1] = commL2;
    }
    
    return NL1toCL2;
  }
}