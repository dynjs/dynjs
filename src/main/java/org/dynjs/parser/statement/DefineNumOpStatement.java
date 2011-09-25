package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynNumber;
import org.dynjs.runtime.RT;

import static me.qmx.jitescript.CodeBlock.newCodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class DefineNumOpStatement implements Statement {

    private final String operation;
    private final Statement leftHandStatement;
    private final Statement rightHandStatement;

    public DefineNumOpStatement(String operation, Statement leftHandStatement, Statement rightHandStatement) {
        this.operation = operation;
        this.leftHandStatement = leftHandStatement;
        this.rightHandStatement = rightHandStatement;
    }

    @Override
    public CodeBlock getCodeBlock() {
        String instruction = "dynjs:bop:" + operation;
        return newCodeBlock()
                .append(leftHandStatement.getCodeBlock())
                .append(rightHandStatement.getCodeBlock())
                .invokedynamic(instruction, sig(DynNumber.class, DynAtom.class, DynAtom.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
    }
}
