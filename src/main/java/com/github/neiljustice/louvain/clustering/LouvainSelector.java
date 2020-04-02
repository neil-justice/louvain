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

import com.github.neiljustice.louvain.graph.Graph;
import com.github.neiljustice.louvain.graph.GraphBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Runs the louvain detector the set number of times and writes out the
 * partition data.
 */
public class LouvainSelector implements Clusterer {
  private final static Logger LOG = LogManager.getLogger(LouvainSelector.class);

  private final Random rnd = new Random();
  private final String dir;
  private final PartitionWriter writer;

  public LouvainSelector(String dir) {
    this.dir = dir;
    writer = new PartitionWriter(dir);
  }

  @Override
  public List<int[]> run() {
    return run(10);
  }

  public List<int[]> run(int times) {
    long seed;
    double maxMod = 0d;
    double mod;
    List<int[]> output = new ArrayList<>();

    LOG.info("Running " + times + " times:");
    for (int i = 0; i < times; i++) {
      seed = rnd.nextLong();
      LOG.info("Run " + i + ":");
      Graph g = new GraphBuilder().fromFile(dir + "graph.csv");
      LouvainDetector detector = new LouvainDetector(g, seed);
      detector.run();
      mod = detector.modularity();
      if (mod > maxMod) {
        maxMod = mod;
        output = detector.communities();
      }
    }

    LOG.info("highest mod was " + maxMod);
    write(output);
    return output;
  }

  private void write(List<int[]> communities) {
    writer.write(communities, dir + "best-louvain.csv");
  }
}