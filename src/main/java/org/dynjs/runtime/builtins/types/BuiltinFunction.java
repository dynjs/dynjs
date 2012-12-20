package org.dynjs.runtime.builtins.types;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.FunctionDescriptor;
import org.dynjs.parser.js.ASTFactory;
import org.dynjs.parser.js.CharStream;
import org.dynjs.parser.js.CircularCharBuffer;
import org.dynjs.parser.js.JavascriptParser;
import org.dynjs.parser.js.Lexer;
import org.dynjs.parser.js.Parser;
import org.dynjs.parser.js.ParserException;
import org.dynjs.parser.js.TokenQueue;
import org.dynjs.parser.js.TokenStream;
import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.function.prototype.Apply;
import org.dynjs.runtime.builtins.types.function.prototype.Bind;
import org.dynjs.runtime.builtins.types.function.prototype.Call;
import org.dynjs.runtime.builtins.types.function.prototype.ToString;

public class BuiltinFunction extends AbstractBuiltinType {

    public BuiltinFunction(final GlobalObject globalObject) {
        super(globalObject, "args");

        final JSFunction proto = new AbstractNativeFunction(globalObject, false) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return Types.UNDEFINED;
            }
        };
        
        setPrototypeProperty(proto);
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setPrototype(globalObject.getPrototypeFor("Object"));
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject));
        defineNonEnumerableProperty(proto, "apply", new Apply(globalObject));
        defineNonEnumerableProperty(proto, "call", new Call(globalObject));
        defineNonEnumerableProperty(proto, "bind", new Bind(globalObject));
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.3.2.1
        int numArgs = args.length;
        String body = "";

        StringBuffer formalParams = new StringBuffer();
        boolean first = true;

        Set<String> seenParams = new HashSet<>();
        boolean duplicateFormalParams = false;

        for (int i = 0; i < numArgs - 1; ++i) {
            if (!first) {
                formalParams.append(",");
            }
            String param = Types.toString(context, args[i]);
            if (seenParams.contains(param)) {
                duplicateFormalParams = true;
            }
            seenParams.add(param);
            formalParams.append(param);
            first = false;
        }
        
        if (numArgs > 0) {
            body = Types.toString(context, args[numArgs - 1]);
        }

        StringBuffer code = new StringBuffer();
        code.append("function(" + formalParams.toString() + "){\n");
        code.append(body);
        code.append("}");
        
        try {
            FunctionDescriptor descriptor = parseFunction(context, code.toString());
            JSCompiler compiler = context.getGlobalObject().getCompiler();
            JSFunction function = compiler.compileFunction(context, descriptor.getIdentifier(), descriptor.getFormalParameterNames(), descriptor.getBlock(), descriptor.isStrict() );
            if (function.isStrict() && duplicateFormalParams) {
                throw new ThrowException(context, context.createSyntaxError("duplicate formal parameters in function definition"));
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
        Reader in = new StringReader( code );
        CharStream charStream = new CircularCharBuffer(in);
        Lexer lexer = new Lexer(charStream);
        TokenStream tokenStream = new TokenQueue(lexer);
        Parser parser = new Parser( new ASTFactory(), tokenStream );
        return parser.functionDescriptor();
    }

}
