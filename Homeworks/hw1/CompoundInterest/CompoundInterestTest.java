import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(CompoundInterest.numYears(2023), 1);
        assertEquals(CompoundInterest.numYears(2033), 11);
        assertEquals(CompoundInterest.numYears(2122), 100);
    }

    @Test
    public void testFutureValue() {
        // When working with decimals, we often want to specify a certain
        // range of "wiggle room", or tolerance. For example, if the answer
        // is 5.04, but anything between 5.02 and 5.06 would be okay too,
        // then we can do assertEquals(5.04, answer, .02).

        // The variable below can be used when you write your tests.
        double tolerance = 0.01;

        assertEquals(CompoundInterest.futureValue(10, 12, 2024), 12.544, tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;

        assertEquals(CompoundInterest.futureValueReal(10, 12, 2024, 3), 11.8026496, tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;

        assertEquals(CompoundInterest.totalSavings(5000, 2024, 10), 16550, tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavingsReal(200, 2027, 5, 3), 1168.2, tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
