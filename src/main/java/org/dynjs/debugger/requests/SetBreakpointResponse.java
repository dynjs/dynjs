package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class SetBreakpointResponse extends AbstractResponse<SetBreakpointRequest> {

    public static class ActualLocation {
        private final int scriptId;
        private final int line;
        private final int column;

        public ActualLocation(int scriptId, int line, int column) {
            this.scriptId = scriptId;
            this.line = line;
            this.column = column;
        }

        @JsonProperty("script_id")
        public int getScriptId() {
            return this.scriptId;
        }

        public int getLine() {
            return this.line;
        }

        public int getColumn() {
            return this.column;
        }
    }

    private final long num;

    public SetBreakpointResponse(SetBreakpointRequest request, long num, boolean success, boolean running) {
        super(request, success, running);
        this.num = num;
    }

    @JsonProperty("breakpoint")
    public long getNumber() {
        return this.num;
    }

    public String getType() {
        return getRequest().getType();
    }

    @JsonProperty("actual_locations")
    public List<ActualLocation> getActualLocations() {
        //return Collections.singletonList( new ActualLocation( this.getRequest().getArguments().getLine(), this.getRequest().getArguments().getColumn() ) );
        return Collections.emptyList();
    }
}
