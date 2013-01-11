package org.dynjs.runtime.interp;

import org.dynjs.parser.CodeVisitor;

public interface InterpretingVisitor extends CodeVisitor {
    
    Object pop();

}
