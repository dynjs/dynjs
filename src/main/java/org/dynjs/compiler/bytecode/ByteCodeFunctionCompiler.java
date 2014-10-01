package org.dynjs.compiler.bytecode;

import org.dynjs.compiler.CompilationContext;
import org.dynjs.compiler.FunctionCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.DeclarativeEnvironmentRecord;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.wrapper.JavascriptFunction;

public class ByteCodeFunctionCompiler implements FunctionCompiler {

    public JSFunction compile(final CompilationContext context, final String identifier, final String[] formalParameters, final Statement body, final boolean strict) {
        int statementNumber = body.getStatementNumber();
        BlockManager.Entry entry = context.getBlockManager().retrieve(statementNumber);
        BasicBlock code = entry.getCompiled();
        if (code == null) {
            code = context.getCompiler().compileBasicBlock(context, "FunctionBody", body, strict);
            entry.setCompiled(code);
        }

        LexicalEnvironment lexEnv = null;

        if ( identifier != null ) {
            LexicalEnvironment funcEnv = LexicalEnvironment.newDeclarativeEnvironment( context.getLexicalEnvironment() );
            ((DeclarativeEnvironmentRecord)funcEnv.getRecord()).createMutableBinding(identifier, true);
            lexEnv = funcEnv;
        } else {
            lexEnv = context.getLexicalEnvironment();
        }

        JavascriptFunction function = new JavascriptFunction(context.getGlobalContext(), identifier, code, lexEnv, strict, formalParameters);
        if ( identifier != null ) {
            ((DeclarativeEnvironmentRecord)lexEnv.getRecord()).setMutableBinding(identifier, function, strict);
        }
        function.setDebugContext( "<anonymous>" );
        return function;
    }
}
