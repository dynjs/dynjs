package org.dynjs.runtime;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.dynjs.parser.Statement;

public class BlockManager {

    public BlockManager() {

    }

    public Entry retrieve(int statementNumber) {
        Entry entry = this.storage.get(statementNumber);
        if (entry == null) {
            entry = new Entry(statementNumber);
            this.storage.put(statementNumber, entry);
        }

        return entry;
    }

    public static class Entry {

        public Entry(int statementNumber) {
            this.statementNumber = statementNumber;
        }

        public Object getCompiled() {
            /*
             * if (compiled == null) {
             * System.err.println( "returning null early" );
             * return null;
             * }
             * 
             * 
             * return compiled.get();
             */
            return compiled;
        }

        public void setCompiled(Object compiled) {
            // this.compiled = new WeakReference<Object>( compiled );
            this.compiled = compiled;
        }

        public String toString() {
            // return "[Entry: statement=" + statement + "; compiled=" + (compiled == null ? null : compiled.get()) + "]";
            return "[Entry: statement=" + statement + "; compiled=" + compiled + "]";
        }

        public int statementNumber;
        public Statement statement;
        // private WeakReference<Object> compiled;
        private Object compiled;
    }

    private Map<Integer, Entry> storage = new HashMap<>();
}
