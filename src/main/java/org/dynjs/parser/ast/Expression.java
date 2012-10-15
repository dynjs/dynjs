package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Position;
import org.dynjs.runtime.ExecutionContext;

public interface Expression {
    Position getPosition();

    CodeBlock getCodeBlock();

    String dump(String indent);
    
    void verify(ExecutionContext context, boolean strict);
}
