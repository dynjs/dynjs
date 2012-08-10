package org.dynjs.runtime.java;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JavaPrototypeFunction {
    String name() default "";
}
