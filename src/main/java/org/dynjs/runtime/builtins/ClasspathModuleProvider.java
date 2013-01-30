package org.dynjs.runtime.builtins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Runner;
import org.dynjs.runtime.modules.ModuleProvider;

public class ClasspathModuleProvider implements ModuleProvider {

    private final GlobalObject globalObject;
    private final DynObject module;
    private final DynObject exports;

    public ClasspathModuleProvider(GlobalObject globalObject) {
        this.globalObject = globalObject;
        this.module = new DynObject(globalObject);
        this.exports = new DynObject(globalObject);
        module.put(null, "exports", exports, true);
    }

    @Override
    public Object load(ExecutionContext context, String moduleName) {
        if (moduleName == null) {
            return null;
        }
        DynJS runtime = context.getGlobalObject().getRuntime();
        ExecutionContext requireContext = ExecutionContext.createGlobalExecutionContext(runtime);
        GlobalObject requireGlobal = requireContext.getGlobalObject();
        requireGlobal.put(requireContext, "module", module, true);
        requireGlobal.put(requireContext, "exports", exports, true);
        
        String originalName = moduleName;
        moduleName = normalizeName(moduleName);
        ClassLoader classLoader = this.getClass().getClassLoader();
        ExecutionContext parent = context.getParent();
        while (parent != null) {
            context = parent;
            parent = context.getParent();
        }
        try {
            InputStream is = classLoader.getResourceAsStream(moduleName);
            if (is == null) {
                is = classLoader.getResourceAsStream(originalName + "/index.js");
                if (is == null) {
                    throw new FileNotFoundException("Cannot find module: " + moduleName);
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Object ret = runtime.newRunner().withContext(requireContext).withSource(reader).execute();
            try {
                is.close();
            } catch (IOException ignore) {
            }
            return (JSObject) ret;
        }
        catch(FileNotFoundException e) {
            System.err.println("Module not found: " + moduleName);
        }
        return null;
    }
    
    private String normalizeName(String originalName) {
        if (originalName.endsWith(".js")) {
            return originalName;
        }
        return originalName + ".js";
    }
    
}
