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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

public class BlockStatement extends AbstractStatement implements Statement {

    private final List<Statement> blockContent;

    public BlockStatement(Tree tree, final List<Statement> blockContent) {
        super(tree);
        this.blockContent = blockContent;
    }

    public BlockStatement(final List<Statement> blockContent) {
        super((blockContent == null || blockContent.isEmpty()) ? null : blockContent.get(0).getPosition());
        this.blockContent = blockContent;
    }

    public List<Statement> getBlockContent() {
        return this.blockContent;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
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

        return decls;
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        for (Statement each : this.blockContent) {
            if (each instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement statement = (VariableDeclarationStatement) each;
                decls.addAll(statement.getVariableDeclarations());
            } else if (!(each instanceof FunctionDeclaration)) {
                decls.addAll(each.getVariableDeclarations());
            }
        }
        return decls;
    }

    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit(context, this, strict);
    }

    public String dump(String indent) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(super.dump(indent));
        for (Statement each : this.blockContent) {
            buffer.append(each.dump(indent + "  "));
        }

        return buffer.toString();
    }

    public String toIndentedString(String indent) {
        StringBuffer buffer = new StringBuffer();

        for (Statement each : this.blockContent) {
            if (each != null) {
                buffer.append(each.toIndentedString(indent));
                buffer.append("\n");
            }
        }

        return buffer.toString();
    }
}
