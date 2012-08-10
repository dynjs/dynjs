/**
 *  Copyright 2012 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.concurrent.atomic.AtomicInteger;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Position;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.Completion.Type;
import org.objectweb.asm.tree.LabelNode;

public abstract class AbstractStatement extends AbstractByteCodeEmitter implements Statement {

    private final static AtomicInteger counter = new AtomicInteger();
    private final Position position;
    private int number;

    AbstractStatement(final Tree tree) {
        this.position = new Position( tree );
        this.number = counter.incrementAndGet();
    }

    public Position getPosition() {
        return position;
    }

    public int getStatementNumber() {
        return this.number;
    }

    public CodeBlock normalCompletion() {
        return new CodeBlock() {
            {
                invokestatic( p( Completion.class ), "createNormal", sig( Completion.class ) );
            }
        };
    }

    public CodeBlock normalCompletionWithValue() {
        return new CodeBlock() {
            {
                invokestatic( p( Completion.class ), "createNormal", sig( Completion.class, Object.class ) );
            }
        };
    }

    public CodeBlock returnCompletion() {
        return new CodeBlock() {
            {
                // IN value
                invokestatic( p( Completion.class ), "createReturn", sig( Completion.class, Object.class ) );
                // completion
            }
        };
    }

    public CodeBlock continueCompletion(final String target) {
        return new CodeBlock() {
            {
                // <EMPTY>
                if (target == null) {
                    aconst_null();
                } else {
                    ldc( target );
                }
                // target
                invokestatic( p( Completion.class ), "createContinue", sig( Completion.class, String.class ) );
                // completion
            }
        };
    }
    
    public CodeBlock breakCompletion(final String target) {
        return new CodeBlock() {
            {
                // <EMPTY>
                if (target == null) {
                    aconst_null();
                } else {
                    ldc( target );
                }
                // target
                invokestatic( p( Completion.class ), "createBreak", sig( Completion.class, String.class ) );
                // completion
            }
        };
    }
    
    public CodeBlock throwCompletion() {
        return new CodeBlock() {
            {
                // IN value
                invokestatic( p( Completion.class ), "createThrow", sig( Completion.class, Object.class ) );
                // completion
            }
        };
    }

    public CodeBlock handleCompletion(
            final LabelNode normalTarget,
            final LabelNode breakTarget,
            final LabelNode continueTarget,
            final LabelNode returnTarget,
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

    public CodeBlock jsCompletionValue() {
        return new CodeBlock() {
            {
                // IN completion
                getfield( p( Completion.class ), "value", sig( Object.class ) );
                // value
            }
        };
    }

}
