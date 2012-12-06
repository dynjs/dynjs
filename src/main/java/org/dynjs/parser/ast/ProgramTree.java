package org.dynjs.parser.ast;

import java.util.List;

import org.dynjs.parser.Statement;

public class ProgramTree extends BlockStatement {

    private boolean strict;

    public ProgramTree(final List<Statement> blockContent, boolean strict) {
        super(blockContent);
        this.strict = strict;
    }
    
    public ProgramTree(boolean strict, List<Statement> blockContent) {
        super(blockContent);
        this.strict = strict;
    }
    
    public boolean isStrict() {
        return this.strict;
    }
    
    public boolean isEmpty() {
        return getBlockContent().isEmpty();
    }
    

}
