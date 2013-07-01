package org.dynjs.runtime.modules;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

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
        globalObject.addLoadPath(System.getProperty("user.dir") + "/");
        globalObject.addLoadPath(System.getProperty("user.dir") + "/node_modules");
        globalObject.addLoadPath(System.getProperty("user.home") + "/.node_modules/");
        globalObject.addLoadPath(System.getProperty("user.home") + "/.node_libraries/");
        globalObject.addLoadPath("/usr/local/lib/node/");
        globalObject.addLoadPath("/usr/local/lib/node_modules/");
        String customRequirePath = this.getCustomRequirePath();
        if (customRequirePath != null) {
            String[] paths = customRequirePath.split(":");
            for(String path : paths) {
                globalObject.addLoadPath(path);
            }
        }
    }

    @Override
    boolean load(DynJS runtime, ExecutionContext context, String moduleID) {
        GlobalObject global = context.getGlobalObject();
        File file = new File(moduleID);
        if (file.exists()) {
            global.addLoadPath(file.getParent());
            try {
                runtime.newRunner().withContext(context).withSource(file).execute();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String generateModuleID(ExecutionContext context, String moduleName) {
        File moduleFile = findFile(context.getGlobalObject().getLoadPaths(), moduleName);
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
            file = new File(loadPath, fileName);
            if (file.exists()) break;
            else {
                file = new File(loadPath, moduleName + "/index.js");
                if (file.exists()) break;
            }
        }
        return file;
    }

    private String getCustomRequirePath() {
        if (System.getenv("DYNJS_REQUIRE_PATH") != null) {
            return System.getenv("DYNJS_REQUIRE_PATH");
        }
        return System.getProperty("dynjs.require.path");
    }
}
