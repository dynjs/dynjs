package org.dynjs.runtime;

public class DynRegExp extends DynObject {
	public DynRegExp(String source, String flags) {
		define("source", source);
		define("ignoreCase", false);
		define("multiline", false);
		define("global", false);
		define("lastIndex", 0);
		parseFlags(flags);
	}

	private void parseFlags(String flags) {
		for (char flag : flags.toCharArray()) {
			if (flag == 'i') {
				setProperty("ignoreCase", true);
			} else if (flag == 'm') {
				setProperty("multiline", true);
			} else if (flag == 'g') {
				setProperty("global", true);
			}
		}
	}
}
