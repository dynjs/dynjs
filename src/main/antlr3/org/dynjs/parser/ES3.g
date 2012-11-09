/*

Software License Agreement (BSD License)

Copyright (c) 2008-2009, Xebic Research B.V.
All rights reserved.

Redistribution and use of this software in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

* Redistributions of source code must retain the above
  copyright notice, this list of conditions and the
  following disclaimer.

* Redistributions in binary form must reproduce the above
  copyright notice, this list of conditions and the
  following disclaimer in the documentation and/or other
  materials provided with the distribution.

* Neither the name of Xebic Research B.V. nor the names of its
  contributors may be used to endorse or promote products
  derived from this software without specific prior
  written permission of Xebic Research B.V.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Original work by Patrick Hulsmeijer.

This ANTLR 3 LL(*) grammar is based on Ecma-262 3rd edition (JavaScript 1.5, JScript 5.5). 
The annotations refer to the "A Grammar Summary" section (e.g. A.1 Lexical Grammar) and the numbers in parenthesis to the paragraph numbers (e.g. (7.8) ).
This document is best viewed with ANTLRWorks (www.antlr.org).


The major challenges faced in defining this grammar were:

-1- Ambiguity surrounding the DIV sign in relation to the multiplicative expression and the regular expression literal.
This is solved with some lexer driven magic: a gated semantical predicate turns the recognition of regular expressions on or off, based on the
value of the RegularExpressionsEnabled property. When regular expressions are enabled they take precedence over division expressions. The decision whether
regular expressions are enabled is based on the heuristics that the previous token can be considered as last token of a left-hand-side operand of a division.

-2- Automatic semicolon insertion.
This is solved within the parser. The semicolons are not physically inserted but the situations in which they should are recognized and treated as if they were.
The physical insertion of semicolons would be undesirable because of several reasons:
- performance degration because of how ANTLR handles tokens in token streams
- the alteration of the input, which we need to have unchanged
- it is superfluous being of no interest to AST construction

-3- Unicode identifiers
Because ANTLR couldn't handle the unicode tables defined in the specification well and for performance reasons unicode identifiers are implemented as an action 
driven alternative to ASCII identifiers. First the ASCII version is tried that is defined in detail in this grammar and then the unicode alternative is tried action driven.
Because of the fact that the ASCII version is defined in detail the mTokens switch generation in the lexer can predict identifiers appropriately.
For details see the identifier rules.


The minor challenges were related to converting the grammar to an ANTLR LL(*) grammar:
- Resolving the ambiguity between functionDeclaration vs functionExpression and block vs objectLiteral stemming from the expressionStatement production.
- Left recursive nature of the left hand side expressions.
- The assignmentExpression production.
- The forStatement production.
The grammar was kept as close as possible to the grammar in the "A Grammar Summary" section of Ecma-262.

*/

grammar ES3 ;

options
{
	output = AST ;
}

tokens
{
// Reserved words
	NULL		= 'null' ;
	TRUE		= 'true' ;
	FALSE		= 'false' ;

// Keywords
	BREAK		= 'break' ;
	CASE		= 'case' ;
	CATCH 		= 'catch' ;
	CONTINUE 	= 'continue' ;
	DEFAULT		= 'default' ;
	DELETE		= 'delete' ;
	DO 		= 'do' ;
	ELSE 		= 'else' ;
	FINALLY 	= 'finally' ;
	FOR 		= 'for' ;
	FUNCTION 	= 'function' ;
	IF 		= 'if' ;
	IN 		= 'in' ;
	INSTANCEOF 	= 'instanceof' ;
	NEW 		= 'new' ;
	RETURN 		= 'return' ;
	SWITCH 		= 'switch' ;
	THIS 		= 'this' ;
	THROW 		= 'throw' ;
	TRY 		= 'try' ;
	TYPEOF 		= 'typeof' ;
	VAR 		= 'var' ;
	VOID 		= 'void' ;
	WHILE 		= 'while' ;
	WITH 		= 'with' ;

// Future reserved words
	//ABSTRACT	= 'abstract' ;
	//BOOLEAN 	= 'boolean' ;
	//BYTE 		= 'byte' ;
	//CHAR 		= 'char' ;
	CLASS 		= 'class' ;
	CONST 		= 'const' ;
	DEBUGGER 	= 'debugger' ;
	//DOUBLE		= 'double' ;
	ENUM 		= 'enum' ;
	EXPORT 		= 'export' ;
	EXTENDS		= 'extends' ;
	//FINAL 		= 'final' ;
	//FLOAT 		= 'float' ;
	//GOTO 		= 'goto' ;
	//IMPLEMENTS 	= 'implements' ;
	IMPORT		= 'import' ;
	//INT 		= 'int' ;
	//INTERFACE 	= 'interface' ;
	//LONG 		= 'long' ;
	//NATIVE 		= 'native' ;
	//PACKAGE 	= 'package' ;
	//PRIVATE 	= 'private' ;
	//PROTECTED 	= 'protected' ;
	//PUBLIC		= 'public' ;
	//SHORT 		= 'short' ;
	//STATIC 		= 'static' ;
	SUPER 		= 'super' ;
	//SYNCHRONIZED 	= 'synchronized' ;
	//THROWS 		= 'throws' ;
	//TRANSIENT 	= 'transient' ;
	//VOLATILE 	= 'volatile' ;

// Punctuators
	LBRACE		= '{' ;
	RBRACE		= '}' ;
	LPAREN		= '(' ;
	RPAREN		= ')' ;
	LBRACK		= '[' ;
	RBRACK		= ']' ;
	DOT		= '.' ;
	SEMIC		= ';' ;
	COMMA		= ',' ;
	LT		= '<' ;
	GT		= '>' ;
	LTE		= '<=' ;
	GTE		= '>=' ;
	EQ		= '==' ;
	NEQ		= '!=' ;
	SAME		= '===' ;
	NSAME		= '!==' ;
	ADD		= '+' ;
	SUB		= '-' ;
	MUL		= '*' ;
	MOD		= '%' ;
	INC		= '++' ;
	DEC		= '--' ;
	SHL		= '<<' ;
	SHR		= '>>' ;
	SHU		= '>>>' ;
	AND		= '&' ;
	OR		= '|' ;
	XOR		= '^' ;
	NOT		= '!' ;
	INV		= '~' ;
	LAND		= '&&' ;
	LOR		= '||' ;
	QUE		= '?' ;
	COLON		= ':' ;
	ASSIGN		= '=' ;
	ADDASS		= '+=' ;
	SUBASS		= '-=' ;
	MULASS		= '*=' ;
	MODASS		= '%=' ;
	SHLASS		= '<<=' ;
	SHRASS		= '>>=' ;
	SHUASS		= '>>>=' ;
	ANDASS		= '&=' ;
	ORASS		= '|=' ;
	XORASS		= '^=' ;
	DIV		= '/' ;
	DIVASS		= '/=' ;
	
// Imaginary
	ARGS ;
	ARRAY ;
	BLOCK ;
	BYFIELD ;
	BYINDEX ;
	CALL ;
	CEXPR ;
	EXPR ;
	FORITER ;
	FORSTEP ;
	ITEM ;
	LABELLED ;
	NAMEDVALUE ;
	PROPERTYSET ;
	PROPERTYGET ;
	NEG ;
	OBJECT ;
	PDEC ;
	PINC ;
	POS ;
	PROGRAM ;
	ContinuedStringLiteral;
	
// Soft Keywords

	SK_PRINT;
}

