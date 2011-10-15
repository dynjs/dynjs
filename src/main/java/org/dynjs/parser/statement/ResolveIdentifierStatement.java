package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ResolveIdentifierStatement implements Statement {

    private final String name;

    public ResolveIdentifierStatement(String name) {
        this.name = name;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return newCodeBlock()
                .aload(1)
                .aload(2)
                .ldc(name)
                .invokedynamic("dynjs:scope:resolve", sig(Object.class, DynThreadContext.class, Scope.class, String.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
    }
}
