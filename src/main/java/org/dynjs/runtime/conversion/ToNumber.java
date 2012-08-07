package org.dynjs.runtime.conversion;


// TODO: implement according to http://es5.github.com/#x9.3
public class ToNumber {
	public static Double toNumber(String s) {
		return s.isEmpty() ? 0.0 : Double.parseDouble(s);
	}

	public static Double toNumber(Boolean b) {
		return b ? 1.0 : 0.0;
	}
}
