package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class NumberLiteralStatement implements Statement {

    private final String value;
    private final int radix;

    public NumberLiteralStatement(String value, int radix) {
        this.value = value;
        this.radix = radix;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
                .aload(1)
                .ldc(value)
                .invokevirtual(p(DynThreadContext.class), getFactoryMethod(), sig(Number.class, String.class));
    }

    private String getFactoryMethod() {
        switch (this.radix) {
            case 8:
                return "defineOctalLiteral";
            case 10:
                return "defineDecimalLiteral";
            case 16:
                return "defineHexaDecimalLiteral";
            default:
                throw new IllegalArgumentException("unsupported radix");
        }
    }
}
