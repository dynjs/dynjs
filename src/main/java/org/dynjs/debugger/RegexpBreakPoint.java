package org.dynjs.debugger;

import org.dynjs.parser.Statement;

import java.util.regex.Pattern;

/**
 * @author Bob McWhirter
 */
public class RegexpBreakPoint implements BreakPoint {

    private final long number;
    private final Pattern regexp;
    private final long line;

    public RegexpBreakPoint(long number, String regexp, long line) {
        this.number = number;
        this.regexp = Pattern.compile( regexp );
        this.line = line;
    }

    @Override
    public long getNumber() {
        return this.number;
    }

    @Override
    public boolean shouldBreak(Statement statement, Statement previousStatement) {
        if (statement.getPosition() == null) {
            return false;
        }

        if ( ! this.regexp.matcher( statement.getPosition().getFileName() ).matches() ) {
            return false;
        }

        if (this.line >= 0) {
            if (this.line <= statement.getPosition().getLine()) {
                if (previousStatement == null) {
                    return true;
                }

                return (this.line > (previousStatement.getPosition().getLine() - 1));
            } else {
                return false;
            }
        }

        return false;
    }
}