@lexer::header {
package org.dynjs.parser;
}

@parser::header {
package org.dynjs.parser;

import java.util.Set;
import java.util.HashSet;
}


@lexer::members
{
private Token last;

private List<String> errors = new ArrayList<String>();
public void displayRecognitionError(String[] tokenNames, RecognitionException e) 
{
    String hdr = getErrorHeader(e);
    String msg = getErrorMessage(e, tokenNames);
    errors.add(hdr + " " + msg);
}

public List<String> getErrors() {
  return this.errors;
}

private final boolean areRegularExpressionsEnabled()
{
	if (last == null)
	{
		return true;
	}
	switch (last.getType())
	{
	// identifier
		case Identifier:
	// literals
		case NULL:
		case TRUE:
		case FALSE:
		case THIS:
		case OctalIntegerLiteral:
		case DecimalLiteral:
		case HexIntegerLiteral:
		case StringLiteral:
		case ContinuedStringLiteral:
	// member access ending 
		case RBRACK:
	// function call or nested expression ending
		case RPAREN:
			return false;
	// otherwise OK
		default:
			return true;
	}
}
	
private final void consumeIdentifierUnicodeStart() throws RecognitionException, NoViableAltException
{
	int ch = input.LA(1);
	if (isIdentifierStartUnicode(ch))
	{
		matchAny();
		do
		{
			ch = input.LA(1);
			if (ch == '$' || (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || ch == '\\' || ch == '_' || (ch >= 'a' && ch <= 'z') || isIdentifierPartUnicode(ch))
			{
				mIdentifierPart();
			}
			else
			{
				return;
			}
		}
		while (true);
	}
	else
	{
		throw new NoViableAltException();
	}
}
	
private final boolean isIdentifierPartUnicode(int ch)
{
	return Character.isJavaIdentifierPart(ch);
}
	
private final boolean isIdentifierStartUnicode(int ch)
{
	return Character.isJavaIdentifierStart(ch);
}

public Token nextToken()
{
	Token result = super.nextToken();
	if (result.getChannel() == Token.DEFAULT_CHANNEL)
	{
		last = result;
	}
	return result;		
}
}

@parser::members
{

public ParserWatcher getWatcher() {
  return (ParserWatcher) ((Object)getTreeAdaptor());
}

public boolean isStrict() {
  return getWatcher().isStrict();
}

public void enterLabel(String label) {
  getWatcher().enterLabel( label );
}

public void exitLabel() {
  getWatcher().exitLabel();
}

public void enterIteration() {
  getWatcher().enterIteration();
}

public void exitIteration() {
  getWatcher().exitIteration();
}

public boolean isValidIteration(String label) {
  return getWatcher().isValidIteration(label);
}

public boolean isValidReturn() {
  return getWatcher().isValidReturn();
}

public boolean isValidIdentifier(String ident) {
  if ( ident == null ) {
    return true;
  }
  return getWatcher().isValidIdentifier(ident);
}

public boolean isValidIdentifierIfIdentifier(CommonTree tree) {
  return getWatcher().isValidIdentifierIfIdentifier(tree);
}

public boolean areValidParameterNames(List<String> names) {
  return getWatcher().areValidParameterNames( names );
}
	
private List<String> errors = new ArrayList<String>();
public void displayRecognitionError(String[] tokenNames, RecognitionException e) 
{
    String hdr = getErrorHeader(e);
    String msg = getErrorMessage(e, tokenNames);
    errors.add(hdr + " " + msg);
}
    
public List<String> getErrors() 
{
    return errors;
}

private String retrieveLT(int LTNumber) {
    if (null == input)
        return null;
    if (null == input.LT(LTNumber))
        return null;
    if (null == input.LT(LTNumber).getText())
        return null;

    return input.LT(LTNumber).getText();
}

private boolean validateLT(int LTNumber, String text) {
    String text2Validate = retrieveLT( LTNumber );
    return text2Validate == null ? false : text2Validate.equals(text);
}

private boolean validateIdentifierKey(String text) {
	return validateLT(1, text);
}

private final boolean isLeftHandSideAssign(RuleReturnScope lhs, Object[] cached)
{
	if (cached[0] != null)
	{
		return ((Boolean)cached[0]).booleanValue();
	}
	
	boolean result;
	if (isLeftHandSideExpression(lhs))
	{
		switch (input.LA(1))
		{
			case ASSIGN:
			case MULASS:
			case DIVASS:
			case MODASS:
			case ADDASS:
			case SUBASS:
			case SHLASS:
			case SHRASS:
			case SHUASS:
			case ANDASS:
			case XORASS:
			case ORASS:
				result = true;
				break;
			default:
				result = false;
				break;
		}
	}
	else
	{
		result = false;
	}
	
	cached[0] = new Boolean(result);
	return result;
}

private final static boolean isLeftHandSideExpression(RuleReturnScope lhs)
{
	if (lhs.getTree() == null) // e.g. during backtracking
	{
		return true;
	}
	else
	{
		switch (((Tree)lhs.getTree()).getType())
		{
		// primaryExpression
			case THIS:
			case Identifier:
			case NULL:
			case TRUE:
			case FALSE:
			case DecimalLiteral:
			case OctalIntegerLiteral:
			case HexIntegerLiteral:
			case StringLiteral:
			case ContinuedStringLiteral:
			case RegularExpressionLiteral:
			case ARRAY:
			case OBJECT:
		// functionExpression
			case FUNCTION:
		// newExpression
			case NEW:
		// leftHandSideExpression
			case CALL:
			case BYFIELD:
			case BYINDEX:
				return true;
			
			default:
				return false;
		}
	}
}
	
private final boolean isLeftHandSideIn(RuleReturnScope lhs, Object[] cached)
{
	if (cached[0] != null)
	{
		return ((Boolean)cached[0]).booleanValue();
	}
	
	boolean result = isLeftHandSideExpression(lhs) && (input.LA(1) == IN);
	cached[0] = new Boolean(result);
	return result;
}

private final void promoteEOL(ParserRuleReturnScope rule)
{
	// Get current token and its type (the possibly offending token).
	Token lt = input.LT(1);
	int la = lt.getType();
	
	// We only need to promote an EOL when the current token is offending (not a SEMIC, EOF, RBRACE, EOL or MultiLineComment).
	// EOL and MultiLineComment are not offending as they're already promoted in a previous call to this method.
	// Promoting an EOL means switching it from off channel to on channel.
	// A MultiLineComment gets promoted when it contains an EOL.
	if (!(la == SEMIC || la == EOF || la == RBRACE || la == EOL || la == MultiLineComment))
	{
		// Start on the possition before the current token and scan backwards off channel tokens until the previous on channel token.
		for (int ix = lt.getTokenIndex() - 1; ix > 0; ix--)
		{
			lt = input.get(ix);
			if (lt.getChannel() == Token.DEFAULT_CHANNEL)
			{
				// On channel token found: stop scanning.
				break;
			}
			else if (lt.getType() == EOL || (lt.getType() == MultiLineComment && lt.getText().matches("/.*\r\n|\r|\n")))
			{
				// We found our EOL: promote the token to on channel, position the input on it and reset the rule start.
				lt.setChannel(Token.DEFAULT_CHANNEL);
				input.seek(lt.getTokenIndex());
				if (rule != null)
				{
					rule.start = lt;
				}
				break;
			}
		}
	}
}	
}




//
// $<	A.1 Lexical Grammar (7)
//

// Added for lexing purposes

fragment BSLASH
	: '\\'
	;
	
fragment DQUOTE
	: '"'
	;
	
fragment SQUOTE
	: '\''
	;

// $<	Whitespace (7.2)

fragment TAB
	: '\u0009'
	;

fragment VT // Vertical TAB
	: '\u000b'
	;

fragment FF // Form Feed
	: '\u000c'
	;

fragment SP // Space
	: '\u0020'
	;

fragment NBSP // Non-Breaking Space
	: '\u00a0'
	;

fragment USP // Unicode Space Separator (rest of Unicode category Zs)
	: '\u1680'  // OGHAM SPACE MARK
	| '\u180E'  // MONGOLIAN VOWEL SEPARATOR
	| '\u2000'  // EN QUAD
	| '\u2001'  // EM QUAD
	| '\u2002'  // EN SPACE
	| '\u2003'  // EM SPACE
	| '\u2004'  // THREE-PER-EM SPACE
	| '\u2005'  // FOUR-PER-EM SPACE
	| '\u2006'  // SIX-PER-EM SPACE
	| '\u2007'  // FIGURE SPACE
	| '\u2008'  // PUNCTUATION SPACE
	| '\u2009'  // THIN SPACE
	| '\u200A'  // HAIR SPACE
	| '\u202F'  // NARROW NO-BREAK SPACE
	| '\u205F'  // MEDIUM MATHEMATICAL SPACE
	| '\u3000'  // IDEOGRAPHIC SPACE
	;

WhiteSpace
	: ( TAB | VT | FF | SP | NBSP | USP )+ { $channel = HIDDEN; }
	;

// $>

// $<	Line terminators (7.3)

fragment LF // Line Feed
	: '\n'
	;

fragment CR // Carriage Return
	: '\r'
	;

fragment LS // Line Separator
	: '\u2028'
	;

fragment PS // Paragraph Separator
	: '\u2029'
	;

fragment LineTerminator
	: CR | LF | LS | PS
	;
		
EOL
	: ( ( CR LF? ) | LF | LS | PS ) { $channel = HIDDEN; }
	;
// $>

// $<	Comments (7.4)

MultiLineComment
	: '/*' ( options { greedy = false; } : . )* '*/' { $channel = HIDDEN; }
	;

SingleLineComment
	: '//' ( ~( LineTerminator ) )* { $channel = HIDDEN; }
	;

// $>

// $<	Tokens (7.5)

token
	: reservedWord
	| Identifier
	| punctuator
	| numericLiteral
	| StringLiteral
	| ContinuedStringLiteral
	;

// $<	Reserved words (7.5.1)

reservedWord
	: keyword
	| futureReservedWord
	| NULL
	| booleanLiteral
	;

// $>
	
// $<	Keywords (7.5.2)

keyword
	: BREAK
	| CASE
	| CATCH
	| CONTINUE
	| DEFAULT
	| DELETE
	| DO
	| ELSE
	| FINALLY
	| FOR
	| FUNCTION
	| IF
	| IN
	| INSTANCEOF
	| NEW
	| RETURN
	| SWITCH
	| THIS
	| THROW
	| TRY
	| TYPEOF
	| VAR
	| VOID
	| WHILE
	| WITH
	;

// $>

// $<	Future reserved words (7.5.3)

futureReservedWord
	: //ABSTRACT
	//| BOOLEAN
	//| BYTE
	//| CHAR
	CLASS
	| CONST
	| DEBUGGER
	//| DOUBLE
	| ENUM
	| EXPORT
	| EXTENDS
	//| FINAL
	//| FLOAT
	//| GOTO
	//| IMPLEMENTS
	| IMPORT
	//| INT
	//| INTERFACE
	//| LONG
	//| NATIVE
	//| PACKAGE
	//| PRIVATE
	//| PROTECTED
	//| PUBLIC
	//| SHORT
	//| STATIC
	| SUPER
	//| SYNCHRONIZED
	//| THROWS
	//| TRANSIENT
	//| VOLATILE
	;

// $>

// $>
	
// $<	Identifiers (7.6)

fragment IdentifierStartASCII
	: 'a'..'z' | 'A'..'Z'
	| '$'
	| '_'
	| BSLASH 'u' HexDigit HexDigit HexDigit HexDigit // UnicodeEscapeSequence
	;

/*
The first two alternatives define how ANTLR can match ASCII characters which can be considered as part of an identifier.
The last alternative matches other characters in the unicode range that can be sonsidered as part of an identifier.
*/
fragment IdentifierPart
	: DecimalDigit
	| IdentifierStartASCII
	| { isIdentifierPartUnicode(input.LA(1)) }? { matchAny(); }
	;

fragment IdentifierNameASCIIStart
	: IdentifierStartASCII IdentifierPart*
	;

/*
The second alternative acts as an action driven fallback to evaluate other characters in the unicode range than the ones in the ASCII subset.
Due to the first alternative this grammar defines enough so that ANTLR can generate a lexer that correctly predicts identifiers with characters in the ASCII range.
In that way keywords, other reserved words and ASCII identifiers are recognized with standard ANTLR driven logic. When the first character for an identifier fails to 
match this ASCII definition, the lexer calls consumeIdentifierUnicodeStart because of the action in the alternative. This method checks whether the character matches 
as first character in ranges other than ASCII and consumes further characters belonging to the identifier with help of mIdentifierPart generated out of the 
IdentifierPart rule above.
*/
Identifier
	: IdentifierNameASCIIStart
	| { consumeIdentifierUnicodeStart(); }
	;

// $>

// $<	Punctuators (7.7)

punctuator
	: LBRACE
	| RBRACE
	| LPAREN
	| RPAREN
	| LBRACK
	| RBRACK
	| DOT
	| SEMIC
	| COMMA
	| LT
	| GT
	| LTE
	| GTE
	| EQ
	| NEQ
	| SAME
	| NSAME
	| ADD
	| SUB
	| MUL
	| MOD
	| INC
	| DEC
	| SHL
	| SHR
	| SHU
	| AND
	| OR
	| XOR
	| NOT
	| INV
	| LAND
	| LOR
	| QUE
	| COLON
	| ASSIGN
	| ADDASS
	| SUBASS
	| MULASS
	| MODASS
	| SHLASS
	| SHRASS
	| SHUASS
	| ANDASS
	| ORASS
	| XORASS
	| DIV
	| DIVASS
	;

// $>

// $<	Literals (7.8)

literal
	: NULL
	| booleanLiteral
	| numericLiteral
	| StringLiteral
	| ContinuedStringLiteral 
	| RegularExpressionLiteral
	;

booleanLiteral
	: TRUE
	| FALSE
	;

// $<	Numeric literals (7.8.3)

/*
Note: octal literals are described in the B Compatibility section.
These are removed from the standards but are here for backwards compatibility with earlier ECMAScript definitions.
*/

fragment DecimalDigit
	: '0'..'9'
	;

fragment HexDigit
	: DecimalDigit | 'a'..'f' | 'A'..'F'
	;

fragment OctalDigit
	: '0'..'7'
	;

fragment ExponentPart
	: ( 'e' | 'E' ) ( '+' | '-' )? DecimalDigit+
	;

fragment DecimalIntegerLiteral
	: '0'
	| '1'..'9' DecimalDigit*
	;

DecimalLiteral
	: DecimalIntegerLiteral '.' DecimalDigit* ExponentPart?
	| '.' DecimalDigit+ ExponentPart?
	| DecimalIntegerLiteral ExponentPart?
	;

OctalIntegerLiteral
	: '0' OctalDigit+
	;

HexIntegerLiteral
	: ( '0x' | '0X' ) HexDigit+
	;

numericLiteral
	: DecimalLiteral
	| { ! isStrict() }? OctalIntegerLiteral 
	| HexIntegerLiteral
	;

// $>

// $<	String literals (7.8.4)

/*
Note: octal escape sequences are described in the B Compatibility section.
These are removed from the standards but are here for backwards compatibility with earlier ECMAScript definitions.
*/
	
EscapeSequence
	:
	BSLASH
	( ~(DecimalDigit | 'x' | 'u' | LineTerminator )
	| ( OctalDigit | '0'..'3' OctalDigit OctalDigit | '4'..'7' OctalDigit OctalDigit )
	| 'x' HexDigit HexDigit
	| 'u' HexDigit HexDigit HexDigit HexDigit 
	)
	;
	
LineContinuation
    :
    BSLASH EOL
    ;

StringLiteral
@init{
  StringBuffer buf = new StringBuffer();
  boolean continuation = false;
}
	: 
	( SQUOTE 
	  ( ch=~( SQUOTE | BSLASH | LineTerminator ) {buf.append( (char) $ch ); }
	  | es=EscapeSequence {buf.append( $es.getText() ); }
	  | lc=LineContinuation { continuation = true; }
	  )* 
	  SQUOTE
	| DQUOTE 
	  ( ch=~( DQUOTE | BSLASH | LineTerminator ) {buf.append( (char) $ch ); }
	  | es=EscapeSequence {buf.append( $es.getText() ); }
	  | lc=LineContinuation { continuation = true; }
	  )* 
	  DQUOTE)
	  //{ setText(input.substring($start+1, $stop - 1)); }
	  { 
	    setText( buf.toString() );
	    if ( continuation ) {
	      $type = ContinuedStringLiteral; 
	    }
	  }
	;

// $>

// $<	Regular expression literals (7.8.5)

fragment BackslashSequence
	: BSLASH ~( LineTerminator )
	;

fragment RegularExpressionFirstChar
	: ~ ( LineTerminator | MUL | BSLASH | DIV )
	| BackslashSequence
	;

fragment RegularExpressionChar
	: ~ ( LineTerminator | BSLASH | DIV )
	| BackslashSequence
	;

RegularExpressionLiteral
	: { areRegularExpressionsEnabled() }?=> DIV RegularExpressionFirstChar RegularExpressionChar* DIV IdentifierPart*
	;

// $>

// $>

// $>

//
// $<	A.3 Expressions (11)
//

// $<Primary expressions (11.1)

primaryExpression
	: THIS
	| Identifier 
	| literal
	| arrayLiteral
	| objectLiteral
	| LPAREN! expression RPAREN!
	;

arrayLiteral
	: lb=LBRACK ( arrayItem ( COMMA arrayItem )* )? RBRACK
	-> ^( ARRAY[$lb, "ARRAY"] arrayItem* )
	;

arrayItem
	: ( expr=assignmentExpression | { input.LA(1) == COMMA || input.LA(1) == RBRACK }? )
	-> ^( ITEM $expr? )
	;

objectLiteral
@init {
  ObjectLiteralWatcher watcher = new ObjectLiteralWatcher( isStrict() );
}
	: lb=LBRACE ( propertyAssignment[watcher] ( COMMA propertyAssignment[watcher] )* COMMA? )? RBRACE
	-> ^( OBJECT[$lb, "OBJECT"] propertyAssignment* )
	;
	
propertyAssignment[ObjectLiteralWatcher watcher]
@init {
  getWatcher().startPropertyAssignment();
}
@after {
  getWatcher().endPropertyAssignment(retval);
}
    : 
      { input.LT(1).getText().equals( "get" ) }?=>propertyGet[watcher]
    | { input.LT(1).getText().equals( "set" ) }?=>propertySet[watcher]
    | nameValuePair[watcher]
    ;
	
nameValuePair[ObjectLiteralWatcher watcher]
	: pn=propertyName { watcher.addValue( $pn.name ) }? COLON assignmentExpression
	-> ^( NAMEDVALUE propertyName assignmentExpression )
	;

propertyName returns [String name]
	: id=Identifier     { $name = id.getText(); }
	| sl=StringLiteral  { $name = sl.getText(); }
	| csl=ContinuedStringLiteral  { $name = csl.getText(); }
	| nl=numericLiteral { $name = ((CommonTree)nl.getTree()).getText(); }
	| rw=reservedWord   { $name = ((CommonTree)rw.getTree()).getText(); } -> ^(Identifier[$reservedWord.text]) 
	;
	
propertyGet[ObjectLiteralWatcher watcher]
	: get=Identifier 
	  pn=propertyName { watcher.addGetter( $pn.name ) }? 
	  LPAREN RPAREN 
	    functionBody
	-> ^(PROPERTYGET[$get] propertyName functionBody)
	;
  
propertySet[ObjectLiteralWatcher watcher]
    : set=Identifier 
        pn=propertyName { watcher.addSetter( $pn.name ) }? 
        LPAREN 
          id=Identifier { isValidIdentifier( $id.getText() ) }?
        RPAREN 
          functionBody
	-> ^(PROPERTYSET[$set] propertyName $id functionBody)
    ;
    
    

// $>

// $<Left-hand-side expressions (11.2)

/*
Refactored some rules to make them LL(*) compliant:
all the expressions surrounding member selection and calls have been moved to leftHandSideExpression to make them right recursive
*/

memberExpression
	: primaryExpression
	| functionExpression
	//| newExpression
	;

newExpression
	: NEW^ leftHandSideExpression 
	;

	
arguments
	: LPAREN ( assignmentExpression ( COMMA assignmentExpression )* )? RPAREN
	-> ^( ARGS assignmentExpression* )
	;
	
leftHandSideExpression
	:
	( newExpression) | (
	(
		memberExpression 		-> memberExpression
	)
	(
		arguments			-> ^( CALL $leftHandSideExpression arguments )
		| LBRACK expression RBRACK	-> ^( BYINDEX $leftHandSideExpression expression )
		| DOT Identifier		-> ^( BYFIELD $leftHandSideExpression Identifier )
		| DOT reservedWord      -> ^( BYFIELD $leftHandSideExpression ^( Identifier[$reservedWord.text] ) )
	)* )
	;

// $>

// $<Postfix expressions (11.3)

/*
The specification states that there are no line terminators allowed before the postfix operators.
This is enforced by the call to promoteEOL in the action before ( INC | DEC ).
We only must promote EOLs when the la is INC or DEC because this production is chained as all expression rules.
In other words: only promote EOL when we are really in a postfix expression. A check on the la will ensure this.
*/
postfixExpression
	: lhs=leftHandSideExpression { if (input.LA(1) == INC || input.LA(1) == DEC) promoteEOL(null); } ( postfixOperator^ { isValidIdentifier(((CommonTree)lhs.getTree()).getText()) }? )?
	;
	
postfixOperator
	: op=INC { $op.setType(PINC); }
	| op=DEC { $op.setType(PDEC); }
	;

// $>

// $<Unary operators (11.4)

unaryExpression
	: postfixExpression
	| unaryOperator^ ue=unaryExpression { isValidIdentifierIfIdentifier( (CommonTree) ue.getTree() ) }?
	;
	
unaryOperator
	: DELETE
	| VOID
	| TYPEOF
	| INC
	| DEC
	| op=ADD { $op.setType(POS); }
	| op=SUB { $op.setType(NEG); }
	| INV
	| NOT
	;

// $>

// $<Multiplicative operators (11.5)

multiplicativeExpression
	: unaryExpression ( ( MUL | DIV | MOD )^ unaryExpression )*
	;

// $>

// $<Additive operators (11.6)

additiveExpression
	: multiplicativeExpression ( ( ADD | SUB )^ multiplicativeExpression )*
	;

// $>
	
// $<Bitwise shift operators (11.7)

shiftExpression
	: additiveExpression ( ( SHL | SHR | SHU )^ additiveExpression )*
	;

// $>
	
// $<Relational operators (11.8)

relationalExpression
	: shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF | IN )^ shiftExpression )*
	;

