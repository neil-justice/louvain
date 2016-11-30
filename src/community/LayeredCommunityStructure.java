import java.util.*;

public class LayeredCommunityStructure {
  private final List<CommunityStructure> layerStructures;
  private final int layers;
  
  public LayeredCommunityStructure(List<int[]> communities) {
    layers = communities.size();
    layerStructures = new ArrayList<CommunityStructure>();
    for (int layer = 0; layer < layers; layer++) {
      layerStructures.add(new CommunityStructure(communities.get(layer)));
    }
  }
  
  public CommunityStructure layer(int layer) {
    if (layer >= layers) throw new Error("layer index out of bounds");
    return layerStructures.get(layer);
  }
  
  public int layers() { return layers; }
}
