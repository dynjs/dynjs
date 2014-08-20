package org.dynjs.parser.js;

import static org.dynjs.parser.js.TokenType.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dynjs.compiler.CompilationContext;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.AbstractBinaryExpression;
import org.dynjs.parser.ast.ArrayLiteralExpression;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.BreakStatement;
import org.dynjs.parser.ast.CaseClause;
import org.dynjs.parser.ast.CatchClause;
import org.dynjs.parser.ast.ContinueStatement;
import org.dynjs.parser.ast.DebuggerStatement;
import org.dynjs.parser.ast.DefaultCaseClause;
import org.dynjs.parser.ast.DoWhileStatement;
import org.dynjs.parser.ast.EmptyStatement;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.ExpressionStatement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.FunctionDescriptor;
import org.dynjs.parser.ast.FunctionExpression;
import org.dynjs.parser.ast.IdentifierReferenceExpression;
import org.dynjs.parser.ast.IfStatement;
import org.dynjs.parser.ast.NamedValue;
import org.dynjs.parser.ast.ObjectLiteralExpression;
import org.dynjs.parser.ast.Parameter;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.parser.ast.PropertyAssignment;
import org.dynjs.parser.ast.PropertyGet;
import org.dynjs.parser.ast.PropertySet;
import org.dynjs.parser.ast.ReturnStatement;
import org.dynjs.parser.ast.StringLiteralExpression;
import org.dynjs.parser.ast.SwitchStatement;
import org.dynjs.parser.ast.ThrowStatement;
import org.dynjs.parser.ast.TryStatement;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.parser.ast.VariableStatement;
import org.dynjs.parser.ast.WhileStatement;
import org.dynjs.parser.ast.WithStatement;
import org.dynjs.parser.js.ParserContext.ContextType;
import org.dynjs.runtime.ExecutionContext;

public class Parser {

    private CompilationContext compilationContext;
    private ASTFactory factory;
    private TokenStream stream;
    private List<ParserContext> context = new ArrayList<ParserContext>();

    private int parens = 0;

    private boolean forceStrict;

    public Parser(CompilationContext compilationContext, ASTFactory factory, TokenStream stream) {
        this.compilationContext = compilationContext;
        this.factory = factory;
        this.stream = stream;
    }

    public void forceStrict(boolean forceStrict) {
        this.forceStrict = forceStrict;
    }

    public boolean isForceStrict() {
        return this.forceStrict;
    }

    protected TokenType la() {
        return this.stream.peek();
    }

    protected TokenType la(int pos) {
        return this.stream.peek(pos);
    }

    protected Token laToken() {
        return this.stream.peekToken();
    }

    protected Token laToken(int pos) {
        return this.stream.peekToken(pos);
    }

    protected Token laToken(boolean skipSkippable) {
        return this.stream.peekToken(skipSkippable);
    }

    protected Token consume() {
        return this.stream.consume();
    }

    protected Token consume(TokenType type) {
        Token t = consume();
        if (t.getType() != type) {
            throw new SyntaxError(t, "expected token " + type + " but was '" + t.getText() + "'");
        }
        return t;
    }

    protected TokenType la(boolean skipSkippable) {
        return this.stream.peek(skipSkippable);
    }

    protected TokenType la(boolean skipSkippable, int pos) {
        return this.stream.peek(skipSkippable, pos);
    }

    protected Token consume(boolean skipSkippable) {
        return this.stream.consume(skipSkippable);
    }

    protected Token consume(boolean skipSkippable, TokenType type) {
        Token t = this.stream.consume(skipSkippable);
        if (t.getType() != type) {
            throw new SyntaxError(t, "expected token " + type + " but was '" + t.getText() + "'");
        }
        return t;
    }

    private void pushContext(ContextType type) {
        ParserContext parent = currentContext();
        ParserContext ctx = new ParserContext(parent, type);
        if (this.forceStrict || (this.context.size() > 0 && currentContext().isStrict())) {
            ctx.setStrict(true);
        }
        this.context.add(ctx);
    }

    private void popContext() {
        this.context.remove(this.context.size() - 1);
    }

    private ParserContext currentContext() {
        if (this.context.isEmpty()) {
            return null;
        }
        return this.context.get(this.context.size() - 1);
    }

    private boolean isValidReturn() {
        return currentContext().isValidReturn();
    }

    private boolean isValidBreak(String target) {
        return currentContext().isValidBreak(target);
    }

    private boolean isValidContinue(String target) {
        return currentContext().isValidContinue(target);
    }

    private boolean isValidIdentifier(Token token) {
        if (isReservedWord(token)) {
            return false;
        }
        if (currentContext().isStrict() && isStrictFutureReservedWord(token.getText())) {
            return false;
        }
        return true;
    }

    public ProgramTree program() {
        try {
            pushContext(ContextType.PROGRAM);
            List<Statement> statements = sourceElements();
            return factory.program(statements, currentContext().isStrict());
        } finally {
            popContext();
        }
    }

    // ----------------------------------------------------------------------
    // Expressions
    // ----------------------------------------------------------------------

