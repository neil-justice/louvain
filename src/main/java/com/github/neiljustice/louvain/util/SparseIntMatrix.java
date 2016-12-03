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