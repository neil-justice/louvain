public class SoftClustering implements Clustering {
  private final int N; // no. of nodes
  private final double[] dist;
  private final int topicCount;
  private final double[][] theta;
  
  public SoftClustering(double[][] theta) {
    this.theta = theta;
    N = theta[0].length;
    topicCount = theta.length;
    dist = new double[topicCount];

    for (int topic = 0; topic < topicCount; topic++) {
      for (int doc = 0; doc < N; doc++) {
        dist[topic] += theta[topic][doc];
      }
      dist[topic] /= N;
    }
  }
  
  @Override
  public double distribution(int index) { return dist[index]; }  
  
  @Override
  public double entropy() { return Entropy.entropy(dist); }
  
  @Override
  public int length() { return topicCount; }
  
  public double theta(int topic, int node) { return theta[topic][node]; }
  
  public int N() { return N; }
}