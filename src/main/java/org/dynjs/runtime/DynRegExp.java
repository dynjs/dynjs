package org.dynjs.runtime;

public class DynRegExp extends DynObject {
	private String source;
	private boolean global;
	private boolean ignoreCase;

	private boolean multiline;
	private int lastIndex;

	public DynRegExp(String source, String flags) {
		this.source = source;
		parseFlags(flags);
	}

	private void parseFlags(String flags) {
		for (char flag : flags.toCharArray()) {
			if (flag == 'i') {
				ignoreCase = true;
			} else if (flag == 'm') {
				multiline = true;
			} else if (flag == 'g') {
				global = true;
			}
		}
	}

	public String getSource() {
		return source;
	}

	public boolean isGlobal() {
		return global;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public boolean isMultiline() {
		return multiline;
	}

	public int getLastIndex() {
		return lastIndex;
	}
}
