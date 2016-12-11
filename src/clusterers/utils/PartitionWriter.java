/* Writes the community of each node in each layer to a file. */
import java.nio.file.*;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.*;

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