package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class AssignmentOperationStatement implements Statement {
    private final Statement lhs;
    private final Statement rhs;

    public AssignmentOperationStatement(Statement lhs, Statement rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public CodeBlock getCodeBlock() {
        if (lhs instanceof ResolveByIndexStatement) {
            ResolveByIndexStatement statement = (ResolveByIndexStatement) lhs;
            Statement targetObject = statement.getLhs();
            Statement index = statement.getIndex();
            return CodeBlock.newCodeBlock()
                    .append(targetObject.getCodeBlock())
                    .append(index.getCodeBlock())
                    .append(rhs.getCodeBlock())
                    .invokedynamic("dyn:setElement", sig(void.class, Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
        }
        return null;
    }
}
