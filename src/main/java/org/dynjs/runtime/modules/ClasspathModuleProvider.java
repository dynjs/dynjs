package org.dynjs.runtime.modules;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.source.ClassLoaderSourceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ClasspathModuleProvider extends ModuleProvider {

    @Override
    public boolean load(ExecutionContext context, String moduleId) {
        ClassLoader classLoader = context.getClassLoader();
        // System.err.println("Classloader: " + classLoader.toString());
        // System.err.println("Looking for module " + moduleId);
        try {
            InputStream is = classLoader.getResourceAsStream(moduleId);
            if (is == null) {
                return false;
            }
            context.getRuntime().newRunner()
                    .withFileName(moduleId)
                    .withContext(context)
                    .withSource(new ClassLoaderSourceProvider( classLoader, moduleId )).execute();
            try {
                is.close();
            } catch (IOException ignore) {
            }
            return true;
        }
        catch(Exception e) {
            System.err.println("There was an error loading the module " + moduleId + ". Error message: " + e.getMessage());
            e.printStackTrace();
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
