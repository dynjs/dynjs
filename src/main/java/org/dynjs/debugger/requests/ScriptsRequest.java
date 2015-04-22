package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
@JsonIgnoreProperties("maxStringLength")
public class ScriptsRequest extends AbstractRequest<ScriptsResponse> {

    private boolean includeSource;
    private int types;
    private int[] ids;

    public ScriptsRequest() {
        super("scripts");
    }

    public void setIncludeSource(boolean includeSource) {
        this.includeSource = includeSource;
    }

    public boolean isIncludeSource() {
        return this.includeSource;
    }

    public void setTypes(int type) {
        this.types = types;
    }

    public int getTypes() {
        return this.types;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public int[] getIds() {
        return this.ids;
    }

}

