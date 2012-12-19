package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

public class PropertySet extends PropertyAccessor {

    private String identifier;

    public PropertySet(String name, String identifier, Statement block) {
        super(name, block);
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
    
    public int getSizeMetric() {
        return super.getSizeMetric() + 1;
    }
}
