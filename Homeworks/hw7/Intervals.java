import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

/** HW #7, Sorting ranges.
 *  @author Eduard Mirzoyan
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        intervals.sort(Comparator.comparingInt(array -> array[0]));

        int start = Integer.MAX_VALUE;
        int end = Integer.MIN_VALUE;
        int result = 0;

        for (int i = 0; i < intervals.size(); i++) {

            if (intervals.get(i)[0] > end) {
                result = result + end - start;
                end = intervals.get(i)[1];
                start = intervals.get(i)[0];
            }
            else if (intervals.get(i)[0] <= end && intervals.get(i)[1] > end) {
                end = intervals.get(i)[1];
            }

        }
        result = result + end - start - 1;

        return result;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
