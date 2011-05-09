/*
 * $Id: 20811 2011-05-07 16:52:20Z porcelli $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.dynjs.parser;

import org.antlr.runtime.tree.CommonTree;
import org.objectweb.asm.*;
import org.objectweb.asm.util.TraceClassVisitor;

public class Executor implements Opcodes {

    public Statement printStatement(final Statement expression1) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "invokedynamic", null, "java/lang/Object", null);
        final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "print", "(Ljava/lang/String;)V", null, null);

        Statement stmt = new Statement() {

            @Override
            public void reify(MethodVisitor mv) {
                expression1.reify(mv);
            }
        };
        stmt.reify(mv);
        mv.visitEnd();
        cw.visitEnd();
        System.out.println(cw.toByteArray());
        
        return stmt;
    }

    public Statement createLDC(final CommonTree stringLiteral) {

        return new Statement() {

            @Override
            public void reify(MethodVisitor mv) {
                mv.visitLdcInsn(stringLiteral.getText());
            }
        };
    }
}
