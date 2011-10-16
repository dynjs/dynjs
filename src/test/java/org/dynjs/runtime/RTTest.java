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
        Function f = new BaseFunction() {
            @Override
            public Object call(DynThreadContext context, Scope scope, Object[] arguments) {
                return "";
            }

        };

        Object result = RT.FUNCTION_CALL.bindTo(f).invoke(context, scope, new DynAtom[]{});
        assertThat(result)
                .isNotNull()
                .isInstanceOf(String.class);

    }

}
