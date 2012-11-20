package org.dynjs.runtime.modules;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.InvalidModuleException;
import org.dynjs.exception.ModuleLoadException;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;

public class JavaClassModuleProvider implements ModuleProvider {

    public void addModule(Object module) throws InvalidModuleException {
        Module moduleAnno = module.getClass().getAnnotation(Module.class);

        if (moduleAnno == null) {
            throw new InvalidModuleException(module, "No @Module annotation");
        }

        String moduleName = moduleAnno.name();

        if (moduleName == null) {
            throw new InvalidModuleException(module, "Name not specified in @Module");
        }

        modules.put(moduleName, module);
    }

    @Override
    public JSObject load(ExecutionContext context, String moduleName) {
        Object javaModule = modules.get(moduleName);

        if (javaModule == null) {
            return null;
        }

        try {
            return buildExports(context, moduleName, javaModule);
        } catch (IllegalAccessException e) {
            throw new ModuleLoadException(moduleName, e);
        }
    }

    private JSObject buildExports(ExecutionContext context, String moduleName, Object javaModule) throws IllegalAccessException {
        Method[] methods = javaModule.getClass().getMethods();

        JSObject exports = new DynObject(context.getGlobalObject());

        for (Method method : methods) {
            Export exportAnno = method.getAnnotation(Export.class);

            if (exportAnno == null) {
                continue;
            }

            String exportName = exportAnno.name();

            if ("".equals(exportName)) {
                exportName = method.getName();
            }

            final JSFunction function = buildFunction(context.getGlobalObject(), javaModule, method);
            PropertyDescriptor desc = new PropertyDescriptor() {
                {
                    set("Value", function);
                }
            };
            function.setDebugContext(moduleName + "." + exportName);
            exports.defineOwnProperty(context, exportName, desc, false);
        }
        return exports;
    }

    private JSFunction buildFunction(GlobalObject globalObject, Object module, Method method) throws IllegalAccessException {
        return new JavaFunction(globalObject, module, method);
    }

    private Map<String, Object> modules = new HashMap<String, Object>();

}
