package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;

public class PropertySet extends PropertyAccessor {

    private String identifier;

    public PropertySet(Position position, String name, String identifier, Statement block) {
        super(position, name, block);
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit( context, this, strict );
    }
    
    public int getSizeMetric() {
        return super.getSizeMetric() + 1;
    }

    @Override
    public Object interpret(ExecutionContext context, boolean debug) {
        JSFunction compiledFn = ((ExecutionContext) context).getCompiler().compileFunction((ExecutionContext) context,
                null,
                new String[]{getIdentifier()},
                getBlock(),
                context.isStrict());
        return(compiledFn);
    }
}
