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
package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

public abstract class AbstractUnaryOperationStatement extends AbstractStatement implements Statement {

    private final Statement expression;

    public AbstractUnaryOperationStatement(final Tree tree, final Statement expression) {
        super( tree );
        this.expression = expression;
    }

    protected abstract String operation();

    @Override
    public CodeBlock getCodeBlock() {
        final IdentifierReferenceExpression resolvable = (IdentifierReferenceExpression) expression;
        return new CodeBlock() {
            {
                // relocate +1, since this statement uses astore(4)
                append( CodeBlockUtils.relocateLocalVars( expression.getCodeBlock(), 1 ) );
                append( before() );
                append( processOperation() );
                append( after() );
                aload( DynJSCompiler.Arities.THIS );
                swap();
                ldc( resolvable.getName() );
                swap();
                invokedynamic( "dyn:setProp", sig( void.class, Object.class, Object.class, Object.class ), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS );
                aload( 4 );
            }
        };
    }

    protected CodeBlock after() {
        return DynJSCompiler.Helper.EMPTY_CODEBLOCK;
    }

    protected CodeBlock before() {
        return DynJSCompiler.Helper.EMPTY_CODEBLOCK;
    }

    protected CodeBlock store() {
        return new CodeBlock() {
            {
                dup();
                astore( 4 );
            }
        };
    }

    private CodeBlock processOperation() {
        return new CodeBlock() {
            {
                append( new NumberLiteralStatement( null, "1", 10 ).getCodeBlock() );
                invokedynamic( AbstractUnaryOperationStatement.this.operation(), DynJSCompiler.Signatures.ARITY_2, RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS );
            }
        };
    }

}
