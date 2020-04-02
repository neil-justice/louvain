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

package com.github.neiljustice.louvain.nmi;

import com.github.neiljustice.louvain.clustering.CommunityStructure;

public class NMI {

  public static double NMI(int[] p1, int[] p2) {
    return NMI(new CommunityStructure(p1), new CommunityStructure(p2));
  }

  public static double NMI(CommunityStructure structure, double[][] fuzzyPartitioning) {
    SoftClustering dist1 = new SoftClustering(fuzzyPartitioning);
    HardClustering dist2 = new HardClustering(structure);
    JointDistribution joint = new JointDistribution(dist1, dist2);

    return run(dist1, dist2, joint);
  }

  public static double NMI(CommunityStructure s1, CommunityStructure s2) {
    HardClustering dist1 = new HardClustering(s1);
    HardClustering dist2 = new HardClustering(s2);
    JointDistribution joint = new JointDistribution(dist1, dist2);

    return run(dist1, dist2, joint);
  }

  public static double NMI(HardClustering dist1, HardClustering dist2) {
    JointDistribution joint = new JointDistribution(dist1, dist2);

    return run(dist1, dist2, joint);
  }

  private static double run(Clustering dist1, Clustering dist2,
                            JointDistribution joint) {
    double MI = MI(dist1, dist2, joint);
    return normalise(MI, dist1, dist2);
  }

  private static double MI(Clustering dist1, Clustering dist2,
                           JointDistribution joint) {
    double MI = 0d;
    for (int i = 0; i < dist1.length(); i++) {
      double d1 = dist1.distribution(i);
      for (int j = 0; j < dist2.length(); j++) {
        double jointDist = joint.distribution(i, j);
        double d2 = dist2.distribution(j);
        MI += jointDist * log(jointDist / (d1 * d2));
      }
    }
    return MI / Math.log(2);
  }

  private static double normalise(double MI, Clustering dist1, Clustering dist2) {
    double e1 = dist1.entropy();
    double e2 = dist2.entropy();
    return MI / ((e1 + e2) * 0.5);
  }

  // in information theory, 0 log(0) == 0.
  // this happens because often comm1 and comm2 have no nodes in common
  private static double log(double val) {
    if (val == 0) {
      return 0;
    } else {
      return Math.log(val);
    }
  }
}