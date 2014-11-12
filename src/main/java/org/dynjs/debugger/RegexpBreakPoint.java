package org.dynjs.debugger;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dynjs.parser.Statement;

import java.util.regex.Pattern;

/**
 * @author Bob McWhirter
 */
public class RegexpBreakPoint implements BreakPoint {

    private final long number;
    private final Pattern regexp;
    private final long line;

    private long hitCount = 0;

    public RegexpBreakPoint(long number, String regexp, long line) {
        this.number = number;
        this.regexp = Pattern.compile(regexp);
        this.line = line;
    }

    @JsonProperty("script")
    public String getRegexp() {
        return this.regexp.toString();
    }

    public long getLine() {
        return this.line;
    }

    @Override
    public long getNumber() {
        return this.number;
    }

    @JsonProperty("hit_count")
    public long getHitCount() {
        return this.hitCount;
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

        if (result) {
            ++this.hitCount;
        }

        return result;
    }
}
