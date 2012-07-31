package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.RT;

public class BitwiseOperationStatement extends BaseStatement implements
		Statement {

	private final Statement leftHandStatement;
	private final Statement rightHandStatement;
	private final String operation;

	public BitwiseOperationStatement(Tree tree, String operation,
			Statement leftHandStatement, Statement rightHandStatement) {
		super(tree);
		this.operation = operation;
		this.leftHandStatement = leftHandStatement;
		this.rightHandStatement = rightHandStatement;
	}

	@Override
	public CodeBlock getCodeBlock() {
		return new CodeBlock() {
			{
				append(leftHandStatement.getCodeBlock());
				append(rightHandStatement.getCodeBlock());
				invokedynamic(operation, DynJSCompiler.Signatures.ARITY_2,
						RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
			}
		};
	}

}
