package org.dynjs.compiler.jite;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.JiteClass;

import org.dynjs.parser.Position;

public abstract class AbstractDynJSJiteClass extends JiteClass {
    
    public AbstractDynJSJiteClass(String className, Class<?> superClass, Position position) {
        super(className, p(superClass), new String[]{} );
        
        String sourceFile = "<eval>";
        if ( position != null && position.getFileName() != null ) {
            sourceFile = position.getFileName();
        }
        
        setSourceFile(sourceFile);
    }

}
