package org.dynjs.ir;

import org.dynjs.exception.ThrowException;
import org.dynjs.ir.instructions.Add;
import org.dynjs.ir.instructions.BEQ;
import org.dynjs.ir.instructions.Call;
import org.dynjs.ir.instructions.Constructor;
import org.dynjs.ir.instructions.Copy;
import org.dynjs.ir.instructions.DefineFunction;
import org.dynjs.ir.instructions.Instanceof;
import org.dynjs.ir.instructions.Jump;
import org.dynjs.ir.instructions.LE;
import org.dynjs.ir.instructions.LT;
import org.dynjs.ir.instructions.Raise;
import org.dynjs.ir.instructions.ReceiveFunctionParameter;
import org.dynjs.ir.instructions.ResultInstruction;
import org.dynjs.ir.instructions.Return;
import org.dynjs.ir.instructions.Sub;
import org.dynjs.ir.operands.LocalVariable;
import org.dynjs.ir.operands.OffsetVariable;
import org.dynjs.ir.operands.Variable;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.ObjectEnvironmentRecord;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;

/**
 * Created by enebo on 4/11/14.
 */
public class Interpreter {

    public static Object execute(ExecutionContext context, Scope scope, Instruction[] instructions) {
        //System.out.println("Executing: " + context.getFileName() + ":" + context.getLineNumber());
        Object result = Types.UNDEFINED;
        Object[] temps = new Object[scope.getTemporaryVariableSize()];
        int size = instructions.length;
        Object value = Types.UNDEFINED;

        int ipc = 0;
        while (ipc < size) {
            Instruction instr = instructions[ipc];
            ipc++;
            //System.out.println("EX: " + instr);

            switch(instr.getOperation()) {
                case ADD:
                    value = add(context,
                            ((Add) instr).getLHS().retrieve(context, temps),
                            ((Add) instr).getRHS().retrieve(context, temps));
                    break;
                case SUB:
                    value = sub(context,
                            ((Sub) instr).getLHS().retrieve(context, temps),
                            ((Sub) instr).getRHS().retrieve(context, temps));
                    break;
                case RECEIVE_FUNCTION_PARAM:
                    value = context.getFunctionParameters()[((ReceiveFunctionParameter) instr).getIndex()];
                    break;
                case COPY:
                    value = ((Copy) instr).getValue().retrieve(context, temps);
                    break;
                case JUMP:
                    ipc = ((Jump) instr).getTarget().getTargetIPC();
                    break;
                case CALL: {
                    Call call = (Call) instr;
                    Object ref = call.getIdentifier().retrieve(context, temps);
                    Object function = Types.getValue(context, ref);
                    Operand[] opers = call.getArgs();
                    Object[] args = new Object[opers.length];

                    if (!(function instanceof JSFunction)) {
                        throw new ThrowException(context, context.createTypeError(ref + " is not callable"));
                    }

                    for (int i = 0; i < args.length; i++) {
                        args[i] = opers[i].retrieve(context, temps);
                    }

                    Object thisValue = getThis(ref);

                    value = context.call(ref, (JSFunction) function, thisValue, args);
                }
                break;
                case CONSTRUCTOR: {
                    Constructor constructor = (Constructor) instr;
                    Object ref = constructor.getIdentifier().retrieve(context, temps);
                    Object function = Types.getValue(context, ref);
                    Operand[] opers = constructor.getArgs();
                    Object[] args = new Object[opers.length];

                    if (!(function instanceof JSFunction)) {
                        throw new ThrowException(context, context.createTypeError(ref + " is not callable"));
                    }

                    for (int i = 0; i < args.length; i++) {
                        args[i] = opers[i].retrieve(context, temps);
                    }

                    value = context.construct(ref, (JSFunction) function, args);
                }
                break;
                case LT: {
                    Object arg1  = ((LT) instr).getArg1().retrieve(context, temps);
                    Object arg2  = ((LT) instr).getArg2().retrieve(context, temps);
                    Object r = Types.compareRelational(context, arg1, arg2, true);
                    value = r == Types.UNDEFINED ? false : r;
                    break;
                }
                case LE: {
                    Object arg1  = ((LE) instr).getArg1().retrieve(context, temps);
                    Object arg2  = ((LE) instr).getArg2().retrieve(context, temps);
                    Object r = Types.compareRelational(context, arg1, arg2, true);
                    value = r == Boolean.TRUE || r == Types.UNDEFINED ? false : r;
                    break;
                }
                case BEQ: {
                    BEQ beq = (BEQ) instr;
                    Object arg1 = beq.getArg1().retrieve(context, temps);
                    Object arg2 = beq.getArg2().retrieve(context, temps);

                    if (arg1.equals(arg2)) {
                        ipc = beq.getTarget().getTargetIPC();
                    }
                    break;
                }
                case RETURN:
                    return ((Return) instr).getValue().retrieve(context, temps);
                case DEFINE_FUNCTION: {
                    // FIXME: configurableBindings is false here but I think a define_function in an eval they should be true
                    FunctionScope functionScope = ((DefineFunction) instr).getScope();
                    value = new IRJSFunction(functionScope, context.getVars(),
                            context.getLexicalEnvironment(), context.getGlobalContext());

                    if (functionScope.getName() != null) {
                        EnvironmentRecord env = context.getVariableEnvironment().getRecord();
                        String identifier = functionScope.getName();
                        if (!env.hasBinding(context, identifier)) {
                            env.createMutableBinding(context, identifier, false);
                        } else if (env.isGlobal()) {
                            JSObject globalObject = ((ObjectEnvironmentRecord) env).getBindingObject();
                            PropertyDescriptor existingProp = (PropertyDescriptor) globalObject.getProperty(context, identifier, false);
                            if (existingProp.isConfigurable()) {
                                globalObject.defineOwnProperty(context, identifier,
                                        PropertyDescriptor.newDataPropertyDescriptor(Types.UNDEFINED, true, false, true), true);
                            } else if (existingProp.isAccessorDescriptor() || (!existingProp.isWritable() && !existingProp.isEnumerable())) {
                                throw new ThrowException(context, context.createTypeError("unable to bind function '" + identifier + "'"));
                            }
                        }

                        ((JSFunction) value).setDebugContext(identifier);
                        env.setMutableBinding(context, identifier, value, functionScope.isStrict());
                    }
                    break;
                }
                case RAISE:
                    throw new ThrowException(context,
                            context.createError(((Raise) instr).getType(), ((Raise) instr).getMessage()));
                case INSTANCEOF: {
                    Object lhs  = ((Instanceof) instr).getLHS().retrieve(context, temps);
                    Object rhs  = ((Instanceof) instr).getRHS().retrieve(context, temps);

                    if (rhs instanceof JSObject) {
                        if (!(rhs instanceof JSFunction)) {
                            // FIXME: Might have to pass in original rhs to instr for proper string...
                            throw new ThrowException(context, context.createTypeError(rhs + " is not a function"));
                        }

                        value = ((JSFunction) rhs).hasInstance(context, lhs);
                    } else if (rhs instanceof Class) {
                        Class clazz = (Class) rhs;
                        value = lhs.getClass().getName().equals(clazz.getName());
                    } else {
                        // FIXME: Review against master...
                        throw new ThrowException(context, context.createTypeError(rhs + " is not a function"));
                    }
                }
            }

            if (instr instanceof ResultInstruction) {
                Variable variable = ((ResultInstruction) instr).getResult();
                if (variable instanceof OffsetVariable) {
                    int offset = ((OffsetVariable) variable).getOffset();

                    if (variable instanceof LocalVariable) {
                        context.getVars().setVar(offset, ((LocalVariable) variable).getDepth(), value);
                    } else {
                        temps[offset] = value;
                    }
                } else {
                    // FIXME: Lookup dynamicvariable
                }
            }
        }

        System.out.println("RESULT is " + result);

        return result;
    }

    // FIXME: move to helper class
    public static Object getThis(Object ref) {
        Object thisValue = null;

        // FIXME: We can probably remove this check for IRJSFunctions since we will not be using references
        if (ref instanceof Reference) {
            if (((Reference) ref).isPropertyReference()) {
                thisValue = ((Reference) ref).getBase();
            } else {
                thisValue = ((EnvironmentRecord) ((Reference) ref).getBase()).implicitThisValue();
            }
        }
        return thisValue;
    }

    // FIXME: This breaks for non-numeric uses if isSubtraction
    private static Object add(ExecutionContext context, Object lhs, Object rhs) {
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

            return(lhsNum.doubleValue() - rhsNum.doubleValue());
        }

        return(lhsNum.longValue() + rhsNum.longValue());
    }

    private static Object sub(ExecutionContext context, Object lhs, Object rhs) {
        if (lhs instanceof String || rhs instanceof String) {
            return(Double.NaN);
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

            return(lhsNum.doubleValue() - rhsNum.doubleValue());
        }

        return(lhsNum.longValue() - rhsNum.longValue());
    }
}
