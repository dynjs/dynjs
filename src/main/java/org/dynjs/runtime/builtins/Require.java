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

import java.util.*;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.*;
import org.dynjs.runtime.modules.*;

/**
 * Built-in implementation of <code>require(moduleName)</code>.
 * 
 * @author Lance Ball
 * @author Bob McWhirter
 * http://wiki.commonjs.org/wiki/Modules/1.1
 * @see ModuleProvider
 */
public class Require extends AbstractNativeFunction {

    private List<ModuleProvider> moduleProviders = new ArrayList<>();
    private LinkedList<String> loadPaths = new LinkedList<>();

    public Require(GlobalContext globalContext) {
        super(globalContext, "name");
        DynObject module = new DynObject(globalContext);
        module.put("id", "dynjs");
        module.put("exports", new DynObject(globalContext));
        globalContext.getObject().put( null, "module", module, false);
        globalContext.getObject().put( null, "exports", module.get("exports"), false );

        // ----------------------------------------
        // Module-provider setup
        // ----------------------------------------
        JavaClassModuleProvider javaClassModuleProvider = new JavaClassModuleProvider();
        javaClassModuleProvider.addModule(new ConsoleModule());
        javaClassModuleProvider.addModule(new UtilModule());

        this.moduleProviders.add(javaClassModuleProvider);
        this.moduleProviders.add(new ClasspathModuleProvider());
        this.moduleProviders.add(new FilesystemModuleProvider(this));

        String customRequirePath = this.getCustomRequirePath();
        if (customRequirePath != null) {
            String[] paths = customRequirePath.split(":");
            Collections.addAll(this.loadPaths, paths);
        }

        this.loadPaths.add(System.getProperty("user.dir") + "/");

        this.put("paths", loadPaths);
        this.put("addLoadPath", new AbstractNativeFunction(globalContext) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                if (args[0] == null || args[0] == Types.UNDEFINED || args[0] == Types.NULL) {
                    return loadPaths;
                }
                addLoadPath((String) args[0]);
                return loadPaths;
            }
        });
        this.put("removeLoadPath", new AbstractNativeFunction(globalContext) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                if (args[0] == null || args[0] == Types.UNDEFINED || args[0] == Types.NULL) {
                    return Types.NULL;
                }
                removeLoadPath((String) args[0]);
                return loadPaths;
            }
        });
        this.put("pushLoadPath", new AbstractNativeFunction(globalContext) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                if (args[0] == null || args[0] == Types.UNDEFINED || args[0] == Types.NULL) {
                    return Types.NULL;
                }
                pushLoadPath((String) args[0]);
                return loadPaths;
            }
        });
    }

    public List<ModuleProvider> getModuleProviders() {
        return this.moduleProviders;
    }

    public void addModuleProvider(ModuleProvider provider) {
        this.moduleProviders.add(provider);
    }

    public void removeLoadPath(String path) {
        loadPaths.remove(path);
        this.put("paths", loadPaths);
    }

    public void addLoadPath(String path) {
        this.loadPaths.add(path);
        this.put("paths", loadPaths);
    }

    public void pushLoadPath(String path) {
        this.loadPaths.push(path);
        this.put("paths", loadPaths);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... arguments) {
        if (arguments[0] == Types.UNDEFINED) {
            throw new ThrowException(context, context.createError("Error", "no module identifier provided"));
        }

        String moduleName = (String) arguments[0];
        List<ModuleProvider> moduleProviders = this.getModuleProviders();
        // Load module providers in reverse order
        for (int i = moduleProviders.size(); i > 0; i--) {
            ModuleProvider provider = moduleProviders.get(i-1);

            // if a module provider can generate a module ID, then it can load the module.
            String moduleId = provider.generateModuleID(context, moduleName);
            if (moduleId != null) {
                // first check to see if we have the module cached, if so return it
                if (cache.containsKey(moduleId)) {
                    return cache.get(moduleId);
                }
                // add ID + empty module.exports object to cache
                GlobalContext globalContext = context.getGlobalContext();
                JSObject module = new DynObject(globalContext);
                JSObject exports = new DynObject(globalContext);
                module.put(context, "exports", exports, true);
                module.put(context, "id", moduleId, false);
                cache.put(moduleId, exports);

                // setup our module/exports/id in the execution context
                ModuleProvider.setLocalVar(context, "module", module);
                ModuleProvider.setLocalVar(context, "exports", exports);
                ModuleProvider.setLocalVar(context, "id", moduleId);

                if (provider.load(context, moduleId)) {
                    final Object exported = module.get(context, "exports");
                    cache.put(moduleId, exported);
                    return exported;
                };
                return null;
            }
        }
        throw new ThrowException(context, context.createError("Error", "cannot load module " + moduleName));
    }

    private String getCustomRequirePath() {
        if (System.getenv("DYNJS_REQUIRE_PATH") != null) {
            return System.getenv("DYNJS_REQUIRE_PATH");
        }
        return System.getProperty("dynjs.require.path");
    }

    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/Require.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: require>";
    }

    public List<String> getLoadPaths() {
        return loadPaths;
    }

    private final HashMap<String, Object> cache = new HashMap<>();
}
