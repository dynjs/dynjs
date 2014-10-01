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
package org.dynjs.runtime.builtins;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.js.LexerException;
import org.dynjs.parser.js.ParserException;
import org.dynjs.runtime.*;

public class Eval extends AbstractNonConstructorFunction {

    public Eval(GlobalContext globalContext) {
        super(globalContext, "code");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        boolean direct = false;
        if (context.getFunctionReference() != null) {
            if (context.getFunctionReference() instanceof Reference) {
                Reference ref = (Reference) context.getFunctionReference();
                if (ref.getBase() instanceof EnvironmentRecord) {
                    direct = ref.getReferencedName().equals("eval");
                }
            }
        }
        Object code = args[0];
        if (code instanceof String) {
            try {
                Runner runner = context.getRuntime().newRunner();
                final ExecutionContext parent = context.getParent();
                parent.inEval(true);
                Object result = runner.withContext(parent)
                        .forceStrict(parent.isStrict() && direct)
                        .directEval(direct)
                        .withSource((String) code)
                        .evaluate();
                if (result == null) {
                    return Types.UNDEFINED;
                }
                return result;
            } catch (LexerException e) {
                throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
            } catch (ParserException e) {
                throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
            }
        } else {
            return code;
        }
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/Eval.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: eval>";
    }

}
