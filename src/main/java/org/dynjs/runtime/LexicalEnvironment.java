package org.dynjs.runtime;

public class LexicalEnvironment {

    public static LexicalEnvironment newDeclarativeEnvironment(LexicalEnvironment outer) {
        return new LexicalEnvironment(new DeclarativeEnvironmentRecord(), outer);
    }

    public static LexicalEnvironment newObjectEnvironment(JSObject object, boolean provideThis, LexicalEnvironment outer) {
        return new LexicalEnvironment(new ObjectEnvironmentRecord(object, provideThis), outer);
    }

    public static LexicalEnvironment newGlobalEnvironment(DynJS runtime) {
        return new LexicalEnvironment(new ObjectEnvironmentRecord(GlobalObject.newGlobalObject(runtime), false), null);
    }
    
    public static LexicalEnvironment newGlobalEnvironment(GlobalObject globalObject) {
        return new LexicalEnvironment(new ObjectEnvironmentRecord(globalObject, false), null);
    }

    private LexicalEnvironment outer;
    private EnvironmentRecord record;
    private GlobalObject globalObject;

    private LexicalEnvironment(EnvironmentRecord record, LexicalEnvironment outer) {
        this.record = record;
        this.outer = outer;
        if (this.outer == null) {
            this.globalObject = (GlobalObject) ((ObjectEnvironmentRecord) this.record).getBindingObject();
        } else {
            this.globalObject = this.outer.getGlobalObject();
        }
    }

    public EnvironmentRecord getRecord() {
        return this.record;
    }

    public LexicalEnvironment getOuter() {
        return this.outer;
    }

    public GlobalObject getGlobalObject() {
        return this.globalObject;
    }

    public Reference getIdentifierReference(ExecutionContext context, String name, boolean strict) {
        // 10.2.2.1
        boolean exists = record.hasBinding(context, name);
        if (exists) {
            return new Reference(getGlobalObject(), name, this.record, strict);
        }

        if (outer == null) {
            return new Reference(getGlobalObject(), name, Types.UNDEFINED, strict);
        }
        
        return outer.getIdentifierReference(context, name, strict);
    }

}
