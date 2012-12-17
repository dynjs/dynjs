package org.dynjs.runtime.interp;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractJavascriptFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.LexicalEnvironment;

public class InterpretedFunction extends AbstractJavascriptFunction {

    public InterpretedFunction(Statement body, LexicalEnvironment scope, boolean strict, String[] formalParameters) {
        super(body, scope, strict, formalParameters);
    }

    @Override
    public Object call(ExecutionContext context) {
        InterpretingVisitor visitor = new InterpretingVisitor();
        getBody().accept(context, visitor, isStrict() );
        return visitor.pop();
    }

}
