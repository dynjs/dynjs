package org.dynjs.parser.ast;

import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.SourceProvider;

public class ProgramTree extends BlockStatement {

    private SourceProvider source;
    private boolean strict;

    public ProgramTree(final List<Statement> blockContent, boolean strict) {
        super(blockContent);
        this.strict = strict;
    }
    
    public ProgramTree(boolean strict, List<Statement> blockContent) {
        super(blockContent);
        this.strict = strict;
    }

    public void setSource(SourceProvider source) {
        this.source = source;
    }

    public SourceProvider getSource() {
        return this.source;
    }
    
    public boolean isStrict() {
        return this.strict;
    }
    
    public boolean isEmpty() {
        return getBlockContent().isEmpty();
    }
    

}
