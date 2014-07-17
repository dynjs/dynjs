package org.dynjs.codegen;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.io.PrintStream;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.compiler.CompilationContext;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.exception.ThrowException;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.AdditiveExpression;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.Completion.Type;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.interp.InterpretedBasicBlock;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;
import me.qmx.jitescript.internal.org.objectweb.asm.tree.LabelNode;

public abstract class CodeGeneratingVisitor extends CodeBlock implements CodeVisitor {

    public static interface Arities {
        int THIS = 0;
        int EXECUTION_CONTEXT = 1;
        int COMPLETION = 2;
    }

    private InterpretingVisitorFactory interpFactory;
    private BlockManager blockManager;

    public CodeGeneratingVisitor(InterpretingVisitorFactory interpFactory, BlockManager blockManager) {
        this.interpFactory = interpFactory;
        this.blockManager = blockManager;
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    protected void emitDebug(String message) {
        ldc(message);
        aprintln();
        pop();
    }

    protected void emitTop() {
        aprintln();
    }

    public CodeBlock aprintln() {
        dup();
        getstatic(p(System.class), "err", ci(PrintStream.class));
        swap();
        invokevirtual(p(PrintStream.class), "println", sig(void.class, params(Object.class)));
        return this;
    }

    @Override
    public Object visit(Object context, AdditiveExpression expr, boolean strict) {
        if (expr.getOp().equals("+")) {
            return visitPlus((ExecutionContext) context, expr, strict);
        } else {
            return visitMinus((ExecutionContext) context, expr, strict);
        }
    }

    public abstract Object visitPlus(ExecutionContext context, AdditiveExpression expr, boolean strict);

    public abstract Object visitMinus(ExecutionContext context, AdditiveExpression expr, boolean strict);

    public CodeBlock jsCheckObjectCoercible(final String debug) {
        CodeBlock codeBlock = new CodeBlock()
            // IN: obj
            .dup()
            // obj obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj obj context
            .swap();
        // obj context obj
        if (debug != null) {
            codeBlock.ldc(debug);
        } else {
            codeBlock.aconst_null();
        }
        codeBlock.invokestatic(p(Types.class), "checkObjectCoercible", sig(void.class, ExecutionContext.class, Object.class, String.class));
        // obj
        return codeBlock;
    }

    public CodeBlock jsResolve(final String identifier) {
        return new CodeBlock()
            // <EMPTY>
            .aload(Arities.EXECUTION_CONTEXT)
            .ldc(identifier)
            .invokevirtual(p(ExecutionContext.class), "resolve", sig(Reference.class, String.class));
            // reference
    }

    public CodeBlock jsPushUndefined() {
        return new CodeBlock()
            .getstatic(p(Types.class), "UNDEFINED", ci(Types.Undefined.class));
    }

    public CodeBlock jsPushNull() {
        return new CodeBlock()
            .getstatic(p(Types.class), "NULL", ci(Types.Null.class));
    }

    public CodeBlock jsToPrimitive() {
        return new CodeBlock()
            // IN: obj preferredType
            .aload(Arities.EXECUTION_CONTEXT)
            // obj preferredType context
            .dup_x2()
            // context obj preferredType context
            .pop()
            // context obj preferredType
            .invokestatic(p(Types.class), "toPrimitive", sig(Object.class, ExecutionContext.class, Object.class, String.class));
            // obj
    }

    public CodeBlock jsToNumber() {
        return new CodeBlock()
            // IN obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj context
            .swap()
            // context obj
            .invokestatic(p(Types.class), "toNumber", sig(Number.class, ExecutionContext.class, Object.class));
            // obj
    }

    public CodeBlock jsToBoolean() {
        return new CodeBlock()
            // IN obj
            .invokestatic(p(Types.class), "toBoolean", sig(Boolean.class, Object.class));
            // obj
    }

    public CodeBlock jsToInt32() {
        return new CodeBlock()
            // IN obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj context
            .swap()
            // context obj
            .invokestatic(p(Types.class), "toInt32", sig(Long.class, ExecutionContext.class, Object.class));
            // obj
    }

    public CodeBlock jsToUint32() {
        return new CodeBlock()
            // IN obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj context
            .swap()
            // context obj
            .invokestatic(p(Types.class), "toUint32", sig(Long.class, ExecutionContext.class, Object.class));
            // obj
    }

    public CodeBlock jsToObject() {
        return new CodeBlock()
            // IN obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj context
            .swap()
            // context object
            .invokestatic(p(Types.class), "toObject", sig(JSObject.class, ExecutionContext.class, Object.class));
            // obj
    }

    public CodeBlock jsGetValue() {
        return jsGetValue(null);
    }

    public abstract CodeBlock jsGetValue(final Class<?> throwIfNot);

    public CodeBlock jsGetBase() {
        return new CodeBlock()
            // IN: reference
            // reference
            .invokevirtual(p(Reference.class), "getBase", sig(Object.class));
            // value
    }

    public CodeBlock jsToString() {
        return new CodeBlock()
            // IN: obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj context
            .swap()
            // context obj
            .invokestatic(p(Types.class), "toString", sig(String.class, ExecutionContext.class, Object.class));
    }

    public CodeBlock jsCreatePropertyReference() {
        return new CodeBlock()
            // IN: context obj identifier
            .invokevirtual(p(ExecutionContext.class), "createPropertyReference", sig(Reference.class, Object.class, String.class));
    }

    public CodeBlock jsThrowTypeError(final String message) {
        return new CodeBlock()
            .newobj(p(ThrowException.class))
            // obj
            .dup()
            // obj obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj obj context
            .ldc(message)
            // obj obj context message
            .invokevirtual(p(ExecutionContext.class), "createTypeError", sig(JSObject.class, String.class))
            // obj obj ex
            .aload(Arities.EXECUTION_CONTEXT)
            // obj obj ex context
            .swap()
            // obj obj context ex
            .invokespecial(p(ThrowException.class), "<init>", sig(void.class, ExecutionContext.class, Object.class))
            // obj
            .athrow();
    }

    public CodeBlock jsThrowReferenceError(final String message) {
        return new CodeBlock()
            .newobj(p(ThrowException.class))
            // obj
            .dup()
            // obj obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj obj context
            .ldc(message)
            // obj obj context message
            .invokevirtual(p(ExecutionContext.class), "createReferenceError", sig(JSObject.class, String.class))
            // obj obj ex
            .aload(Arities.EXECUTION_CONTEXT)
            // obj obj ex context
            .swap()
            // obj obj context ex
            .invokespecial(p(ThrowException.class), "<init>", sig(void.class, ExecutionContext.class, Object.class))
            // obj
            .athrow();
    }

    public CodeBlock jsThrowSyntaxError(final String message) {
        return new CodeBlock()
            .newobj(p(ThrowException.class))
            // obj
            .dup()
            // obj obj
            .aload(Arities.EXECUTION_CONTEXT)
            // obj obj context
            .ldc(message)
            // obj obj context message
            .invokevirtual(p(ExecutionContext.class), "createSyntaxError", sig(JSObject.class, String.class))
            // obj obj ex
            .aload(Arities.EXECUTION_CONTEXT)
            // obj obj ex context
            .swap()
            // obj obj context ex
            .invokespecial(p(ThrowException.class), "<init>", sig(void.class, ExecutionContext.class, Object.class))
            // obj
            .athrow();
    }

    public CodeBlock ifEitherIsDouble(final LabelNode target) {
        // IN: Number Number
        return new CodeBlock()
            .checkcast(p(Number.class))
            .swap()
            // val(rhs) Number(lhs)
            .checkcast(p(Number.class))
            .swap()
            // Number(lhs) Number(rhs)
            .dup()
            // Number(lhs) Number(rhs) Number(rhs)
            .instance_of(p(Double.class))
            // Number(lhs) Number(rhs) bool
            .iftrue(target)
            // Number(lhs) Number(rhs)
            .swap()
            // Number(rhs) Number(lhs)
            .dup_x1()
            // Number(lhs) Number(rhs) Number(lhs)
            .instance_of(p(Double.class))
            // Number(lhs) Number(rhs) bool
            .iftrue(target);
            // Number(lhs) Number(rhs)
    }

    public CodeBlock ifEitherIsNaN(final LabelNode target) {
        // IN: Number Number
        return new CodeBlock()
            // Number(x) Number(y)
            .checkcast(p(Number.class))
            // val(x) Number(y)
            .dup_x1()
            // Number(y) val(x) Number(y)
            .swap()
            // Number(y) Number(y) val(x)
            .checkcast(p(Number.class))
            // Number(y) Number(y) Number(x)
            .dup_x2()
            // Number(x) Number(y) Number(y) Number(x)
            .swap()
            // Number(x) Number(y) Number(x) Number(y)
            .invokestatic(p(CodeGeneratingVisitor.class), "isEitherNaN", sig(boolean.class, Number.class, Number.class))
            // Number(x) Number(y) bool
            .iftrue(target);
            // Number(x) Number(y)
    }

    public CodeBlock ifTopIsZero(final LabelNode target) {
        // IN: Number
        return new CodeBlock()
            // Number
            .checkcast(p(Number.class))
            // Number
            .dup()
            // Number Number
            .invokestatic(p(CodeGeneratingVisitor.class), "isZero", sig(boolean.class, Number.class))
            // Number bool
            .iftrue(target);
            // Number
    }

    public static boolean isEitherNaN(Number lhs, Number rhs) {
        return (Double.isNaN(lhs.doubleValue()) || Double.isNaN(rhs.doubleValue()));
    }

    public static boolean isZero(Number num) {
        return (num.doubleValue() == 0.0);
    }

    public CodeBlock ifBothAreString(final LabelNode target) {
        LabelNode end = new LabelNode();
        return new CodeBlock()
            // IN: obj(lhs) obj(rhs)
            .dup()
            // obj(lhs) obj(rhs) obj(rhs)
            .instance_of(p(String.class))
            // obj(lhs) obj(rhs) bool(rhs)
            .iffalse(end)
            // obj(lhs) obj(rhs)
            .swap()
            // obj(rhs) obj(lhs)
            .dup_x1()
            // obj(lhs) obj(rhs) obj(lhs)
            .instance_of(p(String.class))
            // obj(lhs) obj(rhs) bool(lhs)
            .iftrue(target)
            // obj(lhs) obj(rhs)
            .label(end);
            // obj(lhs) obj(rhs)
    }

    public CodeBlock convertTopTwoToPrimitiveLongs() {
        return new CodeBlock()
            // IN: Number Number
            .invokevirtual(p(Number.class), "longValue", sig(long.class))
            // Number(lhs) long(rhs)
            .dup2_x1()
            // long(rhs) Number(lhs) long(rhs)
            .pop2()
            // long(rhs) Number(lhs)
            .invokevirtual(p(Number.class), "longValue", sig(long.class))
            // long(rhs) long(lhs)
            .dup2_x2()
            // long(lhs) long(rhs) long(lhs);
            .pop2();
            // long(lhs) long(rhs)
    }

    public CodeBlock convertTopToLong() {
        return new CodeBlock()
            // IN: int
            .invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
    }

    public CodeBlock convertTopToInteger() {
        return new CodeBlock()
            // IN: int
            .invokestatic(p(Integer.class), "valueOf", sig(Integer.class, int.class));
    }

    public CodeBlock convertTopTwoToPrimitiveDoubles() {
        return new CodeBlock()
            // IN Number Number
            .checkcast(p(Number.class))
            .swap()
            .checkcast(p(Number.class))
            .swap()
            .invokevirtual(p(Number.class), "doubleValue", sig(double.class))
            // Number(lhs) double(rhs)
            .dup2_x1()
            // double(rhs) Number(lhs) double(rhs);
            .pop2()
            // double(rhs) Number(lhs)
            .invokevirtual(p(Number.class), "doubleValue", sig(double.class))
            // double(rhs) double(lhs)
            .swap2();
            // OUT double double
    }

    public CodeBlock convertTopToDouble() {
        return new CodeBlock()
            // IN: int
            .invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
    }

    // ----------------------------------------

    public CodeBlock jsCompletionValue() {
        return new CodeBlock()
            // IN completion
            .getfield(p(Completion.class), "value", ci(Object.class));
            // value
    }

    public CodeBlock handleCompletion(
            final LabelNode normalTarget,
            final LabelNode breakTarget,
            final LabelNode continueTarget,
            final LabelNode returnTarget) {
        return new CodeBlock()
            // IN: completion
            .append(jsCompletionType())
            .lookupswitch(normalTarget,
                         new int[] { Type.NORMAL.ordinal(), Type.BREAK.ordinal(), Type.CONTINUE.ordinal(), Type.RETURN.ordinal() },
                         new LabelNode[] { normalTarget, breakTarget, continueTarget, returnTarget });
    }

    public CodeBlock jsCompletionTarget() {
        return new CodeBlock()
            // IN completion
            .getfield(p(Completion.class), "target", ci(String.class));
            // value
    }

    public CodeBlock jsCompletionType() {
        return new CodeBlock()
            // IN completion
            .getfield(p(Completion.class), "type", ci(Completion.Type.class))
            // type
            .invokevirtual(p(Completion.Type.class), "ordinal", sig(int.class));
    }

    public void breakCompletion(final String target) {
        // <EMPTY>
        if (target == null) {
            aconst_null();
        } else {
            ldc(target);
        }
        // target
        invokestatic(p(Completion.class), "createBreak", sig(Completion.class, String.class));
        // completion
    }

    public void continueCompletion(final String target) {
        // <EMPTY>
        if (target == null) {
            aconst_null();
        } else {
            ldc(target);
        }
        // target
        invokestatic(p(Completion.class), "createContinue", sig(Completion.class, String.class));
        // completion
    }

    public void normalCompletion() {
        invokestatic(p(Completion.class), "createNormal", sig(Completion.class));
    }

    public void normalCompletionWithValue() {
        // IN: val
        invokestatic(p(Completion.class), "createNormal", sig(Completion.class, Object.class));
    }

    public void returnCompletion() {
        invokestatic(p(Completion.class), "createReturn", sig(Completion.class, Object.class));
    }

    public void convertToNormalCompletion() {
        // IN: completion
        dup();
        // completion completion
        getstatic(p(Completion.Type.class), "NORMAL", ci(Type.class));
        // completion completion NORMAL
        putfield(p(Completion.class), "type", ci(Type.class));
        // completion
    }

    public static void injectLineNumber(CodeBlock block, Statement statement) {
        Position position = statement.getPosition();
        if (position != null) {
            LabelNode lineLabel = new LabelNode();
            block.line(position.getLine(), lineLabel);
            block.label(lineLabel);
        }
    }

    public void invokeCompiledStatementBlock(final String grist, final Statement block, final boolean strict) {
        compiledStatementBlock(grist, block, strict);
        // basic-block
        aload(Arities.EXECUTION_CONTEXT);
        // basic-block context
        // invokevirtual( p( BasicBlock.class ), "call", sig( Completion.class, ExecutionContext.class ) );
        invokeinterface(p(BasicBlock.class), "call", sig(Completion.class, ExecutionContext.class));
        // completion
    }

    public void compiledStatementBlock(final String grist, final Statement block, final boolean strict) {
        int statementNumber = block.getStatementNumber();
        Entry entry = blockManager.retrieve(statementNumber);

        // Stash statement if required
        if (entry.statement == null) {
            entry.statement = block;
        }

        // ----------------------------------------
        // ----------------------------------------

        aload(Arities.EXECUTION_CONTEXT);
        // context
        ldc(statementNumber);
        // context statement-num
        invokevirtual(p(ExecutionContext.class), "retrieveBlockEntry", sig(Entry.class, int.class));
        // entry

        aload(Arities.EXECUTION_CONTEXT);
        // entry context

        invokevirtual(p(ExecutionContext.class), "getCompiler", sig(JSCompiler.class));
        // entry compiler

        swap();
        // compiler entry

        aload(Arities.EXECUTION_CONTEXT);
        // compiler entry context

        swap();
        // compiler context entry

        ldc(grist);
        // compiler context entry grist

        swap();
        // compiler context grist entry

        getfield(p(Entry.class), "statement", ci(Statement.class));
        // compiler context grist statement
        
        if ( strict ) {
            iconst_1();
        } else {
            iconst_0();
        }
        
        // compiler context grist statement strict

        invokevirtual(p(JSCompiler.class), "compileBasicBlock", sig(BasicBlock.class, ExecutionContext.class, String.class, Statement.class, boolean.class));
        // basic-block

    }

    public void compiledFunction(final String identifier, final String[] formalParams, final Statement block, final boolean strict) {

        int statementNumber = block.getStatementNumber();
        Entry entry = blockManager.retrieve(statementNumber);

        // Stash statement if required
        if (entry.statement == null) {
            entry.statement = block;
        }

        // ----------------------------------------
        // ----------------------------------------

        aload(Arities.EXECUTION_CONTEXT);
        // context
        ldc(statementNumber);
        // context statement-number
        
        invokevirtual(p(ExecutionContext.class), "retrieveBlockEntry", sig(Entry.class, int.class));
        // entry
        
        aload(Arities.EXECUTION_CONTEXT);
        // entry context

        invokevirtual(p(ExecutionContext.class), "getCompiler", sig(JSCompiler.class));
        // entry compiler

        swap();
        // compiler entry

        getfield(p(Entry.class), "statement", ci(Statement.class));
        // compiler statement

        aload(Arities.EXECUTION_CONTEXT);
        // compiler statement context
        swap();
        // compiler context statement

        if (identifier != null) {
            ldc(identifier);
        } else {
            aconst_null();
        }
        // compiler context statement identifier
        swap();
        // compiler context identifier statement

        checkcast(p(BlockStatement.class));

        bipush(formalParams.length);
        // compiler context identifier statement params-en
        anewarray(p(String.class));
        // compiler context identifier statement params

        for (int i = 0; i < formalParams.length; ++i) {
            dup();
            bipush(i);
            ldc(formalParams[i]);
            aastore();
        }
        // compiler context identifier statement params
        swap();
        // compiler context identifier params statement

        if (strict) {
            iconst_1();
        } else {
            iconst_0();
        }

        // compiler context identifer params statement bool

        invokevirtual(p(JSCompiler.class), "compileFunction", sig(JSFunction.class, CompilationContext.class, String.class, String[].class, Statement.class, boolean.class));
        // fn
    }

    void interpretedStatement(Statement statement, boolean strict) {
        Entry entry = getBlockManager().retrieve(statement.getStatementNumber());
        InterpretedBasicBlock interpreted = new InterpretedBasicBlock(this.interpFactory, statement, strict);
        entry.setCompiled(interpreted);

        aload(Arities.EXECUTION_CONTEXT);
        ldc(statement.getStatementNumber());
        invokevirtual(p(ExecutionContext.class), "retrieveBlockEntry", sig(Entry.class, int.class));
        invokevirtual(p(Entry.class), "getCompiled", sig(BasicBlock.class));
        // object
        aload(Arities.EXECUTION_CONTEXT);
        // obj context
        invokeinterface(p(BasicBlock.class), "call", sig(Completion.class, ExecutionContext.class));
        // completion
    }

}
