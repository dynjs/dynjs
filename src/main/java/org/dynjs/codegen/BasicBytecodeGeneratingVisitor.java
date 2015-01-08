package org.dynjs.codegen;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.internal.org.objectweb.asm.tree.LabelNode;
import org.dynjs.compiler.bytecode.Chunker;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.*;
import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.BuiltinArray;
import org.dynjs.runtime.builtins.types.BuiltinNumber;
import org.dynjs.runtime.builtins.types.BuiltinObject;
import org.dynjs.runtime.builtins.types.BuiltinRegExp;
import org.dynjs.runtime.builtins.types.regexp.DynRegExp;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.qmx.jitescript.util.CodegenUtils.*;

public class BasicBytecodeGeneratingVisitor extends CodeGeneratingVisitor {

    private static final String[] EMPTY_STRING_ARRAY = {};

    public BasicBytecodeGeneratingVisitor(InterpretingVisitorFactory interpFactory, BlockManager blockManager) {
        super(interpFactory, blockManager);
    }

    @Override
    public CodeBlock jsGetValue(final Class<?> throwIfNot) {
        CodeBlock codeBlock = new CodeBlock()
            // IN: reference
            .aload(Arities.EXECUTION_CONTEXT)
            // reference context
            .swap()
            // context reference
            .invokestatic(p(Types.class), "getValue", sig(Object.class, ExecutionContext.class, Object.class));
            // value
        if (throwIfNot != null) {
            LabelNode end = new LabelNode();
            codeBlock.dup()
                // value value
                .instance_of(p(throwIfNot))
                // value bool
                .iftrue(end)
                // value
                .pop()
                .append(jsThrowTypeError("expected " + throwIfNot.getName()))
                .label(end)
                .nop();
        }
        return codeBlock;
    }

    public Object visitPlus(ExecutionContext context, AdditiveExpression expr, boolean strict) {
        LabelNode doubleNums = new LabelNode();

        LabelNode stringConcatByLeft = new LabelNode();
        LabelNode stringConcat = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // ref(lhs)
        append(jsGetValue());
        // val(lhs)
        aconst_null();
        // val(lhs) null
        append(jsToPrimitive());
        // val(lhs)
        dup();
        // val(lhs) val(lhs)
        instance_of(p(String.class));
        // val(lhs) bool
        expr.getRhs().accept(context, this, strict);
        // val(lhs) bool ref(rhs)
        append(jsGetValue());
        // val(lhs) bool val(rhs)
        swap();
        // val(lhs) val(rhs) bool
        iftrue(stringConcatByLeft);

        aconst_null();
        // val(lhs) val(rhs) null
        append(jsToPrimitive());
        // val(lhs) val(rhs)
        dup();
        // val(lhs) val(rhs) val(rhs)
        instance_of(p(String.class));
        // val(lhs) val(rhs) bool
        iftrue(stringConcat);

        // ----------------------------------------
        // Numbers

        // val(lhs) val(rhs)
        append(jsToNumber());
        swap();
        append(jsToNumber());
        swap();
        // num(lhs) num(rhs)

        // Number(lhs) Number(rhs)
        append(ifEitherIsDouble(doubleNums));

        // ----------------------------------------------
        // Integral

        append(convertTopTwoToPrimitiveLongs());
        // long(lhs) long(rhs)
        ladd();
        // num(total)
        append(convertTopToLong());
        // Long(total)
        go_to(end);

        // ----------------------------------------------
        // Double

        label(doubleNums);
        // (doubles) Number(lhs) Number(rhs)
        append(convertTopTwoToPrimitiveDoubles());
        // double(lhs) double(rhs)
        dadd();
        // num(total)
        append(convertTopToDouble());
        // Double(total)
        go_to(end);

        // ----------------------------------------
        // Strings forced by LHS
        label(stringConcatByLeft);
        // val(lhs) val(rhs)
        aconst_null();
        // val(lhs) val(rhs) null
        append(jsToPrimitive());
        // val(lhs) val(rhs);

        // ----------------------------------------
        // Strings
        label(stringConcat);
        // val(lhs) val(rhs)
        append(jsToString());
        // val(lhs) str(rhs)
        swap();
        // str(rhs) val(lhs)
        append(jsToString());
        // str(lhs) str(lhs)
        swap();
        // str(lhs) str(rhs)
        invokevirtual(p(String.class), "concat", sig(String.class, String.class));
        // obj(concat)

        // ----------------------------------------
        // TODO: Arrays

        // ----------------------------------------
        label(end);
        nop();
        return null;
    }

    public Object visitMinus(ExecutionContext context, AdditiveExpression expr, boolean strict) {

        LabelNode doubleNums = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // obj(lhs)
        append(jsGetValue());
        // val(lhs)
        append(jsToNumber());
        expr.getRhs().accept(context, this, strict);
        // val(lhs) obj(rhs)
        append(jsGetValue());
        // val(lhs) val(rhs)
        append(jsToNumber());

        append(ifEitherIsDouble(doubleNums));

        // -------------------------------------
        // Integral

        append(convertTopTwoToPrimitiveLongs());
        lsub();
        append(convertTopToLong());
        go_to(end);

        // -------------------------------------
        // Double

        label(doubleNums);
        append(convertTopTwoToPrimitiveDoubles());
        dsub();
        append(convertTopToDouble());

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, BitwiseExpression expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        append(jsGetValue());
        // value
        if (expr.getOp().equals(">>>")) {
            append(jsToUint32());
        } else {
            append(jsToInt32());
        }
        // Number
        invokevirtual(p(Number.class), "longValue", sig(long.class));
        // long

        expr.getRhs().accept(context, this, strict);
        append(jsGetValue());
        // long value

        switch (expr.getOp()) {
        case "<<":
        case ">>":
        case ">>>":
            // 11.7.1 - 11.7.3
            append(jsToUint32());
            // long Number
            invokevirtual(p(Number.class), "longValue", sig(long.class));
            // long long
            l2i();
            // long int
            ldc(0x1F);
            // long int int
            iand();
            // long int
            break;
        case "&":
        case "|":
        case "^":
            append(jsToInt32());
            // long Number
            invokevirtual(p(Number.class), "longValue", sig(long.class));
            // long long
            break;
        }
        if (expr.getOp().equals("<<")) {
            lshl();
            // long
            l2i();
            // int
            append(convertTopToInteger());
            // Integer
        } else if (expr.getOp().equals(">>")) {
            lshr();
            // long
            l2i();
            // int
            append(convertTopToInteger());
            // Integer
        } else if (expr.getOp().equals(">>>")) {
            lushr();
            // long
            append(convertTopToLong());
            // Long
        } else if (expr.getOp().equals("&")) {
            land();
            // long
            append(convertTopToLong());
            // Long
        } else if (expr.getOp().equals("|")) {
            lor();
            // long
            append(convertTopToLong());
            // Long
        } else if (expr.getOp().equals("^")) {
            lxor();
            // long
            append(convertTopToLong());
            // Long
        }
        return null;
    }

    @Override
    public Object visit(Object context, ArrayLiteralExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context
        invokestatic(p(BuiltinArray.class), "newArray", sig(DynArray.class, ExecutionContext.class));
        // array

        int index = 0;

        for (Expression each : expr.getExprs()) {
            if (each != null) {
                dup();
                // array array
                aload(Arities.EXECUTION_CONTEXT);
                // array array context
                ldc(index + "");
                // array array context name
                each.accept(context, this, strict);
                // array array context name val
                append(jsGetValue());
                // array array context name val
                invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializer", sig(PropertyDescriptor.class, Object.class));
                // array array context name desc
                iconst_0();
                i2b();
                // array array context name desc bool
                invokevirtual(p(DynArray.class), "defineOwnProperty",
                        sig(boolean.class, ExecutionContext.class, String.class, PropertyDescriptor.class, boolean.class));
                // array bool
                pop();
                // array
            }
            ++index;
        }

        // array
        dup();
        // array array
        aload(Arities.EXECUTION_CONTEXT);
        // array array context
        ldc("length");
        // array array context name
        ldc((long) expr.getExprs().size());
        // array array context name size
        invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
        // array array context name size
        iconst_0();
        i2b();
        // array array context name size bool
        invokeinterface(p(JSObject.class), "put", sig(void.class, ExecutionContext.class, String.class, Object.class, boolean.class));
        // array
        return null;
    }

    @Override
    public Object visit(Object context, AssignmentExpression expr, boolean strict) {
        LabelNode throwRefError = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // reference
        dup();
        // reference reference
        instance_of(p(Reference.class));
        // reference bool
        iffalse(throwRefError);
        // reference
        checkcast(p(Reference.class));
        expr.getRhs().accept(context, this, strict);
        // reference expr
        append(jsGetValue());
        // reference value
        dup_x1();
        // value reference value
        aload(Arities.EXECUTION_CONTEXT);
        // value reference value context
        swap();
        // value reference context value
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // value
        go_to(end);

        label(throwRefError);
        // reference
        pop();

        newobj(p(ThrowException.class));
        // ex
        dup();
        // ex ex
        aload(Arities.EXECUTION_CONTEXT);
        // ex ex context
        ldc(expr.getLhs().toString() + " is not a reference");
        // ex ex context str
        invokevirtual(p(ExecutionContext.class), "createReferenceError", sig(JSObject.class, String.class));
        // ex ex error
        aload(Arities.EXECUTION_CONTEXT);
        // ex ex error context
        swap();
        // ex ex context error
        invokespecial(p(ThrowException.class), "<init>", sig(void.class, ExecutionContext.class, Object.class));
        // ex ex
        athrow();

        label(end);
        nop();

        return null;
    }

