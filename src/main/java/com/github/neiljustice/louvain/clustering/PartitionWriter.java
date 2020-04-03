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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes the community of each node in each layer to a file.
 */
public class PartitionWriter {
  private static final String DEL = ":";

  public void write(List<int[]> communities, String filename) {
    final int order = communities.get(0).length;
    final int layers = communities.size();
    writeOut(filename, communities, order, layers);
  }

  private List<String> prepareData(List<int[]> communities, int order, int layers) {
    final List<String> data = new ArrayList<>();

    for (int node = 0; node < order; node++) {
      final StringBuilder builder = new StringBuilder();
      builder.append(node);
      for (int layer = 0; layer < layers; layer++) {
        builder.append(DEL);
        builder.append(communities.get(layer)[node]);
      }
      data.add(builder.toString());
    }
    return data;
  }

  private void writeOut(String filename, List<int[]> communities, int order, int layers) {
    final Path filepath = Paths.get(filename);
    final List<String> data = prepareData(communities, order, layers);

    try {
      Files.write(filepath, data, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}