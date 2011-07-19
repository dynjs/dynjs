package org.dynjs.runtime;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsUndefined extends TypeSafeMatcher<Attribute> {
    @Override
    public boolean matchesSafely(Attribute attribute) {
        return attribute.isUndefined();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is undefined");
    }

    @Factory
    public static <T> Matcher<Attribute> undefined() {
        return new IsUndefined();
    }
}
