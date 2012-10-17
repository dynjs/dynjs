package org.dynjs.runtime;

public class CallContext {
    
    private int pendingConstructorCount;
    
    public CallContext() {
        
    }
    
    public void incrementPendingConstructorCount() {
        ++this.pendingConstructorCount;
    }
    
    public void decrementPendingConstructorCount() {
        --this.pendingConstructorCount;
    }
    
    public int getPendingConstructorCount() {
        return this.pendingConstructorCount;
    }

}
