package org.dynjs.debugger;

import org.dynjs.parser.Statement;

/**
 * @author Bob McWhirter
 */
public interface BreakPoint {
    long getNumber();
    boolean shouldBreak(Statement statement, Statement previousStatement);
}

