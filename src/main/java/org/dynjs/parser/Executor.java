/**
 *  Copyright 2011 Douglas Campos <qmx@qmx.me>
 *  Copyright 2011 Alexandre Porcelli <alexandre.porcelli@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.parser;

import java.io.PrintWriter;
import java.util.List;

import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.MethodBody;
import org.antlr.runtime.tree.CommonTree;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.TraceClassVisitor;

public class Executor implements Opcodes {

    private boolean DEBUG = true;

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
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        if (DEBUG) {
            TraceClassVisitor traceClassVisitor = new TraceClassVisitor(cw, new PrintWriter(System.err));
            classNode.accept(traceClassVisitor);
        } else {
            classNode.accept(cw);
        }
        return cw.toByteArray();
    }

    public Statement printStatement(final Statement expression) {

        InsnList print = new InsnList();

        if (expression.isList()) {
            print.add(expression.getStatementList());
        } else {
            print.add(expression.getStatement());
        }

        CodeBlock codeBlock = new CodeBlock() {{
            getstatic("java/lang/System", "out", "Ljava/io/PrintStream;");
            swap();
            invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/Object;)V");
            voidreturn();
        }};

        print.add(codeBlock.getInstructionList());

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
