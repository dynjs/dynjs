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

import org.dynjs.api.Function;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.modules.ModuleProvider;

/**
 * Built-in implementation of <code>require(moduleName)</code>.
 * 
 * @author Lance Ball
 * @author Bob McWhirter
 * 
 * @see ModuleProvider
 */
public class Require implements Function {

    @Override
    public Object call(Object self, DynThreadContext context, Object... arguments) {
        DynObject exports = null;

        if (arguments.length != 1) {
            return null;
        }

        String moduleName = (String) arguments[0];

        List<ModuleProvider> moduleProviders = context.getModuleProviders();

        for (ModuleProvider provider : moduleProviders) {
            exports = provider.load( context, moduleName );
            if (exports != null) {
                break;
            }
        }

        if (exports == null) {
            System.err.println( "Cannot find module: " + moduleName );
        }

        return exports;
    }

    @Override
    public String[] getParameters() {
        return new String[] { "moduleName" };
    }

}
