/**
 *  Copyright 2014 Red Hat, and individual contributors
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
package org.dynjs.runtime.java;

import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.BuiltinObject;

/**
 * Provides Nashorn-like extensions in Javascript code. See: https://wiki.openjdk.java.net/display/Nashorn/Nashorn+extensions
 * <p></p>
 * Example:
 * <p></p>
 * var obj = new JSAdapter({
 * var store = {};
 * __get__: function(name) { return store[name]; }
 * __put__: function(name, value) { store[name] = value; }
 * });
 */
public class JSAdapter extends BuiltinObject {
    public JSAdapter(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        JSObject prototype = null;
        JSObject overrides = null;
        JSObject adaptee = null;

        if (args.length == 1) {
            adaptee = (JSObject) args[0];
        } else if (args.length == 2) {
            overrides = (JSObject) args[0];
            adaptee = (JSObject) args[1];
        } else if (args.length == 3) {
            prototype = (JSObject) args[0];
            overrides = (JSObject) args[1];
            adaptee = (JSObject) args[2];
        }

        return new JSAdapterObject(context.getGlobalContext(), prototype, overrides, adaptee);
    }

    public static DynObject newObject(ExecutionContext context) {
        return (DynObject) context.construct((Object) null, (JSFunction) context.getGlobalContext().getObject().get(context, "__JS_Adapter"));
    }

    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/java/JSAdapter.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: JSAdapter>";
    }

    private class JSAdapterObject extends DynObject {

        private final JSObject overrides;
        private final JSObject adaptee;

        public JSAdapterObject(GlobalContext globalContext, JSObject prototype, JSObject overrides, Object adaptee) {
            super(globalContext);
            setClassName("JSObject");
            if (prototype != null) {
                setPrototype(prototype);
            }
            if (overrides != null) {
                this.overrides = overrides;
            } else {
                this.overrides = null;
            }

            if ((adaptee instanceof JSObject)) {
                this.adaptee = (JSObject) adaptee;
            } else {
                this.adaptee = new DynObject(globalContext);
            }
        }

        @Override
        public Object get(ExecutionContext context, String name) {
            if ((this.overrides != null) && (this.overrides.getProperty(context, name) != Types.UNDEFINED)) {
                return this.overrides.get(context, name);
            }

            if (getProperty(context, name) != Types.UNDEFINED) {
                return super.get(context, name);
            }

            if (this.adaptee != null) {
                final Object getter = adaptee.get(context, "__get__");
                if (getter != Types.UNDEFINED) {
                    return context.call((JSFunction) getter, this, name);
                }
            }
            return super.get(context, name);
        }

        @Override
        public void put(ExecutionContext context, final String name, final Object value, final boolean shouldThrow) {
            if ((this.overrides != null) && (this.overrides.getProperty(context, name) != Types.UNDEFINED)) {
                this.overrides.put(context, name, value, shouldThrow);
                return;
            }
            if (getProperty(context, name) != Types.UNDEFINED) {
                super.put(context, name, value, shouldThrow);
                return;
            }
            if (this.adaptee != null) {
                final Object setter = adaptee.get(context, "__set__");
                if (setter != Types.UNDEFINED) {
                    context.call((JSFunction) setter, this, name, value);
                    return;
                }
            }
            super.put(context, name, value, shouldThrow);
        }
    }
}
