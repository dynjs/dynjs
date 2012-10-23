package org.dynjs.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

public class JavascriptTree extends CommonTree {
    
    private short STRICT = 1;
    
    private short flags = 0;
    
    
    public JavascriptTree() { 
        
    }
    
    public JavascriptTree(CommonTree node) {
        super(node);
    }
    
    public JavascriptTree(Token payload) {
        super( payload );
    }
    
    public void setStrict(boolean strict) {
        if ( strict ) {
            this.flags = (short) (this.flags | STRICT);
        }
    }
    
    public boolean isStrict() {
        return ( this.flags & STRICT ) != 0;
    }

}
