package org.dynjs.runtime.modules;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;

/**
 * Implementation of <code>ModuleProvider</code> which loads from the
 * filesystem based upon the context's load-paths.
 * 
 * @author Lance Ball
 * @author Bob McWhirter
 */
public class FilesystemModuleProvider implements ModuleProvider {

    public FilesystemModuleProvider(GlobalObject globalObject) {
        globalObject.addLoadPath(System.getProperty("user.dir") + "/");
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
    public DynObject load(ExecutionContext context, String moduleName) {
        String filename = normalizeFileName(moduleName);
        File file = findFile(context, filename);
        if (file == null) {
            file = findFile(context, moduleName + "/index.js");
        }
        if (file != null) {
            DynJS runtime = context.getGlobalObject().getRuntime();
            ExecutionContext requireContext = ExecutionContext.createGlobalExecutionContext(runtime);

            GlobalObject requireGlobal = requireContext.getGlobalObject();

            DynObject module = new DynObject(context.getGlobalObject());
            DynObject exports = new DynObject(context.getGlobalObject());

            requireGlobal.addLoadPath(file.getParent());

            module.put(requireContext, "exports", exports, true);
            requireGlobal.put(requireContext, "module", module, true);
            requireGlobal.put(requireContext, "exports", exports, true);

            try {
                runtime.execute(requireContext, file);
            } catch (IOException e) {
                return null;
            }

            module  = (DynObject) requireGlobal.get(requireContext, "module");
            exports = (DynObject) requireGlobal.get(requireContext, "exports");
            

            return (DynObject) module.get(requireContext, "exports");
        }
        return null;
    }

    private File findFile(ExecutionContext context, String fileName) {
        List<String> loadPaths = context.getGlobalObject().getLoadPaths();
        for (String loadPath : loadPaths) {
//            System.err.println("Looking for " + fileName + " in " + loadPath);
            File file = new File(loadPath, fileName);
            if (file.exists()) {
                return file;
            }
        }

        return null;
    }

    private String normalizeFileName(String originalName) {
        if (originalName.endsWith(".js")) {
            return originalName;
        }
        return originalName + ".js";
    }
    
    private String getCustomRequirePath() {
        if (System.getenv("DYNJS_REQUIRE_PATH") != null) {
            return System.getenv("DYNJS_REQUIRE_PATH");
        }
        return System.getProperty("dynjs.require.path");
    }

}
