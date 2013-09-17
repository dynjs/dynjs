package org.dynjs.runtime.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Runner;

public class ClasspathModuleProvider extends ModuleProvider {

    public ClasspathModuleProvider(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public boolean load(DynJS runtime, ExecutionContext context, String moduleId) {
        ClassLoader classLoader = context.getClassLoader();
        // System.err.println("Classloader: " + classLoader.toString());
        // System.err.println("Looking for module " + moduleId);
        try {
            InputStream is = classLoader.getResourceAsStream(moduleId);
            if (is == null) {
                return false;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            context.getGlobalObject().getRuntime().newRunner()
                    .withFileName(moduleId).withContext(context).withSource(reader).execute();
            try {
                is.close();
            } catch (IOException ignore) {
            }
            return true;
        }
        catch(Exception e) {
            System.err.println("There was an error loading the module " + moduleId + ". Error message: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String generateModuleID(ExecutionContext context, String moduleName) {
        ClassLoader classLoader = context.getClassLoader();
        String name = normalizeName(moduleName);
        URL moduleURL = classLoader.getResource(name);
        if (moduleURL != null) {
            return name;
        } else {
            moduleURL = classLoader.getResource(moduleName + "/index.js");
            if (moduleURL != null) {
                return moduleName + "/index.js";
            }
        }
        // couldn't find the module in our classpath
        return null;
    }

}
