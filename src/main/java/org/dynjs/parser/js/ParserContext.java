package org.dynjs.parser.js;

import java.util.HashSet;
import java.util.Set;

class ParserContext {

    enum ContextType {
        PROGRAM,
        FUNCTION,
        ITERATION,
        SWITCH,
    }

    private ParserContext parent;
    private ContextType type;
    private Set<String> labels = new HashSet<>();
    private boolean inProlog = true;
    private boolean strict;

    ParserContext(ContextType type) {
        this(null, type);
    }

    ParserContext(ParserContext parent, ContextType type) {
        this.parent = parent;
        this.type = type;
        this.strict = false;
    }

    void addLabel(String label) {
        this.labels.add(label);
    }

    void removeLabel(String label) {
        this.labels.remove(label);
    }

    Set<String> getLabels() {
        return this.labels;
    }

    boolean isStrict() {
        return this.strict;
    }

    void setStrict(boolean strict) {
        this.strict = strict;
    }

    boolean isInProlog() {
        return this.inProlog;
    }

    void setInProlog(boolean inProlog) {
        this.inProlog = inProlog;
    }

    boolean isValidReturn() {
        return this.type != ContextType.PROGRAM;
    }

    boolean isValidBreak(String label) {
        if (this.type != ContextType.ITERATION && this.type != ContextType.SWITCH) {
            return false;
        }

        return isValidLabel(label);
    }

    boolean isValidContinue(String label) {
        if (this.type != ContextType.ITERATION) {
            return false;
        }

        return isValidLabel(label);
    }

    boolean isValidLabel(String label) {
        if (label == null) {
            return true;
        }
        if (this.labels.contains(label)) {
            return true;
        }

        if (this.parent != null) {
            return this.parent.isValidLabel(label);
        }
        return false;
    }

}
