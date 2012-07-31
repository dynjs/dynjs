package org.dynjs.runtime;

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
        }
        
        return entry;
    }

    public static class Entry {

        public Entry(int statementNumber) {
            this.statementNumber = statementNumber;
        }

        public int statementNumber;
        public CodeBlock codeBlock;
        public Object compiled;
    }

    private Map<Integer, Entry> storage = new HashMap<>();

}
