package com.tyrill.tracer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TracedTest {
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;
    int iterations() default 1;
}
