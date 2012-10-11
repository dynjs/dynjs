package org.dynjs;

public class ManualClock implements Clock {

    private long currentTimeMillis;
    
    public ManualClock() {
        this.currentTimeMillis = System.currentTimeMillis();
    }
    
    public ManualClock(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }
    
    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }
    
    @Override
    public long currentTimeMillis() {
        return this.currentTimeMillis;
    }

}
