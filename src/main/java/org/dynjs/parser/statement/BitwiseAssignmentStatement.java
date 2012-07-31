package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.sig;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

public class BitwiseAssignmentStatement extends BaseStatement implements
		Statement {

	private final String operation;
	private final Statement leftHandStatement;
	private final Statement rightHandStatement;

	public BitwiseAssignmentStatement(Tree tree, String operation,
			Statement leftHandStatement, Statement rightHandStatement) {
		super(tree);
		this.operation = operation;
		this.leftHandStatement = leftHandStatement;
		this.rightHandStatement = rightHandStatement;
	}

	@Override
	public CodeBlock getCodeBlock() {
		final ResolveIdentifierStatement resolvable = (ResolveIdentifierStatement) leftHandStatement;
		return new CodeBlock() {
			{
				append(leftHandStatement.getCodeBlock());
				append(rightHandStatement.getCodeBlock());
				invokedynamic(operation, DynJSCompiler.Signatures.ARITY_2,
						RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
				aload(DynJSCompiler.Arities.THIS);
				swap();
				ldc(resolvable.getName());
				swap();
				invokedynamic(
						"dyn:setProp",
						sig(void.class, Object.class, Object.class,
								Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
			}
		};
	}

}
