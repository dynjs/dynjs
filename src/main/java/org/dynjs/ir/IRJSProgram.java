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

import java.util.List;
import org.dynjs.exception.ThrowException;
import org.dynjs.ir.instructions.Add;
import org.dynjs.ir.instructions.BEQ;
import org.dynjs.ir.instructions.Call;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.instructions.Jump;
import org.dynjs.ir.instructions.LE;
import org.dynjs.ir.instructions.LT;
import org.dynjs.ir.instructions.ResultInstruction;
import org.dynjs.ir.instructions.Return;
import org.dynjs.ir.operands.LocalVariable;
import org.dynjs.ir.operands.OffsetVariable;
import org.dynjs.ir.operands.Variable;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.jruby.dirgra.DirectedGraph;

public class IRJSProgram implements JSProgram {
    private Scope scope;
    private Instruction[] instructions;

    public IRJSProgram(Scope scope) {
        this.scope = scope;
        this.instructions = scope.prepareForInterpret();

        System.out.println("PROGRAM:");
        int size = instructions.length;
        for (int i = 0; i < size; i++) {
            System.out.println("" + instructions[i]);
        }
    }
    @Override
    public Completion execute(ExecutionContext context) {
        return Completion.createNormal(Interpreter.execute(context, scope, instructions));
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
