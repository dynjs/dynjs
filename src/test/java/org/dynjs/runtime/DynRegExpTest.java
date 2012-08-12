package org.dynjs.runtime;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class DynRegExpTest {
	@Test
	public void initWithNoFlag() {
		DynRegExp regExp = new DynRegExp("\\d{2,4}", "");
		assertThat(regExp.getSource()).isEqualTo("\\d{2,4}");
		assertThat(regExp.isIgnoreCase()).isFalse();
		assertThat(regExp.isMultiline()).isFalse();
		assertThat(regExp.isGlobal()).isFalse();
	}

	@Test
	public void initWithTwoFlags() {
		DynRegExp regExp = new DynRegExp("\\d{2,4}", "im");
		assertThat(regExp.getSource()).isEqualTo("\\d{2,4}");
		assertThat(regExp.isIgnoreCase()).isTrue();
		assertThat(regExp.isMultiline()).isTrue();
		assertThat(regExp.isGlobal()).isFalse();
	}

	@Test
	public void initWithMultipleFlags() {
		DynRegExp regExp = new DynRegExp("\\d{2,4}", "img");
		assertThat(regExp.getSource()).isEqualTo("\\d{2,4}");
		assertThat(regExp.isIgnoreCase()).isTrue();
		assertThat(regExp.isMultiline()).isTrue();
		assertThat(regExp.isGlobal()).isTrue();
	}

}