    public Expression primaryExpression() {
        TokenType t = la();

        Token token = null;

        switch (t) {
            case THIS:
                return factory.thisExpression(consume());
            case IDENTIFIER:
                token = consume(IDENTIFIER);
                if (!isValidIdentifier(token)) {
                    throw new SyntaxError(token, "invalid identifier: " + token.getText());
                }
                return factory.identifier(token, token.getText());
            case STRING_LITERAL:
                token = consume(STRING_LITERAL);
                if (currentContext().isStrict() && token.isEscapedOctalString()) {
                    throw new SyntaxError(token, "octal escapes not allowed in strict mode");
                }
                return factory.stringLiteral(token, token.getText(), token.isEscapedString(), token.isContinuedLine());
            case DECIMAL_LITERAL:
                token = consume(DECIMAL_LITERAL);
                return factory.decimalLiteral(token, token.getText());
            case HEX_LITERAL:
                token = consume(HEX_LITERAL);
                return factory.hexLiteral(token, token.getText());
            case OCTAL_LITERAL:
                token = consume(OCTAL_LITERAL);
                if (currentContext().isStrict()) {
                    throw new SyntaxError(token, "octal literals not allowed in strict mode");
                }
                return factory.octalLiteral(token, token.getText());
            case REGEXP_LITERAL:
                token = consume(REGEXP_LITERAL);
                return factory.regexpLiteral(token, token.getText());
            case TRUE:
                return factory.trueLiteral(consume());
            case FALSE:
                return factory.falseLiteral(consume());
            case NULL:
                return factory.nullLiteral(consume());
            case LEFT_BRACE:
                return objectLiteral();
            case LEFT_BRACKET:
                return arrayLiteral();
            case LEFT_PAREN:
                consume(LEFT_PAREN);
                try {
                    ++this.parens;
                    Expression expr = expression();
                    consume(RIGHT_PAREN);
                    return expr;
                } finally {
                    --this.parens;
                }
        }

        throw new SyntaxError(laToken(), "unexpected token: " + laToken().getText());
    }

    public Expression expression() {
        return expression(false);
    }

    public Expression expressionNoIn() {
        return expression(true);
    }

    public Expression expression(boolean noIn) {
        Expression expr = assignmentExpression(noIn);

        if (la() == COMMA) {
            consume();
            expr = factory.commaOperator(expr, expression(noIn));
        }
        return expr;
    }

    public ObjectLiteralExpression objectLiteral() {
        Token position = consume(LEFT_BRACE);

        List<PropertyAssignment> propAssignments = new ArrayList<>();

        Set<String> values = new HashSet<>();
        Set<String> getters = new HashSet<>();
        Set<String> setters = new HashSet<>();

        while (true) {

            if (la() == RIGHT_BRACE) {
                break;
            }

            PropertyAssignment assignment = null;
            Token token = laToken();

            switch (token.getText()) {
                case "get":
                    if (la(2) != COLON) {
                        assignment = propertyGet();
                        propAssignments.add(assignment);
                        if (values.contains(assignment.getName()) || getters.contains(assignment.getName())) {
                            throw new SyntaxError(token, "duplicate property not allowed: " + assignment.getName());
                        }
                        getters.add(assignment.getName());
                        break;
                    }
                case "set":
                    if (la(2) != COLON) {
                        assignment = propertySet();
                        propAssignments.add(assignment);
                        if (values.contains(assignment.getName()) || setters.contains(assignment.getName())) {
                            throw new SyntaxError(token, "duplicate property not allowed: " + assignment.getName());
                        }
                        setters.add(assignment.getName());
                        break;
                    }
                default:
                    assignment = namedValue();
                    propAssignments.add(assignment);
                    if (getters.contains(assignment.getName()) || setters.contains(assignment.getName())) {
                        throw new SyntaxError(token, "duplicate property not allowed: " + assignment.getName());
                    }
                    if (currentContext().isStrict()) {
                        if (values.contains(assignment.getName())) {
                            throw new SyntaxError(token, "duplicate property not allowed: " + assignment.getName());
                        }
                    }
                    values.add(assignment.getName());
            }

            if (la() != COMMA) {
                break;
            }
            consume(COMMA);
        }
        consume(RIGHT_BRACE);

        return factory.objectLiteral(position, propAssignments);
    }

    protected PropertySet propertySet() {
        try {
            pushContext(ContextType.FUNCTION);
            Token set = consume(IDENTIFIER);
            if (!set.getText().equals("set")) {
                throw new SyntaxError("expected 'set', was: " + set);
            }

            Token token = laToken();
            if (!isPropertyName(token)) {
                throw new SyntaxError(token, "expected property identifier");
            }

            String name = consume().getText();
            consume(LEFT_PAREN);

            Token param = consume(IDENTIFIER);
            if (!isAssignableName(param.getText())) {
                throw new SyntaxError("invalid parameter name: " + param.getText());
            }
            consume(RIGHT_PAREN);
            Token position = consume(LEFT_BRACE);
            BlockStatement body = functionBody();
            consume(RIGHT_BRACE);

            return new PropertySet(position, name, param.getText(), body);
        } finally {
            popContext();
        }
    }

    protected PropertyGet propertyGet() {
        try {
            pushContext(ContextType.FUNCTION);
            Token get = consume(IDENTIFIER);
            if (!get.getText().equals("get")) {
                throw new SyntaxError("expected 'get', was: " + get);
            }

            Token token = laToken();
            if (!isPropertyName(token)) {
                throw new SyntaxError(token, "expected property identifier");
            }
            String name = consume().getText();
            consume(LEFT_PAREN);
            consume(RIGHT_PAREN);
            Token position = consume(LEFT_BRACE);
            BlockStatement body = functionBody();
            consume(RIGHT_BRACE);

            return new PropertyGet(position, name, body);
        } finally {
            popContext();
        }
    }

    protected NamedValue namedValue() {
        Token token = laToken();
        if (!isPropertyName(token)) {
            throw new SyntaxError(token, "expected property identifier");
        }
        String name = consume().getText();
        consume(COLON);
        Expression expr = assignmentExpression();

        return new NamedValue(token, name, expr);
    }

