package com.github.neiljustice.louvain.clustering;

import java.util.List;

/**
 * Interface which all non-fuzzy community detectors or clusterers inherit from.
 */
public interface Clusterer {
  public List<int[]> run();
}