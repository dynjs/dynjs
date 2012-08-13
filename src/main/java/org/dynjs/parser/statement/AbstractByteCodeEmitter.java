package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;

public class AbstractByteCodeEmitter {

    public AbstractByteCodeEmitter() {

    }

    public CodeBlock jsResolve(final String identifier) {
        return new CodeBlock() {
            {
                // <EMPTY>
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                ldc( identifier );
                invokevirtual( p( ExecutionContext.class ), "resolve", sig( Reference.class, String.class ) );
                // reference
            }
        };
    }

    public CodeBlock jsPushUndefined() {
        return new CodeBlock() {
            {
                getstatic( p( Types.class ), "UNDEFINED", ci( Types.Undefined.class ) );
            }
        };
    }

    public CodeBlock jsToPrimitive() {
        return new CodeBlock() {
            {
                // IN: obj preferredType
                invokestatic( p( Types.class ), "toPrimitive", sig( Object.class, Object.class, String.class ) );
                // obj
            }
        };
    }

    public CodeBlock jsToNumber() {
        return new CodeBlock() {
            {
                // IN obj
                invokestatic( p( Types.class ), "toNumber", sig( Double.class, Object.class ) );
                // obj
            }
        };
    }

    public CodeBlock jsToBoolean() {
        return new CodeBlock() {
            {
                // IN obj
                invokestatic( p( Types.class ), "toBoolean", sig( Boolean.class, Object.class ) );
                // obj
            }
        };
    }

    public CodeBlock jsToInt32() {
        return new CodeBlock() {
            {
                // IN obj
                invokestatic( p( Types.class ), "toInt32", sig( Double.class, Object.class ) );
                // obj
            }
        };
    }

    public CodeBlock jsToUint32() {
        return new CodeBlock() {
            {
                // IN obj
                invokestatic( p( Types.class ), "toUint32", sig( Double.class, Object.class ) );
                // obj
            }
        };
    }

    public CodeBlock jsGetValue() {
        return jsGetValue( null );
    }

    public CodeBlock jsGetValue(Class<?> throwIfNot) {
        return new CodeBlock() {
            {
                // IN: reference
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // reference context
                swap();
                // context reference
                invokestatic( p( Types.class ), "getValue", sig( Object.class, ExecutionContext.class, Object.class ) );
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
                invokevirtual( p( Reference.class ), "getBase", sig( Object.class ) );
                // value
            }
        };
    }

    public CodeBlock jsToString() {
        return new CodeBlock() {
            {
                // IN: obj
                invokestatic( p( Types.class ), "toString", sig( String.class, Object.class ) );
            }
        };
    }

    public CodeBlock jsCreatePropertyReference() {
        return new CodeBlock() {
            {
                // IN: context obj identifier
                invokevirtual( p( ExecutionContext.class ), "createPropertyReference", sig( Reference.class, Object.class, String.class ) );

            }
        };
    }

}
