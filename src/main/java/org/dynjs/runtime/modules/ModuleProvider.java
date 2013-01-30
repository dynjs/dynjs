package org.dynjs.runtime.modules;

import org.dynjs.runtime.ExecutionContext;

/**
 * Provider for loading Javascript modules.
 * 
 * @author Bob McWhirter
 */
public interface ModuleProvider {

    /**
     * Load a module.
     * 
     * <p>
     * Given a context and a module name, load the module or return
     * <code>null</code> if un-handled.
     * </p>
     * 
     * @param context The context of the request.
     * @param moduleName The name of the module to load.
     * @return The loaded module (through its exports), otherwise
     *         <code>null</code> if un-loadable.
     */
    Object load(ExecutionContext context, String moduleName);

}
