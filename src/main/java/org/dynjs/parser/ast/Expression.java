package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Position;

public interface Expression {
    Position getPosition();

    CodeBlock getCodeBlock();

    String dump(String indent);
}
