package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public class AdditiveExpression extends AbstractBinaryExpression {

    public AdditiveExpression(final Tree tree, final Expression lhs, final Expression rhs, final String op) {
        super(tree, lhs, rhs, op);
    }

    @Override
    public CodeBlock getCodeBlock() {
        if (getOp().equals("+")) {
            return getCodeBlockForPlus();
        } else {
            return getCodeBlockForMinus();
        }
    }

    public CodeBlock getCodeBlockForPlus() {
        return new CodeBlock() {
            {
                LabelNode intNums = new LabelNode();
                LabelNode doubleNums = new LabelNode();

                LabelNode stringConcatByLeft = new LabelNode();
                LabelNode stringConcat = new LabelNode();
                LabelNode end = new LabelNode();

                append(getLhs().getCodeBlock());
                // ref(lhs)
                append(jsGetValue());
                // val(lhs)
                aconst_null();
                // val(lhs) null
                append(jsToPrimitive());
                // val(lhs)
                dup();
                // val(lhs) val(lhs)
                instance_of(p(String.class));
                // val(lhs) bool
                iftrue(stringConcatByLeft);

                append(getRhs().getCodeBlock());
                // val(lhs) ref(rhs)
                append(jsGetValue());
                // val(lhs) val(rhs)
                aconst_null();
                // val(lhs) val(rhs) null
                append(jsToPrimitive());
                // val(lhs) val(rhs)
                dup();
                // val(lhs) val(rhs) val(rhs)
                instance_of(p(String.class));
                // val(lhs) val(rhs) bool
                iftrue(stringConcat);

                // ----------------------------------------
                // Numbers
                
                
                // val(lhs) val(rhs)
                append( jsToNumber() );
                swap();
                append( jsToNumber() );
                swap();
                // num(lhs) num(rhs)

                // Number(lhs) Number(rhs)
                append(ifEitherIsDouble(doubleNums));

                // ----------------------------------------------
                // Integral

                append(convertTopTwoToPrimitiveLongs());
                // long(lhs) long(rhs)
                ladd();
                // num(total)
                append(convertTopToLong());
                // Long(total)
                go_to(end);

                // ----------------------------------------------
                // Double

                label(doubleNums);
                // (doubles) Number(lhs) Number(rhs)
                append(convertTopTwoToPrimitiveDoubles());
                // double(lhs) double(rhs)
                dadd();
                // num(total)
                append(convertTopToDouble());
                // Double(total)
                go_to(end);

                // ----------------------------------------
                // Strings forced by LHS
                label(stringConcatByLeft);
                // val(lhs)
                append(getRhs().getCodeBlock());
                // val(lhs) ref(rhs)
                append(jsGetValue());
                // val(lhs) val(rhs)
                aconst_null();
                // val(lhs) val(rhs) null
                append(jsToPrimitive());
                // val(lhs) val(rhs);

                // ----------------------------------------
                // Strings
                label(stringConcat);
                // val(lhs) val(rhs)
                append( jsToString() );
                // val(lhs) str(rhs)
                swap();
                // str(rhs) val(lhs)
                append( jsToString() );
                // str(lhs) str(lhs)
                swap();
                // str(lhs) str(rhs)
                invokevirtual(p(String.class), "concat", sig(String.class, String.class));
                // obj(concat)

                // ----------------------------------------
                label(end);
                nop();
            }
        };

    }

    public CodeBlock getCodeBlockForMinus() {
        return new CodeBlock() {
            {
                LabelNode doubleNums = new LabelNode();
                LabelNode end = new LabelNode();

                append(getLhs().getCodeBlock());
                // obj(lhs)
                append(jsGetValue());
                // val(lhs)
                append(jsToNumber());
                append(getRhs().getCodeBlock());
                // val(lhs) obj(rhs)
                append(jsGetValue());
                // val(lhs) val(rhs)
                append(jsToNumber());

                append(ifEitherIsDouble(doubleNums));

                // -------------------------------------
                // Integral

                append(convertTopTwoToPrimitiveLongs());
                lsub();
                append(convertTopToLong());
                go_to(end);

                // -------------------------------------
                // Double

                label(doubleNums);
                append(convertTopTwoToPrimitiveDoubles());
                dsub();
                append(convertTopToDouble());

                label(end);

            }
        };

    }

}
