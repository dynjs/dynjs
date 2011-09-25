package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class NumberLiteralStatement implements Statement {

    private final String value;

    public NumberLiteralStatement(String value) {
        this.value = value;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock()
                .aload(1)
                .ldc(value)
                .invokevirtual(p(DynThreadContext.class), "defineDecimalLiteral", sig(DynPrimitiveNumber.class, String.class));
    }
}
