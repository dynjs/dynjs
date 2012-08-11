package org.dynjs.parser.statement;

import static org.fest.assertions.Assertions.assertThat;

import java.util.regex.Pattern;

import org.dynjs.parser.statement.RegularExpressionStatement.DynRegExpParser;
import org.dynjs.runtime.DynRegExp;
import org.junit.Test;

public class RegularExpressionStatementTest {
	@Test
	public void parseRegExpWithFlags() {
		DynRegExp regExp = DynRegExpParser.parse("/\\d{2,4}/im");
		assertThat(regExp.getRegex()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isEqualTo(
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		assertThat(regExp.isGlobalMatch()).isFalse();
	}

	@Test
	public void parseRegExpWithoutFlags() {
		DynRegExp regExp = DynRegExpParser.parse("/\\d{2,4}/");
		assertThat(regExp.getRegex()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isEqualTo(0);
		assertThat(regExp.isGlobalMatch()).isFalse();
	}

	@Test
	public void parseRegExpWithGlobalFlag() {
		DynRegExp regExp = DynRegExpParser.parse("/\\d{2,4}/ig");
		assertThat(regExp.getRegex()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isEqualTo(Pattern.CASE_INSENSITIVE);
		assertThat(regExp.isGlobalMatch()).isTrue();
	}
}
