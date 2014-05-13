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

import java.lang.invoke.CallSite;
import java.util.Collections;
import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.linker.DynJSBootstrapper;

public abstract class AbstractExpression implements Expression {

    public abstract Position getPosition();

    public String dump(String indent) {
        String data = dumpData();

        return indent + getClass().getSimpleName() + ":" + getPosition().getLine() + " " +
                (data != null ? (" (" + data + ")") : "") + "\n";
    }

    public String dumpData() {
        return null;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        return Collections.emptyList();
    }

    protected Object getValue(CallSite callSite, ExecutionContext context, Object obj) {
        if (obj instanceof Reference) {
            Reference ref = (Reference) obj;
            String name = ref.getReferencedName();
            try {
                Object result = callSite.getTarget().invoke( obj, context, name );
                return result;
            } catch (ThrowException e) {
                throw e;
            } catch (NoSuchMethodError e) {
                if (ref.isPropertyReference() && !ref.isUnresolvableReference()) {
                    return Types.UNDEFINED;
                }
                throw new ThrowException(context, context.createReferenceError("unable to reference: " + name));
            } catch (Throwable e) {
                throw new ThrowException(context, e);
            }
        } else {
            return obj;
        }
    }


}
