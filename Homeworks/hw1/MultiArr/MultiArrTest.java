import static org.junit.Assert.*;
import org.junit.Test;

import java.util.TreeMap;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] arr = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 5, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 7, 0, 0, 0 },
                { 0, 0, 101, 0, 0, 0, 0, 0, 0, 0 }
        };
        int[][] arr2 = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 5, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 7, 0, 0, 0 },
                { 0, 0, 101, 0, 201, 0, 0, 0, 0, 0 }
        };
        assertEquals(MultiArr.maxValue(arr), 101);
        assertEquals(MultiArr.maxValue(arr2), 201);
    }

    @Test
    public void testAllRowSums() {
        int[][] arr = {
                { 0, 0, 0, 2, 0, 0, 0, 0, 0, 0 },
                { 0, 5, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 12, 0, 0, 7, 0, 0, 0 },
                { 0, 10, 0, 0, 0, 0, 7, 0, 0, 0 },
                { 0, 0, 101, 0, 0, 0, 0, 0, 0, 0 }
        };
        int[] answer = {2, 5, 19, 17, 101};

        assertEquals(MultiArr.allRowSums(arr).length, answer.length);
        for (int i = 0; i < answer.length; i++) {
            assertEquals(MultiArr.allRowSums(arr)[i], answer[i]);
        }
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
