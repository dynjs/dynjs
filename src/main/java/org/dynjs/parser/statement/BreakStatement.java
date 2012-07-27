package org.dynjs.parser.statement;

import java.util.Stack;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.objectweb.asm.tree.LabelNode;

public class BreakStatement extends BaseStatement implements Statement {

	private Stack<LabelNode> breakStack;

	public BreakStatement(Stack<LabelNode> breakStack, Tree tree, String id) {
		super(tree);
		this.breakStack = breakStack;
	}

	@Override
	public CodeBlock getCodeBlock() {
		return new CodeBlock() {
			{
				go_to(breakStack.peek());
			}
		};
	}
}