    @Override
    public Object visit(Object context, BitwiseInversionOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        // obj
        append(jsGetValue());
        // val
        append(jsToInt32());
        // Long
        invokevirtual(p(Long.class), "longValue", sig(long.class));
        // long
        ldc(-1L);
        // long -1(long)
        lxor();
        // long
        invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
        // Long
        return null;
    }

    @Override
    public Object visit(Object context, BlockStatement statement, boolean strict) {
        // 12.1
        LabelNode abrupt = new LabelNode();
        LabelNode end = new LabelNode();

        normalCompletion();
        // completion
        astore(Arities.COMPLETION);
        // <empty>

        for (Statement each : statement.getBlockContent()) {
            if (each == null) {
                continue;
            }
            LabelNode nonAbrupt = new LabelNode();
            LabelNode bringForwardValue = new LabelNode();
            LabelNode nextStatement = new LabelNode();

            if (each.getPosition() != null) {
                line(each.getPosition().getLine());
                aload(Arities.EXECUTION_CONTEXT);
                // context
                ldc(each.getPosition().getLine());
                // context line
                invokevirtual(p(ExecutionContext.class), "setLineNumber", sig(void.class, int.class));
                // <empty>
            }
            if (each.getSizeMetric() > Chunker.STATEMENT_THRESHOLD) {
                interpretedStatement(each, strict);
            } else {
                each.accept(context, this, strict);
            }
            // completion(cur)
            dup();
            // completion(cur) completion(cur)
            append(handleCompletion(nonAbrupt, abrupt, abrupt, abrupt));

            // ----------------------------------------
            // Non-abrupt

            label(nonAbrupt);
            // completion(cur);
            dup();
            // completion(cur) completion(cur)
            append(jsCompletionValue());
            // completion(cur) value
            ifnull(bringForwardValue);
            // completion(cur)
            astore(Arities.COMPLETION);
            // <empty>
            go_to(nextStatement);

            // ----------------------------------------

            label(bringForwardValue);
            // completion(cur)
            dup();
            // completion(cur) completion(cur)
            aload(Arities.COMPLETION);
            // completion(cur) completion(cur) completion(prev)
            append(jsCompletionValue());
            // completion(cur) completion(cur) val(prev)
            putfield(p(Completion.class), "value", ci(Object.class));
            // completion(cur)
            astore(Arities.COMPLETION);
            // <empty>
            label(nextStatement);
        }

        go_to(end);

        // ----------------------------------------
        // ABRUPT

        label(abrupt);
        // completion(cur)
        astore(Arities.COMPLETION);
        // <empty>

        // ----------------------------------------
        // END
        label(end);
        // <empty>
        aload(Arities.COMPLETION);
        return null;
    }

    @Override
    public Object visit(Object context, BooleanLiteralExpression expr, boolean strict) {
        if (expr.getValue()) {
            getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
        } else {
            getstatic(p(Boolean.class), "FALSE", ci(Boolean.class));
        }
        return null;
    }

    @Override
    public Object visit(Object context, BreakStatement statement, boolean strict) {
        breakCompletion(statement.getTarget());
        return null;
    }

    @Override
    public Object visit(Object context, CaseClause clause, boolean strict) {
        clause.getBlock().accept(context, this, strict);
        return null;
    }

    @Override
    public Object visit(Object context, DefaultCaseClause clause, boolean strict) {
        clause.getBlock().accept(context, this, strict);
        return null;
    }

    @Override
    public Object visit(Object context, CatchClause clause, boolean strict) {
        clause.getBlock().accept(context, this, strict);
        return null;
    }

    @Override
    public Object visit(Object context, CompoundAssignmentExpression expr, boolean strict) {
        expr.getRootExpr().accept(context, this, strict);
        // value

        dup();
        // value value

        expr.getRootExpr().getLhs().accept(context, this, strict);
        // value value reference

        swap();
        // value reference value

        aload(Arities.EXECUTION_CONTEXT);
        // value reference value context

        swap();
        // value reference context value

        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // value
        return null;
    }

    @Override
    public Object visit(Object context, ContinueStatement statement, boolean strict) {
        continueCompletion(statement.getTarget());
        return null;
    }

    @Override
    public Object visit(Object context, DeleteOpExpression expr, boolean strict) {
        LabelNode checkAsProperty = new LabelNode();
        LabelNode handleEnvRec = new LabelNode();
        LabelNode notMap = new LabelNode();
        LabelNode returnTrue = new LabelNode();
        LabelNode end = new LabelNode();
        // ----------------------------------------

        expr.getExpr().accept(context, this, strict);
        // ref
        dup();
        // ref ref
        instance_of(p(Reference.class));
        // ref bool
        iffalse(returnTrue);
        // ref
        checkcast(p(Reference.class));
        // ref
        dup();
        // ref ref
        invokevirtual(p(Reference.class), "isUnresolvableReference", sig(boolean.class));
        // ref bool
        iffalse(checkAsProperty);
        // ref
        dup();
        // ref ref
        invokevirtual(p(Reference.class), "isStrictReference", sig(boolean.class));
        // ref bool
        iffalse(returnTrue);
        // ref
        append(jsThrowSyntaxError("unable to delete " + expr.getExpr()));
        // ref + throw
        go_to(returnTrue);

        // ----------------------------------------
        // Check as property

        label(checkAsProperty);
        // ref
        dup();
        // ref ref
        invokevirtual(p(Reference.class), "isPropertyReference", sig(boolean.class));
        // ref bool
        iffalse(handleEnvRec);
        // ref
        dup();
        // ref ref
        append(jsGetBase());
        // ref base
        dup();
        // ref base base
        instance_of( p(JSObject.class) );
        // ref base is-JSObject?
        iftrue( notMap );
        // ref base
        dup();
        // ref base base
        instance_of(p(Map.class));
        // ref base is-Map
        iffalse( notMap );
        // ref base
        checkcast(p(Map.class));
        // ref map
        swap();
        // map ref
        invokevirtual(p(Reference.class), "getReferencedName", sig(String.class));
        // map name
        invokeinterface(p(Map.class), "remove", sig(Object.class, Object.class));
        // prev-value
        go_to(returnTrue);

        // ----------------------------------------
        label(notMap);
        // ref base
        append(jsToObject());
        // ref obj
        swap();
        // obj ref
        aload(Arities.EXECUTION_CONTEXT);
        // obj ref context
        swap();
        // obj context ref
        dup();
        // obj context ref ref
        invokevirtual(p(Reference.class), "getReferencedName", sig(String.class));
        // obj context ref name
        swap();
        // obj context name ref
        invokevirtual(p(Reference.class), "isStrictReference", sig(boolean.class));
        // obj context name bool
        invokeinterface(p(JSObject.class), "delete", sig(boolean.class, ExecutionContext.class, String.class, boolean.class));
        // bool
        invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
        // Boolean
        go_to(end);

        // ----------------------------------------
        // Environment record
        LabelNode throwSyntax = new LabelNode();

        label(handleEnvRec);
        // ref
        dup();
        // ref ref
        invokevirtual(p(Reference.class), "isStrictReference", sig(boolean.class));
        // ref bool
        iftrue(throwSyntax);
        // ref
        dup();
        // ref ref
        append(jsGetBase());
        // ref base
        checkcast(p(EnvironmentRecord.class));
        // ref env-rec
        swap();
        // env-rec ref
        invokevirtual(p(Reference.class), "getReferencedName", sig(String.class));
        // env-rec name
        aload(Arities.EXECUTION_CONTEXT);
        // env-rec name context
        swap();
        // env-rec context name
        invokeinterface(p(EnvironmentRecord.class), "deleteBinding", sig(boolean.class, ExecutionContext.class, String.class));
        // bool
        invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
        // Boolean
        go_to(end);

        label(throwSyntax);
        // ref
        append(jsThrowSyntaxError("unable to delete"));
        // ref
        go_to(end);

        // ----------------------------------------
        // Simple true (with pop)
        label(returnTrue);
        // ref
        pop();
        // <EMPTY>
        getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
        // Boolean

        // ----------------------------------------
        label(end);
        nop();
        return null;

    }

    @Override
    public Object visit(Object context, DoWhileStatement statement, boolean strict) {
        LabelNode begin = new LabelNode();
        LabelNode normalTarget = new LabelNode();
        LabelNode breakTarget = new LabelNode();
        LabelNode continueTarget = new LabelNode();
        LabelNode end = new LabelNode();

        label(begin);
        invokeCompiledStatementBlock("Do", statement.getBlock(), strict);
        // completion(block)
        dup();
        // completion(block) completion(block)
        append(handleCompletion(normalTarget, breakTarget, continueTarget, end));

        // ----------------------------------------
        // NORMAL
        label(normalTarget);
        // completion(block)

        statement.getTest().accept(context, this, strict);
        // completion(block) result
        append(jsGetValue());
        // completion(block) result
        append(jsToBoolean());
        // completion(block) Boolean
        invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
        // completion(block) bool
        iffalse(end);
        pop();
        // <EMPTY>
        go_to(begin);

        // ----------------------------------------
        // BREAK
        label(breakTarget);
        // completion(block,BREAK)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(end);

        // ----------------------------------------
        // CONTINUE

        label(continueTarget);
        // completion(block,CONTINUE)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        go_to(normalTarget);

        // ----------------------------------------
        label(end);
        // completion(block)
        nop();
        // completion(block)
        return null;
    }

    @Override
    public Object visit(Object context, EmptyStatement statement, boolean strict) {
        normalCompletion();
        return null;
    }

