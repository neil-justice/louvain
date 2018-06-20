
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

import com.github.neiljustice.louvain.clustering.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class NMITest {
  private static CommunityStructure cs;
  private static CommunityStructure c2;
  private static int[] p1 = {0, 0, 1, 1, 2, 2, 3, 4, 4, 5};
  private static int[] p2 = {5, 5, 4, 4, 3, 3, 2, 1, 1, 0};
  
  @BeforeClass
  public static void init() {
    cs = new CommunityStructure(p1);
    c2 = new CommunityStructure(p2);
  }

  @Test
  public void checkSelfNMI() {
    assertEquals(NMI.NMI(cs, c2), 1d, 0d);
    assertEquals(NMI.NMI(cs, cs), 1d, 0d);
    assertEquals(NMI.NMI(c2, c2), 1d, 0d);
    assertEquals(NMI.NMI(p1, p2), 1d, 0d);
  }
}