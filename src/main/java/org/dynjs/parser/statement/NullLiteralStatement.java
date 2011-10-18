package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

public class NullLiteralStatement implements Statement {

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
                .aconst_null();
    }
}
