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
import org.dynjs.runtime.builtins.types.BuiltinObject;

public class ObjectLiteralExpression extends AbstractExpression {

    private final List<PropertyAssignment> propertyAssignments;

    public ObjectLiteralExpression(final Tree tree, final List<PropertyAssignment> propertyAssignments) {
        super(tree);
        this.propertyAssignments = propertyAssignments;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // context
                
                invokestatic(p(BuiltinObject.class), "newObject", sig(DynObject.class, ExecutionContext.class));
                // obj

                for (PropertyAssignment each : propertyAssignments) {
                    dup();
                    // obj obj
                    append( each.getCodeBlock() );
                }
                // obj
            }
        };
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append( "{ " );
        boolean first = true;
        
        for ( PropertyAssignment each : this.propertyAssignments ) {
            if ( ! first ) {
                buf.append( ", " );
            }
            first = false;
            buf.append( each.toString() );
            
        }
        buf.append( " }" );
        
        return buf.toString();
    }
}
