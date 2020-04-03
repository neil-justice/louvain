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

import com.github.neiljustice.louvain.file.FileLoader;

import java.util.ArrayList;
import java.util.List;

public class PartitionReader implements Clusterer {
  private static final String DEL = ":";

  private String filename;

  public PartitionReader(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  @Override
  public LayeredCommunityStructure cluster() {
    final List<String> data = FileLoader.readFile(filename);
    final List<int[]> communities = new ArrayList<>();
    final int order = data.size();
    final int layers = data.get(0).split(DEL).length - 1; // first col is node num.

    for (int layer = 0; layer < layers; layer++) {
      communities.add(new int[order]);
    }

    try {
      for (int node = 0; node < order; node++) {
        final String[] line = data.get(node).split(DEL);
        for (int layer = 0; layer < layers; layer++) {
          final int comm = Integer.parseInt(line[layer + 1]);
          communities.get(layer)[node] = comm;
        }
      }

      return new LayeredCommunityStructure(communities);
    } catch (NumberFormatException e) {
      throw new IllegalStateException(e);
    }
  }
}