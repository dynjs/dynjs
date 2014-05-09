package org.dynjs.runtime.linker.js.undefined;

import com.headius.invokebinder.Binder;
import org.dynjs.codegen.DereferencedReference;
import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Reference;
import org.dynjs.runtime.linker.js.object.JSObjectCallLink;
import org.dynjs.runtime.linker.js.object.JSObjectConstructLink;
import org.dynjs.runtime.linker.js.object.JSObjectPropertyGetLink;
import org.dynjs.runtime.linker.js.object.JSObjectPropertySetLink;
import org.projectodd.rephract.ContextualLinker;
import org.projectodd.rephract.Invocation;
import org.projectodd.rephract.Link;
import org.projectodd.rephract.LinkLogger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class JavascriptUndefinedLinker extends ContextualLinker {

    public JavascriptUndefinedLinker(LinkLogger logger) {
        super(logger);
    }

    /*
    @Override
    public Link linkGetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSObjectPropertyGetLink( invocation.builder() );
    }
    */

    @Override
    public Link linkSetProperty(Invocation invocation, String propertyName) throws Exception {
        return new JSUndefinedPropertySetLink( invocation.builder() );
    }

}
