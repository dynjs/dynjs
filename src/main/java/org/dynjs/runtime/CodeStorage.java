package org.dynjs.runtime;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import me.qmx.jitescript.CodeBlock;

public class CodeStorage {

    public CodeStorage() {

    }

    public Entry retrieve(int statementNumber) {
        Entry entry = this.storage.get( statementNumber );
        if ( entry == null ) {
            entry = new Entry( statementNumber );
            this.storage.put( statementNumber, entry );
        }
        
        return entry;
    }

    public static class Entry {

        public Entry(int statementNumber) {
            this.statementNumber = statementNumber;
        }
        
        public Object getCompiled() {
            if ( compiled == null ) {
               return null; 
            }
            
            return compiled.get();
        }
        
        public void setCompiled(Object compiled) {
            this.compiled = new WeakReference<Object>( compiled );
        }
        
        public String toString() {
            return "[Entry: codeBlock=" + codeBlock + "; compiled=" + ( compiled == null ? null : compiled.get() ) + "]";
        }

        public int statementNumber;
        public CodeBlock codeBlock;
        private WeakReference<Object> compiled;
    }

    private Map<Integer, Entry> storage = new HashMap<>();
}
