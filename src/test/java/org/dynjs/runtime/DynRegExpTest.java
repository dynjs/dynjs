package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class DynRegExpTest {
	@Test
	public void initWithNoFlag() {
		DynRegExp regExp = new DynRegExp("\\d{2,4}", "");
		assertThat(regExp.getProperty("source").getAttribute("value"))
				.isEqualTo("\\d{2,4}");
		assertThat(regExp.getProperty("ignoreCase").getAttribute("value"))
				.isEqualTo(false);
		assertThat(regExp.getProperty("multiline").getAttribute("value"))
				.isEqualTo(false);
		assertThat(regExp.getProperty("global").getAttribute("value"))
				.isEqualTo(false);
	}

	@Test
	public void initWithTwoFlags() {
		DynRegExp regExp = new DynRegExp("\\d{2,4}", "im");
		assertThat(regExp.getProperty("source").getAttribute("value"))
				.isEqualTo("\\d{2,4}");
		assertThat(regExp.getProperty("ignoreCase").getAttribute("value"))
				.isEqualTo(true);
		assertThat(regExp.getProperty("multiline").getAttribute("value"))
				.isEqualTo(true);
		assertThat(regExp.getProperty("global").getAttribute("value"))
				.isEqualTo(false);
	}

	@Test
	public void initWithMultipleFlags() {
		DynRegExp regExp = new DynRegExp("\\d{2,4}", "img");
		assertThat(regExp.getProperty("source").getAttribute("value"))
				.isEqualTo("\\d{2,4}");
		assertThat(regExp.getProperty("ignoreCase").getAttribute("value"))
				.isEqualTo(true);
		assertThat(regExp.getProperty("multiline").getAttribute("value"))
				.isEqualTo(true);
		assertThat(regExp.getProperty("global").getAttribute("value"))
				.isEqualTo(true);
	}

}
