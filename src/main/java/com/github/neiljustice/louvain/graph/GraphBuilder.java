
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

import com.github.neiljustice.louvain.util.*;

import java.io.*;
import java.util.*;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class GraphBuilder {
  
  private SparseIntMatrix matrix;
  private TIntArrayList[] adjList;
  private int[] degrees;
  private int order = 0;
  private int sizeDbl  = 0;
  private int layer = 0;
  
  public Graph fromFile(String filename) {
    return fromFile(filename, false);
  }
  
  public Graph fromFile(File file) {
    try {
      readAll(file, ",");
    } catch(NumberFormatException e) {
      e.printStackTrace();
      throw new Error("invalid file format");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new Error("file not found");
    } catch (IOException e) {
      e.printStackTrace();
      throw new Error("IO error");
    }
    return build();
  }  
  
  public Graph fromFile(String filename, boolean needsReIndexing) {
    try {
      if (!needsReIndexing) readAll(new File(filename), ",");
      else readAllNonIntIndexed(new File(filename), ",");
    } catch(NumberFormatException e) {
      e.printStackTrace();
      throw new Error("invalid file format");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new Error("file not found");
    } catch (IOException e) {
      e.printStackTrace();
      throw new Error("IO error");
    }
    return build();
  }
  
  private void readAll(File file, String delimiter) 
  throws NumberFormatException, FileNotFoundException, IOException {
    
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    order = getOrder(file, delimiter);
    initialise();
    
    while ((line = reader.readLine()) != null) {
      String[] splitLine = line.split(delimiter);
      int n1 = Integer.parseInt(splitLine[0]);
      int n2 = Integer.parseInt(splitLine[1]);
      int weight = Integer.parseInt(splitLine[2]);

      if (matrix.get(n1, n2) == 0 && matrix.get(n2, n1) == 0) insertEdgeSym(n1, n2, weight);
    }
    reader.close();
    if (!matrix.isSymmetric()) throw new Error("constructed asymmetric matrix");
  }
  
  // gets the no. of nodes from the file
  private int getOrder(File file, String delimiter)
  throws NumberFormatException, FileNotFoundException, IOException {
    
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    int max = 0;
    while ((line = reader.readLine()) != null) {
      String[] splitLine = line.split(delimiter);
      int n1 = Integer.parseInt(splitLine[0]);
      int n2 = Integer.parseInt(splitLine[1]);
      if (n1 > max) max = n1;
      if (n2 > max) max = n2;
    }
    max++;
    reader.close();
    return max;
  }
  
  private void readAllNonIntIndexed(File file, String delimiter) 
  throws NumberFormatException, FileNotFoundException, IOException {
    
    BufferedReader reader = new BufferedReader(new FileReader(file));

    String line;
    TObjectIntHashMap index = buildIndex(file, delimiter);
    order = index.size();
    initialise();
    
    while ((line = reader.readLine()) != null) {
      String[] splitLine = line.split(delimiter);
      String srcId = splitLine[0];
      String dstId = splitLine[1];
      int weight = Integer.parseInt(splitLine[2]);
      int n1 = index.get(srcId);
      int n2 = index.get(dstId);
      
      if (matrix.get(n1, n2) != 0 || matrix.get(n2, n1) != 0) {
        throw new Error("duplicate val at " + srcId + " " + dstId);
      }
      insertEdgeSym(n1, n2, weight);
    }
    
    if (!matrix.isSymmetric()) throw new Error("constructed asymmetric matrix");
    reader.close();
  }
  
  private TObjectIntHashMap<String> buildIndex(File file, String delimiter) 
  throws NumberFormatException, FileNotFoundException, IOException {
    
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    int i = 0;
    TObjectIntHashMap<String> index = new TObjectIntHashMap<String>();
    
    while ((line = reader.readLine()) != null) {
      String[] splitLine = line.split(delimiter);
      String n1 = splitLine[0];
      String n2 = splitLine[1];
      if (!index.contains(n1)) {
        index.put(n1, i);
        i++;
      }
      if (!index.contains(n2)) {
        index.put(n2, i);
        i++;
      }
    }
    reader.close();
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
    if (n1 != n2) insertEdge(n2, n1, weight);
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
    if (n1 >= order) throw new Error("" + n1 " >= " + order);
    if (n2 >= order) throw new Error("" + n2 " >= " + order);
    if (matrix.get(n1, n2) != 0) throw new Error("already exists");
    if (matrix == null) throw new Error("initialise first");
    insertEdgeSym(n1, n2, weight);
    
    return this;
  }
  
  public Graph coarseGrain(Graph g, TIntIntHashMap map) {
    this.order = g.partitioning().numComms();
    this.layer = g.layer() + 1;
    initialise();
    int sum = 0;
    
    for ( SparseIntMatrix.Iterator it = g.partitioning().commWeightIterator(); it.hasNext(); ) {
      it.advance();
      int weight = it.value();
      if (weight != 0) {
        int n1 = map.get((int) it.x());
        int n2 = map.get((int) it.y());
        insertEdge(n1, n2, weight);
        sum += weight;
      }
    }
    
    if (!matrix.isSymmetric()) throw new Error("asymmetric matrix");
    if (sum != g.size() * 2) throw new Error("builder recieved wrong weights: " + sum + " " + (g.size() * 2));
    if (sum != sizeDbl) throw new Error("Coarse-grain error: " + sum + " != " + sizeDbl);
    return build();
  }
  
  public Graph fromCommunity(Graph g, TIntArrayList members) {
    this.order = members.size();
    initialise();
    
    for (int newNode = 0; newNode < order; newNode++) {
      int oldNode = members.get(newNode);
      for (int i = 0; i < g.neighbours(oldNode).size(); i++) {
        int oldNeigh = g.neighbours(oldNode).get(i);
        int newNeigh = -1;
        if ((newNeigh = members.indexOf(oldNeigh)) != -1) {
          insertEdge(newNode, newNeigh, g.weight(oldNode, oldNeigh));
        }
      }
    }
    if (!matrix.isSymmetric()) throw new Error("asymmetric matrix");
    return build();
  }
  
  public Graph erdosRenyi(int order, double prob) {
    this.order = order;
    Random rnd = new Random();
    initialise();
    
    for (int n1 = 0; n1 < order; n1++) {
      for (int n2 = 0; n2 < order; n2++) {
        if (matrix.get(n2, n1) == 0 && n1 != n2 && rnd.nextDouble() < prob) {
          insertEdgeSym(n1, n2, 1);
        }
      }
    }
    if (!matrix.isSymmetric()) throw new Error("asymmetric matrix");
    return build();    
  }
  
  public SparseIntMatrix matrix() { return matrix; }
  public TIntArrayList[] adjList() { return adjList; }
  public int[] degrees() { return degrees; }
  public int sizeDbl() { return sizeDbl; }
  public int order() { return order; }
  public int layer() { return layer; }
  
  public Graph build() {
    return new Graph(this);
  }  
}