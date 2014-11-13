package org.dynjs.runtime.builtins.types;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.FunctionDescriptor;
import org.dynjs.parser.js.ASTFactory;
import org.dynjs.parser.js.CharStream;
import org.dynjs.parser.js.CircularCharBuffer;
import org.dynjs.parser.js.Lexer;
import org.dynjs.parser.js.Parser;
import org.dynjs.parser.js.ParserException;
import org.dynjs.parser.js.TokenQueue;
import org.dynjs.parser.js.TokenStream;
import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.function.prototype.Apply;
import org.dynjs.runtime.builtins.types.function.prototype.Bind;
import org.dynjs.runtime.builtins.types.function.prototype.Call;
import org.dynjs.runtime.builtins.types.function.prototype.ToString;

public class BuiltinFunction extends AbstractBuiltinType {

    public BuiltinFunction(final GlobalContext globalContext) {
        super(globalContext, "args");

        final JSFunction proto = new AbstractNativeFunction(globalContext, false) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return Types.UNDEFINED;
            }
        };

        setPrototypeProperty(proto);
    }

    @Override
    public void initialize(GlobalContext globalContext, JSObject proto) {
        proto.setPrototype(globalContext.getPrototypeFor("Object"));
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalContext));
        defineNonEnumerableProperty(proto, "apply", new Apply(globalContext));
        defineNonEnumerableProperty(proto, "call", new Call(globalContext));
        defineNonEnumerableProperty(proto, "bind", new Bind(globalContext));
        //defineNonEnumerableProperty(proto, "__proto__", proto);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.2.1
        int numArgs = args.length;
        String body = "";

        StringBuilder formalParams = new StringBuilder();
        boolean first = true;

        Set<String> seenParams = new HashSet<>();
        boolean duplicateFormalParams = false;

        for (int i = 0; i < numArgs - 1; ++i) {
            String paramStr = Types.toString(context, args[i]);
            StringTokenizer paramTokens = new StringTokenizer(paramStr, ",");
            while (paramTokens.hasMoreTokens()) {
                if (!first) {
                    formalParams.append(",");
                }
                String param = paramTokens.nextToken().trim();
                if (seenParams.contains(param)) {
                    duplicateFormalParams = true;
                }
                seenParams.add(param);
                formalParams.append(param);
                first = false;
            }
        }

        if (numArgs > 0) {
            body = Types.toString(context, args[numArgs - 1]);
        }

        StringBuilder code = new StringBuilder();
        code.append("function(" + formalParams.toString() + "){");
        code.append(body);
        code.append("}");

        try {
            FunctionDescriptor descriptor = parseFunction(context, code.toString());
            JSCompiler compiler = context.getCompiler();
            JSFunction function = compiler.compileFunction(context, descriptor.getIdentifier(), descriptor.getFormalParameterNames(), descriptor.getBlock(),
                    descriptor.isStrict());
            if (function.isStrict()) {
                if (duplicateFormalParams) {
                    throw new ThrowException(context, context.createSyntaxError("duplicate formal parameters in function definition"));
                }
                if (seenParams.contains("eval")) {
                    throw new ThrowException(context, context.createSyntaxError("formal parameter 'eval' not allowed in function definition in strict-mode"));
                }
            }
            function.setPrototype(getPrototype());
            return function;
        } catch (ParserException e) {
            throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
        } catch (IOException e) {
            throw new ThrowException(context, context.createSyntaxError(e.getMessage()));
        }

    }

    public FunctionDescriptor parseFunction(ExecutionContext context, String code) throws IOException {
        String filename = null;
        ExecutionContext parent = context.getParent();
        if (parent != null) {
            Reference filenameReference = parent.resolve("__FILENAME__");
            if (!filenameReference.isUnresolvableReference()) {
                filename = filenameReference.getValue(parent).toString();
            }
        }

        Reader in = new StringReader(code);
        CharStream charStream = new CircularCharBuffer(in);
        Lexer lexer = new Lexer(charStream);
        if (filename != null) {
            lexer.setFileName(filename);
        }
        TokenStream tokenStream = new TokenQueue(lexer);
        Parser parser = new Parser(context, new ASTFactory(), tokenStream);
        return parser.functionDescriptor();
    }

    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinFunction.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Function>";
    }

}