relationalExpressionNoIn
	: shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF )^ shiftExpression )*
	;

// $>
	
// $<Equality operators (11.9)

equalityExpression
	: relationalExpression ( ( EQ | NEQ | SAME | NSAME )^ relationalExpression )*
	;

equalityExpressionNoIn
	: relationalExpressionNoIn ( ( EQ | NEQ | SAME | NSAME )^ relationalExpressionNoIn )*
	;

// $>
		
// $<Binary bitwise operators (11.10)

bitwiseANDExpression
	: equalityExpression ( AND^ equalityExpression )*
	;

bitwiseANDExpressionNoIn
	: equalityExpressionNoIn ( AND^ equalityExpressionNoIn )*
	;
		
bitwiseXORExpression
	: bitwiseANDExpression ( XOR^ bitwiseANDExpression )*
	;
		
bitwiseXORExpressionNoIn
	: bitwiseANDExpressionNoIn ( XOR^ bitwiseANDExpressionNoIn )*
	;
	
bitwiseORExpression
	: bitwiseXORExpression ( OR^ bitwiseXORExpression )*
	;
	
bitwiseORExpressionNoIn
	: bitwiseXORExpressionNoIn ( OR^ bitwiseXORExpressionNoIn )*
	;

// $>
	
// $<Binary logical operators (11.11)

