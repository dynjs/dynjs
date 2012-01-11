/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
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

import me.qmx.jitescript.CodeBlock;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;

import static me.qmx.jitescript.util.CodegenUtils.sig;

public class ResolveByIndexStatement implements Statement {

    private final Statement lhs;
    private final Statement index;

    public ResolveByIndexStatement(Statement lhs, String index) {
        this(lhs, new StringLiteralStatement(index));

    }
    public ResolveByIndexStatement(Statement lhs, Statement index) {
        this.lhs = lhs;
        this.index = index;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlock.newCodeBlock()
        .append(lhs.getCodeBlock())
        .append(index.getCodeBlock())
        .invokeinterface(DynJSCompiler.Types.Scope, "resolve", sig(Object.class, String.class));
    }
}
