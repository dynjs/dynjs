package org.dynjs.runtime;

public class DynRegExp extends DynObject {
	public DynRegExp(String source, String flags) {
		setClassName("RegExp");
		defineRegExpProperty("source", source);
		defineRegExpProperty("ignoreCase", false);
		defineRegExpProperty("multiline", false);
		defineRegExpProperty("global", false);
		defineRegExpProperty("lastIndex", 0);
		parseFlags(flags);
	}

	private void parseFlags(String flags) {
		for (char flag : flags.toCharArray()) {
			if (flag == 'i') {
				defineRegExpProperty("ignoreCase", true);
			} else if (flag == 'm') {
				defineRegExpProperty("multiline", true);
			} else if (flag == 'g') {
				defineRegExpProperty("global", true);
			}
		}
	}
	
	private void defineRegExpProperty(final String name, final Object value) {
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set("Value", value);
                set("Writable", true);
                set("Configurable", true);
                set("Enumerable", true);
            }
        };
        defineOwnProperty(null, name, desc, false);
    }
}