logicalANDExpression
	: bitwiseORExpression ( LAND^ bitwiseORExpression )*
	;

logicalANDExpressionNoIn
	: bitwiseORExpressionNoIn ( LAND^ bitwiseORExpressionNoIn )*
	;
	
logicalORExpression
	: logicalANDExpression ( LOR^ logicalANDExpression )*
	;
	
logicalORExpressionNoIn
	: logicalANDExpressionNoIn ( LOR^ logicalANDExpressionNoIn )*
	;

// $>
	
// $<Conditional operator (11.12)

conditionalExpression
	: logicalORExpression ( QUE^ assignmentExpression COLON! assignmentExpression )?
	;

conditionalExpressionNoIn
	: logicalORExpressionNoIn ( QUE^ assignmentExpressionNoIn COLON! assignmentExpressionNoIn )?
	;
	
// $>

// $<Assignment operators (11.13)

/*
The specification defines the AssignmentExpression rule as follows:
AssignmentExpression :
	ConditionalExpression 
	LeftHandSideExpression AssignmentOperator AssignmentExpression
This rule has a LL(*) conflict. Resolving this with a syntactical predicate will yield something like this:

assignmentExpression
	: ( leftHandSideExpression assignmentOperator )=> leftHandSideExpression assignmentOperator^ assignmentExpression
	| conditionalExpression
	;
assignmentOperator
	: ASSIGN | MULASS | DIVASS | MODASS | ADDASS | SUBASS | SHLASS | SHRASS | SHUASS | ANDASS | XORASS | ORASS
	;
	
But that didn't seem to work. Terence Par writes in his book that LL(*) conflicts in general can best be solved with auto backtracking. But that would be 
a performance killer for such a heavy used rule.
The solution I came up with is to always invoke the conditionalExpression first and than decide what to do based on the result of that rule.
When the rule results in a Tree that can't be coming from a left hand side expression, then we're done.
When it results in a Tree that is coming from a left hand side expression and the LA(1) is an assignment operator then parse the assignment operator
followed by the right recursive call.
*/
assignmentExpression
@init
{
	Object[] isLhs = new Object[1];
}
	: lhs=conditionalExpression ( { isLeftHandSideAssign(lhs, isLhs) && isValidIdentifier(((CommonTree)lhs.getTree()).getText()) }? assignmentOperator^ assignmentExpression )?	
	;

