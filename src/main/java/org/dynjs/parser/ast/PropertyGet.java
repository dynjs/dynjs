package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;

public class PropertyGet extends PropertyAccessor {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public PropertyGet(BlockManager blockManager, String name, Statement block) {
        super(blockManager, name, block);
    }

    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // IN obj
                dup();
                // obj obj
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // obj obj context
                ldc(getName());
                // obj obj context name
                invokeinterface(p(JSObject.class), "getOwnProperty", sig(Object.class, ExecutionContext.class, String.class));
                // obj desc(orig)
                append(CodeBlockUtils.compiledFunction(getBlockManager(), EMPTY_STRING_ARRAY, getBlock()));
                // obj desc(orig) fn
                ldc(getName());
                // obj desc(orig) fn name
                swap();
                // obj desc(orig) name fn
                invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializerGet", sig(PropertyDescriptor.class, Object.class, String.class,
                        JSFunction.class));
                // obj desc(new)
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // obj desc(new) context
                swap();
                // obj context desc(new)
                ldc(getName());
                // obj context desc(new) name
                swap();
                // obj context name desc(new)
                iconst_0();
                // obj context name desc(new) 0
                i2b();
                // obj context name desc(new) false
                invokeinterface(p(JSObject.class), "defineOwnProperty",
                        sig(boolean.class, ExecutionContext.class, String.class, PropertyDescriptor.class, boolean.class));
                // bool
                pop();
                // <EMPTY>
            }
        };
    }

}
