/* Sparse square matrix using hashmap. */
import gnu.trove.map.hash.TLongIntHashMap;
import gnu.trove.iterator.TLongIntIterator;
import tester.Tester;

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
  
  public static void main(String[] args) {
    Tester t = new Tester();
    int[][] m1 = new int[3][3];
    SparseIntMatrix m2 = new SparseIntMatrix(3);
    
    m1[1][2] = 3;
    m1[1][2] += 1101;
    m1[2][1] = 1104;
    m1[1][1] = 5;
    m1[2][2] = 100203;
    m2.set(1, 2, 3);
    m2.add(1, 2, 1101);
    m2.set(2, 1, 1104);
    m2.set(1, 1, 5);
    m2.set(2, 2, 100203);
    
    t.is(m1[1][2], m2.get(1, 2));
    t.is(m1[1][1], m2.get(1, 1));
    t.is(m1[2][2], m2.get(2, 2));
    t.is(m2.isSymmetric(), true);
    
    t.results();
  }
  
}