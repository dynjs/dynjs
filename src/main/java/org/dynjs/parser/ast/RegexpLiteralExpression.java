package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;

public class RegexpLiteralExpression extends AbstractExpression {

    private String pattern;
    private String flags;

    public RegexpLiteralExpression(Tree tree, String text) {
        super(tree );
        int secondSlashLoc = text.indexOf( "/", 1 );
        this.pattern = text.substring( 1, secondSlashLoc );
        this.flags = text.substring( secondSlashLoc + 1 );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                aload( JSCompiler.Arities.EXECUTION_CONTEXT );
                // context
                invokestatic(p(BuiltinRegExp.class), "newRegExp", sig(DynRegExp.class, ExecutionContext.class));
                // regexp
                dup();
                // regexp 
                ldc( pattern );
                // regexp regexp str
                ldc( flags );
                // regexp regexp str str
                invokevirtual(p(DynRegExp.class), "setPatternAndFlags", sig(void.class, String.class, String.class));
                // regexp
            }
        };
    }
}
