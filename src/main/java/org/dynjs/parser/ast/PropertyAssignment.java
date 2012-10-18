package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;

public abstract class PropertyAssignment extends AbstractByteCodeEmitter {

    private String name;

    public PropertyAssignment(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract CodeBlock getCodeBlock();
    
    public abstract void accept(ExecutionContext context, CodeVisitor visitor, boolean strict);

}
