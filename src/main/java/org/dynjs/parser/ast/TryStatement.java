package org.dynjs.parser.ast;

import java.util.ArrayList;
import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class TryStatement extends BaseStatement {
    private final Statement tryBlock;
    private final CatchClause catchClause;
    private final Statement finallyBlock;

    public TryStatement(Position position, Statement tryBlock, CatchClause catchClause, Statement finallyBlock) {
        super(position);
        this.tryBlock = tryBlock;
        this.catchClause = catchClause;
        this.finallyBlock = finallyBlock;
    }

    public Statement getTryBlock() {
        return this.tryBlock;
    }

    public CatchClause getCatchClause() {
        return this.catchClause;
    }

    public Statement getFinallyBlock() {
        return this.finallyBlock;
    }

    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.tryBlock.getFunctionDeclarations();
    }

    public List<VariableDeclaration> getVariableDeclarations() {
        List<VariableDeclaration> decls = new ArrayList<>();
        decls.addAll(this.tryBlock.getVariableDeclarations());
        if (this.catchClause != null) {
            decls.addAll(this.catchClause.getVariableDeclarations());
        }
        return decls;
    }

    public int getSizeMetric() {
        return 7;
    }

    @Override
    public Completion interpret(ExecutionContext context, boolean debug) {
        Completion b = null;
        boolean finallyExecuted = false;
        try {
            b = invokeCompiledBlockStatement(context, "Try", getTryBlock());
        } catch (ThrowException e) {
            if (getCatchClause() != null) {
                // BasicBlock catchBlock = new InterpretedStatement(statement.getCatchClause().getBlock(), strict);
                BasicBlock catchBlock = compiledBlockStatement(context, "Catch", getCatchClause().getBlock());
                try {
                    b = ((ExecutionContext) context).executeCatch(catchBlock, getCatchClause().getIdentifier(), e.getValue());
                } catch (ThrowException e2) {
                    if (getFinallyBlock() != null) {
                        Completion f = invokeCompiledBlockStatement(context, "Finally", getFinallyBlock());
                        if (f.type == Completion.Type.NORMAL) {
                            if (b != null) {
                                return(b);
                            } else {
                                throw e2;
                            }
                        } else {
                            return(f);

                        }
                    } else {
                        throw e2;
                    }
                }
            }

            if (getFinallyBlock() != null) {
                finallyExecuted = true;
                Completion f = invokeCompiledBlockStatement(context, "Finally", getFinallyBlock());
                if (f.type == Completion.Type.NORMAL) {
                    if (b != null) {
                        return(b);
                    } else {
                        throw e;
                    }
                } else {
                    return(f);

                }
            } else {
                if (b != null) {
                    return(b);
                } else {
                    throw e;
                }
            }
        }

        if (!finallyExecuted && getFinallyBlock() != null) {
            Completion f = invokeCompiledBlockStatement(context, "Finally", getFinallyBlock());
            if (f.type == Completion.Type.NORMAL) {
                return(b);
            } else {
                return(f);
            }
        }

        return b;

    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("try {\n");
        buf.append(this.tryBlock.toIndentedString(indent + "  "));
        buf.append(indent).append("}\n");
        if (this.catchClause != null) {
            buf.append(this.catchClause.toIndentedString(indent + "  "));
        }
        if (this.finallyBlock != null) {
            buf.append(indent).append("finally {\n");
            buf.append(this.finallyBlock.toIndentedString(indent + "  "));
            buf.append(indent).append("}");
        }
        return buf.toString();
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return visitor.visit(context, this, strict);
    }

}
