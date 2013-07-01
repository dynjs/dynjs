package org.dynjs.runtime.modules;

import java.util.HashMap;

import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.Require;

/**
 * Provider for loading Javascript modules.
 * 
 * @author Bob McWhirter
 */
public abstract class ModuleProvider {

    /**
     * Load a module.
     * 
     * <p>
     * Given a context and a module name, load the module or return
     * <code>false</code> if not handled.
     * </p>
     * 
     * @param runtime The active runtime.
     * @param context The context of the request.
     * @param moduleID The id of the module to load.
     * @return <code>true</code> if the module was loaded, false if not
     */
    abstract boolean load(DynJS runtime, ExecutionContext context, String moduleID);

    /**
     * Generate a unique module ID for <code>moduleName</code>
     * 
     * @param context the current execution context
     * @param moduleName the name of the module
     * @return the module ID
     */
    public abstract String generateModuleID(ExecutionContext context, String moduleName);

    /**
     * A template method used by require() which is responsible for ensuring the
     * module loading contract is enforced by subclasses.
     * 
     * @see http://wiki.commonjs.org/wiki/Modules/1.1
     * @param require
     * @param context The execution context of the request
     * @param moduleId The name of the module to load   @return The loaded module or <code>null</code if un-loadable.
     */
    public Object findAndLoad(ExecutionContext context, String moduleId) {
        DynJS runtime = context.getGlobalObject().getRuntime();
        ExecutionContext requireContext = ExecutionContext.createGlobalExecutionContext(runtime);
        GlobalObject requireGlobal = requireContext.getGlobalObject();
//        requireGlobal = context.getGlobalObject();

        // if the module ID is null, we can't find the module, so bail
        if (moduleId == null) {
            return null;
        }
        
        // check the cache & return if found
        if (CACHE.containsKey(moduleId)) {
            return CACHE.get(moduleId);
        }
        
        // add ID + empty module.exports object to cache
        JSObject module = new DynObject(context.getGlobalObject());
        Object exports = new DynObject(context.getGlobalObject());
        module.put(requireContext, "exports", exports, true);
        module.put(requireContext, "id", moduleId, false);
        requireGlobal.put(requireContext, "module", module, true);
        requireGlobal.put(requireContext, "exports", exports, true);
        CACHE.put(moduleId, exports);
        
        // try to load the module
        // if successful, add to the cache
        if (this.load(runtime, requireContext, moduleId)) {
            exports = module.get(requireContext, "exports");
            CACHE.put(moduleId, exports);
            return exports;
        }
        return null;
    }
    
    public static void clearCache() {
        CACHE.clear();
    }
    
    /**
     * Helper method. Since module names should not include the .js extension, but the actual
     * modules themselves usually do.
     * @param originalName
     * @return
     */
    String normalizeName(String originalName) {
        if (originalName == null || originalName.endsWith(".js")) {
            return originalName;
        }
        return originalName + ".js";
    }
    
    private static final HashMap<String, Object> CACHE = new HashMap<String, Object>();
}
