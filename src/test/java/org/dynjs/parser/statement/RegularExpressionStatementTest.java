package org.dynjs.parser.statement;

import static org.fest.assertions.Assertions.assertThat;

import org.dynjs.parser.statement.RegularExpressionStatement.DynRegExpParser;
import org.junit.Test;

public class RegularExpressionStatementTest {
	@Test
	public void parseNoFlags() {
		DynRegExpParser regExp = DynRegExpParser.parse("/\\d{2,4}/");
		assertThat(regExp.getSource()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isEqualTo("");
	}

	@Test
	public void parseWithFlags() {
		DynRegExpParser regExp = DynRegExpParser.parse("/\\d{2,4}/im");
		assertThat(regExp.getSource()).isEqualTo("\\d{2,4}");
		assertThat(regExp.getFlags()).isEqualTo("im");
	}
}
