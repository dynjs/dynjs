package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

public class NameEnumerator {

    private List<String> names;
    private int counter;

    public NameEnumerator(List<String> names) {
        this.names = names;
        this.counter = 0;
    }

    public boolean hasNext() {
        return this.counter < names.size();
    }

    public String next() {
        return this.names.get(this.counter++);
    }
    
    public List<String> toList() {
        List<String> list = new ArrayList<>();
        list.addAll( this.names );
        return list;
    }

}
