package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** A tester for matrix accumulation
 *  @author Eduard Mirzoyan
 */

public class MatrixUtilsTest {

    @Test
    public void accumulateTest(){
        double[][] input = {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000},
        };
        double[][] answer = {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919},
        };
        double[][] result = MatrixUtils.accumulateVertical(input);

        assertTrue(java.util.Arrays.deepEquals(answer, result));
    }

    @Test
    public void transposeTest(){
        double[][] input = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[][] answer = {
                {1, 4, 7},
                {2, 5, 8},
                {3, 6, 9}
        };
        System.out.println(java.util.Arrays.deepToString(MatrixUtils.transpose(input)));
        assertTrue(java.util.Arrays.deepEquals(answer, MatrixUtils.transpose(input)));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
