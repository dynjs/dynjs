package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Position;
import org.dynjs.runtime.ExecutionContext;

public interface Expression {
    Position getPosition();

    CodeBlock getCodeBlock();

    String dump(String indent);
    
    void accept(ExecutionContext context, CodeVisitor visitor, boolean strict);
}