assignmentOperator
	: ASSIGN | MULASS | DIVASS | MODASS | ADDASS | SUBASS | SHLASS | SHRASS | SHUASS | ANDASS | XORASS | ORASS
	;

assignmentExpressionNoIn
@init
{
	Object[] isLhs = new Object[1];
}
	: lhs=conditionalExpressionNoIn
	( { isLeftHandSideAssign(lhs, isLhs) }? assignmentOperator^ assignmentExpressionNoIn )?
	;
	
// $>
	
// $<Comma operator (11.14)

expression
	: exprs+=assignmentExpression ( COMMA exprs+=assignmentExpression )*
	-> { $exprs.size() > 1 }? ^( CEXPR $exprs+ )
	-> $exprs
	;

expressionNoIn
	: exprs+=assignmentExpressionNoIn ( COMMA exprs+=assignmentExpressionNoIn )*
	-> { $exprs.size() > 1 }? ^( CEXPR $exprs+ )
	-> $exprs
	;

// $>

// $>
	
//
// $<	A.4 Statements (12)
//

/*
This rule handles semicolons reported by the lexer and situations where the ECMA 3 specification states there should be semicolons automaticly inserted.
The auto semicolons are not actually inserted but this rule behaves as if they were.

In the following situations an ECMA 3 parser should auto insert absent but grammaticly required semicolons:
- the current token is a right brace
- the current token is the end of file (EOF) token
- there is at least one end of line (EOL) token between the current token and the previous token.

The RBRACE is handled by matching it but not consuming it.
The EOF needs no further handling because it is not consumed by default.
The EOL situation is handled by promoting the EOL or MultiLineComment with an EOL present from off channel to on channel
and thus making it parseable instead of handling it as white space. This promoting is done in the action promoteEOL.
*/
semic
@init
{
	// Mark current position so we can unconsume a RBRACE.
	int marker = input.mark();
	// Promote EOL if appropriate	
	promoteEOL(retval);
}
	: SEMIC
	| EOF
	| RBRACE { input.rewind(marker); }
	| EOL | MultiLineComment // (with EOL in it)
	;