    protected boolean isPropertyName(Token token) {
        switch (token.getType()) {
            case IDENTIFIER:
            case DECIMAL_LITERAL:
            case HEX_LITERAL:
            case STRING_LITERAL:
                return true;
            default:
                return isReservedWord(token);
        }
    }

    protected boolean isAssignableName(String name) {
        if (isFutureReservedWord(name)) {
            return false;
        }

        if (currentContext().isStrict()) {
            if (isStrictFutureReservedWord(name) || name.equals("arguments") || name.equals("eval")) {
                return false;
            }
        }

        return true;
    }

    protected boolean isReservedWord(Token token) {
        return isKeyword(token.getType()) || isFutureReservedWord(token.getText());
    }

    protected boolean isKeyword(TokenType type) {
        switch (type) {
            case BREAK:
            case DO:
            case INSTANCEOF:
            case TYPEOF:
            case CASE:
            case ELSE:
            case NEW:
            case VAR:
            case CATCH:
            case FINALLY:
            case RETURN:
            case VOID:
            case CONTINUE:
            case FOR:
            case SWITCH:
            case WHILE:
            case DEBUGGER:
            case FUNCTION:
            case THIS:
            case WITH:
            case DEFAULT:
            case IF:
            case THROW:
            case DELETE:
            case IN:
            case OF:
            case TRY:
            case NULL:
            case TRUE:
            case FALSE:
                return true;
        }

        return false;
    }

    protected boolean isFutureReservedWord(String name) {
        switch (name) {
            case "class":
            case "enum":
            case "extends":
            case "super":
            case "const":
            case "export":
            case "import":
                return true;
        }
        return false;
    }

    protected boolean isStrictFutureReservedWord(String name) {
        switch (name) {
            case "implements":
            case "let":
            case "private":
            case "public":
            case "yield":
            case "interface":
            case "package":
            case "protected":
            case "static":
                return true;
        }
        return false;

    }

    public ArrayLiteralExpression arrayLiteral() {
        Token start = consume(LEFT_BRACKET);
        List<Expression> exprs = new ArrayList<>();

        while (la() != RIGHT_BRACKET) {
            if (la() == COMMA) {
                // System.err.println("comma");
                consume();
                exprs.add(null);
                continue;
            } else {
                // System.err.println("not comma");
                exprs.add(assignmentExpression());
                if (la() == COMMA) {
                    // System.err.println("followed by comma");
                    consume();
                } else {
                    // System.err.println("followed by not comma");
                    break;
                }
            }
        }

        consume(RIGHT_BRACKET);

        // System.err.println("ARRAY: " + exprs);

        return factory.arrayLiteral(start, exprs);
    }

    public Expression assignmentExpression() {
        return assignmentExpression(false);
    }

    public Expression assignmentExpressionNoIn() {
        return assignmentExpression(true);
    }

