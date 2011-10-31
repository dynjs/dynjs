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
import org.dynjs.parser.Statement;
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
        return newCodeBlock()
                .append(leftHandStatement.getCodeBlock())
                .append(rightHandStatement.getCodeBlock())
                .invokedynamic(operation, sig(Object.class, Object.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
    }
}
