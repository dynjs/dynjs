package org.dynjs.runtime;

public class Completion {

    public enum Type {
        NORMAL,
        BREAK,
        CONTINUE,
        RETURN,
        THROW
    }

    public Type type;
    public Object value;
    public String target;

    public Completion(Type type, Object value, String target) {
        this.type = type;
        this.value = value;
        this.target = target;
    }

    public static Completion createNormal() {
        return new Completion(Type.NORMAL, null, null);
    }

    public static Completion createNormal(Object value) {
        return new Completion(Type.NORMAL, value, null);
    }

    public static Completion createBreak() {
        return new Completion(Type.BREAK, null, null);
    }

    public static Completion createBreak(String target) {
        return new Completion(Type.BREAK, null, target);
    }

    public static Completion createContinue(String target) {
        return new Completion(Type.CONTINUE, null, target);
    }

    public static Completion createReturn(Object value) {
        return new Completion(Type.RETURN, value, null);
    }

    public static Completion createThrow(Object value) {
        return new Completion(Type.THROW, value, null);
    }

    public String toString() {
        return "(" + this.type + ", " + this.value + ", " + this.target + ")";
    }

}
