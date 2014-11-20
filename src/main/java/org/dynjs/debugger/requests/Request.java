package org.dynjs.debugger.requests;

import org.dynjs.debugger.Message;

/**
 * @author Bob McWhirter
 */
public interface Request<T extends Response> extends Message {

    String getCommand();

    void setSeq(int seq);
    int getSeq();
}
