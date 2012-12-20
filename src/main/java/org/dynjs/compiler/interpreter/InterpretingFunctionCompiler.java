package org.dynjs.compiler.interpreter;

import org.dynjs.compiler.FunctionCompiler;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.interp.InterpretedFunction;

public class InterpretingFunctionCompiler implements FunctionCompiler {

    @Override
    public JSFunction compile(ExecutionContext context, String identifier, String[] formalParameters, BlockStatement body, boolean strict) {
        return new InterpretedFunction(identifier, body, context.getLexicalEnvironment(), strict, formalParameters);
    }

}
