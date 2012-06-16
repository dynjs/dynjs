package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;

public class CatchClauseStatement extends BaseStatement implements Statement {
    private final String id;
    private final Statement block;

    public CatchClauseStatement(Tree tree, String id, Statement block) {
        super(tree);
        this.id = id;
        this.block = block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            // require frame
            // store exception
            // append block
            // pop frame
        }};
    }
}
