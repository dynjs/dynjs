package org.dynjs.runtime;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.primitives.DynPrimitiveBoolean;

public class Functions {

    private static final DynJSCompiler compiler = new DynJSCompiler();

    public static final Function CONSTRUCTOR;

    static {
        Function fn = new BaseFunction() {
            public DynAtom call(DynThreadContext context, Scope scope, DynAtom[] arguments) {
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
        Function fn = compiler.compile(new DynFunction() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .areturn();
            }

            @Override
            public String[] getArguments() {
                return new String[]{"scope", "propertyName"};
            }
        });
        GET_PROPERTY = fn;
    }

}
