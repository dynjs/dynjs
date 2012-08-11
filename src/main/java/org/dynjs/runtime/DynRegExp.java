package org.dynjs.runtime;

import java.util.regex.Pattern;

public class DynRegExp extends DynObject {
	private final Pattern pattern;
	private final boolean isGlobalMatch;

	public DynRegExp(String regexp, Integer flags, boolean isGlobalMatch) {
		this.isGlobalMatch = isGlobalMatch;
		if (flags == null) {
			pattern = Pattern.compile(regexp);
		} else {
			pattern = Pattern.compile(regexp, flags);
		}
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
