/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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

import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import org.dynalang.dynalink.linker.CallSiteDescriptor;
import org.dynalang.dynalink.linker.GuardedInvocation;
import org.dynalang.dynalink.linker.GuardingDynamicLinker;
import org.dynalang.dynalink.linker.GuardingTypeConverterFactory;
import org.dynalang.dynalink.linker.LinkRequest;
import org.dynalang.dynalink.linker.LinkerServices;
import org.dynalang.dynalink.support.Guards;
import org.dynalang.dynalink.support.Lookup;
import org.dynjs.api.Scope;
import org.dynjs.runtime.Converters;
import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.RT;
import org.dynjs.runtime.extensions.ObjectOperations;

import com.headius.invokebinder.Binder;

public class DynJSLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory {

    public static final MethodHandle RESOLVE;
    public static final MethodHandle DEFINE;
    public static final MethodHandle GETELEMENT;
    public static final MethodHandle SETELEMENT;
    public static final MethodHandle TYPEOF;

    static {
        try {
            RESOLVE = Binder
                    .from( Object.class, Object.class, Object.class )
                    .convert( Object.class, Scope.class, String.class )
                    .invokeVirtual( lookup(), "resolve" );
            DEFINE = Binder
                    .from( void.class, Object.class, Object.class, Object.class )
                    .convert( void.class, Scope.class, String.class, Object.class )
                    .invokeVirtual( lookup(), "define" );
            GETELEMENT = Binder
                    .from( Object.class, Object.class, Object.class )
                    .filter( 1, Converters.toInteger )
                    .convert( Object.class, DynArray.class, int.class )
                    .invokeVirtual( lookup(), "get" );
            SETELEMENT = Binder
                    .from( void.class, Object.class, Object.class, Object.class )
                    .filter( 1, Converters.toInteger )
                    .convert( void.class, DynArray.class, int.class, Object.class )
                    .invokeVirtual( lookup(), "set" );
            TYPEOF = Binder.from( String.class, Object.class )
                    .invokeStatic( lookup(), RT.class, "typeof" );
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        CallSiteDescriptor callSiteDescriptor = linkRequest.getCallSiteDescriptor();
        MethodType methodType = callSiteDescriptor.getMethodType();
        MethodHandle targetHandle = null;
        if ("print".equals( callSiteDescriptor.getName() )) {
            targetHandle = lookup().findStatic( RT.class, "print", methodType );
        } else if ("typeof".equals( callSiteDescriptor.getName() )) {
            Object o = linkRequest.getArguments()[0];
            if (o != null && !PrimitivesLinker.vtable.containsKey( o.getClass() )) {
                Class<? extends Object> targetClass = o.getClass();

                MethodHandle typeof = Binder.from( String.class, Object.class )
                        .convert( String.class, targetClass )
                        .invokeVirtual( lookup(), "typeof" );
                return new GuardedInvocation( typeof, null );
            }
        } else if ("instanceof".equals( callSiteDescriptor.getName() )) {
            Object lhs = linkRequest.getArguments()[0];
            Object rhs = linkRequest.getArguments()[1];
            if (lhs != null && !PrimitivesLinker.vtable.containsKey( lhs.getClass() )) {
                Class<? extends Object> lhsClass = lhs.getClass();
                Class<? extends Object> rhsClass = rhs.getClass();
                MethodHandle typeof = Binder.from( Boolean.class, Object.class, Object.class )
                        .convert( Boolean.class, lhsClass, Object.class )
                        .invokeVirtual( lookup(), "hasInstance" );
                return new GuardedInvocation( typeof, null );
            }
        } else if ("new".equals( callSiteDescriptor.getName() )) {
            return new GuardedInvocation( RT.CONSTRUCT, null );
        } else if ("delete".equals( callSiteDescriptor.getName() )) {
            Object o = linkRequest.getArguments()[0];
            if (o != null && !PrimitivesLinker.vtable.containsKey( o.getClass() )) {
                Class<? extends Object> targetClass = o.getClass();

                MethodHandle delete = Binder.from( Boolean.class, Object.class, Object.class )
                        .convert( Boolean.class, targetClass, String.class )
                        .invokeVirtual( lookup(), "delete" );
                return new GuardedInvocation( delete, null );
            }
        } else if ("eq".equals( callSiteDescriptor.getName() )) {
            targetHandle = lookup().findStatic( ObjectOperations.class, "eq", methodType );
        } else if ("strict_eq".equals( callSiteDescriptor.getName() )) {
            targetHandle = lookup().findStatic( ObjectOperations.class, "strict_eq", methodType );
        } else if ("this".equals( callSiteDescriptor.getName() )) {
            targetHandle = lookup().findStatic( RT.class, "findThis", methodType );
        } else if (isFromDynalink( callSiteDescriptor )) {
            if (callSiteDescriptor.getNameToken( 1 ).equals( "call" )) {
                MethodType functionMethodType = methodType( Object.class, DynThreadContext.class, Object[].class );

                Object[] arguments = linkRequest.getArguments();
                Scope scope = (Scope) arguments[0];
                arguments[0] = scope.resolve( "call" );
                linkRequest.replaceArguments( callSiteDescriptor, arguments );
                MethodHandle call = Lookup.PUBLIC.findVirtual( linkRequest.getArguments()[0].getClass(), "call", functionMethodType );

                return new GuardedInvocation(
                        linkerServices.asType( call, callSiteDescriptor.getMethodType() ),
                        Guards.isInstance( linkRequest.getArguments()[0].getClass(), callSiteDescriptor.getMethodType() ) );
            } else if ("getProp".equals( callSiteDescriptor.getNameToken( 1 ) )) {
                return handleGetProp( callSiteDescriptor );
            } else if ("setProp".equals( callSiteDescriptor.getNameToken( 1 ) )) {
                return handleSetProp( callSiteDescriptor );
            } else if ("getElement".equals( callSiteDescriptor.getNameToken( 1 ) )) {
                return handleGetElement( callSiteDescriptor );
            } else if ("setElement".equals( callSiteDescriptor.getNameToken( 1 ) )) {
                return handleSetElement( callSiteDescriptor );
            }
        } else if (isFromDynJS( callSiteDescriptor )) {
            if (callSiteDescriptor.getNameTokenCount() == 3) {
                String action = callSiteDescriptor.getNameToken( 2 );
                String subsystem = callSiteDescriptor.getNameToken( 1 );
                if (subsystem.equals( "convert" )) {
                    switch (action) {
                    case "to_boolean":
                        targetHandle = Converters.toBoolean;
                        break;
                    }
                }
            }
        }

        if (targetHandle != null) {
            return new GuardedInvocation( targetHandle, null );
        }

        return null;
    }

    private boolean isFromDynJS(CallSiteDescriptor callSiteDescriptor) {
        return callSiteDescriptor.getNameTokenCount() > 1 && callSiteDescriptor.getNameToken( 0 ).equals( "dynjs" );
    }

    private GuardedInvocation handleGetElement(CallSiteDescriptor callSiteDescriptor) {
        return new GuardedInvocation( GETELEMENT, Guards.isInstance( Scope.class, GETELEMENT.type() ) );
    }

    private GuardedInvocation handleSetElement(CallSiteDescriptor callSiteDescriptor) {
        return new GuardedInvocation( SETELEMENT, Guards.isInstance( Scope.class, SETELEMENT.type() ) );
    }

    private GuardedInvocation handleGetProp(CallSiteDescriptor callSiteDescriptor) {
        if (hasConstantCall( callSiteDescriptor )) {
            final MethodHandle handle = Binder
                    .from( Object.class, Object.class )
                    .convert( RESOLVE.type() )
                    .insert( 1, callSiteDescriptor.getNameToken( 2 ) )
                    .invoke( RESOLVE );
            return new GuardedInvocation( handle,
                    Guards.isInstance( Scope.class, handle.type() ) );
        } else {
            return new GuardedInvocation( RESOLVE, Guards.isInstance( Scope.class, RESOLVE.type() ) );
        }
    }

    private GuardedInvocation handleSetProp(CallSiteDescriptor callSiteDescriptor) {
        if (hasConstantCall( callSiteDescriptor )) {
            final MethodHandle handle = Binder
                    .from( void.class, Object.class, Object.class )
                    .convert( DEFINE.type() )
                    .insert( 1, callSiteDescriptor.getNameToken( 2 ) )
                    .invoke( DEFINE );
            return new GuardedInvocation( handle,
                    Guards.isInstance( Scope.class, handle.type() ) );
        } else {
            return new GuardedInvocation( DEFINE, Guards.isInstance( Scope.class, DEFINE.type() ) );
        }
    }

    private boolean hasConstantCall(CallSiteDescriptor callSiteDescriptor) {
        return callSiteDescriptor.getNameTokenCount() == 3;
    }

    private boolean isFromDynalink(CallSiteDescriptor callSiteDescriptor) {
        return callSiteDescriptor.getNameTokenCount() > 1 && callSiteDescriptor.getNameToken( 0 ).equals( "dyn" );
    }

    @Override
    public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType) {
        return null;
    }
}
