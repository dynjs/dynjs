package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;

public class BitwiseExpression extends AbstractBinaryExpression {

    public BitwiseExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super(tree, lhs, rhs, op);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append(getLhs().getCodeBlock());
                append(jsGetValue());
                if (getOp().equals(">>>")) {
                    append(jsToUint32());
                } else {
                    append(jsToInt32());
                }
                invokevirtual(p(Number.class), "longValue", sig(long.class));

                append(getRhs().getCodeBlock());
                append(jsGetValue());
                append(jsToUint32());
                invokevirtual(p(Number.class), "longValue", sig(long.class));
                // int int

                if (getOp().equals("<<")) {
                    //ldc(0x1F);
                    //land();
                    l2i();
                    lshl();
                } else if (getOp().equals(">>")) {
                    //ldc(0x1F);
                    //land();
                    l2i();
                    lshr();
                } else if (getOp().equals(">>>")) {
                    //ldc(0x1F);
                    //land();
                    l2i();
                    lushr();
                } else if (getOp().equals("&")) {
                    land();
                } else if (getOp().equals("|")) {
                    lor();
                } else if (getOp().equals("^")) {
                    lxor();
                }
                // long

                append(convertTopToLong());
                // Long
            }
        };
    }

}
