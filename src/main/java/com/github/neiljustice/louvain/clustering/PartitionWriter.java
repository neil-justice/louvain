
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

import java.nio.file.*;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.*;

/** 
 * Writes the community of each node in each layer to a file.
 */
public class PartitionWriter {
  private final String del = ":";
  private final String dir;
  private int order;
  private int layers;
  private List<int[]> communities;
  
  public PartitionWriter(String dir) {
    this.dir = dir;
  }
  
  public void write(List<int[]> communities, String filename) {
    this.communities = communities;
    order = communities.get(0).length;
    layers = communities.size();
    writeOut(filename);
  }
  
  private List<String> prepareData() {
    List<String> data = new ArrayList<String>();
    
    for (int node = 0; node < order; node++) {
      StringBuilder builder = new StringBuilder();
      builder.append(node);
      for (int layer = 0; layer < layers; layer++) {
        builder.append(del);
        builder.append(communities.get(layer)[node]);
      }
      data.add(builder.toString());
    }
    return data;
  }
  
  private void writeOut(String filename) {
    Path filepath = Paths.get(dir + filename);
    List<String> data = prepareData();
    
    try {
      Files.write(filepath, data, Charset.forName("UTF-8"));
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }  
}