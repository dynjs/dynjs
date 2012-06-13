package org.dynjs.runtime.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.dynjs.exception.ModuleLoadException;
import org.dynjs.exception.ReferenceError;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;

/** Implementation of <code>ModuleProvider</code> which loads from the
 * filesystem based upon the context's load-paths.
 * 
 * @author Lance Ball
 * @author Bob McWhirter
 */
public class FilesystemModuleProvider implements ModuleProvider {

	@Override
	public DynObject load(DynThreadContext context, String moduleName) {
		DynObject exports = null;
		String filename = normalizeFileName(moduleName);
		File file = findFile(context, filename);
		if (file == null) {
			file = findFile(context, moduleName + "/index.js");
		}
		if (file != null) {
			try {
				DynThreadContext evalContext = new DynThreadContext( context );
				context.getRuntime().eval(evalContext, "var exports = {};");
				context.getRuntime().eval(evalContext,
						new FileInputStream(file), filename);
				try {
					exports = (DynObject) evalContext.getScope().resolve("exports");
				} catch (ReferenceError error) {
					System.err.println(error.getLocalizedMessage());
				}
			} catch (FileNotFoundException e) {
				throw new ModuleLoadException( moduleName, e );
			}
		}
		return exports;
	}

	private File findFile(DynThreadContext context, String fileName) {
		File file = null;
		Iterator<String> iterator = context.getLoadPaths().iterator();
		while (iterator.hasNext()) {
			String path = iterator.next();
			file = new File(path + fileName);
			if (file.exists()) {
				break;
			} else {
				file = null;
			}
		}
		return file;
	}

	private String normalizeFileName(String originalName) {
		if (originalName.endsWith(".js")) {
			return originalName;
		}
		StringBuilder filename = new StringBuilder(originalName);
		filename.append(".js");
		return filename.toString();
	}

}
