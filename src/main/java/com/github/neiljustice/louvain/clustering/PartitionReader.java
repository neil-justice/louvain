package com.github.neiljustice.louvain.clustering;

import java.util.*;
import com.github.neiljustice.louvain.file.FileLoader;

public class PartitionReader implements Clusterer {
  private final String del = ":";
  private final String filename;
  private final int order;
  private final int layers;
  private final List<int[]> communities = new ArrayList<>();
  private final List<String> data;
  
  public PartitionReader(String filename) {
    this.filename = filename;
    data = FileLoader.readFile(filename);
    order = data.size();
    layers = data.get(0).split(del).length - 1; // first col is node num.
    reconstruct();
  }
  
  private void reconstruct() {
    for (int layer = 0; layer < layers; layer++) {
      communities.add(new int[order]);
    }
    
    try {
      for (int node = 0; node < order; node++) {
        String[] line = data.get(node).split(del);
        for (int layer = 0; layer < layers + 0; layer++) {
          int comm = Integer.parseInt(line[layer + 1]);
          communities.get(layer)[node] = comm;
        }
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public List<int[]> run() {
    return communities;
  }
}