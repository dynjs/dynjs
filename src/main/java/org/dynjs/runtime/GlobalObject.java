package org.dynjs.runtime;

import org.dynjs.Config;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.BlockManager.Entry;


public class GlobalObject extends DynObject {
    
    private JSCompiler compiler;
    private BlockManager blockManager;

    public GlobalObject(Config config) {
        this.compiler = new JSCompiler( config );
        this.blockManager = new BlockManager();
        
        defineGlobalProperty( "undefined", Types.UNDEFINED );
        defineGlobalProperty( "NaN", Double.NaN );
        defineGlobalProperty( "Infinity", Double.POSITIVE_INFINITY );
        defineGlobalProperty( "-Infinite", Double.NEGATIVE_INFINITY );
        
        /*
        put("-Infinity", Double.NEGATIVE_INFINITY);
        put("Object", new DynObject() {{
            setProperty("defineProperty", new DefineProperty());
        }});
        put("Number", new DynObject());
        put("Array", new DynObject());
        put("Date", new DynObject());
        put("String", new DynObject());
        put("Boolean", new DynObject());
        put("Error", new DynObject());
        put("Function", new DynObject() {{
            setProperty("prototype", get("Object"));
        }});
        put("eval", DynJSCompiler.wrapFunction(get("Function"), new Eval()));
        put("require", DynJSCompiler.wrapFunction(get("Function"), new Require()));
        put("parseFloat", DynJSCompiler.wrapFunction(get("Function"), new ParseFloat()));
        put("parseInt", DynJSCompiler.wrapFunction(get("Function"), new ParseInt()));
        put("isNaN", DynJSCompiler.wrapFunction( get("Function"), new IsNaN()));
        put("Math", new DynObject());
         */
    }
    
    public JSCompiler getCompiler() {
        return this.compiler;
    }
    
    public BlockManager getBlockManager() {
        return this.blockManager;
    }
    
    public Entry retrieveBlockEntry(int statementNumber) {
        return this.blockManager.retrieve( statementNumber );
    }
    
    protected void defineGlobalProperty(final String name, final Object value) {
        PropertyDescriptor desc = new PropertyDescriptor(){{
            set( "Value", value );
            set( "Writable", true );
            set( "Configurable", true );
            set( "Enumerable", true);
        }};
        defineOwnProperty( null, name, desc, false );
    }

}
