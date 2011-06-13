package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

public class LdcStatement implements Statement {

    private final CodeBlock codeBlock;

    public LdcStatement(final Object arg0){
        this.codeBlock = new CodeBlock(){{
            ldc(arg0);
        }};
    }

    @Override
    public CodeBlock getCodeBlock() {
        return codeBlock;
    }
}
