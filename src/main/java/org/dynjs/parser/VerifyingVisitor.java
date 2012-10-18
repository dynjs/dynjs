package org.dynjs.parser;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.AssignmentExpression;
import org.dynjs.parser.ast.CompoundAssignmentExpression;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.IdentifierReferenceExpression;
import org.dynjs.parser.ast.PostOpExpression;
import org.dynjs.parser.ast.PreOpExpression;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.parser.ast.WithStatement;
import org.dynjs.runtime.ExecutionContext;

public class VerifyingVisitor extends DefaultVisitor {

    /*
     * @Override
     * public void visit(ExecutionContext context, AbstractUnaryOperatorExpression expr, boolean strict) {
     * if (strict) {
     * if (expr.getExpr() instanceof IdentifierReferenceExpression) {
     * String ident = ((IdentifierReferenceExpression) expr.getExpr()).getIdentifier();
     * VerifierUtils.verifyStrictIdentifier(context, ident);
     * }
     * }
     * super.visit(context, expr, strict);
     * }
     */

    @Override
    public void visit(ExecutionContext context, VariableDeclaration expr, boolean strict) {
        if (strict) {
            String ident = expr.getIdentifier();
            VerifierUtils.verifyStrictIdentifier(context, ident);
        }
        super.visit(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, WithStatement statement, boolean strict) {
        if ( strict ) {
            throw new ThrowException( context, context.createSyntaxError( "with() not allowed within strict code" ) );
        }
    }

    @Override
    public void visit(ExecutionContext context, AssignmentExpression expr, boolean strict) {
        if (strict) {
            if (expr.getLhs() instanceof IdentifierReferenceExpression) {
                String ident = ((IdentifierReferenceExpression) expr.getLhs()).getIdentifier();
                VerifierUtils.verifyStrictIdentifier(context, ident);
            }
        }
        super.visit(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, CompoundAssignmentExpression expr, boolean strict) {
        if (strict) {
            Expression lhs = expr.getRootExpr().getLhs();
            if (lhs instanceof IdentifierReferenceExpression) {
                String ident = ((IdentifierReferenceExpression) lhs).getIdentifier();
                VerifierUtils.verifyStrictIdentifier(context, ident);
            }
        }
        super.visit(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, FunctionDeclaration statement, boolean strict) {
        // DO NOT WALK
    }

    @Override
    public void visit(ExecutionContext context, PostOpExpression expr, boolean strict) {
        if (strict) {
            Expression lhs = expr.getExpr();
            if (lhs instanceof IdentifierReferenceExpression) {
                String ident = ((IdentifierReferenceExpression) lhs).getIdentifier();
                VerifierUtils.verifyStrictIdentifier(context, ident);
            }
        }
        super.visit(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, PreOpExpression expr, boolean strict) {
        if (strict) {
            Expression lhs = expr.getExpr();
            if (lhs instanceof IdentifierReferenceExpression) {
                String ident = ((IdentifierReferenceExpression) lhs).getIdentifier();
                VerifierUtils.verifyStrictIdentifier(context, ident);
            }
        }
        super.visit(context, expr, strict);
    }
    
    

}
