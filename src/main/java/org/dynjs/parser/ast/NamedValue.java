package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;

import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;

import me.qmx.jitescript.CodeBlock;

public class NamedValue extends PropertyAssignment {

    private Expression expr;

    public NamedValue(String name, Expression expr) {
        super(name);
        this.expr = expr;
    }

    public Expression getExpr() {
        return this.expr;
    }

    public String toString() {
        return getName() + ":" + this.expr;
    }

    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // obj obj context
                ldc(getName());
                // obj obj context name
                append(expr.getCodeBlock());
                // obj obj context name val
                append(jsGetValue());
                // obj obj context name val
                if (expr instanceof FunctionExpression) {
                    ldc(getName());
                    swap();
                    invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializer", sig(PropertyDescriptor.class, String.class, Object.class));
                } else {
                    invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializer", sig(PropertyDescriptor.class, Object.class));
                }
                // obj obj context name desc
                iconst_0();
                // obj obj context name desc 0
                i2b();
                // obj obj context name desc false
                invokeinterface(p(JSObject.class), "defineOwnProperty",
                        sig(boolean.class, ExecutionContext.class, String.class, PropertyDescriptor.class, boolean.class));
                // obj bool
                pop();
                // obj
            }
        };
    }

}
