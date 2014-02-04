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
     * Given a context and a module ID, load the module or return
     * <code>false</code> if not handled.
     * </p>
     * 
     *
     * @param context The context of the request.
     * @param moduleID The id of the module to load.
     * @return <code>true</code> if the module was loaded, false if not
     */
    public abstract boolean load(ExecutionContext context, String moduleID);

    /**
     * Generate a unique module ID for <code>moduleName</code> if it can be resolved.
     * 
     * @param context the current execution context
     * @param moduleName the name of the module
     * @return the module ID or null if the module can't be found
     */
    public abstract String generateModuleID(ExecutionContext context, String moduleName);

    /**
     * A convenience for module providers. Sets a scoped variable on the provided
     * ExecutionContext.
     *
     * @param context The execution context to bind the variable to
     * @param name The name of the variable
     * @param value The value to set the variable to
     */
    public static void setLocalVar(ExecutionContext context, String name, Object value) {
        LexicalEnvironment localEnv = context.getLexicalEnvironment();
        localEnv.getRecord().createMutableBinding(context, name, false);
        localEnv.getRecord().setMutableBinding(context, name, value, false);
    }

    /**
     * A convenience for module providers. Gets a scoped variable from
     * the provided ExecutionContext
     * @param context The context to search for name
     * @param name The name of the variable
     * @return The value of the variable
     */
    public static Object getLocalVar(ExecutionContext context, String name) {
        LexicalEnvironment localEnv = context.getLexicalEnvironment();
        if (localEnv.getRecord().hasBinding(context, name)) {
            return localEnv.getRecord().getBindingValue(context, name, false);
        }
        return null;
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
