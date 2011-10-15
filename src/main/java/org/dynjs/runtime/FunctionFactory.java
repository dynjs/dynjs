package org.dynjs.runtime;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;

import java.util.Deque;

public class FunctionFactory implements Function {

    private final Class<Function> clazz;

    public FunctionFactory(Class<Function> clazz) {
        this.clazz = clazz;
    }

    public static FunctionFactory create(Class<Function> clazz) {
        return new FunctionFactory(clazz);
    }

    @Override
    public DynAtom call(DynThreadContext context, Scope scope, DynAtom[] arguments) {
        Function function = instantiate();
        RT.paramPopulator((DynFunction) function, arguments);
        Deque<Function> callStack = context.getCallStack();
        callStack.push(function);
        DynAtom result = function.call(context, scope, arguments);
        callStack.pop();
        return result;
    }

    private Function instantiate() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public Scope getEnclosingScope() {
        return null;
    }

    @Override
    public Object resolve(String name) {
        return null;
    }

    @Override
    public void define(String property, DynAtom value) {
    }

}
