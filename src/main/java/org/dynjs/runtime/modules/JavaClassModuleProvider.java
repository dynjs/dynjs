package org.dynjs.runtime.modules;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.dynjs.exception.InvalidModuleException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
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
    public boolean load(ExecutionContext context, String moduleName) {
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

		JSObject module = (JSObject) context.getVariableEnvironment().getRecord().getBindingValue(context, "module", true);
		JSObject exports = (JSObject) module.get(context, "exports");

        for (Method method : methods) {
            Export exportAnno = method.getAnnotation(Export.class);

            if (exportAnno == null) {
                continue;
            }

            String exportName = exportAnno.name();

            if ("".equals(exportName)) {
                exportName = method.getName();
            }

            final JSFunction function = buildFunction(context.getGlobalContext(), javaModule, method);
            PropertyDescriptor desc = new PropertyDescriptor();
            desc.setValue(function);
            function.setDebugContext(moduleName + "." + exportName);
            exports.defineOwnProperty(context, exportName, desc, false);
        }
        return exports;
    }

    private JSFunction buildFunction(GlobalContext globalContext, Object module, Method method) throws IllegalAccessException {
        return new JavaFunction(globalContext, module, method);
    }

    private Map<String, Object> modules = new HashMap<>();

    @Override
    public String generateModuleID(ExecutionContext context, String moduleName) {
        if (modules.get(moduleName) != null) return moduleName;
        return null;
    }

}
