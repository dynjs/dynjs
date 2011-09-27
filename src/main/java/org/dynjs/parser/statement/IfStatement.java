package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
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
                .append(vbool.getCodeBlock())
                .append(thenFn.getCodeBlock())
                .append(elseFn.getCodeBlock())
                .invokedynamic("dynjs:compile:if", sig(Function.class, DynAtom.class, Function.class, Function.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .aload(1)
                .aload(2)
                .aload(3)
                .invokedynamic("dynjs:runtime:call", sig(DynAtom.class, Function.class, DynThreadContext.class, Scope.class, DynAtom[].class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS)
                .areturn();
        return codeBlock;
    }
}
