
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