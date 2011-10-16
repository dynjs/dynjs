package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.util.List;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class CallStatement implements Statement {

    private final DynThreadContext context;
    private final Statement lhs;
    private final List<Statement> args;

    public CallStatement(DynThreadContext context, Statement lhs, List<Statement> args) {
        this.context = context;
        this.lhs = lhs;
        this.args = args;
    }

    @Override
    public CodeBlock getCodeBlock() {
        CodeBlock codeBlock = newCodeBlock();
        codeBlock = codeBlock
                .bipush(args.size())
                .anewarray(p(Object.class))
                .astore(4);

        for (int i = 0; i < args.size(); i++) {
            codeBlock = codeBlock
                    .aload(4)
                    .bipush(i)
                    .append(args.get(i).getCodeBlock())
                    .aastore();
        }

        codeBlock = codeBlock
                .append(lhs.getCodeBlock())
                .aload(1)
                .aload(2)
                .aload(4)
                .invokedynamic("dynjs:runtime:call", sig(Object.class, Function.class, DynThreadContext.class, Scope.class, Object[].class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);

        return codeBlock;
    }
}
