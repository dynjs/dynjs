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
package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;

public class ObjectLiteralExpression extends AbstractExpression {

    private final List<NamedValue> namedValues;

    public ObjectLiteralExpression(final Tree tree, final List<NamedValue> namedValues) {
        super(tree);
        this.namedValues = namedValues;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                newobj(p(DynObject.class));
                // obj
                dup();
                // obj obj
                invokespecial(p(DynObject.class), "<init>", sig(void.class));
                // obj

                for (NamedValue each : namedValues) {
                    dup();
                    // obj obj
                    aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                    // obj obj context
                    ldc(each.getName());
                    // obj obj context name
                    Expression expr = each.getExpr();
                    append(expr.getCodeBlock());
                    // obj obj context name val
                    append(jsGetValue());
                    // obj obj context name val
                    if (expr instanceof FunctionExpression) {
                        ldc( each.getName() );
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
                // obj
            }
        };
    }
}
