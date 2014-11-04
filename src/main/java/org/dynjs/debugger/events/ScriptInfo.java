package org.dynjs.debugger.events;

/**
 * @author Bob McWhirter
 */
public class ScriptInfo {

    private final String name;

    public ScriptInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
