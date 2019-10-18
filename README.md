# Tracer

## General
Tracer makes it easy to log the execution time of any test written in Java. 

Using the `@TracedTest` annotation we can log and even benchmark multiple runs of the same test.


## Annotation options

| Type          | Name          | Default value         | Description                         |
|:-------------|:-------------|:---------------------|:---------------------------------------|
| int           | iterations    |                     1 | How many times the test should run. |
| TimeUnit      | timeunit      | TimeUnit.MILLISECONDS | What time unit the duration values should be outputted as.|


## Example Usage
All we need to do in order to use Tracer is extending our test class with the `TestWithTracer` class,
and annotate the method we want to trace with `@TracedTest`.


```java
public class MyTestClass extends TestWithTracer {

 @Test
 @TracedTest(iterations = 100, timeunit = TimeUnit.MICROSECONDS)
 public void shouldTestSomething() {
  // Your awesome test here
 }

}
```


Output:
```text
16:29:58.153  INFO  com.tyrill.tracer.TestWithTracer : shouldTestSomething execution took: 24564 MICROSECONDS
16:29:58.182  INFO  com.tyrill.tracer.TestWithTracer : 100 isolated invocations took: 203058 MICROSECONDS
16:29:58.184  INFO  com.tyrill.tracer.TestWithTracer : Average: 2045.55 MICROSECONDS
```