    @Override
    public Object visit(Object context, EqualityOperatorExpression expr, boolean strict) {
        LabelNode returnTrue = new LabelNode();
        LabelNode returnFalse = new LabelNode();
        LabelNode end = new LabelNode();

        aload(Arities.EXECUTION_CONTEXT);
        // context
        expr.getLhs().accept(context, this, strict);
        // context obj(lhs)
        append(jsGetValue());
        // context val(lhs)
        expr.getRhs().accept(context, this, strict);
        // context val(lhs) obj(rhs)
        append(jsGetValue());
        // context val(lhs) val(rhs)
        invokestatic(p(Types.class), "compareEquality", sig(boolean.class, ExecutionContext.class, Object.class, Object.class));
        // bool

        if (expr.getOp().equals("==")) {
            iftrue(returnTrue);
            go_to(returnFalse);
        } else {
            iffalse(returnTrue);
            go_to(returnFalse);
        }

        label(returnTrue);
        getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
        go_to(end);

        label(returnFalse);
        getstatic(p(Boolean.class), "FALSE", ci(Boolean.class));

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, CommaOperator expr, boolean strict) {
        expr.getLhs().accept(context, this, strict);
        jsGetValue();
        pop();
        expr.getRhs().accept(context, this, strict);
        jsGetValue();
        return null;
    }

    @Override
    public Object visit(Object context, ExpressionStatement statement, boolean strict) {
        Expression expr = statement.getExpr();
        if (expr instanceof FunctionDeclaration) {
            normalCompletion();
        } else {
            expr.accept(context, this, strict);
            // value
            append(jsGetValue());
            // value
            normalCompletionWithValue();
            // Completion
        }
        return null;
    }

    @Override
    public Object visit(Object context, FloatingNumberExpression expr, boolean strict) {
        visit(context, (NumberLiteralExpression) expr, strict);
        return null;
    }

