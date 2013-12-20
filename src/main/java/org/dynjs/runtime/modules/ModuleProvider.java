package org.dynjs.runtime.modules;

import org.dynjs.runtime.*;

/**
 * Provider for loading Javascript modules.
 * 
 * @author Bob McWhirter
 * @author Lance Ball
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
    protected abstract boolean load(DynJS runtime, ExecutionContext context, String moduleID);

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
     * @see <a href="http://wiki.commonjs.org/wiki/Modules/1.1">CommonJS Spec</a>
     * @param context The execution context of the request
     * @param moduleId The name of the module to load   @return The loaded module or <code>null</code> if un-loadable.
     * @return the module's exports
     */
    public Object findAndLoad(ExecutionContext context, String moduleId, JSObject module, JSObject exports) {
        // setup our module/exports/id in the execution context
        LexicalEnvironment localEnv = context.getVariableEnvironment();
        localEnv.getRecord().createMutableBinding(context, "module", false);
        localEnv.getRecord().createMutableBinding(context, "exports", false);
        localEnv.getRecord().createMutableBinding(context, "id", false);
        
        localEnv.getRecord().setMutableBinding(context, "module", module, false);
        localEnv.getRecord().setMutableBinding(context, "exports", exports, false);
        localEnv.getRecord().setMutableBinding(context, "id", moduleId, false);
        return (this.load(context.getGlobalObject().getRuntime(), context, moduleId)) ?  module.get(context, "exports") : null;
    }

    /**
     * Helper method. Since module names should not include the .js extension, but the actual
     * modules themselves usually do.
     * @param originalName the module name
     * @return the normalized name
     */
    protected String normalizeName(String originalName) {
        if (originalName == null || originalName.endsWith(".js")) {
            return originalName;
        }
        return originalName + ".js";
    }
}
