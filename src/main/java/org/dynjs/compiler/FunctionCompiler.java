package org.dynjs.compiler;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.wrapper.JavascriptFunction;

public class FunctionCompiler {

    public JSFunction compile(final ExecutionContext context, final String identifier, final String[] formalParameters, final Statement body, final boolean strict) {
        int statementNumber = body.getStatementNumber();
        Entry entry = context.getBlockManager().retrieve(statementNumber);
        BasicBlock code = entry.getCompiled();
        if (code == null) {
            code = context.getCompiler().compileBasicBlock(context, "FunctionBody", body, strict);
            entry.setCompiled(code);
        }
        JavascriptFunction function = new JavascriptFunction(identifier, code, context.getLexicalEnvironment(), strict, formalParameters);
        function.setDebugContext( "<anonymous>" );
        return function;
    }

}