package org.dynjs.runtime.builtins;

import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.runtime.BaseFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.modules.ModuleProvider;

public class TestModuleProvider implements ModuleProvider {

	@Override
	public DynObject load(DynThreadContext context, String moduleName) {
		if ( moduleName.equals( "java_impl" ) ) {
			DynObject exports = new DynObject();
			exports.setProperty("cheese", DynJSCompiler.wrapFunction(context.getBuiltin( "function"), new Cheddar() ) );
			return exports;
		}
		return null;
	}
	
	public static class Cheddar extends BaseFunction {

		@Override
		public Object call(Object self, DynThreadContext context, Object... arguments) {
			return "cheddar";
		}

		@Override
		public String[] getParameters() {
			return new String[0];
		}
		
	}
	

}
