/*
 * Copyright 2012 JBoss Inc
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

import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.modules.ModuleProvider;

/**
 * Built-in implementation of <code>require(moduleName)</code>.
 * 
 * @author Lance Ball
 * @author Bob McWhirter
 * 
 * @see ModuleProvider
 */
public class Require extends AbstractNativeFunction {

    public Require(GlobalObject globalObject) {
        super(globalObject, "name");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        Object exports = null;

        if (arguments[0] == Types.UNDEFINED) {
            throw new ThrowException(context, context.createError("Error", "no module identifier provided"));
        }

        String moduleName = (String) arguments[0];

        List<ModuleProvider> moduleProviders = context.getGlobalObject().getModuleProviders();

        for (ModuleProvider provider : moduleProviders) {
            exports = provider.load(context, moduleName);
            if (exports != null) {
                break;
            }
        }

        if (exports == null) {
            throw new ThrowException(context, context.createError("Error", "cannot find module " + moduleName));
        }

        return exports;
    }
}