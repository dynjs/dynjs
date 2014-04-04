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
    private String filename;
    boolean isStrict;
    private Instruction[] instructions;

    public IRJSProgram(Scope scope, String filename, boolean isStrict) {
        this.scope = scope;
        this.filename = filename;
        this.isStrict = isStrict;
        this.instructions = scope.prepareForInterpret();

        System.out.println("PROGRAM:");
        int size = instructions.length;
        for (int i = 0; i < size; i++) {
            System.out.println("" + instructions[i]);
        }
    }
    @Override
    public Completion execute(ExecutionContext context) {
        Object result = Types.UNDEFINED;
        Object[] temps = new Object[scope.getTemporaryVariableSize()];
        Object[] vars = new Object[scope.getLocalVariableSize()];
        int size = instructions.length;
        Object value = Types.UNDEFINED;

        int ipc = 0;
        while (ipc < size) {
            Instruction instr = instructions[ipc];
            ipc++;
            System.out.println("EX: " + instr);

            if (instr instanceof Add) {
                value = add(context,
                        ((Add) instr).getLHS().retrieve(context, temps, vars),
                        ((Add) instr).getRHS().retrieve(context, temps, vars));
            } else if (instr instanceof Copy) {
                value = ((Copy) instr).getValue().retrieve(context, temps, vars);
            } else if (instr instanceof Jump) {
                ipc = ((Jump) instr).getTarget().getTargetIPC();
            } else if (instr instanceof Call) {
                Call call = (Call) instr;
                Object ref = call.getIdentifier().retrieve(context, temps, vars);
                Object function = Types.getValue(context, ref);
                Operand[] opers = call.getArgs();
                Object[] args = new Object[opers.length];

                for (int i = 0; i < args.length; i++) {
                    args[i] = opers[i].retrieve(context, temps, vars);
                }

                if (!(function instanceof JSFunction)) {
                    throw new ThrowException(context, context.createTypeError(ref + " is not calllable"));
                }

                Object thisValue = null;

                if (ref instanceof Reference) {
                    if (((Reference) ref).isPropertyReference()) {
                        thisValue = ((Reference) ref).getBase();
                    } else {
                        thisValue = ((EnvironmentRecord) ((Reference) ref).getBase()).implicitThisValue();
                    }
                }

                value = context.call(ref, (JSFunction) function, thisValue, args);
            } else if (instr instanceof LT) {
                Object arg1  = ((LT) instr).getArg1().retrieve(context, temps, vars);
                Object arg2  = ((LT) instr).getArg2().retrieve(context, temps, vars);
                Object r = Types.compareRelational(context, arg1, arg2, true);
                value = r == Types.UNDEFINED ? false : r;
            } else if (instr instanceof LE) {
                Object arg1  = ((LE) instr).getArg1().retrieve(context, temps, vars);
                Object arg2  = ((LE) instr).getArg2().retrieve(context, temps, vars);
                Object r = Types.compareRelational(context, arg1, arg2, true);
                value = r == Boolean.TRUE || r == Types.UNDEFINED ? false : r;
            } else if (instr instanceof BEQ) {
                BEQ beq = (BEQ) instr;
                Object arg1 = beq.getArg1().retrieve(context, temps, vars);
                Object arg2 = beq.getArg2().retrieve(context, temps, vars);

                if (arg1.equals(arg2)) {
                    ipc = beq.getTarget().getTargetIPC();
                }
            } else if (instr instanceof Return) {
                result = ((Return) instr).getValue().retrieve(context, temps, vars);
                break;
            }

            if (instr instanceof ResultInstruction) {
                Variable variable = ((ResultInstruction) instr).getResult();
                if (variable instanceof OffsetVariable) {
                    int offset = ((OffsetVariable) variable).getOffset();

                    if (variable instanceof LocalVariable) {
                        vars[offset] = value;
                    } else {
                        temps[offset] = value;
                    }
                } else {
                    // FIXME: Lookup dynamicvariable
                }
            }
        }

        System.out.println("RESULT is " + result);

        return Completion.createNormal(result);
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    public boolean isStrict() {
        return isStrict;
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

    private Object add(ExecutionContext context, Object lhs, Object rhs) {
        if (lhs instanceof String || rhs instanceof String) {
            return(Types.toString(context, lhs) + Types.toString(context, rhs));
        }

        Number lhsNum = Types.toNumber(context, lhs);
        Number rhsNum = Types.toNumber(context, rhs);

        if (Double.isNaN(lhsNum.doubleValue()) || Double.isNaN(rhsNum.doubleValue())) {
            return(Double.NaN);
        }

        if (lhsNum instanceof Double || rhsNum instanceof Double) {
            if (lhsNum.doubleValue() == 0.0 && rhsNum.doubleValue() == 0.0) {
                if (Double.compare(lhsNum.doubleValue(), 0.0) < 0 && Double.compare(rhsNum.doubleValue(), 0.0) < 0) {
                    return(-0.0);
                } else {
                    return(0.0);
                }
            }
            return(lhsNum.doubleValue() + rhsNum.doubleValue());
        }

        return(lhsNum.longValue() + rhsNum.longValue());
    }
}
