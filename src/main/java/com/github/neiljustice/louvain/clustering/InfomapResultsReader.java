package com.github.neiljustice.louvain.clustering;

import java.util.*;
import java.io.*;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Reads the .tree files produced by the Infomap community detection algorithm.
 * See <a href="http://www.mapequation.org/code.html" target="_top>
 * The official Infomap website</a> for more on Infomap.
 */
public class InfomapResultsReader implements Clusterer {
  private int nodeCount = 0;
  private int layerCount = 0;
  private final File file;
  private final List<int[]> communities = new ArrayList<int[]>();
  private final TIntObjectHashMap<String> commTags = new TIntObjectHashMap<>();
  
  public InfomapResultsReader(String filename) {
    file = new File(filename);
  }
  
  @Override
  public List<int[]> run() {
    read();
    process();
    return communities;
  }
  
  public void read(){
    try {
      getInfo();
      initialise();
      readLines();
    } catch(NumberFormatException e) {
      throw new Error("invalid file format");
    } catch (FileNotFoundException e) {
      throw new Error("file not found");
    } catch (IOException e) {
      throw new Error("IO error");
    }
  }
  
  // reads lines of format:
  // 1:1:1 0.00244731 "83698" 83698
  // and puts node ID and community info into <int, String> map
  private void readLines() 
  throws NumberFormatException, FileNotFoundException, IOException {
    
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    
    while ((line = reader.readLine()) != null) {
      if (!line.startsWith("#")) {      
        String[] splitLine = line.split(" ");
        int node = Integer.parseInt(splitLine[3]);
        String layers = splitLine[0].substring(0, splitLine[0].lastIndexOf(":"));
        commTags.put(node, layers);
      }
    }
    reader.close();
  }
  
  // gets the no. of nodes and layers from the file
  private void getInfo()
  throws NumberFormatException, FileNotFoundException, IOException {
    
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = reader.readLine()) != null) {
      if (!line.startsWith("#")) {
        String[] splitLine = line.split(" ");
        int node = Integer.parseInt(splitLine[3]);
        int layers = splitLine[0].split(":").length - 1;
        if (node > nodeCount) nodeCount = node;
        if (layers > layerCount) layerCount = layers;
      }
    }
    nodeCount++;
    reader.close();
  }
  
  private void initialise() {
    for (int layer = 0; layer < layerCount; layer++) {
      int[] comms = new int[nodeCount];
      communities.add(comms);
    }
  }
  
  private void print() {
    for (int node = 0; node < nodeCount; node++) {
      System.out.print(node + ":");
      for (int layer = 0; layer < layerCount; layer++) {
        System.out.print(communities.get(layer)[node] + ":");
      }
      System.out.println();
    }
  }
  
  private void process() {
    for (int layer = 0; layer < layerCount; layer++) {
      assignCommunityIDs(layer);
    }
  }
  
  private void assignCommunityIDs(int layer) {
    TObjectIntHashMap<String> map = new TObjectIntHashMap<String>();
    int count = 0;
    int[] comms = communities.get(layer);
    
    for (int node = 0; node < nodeCount; node++) {
      String tag = truncateTag(commTags.get(node), layer);
      if (!map.containsKey(tag)) {
        map.put(tag, count);
        count++;
      }
    }
    
    for (int node = 0; node < nodeCount; node++) {
      String tag = truncateTag(commTags.get(node), layer);
      comms[node] = map.get(tag);
    }
  }
  
  // infomap communities are ordered as: 'top:high:mid:low'  if layer == 2, this would
  // cut off ':mid:low', returning 'top:high'
  private String truncateTag(String tag, int layer) {
    for (int i = 0; i < layer; i++) {
      int t = tag.lastIndexOf(":");
      if (t != -1) tag = tag.substring(0, t);
    }
    return tag;
  }
}