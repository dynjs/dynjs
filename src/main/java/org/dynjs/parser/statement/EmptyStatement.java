package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;

public class EmptyStatement implements Statement {

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock();
    }
}
