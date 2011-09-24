package org.dynjs.runtime;

import me.qmx.jitescript.CodeBlock;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.primitives.DynPrimitiveNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DynThreadContext {

    private ThreadLocal<DynJS> runtime = new ThreadLocal<>();
    private AtomicInteger storageCounter = new AtomicInteger();
    private Map<Integer, CodeBlock> storage = new HashMap<>();

    public DynJS getRuntime() {
        return this.runtime.get();
    }

    public void setRuntime(DynJS runtime) {
        this.runtime.set(runtime);
    }

    public DynAtom defineStringLiteral(final String value) {
        return new DynString(value);
    }

    public DynPrimitiveNumber defineDecimalLiteral(final String value) {
        return new DynPrimitiveNumber(value, 10);
    }

    public DynPrimitiveNumber defineOctalLiteral(final String value) {
        return new DynPrimitiveNumber(value, 8);
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
     * @param id
     * @return
     */
    public CodeBlock retrieve(int id){
        return storage.get(id);
    }
}
