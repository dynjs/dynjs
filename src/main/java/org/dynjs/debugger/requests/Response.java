package org.dynjs.debugger.requests;

/**
 * @author Bob McWhirter
 */
public interface Response {

    String getCommand();
    boolean isSuccess();
    boolean isRunning();
}
