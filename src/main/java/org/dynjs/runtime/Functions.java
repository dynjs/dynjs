package org.dynjs.runtime;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.primitives.DynPrimitiveBoolean;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class Functions {

    private static final DynJSCompiler compiler = new DynJSCompiler();

    public static final Function CONSTRUCTOR;

    static {
        Function fn = new BaseFunction() {
            public DynAtom call(DynThreadContext context, Scope scope, DynAtom... arguments) {
                DynObject object = new DynObject();
                object.setProperty("Class", new DynString("Object"));
                object.setProperty("Extensible", DynPrimitiveBoolean.TRUE);
                return object;
            }
        };
        CONSTRUCTOR = fn;
    }

    public static final Function GET_PROPERTY;

    static {
        Function fn = compiler.compile(new DynFunction("scope", "propertyName") {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(getArgumentsOffset())
                        .pushInt(getArgumentOffset("scope"))
                        .aaload()

                        .aload(getArgumentsOffset())
                        .pushInt(getArgumentOffset("propertyName"))
                        .aaload()
                        .invokevirtual(p(Object.class), "toString", sig(String.class))

                        .invokedynamic("dyn:getProp", sig(DynAtom.class, Scope.class, String.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                        .areturn();
            }
        });
        GET_PROPERTY = fn;
    }

}
