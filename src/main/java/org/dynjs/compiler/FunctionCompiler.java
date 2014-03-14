package org.dynjs.compiler;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.DeclarativeEnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.wrapper.JavascriptFunction;

public class FunctionCompiler {

    public JSFunction compile(final ExecutionContext context, final String identifier, final String[] formalParameters, final Statement body, final boolean strict) {
        LexicalEnvironment lexEnv = null;
        
        if ( identifier != null ) {
            LexicalEnvironment funcEnv = LexicalEnvironment.newDeclarativeEnvironment( context.getLexicalEnvironment() );
            ((DeclarativeEnvironmentRecord)funcEnv.getRecord()).createImmutableBinding(identifier);
            lexEnv = funcEnv;
        } else {
            lexEnv = context.getLexicalEnvironment();
        }
        int statementNumber = body.getStatementNumber();
        Entry entry = context.getBlockManager().retrieve(statementNumber);
        BasicBlock code = entry.getCompiled();
        if (code == null) {
            code = context.getCompiler().compileBasicBlock(context, "FunctionBody", body, strict);
            entry.setCompiled(code);
        }
        JavascriptFunction function = new JavascriptFunction(identifier, code, lexEnv, strict, formalParameters);
        if ( identifier != null ) {
            ((DeclarativeEnvironmentRecord)lexEnv.getRecord()).initializeImmutableBinding(identifier, function);
        }
        function.setDebugContext( "<anonymous>" );
        return function;
    }

}
