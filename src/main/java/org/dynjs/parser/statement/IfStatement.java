package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.util.Collections;

import static me.qmx.jitescript.util.CodegenUtils.sig;

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
        final FunctionStatement thenFn = new FunctionStatement(context, Collections.<String>emptyList(), this.vthen);
        final FunctionStatement elseFn = new FunctionStatement(context, Collections.<String>emptyList(), this.velse);
        CodeBlock codeBlock = CodeBlock.newCodeBlock()
                .invokedynamic("dynjs:compile:if", sig(void.class, DynAtom.class, Function.class, Function.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
                .append(vbool.getCodeBlock())
                .append(thenFn.getCodeBlock())
                .append(elseFn.getCodeBlock())
        return codeBlock;
    }
}
