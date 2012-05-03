/**
 *  Copyright 2012 Douglas Campos, and individual contributors
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
package org.dynjs.runtime;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.api.Function;
import org.dynjs.api.Scope;
import org.dynjs.runtime.builtins.Eval;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DynThreadContext {

    public static final Object UNDEFINED = new Object() {
        @Override
        public String toString() {
            return "undefined";
        }
    };

    public static final Object NULL = new Object();

    private static final Map<String, Object> BUILTINS = new LinkedHashMap<String, Object>() {{
        put("eval", new Eval());
    }};

    private ThreadLocal<DynJS> runtime = new ThreadLocal<>();
    private AtomicInteger storageCounter = new AtomicInteger();
    private Map<Integer, CodeBlock> storage = new HashMap<>();
    private Scope scope = new DynObject();
    private Deque<Function> callStack = new LinkedList<>();
    private DynamicClassLoader classLoader;

    public DynThreadContext() {
        for (Map.Entry<String, Object> builin : BUILTINS.entrySet()) {
            scope.define(builin.getKey(), builin.getValue());
        }
    }

    public DynJS getRuntime() {
        return this.runtime.get();
    }

    public void setRuntime(DynJS runtime) {
        this.runtime.set(runtime);
    }

    public String defineStringLiteral(final String value) {
        return value;
    }

    public Number defineDecimalLiteral(final String value) {
        return Double.parseDouble(value);
    }

    public Number defineOctalLiteral(final String value) {
        return Integer.parseInt(value, 8);
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    /**
     * stores codeblock on internal storage
     *
     * @param block
     */
    public int store(CodeBlock block) {
        Integer slot = storageCounter.getAndIncrement();
        storage.put(slot, block);
        return slot;
    }

    /**
     * retrieves codeblock from internal storage
     *
     * @param id
     * @return
     */
    public CodeBlock retrieve(int id) {
        return storage.get(id);
    }

    public Deque<Function> getCallStack() {
        return callStack;
    }

    public void setClassLoader(DynamicClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public DynamicClassLoader getClassLoader() {
        return classLoader;
    }

}
