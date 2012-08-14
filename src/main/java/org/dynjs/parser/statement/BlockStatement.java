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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.Completion;
import org.objectweb.asm.tree.LabelNode;

public class BlockStatement extends AbstractStatement implements Statement {

    private final List<Statement> blockContent;

    public BlockStatement(final Tree tree, final List<Statement> blockContent) {
        super( tree );
        this.blockContent = blockContent;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        System.err.println( "blockContent=" + this.blockContent );
        if (this.blockContent == null) {
            System.err.println( "empty!" );
            return Collections.emptyList();
        }

        List<FunctionDeclaration> decls = new ArrayList<>();
        
        for (Statement each : this.blockContent) {
            System.err.println( "each=" + each );
            if (each instanceof FunctionDeclaration) {
                decls.add( (FunctionDeclaration) each );
            }
        }

        return decls;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList();
        for (Statement each : this.blockContent) {
            if (each instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement statement = (VariableDeclarationStatement) each;
                decls.addAll( statement.getVariableDeclarations() );
            }
        }
        return decls;
    }

    @Override
    public CodeBlock getCodeBlock() {
        System.err.println( "Block(" + this + ") - getCodeBlock - " + blockContent );
        return new CodeBlock() {
            {
                // 12.1
                LabelNode abrupt = new LabelNode();
                LabelNode end = new LabelNode();

                append( normalCompletion() );
                // completion

                for (Statement statement : blockContent) {
                    LabelNode nonAbrupt = new LabelNode();
                    LabelNode bringForwardValue = new LabelNode();
                    LabelNode nextStatement = new LabelNode();

                    line( statement.getPosition().getLine() );
                    System.err.println( statement + " // " + statement.getClass().getName() );
                    append( statement.getCodeBlock() );
                    // completion(prev) completion(cur)
                    dup();
                    // completion(prev) completion(cur) completion(cur)
                    append( handleCompletion( nonAbrupt, nonAbrupt, abrupt, abrupt, abrupt ) );
                    label( nonAbrupt );
                    // completion(prev) completion(cur);

                    dup();
                    // completion(prev) completion(cur) completion(cur)
                    append( jsCompletionValue() );
                    // completion(prev) completion(cur) value
                    ifnull( bringForwardValue );
                    // completion(prev) completion(cur)
                    swap();
                    // completion(cur) completion(prev)
                    pop();
                    // completion(cur)
                    go_to( nextStatement );

                    label( bringForwardValue );
                    // completion(prev) completion(cur)
                    dup_x1();
                    // completion(cur) completion(prev) completion(cur)
                    swap();
                    // completion(cur) completion(cur) completion(prev)
                    append( jsCompletionValue() );
                    // completion(cur) val(prev)
                    putfield( p( Completion.class ), "value", ci( Object.class ) );
                    // completion(cur)
                    label( nextStatement );
                    // completion(cur)
                }

                go_to( end );

                // ----------------------------------------
                // ABRUPT

                label( abrupt );
                // completion(prev) completion(cur)
                swap();
                // completion(cur) completion(prev)
                pop();
                // completion(cur)

                // ----------------------------------------
                // END
                label( end );
                // completion
                nop();
            }
        };
    }
}
