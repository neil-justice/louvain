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

package com.github.neiljustice.louvain.graph;

import com.github.neiljustice.louvain.exception.LouvainException;
import com.github.neiljustice.louvain.util.SparseIntMatrix;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GraphBuilder {

  private SparseIntMatrix matrix;
  private TIntArrayList[] adjList;
  private int[] degrees;
  private int order = 0;
  private int sizeDbl = 0;
  private int layer = 0;

  public Graph fromFile(String filename) {
    return fromFile(filename, false);
  }

  public Graph fromFile(File file) {
    readAll(file, ",", StandardCharsets.UTF_8);
    return build();
  }

  public Graph fromFile(String filename, boolean needsReIndexing) {
    return fromFile(filename, needsReIndexing, ",", StandardCharsets.UTF_8);
  }

  public Graph fromFile(String filename, boolean needsReIndexing, String delimiter, Charset charset) {
    if (!needsReIndexing) {
      readAll(new File(filename), delimiter, charset);
    } else {
      readAllNonIntIndexed(new File(filename), delimiter, charset);
    }
    return build();
  }

  private void readAll(File file, String delimiter, Charset charset) {
    String line;
    order = getOrder(file, delimiter, charset);
    initialise();

    try (FileInputStream fis = new FileInputStream(file);
         InputStreamReader isr = new InputStreamReader(fis, charset);
         BufferedReader reader = new BufferedReader(isr)) {
      while ((line = reader.readLine()) != null) {
        final String[] splitLine = line.split(delimiter);
        final int n1 = Integer.parseInt(splitLine[0]);
        final int n2 = Integer.parseInt(splitLine[1]);
        final int weight = Integer.parseInt(splitLine[2]);

        if (matrix.get(n1, n2) == 0 && matrix.get(n2, n1) == 0) {
          insertEdgeSym(n1, n2, weight);
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

    if (!matrix.isSymmetric()) {
      throw new LouvainException("constructed asymmetric matrix");
    }
  }

  /**
   * Get the number of nodes from the file.
   */
  private int getOrder(File file, String delimiter, Charset charset) {
    String line;
    int max = 0;

    try (FileInputStream fis = new FileInputStream(file);
         InputStreamReader isr = new InputStreamReader(fis, charset);
         BufferedReader reader = new BufferedReader(isr)) {
      while ((line = reader.readLine()) != null) {
        final String[] splitLine = line.split(delimiter);
        final int n1 = Integer.parseInt(splitLine[0]);
        final int n2 = Integer.parseInt(splitLine[1]);
        if (n1 > max) {
          max = n1;
        }
        if (n2 > max) {
          max = n2;
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

    max++;
    return max;
  }

  private void readAllNonIntIndexed(File file, String delimiter, Charset charset) {
    final TObjectIntHashMap<String> index = buildIndex(file, delimiter, charset);
    String line;
    order = index.size();
    initialise();

    try (FileInputStream fis = new FileInputStream(file);
         InputStreamReader isr = new InputStreamReader(fis, charset);
         BufferedReader reader = new BufferedReader(isr)) {
      while ((line = reader.readLine()) != null) {
        final String[] splitLine = line.split(delimiter);
        final String srcId = splitLine[0];
        final String dstId = splitLine[1];
        final int weight = Integer.parseInt(splitLine[2]);
        final int n1 = index.get(srcId);
        final int n2 = index.get(dstId);

        if (matrix.get(n1, n2) != 0 || matrix.get(n2, n1) != 0) {
          throw new LouvainException("duplicate val at " + srcId + " " + dstId);
        }
        insertEdgeSym(n1, n2, weight);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

    if (!matrix.isSymmetric()) {
      throw new LouvainException("constructed asymmetric matrix");
    }
  }

  private TObjectIntHashMap<String> buildIndex(File file, String delimiter, Charset charset) {
    final TObjectIntHashMap<String> index = new TObjectIntHashMap<>();
    String line;
    int i = 0;

    try (FileInputStream fis = new FileInputStream(file);
         InputStreamReader isr = new InputStreamReader(fis, charset);
         BufferedReader reader = new BufferedReader(isr)) {
      while ((line = reader.readLine()) != null) {
        final String[] splitLine = line.split(delimiter);
        final String n1 = splitLine[0];
        final String n2 = splitLine[1];
        if (!index.contains(n1)) {
          index.put(n1, i);
          i++;
        }
        if (!index.contains(n2)) {
          index.put(n2, i);
          i++;
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

    return index;
  }

  private void initialise() {
    matrix = new SparseIntMatrix(order);
    degrees = new int[order];
    adjList = new TIntArrayList[order];
    for (int i = 0; i < order; i++) {
      adjList[i] = new TIntArrayList();
    }
  }

  //inserts symmetrical edge
  private void insertEdgeSym(int n1, int n2, int weight) {
    insertEdge(n1, n2, weight);
    if (n1 != n2) {
      insertEdge(n2, n1, weight);
    }
  }

  private void insertEdge(int n1, int n2, int weight) {
    matrix.set(n1, n2, weight);
    adjList[n1].add(n2);
    degrees[n1] += weight;
    sizeDbl += weight;
  }

  public GraphBuilder setSize(int order) {
    this.order = order;
    initialise();

    return this;
  }

  public GraphBuilder addEdge(int n1, int n2, int weight) {
    if (n1 >= order) {
      throw new LouvainException("" + n1 + " >= " + order);
    }
    if (n2 >= order) {
      throw new LouvainException("" + n2 + " >= " + order);
    }
    if (matrix == null) {
      throw new LouvainException("initialise first");
    }
    if (matrix.get(n1, n2) != 0) {
      throw new LouvainException("already exists");
    }
    insertEdgeSym(n1, n2, weight);

    return this;
  }

  public Graph coarseGrain(Graph g, TIntIntHashMap map) {
    this.order = g.partitioning().numComms();
    this.layer = g.layer() + 1;
    initialise();
    int sum = 0;

    for (final SparseIntMatrix.Iterator it = g.partitioning().commWeightIterator(); it.hasNext(); ) {
      it.advance();
      final int weight = it.value();
      if (weight != 0) {
        final int n1 = map.get(it.x());
        final int n2 = map.get(it.y());
        insertEdge(n1, n2, weight);
        sum += weight;
      }
    }

    if (!matrix.isSymmetric()) {
      throw new LouvainException("asymmetric matrix");
    }
    if (sum != g.size() * 2) {
      throw new LouvainException("builder recieved wrong weights: " + sum + " " + (g.size() * 2));
    }
    if (sum != sizeDbl) {
      throw new LouvainException("Coarse-grain error: " + sum + " != " + sizeDbl);
    }
    return build();
  }

  public Graph fromCommunity(Graph g, TIntArrayList members) {
    this.order = members.size();
    initialise();

    for (int newNode = 0; newNode < order; newNode++) {
      final int oldNode = members.get(newNode);
      for (int i = 0; i < g.neighbours(oldNode).size(); i++) {
        final int oldNeigh = g.neighbours(oldNode).get(i);
        final int newNeigh;
        if ((newNeigh = members.indexOf(oldNeigh)) != -1) {
          insertEdge(newNode, newNeigh, g.weight(oldNode, oldNeigh));
        }
      }
    }
    if (!matrix.isSymmetric()) {
      throw new LouvainException("asymmetric matrix");
    }
    return build();
  }

  public Graph erdosRenyi(int order, double prob) {
    this.order = order;
    final Random rnd = new Random();
    initialise();

    for (int n1 = 0; n1 < order; n1++) {
      for (int n2 = 0; n2 < order; n2++) {
        if (matrix.get(n2, n1) == 0 && n1 != n2 && rnd.nextDouble() < prob) {
          insertEdgeSym(n1, n2, 1);
        }
      }
    }
    if (!matrix.isSymmetric()) {
      throw new LouvainException("asymmetric matrix");
    }
    return build();
  }

  public SparseIntMatrix matrix() {
    return matrix;
  }

  public TIntArrayList[] adjList() {
    return adjList;
  }

  public int[] degrees() {
    return degrees;
  }

  public int sizeDbl() {
    return sizeDbl;
  }

  public int order() {
    return order;
  }

  public int layer() {
    return layer;
  }

  public Graph build() {
    return new Graph(this);
  }
}
