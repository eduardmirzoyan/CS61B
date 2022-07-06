package lists;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/** Testing naturalRuns
 *
 *  @author Eduard Mirzoyan
 */

public class ListsTest {

    @Test
    public void basicRunsTest() {
        IntList input = IntList.list(1, 2, 3, 1, 2);
        IntList run1 = IntList.list(1, 2, 3);
        IntList run2 = IntList.list(1, 2);
        IntListList result = IntListList.list(run1, run2);
        assertNotEquals(input, result);
        int[] array = new int[]{1, 2, 3, 1, 2};
        assertEquals(input, IntList.list(array));
    }

    @Test
    public void naturalRunsTest() {
        IntList input = IntList.list(1, 2, 3, 1, 2);
        IntList run1 = IntList.list(1, 2, 3);
        IntList run2 = IntList.list(1, 2);
        IntListList result = Lists.naturalRuns(input);
        IntListList answer = IntListList.list(run1, run2);
        assertEquals(result, answer);

        IntList input2 = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        run1 = IntList.list(1, 3, 7);
        run2 = IntList.list(5);
        IntList run3 = IntList.list(4, 6, 9, 10);
        IntList run4 = IntList.list(10, 11);
        answer = IntListList.list(run1, run2, run3, run4);
        result = Lists.naturalRuns(input2);
        assertEquals(answer, result);

        input = IntList.list(10, 30, 42, 120, 222);
        answer = IntListList.list(input);
        result = Lists.naturalRuns(input);
        assertEquals(answer, result);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
