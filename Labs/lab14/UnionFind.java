
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author Eduard Mirzoyan
 */
public class UnionFind {


    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        _size = new int[N + 1];
        _parents = new int[N + 1];

        for (int i = 1; i < N + 1; i++) {
            _size[i] = 1;
        }

        for (int i = 1; i < N + 1; i++) {
            _parents[i] = i;
        }

    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        if (_parents[v] == v) {
            return v;
        }
        else {
            return find(_parents[v]);
        }
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        if (samePartition(u, v)) {
            return find(u);
        }

        int rootU = find(u);
        int rootV = find(v);

        if (_size[rootU] > _size[rootV]) {
            _parents[rootV] = rootU;
            _size[rootU] += _size[rootV];

            return rootU;

        }
        else if (_size[rootV] > _size[rootU] || _size[rootV] == _size[rootU]) {
            _parents[rootU] = rootV;
            _size[rootV] += _size[rootU];

            return rootV;
        }

        // Else return largest int value
        return Integer.MAX_VALUE;
    }

    // Fields
    int[] _size;
    int[] _parents;
}