/*
To solve the ambiguity between block and objectLiteral via expressionStatement all but the block alternatives have been moved to statementTail.
Now when k = 1 and a semantical predicate is defined ANTLR generates code that always will prefer block when the LA(1) is a LBRACE.
This will result in the same behaviour that is described in the specification under 12.4 on the expressionStatement rule.
*/
statement
options
{
	k = 1 ;
}
	: 
	{ input.LA(1) == LBRACE }? block
	| printStatement
	| statementTail
	;
	
statementTail
	: variableStatement
	| emptyStatement
	| expressionStatement
	| ifStatement
	| iterationStatement
	| continueStatement
	| breakStatement
	| returnStatement
	| withStatement
	| labelledStatement
	| switchStatement
	| throwStatement
	| tryStatement
	;

// $<Block (12.1)

block
	: lb=LBRACE statement* RBRACE
	-> ^( BLOCK[$lb, "BLOCK"] statement* )
	;

// $>

// $<Print statement !CUSTOM!)

printStatement
	: print_key^ LPAREN! expression RPAREN!
	;

print_key
	:	{(validateIdentifierKey(DynJSSoftKeywords.PRINT))}?=>  id=Identifier
		->	SK_PRINT[$id]
	;

// $>


// $<Variable statement 12.2)

variableStatement
	: VAR variableDeclaration ( COMMA variableDeclaration )* semic
	-> ^( VAR variableDeclaration+ )
	;

