import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i++) {
                int index = i - 1;
                int storedValue = array[i];

                while (index >= 0 && array[index] > storedValue) {
                    array[index + 1] = array[index];
                    index--;
                }
                array[index + 1] = storedValue;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                }

                // Swapping
                int temp = array[minIndex];
                array[minIndex] = array[i];
                array[i] = temp;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int[] result = mergeSort(Arrays.copyOf(array, k));
            for (int i = 0; i < k; i++) {
                array[i] = result[i];
            }
        }

        private int[] merge(int[] arrayA, int[] arrayB) {
            int[] result = new int[arrayA.length + arrayB.length];

            int indexA = 0;
            int indexB = 0;
            int resultIndex = 0;

            while (indexA < arrayA.length && indexB < arrayB.length) {
                if (arrayA[indexA] <= arrayB[indexB]) {
                    result[resultIndex] = arrayA[indexA];
                    indexA++;
                }
                else {
                    result[resultIndex] = arrayB[indexB];
                    indexB++;
                }
                resultIndex++;
            }
            // Only A or B has elements now
            while (indexA < arrayA.length) {
                result[resultIndex] = arrayA[indexA];
                indexA++;
                resultIndex++;
            }

            while (indexB < arrayB.length) {
                result[resultIndex] = arrayB[indexB];
                indexB++;
                resultIndex++;
            }

            return result;
        }

        private int[] mergeSort(int[] array) {
            if (array.length == 1) {
                return array;
            }

            int[] a = mergeSort(Arrays.copyOfRange(array, 0, array.length / 2));

            int[] b = mergeSort(Arrays.copyOfRange(array, array.length / 2, array.length));

            return merge(a, b);
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int max = getMaxValue(a, k);

            for (int i = 1; max / i > 0; i *= 10) {
                sortHelper(a, k, i);
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }

        void sortHelper(int array[], int size, int place) {
            int[] result = new int[size + 1];
            int max = array[0];

            for (int i = 1; i < size; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }

            int[] count = new int[max + 1];

            for (int i = 0; i < max; ++i) {
                count[i] = 0;
            }

            for (int i = 0; i < size; i++) {
                count[(array[i] / place) % 10]++;
            }

            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            for (int i = size - 1; i >= 0; i--) {

                result[count[(array[i] / place) % 10] - 1] = array[i];

                count[(array[i] / place) % 10]--;
            }

            for (int i = 0; i < size; i++) {
                array[i] = result[i];
            }
        }

        int getMaxValue(int array[], int size) {
            int max = array[0];

            for (int i = 1; i < size; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }

            return max;
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
