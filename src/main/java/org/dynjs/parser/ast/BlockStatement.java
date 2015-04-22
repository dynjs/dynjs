/**
 *  Copyright 2013 Douglas Campos, and individual contributors
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;

public class BlockStatement extends AbstractStatement {

    private final List<Statement> blockContent;
    private List<FunctionDeclaration> functionDeclarations = null;
    private List<VariableDeclaration> variableDeclarations = null;

    public BlockStatement(final List<Statement> blockContent) {
        this.blockContent = blockContent;
    }
    
    public Position getPosition() {
        if ( blockContent.isEmpty() ) {
            return null;
        }
        
        return blockContent.get(0).getPosition();
    }

    public List<Statement> getBlockContent() {
        return this.blockContent;
    }

    public List<BlockStatement> getAsChunks(int chunkSize) {
        if (this.blockContent.size() <= chunkSize) {
            return Collections.singletonList(this);
        }

        List<BlockStatement> chunks = new ArrayList<>();

        int chunkStart = 0;
        int totalStatements = this.blockContent.size();

        while (chunkStart < totalStatements) {
            int chunkEnd = chunkStart + chunkSize;
            if (chunkEnd > totalStatements) {
                chunkEnd = totalStatements;
            }

            chunks.add(new BlockStatement(this.blockContent.subList(chunkStart, chunkEnd)));

            chunkStart = chunkEnd;
        }

        return chunks;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        if (this.functionDeclarations != null) {
            return this.functionDeclarations;
        }

        if (this.blockContent == null) {
            return Collections.emptyList();
        }

        List<FunctionDeclaration> decls = new ArrayList<>();

        for (Statement each : this.blockContent) {
            if (each instanceof FunctionDeclaration) {
                decls.add((FunctionDeclaration) each);
            }
            decls.addAll(each.getFunctionDeclarations());
        }

        this.functionDeclarations = decls;

        return decls;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        if (this.variableDeclarations != null) {
            return this.variableDeclarations;
        }

        List<VariableDeclaration> decls = new ArrayList<>();
        for (Statement each : this.blockContent) {
            if (each instanceof VariableStatement) {
                VariableStatement statement = (VariableStatement) each;
                decls.addAll(statement.getVariableDeclarations());
            } else if (!(each instanceof FunctionDeclaration)) {
                decls.addAll(each.getVariableDeclarations());
            }
        }

        this.variableDeclarations = decls;

        return decls;
    }

    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

    public String dump(String indent) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(super.dump(indent));
        for (Statement each : this.blockContent) {
            buffer.append(each.dump(indent + "  "));
        }

        return buffer.toString();
    }
    
    public int getSizeMetric() {
        return 3;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        context.checkResourceUsage();
        List<Statement> content = getBlockContent();

        Object completionValue = Types.UNDEFINED;

        Statement previousStatement = null;

        for (Statement each : content) {
            Position position = each.getPosition();
            if (position != null) {
                context.setLineNumber(position.getLine());
                context.setColumnNumber(position.getColumn());
            }

            if ( ! ( each instanceof FunctionDeclaration ) ) {
                context.debug( each, previousStatement );
            }

            Completion completion = (Completion) each.interpret(context, debug);

            if ( ! ( each instanceof FunctionDeclaration ) ) {
                previousStatement = each;
            }

            if (completion.type == Completion.Type.NORMAL) {
                completionValue = completion.value;
                continue;
            }
            if (completion.type == Completion.Type.CONTINUE) {
                return(completion);

            }
            if (completion.type == Completion.Type.RETURN) {
                return(completion);

            }
            if (completion.type == Completion.Type.BREAK) {
                completion.value = completionValue;
                if (completion.target != null && getLabels().contains(completion.target)) {
                    return(Completion.createNormal(completionValue));
                } else {
                    return(completion);
                }
            }
        }

        return(Completion.createNormal(completionValue));
    }

    public String toIndentedString(String indent) {
        StringBuilder buffer = new StringBuilder();

        for (Statement each : this.blockContent) {
            if (each != null) {
                buffer.append(each.toIndentedString(indent));
                buffer.append("\n");
            }
        }

        return buffer.toString();
    }
}
