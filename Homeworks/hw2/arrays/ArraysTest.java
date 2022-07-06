package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** A tester for the Arrays class
 *  @author Eduard Mirzoyan
 */

public class ArraysTest {

    @Test
    public void catenateTest() {
        int[] inputA = {1,3,4};
        int[] inputB = {1,3,4};
        int[] inputC = {};
        int[] result = Arrays.catenate(inputA, inputB);
        int[] answer = {1, 3, 4, 1, 3, 4};
        assertEquals(answer.length, result.length);
        for (int i = 0; i < answer.length; i++) {
            assertEquals(answer[i], result[i]);
        }

        result = Arrays.catenate(inputA, inputC);
        int[] answer2 = {1, 3, 4};
        assertEquals(answer2.length, result.length);
        for (int i = 0; i < answer2.length; i++) {
            assertEquals(answer2[i], result[i]);
        }
    }

    /*  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    @Test
    public void removeTest() {
        int[] inputA = {0, 1, 2, 3};
        int[] inputB = {1, 2, 3, 4, 5, 6, 7};
        int[] result = Arrays.remove(inputA, 1, 2);
        int[] answer = {0, 3};

        System.out.println(java.util.Arrays.toString(result));
        assertEquals(answer.length, result.length);
        for (int i = 0; i < answer.length; i++) {
            assertEquals(answer[i], result[i]);
        }

        result = Arrays.remove(inputB, 3, 0);
        assertEquals(inputB.length, result.length);
        for (int i = 0; i < inputB.length; i++) {
            assertEquals(inputB[i], result[i]);
        }

        result = Arrays.remove(inputA, 1, 1);
        int[] answer2 = {0, 2, 3};
        assertEquals(answer2.length, result.length);
        for (int i = 0; i < answer2.length; i++) {
            assertEquals(answer2[i], result[i]);
        }

        assertEquals(Arrays.remove(inputB, 5, 10), null);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
