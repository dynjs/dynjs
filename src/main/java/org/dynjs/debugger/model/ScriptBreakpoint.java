package org.dynjs.debugger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dynjs.parser.Statement;

/**
 * @author Bob McWhirter
 */
public class ScriptBreakpoint extends Breakpoint {

    public ScriptBreakpoint(String fileName, int line, int column) {
        super(fileName, line, column);
    }

    @JsonProperty("script")
    public String getFileName() {
        return this.target;
    }

    public boolean shouldBreak(Statement statement, Statement previousStatement) {

        if (statement.getPosition() == null) {
            return false;
        }

        if (!this.target.equals(statement.getPosition().getFileName())) {
            return false;
        }

        boolean result = false;

        if (this.line >= 0) {
            if (this.line <= statement.getPosition().getLine()) {
                if (previousStatement == null) {
                    result = true;
                } else {
                    result = (this.line > (previousStatement.getPosition().getLine() - 1));
                }
            } else {
                result = false;
            }
        }

        return result;
    }

}
