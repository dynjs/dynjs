package org.dynjs.runtime.builtins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.exception.ReferenceError;
import org.dynjs.runtime.DynThreadContext;

public class Require implements Function {

	@Override
	public Scope getEnclosingScope() {
		return null;
	}

	@Override
	public Object resolve(String name) {
		return null;
	}

	@Override
	public void define(String property, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object call(DynThreadContext context, Object[] arguments) {
		// TODO: This should be a real thingy
		Object exports = null;
		if (arguments.length > 0) {
			String filename = normalizeFileName( (String) arguments[0] );
			File file = findFile(context, filename);
			if (file != null) {
				try {
					DynThreadContext evalContext = new DynThreadContext();
					evalContext.getScope().define("exports", null);
					evalContext.setLoadPaths(context.getLoadPaths());
					context.getRuntime().eval(evalContext, new FileInputStream(file));
					try {
						exports = evalContext.getScope().resolve("exports");
					} catch (ReferenceError error) {
						System.err.println(error.getLocalizedMessage());
					}
				} catch (FileNotFoundException e) {
					System.err.println("Module not found: " + filename);
				}
			} else {
				System.err.println("Module not found: " + filename);
			}
		}
		return exports;
	}

	public void setContext(DynThreadContext context) {

	}
	
	private File findFile( DynThreadContext context, String fileName ) {
		File file = null;
		Iterator<String> iterator = context.getLoadPaths().iterator(); 
		while (iterator.hasNext()) {
			file = new File(iterator.next() + fileName);
			if (file.exists()) { break; }
		}
		return file;
	}
	
	private String normalizeFileName(String originalName) {
		if (originalName.endsWith(".js")) { return originalName; }  
		StringBuilder filename = new StringBuilder(originalName);
		filename.append(".js");
		return filename.toString();
	}
}

