/* Runs the louvain detector the set number of times and writes out the
 * partition data. */
import java.util.*;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.io.IOException;

public class LouvainSelector implements Clusterer {
  private final Random rnd = new Random();
  private final String dir;
  private final PartitionWriter writer;
  
  public LouvainSelector(String dir) {
    this.dir = dir;
    writer = new PartitionWriter(dir);
  }
  
  @Override
  public List<int[]> run() { return run(10); }  
  
  public List<int[]> run(int times) {
    long seed;
    double maxMod = 0d;
    double mod = 0d;
    List<int[]> output = new ArrayList<int[]>();
    
    System.out.println("Running " + times + " times:");
    for (int i = 0; i < times; i++) {
      seed = rnd.nextLong();
      System.out.println("Run " + i + ":");
      Graph g = new GraphBuilder().fromFile(dir + "graph.csv");
      LouvainDetector detector = new LouvainDetector(g, seed);
      detector.run();
      mod = detector.modularity();
      if (mod > maxMod) {
        maxMod = mod;
        output = detector.communities();
      }
    }
    
    System.out.println("highest mod was " + maxMod);
    write(output);
    return output;
  }
  
  private void write(List<int[]> communities) {
    writer.write(communities, dir + "best-louvain.csv");
  }
}