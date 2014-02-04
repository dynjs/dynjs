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
    
    public String doIt(String arg, Number num) {
        return "One way";
    }
    
    public String doIt(String arg, Number num, String other) {
        return "Another way";
    }

    public String doIt(String arg, Number num, String other, Number otherNum) {
        return "Yet another way";
    }

    public Float getSomeFloat() {
        return 3.14F;
    }

    public Double getSomeDouble() {
        return 3.14;
    }
}
