package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

public class PropertyGet extends PropertyAccessor {

    public PropertyGet(String name, Statement block) {
        super(name, block);
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }

}
