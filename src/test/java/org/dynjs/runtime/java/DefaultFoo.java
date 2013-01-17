package org.dynjs.runtime.java;

public class DefaultFoo extends AbstractFoo {

    @Override
    public String doIt() {
        return "done in java";
    }
    
    public String doItDifferently() {
        return "done with: " + getContent();
    }
    
    public String getContent() {
        return "default content";
    }
    
    public void avoidMe() {
        
    }

}
