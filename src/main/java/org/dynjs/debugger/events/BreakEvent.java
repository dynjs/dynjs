package org.dynjs.debugger.events;

import org.dynjs.debugger.Debugger;
import org.dynjs.debugger.model.Script;

/**
 * @author Bob McWhirter
 */
public class BreakEvent extends AbstractEvent {

    private final int sourceLine;
    private final int sourceColumn;
    private final Script script;

    public BreakEvent(Debugger debugger, int sourceLine, int sourceColumn, Script script) {
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

    public Script getScript() {
        return this.script;
    }

}
