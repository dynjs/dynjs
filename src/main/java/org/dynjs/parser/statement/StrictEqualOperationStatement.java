package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

public class StrictEqualOperationStatement extends AbstractStatement implements Statement {

	private final Statement leftHandStatement;
	private final Statement rightHandStatement;

	public StrictEqualOperationStatement(Tree tree, Statement leftHandStatement,
			Statement rightHandStatement) {
		super(tree);
		this.leftHandStatement = leftHandStatement;
		this.rightHandStatement = rightHandStatement;
	}

	@Override
	public CodeBlock getCodeBlock() {
		return new CodeBlock() {
			{
				append(leftHandStatement.getCodeBlock());
				append(rightHandStatement.getCodeBlock());
				invokedynamic("strict_eq",
						sig(Boolean.class, Object.class, Object.class),
						RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
			}
		};
	}
}
