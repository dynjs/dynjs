package org.dynjs.debugger;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.DefaultVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.*;
import org.dynjs.runtime.SourceProvider;

import java.util.Map;

/**
 * @author Bob McWhirter
 */
public class DebugCollectingVisitor extends DefaultVisitor {

    private final Map<Statement, SourceProvider> sourceIndex;
    private final SourceProvider source;

    public DebugCollectingVisitor(Map<Statement,SourceProvider> sourceIndex, SourceProvider source) {
        this.sourceIndex = sourceIndex;
        this.source = source;
    }

    @Override
    public Object visit(Object context, FunctionDeclaration statement, boolean strict) {
        this.sourceIndex.put( statement, this.source );
        super.visit(context, statement, strict );
        return null;
    }

    @Override
    public Object visit(Object context, FunctionExpression expr, boolean strict) {
        super.visit(context, expr, strict);
        return null;
    }

}
