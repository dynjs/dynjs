package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;

public class BooleanLiteralStatement implements Statement {

    private final String value;

    public BooleanLiteralStatement(String value) {
        this.value = value;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
                .getstatic(p(Boolean.class), value, ci(Boolean.class));
    }
}