variableDeclaration
	: id=Identifier { isValidIdentifier( id.getText() ) }? ( ASSIGN^ assignmentExpression )?
	;
	
variableDeclarationNoIn
	: id=Identifier { isValidIdentifier( id.getText() ) }? ( ASSIGN^ assignmentExpressionNoIn )?
	;

// $>
	
// $<Empty statement (12.3)

emptyStatement
	: SEMIC^ { getWatcher().emptyStatement(); }
	;

// $>
	
// $<Expression statement (12.4)

/*
The look ahead check on LBRACE and FUNCTION the specification mentions has been left out and its function, resolving the ambiguity between:
- functionExpression and functionDeclaration
- block and objectLiteral
are moved to the statement and sourceElement rules.
*/
expressionStatement
	: expression semic!
	;

// $>
	
// $<The if statement (12.5)

ifStatement
// The predicate is there just to get rid of the warning. ANTLR will handle the dangling else just fine.
	: IF LPAREN expression RPAREN statement ( { input.LA(1) == ELSE }? ELSE statement )?
	-> ^( IF expression statement+ )
	;

// $>
	
// $<Iteration statements (12.6)

iterationStatement
@init{
  enterIteration();
}
@after{
  exitIteration();
}
	: doStatement
	| whileStatement
	| forStatement
	;
	
doStatement
	//: DO statement semic? WHILE LPAREN expression RPAREN semic
	: DO statement WHILE LPAREN expression RPAREN semic
	-> ^( DO statement expression )
	;
	
whileStatement
	: WHILE^ LPAREN! expression RPAREN! statement
	;

/*
The forStatement production is refactored considerably as the specification contains a very none LL(*) compliant definition.
The initial version was like this:	

forStatement
	: FOR^ LPAREN! forControl RPAREN! statement
	;
forControl
options
{
	backtrack = true ;
	//k = 3 ;
}
	: stepClause
	| iterationClause
	;
stepClause
options
{
	memoize = true ;
}
	: ( ex1=expressionNoIn | var=VAR variableDeclarationNoIn ( COMMA variableDeclarationNoIn )* )? SEMIC ex2=expression? SEMIC ex3=expression?
	-> { $var != null }? ^( FORSTEP ^( VAR[$var] variableDeclarationNoIn+ ) ^( EXPR $ex2? ) ^( EXPR $ex3? ) )
	-> ^( FORSTEP ^( EXPR $ex1? ) ^( EXPR $ex2? ) ^( EXPR $ex3? ) )
	;
iterationClause
options
{
	memoize = true ;
}
	: ( leftHandSideExpression | var=VAR variableDeclarationNoIn ) IN expression
	-> { $var != null }? ^( FORITER ^( VAR[$var] variableDeclarationNoIn ) ^( EXPR expression ) )
	-> ^( FORITER ^( EXPR leftHandSideExpression ) ^( EXPR expression ) )
	;
	
But this completely relies on the backtrack feature and capabilities of ANTLR. 
Furthermore backtracking seemed to have 3 major drawbacks:
- the performance cost of backtracking is considerably
- didn't seem to work well with ANTLRWorks
- when introducing a k value to optimize the backtracking away, ANTLR runs out of heap space
*/
forStatement
	: FOR^ LPAREN! forControl RPAREN! statement
	;

forControl
	: forControlVar
	| forControlExpression
	| forControlSemic
	;

forControlVar
	: VAR variableDeclarationNoIn
	(
		(
			IN expression
			-> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) )
		)
		|
		(
			( COMMA variableDeclarationNoIn )* SEMIC ex1=expression? SEMIC ex2=expression?
			-> ^( FORSTEP ^( VAR variableDeclarationNoIn+ ) ^( EXPR $ex1? ) ^( EXPR $ex2? ) )
		)
	)
	;

forControlExpression
@init
{
	Object[] isLhs = new Object[1];
}
	: ex1=expressionNoIn
	( 
		{ isLeftHandSideIn(ex1, isLhs) }? (
			IN ex2=expression
			-> ^( FORITER ^( EXPR $ex1 ) ^( EXPR $ex2 ) )
		)
		|
		(
			SEMIC ex2=expression? SEMIC ex3=expression?
			-> ^( FORSTEP ^( EXPR $ex1 ) ^( EXPR $ex2? ) ^( EXPR $ex3? ) )
		)
	)
	;

