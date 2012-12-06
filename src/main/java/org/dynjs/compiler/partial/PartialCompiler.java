package org.dynjs.compiler.partial;

import me.qmx.jitescript.JiteClass;

import org.dynjs.runtime.ExecutionContext;

public interface PartialCompiler {
    
    void define(JiteClass cls, ExecutionContext context, boolean strict);

}
