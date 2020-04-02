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
import com.github.neiljustice.louvain.util.ArrayUtils;
import gnu.trove.map.hash.TIntIntHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the Louvain method of community detection.
 */
public class LouvainDetector implements Clusterer {
  private final static Logger LOG = LogManager.getLogger(LouvainDetector.class);
  private final List<Graph> graphs = new ArrayList<>();
  private final Maximiser m = new Maximiser();
  private final Random rnd;
  private final LayerMapper mapper = new LayerMapper();
  private int totalMoves = 0;
  private int layer = 0; // current community layer
  private List<int[]> communities;

  private LouvainDetector() {
    rnd = new Random();
  }

  public LouvainDetector(Graph g, long seed) {
    this();
    graphs.add(g);
    rnd.setSeed(seed);
    LOG.info("Using seed " + seed);
  }

  public LouvainDetector(Graph g) {
    this();
    graphs.add(g);
    long seed = rnd.nextLong();
    rnd.setSeed(seed);
    LOG.info("Using seed " + seed);
  }

  @Override
  public List<int[]> run() {
    return run(9999);
  }

  public List<int[]> run(int maxLayers) {
    if (maxLayers <= 0) {
      return null;
    }
    LOG.info("Detecting graph communities...");

    do {
      LOG.info("Round :" + layer);
      totalMoves = m.run(graphs.get(layer));
      if (totalMoves > 0 && maxLayers >= layer) {
        addNewLayer();
      }
    }
    while (totalMoves > 0 && maxLayers >= layer);

    communities = mapper.run();
    return communities;
  }

  public double modularity() {
    return graphs.get(layer).partitioning().modularity();
  }

  public List<int[]> communities() {
    return communities;
  }

  private void addNewLayer() {
    Graph last = graphs.get(layer);
    TIntIntHashMap map = mapper.createLayerMap(last);
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
      LOG.info("seconds taken: " + time);

      return totalMoves;
    }

    private void reassignCommunities() {
      double mod = g.partitioning().modularity();
      double oldMod;
      int moves;
      boolean hasChanged;

      do {
        hasChanged = true;
        oldMod = mod;
        moves = maximiseLocalModularity();
        totalMoves += moves;
        mod = g.partitioning().modularity();
        if (mod - oldMod <= precision) {
          hasChanged = false;
        }
        if (moves == 0) {
          hasChanged = false;
        }
      } while (hasChanged);
      LOG.info("Mod: " + mod +
          " Comms: " + g.partitioning().numComms() +
          " Moves: " + totalMoves);
    }

    private int maximiseLocalModularity() {
      int moves = 0;
      for (int i = 0; i < g.order(); i++) {
        int node = shuffledNodes[i];
        if (makeBestMove(node)) {
          moves++;
        }
      }
      return moves;
    }

    private boolean makeBestMove(int node) {
      double max = 0d;
      int best = -1;

      for (int i = 0; i < g.neighbours(node).size(); i++) {
        int community = g.partitioning().community(g.neighbours(node).get(i));
        double inc = deltaModularity(node, community);
        if (inc > max) {
          max = inc;
          best = community;
        }
      }

      if (best >= 0 && best != g.partitioning().community(node)) {
        g.partitioning().moveToComm(node, best);
        return true;
      } else {
        return false;
      }
    }

    // change in modularity if node is moved to community
    private double deltaModularity(int node, int community) {
      double dnodecomm = g.partitioning().dnodecomm(node, community);
      double ctot = g.partitioning().totDegree(community);
      double wdeg = g.degree(node);
      return dnodecomm - ((ctot * wdeg) / g.m2());
    }
  }
}