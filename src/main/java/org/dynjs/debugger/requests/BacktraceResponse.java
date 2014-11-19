package org.dynjs.debugger.requests;

import org.dynjs.debugger.model.Frame;
import org.dynjs.runtime.Types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Bob McWhirter
 */
public class BacktraceResponse extends AbstractResponse<BacktraceRequest> {

    private final List<Frame> frames;

    public BacktraceResponse(BacktraceRequest request, List<Frame> frames, boolean success, boolean running) {
        super(request, success, running);
        this.frames = frames;
    }

    public List<Frame> getFrames() {
        return this.frames;
    }

    public int fromFrame() {
        return this.frames.get( 0 ).getIndex();
    }

    public int toFrame() {
        return this.frames.get( this.frames.size() - 1 ).getIndex();
    }

    public int totalFrames() {
        return this.frames.size();
    }

    @Override
    public Collection<?> getRefs() {
        List<Object> refs = new ArrayList<>();

        for ( Frame each : this.frames ) {
            Object receiver = each.getReceiver();
            if ( receiver != null && receiver != Types.UNDEFINED ) {
                refs.add( receiver );
            }
        }

        return refs;
    }
}
