package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

public class IfStatement implements Statement {

    private final DynThreadContext context;
    private final Statement vbool;
    private final Statement vthen;
    private final Statement velse;

    public IfStatement(DynThreadContext context, Statement vbool, Statement vthen, Statement velse) {
        this.context = context;
        this.vbool = vbool;
        this.vthen = vthen;
        this.velse = velse;
    }

    @Override
    public CodeBlock getCodeBlock() {
        int vboolSlot = context.store(vbool.getCodeBlock());
        int vthenSlot = context.store(vthen.getCodeBlock());
        int velseSlot = context.store(velse.getCodeBlock());
        return CodeBlock.newCodeBlock()
                ;
    }
}
