package org.dynjs.ir;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSCallable;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.VariableValues;

import java.util.List;

public class IRJSFunction extends DynObject implements JSFunction {
    private final FunctionScope scope;
    private Instruction[] instructions;
    private final LexicalEnvironment lexicalEnvironment;
    private String debugContext = "";
    // Lexically-captured values of this function
    private VariableValues capturedValues;

    public JSFunction compile(ExecutionContext context) {
        return new IRByteCodeCompiler(scope, getFileName(), isStrict()).compileFunction(context);
    }

    private static class IRJSFunctionBox {
        public int callCount = 0;
        public JSCallable compiledFunction;
        public boolean compilationInProgress;
    }

    private IRJSFunctionBox box = new IRJSFunctionBox();

    public IRJSFunction(FunctionScope scope, VariableValues capturedValues, LexicalEnvironment lexicalEnvironment,
                        GlobalContext globalContext) {
        super(globalContext);
        this.scope = scope;
        this.instructions = scope.prepareForInterpret(); // FIXME This is a big up front cost...make lazy
        this.lexicalEnvironment = lexicalEnvironment;
        this.capturedValues = capturedValues;
    }

    public String[] getFormalParameters() {
        return scope.getParameterNames();
    }

    public LexicalEnvironment getScope() {
        return lexicalEnvironment;
    }

    @Override
    public String getFileName() {
        return scope.getFileName();
    }

    // FIXME: Not sure but constructor is likely a different beast than IRJSFunction
    public boolean isConstructor() {
        return false;
    }

    public String getDebugContext() {
        return debugContext;
    }

    public void setDebugContext(String debugContext) {
        this.debugContext = debugContext;
    }

    public JSObject createNewObject(ExecutionContext context) {
        return new DynObject(context.getGlobalContext());
    }

    // FIXME: Stolen from AbstractionFunction (could be refactored out unless we can somehow have whatever calls this
    // replaced by IR (I have no idea)
    public boolean hasInstance(ExecutionContext context, Object v) {
        if (!(v instanceof JSObject)) {
            return false;
        }

        Object o = get(null, "prototype");
        if (!(o instanceof JSObject)) {

            throw new ThrowException(context, context.createTypeError("prototype must be an object"));
        }

        JSObject proto = (JSObject) o;

        if (proto == null || v == Types.UNDEFINED) {
            return false;
        }

        while (true) {
            v = ((JSObject) v).getPrototype();
            if (v == null || v == Types.UNDEFINED) {
                return false;
            }
            if (v == proto) {
                return true;
            }
        }
    }

    @Override
    public Object call(ExecutionContext context) {
        // Allocate space for variables of this function and establish link to captured ones.
        context.allocVars(scope.getLocalVariableSize(), capturedValues);

        if (box.compilationInProgress || box.callCount >= 0) {
            if (tryCompile(context)) {
                return callJitted(context);
            }
        }

        return Interpreter.execute(context, scope, instructions);
    }

    private Object callJitted(ExecutionContext context) {
        return box.compiledFunction.call(context);
    }

    private boolean tryCompile(ExecutionContext context) {
        if (box.compiledFunction != null) {
            return true;
        }

        if (box.callCount++ >= context.getConfig().getJitThreshold()) {
            box.callCount = -1; // disable, we get one shot
            if (!box.compilationInProgress) {
                if (context.getConfig().isJitEnabled()) {
                    final JITCompiler compiler = context.getRuntime().getJitCompiler();
                    box.compilationInProgress = true;
                    compiler.compile(context, this, new JITCompiler.CompilerCallback() {
                        @Override
                        public void done(JSFunction compiledFunction) {
                            box.compiledFunction = compiledFunction;
                        }
                    });
                }

            }

        }
        return false;
    }

    @Override
    public boolean isStrict() {
        return scope.isStrict();
    }

    // FIXME: Remove or replace once we learn how IR should handle these
    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return FunctionDeclaration.EMPTY_LIST;
    }

    // FIXME: Remove or replace once we learn how IR should handle these
    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return VariableDeclaration.EMPTY_LIST;
    }
}
