package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Position;

public interface Expression {
    Position getPosition();
    CodeBlock getCodeBlock();
}