    @Override
    public Object visit(Object context, ForExprInStatement statement, boolean strict) {
        LabelNode nextName = new LabelNode();
        LabelNode checkCompletion = new LabelNode();
        LabelNode bringForward = new LabelNode();
        LabelNode doBreak = new LabelNode();
        LabelNode doContinue = new LabelNode();
        LabelNode undefEnd = new LabelNode();
        LabelNode end = new LabelNode();

        normalCompletion();
        // completion
        statement.getRhs().accept(context, this, strict);
        // completion val
        append(jsGetValue());
        // completion val
        dup();
        // completion val val
        append(jsPushUndefined());
        // completion val val UNDEF
        if_acmpeq(undefEnd);
        // completion val
        dup();
        // completion val val
        append(jsPushNull());
        // completion val val NULL
        if_acmpeq(undefEnd);
        // completion val
        append(jsToObject());
        // completion jsObj

        // -----------------------------------------------
        // completion jsObj
        invokeinterface(p(JSObject.class), "getAllEnumerablePropertyNames", sig(NameEnumerator.class));
        // completion name-enum
        astore(4);
        // completion
        label(nextName);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "hasNext", sig(boolean.class));
        // completion bool
        iffalse(end);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "next", sig(String.class));
        // completion str

        statement.getExpr().accept(context, this, strict);
        // completion str ref
        swap();
        // completion ref str
        aload(Arities.EXECUTION_CONTEXT);
        // completion ref str context
        swap();
        // completion ref context str
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // completion
        invokeCompiledStatementBlock("For", statement.getBlock(), strict);
        // completion(prev) completion(cur)
        dup();
        // completion(prev) completion(cur) completion(cur)
        append(jsCompletionValue());
        // completion(prev) completion(cur) val(cur)
        ifnull(bringForward);
        // completion(prev) completion(cur)

        // ----------------------------------
        // has value
        swap();
        // completion(cur) completion(prev)
        pop();
        // completion(cur)
        go_to(checkCompletion);

        // ----------------------------------------
        // bring previous value forward

        label(bringForward);
        // completion(prev) completion(cur)
        dup_x1();
        // completion(cur) completion(prev) completion(cur)
        swap();
        // completion(cur) completion(cur) completion(prev)
        append(jsGetValue());
        // completion(cur) completion(cur) val(prev)
        putfield(p(Completion.class), "value", ci(Object.class));
        // completion(cur)

        // -----------------------------------------------
        label(checkCompletion);
        // completion(cur)
        dup();
        // completion(cur) completion(cur)
        append(handleCompletion(nextName, doBreak, doContinue, end));
        // completion

        // ----------------------------------------
        // BREAK
        label(doBreak);
        // completion(block,BREAK)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(end);

        // ----------------------------------------
        // CONTINUE

        label(doContinue);
        // completion(block,CONTINUE)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        go_to(nextName);

        // -----------------------------------------------
        // RHS is undefined
        // completion undef
        label(undefEnd);
        // completion undef
        pop();
        // completion

        // -----------------------------------------------

        label(end);
        // completion
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, ForExprOfStatement statement, boolean strict) {
        LabelNode nextName = new LabelNode();
        LabelNode checkCompletion = new LabelNode();
        LabelNode bringForward = new LabelNode();
        LabelNode doBreak = new LabelNode();
        LabelNode doContinue = new LabelNode();
        LabelNode undefEnd = new LabelNode();
        LabelNode end = new LabelNode();

        normalCompletion();
        // completion
        statement.getRhs().accept(context, this, strict);
        // completion val
        append(jsGetValue());
        // completion val
        dup();
        // completion val val
        append(jsPushUndefined());
        // completion val val UNDEF
        if_acmpeq(undefEnd);
        // completion val
        dup();
        // completion val val
        append(jsPushNull());
        // completion val val NULL
        if_acmpeq(undefEnd);
        // completion val
        append(jsToObject());
        // completion jsObj

        // -----------------------------------------------
        // completion jsObj
        invokeinterface(p(JSObject.class), "getAllEnumerablePropertyNames", sig(NameEnumerator.class));
        // completion name-enum
        astore(4);
        // completion
        label(nextName);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "hasNext", sig(boolean.class));
        // completion bool
        iffalse(end);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "next", sig(String.class));
        // completion str
        append(jsToObject());
        // completion str jsObj
        swap();
        // completion jsObj str
        invokevirtual(p(ExecutionContext.class), "createPropertyReference", sig(Reference.class, JSObject.class, String.class));
        // completion val
        statement.getExpr().accept(context, this, strict);
        // completion val ref
        swap();
        // completion ref val
        aload(Arities.EXECUTION_CONTEXT);
        // completion ref val context
        swap();
        // completion ref context val
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // completion
        invokeCompiledStatementBlock("For", statement.getBlock(), strict);
        // completion(prev) completion(cur)
        dup();
        // completion(prev) completion(cur) completion(cur)
        append(jsCompletionValue());
        // completion(prev) completion(cur) val(cur)
        ifnull(bringForward);
        // completion(prev) completion(cur)

        // ----------------------------------
        // has value
        swap();
        // completion(cur) completion(prev)
        pop();
        // completion(cur)
        go_to(checkCompletion);

        // ----------------------------------------
        // bring previous value forward

        label(bringForward);
        // completion(prev) completion(cur)
        dup_x1();
        // completion(cur) completion(prev) completion(cur)
        swap();
        // completion(cur) completion(cur) completion(prev)
        append(jsGetValue());
        // completion(cur) completion(cur) val(prev)
        putfield(p(Completion.class), "value", ci(Object.class));
        // completion(cur)

        // -----------------------------------------------
        label(checkCompletion);
        // completion(cur)
        dup();
        // completion(cur) completion(cur)
        append(handleCompletion(nextName, doBreak, doContinue, end));
        // completion

        // ----------------------------------------
        // BREAK
        label(doBreak);
        // completion(block,BREAK)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(end);

        // ----------------------------------------
        // CONTINUE

        label(doContinue);
        // completion(block,CONTINUE)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        go_to(nextName);

        // -----------------------------------------------
        // RHS is undefined
        // completion undef
        label(undefEnd);
        // completion undef
        pop();
        // completion

        // -----------------------------------------------

        label(end);
        // completion
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, ForVarDeclInStatement statement, boolean strict) {
        LabelNode nextName = new LabelNode();
        LabelNode checkCompletion = new LabelNode();
        LabelNode bringForward = new LabelNode();
        LabelNode doBreak = new LabelNode();
        LabelNode doContinue = new LabelNode();
        LabelNode undefEnd = new LabelNode();
        LabelNode end = new LabelNode();

        normalCompletion();
        // completion
        statement.getRhs().accept(context, this, strict);
        // completion val
        append(jsGetValue());
        // completion val
        dup();
        // completion val val
        append(jsPushUndefined());
        // completion val val UNDEF
        if_acmpeq(undefEnd);
        // completion val
        dup();
        // completion val val
        append(jsPushNull());
        // completion val val NULL
        if_acmpeq(undefEnd);
        // completion val
        append(jsToObject());
        // completion jsObj

        // -----------------------------------------------
        // completion jsObj
        invokeinterface(p(JSObject.class), "getAllEnumerablePropertyNames", sig(NameEnumerator.class));
        // completion name-enum
        astore(4);
        // completion
        label(nextName);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "hasNext", sig(boolean.class));
        // completion bool
        iffalse(end);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "next", sig(String.class));
        // completion str

        statement.getDeclaration().accept(context, this, strict);
        // completion
        pop();
        // <EMPTY>
        aload(Arities.EXECUTION_CONTEXT);
        // context
        ldc(statement.getDeclaration().getIdentifier());
        // context identifier
        invokevirtual(p(ExecutionContext.class), "resolve", sig(Reference.class, String.class));
        // reference

        // completion str ref
        swap();
        // completion ref str
        aload(Arities.EXECUTION_CONTEXT);
        // completion ref str context
        swap();
        // completion ref context str
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // completion
        invokeCompiledStatementBlock("For", statement.getBlock(), strict);
        // completion(prev) completion(cur)
        dup();
        // completion(prev) completion(cur) completion(cur)
        append(jsCompletionValue());
        // completion(prev) completion(cur) val(cur)
        ifnull(bringForward);
        // completion(prev) completion(cur)

        // ----------------------------------
        // has value
        swap();
        // completion(cur) completion(prev)
        pop();
        // completion(cur)
        go_to(checkCompletion);

        // ----------------------------------------
        // bring previous value forward

        label(bringForward);
        // completion(prev) completion(cur)
        dup_x1();
        // completion(cur) completion(prev) completion(cur)
        swap();
        // completion(cur) completion(cur) completion(prev)
        append(jsGetValue());
        // completion(cur) completion(cur) val(prev)
        putfield(p(Completion.class), "value", ci(Object.class));
        // completion(cur)

        // -----------------------------------------------
        label(checkCompletion);
        // completion(cur)
        dup();
        // completion(cur) completion(cur)
        append(handleCompletion(nextName, doBreak, doContinue, end));
        // completion

        // ----------------------------------------
        // BREAK
        label(doBreak);
        // completion(block,BREAK)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(end);

        // ----------------------------------------
        // CONTINUE

        label(doContinue);
        // completion(block,CONTINUE)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        go_to(nextName);

        // -----------------------------------------------
        // RHS is undefined
        // completion undef
        label(undefEnd);
        // completion undef
        pop();
        // completion

        // -----------------------------------------------

        label(end);
        // completion
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, ForVarDeclOfStatement statement, boolean strict) {
        LabelNode nextName = new LabelNode();
        LabelNode checkCompletion = new LabelNode();
        LabelNode bringForward = new LabelNode();
        LabelNode doBreak = new LabelNode();
        LabelNode doContinue = new LabelNode();
        LabelNode undefEnd = new LabelNode();
        LabelNode end = new LabelNode();

        normalCompletion();
        // completion
        statement.getRhs().accept(context, this, strict);
        // completion val
        append(jsGetValue());
        // completion val
        dup();
        // completion val val
        append(jsPushUndefined());
        // completion val val UNDEF
        if_acmpeq(undefEnd);
        // completion val
        dup();
        // completion val val
        append(jsPushNull());
        // completion val val NULL
        if_acmpeq(undefEnd);
        // completion val
        append(jsToObject());
        // completion jsObj

        // -----------------------------------------------
        // completion jsObj
        invokeinterface(p(JSObject.class), "getAllEnumerablePropertyNames", sig(NameEnumerator.class));
        // completion name-enum
        astore(4);
        // completion
        label(nextName);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "hasNext", sig(boolean.class));
        // completion bool
        iffalse(end);
        // completion
        aload(4);
        // completion name-enum
        invokevirtual(p(NameEnumerator.class), "next", sig(String.class));
        // completion str
        append(jsToObject());
        // completion str jsObj
        swap();
        // completion jsObj str
        invokevirtual(p(ExecutionContext.class), "createPropertyReference", sig(Reference.class, JSObject.class, String.class));
        // completion val
        statement.getDeclaration().accept(context, this, strict);
        // completion
        pop();
        // <EMPTY>
        aload(Arities.EXECUTION_CONTEXT);
        // context
        ldc(statement.getDeclaration().getIdentifier());
        // context identifier
        invokevirtual(p(ExecutionContext.class), "resolve", sig(Reference.class, String.class));
        // reference

        // completion str ref
        swap();
        // completion ref str
        aload(Arities.EXECUTION_CONTEXT);
        // completion ref str context
        swap();
        // completion ref context str
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // completion
        invokeCompiledStatementBlock("For", statement.getBlock(), strict);
        // completion(prev) completion(cur)
        dup();
        // completion(prev) completion(cur) completion(cur)
        append(jsCompletionValue());
        // completion(prev) completion(cur) val(cur)
        ifnull(bringForward);
        // completion(prev) completion(cur)

        // ----------------------------------
        // has value
        swap();
        // completion(cur) completion(prev)
        pop();
        // completion(cur)
        go_to(checkCompletion);

        // ----------------------------------------
        // bring previous value forward

        label(bringForward);
        // completion(prev) completion(cur)
        dup_x1();
        // completion(cur) completion(prev) completion(cur)
        swap();
        // completion(cur) completion(cur) completion(prev)
        append(jsGetValue());
        // completion(cur) completion(cur) val(prev)
        putfield(p(Completion.class), "value", ci(Object.class));
        // completion(cur)

        // -----------------------------------------------
        label(checkCompletion);
        // completion(cur)
        dup();
        // completion(cur) completion(cur)
        append(handleCompletion(nextName, doBreak, doContinue, end));
        // completion

        // ----------------------------------------
        // BREAK
        label(doBreak);
        // completion(block,BREAK)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(end);

        // ----------------------------------------
        // CONTINUE

        label(doContinue);
        // completion(block,CONTINUE)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        go_to(nextName);

        // -----------------------------------------------
        // RHS is undefined
        // completion undef
        label(undefEnd);
        // completion undef
        pop();
        // completion

        // -----------------------------------------------

        label(end);
        // completion
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, ForExprStatement statement, boolean strict) {
        if (statement.getExpr() != null) {
            statement.getExpr().accept(context, this, strict);
            pop();
        }
        visitFor(context, statement, strict);
        return null;
    }

    @Override
    public Object visit(Object context, ForVarDeclStatement statement, boolean strict) {
        List<VariableDeclaration> decls = statement.getDeclarationList();
        for (VariableDeclaration each : decls) {
            each.accept(context, this, strict);
            pop();
        }
        visitFor(context, statement, strict);
        return null;
    }

    public Object visitFor(Object context, AbstractForStatement statement, boolean strict) {
        LabelNode begin = new LabelNode();
        LabelNode bringForward = new LabelNode();
        LabelNode hasValue = new LabelNode();
        LabelNode checkCompletion = new LabelNode();
        LabelNode doIncrement = new LabelNode();
        LabelNode doBreak = new LabelNode();
        LabelNode doContinue = new LabelNode();
        LabelNode end = new LabelNode();

        normalCompletion();
        // completion

        label(begin);

        if (statement.getTest() != null) {
            statement.getTest().accept(context, this, strict);
            append(jsGetValue());
            append(jsToBoolean());
            invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
            // completion bool
            iffalse(end);
            // completion
        }

        // completion(prev)
        invokeCompiledStatementBlock("For", statement.getBlock(), strict);
        // completion(prev) completion(cur)
        dup();
        // completion(prev) completion(cur) completion(cur)
        append(jsCompletionValue());
        // completion(prev) completion(cur) val(cur)
        ifnull(bringForward);
        // completion(prev) completion(cur)
        go_to(hasValue);

        // ----------------------------------------
        // bring previous forward

        label(bringForward);
        // completion(prev) completion(cur)
        dup_x1();
        // completion(cur) completion(prev) completion(cur)
        swap();

        // completion(cur) completion(cur) completion(prev)
        append(jsCompletionValue());
        // completion(cur) completion(cur) val(prev)
        putfield(p(Completion.class), "value", ci(Object.class));
        // completion(cur)
        go_to(checkCompletion);

        // ----------------------------------------
        // has value

        label(hasValue);
        // completion(prev) completion(cur)
        swap();
        // completion(cur) completion(prev)
        pop();
        // completion(cur)

        // ----------------------------------------
        // handle current completion

        label(checkCompletion);
        // completion
        dup();
        // completion completion

        append(handleCompletion(doIncrement, /* break */doBreak, /* continue */doContinue, /* return */end));

        // ----------------------------------------
        // do increment

        label(doIncrement);
        // completion
        if (statement.getIncrement() != null) {
            statement.getIncrement().accept(context, this, strict);
            append(jsGetValue());
            pop();
        }
        // completion

        go_to(begin);

        // ----------------------------------------
        // BREAK
        label(doBreak);
        // completion(block,BREAK)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(end);

        // ----------------------------------------
        // CONTINUE

        label(doContinue);
        // completion(block,CONTINUE)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(doIncrement);

        label(end);
        // completion
        return null;
    }

    @Override
    public Object visit(Object context, FunctionCallExpression expr, boolean strict) {
        LabelNode propertyRef = new LabelNode();
        LabelNode noSelf = new LabelNode();
        LabelNode doCall = new LabelNode();
        LabelNode isCallable = new LabelNode();
        // 11.2.3
        aload(Arities.EXECUTION_CONTEXT);
        // context
        expr.getMemberExpression().accept(context, this, strict);
        // context ref
        dup();
        // context ref ref
        append(jsGetValue());
        // context ref function

        swap();
        // context function ref
        dup();
        // context function ref ref
        dup_x2();
        // context ref function ref ref
        instance_of(p(Reference.class));
        // context ref function ref isref?
        iffalse(noSelf);

        // ----------------------------------------
        // Reference

        // context ref function ref
        checkcast(p(Reference.class));

        dup();
        // context ref function ref ref
        invokevirtual(p(Reference.class), "isPropertyReference", sig(boolean.class));
        // context ref function ref bool(is-prop)

        iftrue(propertyRef);

        // ----------------------------------------
        // Environment Record

        // context ref function ref
        append(jsGetBase());
        // context ref function base
        checkcast(p(EnvironmentRecord.class));
        // context ref function env-rec
        invokeinterface(p(EnvironmentRecord.class), "implicitThisValue", sig(Object.class));
        // context ref function self
        go_to(doCall);

        // ----------------------------------------
        // Property Reference
        label(propertyRef);
        // context ref function ref
        append(jsGetBase());
        // context ref function self
        go_to(doCall);

        // ------------------------------------------
        // No self
        label(noSelf);
        // context ref function ref
        pop();
        // context ref function
        append(jsPushUndefined());
        // context ref function UNDEFINED

        // ------------------------------------------
        // call()

        label(doCall);
        // context ref function self

        swap();
        // context ref self function

        List<Expression> argExprs = expr.getArgumentExpressions();
        int numArgs = argExprs.size();
        bipush(numArgs);
        anewarray(p(Object.class));
        // context ref self function array
        for (int i = 0; i < numArgs; ++i) {
            dup();
            bipush(i);

            argExprs.get(i).accept(context, this, strict);
            append(jsGetValue());
            aastore();
        }
        // context ref self function array

        swap();
        // context ref self array function
        dup_x2();
        // context ref function self array function
        invokestatic(p(Types.class), "isCallable", sig(boolean.class, Object.class));
        // context ref function self array bool
        iftrue(isCallable);
        // context ref function self array
        append(jsThrowTypeError(expr.getMemberExpression() + " is not a function"));
        // THROWN!

        label(isCallable);
        // context ref function self array

        // call ExecutionContext#call(ref, fn, self, args) -> Object
        invokevirtual(p(ExecutionContext.class), "call", sig(Object.class, Object.class, JSFunction.class, Object.class, Object[].class));
        // obj
        return null;
    }

    @Override
    public Object visit(Object context, FunctionDeclaration statement, boolean strict) {
        normalCompletion();
        return null;
    }

    @Override
    public Object visit(Object context, FunctionExpression expr, boolean strict) {
        compiledFunction(expr.getDescriptor().getIdentifier(), expr.getDescriptor().getFormalParameterNames(), expr.getDescriptor().getBlock(), expr.getDescriptor()
                .isStrict());
        return null;
    }

    @Override
    public Object visit(Object context, IdentifierReferenceExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context
        ldc(expr.getIdentifier());
        // context identifier
        invokevirtual(p(ExecutionContext.class), "resolve", sig(Reference.class, String.class));
        // reference
        return null;
    }

    @Override
    public Object visit(Object context, IfStatement statement, boolean strict) {
        LabelNode elseBranch = new LabelNode();
        LabelNode noElseBranch = new LabelNode();
        LabelNode end = new LabelNode();

        statement.getTest().accept(context, this, strict);
        // value
        append(jsGetValue());
        // value
        append(jsToBoolean());
        // Boolean
        invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
        // bool

        if (statement.getElseBlock() == null) {
            // completion bool
            iffalse(noElseBranch);
        } else {
            iffalse(elseBranch);
        }
        // <empty>

        // ----------------------------------------
        // THEN

        if (statement.getThenBlock() != null) {
            invokeCompiledStatementBlock("Then", statement.getThenBlock(), strict);
        } else {
            normalCompletion();
        }
        // completion
        go_to(end);

        // ----------------------------------------
        // ELSE
        if (statement.getElseBlock() == null) {
            label(noElseBranch);
            normalCompletion();
        } else {
            label(elseBranch);
            // <empty>
            invokeCompiledStatementBlock("Else", statement.getElseBlock(), strict);
            // completion
        }

        label(end);
        // completion

        nop();
        return null;
    }

    @Override
    public Object visit(Object context, InOperatorExpression expr, boolean strict) {
        LabelNode typeError = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // obj(lhs)
        append(jsGetValue());
        // val(lhs)

        expr.getRhs().accept(context, this, strict);
        // val(lhs) obj(rhs)
        append(jsGetValue());
        // val(lhs) val(rhs)

        dup();
        // val(lhs) val(rhs) val(rhs)
        instance_of(p(JSObject.class));
        // val(lhs) val(rhs) bool

        iffalse(typeError);
        // val(lhs) val(rhs)
        checkcast(p(JSObject.class));
        // val(lhs) obj(rhs)
        swap();
        // obj(rhs) val(lhs)
        append(jsToString());
        // obj(rhs) str(lhs)
        aload(Arities.EXECUTION_CONTEXT);
        // obj(rhs) str(lhs) context
        swap();
        // object(rhs) context str(lhs);
        invokeinterface(p(JSObject.class), "hasProperty", sig(boolean.class, ExecutionContext.class, String.class));
        // bool
        go_to(end);

        label(typeError);
        // val(lhs) val(rhs)
        pop();
        pop();
        iconst_0();
        i2b();
        // bool
        append(jsThrowTypeError("not an object"));

        label(end);
        invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
        // Boolean
        return null;
    }

    @Override
    public Object visit(Object context, OfOperatorExpression expr, boolean strict) {
        LabelNode typeError = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // obj(lhs)
        append(jsGetValue());
        // val(lhs)

        expr.getRhs().accept(context, this, strict);
        // val(lhs) obj(rhs)
        append(jsGetValue());
        // val(lhs) val(rhs)

        dup();
        // val(lhs) val(rhs) val(rhs)
        instance_of(p(JSObject.class));
        // val(lhs) val(rhs) bool

        iffalse(typeError);
        // val(lhs) val(rhs)
        checkcast(p(JSObject.class));
        // val(lhs) obj(rhs)
        swap();
        // obj(rhs) val(lhs)
        append(jsToString());
        // obj(rhs) str(lhs)
        aload(Arities.EXECUTION_CONTEXT);
        // obj(rhs) str(lhs) context
        swap();
        // object(rhs) context str(lhs);
        invokeinterface(p(JSObject.class), "hasProperty", sig(boolean.class, ExecutionContext.class, String.class));
        // bool
        go_to(end);

        label(typeError);
        // val(lhs) val(rhs)
        pop();
        pop();
        iconst_0();
        i2b();
        // bool
        append(jsThrowTypeError("not an object"));

        label(end);
        invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
        // Boolean
        return null;
    }

    @Override
    public Object visit(Object context, InstanceofExpression expr, boolean strict) {
        LabelNode typeError = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // obj(lhs)
        append(jsGetValue());
        // val(lhs)

        expr.getRhs().accept(context, this, strict);
        // val(lhs) obj(rhs)
        append(jsGetValue());
        // val(lhs) val(rhs)

        dup();
        // val(lhs) val(rhs) val(rhs)
        instance_of(p(JSFunction.class));
        // val(lhs) val(rhs) bool

        iffalse(typeError);
        // val(lhs) val(rhs)
        checkcast(p(JSFunction.class));
        // val(lhs) fn(rhs)
        swap();
        // fn(rhs) val(lhs)
        aload(Arities.EXECUTION_CONTEXT);
        // fn(rhs) val(lhs) context
        swap();
        // fn(fhs) context val(lhs)
        invokeinterface(p(JSFunction.class), "hasInstance", sig(boolean.class, ExecutionContext.class, Object.class));
        // bool
        go_to(end);

        label(typeError);
        // val(lhs) val(rhs)
        pop();
        pop();
        iconst_0();
        i2b();
        // bool
        append(jsThrowTypeError("not an object"));

        label(end);
        invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
        // Boolean
        return null;
    }

    @Override
    public Object visit(Object context, IntegerNumberExpression expr, boolean strict) {
        visit(context, (NumberLiteralExpression) expr, strict);
        return null;
    }

    @Override
    public Object visit(Object context, LogicalExpression expr, boolean strict) {
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        append(jsGetValue());
        dup();
        // val(lhs) val(lhs)
        append(jsToBoolean());
        // val(lhs) bool(lhs)
        invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
        // val(lhs) bool(lhs)

        if (expr.getOp().equals("&&")) {
            iffalse(end);
        } else if (expr.getOp().equals("||")) {
            iftrue(end);
        }
        pop();

        // <empty>

        expr.getRhs().accept(context, this, strict);
        // val(rhs)
        append(jsGetValue());
        // val(rhs)
        go_to(end);

        // ----------------------------------------
        label(end);
        // val

        nop();
        return null;
    }

    @Override
    public Object visit(Object context, LogicalNotOperatorExpression expr, boolean strict) {
        LabelNode returnFalse = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getExpr().accept(context, this, strict);
        // obj
        append(jsGetValue());
        // val
        append(jsToBoolean());
        // Boolean
        invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
        // bool
        iftrue(returnFalse);
        iconst_1();
        go_to(end);
        label(returnFalse);
        iconst_0();
        label(end);
        invokestatic(p(Boolean.class), "valueOf", sig(Boolean.class, boolean.class));
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, DotExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context
        expr.getLhs().accept(context, this, strict);
        // context reference
        append(jsGetValue());
        // context object
        ldc(expr.getIdentifier());
        // context object identifier
        swap();
        // context identifier obj
        append(jsCheckObjectCoercible(null));
        // context identifier obj
        swap();
        // context object identifier
        append(jsCreatePropertyReference());
        // reference
        return null;
    }

    @Override
    public Object visit(Object context, BracketExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context
        expr.getLhs().accept(context, this, strict);
        // context reference
        append(jsGetValue());
        // context object
        expr.getRhs().accept(context, this, strict);
        // context object ident-expr
        swap();
        // context ident-expr obj
        append(jsCheckObjectCoercible(null));
        // context ident-expr obj
        swap();
        // context object ident-expr
        append(jsGetValue());
        // context object ident-obj
        append(jsToString());
        // context object ident-str
        append(jsCreatePropertyReference());
        // reference
        return null;
    }

    @Override
    public Object visit(Object context, MultiplicativeExpression expr, boolean strict) {
        LabelNode doubleNums = new LabelNode();
        LabelNode returnNaN = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // val(lhs)
        append(jsGetValue());
        append(jsToNumber());
        expr.getRhs().accept(context, this, strict);
        // val(rhs)
        append(jsGetValue());
        append(jsToNumber());
        // val(lhs) val(rhs)

        append(ifEitherIsNaN(returnNaN));

        if (expr.getOp().equals("%")) {
            append(ifTopIsZero(returnNaN));
        }

        if (!expr.getOp().equals("/")) {
            append(ifEitherIsDouble(doubleNums));

            if (expr.getOp().equals("*")) {
                append(convertTopTwoToPrimitiveLongs());
                lmul();
                append(convertTopToLong());
            } else if (expr.getOp().equals("/")) {
                append(convertTopTwoToPrimitiveLongs());
                ldiv();
                append(convertTopToLong());
            } else if (expr.getOp().equals("%")) {
                invokestatic(p(BuiltinNumber.class), "modulo", sig(Number.class, Number.class, Number.class));
            }
            go_to(end);

            label(doubleNums);
        }

        append(convertTopTwoToPrimitiveDoubles());

        if (expr.getOp().equals("*")) {
            dmul();
        } else if (expr.getOp().equals("/")) {
            ddiv();
        } else if (expr.getOp().equals("%")) {
            drem();
        }
        append(convertTopToDouble());

        go_to(end);

        label(returnNaN);
        pop();
        pop();
        getstatic(p(Double.class), "NaN", ci(double.class));
        invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, NewOperatorExpression expr, boolean strict) {
        LabelNode end = new LabelNode();
        // 11.2.2

        expr.getExpr().accept(context, this, strict);
        // obj

        aload(Arities.EXECUTION_CONTEXT);
        // obj context
        swap();
        // context obj
        append(jsGetValue(JSFunction.class));
        // context ctor-fn

        bipush(0);
        anewarray(p(Object.class));
        // context function array
        invokevirtual(p(ExecutionContext.class), "construct", sig(Object.class, JSFunction.class, Object[].class));
        // obj

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, NullLiteralExpression expr, boolean strict) {
        getstatic(p(Types.class), "NULL", ci(Types.Null.class));
        return null;
    }

    public Object visit(Object context, NumberLiteralExpression expr, boolean strict) {
        String text = expr.getText();

        if (text.indexOf('.') == 0) {
            ldc("0" + text);
            invokestatic(p(Double.class), "valueOf", sig(Double.class, String.class));
        } else if (text.indexOf(".") > 0) {
            ldc(text);
            ldc(10);
            invokestatic(p(Types.class), "parseLongOrDouble", sig(Number.class, String.class, int.class));
            // Long or Double
        } else {
            if (text.startsWith("0x") || text.startsWith("0X")) {
                String realText = text.substring(2);
                ldc(realText);
                bipush(expr.getRadix());
                invokestatic(p(Long.class), "valueOf", sig(Long.class, String.class, int.class));
                // Long
            } else {
                final int index = text.toLowerCase().indexOf('e');
                if (index > 0) {
                    // scientific notation, but without the java-friendlyness. E.g. 1E21 instead of 1.0e21
                    String base = text.substring(0, index);
                    String exponent = text.substring(index);
                    String javafied = base + ".0" + exponent;
                    ldc(javafied);
                    invokestatic(p(Double.class), "valueOf", sig(Double.class, String.class));
                } else {
                    ldc(text);
                    bipush(expr.getRadix());
                    invokestatic(p(Types.class), "parseLongOrDouble", sig(Number.class, String.class, int.class));
                    // Long or Double
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(Object context, ObjectLiteralExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context

        invokestatic(p(BuiltinObject.class), "newObject", sig(DynObject.class, ExecutionContext.class));
        // obj

        for (PropertyAssignment each : expr.getPropertyAssignments()) {
            dup();
            // obj obj
            each.accept(context, this, strict);
        }
        // obj
        return null;
    }

    @Override
    public Object visit(Object context, PostOpExpression expr, boolean strict) {
        LabelNode doubleNum = new LabelNode();
        LabelNode invalid = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getExpr().accept(context, this, strict);
        // obj
        dup();
        // obj obj
        instance_of(p(Reference.class));
        // ref bool
        iffalse(invalid);
        // ref
        dup();
        // ref ref
        invokevirtual(p(Reference.class), "isValidForPrePostIncrementDecrement", sig(boolean.class));
        // ref bool
        iffalse(invalid);
        // ref
        dup();
        // ref ref
        append(jsGetValue());
        // ref value
        append(jsToNumber());
        // ref number
        dup();
        // ref number number
        instance_of(p(Double.class));
        // ref number bool
        iftrue(doubleNum);

        // ----------------------------------------
        // Long

        // ref number
        dup2();
        // ref Long ref Long
        invokevirtual(p(Number.class), "longValue", sig(long.class));
        // ref Long ref long
        ldc(1L);
        // ref Long ref long 1
        if (expr.getOp().equals("++")) {
            ladd();
        } else {
            lsub();
        }
        // ref Long(orig) ref long(new)
        invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
        // ref Long(orig) ref Long(new)
        aload(Arities.EXECUTION_CONTEXT);
        // ref Long(orig) ref Long(new) context
        swap();
        // ref Long(orig) ref context Long(new)
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // ref Long(orig)
        swap();
        // Long(orig) ref
        pop();
        // Long(orig)
        go_to(end);

        // ----------------------------------------
        // Double

        label(doubleNum);

        // ref number
        dup2();
        // ref Double ref Double
        invokevirtual(p(Number.class), "doubleValue", sig(double.class));
        // ref Double ref double
        iconst_1();
        // ref Double ref double 1
        i2d();
        // ref Double ref double 1.0
        if (expr.getOp().equals("++")) {
            dadd();
        } else {
            dsub();
        }
        // ref Double(orig) ref double(new)
        invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
        // ref Double(orig) ref Double(new)
        aload(Arities.EXECUTION_CONTEXT);
        // ref Double(orig) ref Double(new) context
        swap();
        // ref Double(orig) ref context Double(new)
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // ref Double(orig)
        swap();
        // Double(orig) ref
        pop();
        // Double(orig)
        go_to(end);

        // ----------------------------------------
        // Invalid
        label(invalid);
        // ref
        append(jsThrowSyntaxError("invalid operation"));

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, PreOpExpression expr, boolean strict) {
        LabelNode storeNewValue = new LabelNode();
        LabelNode doubleNum = new LabelNode();
        LabelNode invalid = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getExpr().accept(context, this, strict);
        // obj
        dup();
        // obj obj
        instance_of(p(Reference.class));
        // ref bool
        iffalse(invalid);
        // ref
        dup();
        // ref ref
        invokevirtual(p(Reference.class), "isValidForPrePostIncrementDecrement", sig(boolean.class));
        // ref bool
        iffalse(invalid);
        // ref
        dup();
        // ref ref
        dup();
        // ref ref ref
        append(jsGetValue());
        // ref ref value
        append(jsToNumber());
        // ref ref number

        dup();
        // ref ref number number
        instance_of(p(Double.class));
        // ref ref number bool
        iftrue(doubleNum);
        // ref ref number

        // ----------------------------------------
        // Integral

        // ref ref number
        invokevirtual(p(Number.class), "longValue", sig(long.class));
        // ref ref long
        ldc(1L);
        // ref ref long 1L
        if (expr.getOp().equals("++")) {
            ladd();
        } else {
            lsub();
        }
        // ref ref long
        invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
        // ref ref Long
        go_to(storeNewValue);

        // ----------------------------------------
        // Double

        label(doubleNum);
        // ref ref number
        invokevirtual(p(Number.class), "doubleValue", sig(double.class));
        // ref ref double
        iconst_1();
        // ref ref double 1
        i2d();
        // ref ref double 1.0
        if (expr.getOp().equals("++")) {
            dadd();
        } else {
            dsub();
        }
        // ref ref double
        invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
        // ref ref Double

        label(storeNewValue);
        // ref ref newval
        aload(Arities.EXECUTION_CONTEXT);
        // ref ref newval context
        swap();
        // ref ref context newval
        invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
        // ref
        append(jsGetValue());
        // value
        go_to(end);

        label(invalid);
        // ref
        append(jsThrowSyntaxError("invalid operation"));
        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, PropertyGet propertyGet, boolean strict) {
        // IN obj
        dup();
        // obj obj
        aload(Arities.EXECUTION_CONTEXT);
        // obj obj context
        ldc(propertyGet.getName());
        // obj obj context name
        invokeinterface(p(JSObject.class), "getOwnProperty", sig(Object.class, ExecutionContext.class, String.class));
        // obj desc(orig)
        compiledFunction(null, EMPTY_STRING_ARRAY, propertyGet.getBlock(), false);
        // obj desc(orig) fn
        ldc(propertyGet.getName());
        // obj desc(orig) fn name
        swap();
        // obj desc(orig) name fn
        invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializerGet", sig(PropertyDescriptor.class, Object.class, String.class,
                JSFunction.class));
        // obj desc(new)
        aload(Arities.EXECUTION_CONTEXT);
        // obj desc(new) context
        swap();
        // obj context desc(new)
        ldc(propertyGet.getName());
        // obj context desc(new) name
        swap();
        // obj context name desc(new)
        iconst_0();
        // obj context name desc(new) 0
        i2b();
        // obj context name desc(new) false
        invokeinterface(p(JSObject.class), "defineOwnProperty",
                sig(boolean.class, ExecutionContext.class, String.class, PropertyDescriptor.class, boolean.class));
        // bool
        pop();
        // <EMPTY>
        return null;
    }

    @Override
    public Object visit(Object context, PropertySet propertySet, boolean strict) {
        // IN obj
        dup();
        // obj obj
        aload(Arities.EXECUTION_CONTEXT);
        // obj obj context
        ldc(propertySet.getName());
        // obj obj context name
        invokeinterface(p(JSObject.class), "getOwnProperty", sig(Object.class, ExecutionContext.class, String.class));
        // obj desc(orig)
        compiledFunction(null, new String[] { propertySet.getIdentifier() }, propertySet.getBlock(), false);
        // obj desc(orig) fn
        ldc(propertySet.getName());
        // obj desc(orig) fn name
        swap();
        // obj desc(orig) name fn
        invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializerSet", sig(PropertyDescriptor.class, Object.class, String.class,
                JSFunction.class));
        // obj desc(new)
        aload(Arities.EXECUTION_CONTEXT);
        // obj desc(new) context
        swap();
        // obj context desc(new)
        ldc(propertySet.getName());
        // obj context desc(new) name
        swap();
        // obj context name desc(new)
        iconst_0();
        // obj context name desc(new) 0
        i2b();
        // obj context name desc(new) false
        invokeinterface(p(JSObject.class), "defineOwnProperty",
                sig(boolean.class, ExecutionContext.class, String.class, PropertyDescriptor.class, boolean.class));
        // bool
        pop();
        // <EMPTY>
        return null;
    }

    @Override
    public Object visit(Object context, NamedValue namedValue, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // obj obj context
        ldc(namedValue.getName());
        // obj obj context name
        namedValue.getExpr().accept(context, this, strict);
        // obj obj context name val
        append(jsGetValue());
        // obj obj context name val
        if (namedValue.getExpr() instanceof FunctionExpression) {
            ldc(namedValue.getName());
            swap();
            invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializer", sig(PropertyDescriptor.class, String.class, Object.class));
        } else {
            invokestatic(p(PropertyDescriptor.class), "newPropertyDescriptorForObjectInitializer", sig(PropertyDescriptor.class, Object.class));
        }
        // obj obj context name desc
        iconst_0();
        // obj obj context name desc 0
        i2b();
        // obj obj context name desc false
        invokeinterface(p(JSObject.class), "defineOwnProperty",
                sig(boolean.class, ExecutionContext.class, String.class, PropertyDescriptor.class, boolean.class));
        // obj bool
        pop();
        // obj
        return null;
    }

    @Override
    public Object visit(Object context, RegexpLiteralExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context
        ldc(expr.getPattern());
        // context pattern
        ldc(expr.getFlags());
        // context pattern flags
        invokestatic(p(BuiltinRegExp.class), "newRegExp", sig(DynRegExp.class, ExecutionContext.class, Object.class, String.class));
        // regexp
        return null;
    }

    @Override
    public Object visit(Object context, RelationalExpression expr, boolean strict) {
        LabelNode returnFalse = new LabelNode();
        LabelNode end = new LabelNode();

        aload(Arities.EXECUTION_CONTEXT);
        expr.getLhs().accept(context, this, strict);
        append(jsGetValue());
        expr.getRhs().accept(context, this, strict);
        append(jsGetValue());
        // context lhs rhs

        if (expr.getOp().equals(">") || expr.getOp().equals("<=")) {
            swap();
            iconst_0();
            i2b();
            // y x false
        } else {
            iconst_1();
            i2b();
        }

        invokestatic(p(Types.class), "compareRelational", sig(Object.class, ExecutionContext.class, Object.class, Object.class, boolean.class));

        // result
        dup();
        // result result

        if (expr.getOp().equals("<") || expr.getOp().equals(">")) {
            // result result
            append(jsPushUndefined());
            // result result UNDEF
            if_acmpeq(returnFalse);
            // result
            go_to(end);
        } else if (expr.getOp().equals("<=") || expr.getOp().equals(">=")) {
            // result result
            append(jsPushUndefined());
            // result result UNDEF
            if_acmpeq(returnFalse);
            // result
            dup();
            // result result
            getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
            // result result TRUE
            if_acmpeq(returnFalse);
            // result(FALSE)
            pop();
            // <empty>
            getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
            // TRUE
            go_to(end);
        }

        // ----------------------------------------
        // FALSE

        label(returnFalse);
        // result
        pop();
        getstatic(p(Boolean.class), "FALSE", ci(Boolean.class));
        go_to(end);

        // ----------------------------------------
        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, ReturnStatement statement, boolean strict) {
        // 12.9
        if (statement.getExpr() == null) {
            append(jsPushUndefined());
        } else {
            statement.getExpr().accept(context, this, strict);
            append(jsGetValue());
        }
        returnCompletion();
        return null;
    }

    @Override
    public Object visit(Object context, StrictEqualityOperatorExpression expr, boolean strict) {
        LabelNode returnTrue = new LabelNode();
        LabelNode returnFalse = new LabelNode();
        LabelNode end = new LabelNode();

        aload(Arities.EXECUTION_CONTEXT);
        // context
        expr.getLhs().accept(context, this, strict);
        // context obj(lhs)
        append(jsGetValue());
        // context val(lhs)
        expr.getRhs().accept(context, this, strict);
        // context val(lhs) obj(rhs)
        append(jsGetValue());
        // context val(lhs) val(rhs)
        invokestatic(p(Types.class), "compareStrictEquality", sig(boolean.class, ExecutionContext.class, Object.class, Object.class));
        // bool

        if (expr.getOp().equals("===")) {
            iftrue(returnTrue);
            go_to(returnFalse);
        } else {
            iffalse(returnTrue);
            go_to(returnFalse);
        }

        label(returnTrue);
        getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
        go_to(end);

        label(returnFalse);
        getstatic(p(Boolean.class), "FALSE", ci(Boolean.class));

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, StringLiteralExpression expr, boolean strict) {
        ldc(expr.getLiteral());
        return null;
    }

    @Override
    public Object visit(Object context, SwitchStatement statement, boolean strict) {
        LabelNode end = new LabelNode();

        normalCompletion();
        // completion
        astore(Arities.COMPLETION);
        // <empty>

        statement.getExpr().accept(context, this, strict);
        // switchref
        append(jsGetValue());
        // switchval

        List<CaseClause> caseClauses = statement.getCaseClauses();
        List<LabelNode> labels = new ArrayList<>();

        int numClauses = caseClauses.size();

        int defaultIndex = -1;

        // switchval
        for (int i = 0; i < numClauses; ++i) {
            CaseClause eachCase = caseClauses.get(i);
            LabelNode caseLabel = new LabelNode();

            labels.add(caseLabel);

            if (eachCase instanceof DefaultCaseClause) {
                defaultIndex = i;
                continue;
            }

            LabelNode notMatched = new LabelNode();

            dup();
            // switchval switchval
            aload(Arities.EXECUTION_CONTEXT);
            // switchval switchval context
            swap();
            // switchval context switchval
            eachCase.getExpression().accept(context, this, strict);
            // switchval context switchval caseref
            append(jsGetValue());
            // switchval context switchval caseval
            invokestatic(p(Types.class), "compareStrictEquality", sig(boolean.class, ExecutionContext.class, Object.class, Object.class));
            // switchval bool
            iffalse(notMatched);
            // switchval
            pop();
            // <empty>
            go_to(caseLabel);

            label(notMatched);
            // switchval
        }

        // switchval
        pop();
        // <empty>
        if (defaultIndex >= 0) {
            go_to(labels.get(defaultIndex));
        } else {
            go_to(end);
        }

        for (int i = 0; i < numClauses; ++i) {
            LabelNode eachLabel = labels.get(i);

            label(eachLabel);
            CaseClause eachCase = caseClauses.get(i);

            invokeCompiledStatementBlock("Case", eachCase.getBlock(), strict);
            // <completion>

            LabelNode normal = new LabelNode();
            LabelNode broke = new LabelNode();
            LabelNode abrupt = new LabelNode();
            LabelNode caseEnd = new LabelNode();

            // completion
            dup();
            // completion completion
            append(handleCompletion(normal, broke, abrupt, abrupt));

            // ----------------------------------------
            // NORMAL
            // ----------------------------------------

            label(normal);
            // completion
            dup();
            // completion completion
            append(jsCompletionValue());
            // completion value
            ifnonnull(caseEnd);
            // completion
            dup();
            // completion completion
            aload(Arities.COMPLETION);
            // completion completion completion(prev)
            append(jsCompletionValue());
            // completion completion value(prev)
            putfield(p(Completion.class), "value", ci(Object.class));
            // completion
            go_to( caseEnd );

            // ----------------------------------------
            // BREAK
            // ----------------------------------------

            label(broke);
            // completion
            convertToNormalCompletion();
            // completion

            // ----------------------------------------
            // ABRUPT
            // ----------------------------------------
            label(abrupt);
            // completion
            astore(Arities.COMPLETION);
            // <empty>
            go_to(end);

            // ----------------------------------------
            // case-end
            // ----------------------------------------

            label(caseEnd);
            // completion
            astore(Arities.COMPLETION);
            // <empty>
        }

        label(end);
        // <empty>
        aload(Arities.COMPLETION);
        // completion
        return null;
    }

    @Override
    public Object visit(Object context, TernaryExpression expr, boolean strict) {
        LabelNode elseBranch = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getTest().accept(context, this, strict);
        // val
        append(jsGetValue());
        // val
        append(jsToBoolean());
        // Boolean
        invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
        // bool
        iffalse(elseBranch);
        // <empty>
        expr.getThenExpr().accept(context, this, strict);
        // thenval
        go_to(end);

        label(elseBranch);
        expr.getElseExpr().accept(context, this, strict);
        // elseval
        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, ThisExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        invokevirtual(p(ExecutionContext.class), "getThisBinding", sig(Object.class));
        return null;
    }

    @Override
    public Object visit(Object context, ThrowStatement statement, boolean strict) {
        statement.getExpr().accept(context, this, strict);
        append(jsGetValue());
        // val
        newobj(p(ThrowException.class));
        // val ex
        dup_x1();
        // ex val ex
        swap();
        // ex ex val
        aload(Arities.EXECUTION_CONTEXT);
        // ex ex val context
        swap();
        // ex ex context val
        invokespecial(p(ThrowException.class), "<init>", sig(void.class, ExecutionContext.class, Object.class));
        // ex
        athrow();
        return null;
    }

    @Override
    public Object visit(Object context, TryStatement statement, boolean strict) {
        LabelNode end = new LabelNode();

        LabelNode tryStart = new LabelNode();
        LabelNode tryEnd = new LabelNode();

        LabelNode outerCatchHandler = new LabelNode();

        label(tryStart);
        invokeCompiledStatementBlock("Try", statement.getTryBlock(), strict);
        label(tryEnd);
        // completion(try)

        if (statement.getFinallyBlock() != null) {
            // completion(try)
            invokeCompiledStatementBlock("Finally", statement.getFinallyBlock(), strict);
            // completion(try) completion(finally)
            dup();
            // completion(try) completion(finally) completion(finally)
            getfield(p(Completion.class), "type", ci(Completion.Type.class));
            // completion(try) completion(finally) type(finally)
            getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
            // completion(try) completion(finally) type(finally) NORMAL

            LabelNode normalFinally = new LabelNode();
            if_acmpeq(normalFinally);

            // ----------------------------------------
            // Abnormal

            // completion(try) completion(finally)
            swap();
            // completion(finally) completion(try)
            pop();
            // completion(finally)
            go_to(end);

            // ----------------------------------------
            // Normal

            label(normalFinally);
            // completion(try) completion(finally)
            pop();
            // completion(try)
        }

        go_to(end);

        trycatch(tryStart, tryEnd, outerCatchHandler, p(ThrowException.class));

        if (statement.getCatchClause() != null) {

            LabelNode catchCatchHandler = new LabelNode();
            LabelNode catchStart = new LabelNode();
            LabelNode catchEnd = new LabelNode();

            label(outerCatchHandler);
            // ex
            invokevirtual(p(ThrowException.class), "getValue", sig(Object.class));
            // thrown
            aload(Arities.EXECUTION_CONTEXT);
            // thrown context
            swap();
            // context thrown
            compiledStatementBlock("Catch", statement.getCatchClause().getBlock(), strict);
            // context thrown catchblock
            swap();
            // context catchblock thrown
            ldc(statement.getCatchClause().getIdentifier());
            // context catchblock thrown ident
            swap();
            // context catchblock ident thrown
            label(catchStart);
            invokevirtual(p(ExecutionContext.class), "executeCatch", sig(Completion.class, BasicBlock.class, String.class, Object.class));
            label(catchEnd);
            // completion(catch)

            if (statement.getFinallyBlock() != null) {
                // completion(catch)
                invokeCompiledStatementBlock("Finally", statement.getFinallyBlock(), strict);
                // completion(catch) completion(finally)
                dup();
                // completion(catch) completion(finally) completion(finally)
                getfield(p(Completion.class), "type", ci(Completion.Type.class));
                // completion(catch) completion(finally) type(finally)
                getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                // completion(catch) completion(finally) type(finally) NORMAL

                LabelNode normalFinally = new LabelNode();
                if_acmpeq(normalFinally);

                // ----------------------------------------
                // Abnormal

                // completion(catch) completion(finally)
                swap();
                // completion(finally) completion(catch)
                pop();
                // completion(finally)
                go_to(end);

                // ----------------------------------------
                // Normal

                label(normalFinally);
                // completion(catch) completion(finally)
                pop();
                // completion(catch)
                go_to(end);

                // ----------------------------------------
                // IN CASE CATCH ITSELF THROWS
                // ----------------------------------------

                LabelNode normalFinallyAfterThrow = new LabelNode();

                trycatch(catchStart, catchEnd, catchCatchHandler, null);
                label(catchCatchHandler);
                // ex

                invokeCompiledStatementBlock("Finally", statement.getFinallyBlock(), strict);
                // ex completion(finally)
                dup();
                // ex completion(finally) completion(finally)
                getfield(p(Completion.class), "type", ci(Completion.Type.class));
                // ex completion(finally) type(finally)
                getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                // ex completion(finally) type(finally) NORMAL
                if_acmpeq(normalFinallyAfterThrow);
                // ex completion(finally)
                swap();
                // completion(finally) ex
                pop();
                // completion(finally)
                go_to(end);

                label(normalFinallyAfterThrow);
                // ex completion(finally)
                pop();
                // ex
                athrow();
            }
        } else {
            // No catch, but thrown
            label(outerCatchHandler);
            // ex
            if (statement.getFinallyBlock() != null) {
                // ex
                invokeCompiledStatementBlock("Finally", statement.getFinallyBlock(), strict);
                // ex completion(finally)
                dup();
                // ex completion(finally) completion(finally)
                getfield(p(Completion.class), "type", ci(Completion.Type.class));
                // ex completion(finally) type(finally)
                getstatic(p(Completion.Type.class), "NORMAL", ci(Completion.Type.class));
                // ex completion(finally) type(finally) NORMAL

                LabelNode normalFinally = new LabelNode();
                if_acmpeq(normalFinally);

                // ----------------------------------------
                // Abnormal

                // ex completion(finally)
                swap();
                // completion(finally) ex
                pop();
                // completion(finally)
                go_to(end);

                // ----------------------------------------
                // Normal

                label(normalFinally);
                // ex completion(finally)
                pop();
                // ex
                athrow();
                // <THROWN>
            }

        }

        label(end);
        // completion
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, TypeOfOpExpression expr, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context
        expr.getExpr().accept(context, this, strict);
        // context obj
        invokestatic(p(Types.class), "typeof", sig(String.class, ExecutionContext.class, Object.class));
        // string
        return null;
    }

    @Override
    public Object visit(Object context, UnaryMinusExpression expr, boolean strict) {
        LabelNode doubleNum = new LabelNode();
        LabelNode zero = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getExpr().accept(context, this, strict);
        // val
        append(jsGetValue());
        // val
        append(jsToNumber());
        // num
        dup();
        // num num
        instance_of(p(Double.class));
        // num bool
        iftrue(doubleNum);

        // num
        dup();
        // num num
        invokevirtual(p(Number.class), "longValue", sig(long.class));
        // num long
        ldc(0L);
        // num long 0L
        lcmp();
        // num bool
        iffalse(zero);

        // ------------------------------------
        // Integral

        // num(Long)
        invokevirtual(p(Number.class), "longValue", sig(long.class));
        // num(long)

        ldc(-1L);
        // num -1L
        lmul();
        // -num
        invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
        // -num(Long)

        go_to(end);

        // ------------------------------------
        // Double

        label(doubleNum);
        // num(Doube)
        invokevirtual(p(Number.class), "doubleValue", sig(double.class));
        // num(double)
        iconst_m1();
        // num -1
        i2d();
        // num -1.0
        dmul();
        // -num
        invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
        // -num(Double)

        go_to(end);

        // ------------------------------------

        label(zero);
        // num
        pop();
        ldc(-0.0);
        invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, UnaryPlusExpression expr, boolean strict) {
        // 11.4.6
        expr.getExpr().accept(context, this, strict);
        // val
        append(jsGetValue());
        // val
        append(jsToNumber());
        return null;
    }

    @Override
    public Object visit(Object context, VariableDeclaration expr, boolean strict) {
        if (expr.getExpr() == null) {
            ldc(expr.getIdentifier());
            // str
        } else {
            append(jsResolve(expr.getIdentifier()));
            // reference
            aload(Arities.EXECUTION_CONTEXT);
            // reference context
            expr.getExpr().accept(context, this, strict);
            // reference context val
            append(jsGetValue());
            // reference context val
            invokevirtual(p(Reference.class), "putValue", sig(void.class, ExecutionContext.class, Object.class));
            // reference
            ldc(expr.getIdentifier());
            // str
        }
        return null;
    }

    @Override
    public Object visit(Object context, VariableStatement statement, boolean strict) {
        for (VariableDeclaration each : statement.getVariableDeclarations()) {
            each.accept(context, this, strict);
            // identifier
            pop();
            // <EMPTY>
        }
        normalCompletion();
        return null;
    }

    @Override
    public Object visit(Object context, VoidOperatorExpression expr, boolean strict) {
        expr.getExpr().accept(context, this, strict);
        append(jsGetValue());
        pop();
        append(jsPushUndefined());
        return null;
    }

    @Override
    public Object visit(Object context, WhileStatement statement, boolean strict) {
        LabelNode end = new LabelNode();
        LabelNode breakTarget = new LabelNode();
        LabelNode continueTarget = new LabelNode();
        LabelNode begin = new LabelNode();

        normalCompletion();
        // completion(block)

        label(begin);
        statement.getTest().accept(context, this, strict);
        // completion(block) result
        append(jsGetValue());
        // completion(block) result
        append(jsToBoolean());
        // completion(block) Boolean
        invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
        // completion(block) bool

        iffalse(end);
        // completion(block)

        invokeCompiledStatementBlock("Do", statement.getBlock(), strict);
        // completion(block,prev) completion(block,cur)
        swap();
        // completion(block,cur) completion(block,prev)
        pop();
        // completion(block,cur)

        dup();
        // completion(block) completion(block)
        append(handleCompletion(begin, breakTarget, continueTarget, end));

        // completion(block)

        // ----------------------------------------
        // BREAK
        label(breakTarget);
        // completion(block,BREAK)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        convertToNormalCompletion();
        // completion(block,NORMAL)
        go_to(end);

        // ----------------------------------------
        // CONTINUE

        label(continueTarget);
        // completion(block,CONTINUE)
        dup();
        // completion completion
        append(jsCompletionTarget());
        // completion target
        append(statement.isInLabelSet());
        // completion bool
        iffalse(end);
        // completion
        go_to(begin);

        // ----------------------------------------
        label(end);
        // completion(block)
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, WithStatement statement, boolean strict) {
        aload(Arities.EXECUTION_CONTEXT);
        // context
        statement.getExpr().accept(context, this, strict);
        // context val
        append(jsGetValue());
        // context val
        append(jsToObject());
        // context obj
        compiledStatementBlock("With", statement.getBlock(), strict);
        // context obj block
        invokevirtual(p(ExecutionContext.class), "executeWith", sig(Completion.class, JSObject.class, BasicBlock.class));
        // completion
        return null;
    }

}
