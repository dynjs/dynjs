/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynjs.ir;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;

import java.util.List;

public class IRJSProgram implements JSProgram {

    private final BlockManager blockManager;
    private Scope scope;
    private Instruction[] instructions;

    public IRJSProgram(BlockManager blockManager, Scope scope) {
        this.blockManager = blockManager;
        this.scope = scope;
        this.instructions = scope.prepareForInterpret();

//        System.out.println("PROGRAM:");
        int size = instructions.length;
        for (int i = 0; i < size; i++) {
            System.out.println("" + instructions[i]);
        }
    }

    @Override
    public Completion execute(ExecutionContext context) {
        // Allocate space for variables of this function and establish link to captured ones.
        context.allocVars(scope.getLocalVariableSize(), null);

        return Completion.createNormal(Interpreter.execute(context, scope, instructions));
    }

    @Override
    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    @Override
    public String getFileName() {
        return scope.getFileName();
    }

    @Override
    public boolean isStrict() {
        return scope.isStrict();
    }

    // FIXME: Remove or replace once we learn how IR should handle these
    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return FunctionDeclaration.EMPTY_LIST;
    }

    // FIXME: Remove or replace once we learn how IR should handle these
    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return VariableDeclaration.EMPTY_LIST;
    }
}
