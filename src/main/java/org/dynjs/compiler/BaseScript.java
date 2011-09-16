package org.dynjs.compiler;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.Script;

import java.util.List;

public class BaseScript {

    private final Statement[] statements;

    public BaseScript(Statement... statements){
        this.statements = statements;
    }

}
