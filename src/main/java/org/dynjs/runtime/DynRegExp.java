package org.dynjs.runtime;

import java.util.regex.Pattern;

public class DynRegExp extends DynObject {
	private final Pattern pattern;
	private final boolean isGlobalMatch;

	public DynRegExp(String regexp, int flags, boolean isGlobalMatch) {
		this.isGlobalMatch = isGlobalMatch;
		pattern = Pattern.compile(regexp, flags);
	}

	public String getRegex() {
		return pattern.pattern();
	}

	public int getFlags() {
		return pattern.flags();
	}

	public boolean isGlobalMatch() {
		return isGlobalMatch;
	}
}
