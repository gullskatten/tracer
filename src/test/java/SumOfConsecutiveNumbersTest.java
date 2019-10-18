import com.tyrill.tracer.TestWithTracer;
import com.tyrill.tracer.TracedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

/**
 * Example of using @TracedTest to benchmark a test.
 */
public class SumOfConsecutiveNumbersTest extends TestWithTracer {

    /**
     * Test to compare a formula for summing consecutive numbers with a "brute force method" using array streams (reduce).
     * Sum of consecutive numbers source:
     * <link>https://study.com/academy/lesson/finding-the-sum-of-consecutive-numbers.html</link>
     */
    @Test
    @TracedTest(iterations = 1500, timeunit = TimeUnit.MICROSECONDS)
    public void validateSumOfConsecutiveNumbers() {
        ArrayList<Integer> ints = new ArrayList<>();
        int iterations = 55500;

        for (int i = 0; i <= iterations; i++) {
            ints.add(i);
        }

        // Using formula
        int additionOfNumbers = (ints.size() / 2) * (1 + iterations);

        // "Brute force" sum.
        Optional<Integer> reduce = ints.stream().reduce(Integer::sum);

        if (reduce.isPresent()) {
            // Assert that formula gives same result as reducer sum.
            Assert.assertThat(reduce.get(), is(additionOfNumbers));
        } else {
            fail("Reducer could not sum all numbers!?");
        }
    }
}
