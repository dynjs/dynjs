package org.dynjs.runtime;

public class ReferenceContext {

    private Reference reference;
    private ExecutionContext context;
    
    public static ReferenceContext create(Reference reference, ExecutionContext context) {
        return new ReferenceContext(reference, context);
    }

    public ReferenceContext(Reference reference, ExecutionContext context) {
        this.reference = reference;
        this.context = context;
    }
    
    public Reference getReference() {
        return this.reference;
    }
    
    public ExecutionContext getContext() {
        return this.context;
    }
    
}
