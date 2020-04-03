# Louvain

Java implementation of the Louvain method for community detection.  

Input: weighted, undirected graph, defined in a CSV file as a list of edges.  See the graphs at src/test/resources for examples.  Nodes may be named or indexed, and the indexes do not have to start at zero or be continuous.

# Features

* Read graphs from files, build manually, or generate random [Erdős–Rényi graphs](https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93R%C3%A9nyi_model).
* Read [infomap](https://www.mapequation.org/) output files.
* Compare different clusterings of a graph using Normalised Mutual Information ([NMI](https://en.wikipedia.org/wiki/Mutual_information)).
* Assign nodes to random communities (for baseline comparisons of a clustering).
* Read and write community structures to disk.

# Examples

Load a graph file and detect communities using the Louvain method, and print the modularity of the detected communities:
```java
    final Graph g = new GraphBuilder().fromFile("src/test/resources/graphs/arxiv.txt", true);
    final LouvainDetector ld = new LouvainDetector(g);
    final LayeredCommunityStructure cs = ld.cluster();
    System.out.println(ld.modularity());
```

Load a graph file and detect communities using the Louvain method, then compare each layer with each layer from a loaded infomap file:
```java
    final Graph g = new GraphBuilder().fromFile("src/test/resources/graphs/arxiv.txt", true);
    final LouvainDetector ld = new LouvainDetector(g);
    final LayeredCommunityStructure cs = ld.cluster();

    final InfomapResultsReader irr = new InfomapResultsReader("graphs/infomap.tree");
    final LayeredCommunityStructure cs2 = irr.cluster();

     for (int i = 0; i < cs.layers(); i++) {
       for (int j = 0; j < cs2.layers(); j++) {
         System.out.printf("%.03f ", NMI.NMI(cs.layer(i), cs2.layer(j)));
       }
       System.out.println();
     }
```

Generate an Erdős–Rényi graph, detect communities using the Louvain method, and compare the NMI of those communities to randomly-assigned communities of the same size:
```java
    final Graph g = new GraphBuilder().erdosRenyi(1000, 0.1);
    final LouvainDetector ld = new LouvainDetector(g);
    final LayeredCommunityStructure cs = ld.cluster();

    RandomCommunityAssigner rca = new RandomCommunityAssigner(cs);
    final LayeredCommunityStructure cs2 = rca.cluster();

     for (int i = 0; i < cs.layers(); i++) {
       for (int j = 0; j < cs2.layers(); j++) {
         System.out.printf("%.03f ", NMI.NMI(cs.layer(i), cs2.layer(j)));
       }
       System.out.println();
     }
```

Run the Louvain community detection 10 times, and write the clustering with the best modularity to a file:
```java
    final Graph g = new GraphBuilder().erdosRenyi(1000, 0.1);
    final LouvainSelector ls = new LouvainSelector("src/test/resources/graphs/arxiv.txt", "out.csv");
    final LayeredCommunityStructure cs = ls.cluster(10);
```

# Licensing

License: MIT

| Dependency  | License |
| ------------- | ------------- |
| log4j2  | Apache 2.0  |
| trove4j  | LGPL  |
| junit  | Eclipse 1.0  |
