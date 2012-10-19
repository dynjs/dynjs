package org.dynjs.parser;

import java.util.HashSet;
import java.util.Set;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.AssignmentExpression;
import org.dynjs.parser.ast.CompoundAssignmentExpression;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.IdentifierReferenceExpression;
import org.dynjs.parser.ast.NamedValue;
import org.dynjs.parser.ast.ObjectLiteralExpression;
import org.dynjs.parser.ast.PostOpExpression;
import org.dynjs.parser.ast.PreOpExpression;
import org.dynjs.parser.ast.PropertyAssignment;
import org.dynjs.parser.ast.PropertyGet;
import org.dynjs.parser.ast.PropertySet;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.parser.ast.WithStatement;
import org.dynjs.runtime.ExecutionContext;

public class VerifyingVisitor extends DefaultVisitor {

    @Override
    public void visit(ExecutionContext context, ObjectLiteralExpression expr, boolean strict) {
        Set<String> values = new HashSet<>();
        Set<String> setters = new HashSet<>();
        Set<String> getters = new HashSet<>();

        for (PropertyAssignment each : expr.getPropertyAssignments() ) {
            if (each instanceof NamedValue) {
                if (strict) {
                    if (values.contains(each.getName())) {
                        throw new ThrowException(context, context.createSyntaxError("duplicate data properties not allowed in strict code: " + each.getName()));
                    }
                }
                if (setters.contains(each.getName()) || getters.contains(each.getName())) {
                    throw new ThrowException(context, context.createSyntaxError("data property conflicts with accessor property: " + each.getName()));
                }
                values.add(each.getName());
            }
            if (each instanceof PropertyGet) {
                if (values.contains(each.getName()) || getters.contains(each.getName())) {
                    throw new ThrowException(context, context.createSyntaxError("accessor property conflicts with accessor property: " + each.getName()));
                }
                getters.add(each.getName());
            }
            if (each instanceof PropertySet) {
                if (values.contains(each.getName()) || setters.contains(each.getName())) {
                    throw new ThrowException(context, context.createSyntaxError("accessor property conflicts with accessor property: " + each.getName()));
                }
                String identifier = ((PropertySet) each).getIdentifier();
                if (strict && ( identifier.equals("eval") || identifier.equals("arguments")) ) {
                    throw new ThrowException(context, context.createSyntaxError( identifier + " is not allowed as a parameter name in strict code" ) );
                }
                setters.add(each.getName());
            }
        }
        super.visit(context, expr, strict);
    }

    @Override
    public void visit(ExecutionContext context, PropertySet propertySet, boolean strict) {
        if (strict) {
            VerifierUtils.verifyStrictIdentifier(context, propertySet.getIdentifier() );
        }
        super.visit(context, propertySet, strict);
    }

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
        if (strict) {
            throw new ThrowException(context, context.createSyntaxError("with() not allowed within strict code"));
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
