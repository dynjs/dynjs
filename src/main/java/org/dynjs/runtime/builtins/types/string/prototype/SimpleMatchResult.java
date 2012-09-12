package org.dynjs.runtime.builtins.types.string.prototype;

import java.util.regex.MatchResult;

public class SimpleMatchResult implements MatchResult {

    private int end;

    public SimpleMatchResult(int end) {
        this.end = end;
    }
    
    @Override
    public int start() {
        return 0;
    }

    @Override
    public int start(int group) {
        return 0;
    }

    @Override
    public int end() {
        return this.end;
    }

    @Override
    public int end(int group) {
        if ( group == 0 ) {
            return this.end;
        }
        return -1;
    }

    @Override
    public String group() {
        return null;
    }

    @Override
    public String group(int group) {
        return null;
    }

    @Override
    public int groupCount() {
        return 0;
    }

}
