package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;

public class BreakStatement extends BaseStatement implements Statement {


	public BreakStatement(Tree tree, String id) {
		super(tree);
	}

	@Override
	public CodeBlock getCodeBlock() {
		return new CodeBlock() {
			{
				//go_to(breakStack.peek());
			}
		};
	}
}
