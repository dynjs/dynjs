package org.dynjs.runtime.java;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class JavaPackage extends DynObject {

    private String path;

    public JavaPackage(GlobalContext globalContext, String path) {
        super(globalContext);
        this.path  = path;
    }

    @Override
    public Object get(ExecutionContext context, String name) {
        Object result = super.get(context, name);
        if (result == Types.UNDEFINED) {

            ClassLoader cl = context.getClassLoader();
            try {
                Class<?> cls = cl.loadClass(fullPath(name));
                return cls;
            } catch (ClassNotFoundException e) {
                result = new JavaPackage(context.getGlobalContext(), fullPath(name));
            }
        }
        return result;
    }
    
    public String toString() {
        return "[JavaPackage: " + this.path + "]";
    }
    
    private String fullPath(String name) {
        return (this.path == null ? name : this.path + "." + name);
    }

}
