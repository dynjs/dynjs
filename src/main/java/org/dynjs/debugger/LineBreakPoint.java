package org.dynjs.debugger;

import org.dynjs.parser.Statement;

/**
 * @author Bob McWhirter
 */
public class LineBreakPoint {

    private final long number;

    private final String fileName;
    private long line = -1;
    private long column = -1;

    public LineBreakPoint(long number, String fileName, long line, long column) {
        this.number = number;
        this.fileName = fileName;
        this.line = line;
        this.column = column;
    }

    public long getNumber() {
        return this.number;
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean shouldBreak(Statement statement, Statement previousStatement) {

        if (statement.getPosition() == null) {
            return false;
        }

        if (!this.fileName.equals(statement.getPosition().getFileName())) {
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

    public String toString() {
        return "[BreakPoint: fileName=" + this.fileName + "; line=" + this.line + "]";
    }

}
