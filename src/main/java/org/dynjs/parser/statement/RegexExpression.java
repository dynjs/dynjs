package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.ParserException;
import org.dynjs.runtime.DynRegExp;

public class RegexExpression extends AbstractExpression {

	// TODO: Move the parsing logic to the parser
	static class DynRegExpParser {
		private static final String REG_EXP_PATTERN = "^\\/(.*)\\/([igm]{0,})$";

		static DynRegExpParser parse(String text) {
			Pattern pattern = Pattern.compile(REG_EXP_PATTERN);
			Matcher matcher = pattern.matcher(text);
			if (matcher.matches()) {
				return new DynRegExpParser(matcher.group(1), matcher.group(2));
			}

			return null;
		}

		private final String source;
		private final String flags;

		private DynRegExpParser(String source, String flags) {
			this.source = source;
			this.flags = flags;
		}

		public String getSource() {
			return source;
		}

		public String getFlags() {
			return flags;
		}
	}

	private final String source;
	private final String flags;

	public RegexExpression(Tree tree, String regex) {
		super(tree);

		DynRegExpParser regExpParser = DynRegExpParser.parse(regex);
		if (regExpParser == null) {
			throw new ParserException("Invalid regular expression", tree);
		}

		this.source = regExpParser.getSource();
		this.flags = regExpParser.getFlags();
	}

	@Override
	public CodeBlock getCodeBlock() {
		return new CodeBlock() {
			{
				newobj(p(DynRegExp.class));
				dup();
				ldc(source);
				ldc(flags);
				invokespecial(p(DynRegExp.class), "<init>",
						sig(void.class, String.class, String.class));
			}
		};
	}
}
