package com.tyrill.tracer;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;

public class TestWithTracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestWithTracer.class);
    private static final DecimalFormat df = new DecimalFormat("#.#####");
    private static StopWatch stopwatch;
    private static boolean isTraced;

    @BeforeClass
    public static void before() {
        stopwatch = new StopWatch();
    }

    @AfterClass
    public static void tearDown() {
    }

    @Rule
    public TestName testName = new TestName();

    @Before
    public void beforeTest() throws NoSuchMethodException {
        Method method = this.getClass().getMethod(testName.getMethodName());
        if (method.isAnnotationPresent(TracedTest.class)) {
            isTraced = true;
            stopwatch.reset();
            stopwatch.start();
        }
    }

    @After
    public void afterTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (isTraced) {
            Method method = this.getClass().getMethod(testName.getMethodName());
            TracedTest options = method.getDeclaredAnnotation(TracedTest.class);
            TimeUnit timeunit = options.timeunit();

            if (isStopwatchAvailable()) {
                stopwatch.stop();
                LOGGER.info("{} execution took: {} {}", testName.getMethodName(), stopwatch.getTime(timeunit), timeunit.name());
                isTraced = false;
            }

            if (options.iterations() > 1) {
                performBenchmarkTest(method, options, timeunit);
            }
        }
    }

    private void performBenchmarkTest(Method method, TracedTest options, TimeUnit timeunit) throws IllegalAccessException, InvocationTargetException {
        long[] intervals = new long[options.iterations()];

        StopWatch ibsw = new StopWatch();

        for (int i = 0; i < options.iterations(); i++) {
            ibsw.reset();
            ibsw.start();
            method.invoke(this);
            intervals[i] = ibsw.getTime(timeunit);
            ibsw.stop();
        }

        OptionalDouble average = Arrays.stream(intervals).average();
        OptionalLong total = Arrays.stream(intervals).reduce(Long::sum);

        if (average.isPresent() && total.isPresent()) {
            LOGGER.info("{} isolated invocations took: {} {}", options.iterations(), total.getAsLong() + "", timeunit.name());
            LOGGER.info("Average: {} {}", df.format(average.getAsDouble()) + "", timeunit.name());
        }
    }

    private boolean isStopwatchAvailable() {
        return stopwatch != null && stopwatch.isStarted();
    }
}
