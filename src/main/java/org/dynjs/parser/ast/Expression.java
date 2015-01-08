package org.dynjs.parser.ast;

import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public interface Expression {
    Position getPosition();

    String dump(String indent);
    
    Object accept(Object context, CodeVisitor visitor, boolean strict);
    
    int getSizeMetric();
    
    List<FunctionDeclaration> getFunctionDeclarations();

    Object interpret(ExecutionContext context, boolean debug);
}
