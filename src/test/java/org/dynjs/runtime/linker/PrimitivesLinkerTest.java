/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        MethodHandle target = mock(MethodHandle.class);
        when(linkerServices.asType(any(MethodHandle.class), any(MethodType.class))).thenReturn(target);
        CallSiteDescriptor descriptor = new CallSiteDescriptor(MethodHandles.lookup(), "add", methodType(Object.class, Object.class, Object.class));
        Object[] arguments = {2.0, 2.0};
        LinkRequest linkRequest = new LinkRequestImpl(descriptor, arguments);
        GuardedInvocation guardedInvocation = linker.getGuardedInvocation(linkRequest, linkerServices);
        assertThat(guardedInvocation).isNotNull();
    }
}
