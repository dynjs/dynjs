package org.dynjs.runtime.modules;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.InvalidModuleException;
import org.dynjs.exception.ModuleLoadException;
import org.dynjs.runtime.DynObject;

public class JavaClassModuleProvider implements ModuleProvider {

    public void addModule(Object module) throws InvalidModuleException {
        Module moduleAnno = module.getClass().getAnnotation( Module.class );

        if (moduleAnno == null) {
            throw new InvalidModuleException( module, "No @Module annotation" );
        }

        String moduleName = moduleAnno.name();

        if (moduleName == null) {
            throw new InvalidModuleException( module, "Name not specified in @Module" );
        }

        modules.put( moduleName, module );
    }

    @Override
    public DynObject load(DynThreadContext context, String moduleName) {
        Object javaModule = modules.get( moduleName );

        if (javaModule == null) {
            return null;
        }

        try {
            return buildExports( context, javaModule );
        } catch (IllegalAccessException e) {
            throw new ModuleLoadException( moduleName, e );
        }
    }

    private DynObject buildExports(DynThreadContext context, Object javaModule) throws IllegalAccessException {
        Method[] methods = javaModule.getClass().getMethods();

        DynObject exports = new DynObject();

        for (Method method : methods) {
            Export exportAnno = method.getAnnotation( Export.class );

            if (exportAnno == null) {
                continue;
            }

            String exportName = exportAnno.name();

            if ("".equals( exportName )) {
                exportName = method.getName();
            }

            DynObject function = buildFunction( context, javaModule, method );
            exports.setProperty( exportName, function );
        }
        return exports;
    }

    private DynObject buildFunction(DynThreadContext context, Object module, Method method) throws IllegalAccessException {
        return DynJSCompiler.wrapFunction( context, new JavaFunction( module, method ) );
    }

    private Map<String, Object> modules = new HashMap<String, Object>();

}
