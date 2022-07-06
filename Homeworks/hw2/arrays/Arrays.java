package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

import java.sql.PreparedStatement;

/** Array utilities.
 *  @author Eduard Mirzoyan
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] result = new int[A.length + B.length];
        System.arraycopy(A, 0, result, 0, A.length);
        System.arraycopy(B, 0, result, A.length, B.length);
        return result;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. If the start + len is out of bounds for our array, you
     *  can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len) {
        // FIXME: Replace this body with the solution.
        if(start + len > A.length)
            return null;


        int[] result = new int[A.length - len];
        System.arraycopy(A, 0, result, 0, start);
        System.arraycopy(A, start + len, result, start, A.length - (start + len));

        return result;
    }

}
