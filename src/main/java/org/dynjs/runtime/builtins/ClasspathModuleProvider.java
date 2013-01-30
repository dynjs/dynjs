package org.dynjs.runtime.builtins;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.modules.ModuleProvider;

public class ClasspathModuleProvider implements ModuleProvider {

    private DynObject module;
    private DynObject exports;

    @Override
    public Object load(ExecutionContext context, String moduleName) {
        if (moduleName == null) {
            return null;
        }
        DynJS runtime = context.getGlobalObject().getRuntime();
        ExecutionContext requireContext = ExecutionContext.createGlobalExecutionContext(runtime);
        GlobalObject requireGlobal = requireContext.getGlobalObject();
        this.module = new DynObject(requireGlobal);
        this.exports = new DynObject(requireGlobal);
        module.put(null, "exports", exports, true);
        requireGlobal.put(requireContext, "module", module, true);
        requireGlobal.put(requireContext, "exports", exports, true);
        
        String originalName = moduleName;
        moduleName = normalizeName(moduleName);
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            InputStream is = classLoader.getResourceAsStream(moduleName);
            if (is == null) {
                is = classLoader.getResourceAsStream(originalName + "/index.js");
                if (is == null) {
                    throw new FileNotFoundException("Cannot find module: " + moduleName);
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Object ret = requireGlobal.getRuntime().newRunner().withContext(requireContext).withSource(reader).execute();
            try {
                is.close();
            } catch (IOException ignore) {
            }
            return ret;
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
