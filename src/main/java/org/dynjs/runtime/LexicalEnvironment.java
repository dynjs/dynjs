package org.dynjs.runtime;

public class LexicalEnvironment {

    public static LexicalEnvironment newDeclarativeEnvironment(LexicalEnvironment outer) {
        return new LexicalEnvironment(new DeclarativeEnvironmentRecord(), outer);
    }

    public static LexicalEnvironment newObjectEnvironment(JSObject object, boolean provideThis, LexicalEnvironment outer) {
        return new LexicalEnvironment(new ObjectEnvironmentRecord(object, provideThis), outer);
    }

    public static LexicalEnvironment newGlobalEnvironment(DynJS runtime) {
        return new LexicalEnvironment(new ObjectEnvironmentRecord(runtime.getGlobalContext().getObject(), false), null);
    }
    
    public static LexicalEnvironment newGlobalEnvironment(JSObject globalObject) {
        return new LexicalEnvironment(new ObjectEnvironmentRecord(globalObject, false), null);
    }

    private LexicalEnvironment outer;
    private EnvironmentRecord record;

    private LexicalEnvironment(EnvironmentRecord record, LexicalEnvironment outer) {
        this.record = record;
        this.outer = outer;
    }

    public EnvironmentRecord getRecord() {
        return this.record;
    }

    public LexicalEnvironment getOuter() {
        return this.outer;
    }

    public Reference getIdentifierReference(ExecutionContext context, String name, boolean strict) {
        // 10.2.2.1
        boolean exists = record.hasBinding(context, name);
        if (exists) {
            return new Reference(name, this.record, strict);
        }

        if (outer == null) {
            return new Reference(name, Types.UNDEFINED, strict);
        }
        
        return outer.getIdentifierReference(context, name, strict);
    }

}
