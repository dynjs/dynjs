package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.js.Position;
import org.objectweb.asm.tree.LabelNode;

public abstract class AbstractIteratingStatement extends BaseStatement {

    public AbstractIteratingStatement(Position position) {
        super( position );
    }

    public CodeBlock isInLabelSet() {
        LabelNode returnTrue = new LabelNode();
        LabelNode end = new LabelNode();
        CodeBlock codeBlock = new CodeBlock()
            // IN: target
            .dup()
            // target target
            .ifnull( returnTrue );
                
        List<String> labels = getLabels();
        int numLabels = labels.size();
        // target
                
        codeBlock.bipush(numLabels)
            // target int
            .anewarray(p(String.class));
            // target array

        for (int i = 0; i < numLabels; ++i) {
            codeBlock
                .dup()
                // target array array
                .bipush(i)
                // target array array idx
                .ldc(labels.get(i))
                // target array array idx val
                .aastore();
                // target array
        }

        // target array

        codeBlock.invokestatic(p(AbstractIteratingStatement.class), "isInLabelSet", sig(boolean.class, String.class, String[].class))
            // boolean
                
            .go_to( end )
                
            .label( returnTrue )
            // target
            .pop()
            // <empty>
                
            .iconst_1()
            // true
                
            .label( end );
            // boolean

        return codeBlock;
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
