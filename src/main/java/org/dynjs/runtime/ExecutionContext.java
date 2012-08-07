package org.dynjs.runtime;

public class ExecutionContext {

    private LexicalEnvironment lexicalEnvironment;
    private LexicalEnvironment variableEnvironment;
    private JSObject thisBinding;
    private boolean strict;

    public ExecutionContext(LexicalEnvironment lexicalEnvironment, LexicalEnvironment variableEnvironment, JSObject thisBinding, boolean strict) {
        this.lexicalEnvironment = lexicalEnvironment;
        this.variableEnvironment = variableEnvironment;
        this.thisBinding = thisBinding;
        this.strict = strict;
    }

    public LexicalEnvironment getLexicalEnvironment() {
        return this.lexicalEnvironment;
    }

    public LexicalEnvironment getVariableEnvironment() {
        return this.variableEnvironment;
    }

    public JSObject getThisBinding() {
        return this.thisBinding;
    }
    
    public Object resolve(String name) {
        return this.lexicalEnvironment.getIdentifierReference( name, this.strict );
    }
    

}
