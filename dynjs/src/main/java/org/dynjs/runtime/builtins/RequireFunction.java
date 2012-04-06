package org.dynjs.runtime.builtins;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.DynThreadContext;

public class RequireFunction implements Function {

	@Override
	public Scope getEnclosingScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object resolve(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void define(String property, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object call(DynThreadContext context, Object[] arguments) {
		if (arguments.length > 0) {
			String filename = normalizeFileName( (String) arguments[0] );
			try {
				context.getRuntime().eval(context, new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setContext(DynThreadContext context) {
		// TODO Auto-generated method stub

	}
	
	private String normalizeFileName(String originalName) {
		StringBuilder filename = new StringBuilder( System.getProperty("user.dir") );
		// filename.append( System.getProperty("path.separator") );
		filename.append("/");
		filename.append( originalName );
		if (!originalName.endsWith(".js")) {
			filename.append(".js");
		}
		return filename.toString();
	}
}

