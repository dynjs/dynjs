package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.ci;
import static me.qmx.jitescript.util.CodegenUtils.p;

public class UndefinedValueStatement implements Statement {

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock()
                .getstatic(p(DynThreadContext.class), "UNDEFINED", ci(Object.class));
    }
}