forControlSemic
	: SEMIC ex1=expression? SEMIC ex2=expression?
	-> ^( FORSTEP ^( EXPR ) ^( EXPR $ex1? ) ^( EXPR $ex2? ) )
	;

// $>
	
// $<The continue statement (12.7)

/*
The action with the call to promoteEOL after CONTINUE is to enforce the semicolon insertion rule of the specification that there are
no line terminators allowed beween CONTINUE and the optional identifier.
As an optimization we check the la first to decide whether there is an identier following.
*/
continueStatement
	: CONTINUE^ { if (input.LA(1) == Identifier) promoteEOL(null); } Identifier? semic!
      { isValidIteration( $Identifier == null ? null : $Identifier.getText() ) }?
	;

// $>
	
// $<The break statement (12.8)

/*
The action with the call to promoteEOL after BREAK is to enforce the semicolon insertion rule of the specification that there are
no line terminators allowed beween BREAK and the optional identifier.
As an optimization we check the la first to decide whether there is an identier following.
*/
breakStatement
	: BREAK^ { if (input.LA(1) == Identifier) promoteEOL(null); } Identifier? semic!
	  { isValidIteration( $Identifier == null ? null : $Identifier.getText() ) }?
	;

// $>
	
// $<The return statement (12.9)

/*
The action calling promoteEOL after RETURN ensures that there are no line terminators between RETURN and the optional expression as the specification states.
When there are these get promoted to on channel and thus virtual semicolon wannabees.
So the folowing code:

return
1

will be parsed as:

return;
1;
*/
returnStatement
	: RETURN^ { promoteEOL(null); } expression? semic!
	{ isValidReturn() }?
	;

// $>
	
// $<The with statement (12.10)

withStatement
	: { ! isStrict() }? WITH^ LPAREN! expression RPAREN! statement
	;

// $>
	
// $<The switch statement (12.11)

switchStatement
@init
{
  int defaultClauseCount = 0;
  enterIteration();
}
@after{
  exitIteration();
}
	: SWITCH LPAREN expression RPAREN LBRACE ( { defaultClauseCount == 0 }?=> defaultClause { defaultClauseCount++; } | caseClause )* RBRACE
	-> ^( SWITCH expression defaultClause? caseClause* )
	;

caseClause
	: CASE^ expression COLON! statement*
	;
	
defaultClause
	: DEFAULT^ COLON! statement*
	;

// $>
	
// $<Labelled statements (12.12)

labelledStatement
	: Identifier { enterLabel( $Identifier.getText() ); } COLON statement { exitLabel(); }
	-> ^( LABELLED[$COLON] Identifier statement )
	;

// $>
	
// $<The throw statement (12.13)

/*
The action calling promoteEOL after THROW ensures that there are no line terminators between THROW and the expression as the specification states.
When there are line terminators these get promoted to on channel and thus to virtual semicolon wannabees.
So the folowing code:

throw
new Error()

will be parsed as:

throw;
new Error();

which will yield a recognition exception!
*/
throwStatement
	: THROW^ { promoteEOL(null); } expression semic!
	;

// $>
	
// $<The try statement (12.14)

tryStatement
	: TRY^ block ( catchClause finallyClause? | finallyClause )
	;
	
catchClause
	: CATCH^ LPAREN! id=Identifier { isValidIdentifier( id.getText() ) }? RPAREN! block
	;
	
finallyClause
	: FINALLY^ block
	;

// $>

// $>

//
// $<	A.5 Functions and Programs (13, 14)
//

// $<	Function Definition (13)

functionDeclaration
@init {
  getWatcher().startFunctionDeclaration(); 
}
@after {
  getWatcher().endFunctionDeclaration( retval ); 
}
	: FUNCTION name=Identifier { isValidIdentifier( name.getText() ) }? fpl=formalParameterList functionBody
	{ areValidParameterNames( $fpl.names ) }?
	-> ^( FUNCTION $name formalParameterList functionBody )
	;

functionExpression
@init {
  getWatcher().startFunctionExpression(); 
}
@after {
  getWatcher().endFunctionExpression( retval ); 
}
	: 
	FUNCTION name=Identifier? { ( (name == null) ? true : isValidIdentifier( name.getText() ) ) }? fpl=formalParameterList functionBody 
	{ areValidParameterNames( $fpl.names ) }?
	-> ^( FUNCTION $name? formalParameterList functionBody )
	;

formalParameterList returns[List<String> names ]
@init {
  List<String> paramNames = new ArrayList<>();
}
	: LPAREN 
	    ( ident=Identifier { paramNames.add( ident.getText() ); } 
	      ( COMMA ident=Identifier { paramNames.add( ident.getText() ); } )* )? 
	  RPAREN
	  { $names = paramNames; }
	-> ^( ARGS Identifier* )
	;

functionBody
	: lb=LBRACE 
	    (se=sourceElement 
	      { getWatcher().sourceElement( se ); } 
	    )* 
	  RBRACE
	-> ^( BLOCK[$lb, "BLOCK"] sourceElement* )
	;

// $>
	
// $<	Program (14)

program
@init{
  getWatcher().startProgram( retval );
}
@after {
  getWatcher().endProgram( retval );
}
	: (se=sourceElement 
	    { getWatcher().sourceElement( se ); } 
	  )* 
	-> ^( PROGRAM["PROGRAM"] sourceElement* )
	;

/*
By setting k  to 1 for this rule and adding the semantical predicate ANTRL will generate code that will always prefer functionDeclararion over functionExpression
here and therefor remove the ambiguity between these to production.
This will result in the same behaviour that is described in the specification under 12.4 on the expressionStatement rule.
*/
sourceElement
options
{
	k = 1 ;
}
	: { input.LA(1) == FUNCTION }? functionDeclaration
	| statement
	;

// $>

// $>
