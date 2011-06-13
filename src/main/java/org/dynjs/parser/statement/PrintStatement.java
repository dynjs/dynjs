package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;

import java.io.PrintStream;

import static me.qmx.jitescript.util.CodegenUtils.*;

public class PrintStatement implements Statement {

    private final CodeBlock codeBlock;

    public PrintStatement(final Statement expression) {
        this.codeBlock = new CodeBlock() {{
            append(expression.getCodeBlock());
            getstatic(p(System.class), "out", ci(PrintStream.class));
            swap();
            invokevirtual(p(PrintStream.class), "println", sig(void.class, Object.class));
            voidreturn();
        }};
    }

    @Override
    public CodeBlock getCodeBlock() {
        return this.codeBlock;
    }
}
