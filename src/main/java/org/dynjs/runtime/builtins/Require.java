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

import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.modules.ModuleProvider;

/**
 * Built-in implementation of <code>require(moduleName)</code>.
 * 
 * @author Lance Ball
 * @author Bob McWhirter
 * http://wiki.commonjs.org/wiki/Modules/1.1
 * @see ModuleProvider
 */
public class Require extends AbstractNativeFunction {

    public Require(GlobalObject globalObject) {
        super(globalObject, "name");
        DynObject module = new DynObject(globalObject);
        module.put("id", "dynjs");
        globalObject.put("module", module);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        if (arguments[0] == Types.UNDEFINED) {
            throw new ThrowException(context, context.createError("Error", "no module identifier provided"));
        }

        String moduleName = (String) arguments[0];
        List<ModuleProvider> moduleProviders = context.getGlobalObject().getModuleProviders();
        for (ModuleProvider provider : moduleProviders) {
            // if a module provider can generate a module ID, then it can
            // load the module.
            // TODO: Maybe change the method name to be more accurate/descriptive.
            String moduleId = provider.generateModuleID(context, moduleName);
            if (moduleId != null) {
                return context.call(provider, context.getGlobalObject(), moduleId);
            }
        }
        throw new ThrowException(context, context.createError("Error", "cannot load module " + moduleName));
    }
}
