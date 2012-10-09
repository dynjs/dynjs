package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.util.HashSet;
import java.util.Set;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Reference;

public class VariableDeclaration extends AbstractExpression {
    
    private static final Set<String> FUTURE_RESERVED_WORDS = new HashSet<String>() {{
        add( "implements");
        add( "let");
        add( "private");
        add( "public");
        add( "yield");
        add( "interface");
        add( "package");
        add( "protected");
        add( "static");
    }};

    private String identifier;
    private Expression initializer;

    public VariableDeclaration(Tree tree, String identifier, Expression initializer) {
        super(tree);
        this.identifier = identifier;
        this.initializer = initializer;
    }

    public String getIdentifier() {
        return this.identifier;
    }
    
    public void checkStrictCompliance(ExecutionContext context, boolean strict) {
        if ( strict ) {
            if ( FUTURE_RESERVED_WORDS.contains( this.identifier ) ) {
                throw new ThrowException(context, context.createSyntaxError( "'" + this.identifier + "' is not allowed as an identifier in strict-mode code" ) );
            }
        }
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                // 12.2
                if (initializer == null) {
                    ldc(identifier);
                    // str
                } else {
                    append(jsResolve(identifier));
                    // reference
                    aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                    // reference context
                    append(initializer.getCodeBlock());
                    // reference context val
                    append(jsGetValue());
                    // reference context val
                    invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
                    // reference 
                    ldc(identifier);
                    // str
                }
            }
        };
    }

    public String dump(String indent) {
        StringBuffer buf = new StringBuffer();

        buf.append("var " + this.identifier);
        if (this.initializer != null) {
            buf.append(this.initializer.dump(indent + "  "));
        }

        return buf.toString();
    }


}
