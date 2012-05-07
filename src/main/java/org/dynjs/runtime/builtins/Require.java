package org.dynjs.runtime.builtins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;
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
		Object exports = false;
		if (arguments.length > 0) {
			String filename = normalizeFileName( (String) arguments[0] );
			File file = findFile(context, filename);
			if (file != null) {
				try {
					context.getRuntime().eval(context, new FileInputStream(file));
				} catch (FileNotFoundException e) {
					// Should not get here
					System.err.println("Module not found: " + filename);
				}
			} else {
				System.err.println("Module not found: " + filename);
			}
			exports = true;
		}
		return exports;
	}

	public void setContext(DynThreadContext context) {

	}
	
	private File findFile( DynThreadContext context, String fileName ) {
		File file = null;
		Iterator<String> iterator = context.getLoadPaths().iterator(); 
		while (iterator.hasNext()) {
			file = new File(iterator.next());
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

