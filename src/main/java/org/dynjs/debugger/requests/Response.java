package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dynjs.debugger.Message;

import java.util.Collection;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public interface Response extends Message {

    Request getRequest();
    String getCommand();
    boolean isSuccess();
    boolean isRunning();

    @JsonIgnore
    Collection<? extends Object> getRefs();
}
