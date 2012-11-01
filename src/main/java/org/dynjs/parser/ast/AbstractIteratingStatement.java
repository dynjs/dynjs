package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public abstract class AbstractIteratingStatement extends AbstractStatement {

    public AbstractIteratingStatement(Tree tree) {
        super(tree);
    }

    public CodeBlock isInLabelSet() {
        return new CodeBlock() {
            {
                LabelNode returnTrue = new LabelNode();
                LabelNode end = new LabelNode();

                // IN: target
                
                dup();
                // target target
                ifnull( returnTrue );
                
                List<String> labels = getLabels();
                int numLabels = labels.size();
                // target
                
                bipush(numLabels);
                // target int
                anewarray(p(String.class));
                // target array

                for (int i = 0; i < numLabels; ++i) {
                    dup();
                    // target array array
                    bipush(i);
                    // target array array idx
                    ldc(labels.get(i));
                    // target array array idx val
                    aastore();
                    // target array
                }

                // target array

                invokestatic(p(AbstractIteratingStatement.class), "isInLabelSet", sig(boolean.class, String.class, String[].class));
                // boolean
                
                go_to( end );
                
                label( returnTrue );
                // target
                pop();
                // <empty>
                
                iconst_1();
                // true
                
                label( end );
                // boolean

            }
        };
    }

    public static boolean isInLabelSet(String target, String[] labelSet) {
        for (int i = 0; i < labelSet.length; ++i) {
            if (labelSet[i].equals(target)) {
                return true;
            }
        }

        return false;
    }

}
