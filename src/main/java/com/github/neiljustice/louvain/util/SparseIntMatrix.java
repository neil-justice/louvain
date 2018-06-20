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

package com.github.neiljustice.louvain.util;
import gnu.trove.map.hash.TLongIntHashMap;
import gnu.trove.iterator.TLongIntIterator;


/**
 * Sparse square matrix using hashmap.
 */
public class SparseIntMatrix {
  private final TLongIntHashMap map;
  private final long size;
  private boolean compressed = false;
  
  public SparseIntMatrix(int size) {
    this.size = (long) size;
    map = new TLongIntHashMap();
  }
  
  public SparseIntMatrix(SparseIntMatrix m) {
    this.size = (long) m.size();
    map = m.copyMap();
  }
  
  public int get (int x, int y) { 
    return map.get((long) x * size + (long) y);
  }
  
  public void set (int x, int y, int val) { 
    map.put((long) x * size + (long) y, val);
    compressed = false;
  }
  
  public void add (int x, int y, int val) {
    set(x, y, get(x, y) + val);
  }
  
  private boolean isNonZero(long key, int val) {
    if (val == 0) return false;
    return true;
  }
  
  private void compress() { map.retainEntries(this::isNonZero); }
  
  public int size() { return (int) size; }
  
  public SparseIntMatrix.Iterator iterator() { 
    if (compressed == false) {
      compress();
      compressed = true;
    }
    return new Iterator();
  }
  
  public TLongIntHashMap copyMap() { return new TLongIntHashMap(map); }
  
  public boolean isSymmetric() {
    for ( SparseIntMatrix.Iterator it = iterator(); it.hasNext(); ) {
      it.advance();
      if (it.value() != get(it.x(), it.y()) ) return false;
    }
    return true;
  }
  
  public class Iterator {
    private final TLongIntIterator iterator = map.iterator();

    public void advance() { iterator.advance(); }
    public boolean hasNext() { return iterator.hasNext(); }
    public int value() { return iterator.value(); }
    public int x() { return (int) (iterator.key() % size); }
    public int y() { return (int) (iterator.key() / size); }
  }
}
