package org.dynjs.debugger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dynjs.parser.Statement;

/**
 * @author Bob McWhirter
 */
public abstract class Breakpoint {

    private static int COUNTER = 0;

    protected final String target;
    protected final int line;
    protected final int column;

    private long number;
    private long hitCount;
    private boolean enabled = true;

    public Breakpoint(String target, int line, int column) {
        this.number = ++COUNTER;
        this.target = target;
        this.line = line;
        this.column = column;
    }

    public String getTarget() {
        return this.target;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public long getNumber() {
        return this.number;
    }

    @JsonProperty("hit_count")
    public long getHitCount() {
        return this.hitCount;
    }

    public void incrHitCount() {
        ++this.hitCount;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public abstract boolean shouldBreak(Statement statement, Statement previousStatement);

}

