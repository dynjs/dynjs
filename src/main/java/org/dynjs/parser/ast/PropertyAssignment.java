package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

public abstract class PropertyAssignment extends AbstractByteCodeEmitter {
    
    private String name;

    public PropertyAssignment(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public abstract CodeBlock getCodeBlock();

}
