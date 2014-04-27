package org.dynjs.ir;

import java.util.List;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.VariableValues;

public class IRJSFunction extends DynObject implements JSFunction {
    private final FunctionScope scope;
    private Instruction[] instructions;
    private final LexicalEnvironment lexicalEnvironment;
    private String debugContext = "";
    // Lexically-captured values of this function
    private VariableValues capturedValues;

    public IRJSFunction(FunctionScope scope, VariableValues capturedValues, LexicalEnvironment lexicalEnvironment,
                        GlobalObject globalObject) {
        super(globalObject);
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
        return new DynObject(context.getGlobalObject());
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

        return Interpreter.execute(context, scope, instructions);
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
