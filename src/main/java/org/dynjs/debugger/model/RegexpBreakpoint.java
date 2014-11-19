package org.dynjs.debugger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dynjs.parser.Statement;

import java.util.regex.Pattern;

/**
 * @author Bob McWhirter
 */
public class RegexpBreakpoint extends Breakpoint {

    private final Pattern regexp;

    public RegexpBreakpoint(String regexp, int line, int column) {
        super( regexp, line, column );
        this.regexp = Pattern.compile(regexp);
    }

    @JsonProperty("script")
    public String getRegexp() {
        return this.target;
    }

    @Override
    public boolean shouldBreak(Statement statement, Statement previousStatement) {
        if (statement.getPosition() == null) {
            return false;
        }

        if (!this.regexp.matcher(statement.getPosition().getFileName()).matches()) {
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
