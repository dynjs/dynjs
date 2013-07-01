package org.dynjs.runtime.modules;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.InvalidModuleException;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;

public class JavaClassModuleProvider extends ModuleProvider {

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
    public boolean load(DynJS runtime, ExecutionContext context, String moduleName) {
        Object javaModule = modules.get(moduleName);

        if (javaModule == null) {
            return false;
        }

        try {
            buildExports(context, moduleName, javaModule);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    private JSObject buildExports(ExecutionContext context, String moduleName, Object javaModule) throws IllegalAccessException {
        Method[] methods = javaModule.getClass().getMethods();

        JSObject module  = (JSObject) context.getGlobalObject().get("module");
        JSObject exports = (JSObject) module.get(null, "exports");

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

    @Override
    public String generateModuleID(ExecutionContext context, String moduleName) {
        if (modules.get(moduleName) != null) return moduleName;
        return null;
    }

}