    public Expression assignmentExpression(boolean noIn) {
        Expression expr = conditionalExpression(noIn);

        switch (la()) {
            case EQUALS:
                consume(EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.assignmentOperator(expr, assignmentExpression(noIn));
                break;
            case MULTIPLY_EQUALS:
                consume(MULTIPLY_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.multiplicationAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case DIVIDE_EQUALS:
                consume(DIVIDE_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.divisionAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case MODULO_EQUALS:
                consume(MODULO_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.moduloAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case PLUS_EQUALS:
                consume(PLUS_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.additionAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case MINUS_EQUALS:
                consume(MINUS_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.subtractionAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case LEFT_SHIFT_EQUALS:
                consume(LEFT_SHIFT_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.leftShiftAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case RIGHT_SHIFT_EQUALS:
                consume(RIGHT_SHIFT_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.rightShiftAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case UNSIGNED_RIGHT_SHIFT_EQUALS:
                consume(UNSIGNED_RIGHT_SHIFT_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.unsignedRightShiftAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case BITWISE_AND_EQUALS:
                consume(BITWISE_AND_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.bitwiseAndAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case BITWISE_OR_EQUALS:
                consume(BITWISE_OR_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.bitwiseOrAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            case BITWISE_XOR_EQUALS:
                consume(BITWISE_XOR_EQUALS);
                checkAssignmentLHS(expr);
                expr = factory.bitwiseXorAssignmentOperator(expr, assignmentExpression(noIn));
                break;
            default:
                return expr;
        }

        if (currentContext().isStrict()) {
            if (expr instanceof AbstractBinaryExpression) {
                Expression lhs = ((AbstractBinaryExpression) expr).getLhs();
                if (lhs instanceof IdentifierReferenceExpression) {
                    String ident = ((IdentifierReferenceExpression) lhs).getIdentifier();
                    if (!isAssignableName(ident)) {
                        throw new SyntaxError(expr.getPosition(), "invalid identifier for strict-mode: " + ident);
                    }
                }
            }
        }

        return expr;
    }

    protected void checkAssignmentLHS(Expression expr) {
        if (currentContext().isStrict()) {
            if (expr instanceof IdentifierReferenceExpression) {
                String id = ((IdentifierReferenceExpression) expr).getIdentifier();
                if (id.equals("eval") || id.equals("arguments")) {
                    throw new ThrowException(null, this.compilationContext.createSyntaxError(id
                            + " may not appear on the left-hand-side of a compound assignment expression in strict mode"));
                }
            }
        }
    }

    public Expression conditionalExpression(boolean noIn) {
        Expression expr = logicalOrExpression(noIn);

        if (la() == QUESTION) {
            consume(QUESTION);
            Expression thenExpr = assignmentExpression(noIn);
            consume(COLON);
            Expression elseExpr = assignmentExpression(noIn);

            return factory.ternaryOperator(expr, thenExpr, elseExpr);
        }

        return expr;
    }

    public Expression logicalOrExpression(boolean noIn) {
        Expression expr = logicalAndExpression(noIn);

        while (la() == LOGICAL_OR) {
            consume(LOGICAL_OR);
            expr = factory.logicalOrOperator(expr, logicalAndExpression(noIn));
        }

        return expr;
    }

    public Expression logicalAndExpression(boolean noIn) {
        Expression expr = bitwiseOrExpression(noIn);

        while (la() == LOGICAL_AND) {
            consume(LOGICAL_AND);
            expr = factory.logicalAndOperator(expr, bitwiseOrExpression(noIn));
        }

        return expr;
    }

    public Expression bitwiseOrExpression(boolean noIn) {
        Expression expr = bitwiseXorExpression(noIn);

        while (la() == BITWISE_OR) {
            consume(BITWISE_OR);
            expr = factory.bitwiseOrOperator(expr, bitwiseXorExpression(noIn));
        }

        return expr;
    }

    public Expression bitwiseXorExpression(boolean noIn) {
        Expression expr = bitwiseAndExpression(noIn);

        while (la() == BITWISE_XOR) {
            consume(BITWISE_XOR);
            expr = factory.bitwiseXorOperator(expr, bitwiseAndExpression(noIn));
        }

        return expr;
    }

    public Expression bitwiseAndExpression(boolean noIn) {
        Expression expr = equalityExpression(noIn);

        while (la() == BITWISE_AND) {
            consume(BITWISE_AND);
            expr = factory.bitwiseAndOperator(expr, equalityExpression(noIn));
        }

        return expr;
    }

    public Expression equalityExpression(boolean noIn) {
        Expression expr = relationalExpression(noIn);

        while (la() == EQUALITY || la() == NOT_EQUALITY || la() == STRICT_EQUALITY || la() == STRICT_NOT_EQUALITY) {
            switch (la()) {
                case EQUALITY:
                    consume();
                    expr = factory.equalityOperator(expr, relationalExpression(noIn));
                    break;
                case NOT_EQUALITY:
                    consume();
                    expr = factory.notEqualityOperator(expr, relationalExpression(noIn));
                    break;
                case STRICT_EQUALITY:
                    consume();
                    expr = factory.strictEqualityOperator(expr, relationalExpression(noIn));
                    break;
                case STRICT_NOT_EQUALITY:
                    consume();
                    expr = factory.strictNotEqualityOperator(expr, relationalExpression(noIn));
                    break;
            }
        }

        return expr;
    }

    public Expression relationalExpression(boolean noIn) {
        Expression expr = shiftExpression();

        while (la() == LESS_THAN || la() == LESS_THAN_EQUAL || la() == GREATER_THAN || la() == GREATER_THAN_EQUAL || la() == INSTANCEOF || (!noIn && (la() == IN || la() == OF))) {
            switch (la()) {
                case LESS_THAN:
                    consume();
                    expr = factory.lessThanOperator(expr, shiftExpression());
                    break;
                case LESS_THAN_EQUAL:
                    consume();
                    expr = factory.lessThanEqualOperator(expr, shiftExpression());
                    break;
                case GREATER_THAN:
                    consume();
                    expr = factory.greaterThanOperator(expr, shiftExpression());
                    break;
                case GREATER_THAN_EQUAL:
                    consume();
                    expr = factory.greaterThanEqualOperator(expr, shiftExpression());
                    break;
                case INSTANCEOF:
                    consume();
                    expr = factory.instanceofOperator(expr, shiftExpression());
                    break;
                case IN:
                    if (!noIn) {
                        consume();
                        expr = factory.inOperator(expr, shiftExpression());
                        break;
                    }
            }
        }

        return expr;
    }

    public Expression shiftExpression() {
        Expression expr = additiveExpression();

        while (la() == LEFT_SHIFT || la() == RIGHT_SHIFT || la() == UNSIGNED_RIGHT_SHIFT) {
            switch (la()) {
                case LEFT_SHIFT:
                    consume();
                    expr = factory.leftShiftOperator(expr, additiveExpression());
                    break;
                case RIGHT_SHIFT:
                    consume();
                    expr = factory.rightShiftOperator(expr, additiveExpression());
                    break;
                case UNSIGNED_RIGHT_SHIFT:
                    consume();
                    expr = factory.unsignedRightShiftOperator(expr, additiveExpression());
                    break;
            }
        }

        return expr;
    }

    public Expression additiveExpression() {
        Expression expr = multiplicativeExpression();

        while (la() == PLUS || la() == MINUS) {
            switch (la()) {
                case PLUS:
                    consume();
                    expr = factory.additionOperator(expr, multiplicativeExpression());
                    break;
                case MINUS:
                    consume();
                    expr = factory.subtractionOperator(expr, multiplicativeExpression());
                    break;
            }
        }

        return expr;
    }

    public Expression multiplicativeExpression() {
        Expression expr = unaryExpression();

        while (la() == MULTIPLY || la() == DIVIDE || la() == MODULO) {
            switch (la()) {
                case MULTIPLY:
                    consume();
                    expr = factory.multiplicationOperator(expr, unaryExpression());
                    break;
                case DIVIDE:
                    consume();
                    expr = factory.divisionOperator(expr, unaryExpression());
                    break;
                case MODULO:
                    consume();
                    expr = factory.moduloOperator(expr, unaryExpression());
                    break;
            }
        }

        return expr;
    }

    public Expression unaryExpression() {
        Expression expr = null;
        switch (la()) {
            case DELETE:
                consume();
                expr = unaryExpression();
                if (currentContext().isStrict()) {
                    if (expr instanceof IdentifierReferenceExpression) {
                        throw new ThrowException(null, this.compilationContext.createSyntaxError("cannot delete a direct reference in strict-mode"));
                    }
                }
                return factory.deleteOperator(expr);
            case VOID:
                consume();
                return factory.voidOperator(unaryExpression());
            case TYPEOF:
                consume();
                return factory.typeofOperator(unaryExpression());
            case PLUS_PLUS:
                consume();
                expr = unaryExpression();
                checkAssignmentLHS(expr);
                return factory.preIncrementOperator(expr);
            case MINUS_MINUS:
                consume();
                expr = unaryExpression();
                checkAssignmentLHS(expr);
                return factory.preDecrementOperator(expr);
            case PLUS:
                consume();
                return factory.unaryPlusOperator(unaryExpression());
            case MINUS:
                consume();
                return factory.unaryMinusOperator(unaryExpression());
            case INVERSION:
                consume();
                return factory.bitwiseInversionOperator(unaryExpression());
            case NOT:
                consume();
                return factory.unaryNotOperator(unaryExpression());
            default:
                return postfixExpression();
        }
    }

    public Expression postfixExpression() {
        Expression expr = leftHandSideExpression();

        switch (la(false)) {
            case PLUS_PLUS:
                checkAssignmentLHS(expr);
                consume(false);
                return factory.postIncrementOperator(expr);
            case MINUS_MINUS:
                checkAssignmentLHS(expr);
                consume(false);
                return factory.postDecrementOperator(expr);
        }

        return expr;
    }

    public Expression leftHandSideExpression() {
        if (la() == NEW) {
            return newExpression();
        } else {
            return callExpression();
        }
    }

    public Expression memberExpression() {
        Expression expr = null;
        if (la() == FUNCTION) {
            expr = functionExpression();
        } else {
            expr = primaryExpression();
        }

        loop:
        while (true) {
            switch (la()) {
                case DOT:
                    consume(DOT);
                    Token token = laToken();
                    if (!isPropertyName(token)) {
                        throw new SyntaxError(token, "expected property name");
                    }
                    String identifier = consume().getText();
                    expr = factory.dotOperator(expr, identifier);
                    break;
                case LEFT_BRACKET:
                    consume(LEFT_BRACKET);
                    expr = factory.bracketOperator(expr, expression());
                    consume(RIGHT_BRACKET);
                    break;
                default:
                    break loop;
            }
        }

        return expr;
    }

    public Expression callExpression() {
        Expression expr = memberExpression();

        loop:
        while (true) {
            switch (la()) {
                case DOT:
                    consume(DOT);
                    String identifier = consume().getText();
                    expr = factory.dotOperator(expr, identifier);
                    break;
                case LEFT_BRACKET:
                    consume(LEFT_BRACKET);
                    expr = factory.bracketOperator(expr, expression());
                    consume(RIGHT_BRACKET);
                    break;
                case LEFT_PAREN:
                    List<Expression> args = arguments();
                    expr = factory.functionCall(expr, args);
                    break;
                default:
                    break loop;
            }
        }

        return expr;
    }

    public Expression newExpression() {
        consume(NEW);
        Expression expr = null;
        if (la() == NEW) {
            expr = factory.newOperator(newExpression());
        } else {
            expr = memberExpression();
            if (la() == LEFT_PAREN) {
                List<Expression> args = arguments();
                expr = factory.newOperator(expr, args);
            } else {
                expr = factory.newOperator(expr);
            }
        }

        loop:
        while (true) {
            switch (la()) {
                case DOT:
                    consume(DOT);
                    String identifier = consume(IDENTIFIER).getText();
                    expr = factory.dotOperator(expr, identifier);
                    break;
                case LEFT_BRACKET:
                    consume(LEFT_BRACKET);
                    expr = factory.bracketOperator(expr, expression());
                    consume(RIGHT_BRACKET);
                    break;
                case LEFT_PAREN:
                    List<Expression> args = arguments();
                    expr = factory.functionCall(expr, args);
                    break;
                default:
                    break loop;
            }
        }

        return expr;
    }

    public FunctionExpression functionExpression() {
        try {
            pushContext(ContextType.FUNCTION);
            Token position = consume(FUNCTION);
            Token identifier = null;
            String identifierName = null;
            if (la() == IDENTIFIER) {
                identifier = consume(IDENTIFIER);
                if (!isAssignableName(identifier.getText())) {
                    throw new SyntaxError(identifier, "invalid identifier: " + identifier.getText());
                }
                identifierName = identifier.getText();
            }

            List<Parameter> params = formalParameters();

            consume(LEFT_BRACE);
            BlockStatement body = functionBody();
            consume(RIGHT_BRACE);

            return factory.functionExpression(position, identifierName, params, body, currentContext().isStrict());
        } finally {
            popContext();
        }
    }

    public FunctionDeclaration functionDeclaration() {
        try {
            pushContext(ContextType.FUNCTION);
            Token position = consume(FUNCTION);
            Token identifier = consume(IDENTIFIER);
            if (!isAssignableName(identifier.getText())) {
                throw new SyntaxError(identifier, "invalid identifier: " + identifier.getText());
            }

            List<Parameter> params = formalParameters();

            consume(LEFT_BRACE);
            BlockStatement body = functionBody();
            consume(RIGHT_BRACE);

            return factory.functionDeclaration(position, identifier.getText(), params, body, currentContext().isStrict());
        } finally {
            popContext();
        }
    }

    public FunctionDescriptor functionDescriptor() {
        try {
            pushContext(ContextType.FUNCTION);
            Token position = consume(FUNCTION);
            String identifier = null;
            if (la() == IDENTIFIER) {
                identifier = consume(IDENTIFIER).getText();
            }
            List<Parameter> params = formalParameters();

            consume(LEFT_BRACE);
            BlockStatement body = functionBody();
            consume(RIGHT_BRACE);

            return new FunctionDescriptor(position, identifier, params, body, currentContext().isStrict());
        } finally {
            popContext();
        }

    }

    public BlockStatement functionBody() {
        List<Statement> statements = sourceElements();
        return factory.block(statements);
    }

    public List<Parameter> formalParameters() {
        consume(LEFT_PAREN);
        List<Parameter> params = formalParameterList();
        consume(RIGHT_PAREN);
        return params;
    }

    public List<Parameter> formalParameterList() {
        List<Parameter> params = new ArrayList<>();

        while (la() != RIGHT_PAREN) {
            params.add(parameter());
            if (la() == COMMA) {
                consume();
            } else {
                break;
            }
        }

        if (currentContext().isStrict()) {
            Set<String> seen = new HashSet<>();
            for (Parameter each : params) {
                if (seen.contains(each.getIdentifier())) {
                    throw new SyntaxError(each.getPosition(), "duplicate formal parameters not allowed in strict-mode");
                }
                seen.add(each.getIdentifier());
            }
        }

        return params;
    }

    public Parameter parameter() {
        Token identifier = consume(IDENTIFIER);
        if (!isAssignableName(identifier.getText())) {
            throw new SyntaxError(identifier, "invalid formal parameter:" + identifier.getText());
        }
        return factory.parameter(identifier, identifier.getText());
    }

    public List<Expression> arguments() {
        consume(LEFT_PAREN);
        List<Expression> args = argumentList();
        consume(RIGHT_PAREN);
        return args;
    }

    public List<Expression> argumentList() {
        List<Expression> arguments = new ArrayList<>();
        while (la() != RIGHT_PAREN) {
            arguments.add(assignmentExpression());

            if (la() == COMMA) {
                consume();
            } else {
                break;
            }
        }

        return arguments;
    }

    // ----------------------------------------------------------------------
    // Statements
    // ----------------------------------------------------------------------

    public List<Statement> sourceElements() {
        List<Statement> statements = new ArrayList<>();
        while (la() != EOF && la() != RIGHT_BRACE) {
            Statement element = sourceElement();
            if (currentContext().isInProlog()) {
                if (element instanceof ExpressionStatement) {
                    Expression expr = ((ExpressionStatement) element).getExpr();
                    if (expr instanceof StringLiteralExpression) {
                        if (((StringLiteralExpression) expr).getLiteral().equals("use strict")) {
                            if (!((StringLiteralExpression) expr).isContinuedLine() && !((StringLiteralExpression) expr).isEscaped()) {
                                currentContext().setStrict(true);
                            }
                        }
                    } else {
                        currentContext().setInProlog(false);
                    }
                } else {
                    currentContext().setInProlog(false);
                }
            }
            statements.add(element);
        }
        return statements;
    }

    public Statement sourceElement() {
        if (la() == FUNCTION) {
            return functionDeclaration();
        }

        return statement();
    }

    public Statement statement() {
        switch (la()) {
            case LEFT_BRACE:
                return block();
            case VAR:
                return variableStatement();
            case SEMICOLON:
                return emptyStatement();
            case IF:
                return ifStatement();
            case DO:
            case FOR:
            case WHILE:
                return iterationStatement();
            case CONTINUE:
                return continueStatement();
            case BREAK:
                return breakStatement();
            case RETURN:
                return returnStatement();
            case WITH:
                return withStatement();
            case SWITCH:
                return switchStatement();
            case THROW:
                return throwStatement();
            case TRY:
                return tryStatement();
            case DEBUGGER:
                return debuggerStatement();
            case FUNCTION:
                if (currentContext().isStrict()) {
                    throw new SyntaxError("function declaration in statement not allowed in strict-mode code");
                } else {
                    return functionDeclaration();
                }
        }

        if (la(2) == COLON) {
            return labelledStatement();
        }

        return expressionStatement();
    }

    protected void semic() {
        if (la() == EOF) {
            return;
        } else if (la() == RIGHT_BRACE) {
            return;
        } else if (la() == SEMICOLON) {
            consume(SEMICOLON);
        } else {
            Token next = consume(false);
            switch (next.getType()) {
                case CRNL:
                case CR:
                case NL:
                case PARAGRAPH_SEPARATOR:
                case LINE_SEPARATOR:
                    break;
                default:
                    throw new SyntaxError(next, "semicolon expected but saw: " + next);
            }
        }
    }

    public BlockStatement block() {
        try {
            pushContext(ContextType.OTHER);
            consume(LEFT_BRACE);
            List<Statement> statements = new ArrayList<Statement>();

            while (la() != RIGHT_BRACE) {
                statements.add(statement());
            }

            consume(RIGHT_BRACE);

            return factory.block(statements);
        } finally {
            popContext();
        }
    }

    public VariableStatement variableStatement() {
        return variableStatement(false);
    }

    public VariableStatement variableStatementNoIn() {
        return variableStatement(true);
    }

    public VariableStatement variableStatement(boolean noIn) {
        Token position = consume(VAR);
        List<VariableDeclaration> decls = variableDeclarationList(noIn);

        semic();

        return factory.variableStatement(position, decls);
    }

    protected List<VariableDeclaration> variableDeclarationList(boolean noIn) {
        List<VariableDeclaration> decls = new ArrayList<>();

        decls.add(variableDeclaration(noIn));

        while (la() == COMMA) {
            consume(COMMA);
            decls.add(variableDeclaration(noIn));
        }

        return decls;
    }

    public VariableDeclaration variableDeclaration(boolean noIn) {
        Token identifier = consume(IDENTIFIER);

        if (!isValidIdentifier(identifier) || !isAssignableName(identifier.getText())) {
            throw new SyntaxError(identifier, "invalid identifier: " + identifier.getText());
        }

        Expression initializer = null;

        if (la() == EQUALS) {
            consume(EQUALS);
            initializer = assignmentExpression(noIn);
        }

        return factory.variableDeclaration(identifier, identifier.getText(), initializer);
    }

    public EmptyStatement emptyStatement() {
        Token position = consume(SEMICOLON);

        return factory.emptyStatement(position);
    }

    public ExpressionStatement expressionStatement() {
        Expression expr = expression();

        semic();

        return factory.expressionStatement(expr);
    }

    public IfStatement ifStatement() {
        Token position = consume(IF);

        consume(LEFT_PAREN);
        Expression testExpr = expression();
        consume(RIGHT_PAREN);

        Statement body = statement();

        if (la() == ELSE) {
            consume(ELSE);
            Statement elseBody = statement();
            return factory.ifStatement(position, testExpr, body, elseBody);
        }
        return factory.ifStatement(position, testExpr, body);
    }

    public ContinueStatement continueStatement() {
        Token position = consume(CONTINUE);

        String target = null;

        switch (la(false)) {
            case CR:
            case NL:
            case CRNL:
            case PARAGRAPH_SEPARATOR:
            case LINE_SEPARATOR:
            case SEMICOLON:
            case RIGHT_BRACE:
                break;
            case IDENTIFIER:
                target = consume(IDENTIFIER).getText();
                break;
            default:
                throw new SyntaxError(laToken(false), "unexpected token: " + laToken(false).getText());
        }

        if (!isValidContinue(target)) {
            if (target == null) {
                throw new SyntaxError(position, "continue not allowed");
            }
            throw new SyntaxError(position, "continue  " + target + " not allowed");
        }
        semic();
        return factory.continueStatement(position, target);
    }

    public BreakStatement breakStatement() {
        Token position = consume(BREAK);

        String target = null;

        switch (la(false)) {
            case CR:
            case NL:
            case CRNL:
            case PARAGRAPH_SEPARATOR:
            case LINE_SEPARATOR:
            case SEMICOLON:
            case RIGHT_BRACE:
                break;
            case IDENTIFIER:
                target = consume(IDENTIFIER).getText();
                break;
            default:
                throw new SyntaxError(laToken(false), "unexpected token: " + laToken(false).getText());
        }

        if (!isValidBreak(target)) {
            if (target == null) {
                throw new SyntaxError(position, "break not allowed");
            }
            throw new SyntaxError(position, "break " + target + " not allowed");
        }
        semic();
        return factory.breakStatement(position, target);
    }

    public ReturnStatement returnStatement() {
        Token position = consume(RETURN);

        if (!isValidReturn()) {
            throw new SyntaxError(position, "return not allowed");
        }

        Expression expr = null;

        switch (la(false)) {
            case SEMICOLON:
                consume( SEMICOLON );
            case CR:
            case NL:
            case CRNL:
            case PARAGRAPH_SEPARATOR:
            case LINE_SEPARATOR:
            case RIGHT_BRACE:
                return factory.returnStatement(position);
            default:
                expr = expression();
        }

        ReturnStatement statement = factory.returnStatement(position, expr);
        semic();
        return statement;
    }

    public ThrowStatement throwStatement() {
        Token position = consume(THROW);

        switch (la(false)) {
            case NL:
            case CR:
            case CRNL:
            case LINE_SEPARATOR:
            case PARAGRAPH_SEPARATOR:
            case SEMICOLON:
                throw new SyntaxError("unexpected line terminator after 'throw'");
        }

        ThrowStatement statement = factory.throwStatement(position, expression());
        semic();
        return statement;
    }

    public WithStatement withStatement() {
        if (currentContext().isStrict()) {
            throw new SyntaxError("with() statement not allowed in strict-mode code");
        }

        Token position = consume(WITH);

        consume(LEFT_PAREN);
        Expression expr = expression();
        consume(RIGHT_PAREN);

        Statement body = statement();

        return factory.withStatement(position, expr, body);
    }

    public SwitchStatement switchStatement() {
        try {
            pushContext(ContextType.SWITCH);
            Token position = consume(SWITCH);

            consume(LEFT_PAREN);
            Expression expr = expression();
            consume(RIGHT_PAREN);

            consume(LEFT_BRACE);

            List<CaseClause> clauses = new ArrayList<>();

            while (la() == CASE) {
                clauses.add(caseClause());
            }

            if (la() == DEFAULT) {
                clauses.add(defaultClause());
            }

            while (la() == CASE) {
                clauses.add(caseClause());
            }

            consume(RIGHT_BRACE);

            return factory.switchStatement(position, expr, clauses);
        } finally {
            popContext();
        }
    }

    protected CaseClause caseClause() {
        Token position = consume(CASE);
        Expression expr = expression();
        consume(COLON);

        List<Statement> statements = new ArrayList<>();

        loop:
        while (true) {
            TokenType t = la();
            switch (t) {
                case RIGHT_BRACE:
                case CASE:
                case DEFAULT:
                    break loop;
            }
            statements.add(statement());
        }

        return factory.caseClause(position, expr, statements);
    }

    protected DefaultCaseClause defaultClause() {
        Token position = consume(DEFAULT);
        consume(COLON);

        List<Statement> statements = new ArrayList<>();

        loop:
        while (true) {
            TokenType t = la();
            switch (t) {
                case RIGHT_BRACE:
                case CASE:
                case DEFAULT:
                    break loop;
            }
            statements.add(statement());
        }

        return factory.defaultClause(position, statements);
    }

    public TryStatement tryStatement() {
        Token position = consume(TRY);
        BlockStatement block = block();
        CatchClause catchClause = null;
        BlockStatement finallyClause = null;

        if (la() == CATCH) {
            catchClause = catchClause();
        }

        if (la() == FINALLY) {
            finallyClause = finallyClause();
        }

        return factory.tryStatement(position, block, catchClause, finallyClause);
    }

    protected CatchClause catchClause() {
        Token position = consume(CATCH);
        consume(LEFT_PAREN);
        String ident = consume(IDENTIFIER).getText();

        if (!isAssignableName(ident)) {
            throw new SyntaxError(position, "invalid identifier: " + ident);
        }

        consume(RIGHT_PAREN);

        BlockStatement block = block();

        return factory.catchClause(position, ident, block);
    }

    protected BlockStatement finallyClause() {
        consume(FINALLY);
        return block();
    }

    public Statement labelledStatement() {
        String label = consume(IDENTIFIER).getText();
        try {
            currentContext().addLabel(label);
            consume(COLON);
            Statement statement = statement();
            statement.addLabel(label);
            return statement;
        } finally {
            currentContext().removeLabel(label);
        }
    }

    public DebuggerStatement debuggerStatement() {
        Token position = consume(DEBUGGER);
        semic();
        return factory.debuggerStatement(position);
    }

    public Statement iterationStatement() {
        try {
            pushContext(ContextType.ITERATION);

            switch (la()) {
                case DO:
                    return doWhileStatement();
                case WHILE:
                    return whileStatement();
                case FOR:
                    return forStatement();
            }

            throw new SyntaxError("unexpected token: " + laToken().getText());
        } finally {
            popContext();
        }
    }

    public DoWhileStatement doWhileStatement() {
        try {
            pushContext(ContextType.ITERATION);
            Token position = consume(DO);
            Statement body = statement();
            consume(WHILE);
            consume(LEFT_PAREN);
            Expression expr = expression();
            consume(RIGHT_PAREN);

            // don't call semic() here because the semicolon is optional
            if (la() == SEMICOLON) {
                consume(SEMICOLON);
            }
            return factory.doWhileStatement(position, body, expr);
        } finally {
            popContext();
        }
    }

    public WhileStatement whileStatement() {
        Token position = consume(WHILE);
        consume(LEFT_PAREN);
        Expression expr = expression();
        consume(RIGHT_PAREN);
        Statement body = statement();

        return factory.whileStatement(position, expr, body);
    }

    public Statement forStatement() {
        Token position = consume(FOR);
        consume(LEFT_PAREN);
        if (la() == VAR) {
            consume(VAR);
            List<VariableDeclaration> varDeclList = variableDeclarationList(true);
            if (la() == IN) {
                if (varDeclList.size() != 1) {
                    throw new SyntaxError(varDeclList.get(1).getPosition(), "only one variable declaration is allowed");
                }
                // for ( var-decl in ... )
                consume(IN);
                Expression rhs = expression();
                consume(RIGHT_PAREN);
                Statement body = statement();

                return factory.forInStatement(position, varDeclList.get(0), rhs, body);
            } else if (la() == OF) {
                if (varDeclList.size() != 1) {
                    throw new SyntaxError(varDeclList.get(1).getPosition(), "only one variable declaration is allowed");
                }
                // for ( var-decl of ... )
                consume(OF);
                Expression rhs = expression();
                consume(RIGHT_PAREN);
                Statement body = statement();

                return factory.forOfStatement(position, varDeclList.get(0), rhs, body);
            } else {
                // for ( var-decl-list ; ... ; ... )
                consume(SEMICOLON);
                Expression middle = null;
                Expression rhs = null;
                if (la() != SEMICOLON) {
                    middle = expression();
                }
                consume(SEMICOLON);
                if (la() != RIGHT_PAREN) {
                    rhs = expression();
                }
                consume(RIGHT_PAREN);
                Statement body = statement();
                return factory.forStatement(position, varDeclList, middle, rhs, body);
            }
        } else {
            Expression initializer = null;
            if (la() == SEMICOLON) {
                // for ( ;... ; ... )
                // no initializer
            } else {
                initializer = expressionNoIn();
            }

            if (la() == IN) {
                consume(IN);
                // for ( expr in expr )
                Expression rhs = expression();
                consume(RIGHT_PAREN);
                Statement body = statement();
                return factory.forInStatement(position, initializer, rhs, body);
            } else if (la() == OF) {
                consume(OF);
                // for ( expr of expr )
                Expression rhs = expression();
                consume(RIGHT_PAREN);
                Statement body = statement();
                return factory.forOfStatement(position, initializer, rhs, body);
            } else {
                // for ( ... ; ... ; ... )
                consume(SEMICOLON);
                Expression middle = null;
                Expression rhs = null;
                if (la() != SEMICOLON) {
                    middle = expression();
                }
                consume(SEMICOLON);
                if (la() != RIGHT_PAREN) {
                    rhs = expression();
                }
                consume(RIGHT_PAREN);
                Statement body = statement();
                return factory.forStatement(position, initializer, middle, rhs, body);
            }

        }
    }

}
