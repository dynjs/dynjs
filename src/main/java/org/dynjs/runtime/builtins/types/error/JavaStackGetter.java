package org.dynjs.runtime.builtins.types.error;

import org.dynjs.exception.ThrowException;
import org.dynjs.runtime.*;

public class JavaStackGetter extends AbstractNativeFunction {

    private final ThrowException e;

    public JavaStackGetter(GlobalContext globalContext, ThrowException e) {
        super(globalContext);
        this.e = e;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        StringBuilder buf = new StringBuilder();
        Object cause = null;

        if (self instanceof JSObject) {
            JSObject jsObject = (JSObject) self;
            appendHeader( buf, context, jsObject );
            if (jsObject.hasProperty(context, "cause")) {
                cause = jsObject.get(context, "cause");
            }
        } else if ( self instanceof Throwable ) {
            Throwable throwable = (Throwable) self;
            appendHeader( buf, throwable );
            cause = throwable.getCause();
        }

        appendStack( buf, this.e );

        while ( cause != null && cause != Types.UNDEFINED ) {
            System.err.println("cause: " + cause + " // " + System.identityHashCode(cause) + " // " + cause.getClass());
            buf.append("Caused by: ");
            if ( cause instanceof JSObject ) {
                appendHeader( buf, context, (JSObject) cause);
                buf.append( ((JSObject)cause).getProperty( context, "stack" ) );
                if ( ((JSObject)cause).hasProperty(context, "cause")) {
                    cause = ((JSObject)cause).get(context, "cause");
                } else {
                    cause = null;
                }
            } else if ( cause instanceof Throwable ) {
                appendHeader( buf, (Throwable) cause);
                appendStack( buf, (Throwable) cause);
                cause = ((Throwable) cause).getCause();
            } else {
                cause = null;
            }
        }

        return buf.toString();
    }

    void appendHeader(StringBuilder buf, Throwable t) {
        buf.append( t.getClass().getName() );
        if ( t.getMessage() != null ) {
            buf.append( ": " ).append( t.getMessage() );
        }

        buf.append("\n");
    }

    void appendHeader(StringBuilder buf, ExecutionContext context, JSObject jsObject) {

        boolean stringified = false;

        if ( jsObject.hasProperty( context, "toString" ) ) {
            Object toString = jsObject.get(context, "toString");
            if ( toString instanceof JSFunction ) {
                buf.append( ((JSFunction) toString).call( context ) );
                stringified = true;
            }
        }

        if ( ! stringified ) {
            if (jsObject.hasProperty(context, "name")) {
                buf.append(Types.toString(context, jsObject.get(context, "name")));
            } else {
                buf.append("<unknown>");
            }

            if (jsObject.hasProperty(context, "message")) {
                Object message = Types.toString(context, jsObject.get(context, "message"));
                if ((message != null) && !message.equals("") && (message != Types.UNDEFINED)) {
                    buf.append(": ").append(message);
                } else {

                }
            }
        }

        buf.append("\n");
    }

    void appendStack(StringBuilder buf, Throwable t) {
        StackTraceElement[] elements = t.getStackTrace();
        for (StackTraceElement each : elements) {
            if (each.getClassName().startsWith("org.dynjs") || each.getClassName().startsWith( "java.lang.invoke" )) {
                break;
            }
            buf.append("  at ").append(each).append("\n");
        }
    }

    @Override
    public void setFileName() {
        this.filename = "<internal>";
    }

    @Override
    public void setupDebugContext() {
        setDebugContext("<stack-getter>");
    }

}
