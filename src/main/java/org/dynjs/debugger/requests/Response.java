package org.dynjs.debugger.requests;

import org.dynjs.debugger.Message;

/**
 * @author Bob McWhirter
 */
public interface Response extends Message {

    Request getRequest();
    String getCommand();
    boolean isSuccess();
    boolean isRunning();
}
