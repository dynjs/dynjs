package org.dynjs.runtime;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RTTest {

    private DynThreadContext context;
    private DynObject scope;

    @Before
    public void setUp() {
        context = new DynThreadContext();
        scope = new DynObject();
    }

    @Test
    public void testFunctionCall() throws Throwable {
        Function f = new Function() {
            @Override
            public DynAtom call(DynThreadContext context, Scope scope, DynAtom... arguments) {
                return new DynString("");
            }

            @Override
            public boolean isPrimitive() {
                return false;
            }
        };

        Object result = RT.FUNCTION_CALL.bindTo(f).invoke(context, scope, new DynAtom[]{});
        assertThat(result)
                .isNotNull()
                .isInstanceOf(DynString.class);

    }

}
