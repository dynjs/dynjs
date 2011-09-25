package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynThreadContext;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class StringLiteralStatement implements Statement {

    private final String literal;

    public StringLiteralStatement(String literal) {
        this.literal = literal;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock()
                .aload(1)
                .ldc(literal)
                .invokevirtual(p(DynThreadContext.class), "defineStringLiteral", sig(DynAtom.class, String.class));
    }
}
