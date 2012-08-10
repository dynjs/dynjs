package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.sig;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

public class UnaryMinusStatement extends AbstractStatement implements
		Statement {

	private Statement expression;

	public UnaryMinusStatement(Tree tree, Statement expression) {
		super(tree);
		this.expression = expression;
	}

	@Override
	public CodeBlock getCodeBlock() {
		return new CodeBlock() {
			{
				append(expression.getCodeBlock());
				invokedynamic("unary_minus", sig(Double.class, Object.class),
						RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
			}
		};
	}
}
