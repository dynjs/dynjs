package org.dynjs.compiler.bytecode.partial;

import me.qmx.jitescript.JiteClass;

import org.dynjs.compiler.CompilationContext;
import org.dynjs.runtime.ExecutionContext;

public interface PartialCompiler {
    
    void define(JiteClass cls, CompilationContext context, boolean strict);

}
