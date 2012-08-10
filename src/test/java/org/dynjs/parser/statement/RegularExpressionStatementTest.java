package org.dynjs.parser.statement;

import static org.fest.assertions.Assertions.assertThat;

import java.util.regex.Pattern;

import org.dynjs.parser.statement.RegularExpressionStatement.RegExp;
import org.junit.Test;

public class RegularExpressionStatementTest {
	@Test
	public void parseRegExpWithFlags() {
		RegExp regExp = RegExp.parse("/\\d{2,4}/im");
		assertThat(regExp.getRegex()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isEqualTo(
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		assertThat(regExp.isGlobalMatch()).isFalse();
	}

	@Test
	public void parseRegExpWithoutFlags() {
		RegExp regExp = RegExp.parse("/\\d{2,4}/");
		assertThat(regExp.getRegex()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isNull();
		assertThat(regExp.isGlobalMatch()).isFalse();
	}

	@Test
	public void parseRegExpWithGlobalFlag() {
		RegExp regExp = RegExp.parse("/\\d{2,4}/ig");
		assertThat(regExp.getRegex()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isEqualTo(Pattern.CASE_INSENSITIVE);
		assertThat(regExp.isGlobalMatch()).isTrue();
	}
}
