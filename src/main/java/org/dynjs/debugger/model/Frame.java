package org.dynjs.debugger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSCode;
import org.dynjs.runtime.SourceProvider;

import java.util.Collections;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class Frame {

    private final int index;
    private final ExecutionContext context;

    public Frame(int index, ExecutionContext context) {
        this.index = index;
        this.context = context;
    }

    public String getType() {
        return "frame";
    }

    public int getIndex() {
        return this.index;
    }

    @JsonIgnore
    public SourceProvider getScript() {
        return this.context.getSource();
    }

    public boolean isConstructor() {
        return this.context.isConstructor();
    }

    public int getLine() {
        return this.context.getLineNumber();
    }

    public int getColumn() {
        return this.context.getColumnNumber();
    }

    public List<Object> getScopes() {
        return Collections.emptyList();
    }

    public JSCode getFunc() {
        return this.context.getFunction();
    }

    public Object getReceiver() {
        return this.context.getThisBinding();
    }

}
