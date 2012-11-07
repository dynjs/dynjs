package org.dynjs.parser.ast;

import java.util.List;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.ES3Parser.block_return;
import org.dynjs.parser.JavascriptTree;
import org.dynjs.parser.Statement;

public class Program extends BlockStatement {

    private boolean strict;

    public Program(Tree tree, final List<Statement> blockContent) {
        super(tree, blockContent);
        this.strict = ((JavascriptTree)tree).isStrict();
    }
    
    public Program(boolean strict, List<Statement> blockContent) {
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
