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

import java.util.List;
import org.antlr.runtime.tree.CommonTree;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class Executor implements Opcodes {

    public byte[] program(List<Statement> blockContent) {
        ClassNode classNode = new ClassNode();
        classNode.version = V1_7;
        classNode.access = ACC_PUBLIC + ACC_ABSTRACT;
        classNode.superName = "java/lang/Object";
        classNode.name = "WTF";
        final MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC,
                "main", "([Ljava/lang/String;)V", null, null);
        for (Statement statement : blockContent) {
            if (statement.isList()) {
                methodNode.instructions.add(statement.getStatementList());
            } else {
                methodNode.instructions.add(statement.getStatement());
            }
        }

        classNode.methods.add(methodNode);
        ClassWriter cw = new ClassWriter(0);
        classNode.accept(cw);
        return cw.toByteArray();
    }

    public Statement printStatement(final Statement expression) {

        InsnList print = new InsnList();

        if (expression.isList()) {
            print.add(expression.getStatementList());
        } else {
            print.add(expression.getStatement());
        }

        print.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "java/io/PrintStream"));
        print.add(new InsnNode(SWAP));
        print.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V"));
        print.add(new InsnNode(RETURN));

        return new PrintStatementImpl(print);
    }

    public Statement createLDC(final CommonTree stringLiteral) {

        LdcInsnNode node = new LdcInsnNode(stringLiteral.getText());

        return new LDCStatementImpl(node, stringLiteral);
    }

    public Statement block(List<Statement> blockContent) {

        InsnList block = new InsnList();
        for (Statement statement : blockContent) {
            if (statement.isList()) {
                block.add(statement.getStatementList());
            } else {
                block.add(statement.getStatement());
            }
        }

        return new BlockStatementImpl(block);
    }
}
