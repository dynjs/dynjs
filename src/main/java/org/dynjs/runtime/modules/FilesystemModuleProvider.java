package org.dynjs.runtime.modules;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Runner;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Implementation of <code>ModuleProvider</code> which loads from the
 * filesystem based upon the context's load-paths.
 * 
 * @author Lance Ball
 * @author Bob McWhirter
 */
public class FilesystemModuleProvider extends ModuleProvider {

    public FilesystemModuleProvider(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    boolean load(DynJS runtime, ExecutionContext context, String moduleID) {
        File file = new File(moduleID);
        if (file.exists()) {
            runtime.evaluate("require.addLoadPath('" + file.getParent() + "')");
            try {
                runtime.newRunner().withContext(context).withSource(file).execute();
                runtime.evaluate("require.removeLoadPath('" + file.getParent() + "')");
                return true;
            } catch (IOException e) {
                System.err.println("There was an error loading the module " + moduleID + ". Error message: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public String generateModuleID(ExecutionContext context, String moduleName) {
        Runner runtime = context.getGlobalObject().getRuntime().newRunner();
        List<String> requirePaths = (List<String>) runtime.withContext(context).withSource("require.paths").evaluate();
        File moduleFile = findFile(requirePaths, moduleName);
        if (moduleFile != null && moduleFile.exists()) {
            return moduleFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * Finds the module file based on the known load paths.
     * 
     * @param loadPaths the list of load paths to search
     * @param moduleName the name of the module to find
     * @return the File if found, else null
     */
    private File findFile(List<String> loadPaths, String moduleName) {
        String fileName = normalizeName(moduleName);
        File file = null;
        for (String loadPath : loadPaths) {
            // require('foo');
            file = new File(loadPath, fileName);
            // foo.js is in the require path
            if (file.exists()) break;
            else {
                // foo/index.js is in the require path
                file = new File(loadPath, moduleName + "/index.js");
                if (file.exists()) break;
                else {
                    // foo/lib/foo.js
                    file = new File(loadPath, moduleName + "/lib/" + fileName);
                    if (file.exists()) break;
                }
            }
        }
        return file;
    }

}
