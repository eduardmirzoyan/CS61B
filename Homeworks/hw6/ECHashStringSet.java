import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {

    private LinkedList<String>[] buckets;
    private int numElements;

    public ECHashStringSet() {
        buckets = new LinkedList[1000];
        numElements = 0;
    }

    @Override
    public void put(String s) {
        int index = getIndex(s, buckets.length);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<String>();
        }

        buckets[index].add(s);
        numElements++;

        while (getLoadFactor() > 5 || index > buckets.length) {
            resizeArray();
        }
    }

    @Override
    public boolean contains(String s) {
        int index = getIndex(s, buckets.length);
        if (buckets.length == 0 || buckets[index] == null) {
            return false;
        }
        return buckets[index].contains(s);
    }

    @Override
    public List<String> asList() {
        LinkedList<String> result = new LinkedList<String>();
        for(LinkedList<String> linkedList : buckets) {
            if (linkedList != null) {
                for (String value : linkedList) {
                    result.add(value);
                }
            }
        }
        return result;
    }

    private void resizeArray() {
        LinkedList<String>[] newArray = new LinkedList[buckets.length * 2];

        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] == null)  {
                buckets[i] = new LinkedList<String>();
            }
            for (String str : buckets[i]) {
                int index = getIndex(str, newArray.length);
                if (newArray[index] == null) {
                    newArray[index] = new LinkedList<String>();
                }
                newArray[index].add(str);
            }
        }
        buckets = newArray;
    }

    private int getIndex(String s, int bucketLength) {
        return (s.hashCode() & 0x7fffffff) % bucketLength;
    }

    private double getLoadFactor() {
        return numElements / buckets.length;
    }
}
