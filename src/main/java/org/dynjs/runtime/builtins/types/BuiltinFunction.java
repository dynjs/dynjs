package org.dynjs.runtime.builtins.types;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ES3Lexer;
import org.dynjs.parser.ES3Parser;
import org.dynjs.parser.ES3Walker;
import org.dynjs.parser.Executor;
import org.dynjs.parser.ast.FunctionDescriptor;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.function.prototype.Apply;
import org.dynjs.runtime.builtins.types.function.prototype.Bind;
import org.dynjs.runtime.builtins.types.function.prototype.Call;
import org.dynjs.runtime.builtins.types.function.prototype.ToString;

public class BuiltinFunction extends AbstractNativeFunction {

    public BuiltinFunction(final GlobalObject globalObject) {
        super(globalObject, "args");
        
        // register ourselves with the Global before he can.
        globalObject.put(null, "Function", this, false);

        final JSFunction proto = new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return Types.UNDEFINED;
            }
        };

        put( null, "prototype", proto, false );
        proto.put(null, "constructor", this, false);
        proto.put(null, "toString", new ToString(globalObject), false);
        proto.put(null, "apply", new Apply(globalObject), false);
        proto.put(null, "call", new Call(globalObject), false);
        proto.put(null, "bind", new Bind(globalObject), false);
        proto.setPrototype( globalObject.getPrototypeFor( "Object" ) );
        

        setPrototype( proto );
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.2.1
        int numArgs = args.length;
        String body = "";
        if (numArgs > 0) {
            body = Types.toString(args[numArgs - 1]);
        }

        StringBuffer formalParams = new StringBuffer();
        boolean first = true;

        for (int i = 0; i < numArgs - 1; ++i) {
            if (!first) {
                formalParams.append(",");
            }
            formalParams.append(Types.toString(args[i]));
            first = false;
        }

        StringBuffer code = new StringBuffer();
        code.append("function(" + formalParams.toString() + "){\n");
        code.append(body);
        code.append("}");

        try {
            FunctionDescriptor descriptor = parseFunction(context, code.toString());
            JSCompiler compiler = context.getGlobalObject().getCompiler();
            JSFunction function = compiler.compileFunction(context, descriptor.getFormalParameters(), descriptor.getBlock());
            function.setPrototype(getPrototype());
            return function;
        } catch (RecognitionException e) {
            throw new ThrowException(context.createSyntaxError(e.getMessage()));
        }

    }

    public FunctionDescriptor parseFunction(ExecutionContext context, String code) throws RecognitionException {
        final ANTLRStringStream stream = new ANTLRStringStream(code);
        ES3Lexer lexer = new ES3Lexer(stream);

        CommonTokenStream lexerStream = new CommonTokenStream(lexer);
        ES3Parser parser = new ES3Parser(lexerStream);

        ES3Parser.functionExpression_return function = parser.functionExpression();
        List<String> errors = parser.getErrors();
        if (!errors.isEmpty()) {
            System.err.println(errors);
            throw new ThrowException(context.createSyntaxError(errors.get(0)));
        }

        CommonTree tree = (CommonTree) function.getTree();
        CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(tree);
        treeNodeStream.setTokenStream(lexerStream);
        ES3Walker walker = new ES3Walker(treeNodeStream);

        Executor executor = new Executor();
        executor.setBlockManager(context.getBlockManager());
        walker.setExecutor(executor);
        FunctionDescriptor descriptor = walker.functionDescriptor();
        return descriptor;
    }

}
