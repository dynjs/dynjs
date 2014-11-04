package org.dynjs.debugger.events;

import org.dynjs.debugger.Debugger;

/**
 * @author Bob McWhirter
 */
public class BreakEvent extends AbstractEvent {

    private final int sourceLine;
    private final int sourceColumn;
    private final ScriptInfo script;

    public BreakEvent(Debugger debugger, int sourceLine, int sourceColumn, ScriptInfo script) {
        super(debugger, "break");
        this.sourceLine = sourceLine;
        this.sourceColumn = sourceColumn;
        this.script = script;
    }

    public int getSourceLine() {
        return this.sourceLine;
    }

    public int getSourceColumn() {
        return this.sourceColumn;
    }

    public ScriptInfo getScript() {
        return this.script;
    }

}
