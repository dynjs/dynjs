package org.dynjs.runtime.linker;

import org.dynalang.dynalink.linker.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.support.LinkRequestImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodType.methodType;
import static org.fest.assertions.Assertions.assertThat;

public class PrimitivesLinkerTest {

    private PrimitivesLinker linker;
    @Mock
    private LinkerServices linkerServices;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        linker = new PrimitivesLinker();
    }

    @Test
    public void dispatchesToVtableCall() throws Throwable {
        CallSiteDescriptor descriptor = new CallSiteDescriptor(MethodHandles.lookup(), "add", methodType(Object.class, Object.class, Object.class));
        Object[] arguments = {2.0, 2.0};
        LinkRequest linkRequest = new LinkRequestImpl(descriptor, arguments);
        GuardedInvocation guardedInvocation = linker.getGuardedInvocation(linkRequest, linkerServices);
        assertThat(guardedInvocation.getInvocation().type())
                .isEqualTo(methodType(Double.class, Double.class, Double.class));
        assertThat(guardedInvocation.getInvocation().invokeWithArguments(arguments)).isEqualTo(4.0);
    }
}
