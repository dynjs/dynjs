package org.dynjs.codegen;

import static me.qmx.jitescript.util.CodegenUtils.*;

import java.util.List;

import me.qmx.jitescript.CodeBlock;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.ast.AssignmentExpression;
import org.dynjs.parser.ast.Expression;
import org.dynjs.parser.ast.FunctionCallExpression;
import org.dynjs.parser.ast.NewOperatorExpression;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.EnvironmentRecord;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import me.qmx.jitescript.internal.org.objectweb.asm.tree.LabelNode;

public class InvokeDynamicBytecodeGeneratingVisitor extends BasicBytecodeGeneratingVisitor {

    public InvokeDynamicBytecodeGeneratingVisitor(InterpretingVisitorFactory interpFactory, BlockManager blockManager) {
        super(interpFactory, blockManager);
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
            ldc(expr.getIdentifier());
            // reference context name
            expr.getExpr().accept(context, this, strict);
            // reference context name val
            append(jsGetValue());
            // reference context name val
            invokedynamic("dyn:setProperty", sig(void.class, Object.class, ExecutionContext.class, String.class, Object.class), DynJSBootstrapper.HANDLE,
                    DynJSBootstrapper.ARGS);
            // <empty>
            ldc(expr.getIdentifier());
            // str
        }
        return null;
    }

    @Override
    public Object visit(Object context, AssignmentExpression expr, boolean strict) {
        LabelNode throwRefError = new LabelNode();
        LabelNode end = new LabelNode();

        expr.getLhs().accept(context, this, strict);
        // ref
        dup();
        // ref ref
        instance_of(p(Reference.class));
        // ref bool
        iffalse(throwRefError);
        // ref
        checkcast(p(Reference.class));
        // ref

        expr.getRhs().accept(context, this, strict);
        // ref expr
        append(jsGetValue());
        // ref value
        dup_x1();
        // value ref value
        swap();
        // value value ref
        dup_x1();
        // value ref value ref
        invokevirtual(p(Reference.class), "getReferencedName", sig(String.class));
        // value ref value name
        dup_x1();
        // value ref name value name
        pop();
        // value ref name value
        aload(Arities.EXECUTION_CONTEXT);
        // value ref name value context
        dup_x2();
        // value ref context name value context
        pop();
        // value ref context name value
        invokedynamic("dyn:setProperty", sig(void.class, Object.class, ExecutionContext.class, String.class, Object.class), DynJSBootstrapper.HANDLE,
                DynJSBootstrapper.ARGS);
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
    public Object visit(Object context, NewOperatorExpression expr, boolean strict) {
        LabelNode end = new LabelNode();
        // 11.2.2

        expr.getExpr().accept(context, this, strict);
        // obj
        aload(Arities.EXECUTION_CONTEXT);
        // obj context
        swap();
        // context obj
        append(jsGetValue());
        // context ctor-fn
        swap();
        // ctor-fn context

        List<Expression> argExprs = expr.getArgumentExpressions();
        int numArgs = argExprs.size();
        bipush(numArgs);
        anewarray(p(Object.class));
        // ctor-fn context array
        for (int i = 0; i < numArgs; ++i) {
            dup();
            bipush(i);

            argExprs.get(i).accept(context, this, strict);
            append(jsGetValue());
            aastore();
        }
        // ctor-fn context array

        invokedynamic("dyn:construct", sig(Object.class, Object.class, ExecutionContext.class, Object[].class), DynJSBootstrapper.HANDLE, DynJSBootstrapper.ARGS);
        // obj

        label(end);
        nop();
        return null;
    }

    @Override
    public Object visit(Object context, FunctionCallExpression expr, boolean strict) {
        LabelNode propertyRef = new LabelNode();
        LabelNode noSelf = new LabelNode();
        LabelNode doCall = new LabelNode();
        // 11.2.3

        expr.getMemberExpression().accept(context, this, strict);
        // fnexpr
        dup();
        // fnexpr fnexpr
        /*
         * aload(Arities.EXECUTION_CONTEXT);
         * fnexpr fnexpr context
         * invokestatic(p(DereferencedReference.class), "create", sig(Object.class, Object.class, Object.class));
         */
        dup();
        // fnexpr fnexpr fnexpr
        append(jsGetValue());
        // fnexpr fn-ref fn
        invokestatic(p(DereferencedReference.class), "create", sig(Object.class, Object.class, Object.class));
        // fnexpr fn
        swap();
        // fn fnexpr
        dup();
        // fn fnexpr fnexpr
        instance_of(p(Reference.class));
        // fn fnexpr isref?
        iffalse(noSelf);

        // ----------------------------------------
        // Reference

        // fn ref
        checkcast(p(Reference.class));
        // fn ref
        dup();
        // fn ref ref
        invokevirtual(p(Reference.class), "isPropertyReference", sig(boolean.class));
        // fn ref bool(is-prop)

        iftrue(propertyRef);

        // ----------------------------------------
        // Environment Record

        // fn ref
        invokevirtual(p(Reference.class), "getBase", sig(Object.class));
        // fn base
        checkcast(p(EnvironmentRecord.class));
        // fn env-rec
        invokeinterface(p(EnvironmentRecord.class), "implicitThisValue", sig(Object.class));
        // fn self
        go_to(doCall);

        // ----------------------------------------
        // Property Reference
        label(propertyRef);
        // fn ref
        append(jsGetBase());
        // fn self
        go_to(doCall);

        // ------------------------------------------
        // No self
        label(noSelf);
        // fn fnexpr
        pop();
        // fn
        append(jsPushUndefined());
        // fn UNDEFINED

        // ------------------------------------------
        // call()

        label(doCall);
        // fn self

        aload(Arities.EXECUTION_CONTEXT);
        // fn self context

        swap();
        // fn context self

        List<Expression> argExprs = expr.getArgumentExpressions();
        int numArgs = argExprs.size();
        bipush(numArgs);
        anewarray(p(Object.class));
        // fn context self array
        for (int i = 0; i < numArgs; ++i) {
            dup();
            bipush(i);

            argExprs.get(i).accept(context, this, strict);
            append(jsGetValue());
            aastore();
        }
        // fn context self array

        // function context ref self args
        invokedynamic("dyn:call", sig(Object.class, Object.class, ExecutionContext.class, Object.class, Object[].class), DynJSBootstrapper.HANDLE,
                DynJSBootstrapper.ARGS);
        // value
        return null;
    }

    /*
     * @Override
     * public void visit(ExecutionContext context, AssignmentExpression expr, boolean strict) {
     * LabelNode throwRefError = new LabelNode();
     * LabelNode end = new LabelNode();
     * 
     * LabelNode isUnresolvableRef = new LabelNode();
     * LabelNode isPropertyRef = new LabelNode();
     * LabelNode isEnvRecord = new LabelNode();
     * LabelNode doPut = new LabelNode();
     * 
     * expr.getLhs().accept(context, this, strict);
     * // reference
     * dup();
     * // reference reference
     * instance_of(p(Reference.class));
     * // reference bool
     * iffalse(throwRefError);
     * // reference
     * checkcast(p(Reference.class));
     * // ref
     * dup();
     * // ref ref
     * invokevirtual(p(Reference.class), "isUnresolvableReference", sig(boolean.class));
     * // ref unresolv?
     * iftrue(isUnresolvableRef);
     * // ref
     * dup();
     * // ref ref
     * invokevirtual(p(Reference.class), "isPropertyReference", sig(boolean.class));
     * // ref isprop?
     * iftrue(isPropertyRef);
     * // ref
     * go_to(isEnvRecord);
     * 
     * // ----------------------------------------
     * // unresolvable ref
     * // ----------------------------------------
     * 
     * label(isUnresolvableRef);
     * // ref
     * dup();
     * // ref ref
     * invokevirtual(p(Reference.class), "isStrictReference", sig(boolean.class));
     * // ref isstrict?
     * iftrue(throwRefError);
     * // ref
     * aload(Arities.EXECUTION_CONTEXT);
     * // ref context
     * invokevirtual(p(ExecutionContext.class), "getGlobalContext", sig(GlobalObject.class));
     * // ref obj
     * go_to(doPut);
     * 
     * // ----------------------------------------
     * // property ref
     * // ----------------------------------------
     * 
     * label( isPropertyRef );
     * // ref
     * dup();
     * // ref ref
     * invokevirtual(p(Reference.class), "getBase", sig(Object.class));
     * // ref obj
     * go_to( doPut );
     * 
     * // ----------------------------------------
     * // property ref
     * // ----------------------------------------
     * 
     * label( isEnvRecord );
     * // ref
     * dup();
     * // ref ref
     * invokevirtual(p(Reference.class), "getBase", sig(Object.class));
     * // ref obj
     * go_to( doPut );
     * 
     * 
     * label( doPut );
     * // ref obj
     * swap();
     * // obj ref
     * dup();
     * // obj ref ref
     * aload(Arities.EXECUTION_CONTEXT);
     * // obj ref ref context
     * invokestatic(p(ReferenceContext.class), "create", sig(ReferenceContext.class, Reference.class, ExecutionContext.class));
     * // obj ref context
     * swap();
     * // obj context ref
     * invokevirtual(p(Reference.class), "getReferencedName", sig(String.class));
     * // obj context name
     * expr.getRhs().accept(context, this, strict);
     * // obj context name value
     * append(jsGetValue());
     * // object context name value
     * invokedynamic("dyn:setProp", sig(Object.class, Object.class, ReferenceContext.class, String.class, Object.class), DynJSBootstrapper.HANDLE, DynJSBootstrapper.ARGS);
     * // value
     * go_to(end);
     * 
     * label(throwRefError);
     * // reference
     * pop();
     * 
     * newobj(p(ThrowException.class));
     * // ex
     * dup();
     * // ex ex
     * aload(Arities.EXECUTION_CONTEXT);
     * // ex ex context
     * ldc(expr.getLhs().toString() + " is not a reference");
     * // ex ex context str
     * invokevirtual(p(ExecutionContext.class), "createReferenceError", sig(JSObject.class, String.class));
     * // ex ex error
     * aload(Arities.EXECUTION_CONTEXT);
     * // ex ex error context
     * swap();
     * // ex ex context error
     * invokespecial(p(ThrowException.class), "<init>", sig(void.class, ExecutionContext.class, Object.class));
     * // ex ex
     * athrow();
     * 
     * label(end);
     * nop();
     * 
     * }
     */

    @Override
    public CodeBlock jsGetValue(final Class<?> throwIfNot) {
        LabelNode end = new LabelNode();
        LabelNode throwRef = new LabelNode();
        CodeBlock codeBlock = new CodeBlock()
            // IN: reference
            .dup()
            // ref ref
            .instance_of(p(Reference.class))
            // ref isref?
            .iffalse(end)
            .checkcast(p(Reference.class))
            // ref
            .dup()
            // ref ref
            .invokevirtual(p(Reference.class), "isUnresolvableReference", sig(boolean.class))
            // ref unresolv?
            .iftrue(throwRef)
            // ref
            .dup()
            // ref ref
            .invokevirtual(p(Reference.class), "getReferencedName", sig(String.class))
            // ref name
            .aload(Arities.EXECUTION_CONTEXT)
            // ref name context
            .swap()
            // ref context name
            .invokedynamic("dyn:getProperty|getMethod", sig(Object.class, Object.class, ExecutionContext.class, String.class), DynJSBootstrapper.HANDLE,
                          DynJSBootstrapper.ARGS);
            // value
        if (throwIfNot != null) {
            codeBlock.dup()
                // value value
                .instance_of(p(throwIfNot))
                // value bool
                .iftrue(end)
                // value
                .pop()
                .append(jsThrowTypeError("expected " + throwIfNot.getName()));
        }
        // result
        codeBlock.go_to(end)

            .label(throwRef)
            .append(jsThrowReferenceError("unable to dereference"))

            .label(end)
            // value
            .nop();
        return codeBlock;
    }

}
