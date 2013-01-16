package org.dynjs.codegen;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;

public class DereferencedReference {

    public static Object create(Object reference, Object value) {
        if (reference instanceof Reference) {
            return new DereferencedReference((Reference)reference, value);
        }
        return reference;
    }

    private Reference reference;
    private Object value;

    public DereferencedReference(Reference reference, Object value) {
        this.reference = reference;
        this.value = value;
    }

    public Reference getReference() {
        return this.reference;
    }

    public Object getValue() {
        return this.value;
    }
    
    public String toString() {
        return "[DerefRef: value=" + this.value + "; ref=" + this.reference + "]";
    }
    
}
