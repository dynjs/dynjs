/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dynjs.runtime.builtins;

import java.io.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.*;

public class Include extends AbstractNativeFunction {

    public Include(GlobalContext globalContext) {
        super(globalContext, "name");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String includePath = Types.toString(context, args[0]);
        File includeFile = new File(includePath);

        try {
            ExecutionContext parent = context.getParent();
            while (parent != null) {
                context = parent;
                parent = context.getParent();
            }
            Runner runner = context.getRuntime().newRunner();
            if (includeFile.exists()) {
                runner.withContext(context)
                        .withSource(includeFile)
                        .execute();
            } else {
                InputStream is = context.getClassLoader().getResourceAsStream(includePath);
                if (is == null) {
                    throw new FileNotFoundException(includePath);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                Object ret = runner.withContext(context).withSource(reader).execute();
                try {
                    is.close();
                } catch (IOException e) {
                    // ignore
                }
                return ret;
            }
        } catch (IOException e) {
            throw new ThrowException(context, context.createError("Error", e.getMessage()));
        }

        return Types.UNDEFINED;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/Include.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: include>";
    }
}
