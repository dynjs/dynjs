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

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.objectweb.asm.tree.LabelNode;

public class FunctionExpression extends AbstractCompilingStatement {

    private final String identifier;
    private final List<String> formalParameters;
    private BlockStatement block;

    public FunctionExpression(final Tree tree, BlockManager blockManager, final List<String> formalParameters, final BlockStatement block) {
        this( tree, blockManager, null, formalParameters, block );
    }

    public FunctionExpression(final Tree tree, BlockManager blockManager, final String identifier, final List<String> formalParameters, final BlockStatement block) {
        super( tree, blockManager );
        this.identifier = identifier;
        this.formalParameters = formalParameters;
        this.block = block;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public List<String> getFormalParameters() {
        return this.formalParameters;
    }

    public BlockStatement getBlock() {
        return this.block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        stash( block );
        return new CodeBlock() {
            {
                LabelNode skipCompile = new LabelNode();
                
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // context 
                ldc( block.getStatementNumber() );
                // context statement-num
                invokevirtual( p( ExecutionContext.class ), "retrieveBlockEntry", sig( Entry.class, int.class ) );
                // entry
                dup();
                // entry entry
                getfield( p(Entry.class), "compiled", sig(Object.class)  );
                // entry function
                ifnonnull( skipCompile );
                // entry
                dup();
                // entry entry
                
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                invokevirtual( p( ExecutionContext.class ), "getCompiler", sig( JSFunction.class ) );
                // entry entry compiler
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // entry entry compiler context
                dup();
                invokevirtual( p( ExecutionContext.class ), "isStrict", sig( boolean.class ) );
                // entry entry compiler context bool

                bipush( formalParameters.size() );
                anewarray( p( String.class ) );

                for (int i = 0; i < formalParameters.size(); ++i) {
                    dup();
                    bipush( i );
                    ldc( formalParameters.get( i ) );
                }

                // entry entry compiler context bool params
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // entry entry compiler context bool context
                ldc( block.getStatementNumber() );
                // entry entry compiler context bool context statement-num
                invokevirtual( p( ExecutionContext.class ), "retrieveBlockEntry", sig( Entry.class, int.class ) );
                // entry entry compiler context bool entry

                getfield( p( Entry.class ), "statement", sig( Statement.class ) );
                // entry entry compiler context bool statement
                invokevirtual( p( JSCompiler.class ), "compileFunction", sig( JSFunction.class, ExecutionContext.class, boolean.class, Statement.class ) );
                // entry entry function
                
                putfield( p(Entry.class), "compiled", sig(Object.class )  );
                // entry
                
                label(skipCompile);
                // entry 
                getfield( p(Entry.class), "compiled", sig(Object.class )  );
                // function
            }
        };
    }
}
