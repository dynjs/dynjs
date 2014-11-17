package org.dynjs.debugger.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bob McWhirter
 */
public class ScriptsRequest extends AbstractRequest<ScriptsResponse> {

    @JsonIgnoreProperties("maxStringLength")
    public static class Arguments {

        private boolean includeSource;
        private int types;
        private int[] ids;

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

    private Arguments arguments;

    public ScriptsRequest() {
        super("scripts");
    }

    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Arguments getArguments() {
        return this.arguments;
    }

}

