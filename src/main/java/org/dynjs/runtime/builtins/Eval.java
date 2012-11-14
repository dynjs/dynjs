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
package org.dynjs.runtime.builtins;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.SyntaxError;
import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Reference;

public class Eval extends AbstractNonConstructorFunction {

    public Eval(GlobalObject globalObject) {
        super(globalObject, "code");
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
                Object result = context.getGlobalObject().getRuntime().evaluate(context.getParent(), code.toString(), context.getParent().isStrict() && direct, direct );
                return result;
            } catch (SyntaxError e) {
                throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
            }
        } else {
            return code;
        }
    }

}
