package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class AbstractByteCodeEmitter {

    public AbstractByteCodeEmitter() {

    }

    public CodeBlock jsResolve(final String identifier) {
        return new CodeBlock() {
            {
                // <EMPTY>
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                ldc(identifier);
                invokevirtual(p(ExecutionContext.class), "resolve", sig(Reference.class, String.class));
                // reference
            }
        };
    }

    public CodeBlock jsPushUndefined() {
        return new CodeBlock() {
            {
                getstatic(p(Types.class), "UNDEFINED", ci(Types.Undefined.class));
            }
        };
    }
    
    public CodeBlock jsPushNull() {
        return new CodeBlock() {
            {
                getstatic(p(Types.class), "NULL", ci(Types.Null.class));
            }
        };
    }

    public CodeBlock jsToPrimitive() {
        return new CodeBlock() {
            {
                // IN: obj preferredType
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj preferredType context
                dup_x2();
                // context obj preferredType context
                pop();
                // context obj preferredType
                invokestatic(p(Types.class), "toPrimitive", sig(Object.class, ExecutionContext.class, Object.class, String.class));
                // obj
            }
        };
    }

    public CodeBlock jsToNumber() {
        return new CodeBlock() {
            {
                // IN obj
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj context
                swap();
                // context obj
                invokestatic(p(Types.class), "toNumber", sig(Number.class, ExecutionContext.class, Object.class));
                // obj
            }
        };
    }

    public CodeBlock jsToBoolean() {
        return new CodeBlock() {
            {
                // IN obj
                invokestatic(p(Types.class), "toBoolean", sig(Boolean.class, Object.class));
                // obj
            }
        };
    }

    public CodeBlock jsToInt32() {
        return new CodeBlock() {
            {
                // IN obj
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj context
                swap();
                // context obj
                invokestatic(p(Types.class), "toInt32", sig(Integer.class, ExecutionContext.class, Object.class));
                // obj
            }
        };
    }

    public CodeBlock jsToUint32() {
        return new CodeBlock() {
            {
                // IN obj
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj context
                swap();
                // context obj
                invokestatic(p(Types.class), "toUint32", sig(Integer.class, ExecutionContext.class, Object.class));
                // obj
            }
        };
    }

    public CodeBlock jsToObject() {
        return new CodeBlock() {
            {
                // IN obj
                aload( JSCompiler.Arities.EXECUTION_CONTEXT);
                // obj context
                swap();
                // context obj
                invokestatic(p(Types.class), "toObject", sig(JSObject.class, ExecutionContext.class, Object.class));
                // obj
            }
        };
    }

    public CodeBlock jsGetValue() {
        return jsGetValue(null);
    }

    public CodeBlock jsGetValue(Class<?> throwIfNot) {
        return new CodeBlock() {
            {
                // IN: reference
                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                // reference context
                swap();
                // context reference
                invokestatic(p(Types.class), "getValue", sig(Object.class, ExecutionContext.class, Object.class));
                // value
                // FIXME: handle throwing a TypeError if not throwIfNot
            }
        };
    }

    public CodeBlock jsGetBase() {
        return new CodeBlock() {
            {
                // IN: reference
                // reference
                invokevirtual(p(Reference.class), "getBase", sig(Object.class));
                // value
            }
        };
    }
    
    

    public CodeBlock jsToString() {
        return new CodeBlock() {
            {
                // IN: obj
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj context
                swap();
                // context obj
                invokestatic(p(Types.class), "toString", sig(String.class, ExecutionContext.class, Object.class));
            }
        };
    }

    public CodeBlock jsCreatePropertyReference() {
        return new CodeBlock() {
            {
                // IN: context obj identifier
                invokevirtual(p(ExecutionContext.class), "createPropertyReference", sig(Reference.class, Object.class, String.class));

            }
        };
    }

    public CodeBlock jsThrowTypeError(final String message) {
        return new CodeBlock() {
            {
                newobj(p(ThrowException.class));
                // obj
                dup();
                // obj obj
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj obj context
                ldc( message );
                // obj obj context message 
                invokevirtual(p(ExecutionContext.class), "createTypeError", sig(JSObject.class, String.class));
                // obj obj ex
                invokespecial(p(ThrowException.class), "<init>", sig(void.class, Object.class));
                // obj
                athrow();
            }
        };
    }

    public CodeBlock jsThrowReferenceError(final String message) {
        return new CodeBlock() {
            {
                newobj(p(ThrowException.class));
                // obj
                dup();
                // obj obj
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // obj obj context
                ldc( message );
                // obj obj context message 
                invokevirtual(p(ExecutionContext.class), "createReferenceError", sig(JSObject.class, String.class));
                // obj obj ex
                invokespecial(p(ThrowException.class), "<init>", sig(void.class, Object.class));
                // obj
                athrow();
            }
        };
    }

    public CodeBlock jsThrowSyntaxError() {
        return new CodeBlock() {
            {
                invokestatic(p(ExecutionContext.class), "throwSyntaxError", sig(void.class));
            }
        };
    }

    public CodeBlock ifEitherIsDouble(final LabelNode target) {
        // IN: Number Number
        return new CodeBlock() {
            {
                checkcast(p(Number.class));
                swap();
                // val(rhs) Number(lhs)
                checkcast(p(Number.class));
                swap();
                // Number(lhs) Number(rhs)
                dup();
                // Number(lhs) Number(rhs) Number(rhs)
                instance_of(p(Double.class));
                // Number(lhs) Number(rhs) bool
                iftrue(target);
                // Number(lhs) Number(rhs)
                swap();
                // Number(rhs) Number(lhs)
                dup_x1();
                // Number(lhs) Number(rhs) Number(lhs)
                instance_of(p(Double.class));
                // Number(lhs) Number(rhs) bool
                iftrue(target);
                // Number(lhs) Number(rhs)
            }
        };
    }

    public CodeBlock ifBothAreString(final LabelNode target) {
        return new CodeBlock() {
            {
                LabelNode end = new LabelNode();
                // IN: obj(lhs) obj(rhs)
                dup();
                // obj(lhs) obj(rhs) obj(rhs)
                instance_of(p(String.class));
                // obj(lhs) obj(rhs) bool(rhs)
                iffalse(end);
                // obj(lhs) obj(rhs)
                swap();
                // obj(rhs) obj(lhs)
                dup_x1();
                // obj(lhs) obj(rhs) obj(lhs)
                instance_of(p(String.class));
                // obj(lhs) obj(rhs) bool(lhs)
                iftrue(target);
                // obj(lhs) obj(rhs)
                label(end);
                // obj(lhs) obj(rhs)

            }
        };
    }

    public CodeBlock convertTopTwoToPrimitiveInts() {
        return new CodeBlock() {
            {
                // IN: Number Number
                invokevirtual(p(Number.class), "intValue", sig(int.class));
                // Number(lhs) int(rhs)
                swap();
                // int(rhs) Number(rhs)
                invokevirtual(p(Number.class), "intValue", sig(int.class));
                // int(rhs) int(lhs)
                swap();
                // int(lhs) int(rhs);
            }
        };
    }

    public CodeBlock convertTopToInteger() {
        return new CodeBlock() {
            {
                // IN: int
                invokestatic(p(Integer.class), "valueOf", sig(Integer.class, int.class));
            }
        };
    }

    public CodeBlock convertTopTwoToPrimitiveDoubles() {
        return new CodeBlock() {
            {
                // IN Number Number
                checkcast(p(Number.class));
                swap();
                checkcast(p(Number.class));
                swap();
                invokevirtual(p(Number.class), "doubleValue", sig(double.class));
                // Number(lhs) double(rhs)
                dup2_x1();
                // double(rhs) Number(lhs) double(rhs);
                pop2();
                // double(rhs) Number(lhs)
                invokevirtual(p(Number.class), "doubleValue", sig(double.class));
                // double(rhs) double(lhs)
                swap2();
                // OUT double double
            }
        };
    }

    public CodeBlock convertTopToDouble() {
        return new CodeBlock() {
            {
                // IN: int
                invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
            }
        };
    }
}
