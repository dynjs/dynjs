package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class RegexExpressionTest extends AbstractDynJSTestSupport {
	@Test
	public void createDynRegExpObject() {
		Object result = resultFor("var result = /javascript/gi");
		assertThat(result).isInstanceOf(DynRegExp.class);
		DynRegExp regExp = (DynRegExp) result;
		assertThat(getPropertyValue(regExp, "source")).isEqualTo("javascript");
		assertThat(getPropertyValue(regExp, "ignoreCase")).isEqualTo(true);
		assertThat(getPropertyValue(regExp, "multiline")).isEqualTo(false);
		assertThat(getPropertyValue(regExp, "global")).isEqualTo(true);
	}

	private Object getPropertyValue(DynObject object, String name) {
		return ((PropertyDescriptor) object.getOwnProperty(null, name))
				.getValue();
	}
}
