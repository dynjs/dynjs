/**
 *  Copyright 2013 Douglas Campos, and individual contributors
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

package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.Completion.Type;
import org.objectweb.asm.tree.LabelNode;

public abstract class AbstractStatement implements Statement {

    private final static AtomicInteger counter = new AtomicInteger();
    private int number;
    private List<String> labels = new ArrayList<String>();

    AbstractStatement() {
        this.number = counter.incrementAndGet();
    }

    public int getStatementNumber() {
        return this.number;
    }

    public void addLabel(String label) {
        this.labels.add(label);
    }

    public List<String> getLabels() {
        return this.labels;
    }
    
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return Collections.emptyList();
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        return Collections.emptyList();
    }
    
    public CodeBlock normalCompletion() {
        return new CodeBlock()
            .invokestatic(p(Completion.class), "createNormal", sig(Completion.class));
    }

    public CodeBlock normalCompletionWithValue() {
        return new CodeBlock()
            // IN: val
            .invokestatic(p(Completion.class), "createNormal", sig(Completion.class, Object.class));
    }

    public CodeBlock returnCompletion() {
        return new CodeBlock()
            // IN value
            .invokestatic(p(Completion.class), "createReturn", sig(Completion.class, Object.class));
            // completion
    }

    public CodeBlock continueCompletion(final String target) {
        CodeBlock codeBlock = new CodeBlock();
        // <EMPTY>
        if (target == null) {
            codeBlock.aconst_null();
        } else {
            codeBlock.ldc(target);
        }
        // target
        codeBlock.invokestatic(p(Completion.class), "createContinue", sig(Completion.class, String.class));
        // completion
        return codeBlock;
    }

    public CodeBlock breakCompletion(final String target) {
        CodeBlock codeBlock = new CodeBlock();
        // <EMPTY>
        if (target == null) {
            codeBlock.aconst_null();
        } else {
            codeBlock.ldc(target);
        }
        // target
        codeBlock.invokestatic(p(Completion.class), "createBreak", sig(Completion.class, String.class));
        // completion
        return codeBlock;
    }

    public CodeBlock throwCompletion() {
        return new CodeBlock()
            // IN value
            .invokestatic(p(Completion.class), "createThrow", sig(Completion.class, Object.class));
            // completion
    }

    public CodeBlock handleCompletion(
            final LabelNode normalTarget,
            final LabelNode breakTarget,
            final LabelNode continueTarget,
            final LabelNode returnTarget) {
        return new CodeBlock()
            // IN: completion
            .append(jsCompletionType())
            .lookupswitch(normalTarget,
                               new int[] { Type.NORMAL.ordinal(), Type.BREAK.ordinal(), Type.CONTINUE.ordinal(), Type.RETURN.ordinal() },
                               new LabelNode[] { normalTarget, breakTarget, continueTarget, returnTarget });

    }

    public CodeBlock convertToNormal() {
        return new CodeBlock()
            // IN: completion
            .dup()
            // completion completion
            .getstatic(p(Completion.Type.class), "NORMAL", ci(Type.class))
            // completion completion NORMAL
            .putfield(p(Completion.class), "type", ci(Type.class));
            // completion
    }

    public CodeBlock jsCompletionValue() {
        return new CodeBlock()
            // IN completion
            .getfield(p(Completion.class), "value", ci(Object.class));
            // value
    }
    
    public CodeBlock jsCompletionTarget() {
        return new CodeBlock()
            // IN completion
            .getfield(p(Completion.class), "target", ci(String.class));
            // value
    }

    public CodeBlock jsCompletionType() {
        return new CodeBlock()
            // IN completion
            .getfield(p(Completion.class), "type", ci(Completion.Type.class))
            // type
            .invokevirtual(p(Completion.Type.class), "ordinal", sig(int.class));
    }

    public String dump(String indent) {
        String data = dumpData();

        final String line = getPosition() == null ? "?" : getPosition().getLine() + "";
        return indent + getClass().getSimpleName() + ":" + line + " " +
                (data != null ? (" (" + data + ")") : "") + "\n";
    }

    public String dumpData() {
        return null;
    }
}
