package org.dynjs.debugger;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dynjs.parser.Statement;

/**
 * @author Bob McWhirter
 */
public class LineBreakPoint implements BreakPoint {

    private final long number;

    private final String fileName;
    private long line = -1;
    private long column = -1;

    private long hitCount = 0;
    public LineBreakPoint(long number, String fileName, long line, long column) {
        this.number = number;
        this.fileName = fileName;
        this.line = line;
        this.column = column;
    }

    public long getNumber() {
        return this.number;
    }

    @JsonProperty("script")
    public String getFileName() {
        return this.fileName;
    }


    @JsonProperty("hit_count")
    public long getHitCount() {
        return this.hitCount;
    }

    public boolean shouldBreak(Statement statement, Statement previousStatement) {

        if (statement.getPosition() == null) {
            return false;
        }

        if (!this.fileName.equals(statement.getPosition().getFileName())) {
            return false;
        }

        boolean result = false;

        if (this.line >= 0) {
            if (this.line <= statement.getPosition().getLine()) {
                if (previousStatement == null) {
                    result =  true;
                } else {
                    result = (this.line > (previousStatement.getPosition().getLine() - 1));
                }
            } else {
                result = false;
            }
        }

        if ( result ) {
            ++this.hitCount;
        }

        return result;
    }

    public String toString() {
        return "[BreakPoint: fileName=" + this.fileName + "; line=" + this.line + "]";
    }

}
