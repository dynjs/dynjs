package org.dynjs.runtime;

public class DefaultObjectFactory implements GlobalObjectFactory {
    @Override
    public GlobalObject newGlobalObject(DynJS runtime) {
        return new GlobalObject(runtime);
    }
}
