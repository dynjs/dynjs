package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynObject;

import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ObjectLiteralStatement implements Statement {

    private final List<Statement> namedValues;

    public ObjectLiteralStatement(List<Statement> namedValues) {
        this.namedValues = namedValues;
    }

    @Override
    public CodeBlock getCodeBlock() {
        CodeBlock obj = CodeBlock.newCodeBlock()
                .newobj(p(DynObject.class))
                .dup()
                .invokespecial(p(DynObject.class), "<init>", sig(void.class))
                .astore(7);
        for (Statement namedValue : namedValues) {

            obj = obj.aload(7)
                    .append(namedValue.getCodeBlock());
        }
        return obj.aload(7);
    }
}
