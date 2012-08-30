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

            DynObject module = new DynObject( context.getGlobalObject() );
            DynObject exports = new DynObject( context.getGlobalObject() );
            
            requireGlobal.addLoadPath( file.getParent() );

            module.put(requireContext, "exports", exports, true);
            requireGlobal.put(requireContext, "module", module, true);
            requireGlobal.put(requireContext, "exports", exports, true);

            try {
                runtime.execute(requireContext, file);
            } catch (IOException e) {
                return null;
            }
            
            exports = (DynObject) requireGlobal.get( requireContext, "exports" );

            return exports;
        }
        return null;
    }

    private File findFile(ExecutionContext context, String fileName) {
        List<String> loadPaths = context.getGlobalObject().getLoadPaths();
        for ( String loadPath : loadPaths ) {
            File file = new File(loadPath, fileName );
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

}
