package org.dynjs.runtime;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.objectweb.asm.tree.LabelNode;

public class Completion {

    public static final Completion NORMAL_COMPLETION = new Completion( Type.NORMAL, null, null );

    public enum Type {
        NORMAL,
        BREAK,
        CONTINUE,
        RETURN,
        THROW
    }

    public Type type;
    public Object value;
    public String target;

    public Completion(Type type, Object value, String target) {
        this.type = type;
        this.value = value;
        this.target = target;
    }

    public static Completion createNormal() {
        return NORMAL_COMPLETION;
    }

    public static Completion createNormal(Object value) {
        return new Completion( Type.NORMAL, value, null );
    }

    public static Completion createBreak() {
        return new Completion( Type.BREAK, null, null );
    }

    public static Completion createBreak(String target) {
        return new Completion( Type.BREAK, null, target );
    }

    public static Completion createContinue(String target) {
        return new Completion( Type.CONTINUE, null, target );
    }

    public static Completion createReturn(Object value) {
        return new Completion( Type.RETURN, value, null );
    }

    public static Completion createThrow(Object value) {
        return new Completion( Type.THROW, value, null );
    }

    public static CodeBlock handle(final LabelNode normalTarget, final LabelNode breakTarget, final LabelNode continueTarget, final LabelNode returnTarget,
            final LabelNode throwTarget) {
        return new CodeBlock() {
            {
                // IN: completion
                lookupswitch( normalTarget,
                        new int[] { Type.NORMAL.ordinal(), Type.BREAK.ordinal(), Type.CONTINUE.ordinal(), Type.RETURN.ordinal(), Type.THROW.ordinal() },
                        new LabelNode[] { normalTarget, breakTarget, continueTarget, returnTarget, throwTarget } );

            }
        };
    }

    public static CodeBlock convertToNormal() {
        return new CodeBlock() {
            {
                // IN: completion
                dup();
                // completion completion
                getstatic( p( Completion.Type.class ), "NORMAL", sig( Type.class ) );
                // completion completion NORMAL
                putfield( p( Completion.class ), "type", sig( Type.class ) );
                // completion
            }
        };
    }

}
