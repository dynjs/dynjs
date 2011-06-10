// $ANTLR 3.3 Nov 30, 2010 12:45:30 /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g 2011-06-10 09:42:12

package org.dynjs.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.debug.*;
import java.io.IOException;

import org.antlr.runtime.tree.*;

public class ES3Parser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NULL", "TRUE", "FALSE", "BREAK", "CASE", "CATCH", "CONTINUE", "DEFAULT", "DELETE", "DO", "ELSE", "FINALLY", "FOR", "FUNCTION", "IF", "IN", "INSTANCEOF", "NEW", "RETURN", "SWITCH", "THIS", "THROW", "TRY", "TYPEOF", "VAR", "VOID", "WHILE", "WITH", "ABSTRACT", "BOOLEAN", "BYTE", "CHAR", "CLASS", "CONST", "DEBUGGER", "DOUBLE", "ENUM", "EXPORT", "EXTENDS", "FINAL", "FLOAT", "GOTO", "IMPLEMENTS", "IMPORT", "INT", "INTERFACE", "LONG", "NATIVE", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "SHORT", "STATIC", "SUPER", "SYNCHRONIZED", "THROWS", "TRANSIENT", "VOLATILE", "LBRACE", "RBRACE", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "DOT", "SEMIC", "COMMA", "LT", "GT", "LTE", "GTE", "EQ", "NEQ", "SAME", "NSAME", "ADD", "SUB", "MUL", "MOD", "INC", "DEC", "SHL", "SHR", "SHU", "AND", "OR", "XOR", "NOT", "INV", "LAND", "LOR", "QUE", "COLON", "ASSIGN", "ADDASS", "SUBASS", "MULASS", "MODASS", "SHLASS", "SHRASS", "SHUASS", "ANDASS", "ORASS", "XORASS", "DIV", "DIVASS", "ARGS", "ARRAY", "BLOCK", "BYFIELD", "BYINDEX", "CALL", "CEXPR", "EXPR", "FORITER", "FORSTEP", "ITEM", "LABELLED", "NAMEDVALUE", "NEG", "OBJECT", "PAREXPR", "PDEC", "PINC", "POS", "SK_PRINT", "BSLASH", "DQUOTE", "SQUOTE", "TAB", "VT", "FF", "SP", "NBSP", "USP", "WhiteSpace", "LF", "CR", "LS", "PS", "LineTerminator", "EOL", "MultiLineComment", "SingleLineComment", "Identifier", "StringLiteral", "HexDigit", "IdentifierStartASCII", "DecimalDigit", "IdentifierPart", "IdentifierNameASCIIStart", "RegularExpressionLiteral", "OctalDigit", "ExponentPart", "DecimalIntegerLiteral", "DecimalLiteral", "OctalIntegerLiteral", "HexIntegerLiteral", "CharacterEscapeSequence", "ZeroToThree", "OctalEscapeSequence", "HexEscapeSequence", "UnicodeEscapeSequence", "EscapeSequence", "BackslashSequence", "RegularExpressionFirstChar", "RegularExpressionChar"
    };
    public static final int EOF=-1;
    public static final int NULL=4;
    public static final int TRUE=5;
    public static final int FALSE=6;
    public static final int BREAK=7;
    public static final int CASE=8;
    public static final int CATCH=9;
    public static final int CONTINUE=10;
    public static final int DEFAULT=11;
    public static final int DELETE=12;
    public static final int DO=13;
    public static final int ELSE=14;
    public static final int FINALLY=15;
    public static final int FOR=16;
    public static final int FUNCTION=17;
    public static final int IF=18;
    public static final int IN=19;
    public static final int INSTANCEOF=20;
    public static final int NEW=21;
    public static final int RETURN=22;
    public static final int SWITCH=23;
    public static final int THIS=24;
    public static final int THROW=25;
    public static final int TRY=26;
    public static final int TYPEOF=27;
    public static final int VAR=28;
    public static final int VOID=29;
    public static final int WHILE=30;
    public static final int WITH=31;
    public static final int ABSTRACT=32;
    public static final int BOOLEAN=33;
    public static final int BYTE=34;
    public static final int CHAR=35;
    public static final int CLASS=36;
    public static final int CONST=37;
    public static final int DEBUGGER=38;
    public static final int DOUBLE=39;
    public static final int ENUM=40;
    public static final int EXPORT=41;
    public static final int EXTENDS=42;
    public static final int FINAL=43;
    public static final int FLOAT=44;
    public static final int GOTO=45;
    public static final int IMPLEMENTS=46;
    public static final int IMPORT=47;
    public static final int INT=48;
    public static final int INTERFACE=49;
    public static final int LONG=50;
    public static final int NATIVE=51;
    public static final int PACKAGE=52;
    public static final int PRIVATE=53;
    public static final int PROTECTED=54;
    public static final int PUBLIC=55;
    public static final int SHORT=56;
    public static final int STATIC=57;
    public static final int SUPER=58;
    public static final int SYNCHRONIZED=59;
    public static final int THROWS=60;
    public static final int TRANSIENT=61;
    public static final int VOLATILE=62;
    public static final int LBRACE=63;
    public static final int RBRACE=64;
    public static final int LPAREN=65;
    public static final int RPAREN=66;
    public static final int LBRACK=67;
    public static final int RBRACK=68;
    public static final int DOT=69;
    public static final int SEMIC=70;
    public static final int COMMA=71;
    public static final int LT=72;
    public static final int GT=73;
    public static final int LTE=74;
    public static final int GTE=75;
    public static final int EQ=76;
    public static final int NEQ=77;
    public static final int SAME=78;
    public static final int NSAME=79;
    public static final int ADD=80;
    public static final int SUB=81;
    public static final int MUL=82;
    public static final int MOD=83;
    public static final int INC=84;
    public static final int DEC=85;
    public static final int SHL=86;
    public static final int SHR=87;
    public static final int SHU=88;
    public static final int AND=89;
    public static final int OR=90;
    public static final int XOR=91;
    public static final int NOT=92;
    public static final int INV=93;
    public static final int LAND=94;
    public static final int LOR=95;
    public static final int QUE=96;
    public static final int COLON=97;
    public static final int ASSIGN=98;
    public static final int ADDASS=99;
    public static final int SUBASS=100;
    public static final int MULASS=101;
    public static final int MODASS=102;
    public static final int SHLASS=103;
    public static final int SHRASS=104;
    public static final int SHUASS=105;
    public static final int ANDASS=106;
    public static final int ORASS=107;
    public static final int XORASS=108;
    public static final int DIV=109;
    public static final int DIVASS=110;
    public static final int ARGS=111;
    public static final int ARRAY=112;
    public static final int BLOCK=113;
    public static final int BYFIELD=114;
    public static final int BYINDEX=115;
    public static final int CALL=116;
    public static final int CEXPR=117;
    public static final int EXPR=118;
    public static final int FORITER=119;
    public static final int FORSTEP=120;
    public static final int ITEM=121;
    public static final int LABELLED=122;
    public static final int NAMEDVALUE=123;
    public static final int NEG=124;
    public static final int OBJECT=125;
    public static final int PAREXPR=126;
    public static final int PDEC=127;
    public static final int PINC=128;
    public static final int POS=129;
    public static final int SK_PRINT=130;
    public static final int BSLASH=131;
    public static final int DQUOTE=132;
    public static final int SQUOTE=133;
    public static final int TAB=134;
    public static final int VT=135;
    public static final int FF=136;
    public static final int SP=137;
    public static final int NBSP=138;
    public static final int USP=139;
    public static final int WhiteSpace=140;
    public static final int LF=141;
    public static final int CR=142;
    public static final int LS=143;
    public static final int PS=144;
    public static final int LineTerminator=145;
    public static final int EOL=146;
    public static final int MultiLineComment=147;
    public static final int SingleLineComment=148;
    public static final int Identifier=149;
    public static final int StringLiteral=150;
    public static final int HexDigit=151;
    public static final int IdentifierStartASCII=152;
    public static final int DecimalDigit=153;
    public static final int IdentifierPart=154;
    public static final int IdentifierNameASCIIStart=155;
    public static final int RegularExpressionLiteral=156;
    public static final int OctalDigit=157;
    public static final int ExponentPart=158;
    public static final int DecimalIntegerLiteral=159;
    public static final int DecimalLiteral=160;
    public static final int OctalIntegerLiteral=161;
    public static final int HexIntegerLiteral=162;
    public static final int CharacterEscapeSequence=163;
    public static final int ZeroToThree=164;
    public static final int OctalEscapeSequence=165;
    public static final int HexEscapeSequence=166;
    public static final int UnicodeEscapeSequence=167;
    public static final int EscapeSequence=168;
    public static final int BackslashSequence=169;
    public static final int RegularExpressionFirstChar=170;
    public static final int RegularExpressionChar=171;

    // delegates
    // delegators

    public static final String[] ruleNames = new String[] {
        "invalidRule", "emptyStatement", "futureReservedWord", "punctuator", 
        "forControl", "tryStatement", "arrayItem", "formalParameterList", 
        "relationalExpression", "program", "conditionalExpression", "assignmentOperator", 
        "block", "logicalANDExpression", "expression", "additiveExpression", 
        "forStatement", "switchStatement", "breakStatement", "arrayLiteral", 
        "functionDeclaration", "continueStatement", "variableStatement", 
        "bitwiseORExpression", "whileStatement", "literal", "variableDeclaration", 
        "objectLiteral", "forControlSemic", "withStatement", "bitwiseXORExpression", 
        "statementTail", "logicalORExpression", "conditionalExpressionNoIn", 
        "relationalExpressionNoIn", "primaryExpression", "unaryExpression", 
        "ifStatement", "iterationStatement", "newExpression", "equalityExpressionNoIn", 
        "defaultClause", "shiftExpression", "variableDeclarationNoIn", "unaryOperator", 
        "logicalANDExpressionNoIn", "token", "bitwiseORExpressionNoIn", 
        "functionExpression", "assignmentExpressionNoIn", "propertyName", 
        "functionBody", "print_key", "nameValuePair", "equalityExpression", 
        "throwStatement", "labelledStatement", "multiplicativeExpression", 
        "expressionStatement", "booleanLiteral", "arguments", "forControlVar", 
        "bitwiseANDExpressionNoIn", "bitwiseXORExpressionNoIn", "returnStatement", 
        "forControlExpression", "catchClause", "bitwiseANDExpression", "leftHandSideExpression", 
        "numericLiteral", "printStatement", "caseClause", "logicalORExpressionNoIn", 
        "reservedWord", "sourceElement", "keyword", "assignmentExpression", 
        "memberExpression", "finallyClause", "semic", "postfixOperator", 
        "postfixExpression", "statement", "doStatement", "expressionNoIn"
    };
    public static final boolean[] decisionCanBacktrack = new boolean[] {
        false, // invalid decision
        false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, 
            false
    };

     
        public int ruleLevel = 0;
        public int getRuleLevel() { return ruleLevel; }
        public void incRuleLevel() { ruleLevel++; }
        public void decRuleLevel() { ruleLevel--; }
        public ES3Parser(TokenStream input) {
            this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
        }
        public ES3Parser(TokenStream input, int port, RecognizerSharedState state) {
            super(input, state);
            DebugEventSocketProxy proxy =
                new DebugEventSocketProxy(this,port,adaptor);
            setDebugListener(proxy);
            setTokenStream(new DebugTokenStream(input,proxy));
            try {
                proxy.handshake();
            }
            catch (IOException ioe) {
                reportError(ioe);
            }
            TreeAdaptor adap = new CommonTreeAdaptor();
            setTreeAdaptor(adap);
            proxy.setTreeAdaptor(adap);
        }
    public ES3Parser(TokenStream input, DebugEventListener dbg) {
        super(input, dbg);

         
        TreeAdaptor adap = new CommonTreeAdaptor();
        setTreeAdaptor(adap);

    }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }

    protected DebugTreeAdaptor adaptor;
    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = new DebugTreeAdaptor(dbg,adaptor);

    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }


    public String[] getTokenNames() { return ES3Parser.tokenNames; }
    public String getGrammarFileName() { return "/Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g"; }



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
    			case RegularExpressionLiteral:
    			case ARRAY:
    			case OBJECT:
    			case PAREXPR:
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


    public static class token_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "token"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:568:1: token : ( reservedWord | Identifier | punctuator | numericLiteral | StringLiteral );
    public final ES3Parser.token_return token() throws RecognitionException {
        ES3Parser.token_return retval = new ES3Parser.token_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Identifier2=null;
        Token StringLiteral5=null;
        ES3Parser.reservedWord_return reservedWord1 = null;

        ES3Parser.punctuator_return punctuator3 = null;

        ES3Parser.numericLiteral_return numericLiteral4 = null;


        Object Identifier2_tree=null;
        Object StringLiteral5_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "token");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(568, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:569:2: ( reservedWord | Identifier | punctuator | numericLiteral | StringLiteral )
            int alt1=5;
            try { dbg.enterDecision(1, decisionCanBacktrack[1]);

            switch ( input.LA(1) ) {
            case NULL:
            case TRUE:
            case FALSE:
            case BREAK:
            case CASE:
            case CATCH:
            case CONTINUE:
            case DEFAULT:
            case DELETE:
            case DO:
            case ELSE:
            case FINALLY:
            case FOR:
            case FUNCTION:
            case IF:
            case IN:
            case INSTANCEOF:
            case NEW:
            case RETURN:
            case SWITCH:
            case THIS:
            case THROW:
            case TRY:
            case TYPEOF:
            case VAR:
            case VOID:
            case WHILE:
            case WITH:
            case ABSTRACT:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CLASS:
            case CONST:
            case DEBUGGER:
            case DOUBLE:
            case ENUM:
            case EXPORT:
            case EXTENDS:
            case FINAL:
            case FLOAT:
            case GOTO:
            case IMPLEMENTS:
            case IMPORT:
            case INT:
            case INTERFACE:
            case LONG:
            case NATIVE:
            case PACKAGE:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case SHORT:
            case STATIC:
            case SUPER:
            case SYNCHRONIZED:
            case THROWS:
            case TRANSIENT:
            case VOLATILE:
                {
                alt1=1;
                }
                break;
            case Identifier:
                {
                alt1=2;
                }
                break;
            case LBRACE:
            case RBRACE:
            case LPAREN:
            case RPAREN:
            case LBRACK:
            case RBRACK:
            case DOT:
            case SEMIC:
            case COMMA:
            case LT:
            case GT:
            case LTE:
            case GTE:
            case EQ:
            case NEQ:
            case SAME:
            case NSAME:
            case ADD:
            case SUB:
            case MUL:
            case MOD:
            case INC:
            case DEC:
            case SHL:
            case SHR:
            case SHU:
            case AND:
            case OR:
            case XOR:
            case NOT:
            case INV:
            case LAND:
            case LOR:
            case QUE:
            case COLON:
            case ASSIGN:
            case ADDASS:
            case SUBASS:
            case MULASS:
            case MODASS:
            case SHLASS:
            case SHRASS:
            case SHUASS:
            case ANDASS:
            case ORASS:
            case XORASS:
            case DIV:
            case DIVASS:
                {
                alt1=3;
                }
                break;
            case DecimalLiteral:
            case OctalIntegerLiteral:
            case HexIntegerLiteral:
                {
                alt1=4;
                }
                break;
            case StringLiteral:
                {
                alt1=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:569:4: reservedWord
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(569,4);
                    pushFollow(FOLLOW_reservedWord_in_token1751);
                    reservedWord1=reservedWord();

                    state._fsp--;

                    adaptor.addChild(root_0, reservedWord1.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:570:4: Identifier
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(570,4);
                    Identifier2=(Token)match(input,Identifier,FOLLOW_Identifier_in_token1756); 
                    Identifier2_tree = (Object)adaptor.create(Identifier2);
                    adaptor.addChild(root_0, Identifier2_tree);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:571:4: punctuator
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(571,4);
                    pushFollow(FOLLOW_punctuator_in_token1761);
                    punctuator3=punctuator();

                    state._fsp--;

                    adaptor.addChild(root_0, punctuator3.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:572:4: numericLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(572,4);
                    pushFollow(FOLLOW_numericLiteral_in_token1766);
                    numericLiteral4=numericLiteral();

                    state._fsp--;

                    adaptor.addChild(root_0, numericLiteral4.getTree());

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:573:4: StringLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(573,4);
                    StringLiteral5=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_token1771); 
                    StringLiteral5_tree = (Object)adaptor.create(StringLiteral5);
                    adaptor.addChild(root_0, StringLiteral5_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(574, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "token");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "token"

    public static class reservedWord_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "reservedWord"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:578:1: reservedWord : ( keyword | futureReservedWord | NULL | booleanLiteral );
    public final ES3Parser.reservedWord_return reservedWord() throws RecognitionException {
        ES3Parser.reservedWord_return retval = new ES3Parser.reservedWord_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NULL8=null;
        ES3Parser.keyword_return keyword6 = null;

        ES3Parser.futureReservedWord_return futureReservedWord7 = null;

        ES3Parser.booleanLiteral_return booleanLiteral9 = null;


        Object NULL8_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "reservedWord");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(578, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:579:2: ( keyword | futureReservedWord | NULL | booleanLiteral )
            int alt2=4;
            try { dbg.enterDecision(2, decisionCanBacktrack[2]);

            switch ( input.LA(1) ) {
            case BREAK:
            case CASE:
            case CATCH:
            case CONTINUE:
            case DEFAULT:
            case DELETE:
            case DO:
            case ELSE:
            case FINALLY:
            case FOR:
            case FUNCTION:
            case IF:
            case IN:
            case INSTANCEOF:
            case NEW:
            case RETURN:
            case SWITCH:
            case THIS:
            case THROW:
            case TRY:
            case TYPEOF:
            case VAR:
            case VOID:
            case WHILE:
            case WITH:
                {
                alt2=1;
                }
                break;
            case ABSTRACT:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CLASS:
            case CONST:
            case DEBUGGER:
            case DOUBLE:
            case ENUM:
            case EXPORT:
            case EXTENDS:
            case FINAL:
            case FLOAT:
            case GOTO:
            case IMPLEMENTS:
            case IMPORT:
            case INT:
            case INTERFACE:
            case LONG:
            case NATIVE:
            case PACKAGE:
            case PRIVATE:
            case PROTECTED:
            case PUBLIC:
            case SHORT:
            case STATIC:
            case SUPER:
            case SYNCHRONIZED:
            case THROWS:
            case TRANSIENT:
            case VOLATILE:
                {
                alt2=2;
                }
                break;
            case NULL:
                {
                alt2=3;
                }
                break;
            case TRUE:
            case FALSE:
                {
                alt2=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(2);}

            switch (alt2) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:579:4: keyword
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(579,4);
                    pushFollow(FOLLOW_keyword_in_reservedWord1784);
                    keyword6=keyword();

                    state._fsp--;

                    adaptor.addChild(root_0, keyword6.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:580:4: futureReservedWord
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(580,4);
                    pushFollow(FOLLOW_futureReservedWord_in_reservedWord1789);
                    futureReservedWord7=futureReservedWord();

                    state._fsp--;

                    adaptor.addChild(root_0, futureReservedWord7.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:581:4: NULL
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(581,4);
                    NULL8=(Token)match(input,NULL,FOLLOW_NULL_in_reservedWord1794); 
                    NULL8_tree = (Object)adaptor.create(NULL8);
                    adaptor.addChild(root_0, NULL8_tree);


                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:582:4: booleanLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(582,4);
                    pushFollow(FOLLOW_booleanLiteral_in_reservedWord1799);
                    booleanLiteral9=booleanLiteral();

                    state._fsp--;

                    adaptor.addChild(root_0, booleanLiteral9.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(583, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "reservedWord");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "reservedWord"

    public static class keyword_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "keyword"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:589:1: keyword : ( BREAK | CASE | CATCH | CONTINUE | DEFAULT | DELETE | DO | ELSE | FINALLY | FOR | FUNCTION | IF | IN | INSTANCEOF | NEW | RETURN | SWITCH | THIS | THROW | TRY | TYPEOF | VAR | VOID | WHILE | WITH );
    public final ES3Parser.keyword_return keyword() throws RecognitionException {
        ES3Parser.keyword_return retval = new ES3Parser.keyword_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set10=null;

        Object set10_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "keyword");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(589, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:590:2: ( BREAK | CASE | CATCH | CONTINUE | DEFAULT | DELETE | DO | ELSE | FINALLY | FOR | FUNCTION | IF | IN | INSTANCEOF | NEW | RETURN | SWITCH | THIS | THROW | TRY | TYPEOF | VAR | VOID | WHILE | WITH )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(590,2);
            set10=(Token)input.LT(1);
            if ( (input.LA(1)>=BREAK && input.LA(1)<=WITH) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set10));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(615, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "keyword");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "keyword"

    public static class futureReservedWord_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "futureReservedWord"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:621:1: futureReservedWord : ( ABSTRACT | BOOLEAN | BYTE | CHAR | CLASS | CONST | DEBUGGER | DOUBLE | ENUM | EXPORT | EXTENDS | FINAL | FLOAT | GOTO | IMPLEMENTS | IMPORT | INT | INTERFACE | LONG | NATIVE | PACKAGE | PRIVATE | PROTECTED | PUBLIC | SHORT | STATIC | SUPER | SYNCHRONIZED | THROWS | TRANSIENT | VOLATILE );
    public final ES3Parser.futureReservedWord_return futureReservedWord() throws RecognitionException {
        ES3Parser.futureReservedWord_return retval = new ES3Parser.futureReservedWord_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set11=null;

        Object set11_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "futureReservedWord");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(621, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:622:2: ( ABSTRACT | BOOLEAN | BYTE | CHAR | CLASS | CONST | DEBUGGER | DOUBLE | ENUM | EXPORT | EXTENDS | FINAL | FLOAT | GOTO | IMPLEMENTS | IMPORT | INT | INTERFACE | LONG | NATIVE | PACKAGE | PRIVATE | PROTECTED | PUBLIC | SHORT | STATIC | SUPER | SYNCHRONIZED | THROWS | TRANSIENT | VOLATILE )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(622,2);
            set11=(Token)input.LT(1);
            if ( (input.LA(1)>=ABSTRACT && input.LA(1)<=VOLATILE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set11));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(653, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "futureReservedWord");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "futureReservedWord"

    public static class punctuator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "punctuator"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:699:1: punctuator : ( LBRACE | RBRACE | LPAREN | RPAREN | LBRACK | RBRACK | DOT | SEMIC | COMMA | LT | GT | LTE | GTE | EQ | NEQ | SAME | NSAME | ADD | SUB | MUL | MOD | INC | DEC | SHL | SHR | SHU | AND | OR | XOR | NOT | INV | LAND | LOR | QUE | COLON | ASSIGN | ADDASS | SUBASS | MULASS | MODASS | SHLASS | SHRASS | SHUASS | ANDASS | ORASS | XORASS | DIV | DIVASS );
    public final ES3Parser.punctuator_return punctuator() throws RecognitionException {
        ES3Parser.punctuator_return retval = new ES3Parser.punctuator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set12=null;

        Object set12_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "punctuator");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(699, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:700:2: ( LBRACE | RBRACE | LPAREN | RPAREN | LBRACK | RBRACK | DOT | SEMIC | COMMA | LT | GT | LTE | GTE | EQ | NEQ | SAME | NSAME | ADD | SUB | MUL | MOD | INC | DEC | SHL | SHR | SHU | AND | OR | XOR | NOT | INV | LAND | LOR | QUE | COLON | ASSIGN | ADDASS | SUBASS | MULASS | MODASS | SHLASS | SHRASS | SHUASS | ANDASS | ORASS | XORASS | DIV | DIVASS )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(700,2);
            set12=(Token)input.LT(1);
            if ( (input.LA(1)>=LBRACE && input.LA(1)<=DIVASS) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set12));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(748, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "punctuator");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "punctuator"

    public static class literal_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:754:1: literal : ( NULL | booleanLiteral | numericLiteral | StringLiteral | RegularExpressionLiteral );
    public final ES3Parser.literal_return literal() throws RecognitionException {
        ES3Parser.literal_return retval = new ES3Parser.literal_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NULL13=null;
        Token StringLiteral16=null;
        Token RegularExpressionLiteral17=null;
        ES3Parser.booleanLiteral_return booleanLiteral14 = null;

        ES3Parser.numericLiteral_return numericLiteral15 = null;


        Object NULL13_tree=null;
        Object StringLiteral16_tree=null;
        Object RegularExpressionLiteral17_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "literal");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(754, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:755:2: ( NULL | booleanLiteral | numericLiteral | StringLiteral | RegularExpressionLiteral )
            int alt3=5;
            try { dbg.enterDecision(3, decisionCanBacktrack[3]);

            switch ( input.LA(1) ) {
            case NULL:
                {
                alt3=1;
                }
                break;
            case TRUE:
            case FALSE:
                {
                alt3=2;
                }
                break;
            case DecimalLiteral:
            case OctalIntegerLiteral:
            case HexIntegerLiteral:
                {
                alt3=3;
                }
                break;
            case StringLiteral:
                {
                alt3=4;
                }
                break;
            case RegularExpressionLiteral:
                {
                alt3=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(3);}

            switch (alt3) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:755:4: NULL
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(755,4);
                    NULL13=(Token)match(input,NULL,FOLLOW_NULL_in_literal2480); 
                    NULL13_tree = (Object)adaptor.create(NULL13);
                    adaptor.addChild(root_0, NULL13_tree);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:756:4: booleanLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(756,4);
                    pushFollow(FOLLOW_booleanLiteral_in_literal2485);
                    booleanLiteral14=booleanLiteral();

                    state._fsp--;

                    adaptor.addChild(root_0, booleanLiteral14.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:757:4: numericLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(757,4);
                    pushFollow(FOLLOW_numericLiteral_in_literal2490);
                    numericLiteral15=numericLiteral();

                    state._fsp--;

                    adaptor.addChild(root_0, numericLiteral15.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:758:4: StringLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(758,4);
                    StringLiteral16=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_literal2495); 
                    StringLiteral16_tree = (Object)adaptor.create(StringLiteral16);
                    adaptor.addChild(root_0, StringLiteral16_tree);


                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:759:4: RegularExpressionLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(759,4);
                    RegularExpressionLiteral17=(Token)match(input,RegularExpressionLiteral,FOLLOW_RegularExpressionLiteral_in_literal2500); 
                    RegularExpressionLiteral17_tree = (Object)adaptor.create(RegularExpressionLiteral17);
                    adaptor.addChild(root_0, RegularExpressionLiteral17_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(760, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "literal");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "literal"

    public static class booleanLiteral_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "booleanLiteral"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:762:1: booleanLiteral : ( TRUE | FALSE );
    public final ES3Parser.booleanLiteral_return booleanLiteral() throws RecognitionException {
        ES3Parser.booleanLiteral_return retval = new ES3Parser.booleanLiteral_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set18=null;

        Object set18_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "booleanLiteral");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(762, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:763:2: ( TRUE | FALSE )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(763,2);
            set18=(Token)input.LT(1);
            if ( (input.LA(1)>=TRUE && input.LA(1)<=FALSE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set18));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(765, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "booleanLiteral");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "booleanLiteral"

    public static class numericLiteral_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numericLiteral"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:809:1: numericLiteral : ( DecimalLiteral | OctalIntegerLiteral | HexIntegerLiteral );
    public final ES3Parser.numericLiteral_return numericLiteral() throws RecognitionException {
        ES3Parser.numericLiteral_return retval = new ES3Parser.numericLiteral_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set19=null;

        Object set19_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "numericLiteral");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(809, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:810:2: ( DecimalLiteral | OctalIntegerLiteral | HexIntegerLiteral )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(810,2);
            set19=(Token)input.LT(1);
            if ( (input.LA(1)>=DecimalLiteral && input.LA(1)<=HexIntegerLiteral) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set19));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(813, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "numericLiteral");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "numericLiteral"

    public static class primaryExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "primaryExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:897:1: primaryExpression : ( THIS | Identifier | literal | arrayLiteral | objectLiteral | lpar= LPAREN expression RPAREN -> ^( PAREXPR[$lpar, \"PAREXPR\"] expression ) );
    public final ES3Parser.primaryExpression_return primaryExpression() throws RecognitionException {
        ES3Parser.primaryExpression_return retval = new ES3Parser.primaryExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lpar=null;
        Token THIS20=null;
        Token Identifier21=null;
        Token RPAREN26=null;
        ES3Parser.literal_return literal22 = null;

        ES3Parser.arrayLiteral_return arrayLiteral23 = null;

        ES3Parser.objectLiteral_return objectLiteral24 = null;

        ES3Parser.expression_return expression25 = null;


        Object lpar_tree=null;
        Object THIS20_tree=null;
        Object Identifier21_tree=null;
        Object RPAREN26_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try { dbg.enterRule(getGrammarFileName(), "primaryExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(897, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:898:2: ( THIS | Identifier | literal | arrayLiteral | objectLiteral | lpar= LPAREN expression RPAREN -> ^( PAREXPR[$lpar, \"PAREXPR\"] expression ) )
            int alt4=6;
            try { dbg.enterDecision(4, decisionCanBacktrack[4]);

            switch ( input.LA(1) ) {
            case THIS:
                {
                alt4=1;
                }
                break;
            case Identifier:
                {
                alt4=2;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case StringLiteral:
            case RegularExpressionLiteral:
            case DecimalLiteral:
            case OctalIntegerLiteral:
            case HexIntegerLiteral:
                {
                alt4=3;
                }
                break;
            case LBRACK:
                {
                alt4=4;
                }
                break;
            case LBRACE:
                {
                alt4=5;
                }
                break;
            case LPAREN:
                {
                alt4=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(4);}

            switch (alt4) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:898:4: THIS
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(898,4);
                    THIS20=(Token)match(input,THIS,FOLLOW_THIS_in_primaryExpression3113); 
                    THIS20_tree = (Object)adaptor.create(THIS20);
                    adaptor.addChild(root_0, THIS20_tree);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:899:4: Identifier
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(899,4);
                    Identifier21=(Token)match(input,Identifier,FOLLOW_Identifier_in_primaryExpression3118); 
                    Identifier21_tree = (Object)adaptor.create(Identifier21);
                    adaptor.addChild(root_0, Identifier21_tree);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:900:4: literal
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(900,4);
                    pushFollow(FOLLOW_literal_in_primaryExpression3123);
                    literal22=literal();

                    state._fsp--;

                    adaptor.addChild(root_0, literal22.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:901:4: arrayLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(901,4);
                    pushFollow(FOLLOW_arrayLiteral_in_primaryExpression3128);
                    arrayLiteral23=arrayLiteral();

                    state._fsp--;

                    adaptor.addChild(root_0, arrayLiteral23.getTree());

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:902:4: objectLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(902,4);
                    pushFollow(FOLLOW_objectLiteral_in_primaryExpression3133);
                    objectLiteral24=objectLiteral();

                    state._fsp--;

                    adaptor.addChild(root_0, objectLiteral24.getTree());

                    }
                    break;
                case 6 :
                    dbg.enterAlt(6);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:903:4: lpar= LPAREN expression RPAREN
                    {
                    dbg.location(903,8);
                    lpar=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression3140);  
                    stream_LPAREN.add(lpar);

                    dbg.location(903,16);
                    pushFollow(FOLLOW_expression_in_primaryExpression3142);
                    expression25=expression();

                    state._fsp--;

                    stream_expression.add(expression25.getTree());
                    dbg.location(903,27);
                    RPAREN26=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression3144);  
                    stream_RPAREN.add(RPAREN26);



                    // AST REWRITE
                    // elements: expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 903:34: -> ^( PAREXPR[$lpar, \"PAREXPR\"] expression )
                    {
                        dbg.location(903,37);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:903:37: ^( PAREXPR[$lpar, \"PAREXPR\"] expression )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(903,40);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(PAREXPR, lpar, "PAREXPR"), root_1);

                        dbg.location(903,66);
                        adaptor.addChild(root_1, stream_expression.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(904, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "primaryExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "primaryExpression"

    public static class arrayLiteral_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arrayLiteral"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:906:1: arrayLiteral : lb= LBRACK ( arrayItem ( COMMA arrayItem )* )? RBRACK -> ^( ARRAY[$lb, \"ARRAY\"] ( arrayItem )* ) ;
    public final ES3Parser.arrayLiteral_return arrayLiteral() throws RecognitionException {
        ES3Parser.arrayLiteral_return retval = new ES3Parser.arrayLiteral_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lb=null;
        Token COMMA28=null;
        Token RBRACK30=null;
        ES3Parser.arrayItem_return arrayItem27 = null;

        ES3Parser.arrayItem_return arrayItem29 = null;


        Object lb_tree=null;
        Object COMMA28_tree=null;
        Object RBRACK30_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_arrayItem=new RewriteRuleSubtreeStream(adaptor,"rule arrayItem");
        try { dbg.enterRule(getGrammarFileName(), "arrayLiteral");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(906, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:907:2: (lb= LBRACK ( arrayItem ( COMMA arrayItem )* )? RBRACK -> ^( ARRAY[$lb, \"ARRAY\"] ( arrayItem )* ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:907:4: lb= LBRACK ( arrayItem ( COMMA arrayItem )* )? RBRACK
            {
            dbg.location(907,6);
            lb=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_arrayLiteral3168);  
            stream_LBRACK.add(lb);

            dbg.location(907,14);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:907:14: ( arrayItem ( COMMA arrayItem )* )?
            int alt6=2;
            try { dbg.enterSubRule(6);
            try { dbg.enterDecision(6, decisionCanBacktrack[6]);

            int LA6_0 = input.LA(1);

            if ( ((LA6_0>=NULL && LA6_0<=FALSE)||LA6_0==DELETE||LA6_0==FUNCTION||LA6_0==NEW||LA6_0==THIS||LA6_0==TYPEOF||LA6_0==VOID||LA6_0==LBRACE||LA6_0==LPAREN||LA6_0==LBRACK||LA6_0==COMMA||(LA6_0>=ADD && LA6_0<=SUB)||(LA6_0>=INC && LA6_0<=DEC)||(LA6_0>=NOT && LA6_0<=INV)||(LA6_0>=Identifier && LA6_0<=StringLiteral)||LA6_0==RegularExpressionLiteral||(LA6_0>=DecimalLiteral && LA6_0<=HexIntegerLiteral)) ) {
                alt6=1;
            }
            else if ( (LA6_0==RBRACK) ) {
                int LA6_2 = input.LA(2);

                if ( (evalPredicate( input.LA(1) == COMMA ," input.LA(1) == COMMA ")) ) {
                    alt6=1;
                }
            }
            } finally {dbg.exitDecision(6);}

            switch (alt6) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:907:16: arrayItem ( COMMA arrayItem )*
                    {
                    dbg.location(907,16);
                    pushFollow(FOLLOW_arrayItem_in_arrayLiteral3172);
                    arrayItem27=arrayItem();

                    state._fsp--;

                    stream_arrayItem.add(arrayItem27.getTree());
                    dbg.location(907,26);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:907:26: ( COMMA arrayItem )*
                    try { dbg.enterSubRule(5);

                    loop5:
                    do {
                        int alt5=2;
                        try { dbg.enterDecision(5, decisionCanBacktrack[5]);

                        int LA5_0 = input.LA(1);

                        if ( (LA5_0==COMMA) ) {
                            alt5=1;
                        }


                        } finally {dbg.exitDecision(5);}

                        switch (alt5) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:907:28: COMMA arrayItem
                    	    {
                    	    dbg.location(907,28);
                    	    COMMA28=(Token)match(input,COMMA,FOLLOW_COMMA_in_arrayLiteral3176);  
                    	    stream_COMMA.add(COMMA28);

                    	    dbg.location(907,34);
                    	    pushFollow(FOLLOW_arrayItem_in_arrayLiteral3178);
                    	    arrayItem29=arrayItem();

                    	    state._fsp--;

                    	    stream_arrayItem.add(arrayItem29.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(5);}


                    }
                    break;

            }
            } finally {dbg.exitSubRule(6);}

            dbg.location(907,50);
            RBRACK30=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_arrayLiteral3186);  
            stream_RBRACK.add(RBRACK30);



            // AST REWRITE
            // elements: arrayItem
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 908:2: -> ^( ARRAY[$lb, \"ARRAY\"] ( arrayItem )* )
            {
                dbg.location(908,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:908:5: ^( ARRAY[$lb, \"ARRAY\"] ( arrayItem )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(908,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ARRAY, lb, "ARRAY"), root_1);

                dbg.location(908,28);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:908:28: ( arrayItem )*
                while ( stream_arrayItem.hasNext() ) {
                    dbg.location(908,28);
                    adaptor.addChild(root_1, stream_arrayItem.nextTree());

                }
                stream_arrayItem.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(909, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "arrayLiteral");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "arrayLiteral"

    public static class arrayItem_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arrayItem"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:911:1: arrayItem : (expr= assignmentExpression | {...}?) -> ^( ITEM ( $expr)? ) ;
    public final ES3Parser.arrayItem_return arrayItem() throws RecognitionException {
        ES3Parser.arrayItem_return retval = new ES3Parser.arrayItem_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.assignmentExpression_return expr = null;


        RewriteRuleSubtreeStream stream_assignmentExpression=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpression");
        try { dbg.enterRule(getGrammarFileName(), "arrayItem");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(911, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:912:2: ( (expr= assignmentExpression | {...}?) -> ^( ITEM ( $expr)? ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:912:4: (expr= assignmentExpression | {...}?)
            {
            dbg.location(912,4);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:912:4: (expr= assignmentExpression | {...}?)
            int alt7=2;
            try { dbg.enterSubRule(7);
            try { dbg.enterDecision(7, decisionCanBacktrack[7]);

            int LA7_0 = input.LA(1);

            if ( ((LA7_0>=NULL && LA7_0<=FALSE)||LA7_0==DELETE||LA7_0==FUNCTION||LA7_0==NEW||LA7_0==THIS||LA7_0==TYPEOF||LA7_0==VOID||LA7_0==LBRACE||LA7_0==LPAREN||LA7_0==LBRACK||(LA7_0>=ADD && LA7_0<=SUB)||(LA7_0>=INC && LA7_0<=DEC)||(LA7_0>=NOT && LA7_0<=INV)||(LA7_0>=Identifier && LA7_0<=StringLiteral)||LA7_0==RegularExpressionLiteral||(LA7_0>=DecimalLiteral && LA7_0<=HexIntegerLiteral)) ) {
                alt7=1;
            }
            else if ( (LA7_0==RBRACK||LA7_0==COMMA) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(7);}

            switch (alt7) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:912:6: expr= assignmentExpression
                    {
                    dbg.location(912,10);
                    pushFollow(FOLLOW_assignmentExpression_in_arrayItem3214);
                    expr=assignmentExpression();

                    state._fsp--;

                    stream_assignmentExpression.add(expr.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:912:34: {...}?
                    {
                    dbg.location(912,34);
                    if ( !(evalPredicate( input.LA(1) == COMMA ," input.LA(1) == COMMA ")) ) {
                        throw new FailedPredicateException(input, "arrayItem", " input.LA(1) == COMMA ");
                    }

                    }
                    break;

            }
            } finally {dbg.exitSubRule(7);}



            // AST REWRITE
            // elements: expr
            // token labels: 
            // rule labels: retval, expr
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr",expr!=null?expr.tree:null);

            root_0 = (Object)adaptor.nil();
            // 913:2: -> ^( ITEM ( $expr)? )
            {
                dbg.location(913,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:913:5: ^( ITEM ( $expr)? )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(913,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ITEM, "ITEM"), root_1);

                dbg.location(913,13);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:913:13: ( $expr)?
                if ( stream_expr.hasNext() ) {
                    dbg.location(913,13);
                    adaptor.addChild(root_1, stream_expr.nextTree());

                }
                stream_expr.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(914, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "arrayItem");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "arrayItem"

    public static class objectLiteral_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "objectLiteral"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:916:1: objectLiteral : lb= LBRACE ( nameValuePair ( COMMA nameValuePair )* )? RBRACE -> ^( OBJECT[$lb, \"OBJECT\"] ( nameValuePair )* ) ;
    public final ES3Parser.objectLiteral_return objectLiteral() throws RecognitionException {
        ES3Parser.objectLiteral_return retval = new ES3Parser.objectLiteral_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lb=null;
        Token COMMA32=null;
        Token RBRACE34=null;
        ES3Parser.nameValuePair_return nameValuePair31 = null;

        ES3Parser.nameValuePair_return nameValuePair33 = null;


        Object lb_tree=null;
        Object COMMA32_tree=null;
        Object RBRACE34_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_nameValuePair=new RewriteRuleSubtreeStream(adaptor,"rule nameValuePair");
        try { dbg.enterRule(getGrammarFileName(), "objectLiteral");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(916, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:917:2: (lb= LBRACE ( nameValuePair ( COMMA nameValuePair )* )? RBRACE -> ^( OBJECT[$lb, \"OBJECT\"] ( nameValuePair )* ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:917:4: lb= LBRACE ( nameValuePair ( COMMA nameValuePair )* )? RBRACE
            {
            dbg.location(917,6);
            lb=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_objectLiteral3246);  
            stream_LBRACE.add(lb);

            dbg.location(917,14);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:917:14: ( nameValuePair ( COMMA nameValuePair )* )?
            int alt9=2;
            try { dbg.enterSubRule(9);
            try { dbg.enterDecision(9, decisionCanBacktrack[9]);

            int LA9_0 = input.LA(1);

            if ( ((LA9_0>=Identifier && LA9_0<=StringLiteral)||(LA9_0>=DecimalLiteral && LA9_0<=HexIntegerLiteral)) ) {
                alt9=1;
            }
            } finally {dbg.exitDecision(9);}

            switch (alt9) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:917:16: nameValuePair ( COMMA nameValuePair )*
                    {
                    dbg.location(917,16);
                    pushFollow(FOLLOW_nameValuePair_in_objectLiteral3250);
                    nameValuePair31=nameValuePair();

                    state._fsp--;

                    stream_nameValuePair.add(nameValuePair31.getTree());
                    dbg.location(917,30);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:917:30: ( COMMA nameValuePair )*
                    try { dbg.enterSubRule(8);

                    loop8:
                    do {
                        int alt8=2;
                        try { dbg.enterDecision(8, decisionCanBacktrack[8]);

                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==COMMA) ) {
                            alt8=1;
                        }


                        } finally {dbg.exitDecision(8);}

                        switch (alt8) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:917:32: COMMA nameValuePair
                    	    {
                    	    dbg.location(917,32);
                    	    COMMA32=(Token)match(input,COMMA,FOLLOW_COMMA_in_objectLiteral3254);  
                    	    stream_COMMA.add(COMMA32);

                    	    dbg.location(917,38);
                    	    pushFollow(FOLLOW_nameValuePair_in_objectLiteral3256);
                    	    nameValuePair33=nameValuePair();

                    	    state._fsp--;

                    	    stream_nameValuePair.add(nameValuePair33.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(8);}


                    }
                    break;

            }
            } finally {dbg.exitSubRule(9);}

            dbg.location(917,58);
            RBRACE34=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_objectLiteral3264);  
            stream_RBRACE.add(RBRACE34);



            // AST REWRITE
            // elements: nameValuePair
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 918:2: -> ^( OBJECT[$lb, \"OBJECT\"] ( nameValuePair )* )
            {
                dbg.location(918,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:918:5: ^( OBJECT[$lb, \"OBJECT\"] ( nameValuePair )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(918,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(OBJECT, lb, "OBJECT"), root_1);

                dbg.location(918,30);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:918:30: ( nameValuePair )*
                while ( stream_nameValuePair.hasNext() ) {
                    dbg.location(918,30);
                    adaptor.addChild(root_1, stream_nameValuePair.nextTree());

                }
                stream_nameValuePair.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(919, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "objectLiteral");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "objectLiteral"

    public static class nameValuePair_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "nameValuePair"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:921:1: nameValuePair : propertyName COLON assignmentExpression -> ^( NAMEDVALUE propertyName assignmentExpression ) ;
    public final ES3Parser.nameValuePair_return nameValuePair() throws RecognitionException {
        ES3Parser.nameValuePair_return retval = new ES3Parser.nameValuePair_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COLON36=null;
        ES3Parser.propertyName_return propertyName35 = null;

        ES3Parser.assignmentExpression_return assignmentExpression37 = null;


        Object COLON36_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleSubtreeStream stream_propertyName=new RewriteRuleSubtreeStream(adaptor,"rule propertyName");
        RewriteRuleSubtreeStream stream_assignmentExpression=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpression");
        try { dbg.enterRule(getGrammarFileName(), "nameValuePair");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(921, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:922:2: ( propertyName COLON assignmentExpression -> ^( NAMEDVALUE propertyName assignmentExpression ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:922:4: propertyName COLON assignmentExpression
            {
            dbg.location(922,4);
            pushFollow(FOLLOW_propertyName_in_nameValuePair3289);
            propertyName35=propertyName();

            state._fsp--;

            stream_propertyName.add(propertyName35.getTree());
            dbg.location(922,17);
            COLON36=(Token)match(input,COLON,FOLLOW_COLON_in_nameValuePair3291);  
            stream_COLON.add(COLON36);

            dbg.location(922,23);
            pushFollow(FOLLOW_assignmentExpression_in_nameValuePair3293);
            assignmentExpression37=assignmentExpression();

            state._fsp--;

            stream_assignmentExpression.add(assignmentExpression37.getTree());


            // AST REWRITE
            // elements: assignmentExpression, propertyName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 923:2: -> ^( NAMEDVALUE propertyName assignmentExpression )
            {
                dbg.location(923,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:923:5: ^( NAMEDVALUE propertyName assignmentExpression )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(923,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(NAMEDVALUE, "NAMEDVALUE"), root_1);

                dbg.location(923,19);
                adaptor.addChild(root_1, stream_propertyName.nextTree());
                dbg.location(923,32);
                adaptor.addChild(root_1, stream_assignmentExpression.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(924, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "nameValuePair");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "nameValuePair"

    public static class propertyName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "propertyName"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:926:1: propertyName : ( Identifier | StringLiteral | numericLiteral );
    public final ES3Parser.propertyName_return propertyName() throws RecognitionException {
        ES3Parser.propertyName_return retval = new ES3Parser.propertyName_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Identifier38=null;
        Token StringLiteral39=null;
        ES3Parser.numericLiteral_return numericLiteral40 = null;


        Object Identifier38_tree=null;
        Object StringLiteral39_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "propertyName");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(926, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:927:2: ( Identifier | StringLiteral | numericLiteral )
            int alt10=3;
            try { dbg.enterDecision(10, decisionCanBacktrack[10]);

            switch ( input.LA(1) ) {
            case Identifier:
                {
                alt10=1;
                }
                break;
            case StringLiteral:
                {
                alt10=2;
                }
                break;
            case DecimalLiteral:
            case OctalIntegerLiteral:
            case HexIntegerLiteral:
                {
                alt10=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(10);}

            switch (alt10) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:927:4: Identifier
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(927,4);
                    Identifier38=(Token)match(input,Identifier,FOLLOW_Identifier_in_propertyName3317); 
                    Identifier38_tree = (Object)adaptor.create(Identifier38);
                    adaptor.addChild(root_0, Identifier38_tree);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:928:4: StringLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(928,4);
                    StringLiteral39=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_propertyName3322); 
                    StringLiteral39_tree = (Object)adaptor.create(StringLiteral39);
                    adaptor.addChild(root_0, StringLiteral39_tree);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:929:4: numericLiteral
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(929,4);
                    pushFollow(FOLLOW_numericLiteral_in_propertyName3327);
                    numericLiteral40=numericLiteral();

                    state._fsp--;

                    adaptor.addChild(root_0, numericLiteral40.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(930, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "propertyName");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "propertyName"

    public static class memberExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "memberExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:941:1: memberExpression : ( primaryExpression | functionExpression | newExpression );
    public final ES3Parser.memberExpression_return memberExpression() throws RecognitionException {
        ES3Parser.memberExpression_return retval = new ES3Parser.memberExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.primaryExpression_return primaryExpression41 = null;

        ES3Parser.functionExpression_return functionExpression42 = null;

        ES3Parser.newExpression_return newExpression43 = null;



        try { dbg.enterRule(getGrammarFileName(), "memberExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(941, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:942:2: ( primaryExpression | functionExpression | newExpression )
            int alt11=3;
            try { dbg.enterDecision(11, decisionCanBacktrack[11]);

            switch ( input.LA(1) ) {
            case NULL:
            case TRUE:
            case FALSE:
            case THIS:
            case LBRACE:
            case LPAREN:
            case LBRACK:
            case Identifier:
            case StringLiteral:
            case RegularExpressionLiteral:
            case DecimalLiteral:
            case OctalIntegerLiteral:
            case HexIntegerLiteral:
                {
                alt11=1;
                }
                break;
            case FUNCTION:
                {
                alt11=2;
                }
                break;
            case NEW:
                {
                alt11=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(11);}

            switch (alt11) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:942:4: primaryExpression
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(942,4);
                    pushFollow(FOLLOW_primaryExpression_in_memberExpression3345);
                    primaryExpression41=primaryExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, primaryExpression41.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:943:4: functionExpression
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(943,4);
                    pushFollow(FOLLOW_functionExpression_in_memberExpression3350);
                    functionExpression42=functionExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, functionExpression42.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:944:4: newExpression
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(944,4);
                    pushFollow(FOLLOW_newExpression_in_memberExpression3355);
                    newExpression43=newExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, newExpression43.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(945, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "memberExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "memberExpression"

    public static class newExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "newExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:947:1: newExpression : NEW memberExpression ;
    public final ES3Parser.newExpression_return newExpression() throws RecognitionException {
        ES3Parser.newExpression_return retval = new ES3Parser.newExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NEW44=null;
        ES3Parser.memberExpression_return memberExpression45 = null;


        Object NEW44_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "newExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(947, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:948:2: ( NEW memberExpression )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:948:4: NEW memberExpression
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(948,7);
            NEW44=(Token)match(input,NEW,FOLLOW_NEW_in_newExpression3366); 
            NEW44_tree = (Object)adaptor.create(NEW44);
            root_0 = (Object)adaptor.becomeRoot(NEW44_tree, root_0);

            dbg.location(948,9);
            pushFollow(FOLLOW_memberExpression_in_newExpression3369);
            memberExpression45=memberExpression();

            state._fsp--;

            adaptor.addChild(root_0, memberExpression45.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(949, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "newExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "newExpression"

    public static class arguments_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "arguments"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:952:1: arguments : LPAREN ( assignmentExpression ( COMMA assignmentExpression )* )? RPAREN -> ^( ARGS ( assignmentExpression )* ) ;
    public final ES3Parser.arguments_return arguments() throws RecognitionException {
        ES3Parser.arguments_return retval = new ES3Parser.arguments_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LPAREN46=null;
        Token COMMA48=null;
        Token RPAREN50=null;
        ES3Parser.assignmentExpression_return assignmentExpression47 = null;

        ES3Parser.assignmentExpression_return assignmentExpression49 = null;


        Object LPAREN46_tree=null;
        Object COMMA48_tree=null;
        Object RPAREN50_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_assignmentExpression=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpression");
        try { dbg.enterRule(getGrammarFileName(), "arguments");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(952, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:953:2: ( LPAREN ( assignmentExpression ( COMMA assignmentExpression )* )? RPAREN -> ^( ARGS ( assignmentExpression )* ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:953:4: LPAREN ( assignmentExpression ( COMMA assignmentExpression )* )? RPAREN
            {
            dbg.location(953,4);
            LPAREN46=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arguments3382);  
            stream_LPAREN.add(LPAREN46);

            dbg.location(953,11);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:953:11: ( assignmentExpression ( COMMA assignmentExpression )* )?
            int alt13=2;
            try { dbg.enterSubRule(13);
            try { dbg.enterDecision(13, decisionCanBacktrack[13]);

            int LA13_0 = input.LA(1);

            if ( ((LA13_0>=NULL && LA13_0<=FALSE)||LA13_0==DELETE||LA13_0==FUNCTION||LA13_0==NEW||LA13_0==THIS||LA13_0==TYPEOF||LA13_0==VOID||LA13_0==LBRACE||LA13_0==LPAREN||LA13_0==LBRACK||(LA13_0>=ADD && LA13_0<=SUB)||(LA13_0>=INC && LA13_0<=DEC)||(LA13_0>=NOT && LA13_0<=INV)||(LA13_0>=Identifier && LA13_0<=StringLiteral)||LA13_0==RegularExpressionLiteral||(LA13_0>=DecimalLiteral && LA13_0<=HexIntegerLiteral)) ) {
                alt13=1;
            }
            } finally {dbg.exitDecision(13);}

            switch (alt13) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:953:13: assignmentExpression ( COMMA assignmentExpression )*
                    {
                    dbg.location(953,13);
                    pushFollow(FOLLOW_assignmentExpression_in_arguments3386);
                    assignmentExpression47=assignmentExpression();

                    state._fsp--;

                    stream_assignmentExpression.add(assignmentExpression47.getTree());
                    dbg.location(953,34);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:953:34: ( COMMA assignmentExpression )*
                    try { dbg.enterSubRule(12);

                    loop12:
                    do {
                        int alt12=2;
                        try { dbg.enterDecision(12, decisionCanBacktrack[12]);

                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==COMMA) ) {
                            alt12=1;
                        }


                        } finally {dbg.exitDecision(12);}

                        switch (alt12) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:953:36: COMMA assignmentExpression
                    	    {
                    	    dbg.location(953,36);
                    	    COMMA48=(Token)match(input,COMMA,FOLLOW_COMMA_in_arguments3390);  
                    	    stream_COMMA.add(COMMA48);

                    	    dbg.location(953,42);
                    	    pushFollow(FOLLOW_assignmentExpression_in_arguments3392);
                    	    assignmentExpression49=assignmentExpression();

                    	    state._fsp--;

                    	    stream_assignmentExpression.add(assignmentExpression49.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(12);}


                    }
                    break;

            }
            } finally {dbg.exitSubRule(13);}

            dbg.location(953,69);
            RPAREN50=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arguments3400);  
            stream_RPAREN.add(RPAREN50);



            // AST REWRITE
            // elements: assignmentExpression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 954:2: -> ^( ARGS ( assignmentExpression )* )
            {
                dbg.location(954,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:954:5: ^( ARGS ( assignmentExpression )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(954,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ARGS, "ARGS"), root_1);

                dbg.location(954,13);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:954:13: ( assignmentExpression )*
                while ( stream_assignmentExpression.hasNext() ) {
                    dbg.location(954,13);
                    adaptor.addChild(root_1, stream_assignmentExpression.nextTree());

                }
                stream_assignmentExpression.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(955, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "arguments");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "arguments"

    public static class leftHandSideExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "leftHandSideExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:957:1: leftHandSideExpression : ( memberExpression -> memberExpression ) ( arguments -> ^( CALL $leftHandSideExpression arguments ) | LBRACK expression RBRACK -> ^( BYINDEX $leftHandSideExpression expression ) | DOT Identifier -> ^( BYFIELD $leftHandSideExpression Identifier ) )* ;
    public final ES3Parser.leftHandSideExpression_return leftHandSideExpression() throws RecognitionException {
        ES3Parser.leftHandSideExpression_return retval = new ES3Parser.leftHandSideExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LBRACK53=null;
        Token RBRACK55=null;
        Token DOT56=null;
        Token Identifier57=null;
        ES3Parser.memberExpression_return memberExpression51 = null;

        ES3Parser.arguments_return arguments52 = null;

        ES3Parser.expression_return expression54 = null;


        Object LBRACK53_tree=null;
        Object RBRACK55_tree=null;
        Object DOT56_tree=null;
        Object Identifier57_tree=null;
        RewriteRuleTokenStream stream_RBRACK=new RewriteRuleTokenStream(adaptor,"token RBRACK");
        RewriteRuleTokenStream stream_LBRACK=new RewriteRuleTokenStream(adaptor,"token LBRACK");
        RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");
        RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");
        RewriteRuleSubtreeStream stream_memberExpression=new RewriteRuleSubtreeStream(adaptor,"rule memberExpression");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_arguments=new RewriteRuleSubtreeStream(adaptor,"rule arguments");
        try { dbg.enterRule(getGrammarFileName(), "leftHandSideExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(957, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:958:2: ( ( memberExpression -> memberExpression ) ( arguments -> ^( CALL $leftHandSideExpression arguments ) | LBRACK expression RBRACK -> ^( BYINDEX $leftHandSideExpression expression ) | DOT Identifier -> ^( BYFIELD $leftHandSideExpression Identifier ) )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:959:2: ( memberExpression -> memberExpression ) ( arguments -> ^( CALL $leftHandSideExpression arguments ) | LBRACK expression RBRACK -> ^( BYINDEX $leftHandSideExpression expression ) | DOT Identifier -> ^( BYFIELD $leftHandSideExpression Identifier ) )*
            {
            dbg.location(959,2);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:959:2: ( memberExpression -> memberExpression )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:960:3: memberExpression
            {
            dbg.location(960,3);
            pushFollow(FOLLOW_memberExpression_in_leftHandSideExpression3429);
            memberExpression51=memberExpression();

            state._fsp--;

            stream_memberExpression.add(memberExpression51.getTree());


            // AST REWRITE
            // elements: memberExpression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 960:22: -> memberExpression
            {
                dbg.location(960,25);
                adaptor.addChild(root_0, stream_memberExpression.nextTree());

            }

            retval.tree = root_0;
            }

            dbg.location(962,2);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:962:2: ( arguments -> ^( CALL $leftHandSideExpression arguments ) | LBRACK expression RBRACK -> ^( BYINDEX $leftHandSideExpression expression ) | DOT Identifier -> ^( BYFIELD $leftHandSideExpression Identifier ) )*
            try { dbg.enterSubRule(14);

            loop14:
            do {
                int alt14=4;
                try { dbg.enterDecision(14, decisionCanBacktrack[14]);

                switch ( input.LA(1) ) {
                case LPAREN:
                    {
                    alt14=1;
                    }
                    break;
                case LBRACK:
                    {
                    alt14=2;
                    }
                    break;
                case DOT:
                    {
                    alt14=3;
                    }
                    break;

                }

                } finally {dbg.exitDecision(14);}

                switch (alt14) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:963:3: arguments
            	    {
            	    dbg.location(963,3);
            	    pushFollow(FOLLOW_arguments_in_leftHandSideExpression3445);
            	    arguments52=arguments();

            	    state._fsp--;

            	    stream_arguments.add(arguments52.getTree());


            	    // AST REWRITE
            	    // elements: arguments, leftHandSideExpression
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 963:15: -> ^( CALL $leftHandSideExpression arguments )
            	    {
            	        dbg.location(963,18);
            	        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:963:18: ^( CALL $leftHandSideExpression arguments )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        dbg.location(963,21);
            	        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CALL, "CALL"), root_1);

            	        dbg.location(963,26);
            	        adaptor.addChild(root_1, stream_retval.nextTree());
            	        dbg.location(963,50);
            	        adaptor.addChild(root_1, stream_arguments.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }

            	    retval.tree = root_0;
            	    }
            	    break;
            	case 2 :
            	    dbg.enterAlt(2);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:964:5: LBRACK expression RBRACK
            	    {
            	    dbg.location(964,5);
            	    LBRACK53=(Token)match(input,LBRACK,FOLLOW_LBRACK_in_leftHandSideExpression3466);  
            	    stream_LBRACK.add(LBRACK53);

            	    dbg.location(964,12);
            	    pushFollow(FOLLOW_expression_in_leftHandSideExpression3468);
            	    expression54=expression();

            	    state._fsp--;

            	    stream_expression.add(expression54.getTree());
            	    dbg.location(964,23);
            	    RBRACK55=(Token)match(input,RBRACK,FOLLOW_RBRACK_in_leftHandSideExpression3470);  
            	    stream_RBRACK.add(RBRACK55);



            	    // AST REWRITE
            	    // elements: expression, leftHandSideExpression
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 964:30: -> ^( BYINDEX $leftHandSideExpression expression )
            	    {
            	        dbg.location(964,33);
            	        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:964:33: ^( BYINDEX $leftHandSideExpression expression )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        dbg.location(964,36);
            	        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BYINDEX, "BYINDEX"), root_1);

            	        dbg.location(964,44);
            	        adaptor.addChild(root_1, stream_retval.nextTree());
            	        dbg.location(964,68);
            	        adaptor.addChild(root_1, stream_expression.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }

            	    retval.tree = root_0;
            	    }
            	    break;
            	case 3 :
            	    dbg.enterAlt(3);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:965:5: DOT Identifier
            	    {
            	    dbg.location(965,5);
            	    DOT56=(Token)match(input,DOT,FOLLOW_DOT_in_leftHandSideExpression3489);  
            	    stream_DOT.add(DOT56);

            	    dbg.location(965,9);
            	    Identifier57=(Token)match(input,Identifier,FOLLOW_Identifier_in_leftHandSideExpression3491);  
            	    stream_Identifier.add(Identifier57);



            	    // AST REWRITE
            	    // elements: leftHandSideExpression, Identifier
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (Object)adaptor.nil();
            	    // 965:21: -> ^( BYFIELD $leftHandSideExpression Identifier )
            	    {
            	        dbg.location(965,24);
            	        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:965:24: ^( BYFIELD $leftHandSideExpression Identifier )
            	        {
            	        Object root_1 = (Object)adaptor.nil();
            	        dbg.location(965,27);
            	        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BYFIELD, "BYFIELD"), root_1);

            	        dbg.location(965,35);
            	        adaptor.addChild(root_1, stream_retval.nextTree());
            	        dbg.location(965,59);
            	        adaptor.addChild(root_1, stream_Identifier.nextNode());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }

            	    retval.tree = root_0;
            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);
            } finally {dbg.exitSubRule(14);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(967, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "leftHandSideExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "leftHandSideExpression"

    public static class postfixExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "postfixExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:979:1: postfixExpression : leftHandSideExpression ( postfixOperator )? ;
    public final ES3Parser.postfixExpression_return postfixExpression() throws RecognitionException {
        ES3Parser.postfixExpression_return retval = new ES3Parser.postfixExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.leftHandSideExpression_return leftHandSideExpression58 = null;

        ES3Parser.postfixOperator_return postfixOperator59 = null;



        try { dbg.enterRule(getGrammarFileName(), "postfixExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(979, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:980:2: ( leftHandSideExpression ( postfixOperator )? )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:980:4: leftHandSideExpression ( postfixOperator )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(980,4);
            pushFollow(FOLLOW_leftHandSideExpression_in_postfixExpression3526);
            leftHandSideExpression58=leftHandSideExpression();

            state._fsp--;

            adaptor.addChild(root_0, leftHandSideExpression58.getTree());
            dbg.location(980,27);
             if (input.LA(1) == INC || input.LA(1) == DEC) promoteEOL(null); 
            dbg.location(980,95);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:980:95: ( postfixOperator )?
            int alt15=2;
            try { dbg.enterSubRule(15);
            try { dbg.enterDecision(15, decisionCanBacktrack[15]);

            int LA15_0 = input.LA(1);

            if ( ((LA15_0>=INC && LA15_0<=DEC)) ) {
                alt15=1;
            }
            } finally {dbg.exitDecision(15);}

            switch (alt15) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:980:97: postfixOperator
                    {
                    dbg.location(980,112);
                    pushFollow(FOLLOW_postfixOperator_in_postfixExpression3532);
                    postfixOperator59=postfixOperator();

                    state._fsp--;

                    root_0 = (Object)adaptor.becomeRoot(postfixOperator59.getTree(), root_0);

                    }
                    break;

            }
            } finally {dbg.exitSubRule(15);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(981, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "postfixExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "postfixExpression"

    public static class postfixOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "postfixOperator"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:983:1: postfixOperator : (op= INC | op= DEC );
    public final ES3Parser.postfixOperator_return postfixOperator() throws RecognitionException {
        ES3Parser.postfixOperator_return retval = new ES3Parser.postfixOperator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;

        Object op_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "postfixOperator");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(983, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:984:2: (op= INC | op= DEC )
            int alt16=2;
            try { dbg.enterDecision(16, decisionCanBacktrack[16]);

            int LA16_0 = input.LA(1);

            if ( (LA16_0==INC) ) {
                alt16=1;
            }
            else if ( (LA16_0==DEC) ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(16);}

            switch (alt16) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:984:4: op= INC
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(984,6);
                    op=(Token)match(input,INC,FOLLOW_INC_in_postfixOperator3550); 
                    op_tree = (Object)adaptor.create(op);
                    adaptor.addChild(root_0, op_tree);

                    dbg.location(984,11);
                     op.setType(PINC); 

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:985:4: op= DEC
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(985,6);
                    op=(Token)match(input,DEC,FOLLOW_DEC_in_postfixOperator3559); 
                    op_tree = (Object)adaptor.create(op);
                    adaptor.addChild(root_0, op_tree);

                    dbg.location(985,11);
                     op.setType(PDEC); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(986, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "postfixOperator");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "postfixOperator"

    public static class unaryExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unaryExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:992:1: unaryExpression : ( postfixExpression | unaryOperator unaryExpression );
    public final ES3Parser.unaryExpression_return unaryExpression() throws RecognitionException {
        ES3Parser.unaryExpression_return retval = new ES3Parser.unaryExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.postfixExpression_return postfixExpression60 = null;

        ES3Parser.unaryOperator_return unaryOperator61 = null;

        ES3Parser.unaryExpression_return unaryExpression62 = null;



        try { dbg.enterRule(getGrammarFileName(), "unaryExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(992, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:993:2: ( postfixExpression | unaryOperator unaryExpression )
            int alt17=2;
            try { dbg.enterDecision(17, decisionCanBacktrack[17]);

            int LA17_0 = input.LA(1);

            if ( ((LA17_0>=NULL && LA17_0<=FALSE)||LA17_0==FUNCTION||LA17_0==NEW||LA17_0==THIS||LA17_0==LBRACE||LA17_0==LPAREN||LA17_0==LBRACK||(LA17_0>=Identifier && LA17_0<=StringLiteral)||LA17_0==RegularExpressionLiteral||(LA17_0>=DecimalLiteral && LA17_0<=HexIntegerLiteral)) ) {
                alt17=1;
            }
            else if ( (LA17_0==DELETE||LA17_0==TYPEOF||LA17_0==VOID||(LA17_0>=ADD && LA17_0<=SUB)||(LA17_0>=INC && LA17_0<=DEC)||(LA17_0>=NOT && LA17_0<=INV)) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(17);}

            switch (alt17) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:993:4: postfixExpression
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(993,4);
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression3576);
                    postfixExpression60=postfixExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, postfixExpression60.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:994:4: unaryOperator unaryExpression
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(994,17);
                    pushFollow(FOLLOW_unaryOperator_in_unaryExpression3581);
                    unaryOperator61=unaryOperator();

                    state._fsp--;

                    root_0 = (Object)adaptor.becomeRoot(unaryOperator61.getTree(), root_0);
                    dbg.location(994,19);
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression3584);
                    unaryExpression62=unaryExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, unaryExpression62.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(995, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "unaryExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "unaryExpression"

    public static class unaryOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unaryOperator"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:997:1: unaryOperator : ( DELETE | VOID | TYPEOF | INC | DEC | op= ADD | op= SUB | INV | NOT );
    public final ES3Parser.unaryOperator_return unaryOperator() throws RecognitionException {
        ES3Parser.unaryOperator_return retval = new ES3Parser.unaryOperator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token op=null;
        Token DELETE63=null;
        Token VOID64=null;
        Token TYPEOF65=null;
        Token INC66=null;
        Token DEC67=null;
        Token INV68=null;
        Token NOT69=null;

        Object op_tree=null;
        Object DELETE63_tree=null;
        Object VOID64_tree=null;
        Object TYPEOF65_tree=null;
        Object INC66_tree=null;
        Object DEC67_tree=null;
        Object INV68_tree=null;
        Object NOT69_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "unaryOperator");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(997, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:998:2: ( DELETE | VOID | TYPEOF | INC | DEC | op= ADD | op= SUB | INV | NOT )
            int alt18=9;
            try { dbg.enterDecision(18, decisionCanBacktrack[18]);

            switch ( input.LA(1) ) {
            case DELETE:
                {
                alt18=1;
                }
                break;
            case VOID:
                {
                alt18=2;
                }
                break;
            case TYPEOF:
                {
                alt18=3;
                }
                break;
            case INC:
                {
                alt18=4;
                }
                break;
            case DEC:
                {
                alt18=5;
                }
                break;
            case ADD:
                {
                alt18=6;
                }
                break;
            case SUB:
                {
                alt18=7;
                }
                break;
            case INV:
                {
                alt18=8;
                }
                break;
            case NOT:
                {
                alt18=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(18);}

            switch (alt18) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:998:4: DELETE
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(998,4);
                    DELETE63=(Token)match(input,DELETE,FOLLOW_DELETE_in_unaryOperator3596); 
                    DELETE63_tree = (Object)adaptor.create(DELETE63);
                    adaptor.addChild(root_0, DELETE63_tree);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:999:4: VOID
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(999,4);
                    VOID64=(Token)match(input,VOID,FOLLOW_VOID_in_unaryOperator3601); 
                    VOID64_tree = (Object)adaptor.create(VOID64);
                    adaptor.addChild(root_0, VOID64_tree);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1000:4: TYPEOF
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1000,4);
                    TYPEOF65=(Token)match(input,TYPEOF,FOLLOW_TYPEOF_in_unaryOperator3606); 
                    TYPEOF65_tree = (Object)adaptor.create(TYPEOF65);
                    adaptor.addChild(root_0, TYPEOF65_tree);


                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1001:4: INC
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1001,4);
                    INC66=(Token)match(input,INC,FOLLOW_INC_in_unaryOperator3611); 
                    INC66_tree = (Object)adaptor.create(INC66);
                    adaptor.addChild(root_0, INC66_tree);


                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1002:4: DEC
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1002,4);
                    DEC67=(Token)match(input,DEC,FOLLOW_DEC_in_unaryOperator3616); 
                    DEC67_tree = (Object)adaptor.create(DEC67);
                    adaptor.addChild(root_0, DEC67_tree);


                    }
                    break;
                case 6 :
                    dbg.enterAlt(6);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1003:4: op= ADD
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1003,6);
                    op=(Token)match(input,ADD,FOLLOW_ADD_in_unaryOperator3623); 
                    op_tree = (Object)adaptor.create(op);
                    adaptor.addChild(root_0, op_tree);

                    dbg.location(1003,11);
                     op.setType(POS); 

                    }
                    break;
                case 7 :
                    dbg.enterAlt(7);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1004:4: op= SUB
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1004,6);
                    op=(Token)match(input,SUB,FOLLOW_SUB_in_unaryOperator3632); 
                    op_tree = (Object)adaptor.create(op);
                    adaptor.addChild(root_0, op_tree);

                    dbg.location(1004,11);
                     op.setType(NEG); 

                    }
                    break;
                case 8 :
                    dbg.enterAlt(8);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1005:4: INV
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1005,4);
                    INV68=(Token)match(input,INV,FOLLOW_INV_in_unaryOperator3639); 
                    INV68_tree = (Object)adaptor.create(INV68);
                    adaptor.addChild(root_0, INV68_tree);


                    }
                    break;
                case 9 :
                    dbg.enterAlt(9);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1006:4: NOT
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1006,4);
                    NOT69=(Token)match(input,NOT,FOLLOW_NOT_in_unaryOperator3644); 
                    NOT69_tree = (Object)adaptor.create(NOT69);
                    adaptor.addChild(root_0, NOT69_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1007, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "unaryOperator");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "unaryOperator"

    public static class multiplicativeExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "multiplicativeExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1013:1: multiplicativeExpression : unaryExpression ( ( MUL | DIV | MOD ) unaryExpression )* ;
    public final ES3Parser.multiplicativeExpression_return multiplicativeExpression() throws RecognitionException {
        ES3Parser.multiplicativeExpression_return retval = new ES3Parser.multiplicativeExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set71=null;
        ES3Parser.unaryExpression_return unaryExpression70 = null;

        ES3Parser.unaryExpression_return unaryExpression72 = null;


        Object set71_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "multiplicativeExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1013, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1014:2: ( unaryExpression ( ( MUL | DIV | MOD ) unaryExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1014:4: unaryExpression ( ( MUL | DIV | MOD ) unaryExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1014,4);
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression3659);
            unaryExpression70=unaryExpression();

            state._fsp--;

            adaptor.addChild(root_0, unaryExpression70.getTree());
            dbg.location(1014,20);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1014:20: ( ( MUL | DIV | MOD ) unaryExpression )*
            try { dbg.enterSubRule(19);

            loop19:
            do {
                int alt19=2;
                try { dbg.enterDecision(19, decisionCanBacktrack[19]);

                int LA19_0 = input.LA(1);

                if ( ((LA19_0>=MUL && LA19_0<=MOD)||LA19_0==DIV) ) {
                    alt19=1;
                }


                } finally {dbg.exitDecision(19);}

                switch (alt19) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1014:22: ( MUL | DIV | MOD ) unaryExpression
            	    {
            	    dbg.location(1014,22);
            	    set71=(Token)input.LT(1);
            	    set71=(Token)input.LT(1);
            	    if ( (input.LA(1)>=MUL && input.LA(1)<=MOD)||input.LA(1)==DIV ) {
            	        input.consume();
            	        root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set71), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(1014,43);
            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression3678);
            	    unaryExpression72=unaryExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, unaryExpression72.getTree());

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);
            } finally {dbg.exitSubRule(19);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1015, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "multiplicativeExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "multiplicativeExpression"

    public static class additiveExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "additiveExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1021:1: additiveExpression : multiplicativeExpression ( ( ADD | SUB ) multiplicativeExpression )* ;
    public final ES3Parser.additiveExpression_return additiveExpression() throws RecognitionException {
        ES3Parser.additiveExpression_return retval = new ES3Parser.additiveExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set74=null;
        ES3Parser.multiplicativeExpression_return multiplicativeExpression73 = null;

        ES3Parser.multiplicativeExpression_return multiplicativeExpression75 = null;


        Object set74_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "additiveExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1021, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1022:2: ( multiplicativeExpression ( ( ADD | SUB ) multiplicativeExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1022:4: multiplicativeExpression ( ( ADD | SUB ) multiplicativeExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1022,4);
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression3696);
            multiplicativeExpression73=multiplicativeExpression();

            state._fsp--;

            adaptor.addChild(root_0, multiplicativeExpression73.getTree());
            dbg.location(1022,29);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1022:29: ( ( ADD | SUB ) multiplicativeExpression )*
            try { dbg.enterSubRule(20);

            loop20:
            do {
                int alt20=2;
                try { dbg.enterDecision(20, decisionCanBacktrack[20]);

                int LA20_0 = input.LA(1);

                if ( ((LA20_0>=ADD && LA20_0<=SUB)) ) {
                    alt20=1;
                }


                } finally {dbg.exitDecision(20);}

                switch (alt20) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1022:31: ( ADD | SUB ) multiplicativeExpression
            	    {
            	    dbg.location(1022,31);
            	    set74=(Token)input.LT(1);
            	    set74=(Token)input.LT(1);
            	    if ( (input.LA(1)>=ADD && input.LA(1)<=SUB) ) {
            	        input.consume();
            	        root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set74), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(1022,46);
            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression3711);
            	    multiplicativeExpression75=multiplicativeExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, multiplicativeExpression75.getTree());

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);
            } finally {dbg.exitSubRule(20);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1023, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "additiveExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "additiveExpression"

    public static class shiftExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "shiftExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1029:1: shiftExpression : additiveExpression ( ( SHL | SHR | SHU ) additiveExpression )* ;
    public final ES3Parser.shiftExpression_return shiftExpression() throws RecognitionException {
        ES3Parser.shiftExpression_return retval = new ES3Parser.shiftExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set77=null;
        ES3Parser.additiveExpression_return additiveExpression76 = null;

        ES3Parser.additiveExpression_return additiveExpression78 = null;


        Object set77_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "shiftExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1029, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1030:2: ( additiveExpression ( ( SHL | SHR | SHU ) additiveExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1030:4: additiveExpression ( ( SHL | SHR | SHU ) additiveExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1030,4);
            pushFollow(FOLLOW_additiveExpression_in_shiftExpression3730);
            additiveExpression76=additiveExpression();

            state._fsp--;

            adaptor.addChild(root_0, additiveExpression76.getTree());
            dbg.location(1030,23);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1030:23: ( ( SHL | SHR | SHU ) additiveExpression )*
            try { dbg.enterSubRule(21);

            loop21:
            do {
                int alt21=2;
                try { dbg.enterDecision(21, decisionCanBacktrack[21]);

                int LA21_0 = input.LA(1);

                if ( ((LA21_0>=SHL && LA21_0<=SHU)) ) {
                    alt21=1;
                }


                } finally {dbg.exitDecision(21);}

                switch (alt21) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1030:25: ( SHL | SHR | SHU ) additiveExpression
            	    {
            	    dbg.location(1030,25);
            	    set77=(Token)input.LT(1);
            	    set77=(Token)input.LT(1);
            	    if ( (input.LA(1)>=SHL && input.LA(1)<=SHU) ) {
            	        input.consume();
            	        root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set77), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(1030,46);
            	    pushFollow(FOLLOW_additiveExpression_in_shiftExpression3749);
            	    additiveExpression78=additiveExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, additiveExpression78.getTree());

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);
            } finally {dbg.exitSubRule(21);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1031, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "shiftExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "shiftExpression"

    public static class relationalExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relationalExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1037:1: relationalExpression : shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF | IN ) shiftExpression )* ;
    public final ES3Parser.relationalExpression_return relationalExpression() throws RecognitionException {
        ES3Parser.relationalExpression_return retval = new ES3Parser.relationalExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set80=null;
        ES3Parser.shiftExpression_return shiftExpression79 = null;

        ES3Parser.shiftExpression_return shiftExpression81 = null;


        Object set80_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "relationalExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1037, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1038:2: ( shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF | IN ) shiftExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1038:4: shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF | IN ) shiftExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1038,4);
            pushFollow(FOLLOW_shiftExpression_in_relationalExpression3768);
            shiftExpression79=shiftExpression();

            state._fsp--;

            adaptor.addChild(root_0, shiftExpression79.getTree());
            dbg.location(1038,20);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1038:20: ( ( LT | GT | LTE | GTE | INSTANCEOF | IN ) shiftExpression )*
            try { dbg.enterSubRule(22);

            loop22:
            do {
                int alt22=2;
                try { dbg.enterDecision(22, decisionCanBacktrack[22]);

                int LA22_0 = input.LA(1);

                if ( ((LA22_0>=IN && LA22_0<=INSTANCEOF)||(LA22_0>=LT && LA22_0<=GTE)) ) {
                    alt22=1;
                }


                } finally {dbg.exitDecision(22);}

                switch (alt22) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1038:22: ( LT | GT | LTE | GTE | INSTANCEOF | IN ) shiftExpression
            	    {
            	    dbg.location(1038,22);
            	    set80=(Token)input.LT(1);
            	    set80=(Token)input.LT(1);
            	    if ( (input.LA(1)>=IN && input.LA(1)<=INSTANCEOF)||(input.LA(1)>=LT && input.LA(1)<=GTE) ) {
            	        input.consume();
            	        root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set80), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(1038,65);
            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpression3799);
            	    shiftExpression81=shiftExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, shiftExpression81.getTree());

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);
            } finally {dbg.exitSubRule(22);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1039, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "relationalExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "relationalExpression"

    public static class relationalExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relationalExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1041:1: relationalExpressionNoIn : shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF ) shiftExpression )* ;
    public final ES3Parser.relationalExpressionNoIn_return relationalExpressionNoIn() throws RecognitionException {
        ES3Parser.relationalExpressionNoIn_return retval = new ES3Parser.relationalExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set83=null;
        ES3Parser.shiftExpression_return shiftExpression82 = null;

        ES3Parser.shiftExpression_return shiftExpression84 = null;


        Object set83_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "relationalExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1041, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1042:2: ( shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF ) shiftExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1042:4: shiftExpression ( ( LT | GT | LTE | GTE | INSTANCEOF ) shiftExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1042,4);
            pushFollow(FOLLOW_shiftExpression_in_relationalExpressionNoIn3813);
            shiftExpression82=shiftExpression();

            state._fsp--;

            adaptor.addChild(root_0, shiftExpression82.getTree());
            dbg.location(1042,20);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1042:20: ( ( LT | GT | LTE | GTE | INSTANCEOF ) shiftExpression )*
            try { dbg.enterSubRule(23);

            loop23:
            do {
                int alt23=2;
                try { dbg.enterDecision(23, decisionCanBacktrack[23]);

                int LA23_0 = input.LA(1);

                if ( (LA23_0==INSTANCEOF||(LA23_0>=LT && LA23_0<=GTE)) ) {
                    alt23=1;
                }


                } finally {dbg.exitDecision(23);}

                switch (alt23) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1042:22: ( LT | GT | LTE | GTE | INSTANCEOF ) shiftExpression
            	    {
            	    dbg.location(1042,22);
            	    set83=(Token)input.LT(1);
            	    set83=(Token)input.LT(1);
            	    if ( input.LA(1)==INSTANCEOF||(input.LA(1)>=LT && input.LA(1)<=GTE) ) {
            	        input.consume();
            	        root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set83), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(1042,60);
            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpressionNoIn3840);
            	    shiftExpression84=shiftExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, shiftExpression84.getTree());

            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);
            } finally {dbg.exitSubRule(23);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1043, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "relationalExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "relationalExpressionNoIn"

    public static class equalityExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "equalityExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1049:1: equalityExpression : relationalExpression ( ( EQ | NEQ | SAME | NSAME ) relationalExpression )* ;
    public final ES3Parser.equalityExpression_return equalityExpression() throws RecognitionException {
        ES3Parser.equalityExpression_return retval = new ES3Parser.equalityExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set86=null;
        ES3Parser.relationalExpression_return relationalExpression85 = null;

        ES3Parser.relationalExpression_return relationalExpression87 = null;


        Object set86_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "equalityExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1049, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1050:2: ( relationalExpression ( ( EQ | NEQ | SAME | NSAME ) relationalExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1050:4: relationalExpression ( ( EQ | NEQ | SAME | NSAME ) relationalExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1050,4);
            pushFollow(FOLLOW_relationalExpression_in_equalityExpression3859);
            relationalExpression85=relationalExpression();

            state._fsp--;

            adaptor.addChild(root_0, relationalExpression85.getTree());
            dbg.location(1050,25);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1050:25: ( ( EQ | NEQ | SAME | NSAME ) relationalExpression )*
            try { dbg.enterSubRule(24);

            loop24:
            do {
                int alt24=2;
                try { dbg.enterDecision(24, decisionCanBacktrack[24]);

                int LA24_0 = input.LA(1);

                if ( ((LA24_0>=EQ && LA24_0<=NSAME)) ) {
                    alt24=1;
                }


                } finally {dbg.exitDecision(24);}

                switch (alt24) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1050:27: ( EQ | NEQ | SAME | NSAME ) relationalExpression
            	    {
            	    dbg.location(1050,27);
            	    set86=(Token)input.LT(1);
            	    set86=(Token)input.LT(1);
            	    if ( (input.LA(1)>=EQ && input.LA(1)<=NSAME) ) {
            	        input.consume();
            	        root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set86), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(1050,56);
            	    pushFollow(FOLLOW_relationalExpression_in_equalityExpression3882);
            	    relationalExpression87=relationalExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, relationalExpression87.getTree());

            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);
            } finally {dbg.exitSubRule(24);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1051, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "equalityExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "equalityExpression"

    public static class equalityExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "equalityExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1053:1: equalityExpressionNoIn : relationalExpressionNoIn ( ( EQ | NEQ | SAME | NSAME ) relationalExpressionNoIn )* ;
    public final ES3Parser.equalityExpressionNoIn_return equalityExpressionNoIn() throws RecognitionException {
        ES3Parser.equalityExpressionNoIn_return retval = new ES3Parser.equalityExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set89=null;
        ES3Parser.relationalExpressionNoIn_return relationalExpressionNoIn88 = null;

        ES3Parser.relationalExpressionNoIn_return relationalExpressionNoIn90 = null;


        Object set89_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "equalityExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1053, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1054:2: ( relationalExpressionNoIn ( ( EQ | NEQ | SAME | NSAME ) relationalExpressionNoIn )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1054:4: relationalExpressionNoIn ( ( EQ | NEQ | SAME | NSAME ) relationalExpressionNoIn )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1054,4);
            pushFollow(FOLLOW_relationalExpressionNoIn_in_equalityExpressionNoIn3896);
            relationalExpressionNoIn88=relationalExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, relationalExpressionNoIn88.getTree());
            dbg.location(1054,29);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1054:29: ( ( EQ | NEQ | SAME | NSAME ) relationalExpressionNoIn )*
            try { dbg.enterSubRule(25);

            loop25:
            do {
                int alt25=2;
                try { dbg.enterDecision(25, decisionCanBacktrack[25]);

                int LA25_0 = input.LA(1);

                if ( ((LA25_0>=EQ && LA25_0<=NSAME)) ) {
                    alt25=1;
                }


                } finally {dbg.exitDecision(25);}

                switch (alt25) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1054:31: ( EQ | NEQ | SAME | NSAME ) relationalExpressionNoIn
            	    {
            	    dbg.location(1054,31);
            	    set89=(Token)input.LT(1);
            	    set89=(Token)input.LT(1);
            	    if ( (input.LA(1)>=EQ && input.LA(1)<=NSAME) ) {
            	        input.consume();
            	        root_0 = (Object)adaptor.becomeRoot((Object)adaptor.create(set89), root_0);
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(1054,60);
            	    pushFollow(FOLLOW_relationalExpressionNoIn_in_equalityExpressionNoIn3919);
            	    relationalExpressionNoIn90=relationalExpressionNoIn();

            	    state._fsp--;

            	    adaptor.addChild(root_0, relationalExpressionNoIn90.getTree());

            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);
            } finally {dbg.exitSubRule(25);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1055, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "equalityExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "equalityExpressionNoIn"

    public static class bitwiseANDExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bitwiseANDExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1061:1: bitwiseANDExpression : equalityExpression ( AND equalityExpression )* ;
    public final ES3Parser.bitwiseANDExpression_return bitwiseANDExpression() throws RecognitionException {
        ES3Parser.bitwiseANDExpression_return retval = new ES3Parser.bitwiseANDExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND92=null;
        ES3Parser.equalityExpression_return equalityExpression91 = null;

        ES3Parser.equalityExpression_return equalityExpression93 = null;


        Object AND92_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "bitwiseANDExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1061, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1062:2: ( equalityExpression ( AND equalityExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1062:4: equalityExpression ( AND equalityExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1062,4);
            pushFollow(FOLLOW_equalityExpression_in_bitwiseANDExpression3939);
            equalityExpression91=equalityExpression();

            state._fsp--;

            adaptor.addChild(root_0, equalityExpression91.getTree());
            dbg.location(1062,23);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1062:23: ( AND equalityExpression )*
            try { dbg.enterSubRule(26);

            loop26:
            do {
                int alt26=2;
                try { dbg.enterDecision(26, decisionCanBacktrack[26]);

                int LA26_0 = input.LA(1);

                if ( (LA26_0==AND) ) {
                    alt26=1;
                }


                } finally {dbg.exitDecision(26);}

                switch (alt26) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1062:25: AND equalityExpression
            	    {
            	    dbg.location(1062,28);
            	    AND92=(Token)match(input,AND,FOLLOW_AND_in_bitwiseANDExpression3943); 
            	    AND92_tree = (Object)adaptor.create(AND92);
            	    root_0 = (Object)adaptor.becomeRoot(AND92_tree, root_0);

            	    dbg.location(1062,30);
            	    pushFollow(FOLLOW_equalityExpression_in_bitwiseANDExpression3946);
            	    equalityExpression93=equalityExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, equalityExpression93.getTree());

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);
            } finally {dbg.exitSubRule(26);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1063, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "bitwiseANDExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "bitwiseANDExpression"

    public static class bitwiseANDExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bitwiseANDExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1065:1: bitwiseANDExpressionNoIn : equalityExpressionNoIn ( AND equalityExpressionNoIn )* ;
    public final ES3Parser.bitwiseANDExpressionNoIn_return bitwiseANDExpressionNoIn() throws RecognitionException {
        ES3Parser.bitwiseANDExpressionNoIn_return retval = new ES3Parser.bitwiseANDExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND95=null;
        ES3Parser.equalityExpressionNoIn_return equalityExpressionNoIn94 = null;

        ES3Parser.equalityExpressionNoIn_return equalityExpressionNoIn96 = null;


        Object AND95_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "bitwiseANDExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1065, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1066:2: ( equalityExpressionNoIn ( AND equalityExpressionNoIn )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1066:4: equalityExpressionNoIn ( AND equalityExpressionNoIn )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1066,4);
            pushFollow(FOLLOW_equalityExpressionNoIn_in_bitwiseANDExpressionNoIn3960);
            equalityExpressionNoIn94=equalityExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, equalityExpressionNoIn94.getTree());
            dbg.location(1066,27);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1066:27: ( AND equalityExpressionNoIn )*
            try { dbg.enterSubRule(27);

            loop27:
            do {
                int alt27=2;
                try { dbg.enterDecision(27, decisionCanBacktrack[27]);

                int LA27_0 = input.LA(1);

                if ( (LA27_0==AND) ) {
                    alt27=1;
                }


                } finally {dbg.exitDecision(27);}

                switch (alt27) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1066:29: AND equalityExpressionNoIn
            	    {
            	    dbg.location(1066,32);
            	    AND95=(Token)match(input,AND,FOLLOW_AND_in_bitwiseANDExpressionNoIn3964); 
            	    AND95_tree = (Object)adaptor.create(AND95);
            	    root_0 = (Object)adaptor.becomeRoot(AND95_tree, root_0);

            	    dbg.location(1066,34);
            	    pushFollow(FOLLOW_equalityExpressionNoIn_in_bitwiseANDExpressionNoIn3967);
            	    equalityExpressionNoIn96=equalityExpressionNoIn();

            	    state._fsp--;

            	    adaptor.addChild(root_0, equalityExpressionNoIn96.getTree());

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);
            } finally {dbg.exitSubRule(27);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1067, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "bitwiseANDExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "bitwiseANDExpressionNoIn"

    public static class bitwiseXORExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bitwiseXORExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1069:1: bitwiseXORExpression : bitwiseANDExpression ( XOR bitwiseANDExpression )* ;
    public final ES3Parser.bitwiseXORExpression_return bitwiseXORExpression() throws RecognitionException {
        ES3Parser.bitwiseXORExpression_return retval = new ES3Parser.bitwiseXORExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token XOR98=null;
        ES3Parser.bitwiseANDExpression_return bitwiseANDExpression97 = null;

        ES3Parser.bitwiseANDExpression_return bitwiseANDExpression99 = null;


        Object XOR98_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "bitwiseXORExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1069, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1070:2: ( bitwiseANDExpression ( XOR bitwiseANDExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1070:4: bitwiseANDExpression ( XOR bitwiseANDExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1070,4);
            pushFollow(FOLLOW_bitwiseANDExpression_in_bitwiseXORExpression3983);
            bitwiseANDExpression97=bitwiseANDExpression();

            state._fsp--;

            adaptor.addChild(root_0, bitwiseANDExpression97.getTree());
            dbg.location(1070,25);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1070:25: ( XOR bitwiseANDExpression )*
            try { dbg.enterSubRule(28);

            loop28:
            do {
                int alt28=2;
                try { dbg.enterDecision(28, decisionCanBacktrack[28]);

                int LA28_0 = input.LA(1);

                if ( (LA28_0==XOR) ) {
                    alt28=1;
                }


                } finally {dbg.exitDecision(28);}

                switch (alt28) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1070:27: XOR bitwiseANDExpression
            	    {
            	    dbg.location(1070,30);
            	    XOR98=(Token)match(input,XOR,FOLLOW_XOR_in_bitwiseXORExpression3987); 
            	    XOR98_tree = (Object)adaptor.create(XOR98);
            	    root_0 = (Object)adaptor.becomeRoot(XOR98_tree, root_0);

            	    dbg.location(1070,32);
            	    pushFollow(FOLLOW_bitwiseANDExpression_in_bitwiseXORExpression3990);
            	    bitwiseANDExpression99=bitwiseANDExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, bitwiseANDExpression99.getTree());

            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);
            } finally {dbg.exitSubRule(28);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1071, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "bitwiseXORExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "bitwiseXORExpression"

    public static class bitwiseXORExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bitwiseXORExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1073:1: bitwiseXORExpressionNoIn : bitwiseANDExpressionNoIn ( XOR bitwiseANDExpressionNoIn )* ;
    public final ES3Parser.bitwiseXORExpressionNoIn_return bitwiseXORExpressionNoIn() throws RecognitionException {
        ES3Parser.bitwiseXORExpressionNoIn_return retval = new ES3Parser.bitwiseXORExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token XOR101=null;
        ES3Parser.bitwiseANDExpressionNoIn_return bitwiseANDExpressionNoIn100 = null;

        ES3Parser.bitwiseANDExpressionNoIn_return bitwiseANDExpressionNoIn102 = null;


        Object XOR101_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "bitwiseXORExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1073, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1074:2: ( bitwiseANDExpressionNoIn ( XOR bitwiseANDExpressionNoIn )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1074:4: bitwiseANDExpressionNoIn ( XOR bitwiseANDExpressionNoIn )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1074,4);
            pushFollow(FOLLOW_bitwiseANDExpressionNoIn_in_bitwiseXORExpressionNoIn4006);
            bitwiseANDExpressionNoIn100=bitwiseANDExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, bitwiseANDExpressionNoIn100.getTree());
            dbg.location(1074,29);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1074:29: ( XOR bitwiseANDExpressionNoIn )*
            try { dbg.enterSubRule(29);

            loop29:
            do {
                int alt29=2;
                try { dbg.enterDecision(29, decisionCanBacktrack[29]);

                int LA29_0 = input.LA(1);

                if ( (LA29_0==XOR) ) {
                    alt29=1;
                }


                } finally {dbg.exitDecision(29);}

                switch (alt29) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1074:31: XOR bitwiseANDExpressionNoIn
            	    {
            	    dbg.location(1074,34);
            	    XOR101=(Token)match(input,XOR,FOLLOW_XOR_in_bitwiseXORExpressionNoIn4010); 
            	    XOR101_tree = (Object)adaptor.create(XOR101);
            	    root_0 = (Object)adaptor.becomeRoot(XOR101_tree, root_0);

            	    dbg.location(1074,36);
            	    pushFollow(FOLLOW_bitwiseANDExpressionNoIn_in_bitwiseXORExpressionNoIn4013);
            	    bitwiseANDExpressionNoIn102=bitwiseANDExpressionNoIn();

            	    state._fsp--;

            	    adaptor.addChild(root_0, bitwiseANDExpressionNoIn102.getTree());

            	    }
            	    break;

            	default :
            	    break loop29;
                }
            } while (true);
            } finally {dbg.exitSubRule(29);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1075, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "bitwiseXORExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "bitwiseXORExpressionNoIn"

    public static class bitwiseORExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bitwiseORExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1077:1: bitwiseORExpression : bitwiseXORExpression ( OR bitwiseXORExpression )* ;
    public final ES3Parser.bitwiseORExpression_return bitwiseORExpression() throws RecognitionException {
        ES3Parser.bitwiseORExpression_return retval = new ES3Parser.bitwiseORExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OR104=null;
        ES3Parser.bitwiseXORExpression_return bitwiseXORExpression103 = null;

        ES3Parser.bitwiseXORExpression_return bitwiseXORExpression105 = null;


        Object OR104_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "bitwiseORExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1077, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1078:2: ( bitwiseXORExpression ( OR bitwiseXORExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1078:4: bitwiseXORExpression ( OR bitwiseXORExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1078,4);
            pushFollow(FOLLOW_bitwiseXORExpression_in_bitwiseORExpression4028);
            bitwiseXORExpression103=bitwiseXORExpression();

            state._fsp--;

            adaptor.addChild(root_0, bitwiseXORExpression103.getTree());
            dbg.location(1078,25);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1078:25: ( OR bitwiseXORExpression )*
            try { dbg.enterSubRule(30);

            loop30:
            do {
                int alt30=2;
                try { dbg.enterDecision(30, decisionCanBacktrack[30]);

                int LA30_0 = input.LA(1);

                if ( (LA30_0==OR) ) {
                    alt30=1;
                }


                } finally {dbg.exitDecision(30);}

                switch (alt30) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1078:27: OR bitwiseXORExpression
            	    {
            	    dbg.location(1078,29);
            	    OR104=(Token)match(input,OR,FOLLOW_OR_in_bitwiseORExpression4032); 
            	    OR104_tree = (Object)adaptor.create(OR104);
            	    root_0 = (Object)adaptor.becomeRoot(OR104_tree, root_0);

            	    dbg.location(1078,31);
            	    pushFollow(FOLLOW_bitwiseXORExpression_in_bitwiseORExpression4035);
            	    bitwiseXORExpression105=bitwiseXORExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, bitwiseXORExpression105.getTree());

            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);
            } finally {dbg.exitSubRule(30);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1079, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "bitwiseORExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "bitwiseORExpression"

    public static class bitwiseORExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bitwiseORExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1081:1: bitwiseORExpressionNoIn : bitwiseXORExpressionNoIn ( OR bitwiseXORExpressionNoIn )* ;
    public final ES3Parser.bitwiseORExpressionNoIn_return bitwiseORExpressionNoIn() throws RecognitionException {
        ES3Parser.bitwiseORExpressionNoIn_return retval = new ES3Parser.bitwiseORExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OR107=null;
        ES3Parser.bitwiseXORExpressionNoIn_return bitwiseXORExpressionNoIn106 = null;

        ES3Parser.bitwiseXORExpressionNoIn_return bitwiseXORExpressionNoIn108 = null;


        Object OR107_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "bitwiseORExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1081, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1082:2: ( bitwiseXORExpressionNoIn ( OR bitwiseXORExpressionNoIn )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1082:4: bitwiseXORExpressionNoIn ( OR bitwiseXORExpressionNoIn )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1082,4);
            pushFollow(FOLLOW_bitwiseXORExpressionNoIn_in_bitwiseORExpressionNoIn4050);
            bitwiseXORExpressionNoIn106=bitwiseXORExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, bitwiseXORExpressionNoIn106.getTree());
            dbg.location(1082,29);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1082:29: ( OR bitwiseXORExpressionNoIn )*
            try { dbg.enterSubRule(31);

            loop31:
            do {
                int alt31=2;
                try { dbg.enterDecision(31, decisionCanBacktrack[31]);

                int LA31_0 = input.LA(1);

                if ( (LA31_0==OR) ) {
                    alt31=1;
                }


                } finally {dbg.exitDecision(31);}

                switch (alt31) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1082:31: OR bitwiseXORExpressionNoIn
            	    {
            	    dbg.location(1082,33);
            	    OR107=(Token)match(input,OR,FOLLOW_OR_in_bitwiseORExpressionNoIn4054); 
            	    OR107_tree = (Object)adaptor.create(OR107);
            	    root_0 = (Object)adaptor.becomeRoot(OR107_tree, root_0);

            	    dbg.location(1082,35);
            	    pushFollow(FOLLOW_bitwiseXORExpressionNoIn_in_bitwiseORExpressionNoIn4057);
            	    bitwiseXORExpressionNoIn108=bitwiseXORExpressionNoIn();

            	    state._fsp--;

            	    adaptor.addChild(root_0, bitwiseXORExpressionNoIn108.getTree());

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);
            } finally {dbg.exitSubRule(31);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1083, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "bitwiseORExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "bitwiseORExpressionNoIn"

    public static class logicalANDExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "logicalANDExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1089:1: logicalANDExpression : bitwiseORExpression ( LAND bitwiseORExpression )* ;
    public final ES3Parser.logicalANDExpression_return logicalANDExpression() throws RecognitionException {
        ES3Parser.logicalANDExpression_return retval = new ES3Parser.logicalANDExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LAND110=null;
        ES3Parser.bitwiseORExpression_return bitwiseORExpression109 = null;

        ES3Parser.bitwiseORExpression_return bitwiseORExpression111 = null;


        Object LAND110_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "logicalANDExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1089, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1090:2: ( bitwiseORExpression ( LAND bitwiseORExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1090:4: bitwiseORExpression ( LAND bitwiseORExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1090,4);
            pushFollow(FOLLOW_bitwiseORExpression_in_logicalANDExpression4076);
            bitwiseORExpression109=bitwiseORExpression();

            state._fsp--;

            adaptor.addChild(root_0, bitwiseORExpression109.getTree());
            dbg.location(1090,24);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1090:24: ( LAND bitwiseORExpression )*
            try { dbg.enterSubRule(32);

            loop32:
            do {
                int alt32=2;
                try { dbg.enterDecision(32, decisionCanBacktrack[32]);

                int LA32_0 = input.LA(1);

                if ( (LA32_0==LAND) ) {
                    alt32=1;
                }


                } finally {dbg.exitDecision(32);}

                switch (alt32) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1090:26: LAND bitwiseORExpression
            	    {
            	    dbg.location(1090,30);
            	    LAND110=(Token)match(input,LAND,FOLLOW_LAND_in_logicalANDExpression4080); 
            	    LAND110_tree = (Object)adaptor.create(LAND110);
            	    root_0 = (Object)adaptor.becomeRoot(LAND110_tree, root_0);

            	    dbg.location(1090,32);
            	    pushFollow(FOLLOW_bitwiseORExpression_in_logicalANDExpression4083);
            	    bitwiseORExpression111=bitwiseORExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, bitwiseORExpression111.getTree());

            	    }
            	    break;

            	default :
            	    break loop32;
                }
            } while (true);
            } finally {dbg.exitSubRule(32);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1091, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "logicalANDExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "logicalANDExpression"

    public static class logicalANDExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "logicalANDExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1093:1: logicalANDExpressionNoIn : bitwiseORExpressionNoIn ( LAND bitwiseORExpressionNoIn )* ;
    public final ES3Parser.logicalANDExpressionNoIn_return logicalANDExpressionNoIn() throws RecognitionException {
        ES3Parser.logicalANDExpressionNoIn_return retval = new ES3Parser.logicalANDExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LAND113=null;
        ES3Parser.bitwiseORExpressionNoIn_return bitwiseORExpressionNoIn112 = null;

        ES3Parser.bitwiseORExpressionNoIn_return bitwiseORExpressionNoIn114 = null;


        Object LAND113_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "logicalANDExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1093, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1094:2: ( bitwiseORExpressionNoIn ( LAND bitwiseORExpressionNoIn )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1094:4: bitwiseORExpressionNoIn ( LAND bitwiseORExpressionNoIn )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1094,4);
            pushFollow(FOLLOW_bitwiseORExpressionNoIn_in_logicalANDExpressionNoIn4097);
            bitwiseORExpressionNoIn112=bitwiseORExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, bitwiseORExpressionNoIn112.getTree());
            dbg.location(1094,28);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1094:28: ( LAND bitwiseORExpressionNoIn )*
            try { dbg.enterSubRule(33);

            loop33:
            do {
                int alt33=2;
                try { dbg.enterDecision(33, decisionCanBacktrack[33]);

                int LA33_0 = input.LA(1);

                if ( (LA33_0==LAND) ) {
                    alt33=1;
                }


                } finally {dbg.exitDecision(33);}

                switch (alt33) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1094:30: LAND bitwiseORExpressionNoIn
            	    {
            	    dbg.location(1094,34);
            	    LAND113=(Token)match(input,LAND,FOLLOW_LAND_in_logicalANDExpressionNoIn4101); 
            	    LAND113_tree = (Object)adaptor.create(LAND113);
            	    root_0 = (Object)adaptor.becomeRoot(LAND113_tree, root_0);

            	    dbg.location(1094,36);
            	    pushFollow(FOLLOW_bitwiseORExpressionNoIn_in_logicalANDExpressionNoIn4104);
            	    bitwiseORExpressionNoIn114=bitwiseORExpressionNoIn();

            	    state._fsp--;

            	    adaptor.addChild(root_0, bitwiseORExpressionNoIn114.getTree());

            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);
            } finally {dbg.exitSubRule(33);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1095, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "logicalANDExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "logicalANDExpressionNoIn"

    public static class logicalORExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "logicalORExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1097:1: logicalORExpression : logicalANDExpression ( LOR logicalANDExpression )* ;
    public final ES3Parser.logicalORExpression_return logicalORExpression() throws RecognitionException {
        ES3Parser.logicalORExpression_return retval = new ES3Parser.logicalORExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LOR116=null;
        ES3Parser.logicalANDExpression_return logicalANDExpression115 = null;

        ES3Parser.logicalANDExpression_return logicalANDExpression117 = null;


        Object LOR116_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "logicalORExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1097, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1098:2: ( logicalANDExpression ( LOR logicalANDExpression )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1098:4: logicalANDExpression ( LOR logicalANDExpression )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1098,4);
            pushFollow(FOLLOW_logicalANDExpression_in_logicalORExpression4119);
            logicalANDExpression115=logicalANDExpression();

            state._fsp--;

            adaptor.addChild(root_0, logicalANDExpression115.getTree());
            dbg.location(1098,25);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1098:25: ( LOR logicalANDExpression )*
            try { dbg.enterSubRule(34);

            loop34:
            do {
                int alt34=2;
                try { dbg.enterDecision(34, decisionCanBacktrack[34]);

                int LA34_0 = input.LA(1);

                if ( (LA34_0==LOR) ) {
                    alt34=1;
                }


                } finally {dbg.exitDecision(34);}

                switch (alt34) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1098:27: LOR logicalANDExpression
            	    {
            	    dbg.location(1098,30);
            	    LOR116=(Token)match(input,LOR,FOLLOW_LOR_in_logicalORExpression4123); 
            	    LOR116_tree = (Object)adaptor.create(LOR116);
            	    root_0 = (Object)adaptor.becomeRoot(LOR116_tree, root_0);

            	    dbg.location(1098,32);
            	    pushFollow(FOLLOW_logicalANDExpression_in_logicalORExpression4126);
            	    logicalANDExpression117=logicalANDExpression();

            	    state._fsp--;

            	    adaptor.addChild(root_0, logicalANDExpression117.getTree());

            	    }
            	    break;

            	default :
            	    break loop34;
                }
            } while (true);
            } finally {dbg.exitSubRule(34);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1099, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "logicalORExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "logicalORExpression"

    public static class logicalORExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "logicalORExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1101:1: logicalORExpressionNoIn : logicalANDExpressionNoIn ( LOR logicalANDExpressionNoIn )* ;
    public final ES3Parser.logicalORExpressionNoIn_return logicalORExpressionNoIn() throws RecognitionException {
        ES3Parser.logicalORExpressionNoIn_return retval = new ES3Parser.logicalORExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LOR119=null;
        ES3Parser.logicalANDExpressionNoIn_return logicalANDExpressionNoIn118 = null;

        ES3Parser.logicalANDExpressionNoIn_return logicalANDExpressionNoIn120 = null;


        Object LOR119_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "logicalORExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1101, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1102:2: ( logicalANDExpressionNoIn ( LOR logicalANDExpressionNoIn )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1102:4: logicalANDExpressionNoIn ( LOR logicalANDExpressionNoIn )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1102,4);
            pushFollow(FOLLOW_logicalANDExpressionNoIn_in_logicalORExpressionNoIn4141);
            logicalANDExpressionNoIn118=logicalANDExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, logicalANDExpressionNoIn118.getTree());
            dbg.location(1102,29);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1102:29: ( LOR logicalANDExpressionNoIn )*
            try { dbg.enterSubRule(35);

            loop35:
            do {
                int alt35=2;
                try { dbg.enterDecision(35, decisionCanBacktrack[35]);

                int LA35_0 = input.LA(1);

                if ( (LA35_0==LOR) ) {
                    alt35=1;
                }


                } finally {dbg.exitDecision(35);}

                switch (alt35) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1102:31: LOR logicalANDExpressionNoIn
            	    {
            	    dbg.location(1102,34);
            	    LOR119=(Token)match(input,LOR,FOLLOW_LOR_in_logicalORExpressionNoIn4145); 
            	    LOR119_tree = (Object)adaptor.create(LOR119);
            	    root_0 = (Object)adaptor.becomeRoot(LOR119_tree, root_0);

            	    dbg.location(1102,36);
            	    pushFollow(FOLLOW_logicalANDExpressionNoIn_in_logicalORExpressionNoIn4148);
            	    logicalANDExpressionNoIn120=logicalANDExpressionNoIn();

            	    state._fsp--;

            	    adaptor.addChild(root_0, logicalANDExpressionNoIn120.getTree());

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);
            } finally {dbg.exitSubRule(35);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1103, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "logicalORExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "logicalORExpressionNoIn"

    public static class conditionalExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conditionalExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1109:1: conditionalExpression : logicalORExpression ( QUE assignmentExpression COLON assignmentExpression )? ;
    public final ES3Parser.conditionalExpression_return conditionalExpression() throws RecognitionException {
        ES3Parser.conditionalExpression_return retval = new ES3Parser.conditionalExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token QUE122=null;
        Token COLON124=null;
        ES3Parser.logicalORExpression_return logicalORExpression121 = null;

        ES3Parser.assignmentExpression_return assignmentExpression123 = null;

        ES3Parser.assignmentExpression_return assignmentExpression125 = null;


        Object QUE122_tree=null;
        Object COLON124_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "conditionalExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1109, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1110:2: ( logicalORExpression ( QUE assignmentExpression COLON assignmentExpression )? )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1110:4: logicalORExpression ( QUE assignmentExpression COLON assignmentExpression )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1110,4);
            pushFollow(FOLLOW_logicalORExpression_in_conditionalExpression4167);
            logicalORExpression121=logicalORExpression();

            state._fsp--;

            adaptor.addChild(root_0, logicalORExpression121.getTree());
            dbg.location(1110,24);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1110:24: ( QUE assignmentExpression COLON assignmentExpression )?
            int alt36=2;
            try { dbg.enterSubRule(36);
            try { dbg.enterDecision(36, decisionCanBacktrack[36]);

            int LA36_0 = input.LA(1);

            if ( (LA36_0==QUE) ) {
                alt36=1;
            }
            } finally {dbg.exitDecision(36);}

            switch (alt36) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1110:26: QUE assignmentExpression COLON assignmentExpression
                    {
                    dbg.location(1110,29);
                    QUE122=(Token)match(input,QUE,FOLLOW_QUE_in_conditionalExpression4171); 
                    QUE122_tree = (Object)adaptor.create(QUE122);
                    root_0 = (Object)adaptor.becomeRoot(QUE122_tree, root_0);

                    dbg.location(1110,31);
                    pushFollow(FOLLOW_assignmentExpression_in_conditionalExpression4174);
                    assignmentExpression123=assignmentExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpression123.getTree());
                    dbg.location(1110,57);
                    COLON124=(Token)match(input,COLON,FOLLOW_COLON_in_conditionalExpression4176); 
                    dbg.location(1110,59);
                    pushFollow(FOLLOW_assignmentExpression_in_conditionalExpression4179);
                    assignmentExpression125=assignmentExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpression125.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(36);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1111, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "conditionalExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "conditionalExpression"

    public static class conditionalExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "conditionalExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1113:1: conditionalExpressionNoIn : logicalORExpressionNoIn ( QUE assignmentExpressionNoIn COLON assignmentExpressionNoIn )? ;
    public final ES3Parser.conditionalExpressionNoIn_return conditionalExpressionNoIn() throws RecognitionException {
        ES3Parser.conditionalExpressionNoIn_return retval = new ES3Parser.conditionalExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token QUE127=null;
        Token COLON129=null;
        ES3Parser.logicalORExpressionNoIn_return logicalORExpressionNoIn126 = null;

        ES3Parser.assignmentExpressionNoIn_return assignmentExpressionNoIn128 = null;

        ES3Parser.assignmentExpressionNoIn_return assignmentExpressionNoIn130 = null;


        Object QUE127_tree=null;
        Object COLON129_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "conditionalExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1113, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1114:2: ( logicalORExpressionNoIn ( QUE assignmentExpressionNoIn COLON assignmentExpressionNoIn )? )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1114:4: logicalORExpressionNoIn ( QUE assignmentExpressionNoIn COLON assignmentExpressionNoIn )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1114,4);
            pushFollow(FOLLOW_logicalORExpressionNoIn_in_conditionalExpressionNoIn4193);
            logicalORExpressionNoIn126=logicalORExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, logicalORExpressionNoIn126.getTree());
            dbg.location(1114,28);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1114:28: ( QUE assignmentExpressionNoIn COLON assignmentExpressionNoIn )?
            int alt37=2;
            try { dbg.enterSubRule(37);
            try { dbg.enterDecision(37, decisionCanBacktrack[37]);

            int LA37_0 = input.LA(1);

            if ( (LA37_0==QUE) ) {
                alt37=1;
            }
            } finally {dbg.exitDecision(37);}

            switch (alt37) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1114:30: QUE assignmentExpressionNoIn COLON assignmentExpressionNoIn
                    {
                    dbg.location(1114,33);
                    QUE127=(Token)match(input,QUE,FOLLOW_QUE_in_conditionalExpressionNoIn4197); 
                    QUE127_tree = (Object)adaptor.create(QUE127);
                    root_0 = (Object)adaptor.becomeRoot(QUE127_tree, root_0);

                    dbg.location(1114,35);
                    pushFollow(FOLLOW_assignmentExpressionNoIn_in_conditionalExpressionNoIn4200);
                    assignmentExpressionNoIn128=assignmentExpressionNoIn();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpressionNoIn128.getTree());
                    dbg.location(1114,65);
                    COLON129=(Token)match(input,COLON,FOLLOW_COLON_in_conditionalExpressionNoIn4202); 
                    dbg.location(1114,67);
                    pushFollow(FOLLOW_assignmentExpressionNoIn_in_conditionalExpressionNoIn4205);
                    assignmentExpressionNoIn130=assignmentExpressionNoIn();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpressionNoIn130.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(37);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1115, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "conditionalExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "conditionalExpressionNoIn"

    public static class assignmentExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignmentExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1143:1: assignmentExpression : lhs= conditionalExpression ({...}? assignmentOperator assignmentExpression )? ;
    public final ES3Parser.assignmentExpression_return assignmentExpression() throws RecognitionException {
        ES3Parser.assignmentExpression_return retval = new ES3Parser.assignmentExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.conditionalExpression_return lhs = null;

        ES3Parser.assignmentOperator_return assignmentOperator131 = null;

        ES3Parser.assignmentExpression_return assignmentExpression132 = null;




        	Object[] isLhs = new Object[1];

        try { dbg.enterRule(getGrammarFileName(), "assignmentExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1143, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1148:2: (lhs= conditionalExpression ({...}? assignmentOperator assignmentExpression )? )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1148:4: lhs= conditionalExpression ({...}? assignmentOperator assignmentExpression )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1148,7);
            pushFollow(FOLLOW_conditionalExpression_in_assignmentExpression4233);
            lhs=conditionalExpression();

            state._fsp--;

            adaptor.addChild(root_0, lhs.getTree());
            dbg.location(1149,2);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1149:2: ({...}? assignmentOperator assignmentExpression )?
            int alt38=2;
            try { dbg.enterSubRule(38);
            try { dbg.enterDecision(38, decisionCanBacktrack[38]);

            int LA38_0 = input.LA(1);

            if ( ((LA38_0>=ASSIGN && LA38_0<=XORASS)||LA38_0==DIVASS) ) {
                int LA38_1 = input.LA(2);

                if ( (evalPredicate( isLeftHandSideAssign(lhs, isLhs) ," isLeftHandSideAssign(lhs, isLhs) ")) ) {
                    alt38=1;
                }
            }
            } finally {dbg.exitDecision(38);}

            switch (alt38) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1149:4: {...}? assignmentOperator assignmentExpression
                    {
                    dbg.location(1149,4);
                    if ( !(evalPredicate( isLeftHandSideAssign(lhs, isLhs) ," isLeftHandSideAssign(lhs, isLhs) ")) ) {
                        throw new FailedPredicateException(input, "assignmentExpression", " isLeftHandSideAssign(lhs, isLhs) ");
                    }
                    dbg.location(1149,60);
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentExpression4240);
                    assignmentOperator131=assignmentOperator();

                    state._fsp--;

                    root_0 = (Object)adaptor.becomeRoot(assignmentOperator131.getTree(), root_0);
                    dbg.location(1149,62);
                    pushFollow(FOLLOW_assignmentExpression_in_assignmentExpression4243);
                    assignmentExpression132=assignmentExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpression132.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(38);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1150, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "assignmentExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "assignmentExpression"

    public static class assignmentOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignmentOperator"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1152:1: assignmentOperator : ( ASSIGN | MULASS | DIVASS | MODASS | ADDASS | SUBASS | SHLASS | SHRASS | SHUASS | ANDASS | XORASS | ORASS );
    public final ES3Parser.assignmentOperator_return assignmentOperator() throws RecognitionException {
        ES3Parser.assignmentOperator_return retval = new ES3Parser.assignmentOperator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set133=null;

        Object set133_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "assignmentOperator");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1152, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1153:2: ( ASSIGN | MULASS | DIVASS | MODASS | ADDASS | SUBASS | SHLASS | SHRASS | SHUASS | ANDASS | XORASS | ORASS )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1153,2);
            set133=(Token)input.LT(1);
            if ( (input.LA(1)>=ASSIGN && input.LA(1)<=XORASS)||input.LA(1)==DIVASS ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set133));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1154, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "assignmentOperator");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "assignmentOperator"

    public static class assignmentExpressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignmentExpressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1156:1: assignmentExpressionNoIn : lhs= conditionalExpressionNoIn ({...}? assignmentOperator assignmentExpressionNoIn )? ;
    public final ES3Parser.assignmentExpressionNoIn_return assignmentExpressionNoIn() throws RecognitionException {
        ES3Parser.assignmentExpressionNoIn_return retval = new ES3Parser.assignmentExpressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.conditionalExpressionNoIn_return lhs = null;

        ES3Parser.assignmentOperator_return assignmentOperator134 = null;

        ES3Parser.assignmentExpressionNoIn_return assignmentExpressionNoIn135 = null;




        	Object[] isLhs = new Object[1];

        try { dbg.enterRule(getGrammarFileName(), "assignmentExpressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1156, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1161:2: (lhs= conditionalExpressionNoIn ({...}? assignmentOperator assignmentExpressionNoIn )? )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1161:4: lhs= conditionalExpressionNoIn ({...}? assignmentOperator assignmentExpressionNoIn )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1161,7);
            pushFollow(FOLLOW_conditionalExpressionNoIn_in_assignmentExpressionNoIn4320);
            lhs=conditionalExpressionNoIn();

            state._fsp--;

            adaptor.addChild(root_0, lhs.getTree());
            dbg.location(1162,2);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1162:2: ({...}? assignmentOperator assignmentExpressionNoIn )?
            int alt39=2;
            try { dbg.enterSubRule(39);
            try { dbg.enterDecision(39, decisionCanBacktrack[39]);

            int LA39_0 = input.LA(1);

            if ( ((LA39_0>=ASSIGN && LA39_0<=XORASS)||LA39_0==DIVASS) ) {
                int LA39_1 = input.LA(2);

                if ( (evalPredicate( isLeftHandSideAssign(lhs, isLhs) ," isLeftHandSideAssign(lhs, isLhs) ")) ) {
                    alt39=1;
                }
            }
            } finally {dbg.exitDecision(39);}

            switch (alt39) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1162:4: {...}? assignmentOperator assignmentExpressionNoIn
                    {
                    dbg.location(1162,4);
                    if ( !(evalPredicate( isLeftHandSideAssign(lhs, isLhs) ," isLeftHandSideAssign(lhs, isLhs) ")) ) {
                        throw new FailedPredicateException(input, "assignmentExpressionNoIn", " isLeftHandSideAssign(lhs, isLhs) ");
                    }
                    dbg.location(1162,60);
                    pushFollow(FOLLOW_assignmentOperator_in_assignmentExpressionNoIn4327);
                    assignmentOperator134=assignmentOperator();

                    state._fsp--;

                    root_0 = (Object)adaptor.becomeRoot(assignmentOperator134.getTree(), root_0);
                    dbg.location(1162,62);
                    pushFollow(FOLLOW_assignmentExpressionNoIn_in_assignmentExpressionNoIn4330);
                    assignmentExpressionNoIn135=assignmentExpressionNoIn();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpressionNoIn135.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(39);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1163, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "assignmentExpressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "assignmentExpressionNoIn"

    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1169:1: expression : exprs+= assignmentExpression ( COMMA exprs+= assignmentExpression )* -> { $exprs.size() > 1 }? ^( CEXPR ( $exprs)+ ) -> $exprs;
    public final ES3Parser.expression_return expression() throws RecognitionException {
        ES3Parser.expression_return retval = new ES3Parser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMMA136=null;
        List list_exprs=null;
        RuleReturnScope exprs = null;
        Object COMMA136_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_assignmentExpression=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpression");
        try { dbg.enterRule(getGrammarFileName(), "expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1169, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1170:2: (exprs+= assignmentExpression ( COMMA exprs+= assignmentExpression )* -> { $exprs.size() > 1 }? ^( CEXPR ( $exprs)+ ) -> $exprs)
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1170:4: exprs+= assignmentExpression ( COMMA exprs+= assignmentExpression )*
            {
            dbg.location(1170,9);
            pushFollow(FOLLOW_assignmentExpression_in_expression4352);
            exprs=assignmentExpression();

            state._fsp--;

            stream_assignmentExpression.add(exprs.getTree());
            if (list_exprs==null) list_exprs=new ArrayList();
            list_exprs.add(exprs.getTree());

            dbg.location(1170,32);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1170:32: ( COMMA exprs+= assignmentExpression )*
            try { dbg.enterSubRule(40);

            loop40:
            do {
                int alt40=2;
                try { dbg.enterDecision(40, decisionCanBacktrack[40]);

                int LA40_0 = input.LA(1);

                if ( (LA40_0==COMMA) ) {
                    alt40=1;
                }


                } finally {dbg.exitDecision(40);}

                switch (alt40) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1170:34: COMMA exprs+= assignmentExpression
            	    {
            	    dbg.location(1170,34);
            	    COMMA136=(Token)match(input,COMMA,FOLLOW_COMMA_in_expression4356);  
            	    stream_COMMA.add(COMMA136);

            	    dbg.location(1170,45);
            	    pushFollow(FOLLOW_assignmentExpression_in_expression4360);
            	    exprs=assignmentExpression();

            	    state._fsp--;

            	    stream_assignmentExpression.add(exprs.getTree());
            	    if (list_exprs==null) list_exprs=new ArrayList();
            	    list_exprs.add(exprs.getTree());


            	    }
            	    break;

            	default :
            	    break loop40;
                }
            } while (true);
            } finally {dbg.exitSubRule(40);}



            // AST REWRITE
            // elements: exprs, exprs
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: exprs
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_exprs=new RewriteRuleSubtreeStream(adaptor,"token exprs",list_exprs);
            root_0 = (Object)adaptor.nil();
            // 1171:2: -> { $exprs.size() > 1 }? ^( CEXPR ( $exprs)+ )
            if ( list_exprs.size() > 1 ) {
                dbg.location(1171,28);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1171:28: ^( CEXPR ( $exprs)+ )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1171,31);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CEXPR, "CEXPR"), root_1);

                dbg.location(1171,37);
                if ( !(stream_exprs.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_exprs.hasNext() ) {
                    dbg.location(1171,37);
                    adaptor.addChild(root_1, stream_exprs.nextTree());

                }
                stream_exprs.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 1172:2: -> $exprs
            {
                dbg.location(1172,5);
                adaptor.addChild(root_0, stream_exprs.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1173, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "expression"

    public static class expressionNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expressionNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1175:1: expressionNoIn : exprs+= assignmentExpressionNoIn ( COMMA exprs+= assignmentExpressionNoIn )* -> { $exprs.size() > 1 }? ^( CEXPR ( $exprs)+ ) -> $exprs;
    public final ES3Parser.expressionNoIn_return expressionNoIn() throws RecognitionException {
        ES3Parser.expressionNoIn_return retval = new ES3Parser.expressionNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token COMMA137=null;
        List list_exprs=null;
        RuleReturnScope exprs = null;
        Object COMMA137_tree=null;
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_assignmentExpressionNoIn=new RewriteRuleSubtreeStream(adaptor,"rule assignmentExpressionNoIn");
        try { dbg.enterRule(getGrammarFileName(), "expressionNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1175, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1176:2: (exprs+= assignmentExpressionNoIn ( COMMA exprs+= assignmentExpressionNoIn )* -> { $exprs.size() > 1 }? ^( CEXPR ( $exprs)+ ) -> $exprs)
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1176:4: exprs+= assignmentExpressionNoIn ( COMMA exprs+= assignmentExpressionNoIn )*
            {
            dbg.location(1176,9);
            pushFollow(FOLLOW_assignmentExpressionNoIn_in_expressionNoIn4397);
            exprs=assignmentExpressionNoIn();

            state._fsp--;

            stream_assignmentExpressionNoIn.add(exprs.getTree());
            if (list_exprs==null) list_exprs=new ArrayList();
            list_exprs.add(exprs.getTree());

            dbg.location(1176,36);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1176:36: ( COMMA exprs+= assignmentExpressionNoIn )*
            try { dbg.enterSubRule(41);

            loop41:
            do {
                int alt41=2;
                try { dbg.enterDecision(41, decisionCanBacktrack[41]);

                int LA41_0 = input.LA(1);

                if ( (LA41_0==COMMA) ) {
                    alt41=1;
                }


                } finally {dbg.exitDecision(41);}

                switch (alt41) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1176:38: COMMA exprs+= assignmentExpressionNoIn
            	    {
            	    dbg.location(1176,38);
            	    COMMA137=(Token)match(input,COMMA,FOLLOW_COMMA_in_expressionNoIn4401);  
            	    stream_COMMA.add(COMMA137);

            	    dbg.location(1176,49);
            	    pushFollow(FOLLOW_assignmentExpressionNoIn_in_expressionNoIn4405);
            	    exprs=assignmentExpressionNoIn();

            	    state._fsp--;

            	    stream_assignmentExpressionNoIn.add(exprs.getTree());
            	    if (list_exprs==null) list_exprs=new ArrayList();
            	    list_exprs.add(exprs.getTree());


            	    }
            	    break;

            	default :
            	    break loop41;
                }
            } while (true);
            } finally {dbg.exitSubRule(41);}



            // AST REWRITE
            // elements: exprs, exprs
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: exprs
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_exprs=new RewriteRuleSubtreeStream(adaptor,"token exprs",list_exprs);
            root_0 = (Object)adaptor.nil();
            // 1177:2: -> { $exprs.size() > 1 }? ^( CEXPR ( $exprs)+ )
            if ( list_exprs.size() > 1 ) {
                dbg.location(1177,28);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1177:28: ^( CEXPR ( $exprs)+ )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1177,31);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(CEXPR, "CEXPR"), root_1);

                dbg.location(1177,37);
                if ( !(stream_exprs.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_exprs.hasNext() ) {
                    dbg.location(1177,37);
                    adaptor.addChild(root_1, stream_exprs.nextTree());

                }
                stream_exprs.reset();

                adaptor.addChild(root_0, root_1);
                }

            }
            else // 1178:2: -> $exprs
            {
                dbg.location(1178,5);
                adaptor.addChild(root_0, stream_exprs.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1179, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "expressionNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "expressionNoIn"

    public static class semic_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "semic"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1203:1: semic : ( SEMIC | EOF | RBRACE | EOL | MultiLineComment );
    public final ES3Parser.semic_return semic() throws RecognitionException {
        ES3Parser.semic_return retval = new ES3Parser.semic_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEMIC138=null;
        Token EOF139=null;
        Token RBRACE140=null;
        Token EOL141=null;
        Token MultiLineComment142=null;

        Object SEMIC138_tree=null;
        Object EOF139_tree=null;
        Object RBRACE140_tree=null;
        Object EOL141_tree=null;
        Object MultiLineComment142_tree=null;


        	// Mark current position so we can unconsume a RBRACE.
        	int marker = input.mark();
        	// Promote EOL if appropriate	
        	promoteEOL(retval);

        try { dbg.enterRule(getGrammarFileName(), "semic");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1203, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1211:2: ( SEMIC | EOF | RBRACE | EOL | MultiLineComment )
            int alt42=5;
            try { dbg.enterDecision(42, decisionCanBacktrack[42]);

            switch ( input.LA(1) ) {
            case SEMIC:
                {
                alt42=1;
                }
                break;
            case EOF:
                {
                alt42=2;
                }
                break;
            case RBRACE:
                {
                alt42=3;
                }
                break;
            case EOL:
                {
                alt42=4;
                }
                break;
            case MultiLineComment:
                {
                alt42=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(42);}

            switch (alt42) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1211:4: SEMIC
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1211,4);
                    SEMIC138=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_semic4456); 
                    SEMIC138_tree = (Object)adaptor.create(SEMIC138);
                    adaptor.addChild(root_0, SEMIC138_tree);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1212:4: EOF
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1212,4);
                    EOF139=(Token)match(input,EOF,FOLLOW_EOF_in_semic4461); 
                    EOF139_tree = (Object)adaptor.create(EOF139);
                    adaptor.addChild(root_0, EOF139_tree);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1213:4: RBRACE
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1213,4);
                    RBRACE140=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_semic4466); 
                    RBRACE140_tree = (Object)adaptor.create(RBRACE140);
                    adaptor.addChild(root_0, RBRACE140_tree);

                    dbg.location(1213,11);
                     input.rewind(marker); 

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1214:4: EOL
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1214,4);
                    EOL141=(Token)match(input,EOL,FOLLOW_EOL_in_semic4473); 
                    EOL141_tree = (Object)adaptor.create(EOL141);
                    adaptor.addChild(root_0, EOL141_tree);


                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1214:10: MultiLineComment
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1214,10);
                    MultiLineComment142=(Token)match(input,MultiLineComment,FOLLOW_MultiLineComment_in_semic4477); 
                    MultiLineComment142_tree = (Object)adaptor.create(MultiLineComment142);
                    adaptor.addChild(root_0, MultiLineComment142_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1215, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "semic");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "semic"

    public static class statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1222:1: statement options {k=1; } : ({...}? block | printStatement | statementTail );
    public final ES3Parser.statement_return statement() throws RecognitionException {
        ES3Parser.statement_return retval = new ES3Parser.statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.block_return block143 = null;

        ES3Parser.printStatement_return printStatement144 = null;

        ES3Parser.statementTail_return statementTail145 = null;



        try { dbg.enterRule(getGrammarFileName(), "statement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1222, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1227:2: ({...}? block | printStatement | statementTail )
            int alt43=3;
            try { dbg.enterDecision(43, decisionCanBacktrack[43]);

            try {
                isCyclicDecision = true;
                alt43 = dfa43.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(43);}

            switch (alt43) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1227:4: {...}? block
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1227,4);
                    if ( !(evalPredicate( input.LA(1) == LBRACE ," input.LA(1) == LBRACE ")) ) {
                        throw new FailedPredicateException(input, "statement", " input.LA(1) == LBRACE ");
                    }
                    dbg.location(1227,31);
                    pushFollow(FOLLOW_block_in_statement4506);
                    block143=block();

                    state._fsp--;

                    adaptor.addChild(root_0, block143.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1228:4: printStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1228,4);
                    pushFollow(FOLLOW_printStatement_in_statement4511);
                    printStatement144=printStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, printStatement144.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1229:4: statementTail
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1229,4);
                    pushFollow(FOLLOW_statementTail_in_statement4516);
                    statementTail145=statementTail();

                    state._fsp--;

                    adaptor.addChild(root_0, statementTail145.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1230, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "statement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "statement"

    public static class statementTail_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statementTail"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1232:1: statementTail : ( variableStatement | emptyStatement | expressionStatement | ifStatement | iterationStatement | continueStatement | breakStatement | returnStatement | withStatement | labelledStatement | switchStatement | throwStatement | tryStatement );
    public final ES3Parser.statementTail_return statementTail() throws RecognitionException {
        ES3Parser.statementTail_return retval = new ES3Parser.statementTail_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.variableStatement_return variableStatement146 = null;

        ES3Parser.emptyStatement_return emptyStatement147 = null;

        ES3Parser.expressionStatement_return expressionStatement148 = null;

        ES3Parser.ifStatement_return ifStatement149 = null;

        ES3Parser.iterationStatement_return iterationStatement150 = null;

        ES3Parser.continueStatement_return continueStatement151 = null;

        ES3Parser.breakStatement_return breakStatement152 = null;

        ES3Parser.returnStatement_return returnStatement153 = null;

        ES3Parser.withStatement_return withStatement154 = null;

        ES3Parser.labelledStatement_return labelledStatement155 = null;

        ES3Parser.switchStatement_return switchStatement156 = null;

        ES3Parser.throwStatement_return throwStatement157 = null;

        ES3Parser.tryStatement_return tryStatement158 = null;



        try { dbg.enterRule(getGrammarFileName(), "statementTail");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1232, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1233:2: ( variableStatement | emptyStatement | expressionStatement | ifStatement | iterationStatement | continueStatement | breakStatement | returnStatement | withStatement | labelledStatement | switchStatement | throwStatement | tryStatement )
            int alt44=13;
            try { dbg.enterDecision(44, decisionCanBacktrack[44]);

            try {
                isCyclicDecision = true;
                alt44 = dfa44.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(44);}

            switch (alt44) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1233:4: variableStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1233,4);
                    pushFollow(FOLLOW_variableStatement_in_statementTail4528);
                    variableStatement146=variableStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, variableStatement146.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1234:4: emptyStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1234,4);
                    pushFollow(FOLLOW_emptyStatement_in_statementTail4533);
                    emptyStatement147=emptyStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, emptyStatement147.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1235:4: expressionStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1235,4);
                    pushFollow(FOLLOW_expressionStatement_in_statementTail4538);
                    expressionStatement148=expressionStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, expressionStatement148.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1236:4: ifStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1236,4);
                    pushFollow(FOLLOW_ifStatement_in_statementTail4543);
                    ifStatement149=ifStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, ifStatement149.getTree());

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1237:4: iterationStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1237,4);
                    pushFollow(FOLLOW_iterationStatement_in_statementTail4548);
                    iterationStatement150=iterationStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, iterationStatement150.getTree());

                    }
                    break;
                case 6 :
                    dbg.enterAlt(6);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1238:4: continueStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1238,4);
                    pushFollow(FOLLOW_continueStatement_in_statementTail4553);
                    continueStatement151=continueStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, continueStatement151.getTree());

                    }
                    break;
                case 7 :
                    dbg.enterAlt(7);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1239:4: breakStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1239,4);
                    pushFollow(FOLLOW_breakStatement_in_statementTail4558);
                    breakStatement152=breakStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, breakStatement152.getTree());

                    }
                    break;
                case 8 :
                    dbg.enterAlt(8);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1240:4: returnStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1240,4);
                    pushFollow(FOLLOW_returnStatement_in_statementTail4563);
                    returnStatement153=returnStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, returnStatement153.getTree());

                    }
                    break;
                case 9 :
                    dbg.enterAlt(9);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1241:4: withStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1241,4);
                    pushFollow(FOLLOW_withStatement_in_statementTail4568);
                    withStatement154=withStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, withStatement154.getTree());

                    }
                    break;
                case 10 :
                    dbg.enterAlt(10);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1242:4: labelledStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1242,4);
                    pushFollow(FOLLOW_labelledStatement_in_statementTail4573);
                    labelledStatement155=labelledStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, labelledStatement155.getTree());

                    }
                    break;
                case 11 :
                    dbg.enterAlt(11);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1243:4: switchStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1243,4);
                    pushFollow(FOLLOW_switchStatement_in_statementTail4578);
                    switchStatement156=switchStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, switchStatement156.getTree());

                    }
                    break;
                case 12 :
                    dbg.enterAlt(12);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1244:4: throwStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1244,4);
                    pushFollow(FOLLOW_throwStatement_in_statementTail4583);
                    throwStatement157=throwStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, throwStatement157.getTree());

                    }
                    break;
                case 13 :
                    dbg.enterAlt(13);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1245:4: tryStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1245,4);
                    pushFollow(FOLLOW_tryStatement_in_statementTail4588);
                    tryStatement158=tryStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, tryStatement158.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1246, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "statementTail");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "statementTail"

    public static class block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "block"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1250:1: block : lb= LBRACE ( statement )* RBRACE -> ^( BLOCK[$lb, \"BLOCK\"] ( statement )* ) ;
    public final ES3Parser.block_return block() throws RecognitionException {
        ES3Parser.block_return retval = new ES3Parser.block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lb=null;
        Token RBRACE160=null;
        ES3Parser.statement_return statement159 = null;


        Object lb_tree=null;
        Object RBRACE160_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        try { dbg.enterRule(getGrammarFileName(), "block");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1250, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1251:2: (lb= LBRACE ( statement )* RBRACE -> ^( BLOCK[$lb, \"BLOCK\"] ( statement )* ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1251:4: lb= LBRACE ( statement )* RBRACE
            {
            dbg.location(1251,6);
            lb=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_block4603);  
            stream_LBRACE.add(lb);

            dbg.location(1251,14);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1251:14: ( statement )*
            try { dbg.enterSubRule(45);

            loop45:
            do {
                int alt45=2;
                try { dbg.enterDecision(45, decisionCanBacktrack[45]);

                int LA45_0 = input.LA(1);

                if ( ((LA45_0>=NULL && LA45_0<=BREAK)||LA45_0==CONTINUE||(LA45_0>=DELETE && LA45_0<=DO)||(LA45_0>=FOR && LA45_0<=IF)||(LA45_0>=NEW && LA45_0<=WITH)||LA45_0==LBRACE||LA45_0==LPAREN||LA45_0==LBRACK||LA45_0==SEMIC||(LA45_0>=ADD && LA45_0<=SUB)||(LA45_0>=INC && LA45_0<=DEC)||(LA45_0>=NOT && LA45_0<=INV)||(LA45_0>=Identifier && LA45_0<=StringLiteral)||LA45_0==RegularExpressionLiteral||(LA45_0>=DecimalLiteral && LA45_0<=HexIntegerLiteral)) ) {
                    alt45=1;
                }


                } finally {dbg.exitDecision(45);}

                switch (alt45) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1251:14: statement
            	    {
            	    dbg.location(1251,14);
            	    pushFollow(FOLLOW_statement_in_block4605);
            	    statement159=statement();

            	    state._fsp--;

            	    stream_statement.add(statement159.getTree());

            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);
            } finally {dbg.exitSubRule(45);}

            dbg.location(1251,25);
            RBRACE160=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_block4608);  
            stream_RBRACE.add(RBRACE160);



            // AST REWRITE
            // elements: statement
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1252:2: -> ^( BLOCK[$lb, \"BLOCK\"] ( statement )* )
            {
                dbg.location(1252,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1252:5: ^( BLOCK[$lb, \"BLOCK\"] ( statement )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1252,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BLOCK, lb, "BLOCK"), root_1);

                dbg.location(1252,28);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1252:28: ( statement )*
                while ( stream_statement.hasNext() ) {
                    dbg.location(1252,28);
                    adaptor.addChild(root_1, stream_statement.nextTree());

                }
                stream_statement.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1253, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "block");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "block"

    public static class printStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "printStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1259:1: printStatement : print_key LPAREN expression RPAREN ;
    public final ES3Parser.printStatement_return printStatement() throws RecognitionException {
        ES3Parser.printStatement_return retval = new ES3Parser.printStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LPAREN162=null;
        Token RPAREN164=null;
        ES3Parser.print_key_return print_key161 = null;

        ES3Parser.expression_return expression163 = null;


        Object LPAREN162_tree=null;
        Object RPAREN164_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "printStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1259, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1260:2: ( print_key LPAREN expression RPAREN )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1260:4: print_key LPAREN expression RPAREN
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1260,13);
            pushFollow(FOLLOW_print_key_in_printStatement4636);
            print_key161=print_key();

            state._fsp--;

            root_0 = (Object)adaptor.becomeRoot(print_key161.getTree(), root_0);
            dbg.location(1260,21);
            LPAREN162=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_printStatement4639); 
            dbg.location(1260,23);
            pushFollow(FOLLOW_expression_in_printStatement4642);
            expression163=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression163.getTree());
            dbg.location(1260,40);
            RPAREN164=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_printStatement4644); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1261, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "printStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "printStatement"

    public static class print_key_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "print_key"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1263:1: print_key : {...}? =>id= Identifier -> SK_PRINT[$id] ;
    public final ES3Parser.print_key_return print_key() throws RecognitionException {
        ES3Parser.print_key_return retval = new ES3Parser.print_key_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token id=null;

        Object id_tree=null;
        RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");

        try { dbg.enterRule(getGrammarFileName(), "print_key");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1263, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1264:2: ({...}? =>id= Identifier -> SK_PRINT[$id] )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1264:4: {...}? =>id= Identifier
            {
            dbg.location(1264,4);
            if ( !(evalPredicate((validateIdentifierKey(DynJSSoftKeywords.PRINT)),"(validateIdentifierKey(DynJSSoftKeywords.PRINT))")) ) {
                throw new FailedPredicateException(input, "print_key", "(validateIdentifierKey(DynJSSoftKeywords.PRINT))");
            }
            dbg.location(1264,61);
            id=(Token)match(input,Identifier,FOLLOW_Identifier_in_print_key4662);  
            stream_Identifier.add(id);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1265:3: -> SK_PRINT[$id]
            {
                dbg.location(1265,6);
                adaptor.addChild(root_0, (Object)adaptor.create(SK_PRINT, id));

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1266, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "print_key");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "print_key"

    public static class variableStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "variableStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1273:1: variableStatement : VAR variableDeclaration ( COMMA variableDeclaration )* semic -> ^( VAR ( variableDeclaration )+ ) ;
    public final ES3Parser.variableStatement_return variableStatement() throws RecognitionException {
        ES3Parser.variableStatement_return retval = new ES3Parser.variableStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token VAR165=null;
        Token COMMA167=null;
        ES3Parser.variableDeclaration_return variableDeclaration166 = null;

        ES3Parser.variableDeclaration_return variableDeclaration168 = null;

        ES3Parser.semic_return semic169 = null;


        Object VAR165_tree=null;
        Object COMMA167_tree=null;
        RewriteRuleTokenStream stream_VAR=new RewriteRuleTokenStream(adaptor,"token VAR");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_variableDeclaration=new RewriteRuleSubtreeStream(adaptor,"rule variableDeclaration");
        RewriteRuleSubtreeStream stream_semic=new RewriteRuleSubtreeStream(adaptor,"rule semic");
        try { dbg.enterRule(getGrammarFileName(), "variableStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1273, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1274:2: ( VAR variableDeclaration ( COMMA variableDeclaration )* semic -> ^( VAR ( variableDeclaration )+ ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1274:4: VAR variableDeclaration ( COMMA variableDeclaration )* semic
            {
            dbg.location(1274,4);
            VAR165=(Token)match(input,VAR,FOLLOW_VAR_in_variableStatement4685);  
            stream_VAR.add(VAR165);

            dbg.location(1274,8);
            pushFollow(FOLLOW_variableDeclaration_in_variableStatement4687);
            variableDeclaration166=variableDeclaration();

            state._fsp--;

            stream_variableDeclaration.add(variableDeclaration166.getTree());
            dbg.location(1274,28);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1274:28: ( COMMA variableDeclaration )*
            try { dbg.enterSubRule(46);

            loop46:
            do {
                int alt46=2;
                try { dbg.enterDecision(46, decisionCanBacktrack[46]);

                int LA46_0 = input.LA(1);

                if ( (LA46_0==COMMA) ) {
                    alt46=1;
                }


                } finally {dbg.exitDecision(46);}

                switch (alt46) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1274:30: COMMA variableDeclaration
            	    {
            	    dbg.location(1274,30);
            	    COMMA167=(Token)match(input,COMMA,FOLLOW_COMMA_in_variableStatement4691);  
            	    stream_COMMA.add(COMMA167);

            	    dbg.location(1274,36);
            	    pushFollow(FOLLOW_variableDeclaration_in_variableStatement4693);
            	    variableDeclaration168=variableDeclaration();

            	    state._fsp--;

            	    stream_variableDeclaration.add(variableDeclaration168.getTree());

            	    }
            	    break;

            	default :
            	    break loop46;
                }
            } while (true);
            } finally {dbg.exitSubRule(46);}

            dbg.location(1274,59);
            pushFollow(FOLLOW_semic_in_variableStatement4698);
            semic169=semic();

            state._fsp--;

            stream_semic.add(semic169.getTree());


            // AST REWRITE
            // elements: variableDeclaration, VAR
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1275:2: -> ^( VAR ( variableDeclaration )+ )
            {
                dbg.location(1275,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1275:5: ^( VAR ( variableDeclaration )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1275,8);
                root_1 = (Object)adaptor.becomeRoot(stream_VAR.nextNode(), root_1);

                dbg.location(1275,12);
                if ( !(stream_variableDeclaration.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_variableDeclaration.hasNext() ) {
                    dbg.location(1275,12);
                    adaptor.addChild(root_1, stream_variableDeclaration.nextTree());

                }
                stream_variableDeclaration.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1276, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "variableStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "variableStatement"

    public static class variableDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "variableDeclaration"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1278:1: variableDeclaration : Identifier ( ASSIGN assignmentExpression )? ;
    public final ES3Parser.variableDeclaration_return variableDeclaration() throws RecognitionException {
        ES3Parser.variableDeclaration_return retval = new ES3Parser.variableDeclaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Identifier170=null;
        Token ASSIGN171=null;
        ES3Parser.assignmentExpression_return assignmentExpression172 = null;


        Object Identifier170_tree=null;
        Object ASSIGN171_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "variableDeclaration");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1278, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1279:2: ( Identifier ( ASSIGN assignmentExpression )? )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1279:4: Identifier ( ASSIGN assignmentExpression )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1279,4);
            Identifier170=(Token)match(input,Identifier,FOLLOW_Identifier_in_variableDeclaration4721); 
            Identifier170_tree = (Object)adaptor.create(Identifier170);
            adaptor.addChild(root_0, Identifier170_tree);

            dbg.location(1279,15);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1279:15: ( ASSIGN assignmentExpression )?
            int alt47=2;
            try { dbg.enterSubRule(47);
            try { dbg.enterDecision(47, decisionCanBacktrack[47]);

            int LA47_0 = input.LA(1);

            if ( (LA47_0==ASSIGN) ) {
                alt47=1;
            }
            } finally {dbg.exitDecision(47);}

            switch (alt47) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1279:17: ASSIGN assignmentExpression
                    {
                    dbg.location(1279,23);
                    ASSIGN171=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_variableDeclaration4725); 
                    ASSIGN171_tree = (Object)adaptor.create(ASSIGN171);
                    root_0 = (Object)adaptor.becomeRoot(ASSIGN171_tree, root_0);

                    dbg.location(1279,25);
                    pushFollow(FOLLOW_assignmentExpression_in_variableDeclaration4728);
                    assignmentExpression172=assignmentExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpression172.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(47);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1280, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "variableDeclaration");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "variableDeclaration"

    public static class variableDeclarationNoIn_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "variableDeclarationNoIn"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1282:1: variableDeclarationNoIn : Identifier ( ASSIGN assignmentExpressionNoIn )? ;
    public final ES3Parser.variableDeclarationNoIn_return variableDeclarationNoIn() throws RecognitionException {
        ES3Parser.variableDeclarationNoIn_return retval = new ES3Parser.variableDeclarationNoIn_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Identifier173=null;
        Token ASSIGN174=null;
        ES3Parser.assignmentExpressionNoIn_return assignmentExpressionNoIn175 = null;


        Object Identifier173_tree=null;
        Object ASSIGN174_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "variableDeclarationNoIn");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1282, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1283:2: ( Identifier ( ASSIGN assignmentExpressionNoIn )? )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1283:4: Identifier ( ASSIGN assignmentExpressionNoIn )?
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1283,4);
            Identifier173=(Token)match(input,Identifier,FOLLOW_Identifier_in_variableDeclarationNoIn4743); 
            Identifier173_tree = (Object)adaptor.create(Identifier173);
            adaptor.addChild(root_0, Identifier173_tree);

            dbg.location(1283,15);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1283:15: ( ASSIGN assignmentExpressionNoIn )?
            int alt48=2;
            try { dbg.enterSubRule(48);
            try { dbg.enterDecision(48, decisionCanBacktrack[48]);

            int LA48_0 = input.LA(1);

            if ( (LA48_0==ASSIGN) ) {
                alt48=1;
            }
            } finally {dbg.exitDecision(48);}

            switch (alt48) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1283:17: ASSIGN assignmentExpressionNoIn
                    {
                    dbg.location(1283,23);
                    ASSIGN174=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_variableDeclarationNoIn4747); 
                    ASSIGN174_tree = (Object)adaptor.create(ASSIGN174);
                    root_0 = (Object)adaptor.becomeRoot(ASSIGN174_tree, root_0);

                    dbg.location(1283,25);
                    pushFollow(FOLLOW_assignmentExpressionNoIn_in_variableDeclarationNoIn4750);
                    assignmentExpressionNoIn175=assignmentExpressionNoIn();

                    state._fsp--;

                    adaptor.addChild(root_0, assignmentExpressionNoIn175.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(48);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1284, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "variableDeclarationNoIn");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "variableDeclarationNoIn"

    public static class emptyStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "emptyStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1290:1: emptyStatement : SEMIC ;
    public final ES3Parser.emptyStatement_return emptyStatement() throws RecognitionException {
        ES3Parser.emptyStatement_return retval = new ES3Parser.emptyStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEMIC176=null;

        Object SEMIC176_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "emptyStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1290, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1291:2: ( SEMIC )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1291:4: SEMIC
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1291,9);
            SEMIC176=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_emptyStatement4769); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1292, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "emptyStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "emptyStatement"

    public static class expressionStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expressionStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1304:1: expressionStatement : expression semic ;
    public final ES3Parser.expressionStatement_return expressionStatement() throws RecognitionException {
        ES3Parser.expressionStatement_return retval = new ES3Parser.expressionStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.expression_return expression177 = null;

        ES3Parser.semic_return semic178 = null;



        try { dbg.enterRule(getGrammarFileName(), "expressionStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1304, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1305:2: ( expression semic )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1305:4: expression semic
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1305,4);
            pushFollow(FOLLOW_expression_in_expressionStatement4788);
            expression177=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression177.getTree());
            dbg.location(1305,20);
            pushFollow(FOLLOW_semic_in_expressionStatement4790);
            semic178=semic();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1306, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "expressionStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "expressionStatement"

    public static class ifStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ifStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1312:1: ifStatement : IF LPAREN expression RPAREN statement ({...}? ELSE statement )? -> ^( IF expression ( statement )+ ) ;
    public final ES3Parser.ifStatement_return ifStatement() throws RecognitionException {
        ES3Parser.ifStatement_return retval = new ES3Parser.ifStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IF179=null;
        Token LPAREN180=null;
        Token RPAREN182=null;
        Token ELSE184=null;
        ES3Parser.expression_return expression181 = null;

        ES3Parser.statement_return statement183 = null;

        ES3Parser.statement_return statement185 = null;


        Object IF179_tree=null;
        Object LPAREN180_tree=null;
        Object RPAREN182_tree=null;
        Object ELSE184_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
        RewriteRuleTokenStream stream_ELSE=new RewriteRuleTokenStream(adaptor,"token ELSE");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        try { dbg.enterRule(getGrammarFileName(), "ifStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1312, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1314:2: ( IF LPAREN expression RPAREN statement ({...}? ELSE statement )? -> ^( IF expression ( statement )+ ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1314:4: IF LPAREN expression RPAREN statement ({...}? ELSE statement )?
            {
            dbg.location(1314,4);
            IF179=(Token)match(input,IF,FOLLOW_IF_in_ifStatement4808);  
            stream_IF.add(IF179);

            dbg.location(1314,7);
            LPAREN180=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_ifStatement4810);  
            stream_LPAREN.add(LPAREN180);

            dbg.location(1314,14);
            pushFollow(FOLLOW_expression_in_ifStatement4812);
            expression181=expression();

            state._fsp--;

            stream_expression.add(expression181.getTree());
            dbg.location(1314,25);
            RPAREN182=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_ifStatement4814);  
            stream_RPAREN.add(RPAREN182);

            dbg.location(1314,32);
            pushFollow(FOLLOW_statement_in_ifStatement4816);
            statement183=statement();

            state._fsp--;

            stream_statement.add(statement183.getTree());
            dbg.location(1314,42);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1314:42: ({...}? ELSE statement )?
            int alt49=2;
            try { dbg.enterSubRule(49);
            try { dbg.enterDecision(49, decisionCanBacktrack[49]);

            int LA49_0 = input.LA(1);

            if ( (LA49_0==ELSE) ) {
                int LA49_1 = input.LA(2);

                if ( (evalPredicate( input.LA(1) == ELSE ," input.LA(1) == ELSE ")) ) {
                    alt49=1;
                }
            }
            } finally {dbg.exitDecision(49);}

            switch (alt49) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1314:44: {...}? ELSE statement
                    {
                    dbg.location(1314,44);
                    if ( !(evalPredicate( input.LA(1) == ELSE ," input.LA(1) == ELSE ")) ) {
                        throw new FailedPredicateException(input, "ifStatement", " input.LA(1) == ELSE ");
                    }
                    dbg.location(1314,69);
                    ELSE184=(Token)match(input,ELSE,FOLLOW_ELSE_in_ifStatement4822);  
                    stream_ELSE.add(ELSE184);

                    dbg.location(1314,74);
                    pushFollow(FOLLOW_statement_in_ifStatement4824);
                    statement185=statement();

                    state._fsp--;

                    stream_statement.add(statement185.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(49);}



            // AST REWRITE
            // elements: IF, expression, statement
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1315:2: -> ^( IF expression ( statement )+ )
            {
                dbg.location(1315,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1315:5: ^( IF expression ( statement )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1315,8);
                root_1 = (Object)adaptor.becomeRoot(stream_IF.nextNode(), root_1);

                dbg.location(1315,11);
                adaptor.addChild(root_1, stream_expression.nextTree());
                dbg.location(1315,22);
                if ( !(stream_statement.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_statement.hasNext() ) {
                    dbg.location(1315,22);
                    adaptor.addChild(root_1, stream_statement.nextTree());

                }
                stream_statement.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1316, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "ifStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "ifStatement"

    public static class iterationStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "iterationStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1322:1: iterationStatement : ( doStatement | whileStatement | forStatement );
    public final ES3Parser.iterationStatement_return iterationStatement() throws RecognitionException {
        ES3Parser.iterationStatement_return retval = new ES3Parser.iterationStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.doStatement_return doStatement186 = null;

        ES3Parser.whileStatement_return whileStatement187 = null;

        ES3Parser.forStatement_return forStatement188 = null;



        try { dbg.enterRule(getGrammarFileName(), "iterationStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1322, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1323:2: ( doStatement | whileStatement | forStatement )
            int alt50=3;
            try { dbg.enterDecision(50, decisionCanBacktrack[50]);

            switch ( input.LA(1) ) {
            case DO:
                {
                alt50=1;
                }
                break;
            case WHILE:
                {
                alt50=2;
                }
                break;
            case FOR:
                {
                alt50=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(50);}

            switch (alt50) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1323:4: doStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1323,4);
                    pushFollow(FOLLOW_doStatement_in_iterationStatement4857);
                    doStatement186=doStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, doStatement186.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1324:4: whileStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1324,4);
                    pushFollow(FOLLOW_whileStatement_in_iterationStatement4862);
                    whileStatement187=whileStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, whileStatement187.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1325:4: forStatement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1325,4);
                    pushFollow(FOLLOW_forStatement_in_iterationStatement4867);
                    forStatement188=forStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, forStatement188.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1326, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "iterationStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "iterationStatement"

    public static class doStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "doStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1328:1: doStatement : DO statement WHILE LPAREN expression RPAREN semic -> ^( DO statement expression ) ;
    public final ES3Parser.doStatement_return doStatement() throws RecognitionException {
        ES3Parser.doStatement_return retval = new ES3Parser.doStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DO189=null;
        Token WHILE191=null;
        Token LPAREN192=null;
        Token RPAREN194=null;
        ES3Parser.statement_return statement190 = null;

        ES3Parser.expression_return expression193 = null;

        ES3Parser.semic_return semic195 = null;


        Object DO189_tree=null;
        Object WHILE191_tree=null;
        Object LPAREN192_tree=null;
        Object RPAREN194_tree=null;
        RewriteRuleTokenStream stream_DO=new RewriteRuleTokenStream(adaptor,"token DO");
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_WHILE=new RewriteRuleTokenStream(adaptor,"token WHILE");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_semic=new RewriteRuleSubtreeStream(adaptor,"rule semic");
        try { dbg.enterRule(getGrammarFileName(), "doStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1328, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1329:2: ( DO statement WHILE LPAREN expression RPAREN semic -> ^( DO statement expression ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1329:4: DO statement WHILE LPAREN expression RPAREN semic
            {
            dbg.location(1329,4);
            DO189=(Token)match(input,DO,FOLLOW_DO_in_doStatement4879);  
            stream_DO.add(DO189);

            dbg.location(1329,7);
            pushFollow(FOLLOW_statement_in_doStatement4881);
            statement190=statement();

            state._fsp--;

            stream_statement.add(statement190.getTree());
            dbg.location(1329,17);
            WHILE191=(Token)match(input,WHILE,FOLLOW_WHILE_in_doStatement4883);  
            stream_WHILE.add(WHILE191);

            dbg.location(1329,23);
            LPAREN192=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_doStatement4885);  
            stream_LPAREN.add(LPAREN192);

            dbg.location(1329,30);
            pushFollow(FOLLOW_expression_in_doStatement4887);
            expression193=expression();

            state._fsp--;

            stream_expression.add(expression193.getTree());
            dbg.location(1329,41);
            RPAREN194=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_doStatement4889);  
            stream_RPAREN.add(RPAREN194);

            dbg.location(1329,48);
            pushFollow(FOLLOW_semic_in_doStatement4891);
            semic195=semic();

            state._fsp--;

            stream_semic.add(semic195.getTree());


            // AST REWRITE
            // elements: statement, expression, DO
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1330:2: -> ^( DO statement expression )
            {
                dbg.location(1330,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1330:5: ^( DO statement expression )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1330,8);
                root_1 = (Object)adaptor.becomeRoot(stream_DO.nextNode(), root_1);

                dbg.location(1330,11);
                adaptor.addChild(root_1, stream_statement.nextTree());
                dbg.location(1330,21);
                adaptor.addChild(root_1, stream_expression.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1331, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "doStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "doStatement"

    public static class whileStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "whileStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1333:1: whileStatement : WHILE LPAREN expression RPAREN statement ;
    public final ES3Parser.whileStatement_return whileStatement() throws RecognitionException {
        ES3Parser.whileStatement_return retval = new ES3Parser.whileStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WHILE196=null;
        Token LPAREN197=null;
        Token RPAREN199=null;
        ES3Parser.expression_return expression198 = null;

        ES3Parser.statement_return statement200 = null;


        Object WHILE196_tree=null;
        Object LPAREN197_tree=null;
        Object RPAREN199_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "whileStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1333, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1334:2: ( WHILE LPAREN expression RPAREN statement )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1334:4: WHILE LPAREN expression RPAREN statement
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1334,9);
            WHILE196=(Token)match(input,WHILE,FOLLOW_WHILE_in_whileStatement4916); 
            WHILE196_tree = (Object)adaptor.create(WHILE196);
            root_0 = (Object)adaptor.becomeRoot(WHILE196_tree, root_0);

            dbg.location(1334,17);
            LPAREN197=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_whileStatement4919); 
            dbg.location(1334,19);
            pushFollow(FOLLOW_expression_in_whileStatement4922);
            expression198=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression198.getTree());
            dbg.location(1334,36);
            RPAREN199=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_whileStatement4924); 
            dbg.location(1334,38);
            pushFollow(FOLLOW_statement_in_whileStatement4927);
            statement200=statement();

            state._fsp--;

            adaptor.addChild(root_0, statement200.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1335, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "whileStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "whileStatement"

    public static class forStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "forStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1378:1: forStatement : FOR LPAREN forControl RPAREN statement ;
    public final ES3Parser.forStatement_return forStatement() throws RecognitionException {
        ES3Parser.forStatement_return retval = new ES3Parser.forStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FOR201=null;
        Token LPAREN202=null;
        Token RPAREN204=null;
        ES3Parser.forControl_return forControl203 = null;

        ES3Parser.statement_return statement205 = null;


        Object FOR201_tree=null;
        Object LPAREN202_tree=null;
        Object RPAREN204_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "forStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1378, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1379:2: ( FOR LPAREN forControl RPAREN statement )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1379:4: FOR LPAREN forControl RPAREN statement
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1379,7);
            FOR201=(Token)match(input,FOR,FOLLOW_FOR_in_forStatement4940); 
            FOR201_tree = (Object)adaptor.create(FOR201);
            root_0 = (Object)adaptor.becomeRoot(FOR201_tree, root_0);

            dbg.location(1379,15);
            LPAREN202=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_forStatement4943); 
            dbg.location(1379,17);
            pushFollow(FOLLOW_forControl_in_forStatement4946);
            forControl203=forControl();

            state._fsp--;

            adaptor.addChild(root_0, forControl203.getTree());
            dbg.location(1379,34);
            RPAREN204=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_forStatement4948); 
            dbg.location(1379,36);
            pushFollow(FOLLOW_statement_in_forStatement4951);
            statement205=statement();

            state._fsp--;

            adaptor.addChild(root_0, statement205.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1380, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "forStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "forStatement"

    public static class forControl_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "forControl"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1382:1: forControl : ( forControlVar | forControlExpression | forControlSemic );
    public final ES3Parser.forControl_return forControl() throws RecognitionException {
        ES3Parser.forControl_return retval = new ES3Parser.forControl_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.forControlVar_return forControlVar206 = null;

        ES3Parser.forControlExpression_return forControlExpression207 = null;

        ES3Parser.forControlSemic_return forControlSemic208 = null;



        try { dbg.enterRule(getGrammarFileName(), "forControl");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1382, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1383:2: ( forControlVar | forControlExpression | forControlSemic )
            int alt51=3;
            try { dbg.enterDecision(51, decisionCanBacktrack[51]);

            switch ( input.LA(1) ) {
            case VAR:
                {
                alt51=1;
                }
                break;
            case NULL:
            case TRUE:
            case FALSE:
            case DELETE:
            case FUNCTION:
            case NEW:
            case THIS:
            case TYPEOF:
            case VOID:
            case LBRACE:
            case LPAREN:
            case LBRACK:
            case ADD:
            case SUB:
            case INC:
            case DEC:
            case NOT:
            case INV:
            case Identifier:
            case StringLiteral:
            case RegularExpressionLiteral:
            case DecimalLiteral:
            case OctalIntegerLiteral:
            case HexIntegerLiteral:
                {
                alt51=2;
                }
                break;
            case SEMIC:
                {
                alt51=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(51);}

            switch (alt51) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1383:4: forControlVar
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1383,4);
                    pushFollow(FOLLOW_forControlVar_in_forControl4962);
                    forControlVar206=forControlVar();

                    state._fsp--;

                    adaptor.addChild(root_0, forControlVar206.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1384:4: forControlExpression
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1384,4);
                    pushFollow(FOLLOW_forControlExpression_in_forControl4967);
                    forControlExpression207=forControlExpression();

                    state._fsp--;

                    adaptor.addChild(root_0, forControlExpression207.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1385:4: forControlSemic
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1385,4);
                    pushFollow(FOLLOW_forControlSemic_in_forControl4972);
                    forControlSemic208=forControlSemic();

                    state._fsp--;

                    adaptor.addChild(root_0, forControlSemic208.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1386, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "forControl");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "forControl"

    public static class forControlVar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "forControlVar"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1388:1: forControlVar : VAR variableDeclarationNoIn ( ( IN expression -> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) ) ) | ( ( COMMA variableDeclarationNoIn )* SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) ) ) ;
    public final ES3Parser.forControlVar_return forControlVar() throws RecognitionException {
        ES3Parser.forControlVar_return retval = new ES3Parser.forControlVar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token VAR209=null;
        Token IN211=null;
        Token COMMA213=null;
        Token SEMIC215=null;
        Token SEMIC216=null;
        ES3Parser.expression_return ex1 = null;

        ES3Parser.expression_return ex2 = null;

        ES3Parser.variableDeclarationNoIn_return variableDeclarationNoIn210 = null;

        ES3Parser.expression_return expression212 = null;

        ES3Parser.variableDeclarationNoIn_return variableDeclarationNoIn214 = null;


        Object VAR209_tree=null;
        Object IN211_tree=null;
        Object COMMA213_tree=null;
        Object SEMIC215_tree=null;
        Object SEMIC216_tree=null;
        RewriteRuleTokenStream stream_VAR=new RewriteRuleTokenStream(adaptor,"token VAR");
        RewriteRuleTokenStream stream_IN=new RewriteRuleTokenStream(adaptor,"token IN");
        RewriteRuleTokenStream stream_SEMIC=new RewriteRuleTokenStream(adaptor,"token SEMIC");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_variableDeclarationNoIn=new RewriteRuleSubtreeStream(adaptor,"rule variableDeclarationNoIn");
        try { dbg.enterRule(getGrammarFileName(), "forControlVar");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1388, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1389:2: ( VAR variableDeclarationNoIn ( ( IN expression -> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) ) ) | ( ( COMMA variableDeclarationNoIn )* SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) ) ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1389:4: VAR variableDeclarationNoIn ( ( IN expression -> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) ) ) | ( ( COMMA variableDeclarationNoIn )* SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) ) )
            {
            dbg.location(1389,4);
            VAR209=(Token)match(input,VAR,FOLLOW_VAR_in_forControlVar4983);  
            stream_VAR.add(VAR209);

            dbg.location(1389,8);
            pushFollow(FOLLOW_variableDeclarationNoIn_in_forControlVar4985);
            variableDeclarationNoIn210=variableDeclarationNoIn();

            state._fsp--;

            stream_variableDeclarationNoIn.add(variableDeclarationNoIn210.getTree());
            dbg.location(1390,2);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1390:2: ( ( IN expression -> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) ) ) | ( ( COMMA variableDeclarationNoIn )* SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) ) )
            int alt55=2;
            try { dbg.enterSubRule(55);
            try { dbg.enterDecision(55, decisionCanBacktrack[55]);

            int LA55_0 = input.LA(1);

            if ( (LA55_0==IN) ) {
                alt55=1;
            }
            else if ( ((LA55_0>=SEMIC && LA55_0<=COMMA)) ) {
                alt55=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(55);}

            switch (alt55) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1391:3: ( IN expression -> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) ) )
                    {
                    dbg.location(1391,3);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1391:3: ( IN expression -> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) ) )
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1392:4: IN expression
                    {
                    dbg.location(1392,4);
                    IN211=(Token)match(input,IN,FOLLOW_IN_in_forControlVar4997);  
                    stream_IN.add(IN211);

                    dbg.location(1392,7);
                    pushFollow(FOLLOW_expression_in_forControlVar4999);
                    expression212=expression();

                    state._fsp--;

                    stream_expression.add(expression212.getTree());


                    // AST REWRITE
                    // elements: VAR, expression, variableDeclarationNoIn
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 1393:4: -> ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) )
                    {
                        dbg.location(1393,7);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1393:7: ^( FORITER ^( VAR variableDeclarationNoIn ) ^( EXPR expression ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(1393,10);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FORITER, "FORITER"), root_1);

                        dbg.location(1393,18);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1393:18: ^( VAR variableDeclarationNoIn )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1393,21);
                        root_2 = (Object)adaptor.becomeRoot(stream_VAR.nextNode(), root_2);

                        dbg.location(1393,25);
                        adaptor.addChild(root_2, stream_variableDeclarationNoIn.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(1393,51);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1393:51: ^( EXPR expression )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1393,54);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1393,59);
                        adaptor.addChild(root_2, stream_expression.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1396:3: ( ( COMMA variableDeclarationNoIn )* SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) )
                    {
                    dbg.location(1396,3);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1396:3: ( ( COMMA variableDeclarationNoIn )* SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) )
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1397:4: ( COMMA variableDeclarationNoIn )* SEMIC (ex1= expression )? SEMIC (ex2= expression )?
                    {
                    dbg.location(1397,4);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1397:4: ( COMMA variableDeclarationNoIn )*
                    try { dbg.enterSubRule(52);

                    loop52:
                    do {
                        int alt52=2;
                        try { dbg.enterDecision(52, decisionCanBacktrack[52]);

                        int LA52_0 = input.LA(1);

                        if ( (LA52_0==COMMA) ) {
                            alt52=1;
                        }


                        } finally {dbg.exitDecision(52);}

                        switch (alt52) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1397:6: COMMA variableDeclarationNoIn
                    	    {
                    	    dbg.location(1397,6);
                    	    COMMA213=(Token)match(input,COMMA,FOLLOW_COMMA_in_forControlVar5045);  
                    	    stream_COMMA.add(COMMA213);

                    	    dbg.location(1397,12);
                    	    pushFollow(FOLLOW_variableDeclarationNoIn_in_forControlVar5047);
                    	    variableDeclarationNoIn214=variableDeclarationNoIn();

                    	    state._fsp--;

                    	    stream_variableDeclarationNoIn.add(variableDeclarationNoIn214.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop52;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(52);}

                    dbg.location(1397,39);
                    SEMIC215=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_forControlVar5052);  
                    stream_SEMIC.add(SEMIC215);

                    dbg.location(1397,48);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1397:48: (ex1= expression )?
                    int alt53=2;
                    try { dbg.enterSubRule(53);
                    try { dbg.enterDecision(53, decisionCanBacktrack[53]);

                    int LA53_0 = input.LA(1);

                    if ( ((LA53_0>=NULL && LA53_0<=FALSE)||LA53_0==DELETE||LA53_0==FUNCTION||LA53_0==NEW||LA53_0==THIS||LA53_0==TYPEOF||LA53_0==VOID||LA53_0==LBRACE||LA53_0==LPAREN||LA53_0==LBRACK||(LA53_0>=ADD && LA53_0<=SUB)||(LA53_0>=INC && LA53_0<=DEC)||(LA53_0>=NOT && LA53_0<=INV)||(LA53_0>=Identifier && LA53_0<=StringLiteral)||LA53_0==RegularExpressionLiteral||(LA53_0>=DecimalLiteral && LA53_0<=HexIntegerLiteral)) ) {
                        alt53=1;
                    }
                    } finally {dbg.exitDecision(53);}

                    switch (alt53) {
                        case 1 :
                            dbg.enterAlt(1);

                            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1397:48: ex1= expression
                            {
                            dbg.location(1397,48);
                            pushFollow(FOLLOW_expression_in_forControlVar5056);
                            ex1=expression();

                            state._fsp--;

                            stream_expression.add(ex1.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(53);}

                    dbg.location(1397,61);
                    SEMIC216=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_forControlVar5059);  
                    stream_SEMIC.add(SEMIC216);

                    dbg.location(1397,70);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1397:70: (ex2= expression )?
                    int alt54=2;
                    try { dbg.enterSubRule(54);
                    try { dbg.enterDecision(54, decisionCanBacktrack[54]);

                    int LA54_0 = input.LA(1);

                    if ( ((LA54_0>=NULL && LA54_0<=FALSE)||LA54_0==DELETE||LA54_0==FUNCTION||LA54_0==NEW||LA54_0==THIS||LA54_0==TYPEOF||LA54_0==VOID||LA54_0==LBRACE||LA54_0==LPAREN||LA54_0==LBRACK||(LA54_0>=ADD && LA54_0<=SUB)||(LA54_0>=INC && LA54_0<=DEC)||(LA54_0>=NOT && LA54_0<=INV)||(LA54_0>=Identifier && LA54_0<=StringLiteral)||LA54_0==RegularExpressionLiteral||(LA54_0>=DecimalLiteral && LA54_0<=HexIntegerLiteral)) ) {
                        alt54=1;
                    }
                    } finally {dbg.exitDecision(54);}

                    switch (alt54) {
                        case 1 :
                            dbg.enterAlt(1);

                            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1397:70: ex2= expression
                            {
                            dbg.location(1397,70);
                            pushFollow(FOLLOW_expression_in_forControlVar5063);
                            ex2=expression();

                            state._fsp--;

                            stream_expression.add(ex2.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(54);}



                    // AST REWRITE
                    // elements: ex1, variableDeclarationNoIn, VAR, ex2
                    // token labels: 
                    // rule labels: retval, ex2, ex1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_ex2=new RewriteRuleSubtreeStream(adaptor,"rule ex2",ex2!=null?ex2.tree:null);
                    RewriteRuleSubtreeStream stream_ex1=new RewriteRuleSubtreeStream(adaptor,"rule ex1",ex1!=null?ex1.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 1398:4: -> ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) )
                    {
                        dbg.location(1398,7);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1398:7: ^( FORSTEP ^( VAR ( variableDeclarationNoIn )+ ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(1398,10);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FORSTEP, "FORSTEP"), root_1);

                        dbg.location(1398,18);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1398:18: ^( VAR ( variableDeclarationNoIn )+ )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1398,21);
                        root_2 = (Object)adaptor.becomeRoot(stream_VAR.nextNode(), root_2);

                        dbg.location(1398,25);
                        if ( !(stream_variableDeclarationNoIn.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_variableDeclarationNoIn.hasNext() ) {
                            dbg.location(1398,25);
                            adaptor.addChild(root_2, stream_variableDeclarationNoIn.nextTree());

                        }
                        stream_variableDeclarationNoIn.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(1398,52);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1398:52: ^( EXPR ( $ex1)? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1398,55);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1398,60);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1398:60: ( $ex1)?
                        if ( stream_ex1.hasNext() ) {
                            dbg.location(1398,60);
                            adaptor.addChild(root_2, stream_ex1.nextTree());

                        }
                        stream_ex1.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(1398,68);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1398:68: ^( EXPR ( $ex2)? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1398,71);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1398,76);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1398:76: ( $ex2)?
                        if ( stream_ex2.hasNext() ) {
                            dbg.location(1398,76);
                            adaptor.addChild(root_2, stream_ex2.nextTree());

                        }
                        stream_ex2.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }


                    }
                    break;

            }
            } finally {dbg.exitSubRule(55);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1401, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "forControlVar");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "forControlVar"

    public static class forControlExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "forControlExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1403:1: forControlExpression : ex1= expressionNoIn ({...}? ( IN ex2= expression -> ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) ) ) | ( SEMIC (ex2= expression )? SEMIC (ex3= expression )? -> ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) ) ) ) ;
    public final ES3Parser.forControlExpression_return forControlExpression() throws RecognitionException {
        ES3Parser.forControlExpression_return retval = new ES3Parser.forControlExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IN217=null;
        Token SEMIC218=null;
        Token SEMIC219=null;
        ES3Parser.expressionNoIn_return ex1 = null;

        ES3Parser.expression_return ex2 = null;

        ES3Parser.expression_return ex3 = null;


        Object IN217_tree=null;
        Object SEMIC218_tree=null;
        Object SEMIC219_tree=null;
        RewriteRuleTokenStream stream_IN=new RewriteRuleTokenStream(adaptor,"token IN");
        RewriteRuleTokenStream stream_SEMIC=new RewriteRuleTokenStream(adaptor,"token SEMIC");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_expressionNoIn=new RewriteRuleSubtreeStream(adaptor,"rule expressionNoIn");

        	Object[] isLhs = new Object[1];

        try { dbg.enterRule(getGrammarFileName(), "forControlExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1403, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1408:2: (ex1= expressionNoIn ({...}? ( IN ex2= expression -> ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) ) ) | ( SEMIC (ex2= expression )? SEMIC (ex3= expression )? -> ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) ) ) ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1408:4: ex1= expressionNoIn ({...}? ( IN ex2= expression -> ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) ) ) | ( SEMIC (ex2= expression )? SEMIC (ex3= expression )? -> ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) ) ) )
            {
            dbg.location(1408,7);
            pushFollow(FOLLOW_expressionNoIn_in_forControlExpression5129);
            ex1=expressionNoIn();

            state._fsp--;

            stream_expressionNoIn.add(ex1.getTree());
            dbg.location(1409,2);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1409:2: ({...}? ( IN ex2= expression -> ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) ) ) | ( SEMIC (ex2= expression )? SEMIC (ex3= expression )? -> ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) ) ) )
            int alt58=2;
            try { dbg.enterSubRule(58);
            try { dbg.enterDecision(58, decisionCanBacktrack[58]);

            int LA58_0 = input.LA(1);

            if ( (LA58_0==IN) ) {
                alt58=1;
            }
            else if ( (LA58_0==SEMIC) ) {
                alt58=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 58, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(58);}

            switch (alt58) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1410:3: {...}? ( IN ex2= expression -> ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) ) )
                    {
                    dbg.location(1410,3);
                    if ( !(evalPredicate( isLeftHandSideIn(ex1, isLhs) ," isLeftHandSideIn(ex1, isLhs) ")) ) {
                        throw new FailedPredicateException(input, "forControlExpression", " isLeftHandSideIn(ex1, isLhs) ");
                    }
                    dbg.location(1410,37);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1410:37: ( IN ex2= expression -> ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) ) )
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1411:4: IN ex2= expression
                    {
                    dbg.location(1411,4);
                    IN217=(Token)match(input,IN,FOLLOW_IN_in_forControlExpression5144);  
                    stream_IN.add(IN217);

                    dbg.location(1411,10);
                    pushFollow(FOLLOW_expression_in_forControlExpression5148);
                    ex2=expression();

                    state._fsp--;

                    stream_expression.add(ex2.getTree());


                    // AST REWRITE
                    // elements: ex2, ex1
                    // token labels: 
                    // rule labels: retval, ex2, ex1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_ex2=new RewriteRuleSubtreeStream(adaptor,"rule ex2",ex2!=null?ex2.tree:null);
                    RewriteRuleSubtreeStream stream_ex1=new RewriteRuleSubtreeStream(adaptor,"rule ex1",ex1!=null?ex1.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 1412:4: -> ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) )
                    {
                        dbg.location(1412,7);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1412:7: ^( FORITER ^( EXPR $ex1) ^( EXPR $ex2) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(1412,10);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FORITER, "FORITER"), root_1);

                        dbg.location(1412,18);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1412:18: ^( EXPR $ex1)
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1412,21);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1412,26);
                        adaptor.addChild(root_2, stream_ex1.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(1412,33);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1412:33: ^( EXPR $ex2)
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1412,36);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1412,41);
                        adaptor.addChild(root_2, stream_ex2.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1415:3: ( SEMIC (ex2= expression )? SEMIC (ex3= expression )? -> ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) ) )
                    {
                    dbg.location(1415,3);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1415:3: ( SEMIC (ex2= expression )? SEMIC (ex3= expression )? -> ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) ) )
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1416:4: SEMIC (ex2= expression )? SEMIC (ex3= expression )?
                    {
                    dbg.location(1416,4);
                    SEMIC218=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_forControlExpression5194);  
                    stream_SEMIC.add(SEMIC218);

                    dbg.location(1416,13);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1416:13: (ex2= expression )?
                    int alt56=2;
                    try { dbg.enterSubRule(56);
                    try { dbg.enterDecision(56, decisionCanBacktrack[56]);

                    int LA56_0 = input.LA(1);

                    if ( ((LA56_0>=NULL && LA56_0<=FALSE)||LA56_0==DELETE||LA56_0==FUNCTION||LA56_0==NEW||LA56_0==THIS||LA56_0==TYPEOF||LA56_0==VOID||LA56_0==LBRACE||LA56_0==LPAREN||LA56_0==LBRACK||(LA56_0>=ADD && LA56_0<=SUB)||(LA56_0>=INC && LA56_0<=DEC)||(LA56_0>=NOT && LA56_0<=INV)||(LA56_0>=Identifier && LA56_0<=StringLiteral)||LA56_0==RegularExpressionLiteral||(LA56_0>=DecimalLiteral && LA56_0<=HexIntegerLiteral)) ) {
                        alt56=1;
                    }
                    } finally {dbg.exitDecision(56);}

                    switch (alt56) {
                        case 1 :
                            dbg.enterAlt(1);

                            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1416:13: ex2= expression
                            {
                            dbg.location(1416,13);
                            pushFollow(FOLLOW_expression_in_forControlExpression5198);
                            ex2=expression();

                            state._fsp--;

                            stream_expression.add(ex2.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(56);}

                    dbg.location(1416,26);
                    SEMIC219=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_forControlExpression5201);  
                    stream_SEMIC.add(SEMIC219);

                    dbg.location(1416,35);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1416:35: (ex3= expression )?
                    int alt57=2;
                    try { dbg.enterSubRule(57);
                    try { dbg.enterDecision(57, decisionCanBacktrack[57]);

                    int LA57_0 = input.LA(1);

                    if ( ((LA57_0>=NULL && LA57_0<=FALSE)||LA57_0==DELETE||LA57_0==FUNCTION||LA57_0==NEW||LA57_0==THIS||LA57_0==TYPEOF||LA57_0==VOID||LA57_0==LBRACE||LA57_0==LPAREN||LA57_0==LBRACK||(LA57_0>=ADD && LA57_0<=SUB)||(LA57_0>=INC && LA57_0<=DEC)||(LA57_0>=NOT && LA57_0<=INV)||(LA57_0>=Identifier && LA57_0<=StringLiteral)||LA57_0==RegularExpressionLiteral||(LA57_0>=DecimalLiteral && LA57_0<=HexIntegerLiteral)) ) {
                        alt57=1;
                    }
                    } finally {dbg.exitDecision(57);}

                    switch (alt57) {
                        case 1 :
                            dbg.enterAlt(1);

                            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1416:35: ex3= expression
                            {
                            dbg.location(1416,35);
                            pushFollow(FOLLOW_expression_in_forControlExpression5205);
                            ex3=expression();

                            state._fsp--;

                            stream_expression.add(ex3.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(57);}



                    // AST REWRITE
                    // elements: ex2, ex1, ex3
                    // token labels: 
                    // rule labels: retval, ex3, ex2, ex1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_ex3=new RewriteRuleSubtreeStream(adaptor,"rule ex3",ex3!=null?ex3.tree:null);
                    RewriteRuleSubtreeStream stream_ex2=new RewriteRuleSubtreeStream(adaptor,"rule ex2",ex2!=null?ex2.tree:null);
                    RewriteRuleSubtreeStream stream_ex1=new RewriteRuleSubtreeStream(adaptor,"rule ex1",ex1!=null?ex1.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 1417:4: -> ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) )
                    {
                        dbg.location(1417,7);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1417:7: ^( FORSTEP ^( EXPR $ex1) ^( EXPR ( $ex2)? ) ^( EXPR ( $ex3)? ) )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        dbg.location(1417,10);
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FORSTEP, "FORSTEP"), root_1);

                        dbg.location(1417,18);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1417:18: ^( EXPR $ex1)
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1417,21);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1417,26);
                        adaptor.addChild(root_2, stream_ex1.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(1417,33);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1417:33: ^( EXPR ( $ex2)? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1417,36);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1417,41);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1417:41: ( $ex2)?
                        if ( stream_ex2.hasNext() ) {
                            dbg.location(1417,41);
                            adaptor.addChild(root_2, stream_ex2.nextTree());

                        }
                        stream_ex2.reset();

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(1417,49);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1417:49: ^( EXPR ( $ex3)? )
                        {
                        Object root_2 = (Object)adaptor.nil();
                        dbg.location(1417,52);
                        root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                        dbg.location(1417,57);
                        // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1417:57: ( $ex3)?
                        if ( stream_ex3.hasNext() ) {
                            dbg.location(1417,57);
                            adaptor.addChild(root_2, stream_ex3.nextTree());

                        }
                        stream_ex3.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }


                    }
                    break;

            }
            } finally {dbg.exitSubRule(58);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1420, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "forControlExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "forControlExpression"

    public static class forControlSemic_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "forControlSemic"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1422:1: forControlSemic : SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( EXPR ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) ;
    public final ES3Parser.forControlSemic_return forControlSemic() throws RecognitionException {
        ES3Parser.forControlSemic_return retval = new ES3Parser.forControlSemic_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SEMIC220=null;
        Token SEMIC221=null;
        ES3Parser.expression_return ex1 = null;

        ES3Parser.expression_return ex2 = null;


        Object SEMIC220_tree=null;
        Object SEMIC221_tree=null;
        RewriteRuleTokenStream stream_SEMIC=new RewriteRuleTokenStream(adaptor,"token SEMIC");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        try { dbg.enterRule(getGrammarFileName(), "forControlSemic");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1422, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1423:2: ( SEMIC (ex1= expression )? SEMIC (ex2= expression )? -> ^( FORSTEP ^( EXPR ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1423:4: SEMIC (ex1= expression )? SEMIC (ex2= expression )?
            {
            dbg.location(1423,4);
            SEMIC220=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_forControlSemic5264);  
            stream_SEMIC.add(SEMIC220);

            dbg.location(1423,13);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1423:13: (ex1= expression )?
            int alt59=2;
            try { dbg.enterSubRule(59);
            try { dbg.enterDecision(59, decisionCanBacktrack[59]);

            int LA59_0 = input.LA(1);

            if ( ((LA59_0>=NULL && LA59_0<=FALSE)||LA59_0==DELETE||LA59_0==FUNCTION||LA59_0==NEW||LA59_0==THIS||LA59_0==TYPEOF||LA59_0==VOID||LA59_0==LBRACE||LA59_0==LPAREN||LA59_0==LBRACK||(LA59_0>=ADD && LA59_0<=SUB)||(LA59_0>=INC && LA59_0<=DEC)||(LA59_0>=NOT && LA59_0<=INV)||(LA59_0>=Identifier && LA59_0<=StringLiteral)||LA59_0==RegularExpressionLiteral||(LA59_0>=DecimalLiteral && LA59_0<=HexIntegerLiteral)) ) {
                alt59=1;
            }
            } finally {dbg.exitDecision(59);}

            switch (alt59) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1423:13: ex1= expression
                    {
                    dbg.location(1423,13);
                    pushFollow(FOLLOW_expression_in_forControlSemic5268);
                    ex1=expression();

                    state._fsp--;

                    stream_expression.add(ex1.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(59);}

            dbg.location(1423,26);
            SEMIC221=(Token)match(input,SEMIC,FOLLOW_SEMIC_in_forControlSemic5271);  
            stream_SEMIC.add(SEMIC221);

            dbg.location(1423,35);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1423:35: (ex2= expression )?
            int alt60=2;
            try { dbg.enterSubRule(60);
            try { dbg.enterDecision(60, decisionCanBacktrack[60]);

            int LA60_0 = input.LA(1);

            if ( ((LA60_0>=NULL && LA60_0<=FALSE)||LA60_0==DELETE||LA60_0==FUNCTION||LA60_0==NEW||LA60_0==THIS||LA60_0==TYPEOF||LA60_0==VOID||LA60_0==LBRACE||LA60_0==LPAREN||LA60_0==LBRACK||(LA60_0>=ADD && LA60_0<=SUB)||(LA60_0>=INC && LA60_0<=DEC)||(LA60_0>=NOT && LA60_0<=INV)||(LA60_0>=Identifier && LA60_0<=StringLiteral)||LA60_0==RegularExpressionLiteral||(LA60_0>=DecimalLiteral && LA60_0<=HexIntegerLiteral)) ) {
                alt60=1;
            }
            } finally {dbg.exitDecision(60);}

            switch (alt60) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1423:35: ex2= expression
                    {
                    dbg.location(1423,35);
                    pushFollow(FOLLOW_expression_in_forControlSemic5275);
                    ex2=expression();

                    state._fsp--;

                    stream_expression.add(ex2.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(60);}



            // AST REWRITE
            // elements: ex2, ex1
            // token labels: 
            // rule labels: retval, ex2, ex1
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_ex2=new RewriteRuleSubtreeStream(adaptor,"rule ex2",ex2!=null?ex2.tree:null);
            RewriteRuleSubtreeStream stream_ex1=new RewriteRuleSubtreeStream(adaptor,"rule ex1",ex1!=null?ex1.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1424:2: -> ^( FORSTEP ^( EXPR ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) )
            {
                dbg.location(1424,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1424:5: ^( FORSTEP ^( EXPR ) ^( EXPR ( $ex1)? ) ^( EXPR ( $ex2)? ) )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1424,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FORSTEP, "FORSTEP"), root_1);

                dbg.location(1424,16);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1424:16: ^( EXPR )
                {
                Object root_2 = (Object)adaptor.nil();
                dbg.location(1424,19);
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                adaptor.addChild(root_1, root_2);
                }
                dbg.location(1424,26);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1424:26: ^( EXPR ( $ex1)? )
                {
                Object root_2 = (Object)adaptor.nil();
                dbg.location(1424,29);
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                dbg.location(1424,34);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1424:34: ( $ex1)?
                if ( stream_ex1.hasNext() ) {
                    dbg.location(1424,34);
                    adaptor.addChild(root_2, stream_ex1.nextTree());

                }
                stream_ex1.reset();

                adaptor.addChild(root_1, root_2);
                }
                dbg.location(1424,42);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1424:42: ^( EXPR ( $ex2)? )
                {
                Object root_2 = (Object)adaptor.nil();
                dbg.location(1424,45);
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EXPR, "EXPR"), root_2);

                dbg.location(1424,50);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1424:50: ( $ex2)?
                if ( stream_ex2.hasNext() ) {
                    dbg.location(1424,50);
                    adaptor.addChild(root_2, stream_ex2.nextTree());

                }
                stream_ex2.reset();

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1425, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "forControlSemic");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "forControlSemic"

    public static class continueStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "continueStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1436:1: continueStatement : CONTINUE ( Identifier )? semic ;
    public final ES3Parser.continueStatement_return continueStatement() throws RecognitionException {
        ES3Parser.continueStatement_return retval = new ES3Parser.continueStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token CONTINUE222=null;
        Token Identifier223=null;
        ES3Parser.semic_return semic224 = null;


        Object CONTINUE222_tree=null;
        Object Identifier223_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "continueStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1436, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1437:2: ( CONTINUE ( Identifier )? semic )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1437:4: CONTINUE ( Identifier )? semic
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1437,12);
            CONTINUE222=(Token)match(input,CONTINUE,FOLLOW_CONTINUE_in_continueStatement5329); 
            CONTINUE222_tree = (Object)adaptor.create(CONTINUE222);
            root_0 = (Object)adaptor.becomeRoot(CONTINUE222_tree, root_0);

            dbg.location(1437,14);
             if (input.LA(1) == Identifier) promoteEOL(null); 
            dbg.location(1437,67);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1437:67: ( Identifier )?
            int alt61=2;
            try { dbg.enterSubRule(61);
            try { dbg.enterDecision(61, decisionCanBacktrack[61]);

            int LA61_0 = input.LA(1);

            if ( (LA61_0==Identifier) ) {
                alt61=1;
            }
            } finally {dbg.exitDecision(61);}

            switch (alt61) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1437:67: Identifier
                    {
                    dbg.location(1437,67);
                    Identifier223=(Token)match(input,Identifier,FOLLOW_Identifier_in_continueStatement5334); 
                    Identifier223_tree = (Object)adaptor.create(Identifier223);
                    adaptor.addChild(root_0, Identifier223_tree);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(61);}

            dbg.location(1437,84);
            pushFollow(FOLLOW_semic_in_continueStatement5337);
            semic224=semic();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1438, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "continueStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "continueStatement"

    public static class breakStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "breakStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1449:1: breakStatement : BREAK ( Identifier )? semic ;
    public final ES3Parser.breakStatement_return breakStatement() throws RecognitionException {
        ES3Parser.breakStatement_return retval = new ES3Parser.breakStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token BREAK225=null;
        Token Identifier226=null;
        ES3Parser.semic_return semic227 = null;


        Object BREAK225_tree=null;
        Object Identifier226_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "breakStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1449, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1450:2: ( BREAK ( Identifier )? semic )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1450:4: BREAK ( Identifier )? semic
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1450,9);
            BREAK225=(Token)match(input,BREAK,FOLLOW_BREAK_in_breakStatement5356); 
            BREAK225_tree = (Object)adaptor.create(BREAK225);
            root_0 = (Object)adaptor.becomeRoot(BREAK225_tree, root_0);

            dbg.location(1450,11);
             if (input.LA(1) == Identifier) promoteEOL(null); 
            dbg.location(1450,64);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1450:64: ( Identifier )?
            int alt62=2;
            try { dbg.enterSubRule(62);
            try { dbg.enterDecision(62, decisionCanBacktrack[62]);

            int LA62_0 = input.LA(1);

            if ( (LA62_0==Identifier) ) {
                alt62=1;
            }
            } finally {dbg.exitDecision(62);}

            switch (alt62) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1450:64: Identifier
                    {
                    dbg.location(1450,64);
                    Identifier226=(Token)match(input,Identifier,FOLLOW_Identifier_in_breakStatement5361); 
                    Identifier226_tree = (Object)adaptor.create(Identifier226);
                    adaptor.addChild(root_0, Identifier226_tree);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(62);}

            dbg.location(1450,81);
            pushFollow(FOLLOW_semic_in_breakStatement5364);
            semic227=semic();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1451, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "breakStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "breakStatement"

    public static class returnStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "returnStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1470:1: returnStatement : RETURN ( expression )? semic ;
    public final ES3Parser.returnStatement_return returnStatement() throws RecognitionException {
        ES3Parser.returnStatement_return retval = new ES3Parser.returnStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token RETURN228=null;
        ES3Parser.expression_return expression229 = null;

        ES3Parser.semic_return semic230 = null;


        Object RETURN228_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "returnStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1470, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1471:2: ( RETURN ( expression )? semic )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1471:4: RETURN ( expression )? semic
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1471,10);
            RETURN228=(Token)match(input,RETURN,FOLLOW_RETURN_in_returnStatement5383); 
            RETURN228_tree = (Object)adaptor.create(RETURN228);
            root_0 = (Object)adaptor.becomeRoot(RETURN228_tree, root_0);

            dbg.location(1471,12);
             promoteEOL(null); 
            dbg.location(1471,34);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1471:34: ( expression )?
            int alt63=2;
            try { dbg.enterSubRule(63);
            try { dbg.enterDecision(63, decisionCanBacktrack[63]);

            int LA63_0 = input.LA(1);

            if ( ((LA63_0>=NULL && LA63_0<=FALSE)||LA63_0==DELETE||LA63_0==FUNCTION||LA63_0==NEW||LA63_0==THIS||LA63_0==TYPEOF||LA63_0==VOID||LA63_0==LBRACE||LA63_0==LPAREN||LA63_0==LBRACK||(LA63_0>=ADD && LA63_0<=SUB)||(LA63_0>=INC && LA63_0<=DEC)||(LA63_0>=NOT && LA63_0<=INV)||(LA63_0>=Identifier && LA63_0<=StringLiteral)||LA63_0==RegularExpressionLiteral||(LA63_0>=DecimalLiteral && LA63_0<=HexIntegerLiteral)) ) {
                alt63=1;
            }
            } finally {dbg.exitDecision(63);}

            switch (alt63) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1471:34: expression
                    {
                    dbg.location(1471,34);
                    pushFollow(FOLLOW_expression_in_returnStatement5388);
                    expression229=expression();

                    state._fsp--;

                    adaptor.addChild(root_0, expression229.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(63);}

            dbg.location(1471,51);
            pushFollow(FOLLOW_semic_in_returnStatement5391);
            semic230=semic();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1472, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "returnStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "returnStatement"

    public static class withStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "withStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1478:1: withStatement : WITH LPAREN expression RPAREN statement ;
    public final ES3Parser.withStatement_return withStatement() throws RecognitionException {
        ES3Parser.withStatement_return retval = new ES3Parser.withStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WITH231=null;
        Token LPAREN232=null;
        Token RPAREN234=null;
        ES3Parser.expression_return expression233 = null;

        ES3Parser.statement_return statement235 = null;


        Object WITH231_tree=null;
        Object LPAREN232_tree=null;
        Object RPAREN234_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "withStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1478, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1479:2: ( WITH LPAREN expression RPAREN statement )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1479:4: WITH LPAREN expression RPAREN statement
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1479,8);
            WITH231=(Token)match(input,WITH,FOLLOW_WITH_in_withStatement5408); 
            WITH231_tree = (Object)adaptor.create(WITH231);
            root_0 = (Object)adaptor.becomeRoot(WITH231_tree, root_0);

            dbg.location(1479,16);
            LPAREN232=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_withStatement5411); 
            dbg.location(1479,18);
            pushFollow(FOLLOW_expression_in_withStatement5414);
            expression233=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression233.getTree());
            dbg.location(1479,35);
            RPAREN234=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_withStatement5416); 
            dbg.location(1479,37);
            pushFollow(FOLLOW_statement_in_withStatement5419);
            statement235=statement();

            state._fsp--;

            adaptor.addChild(root_0, statement235.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1480, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "withStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "withStatement"

    public static class switchStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "switchStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1486:1: switchStatement : SWITCH LPAREN expression RPAREN LBRACE ({...}? => defaultClause | caseClause )* RBRACE -> ^( SWITCH expression ( defaultClause )? ( caseClause )* ) ;
    public final ES3Parser.switchStatement_return switchStatement() throws RecognitionException {
        ES3Parser.switchStatement_return retval = new ES3Parser.switchStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SWITCH236=null;
        Token LPAREN237=null;
        Token RPAREN239=null;
        Token LBRACE240=null;
        Token RBRACE243=null;
        ES3Parser.expression_return expression238 = null;

        ES3Parser.defaultClause_return defaultClause241 = null;

        ES3Parser.caseClause_return caseClause242 = null;


        Object SWITCH236_tree=null;
        Object LPAREN237_tree=null;
        Object RPAREN239_tree=null;
        Object LBRACE240_tree=null;
        Object RBRACE243_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_SWITCH=new RewriteRuleTokenStream(adaptor,"token SWITCH");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_caseClause=new RewriteRuleSubtreeStream(adaptor,"rule caseClause");
        RewriteRuleSubtreeStream stream_defaultClause=new RewriteRuleSubtreeStream(adaptor,"rule defaultClause");

        	int defaultClauseCount = 0;

        try { dbg.enterRule(getGrammarFileName(), "switchStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1486, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1491:2: ( SWITCH LPAREN expression RPAREN LBRACE ({...}? => defaultClause | caseClause )* RBRACE -> ^( SWITCH expression ( defaultClause )? ( caseClause )* ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1491:4: SWITCH LPAREN expression RPAREN LBRACE ({...}? => defaultClause | caseClause )* RBRACE
            {
            dbg.location(1491,4);
            SWITCH236=(Token)match(input,SWITCH,FOLLOW_SWITCH_in_switchStatement5440);  
            stream_SWITCH.add(SWITCH236);

            dbg.location(1491,11);
            LPAREN237=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_switchStatement5442);  
            stream_LPAREN.add(LPAREN237);

            dbg.location(1491,18);
            pushFollow(FOLLOW_expression_in_switchStatement5444);
            expression238=expression();

            state._fsp--;

            stream_expression.add(expression238.getTree());
            dbg.location(1491,29);
            RPAREN239=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_switchStatement5446);  
            stream_RPAREN.add(RPAREN239);

            dbg.location(1491,36);
            LBRACE240=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_switchStatement5448);  
            stream_LBRACE.add(LBRACE240);

            dbg.location(1491,43);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1491:43: ({...}? => defaultClause | caseClause )*
            try { dbg.enterSubRule(64);

            loop64:
            do {
                int alt64=3;
                try { dbg.enterDecision(64, decisionCanBacktrack[64]);

                int LA64_0 = input.LA(1);

                if ( (LA64_0==DEFAULT) && (evalPredicate( defaultClauseCount == 0 ," defaultClauseCount == 0 "))) {
                    alt64=1;
                }
                else if ( (LA64_0==CASE) ) {
                    alt64=2;
                }


                } finally {dbg.exitDecision(64);}

                switch (alt64) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1491:45: {...}? => defaultClause
            	    {
            	    dbg.location(1491,45);
            	    if ( !(evalPredicate( defaultClauseCount == 0 ," defaultClauseCount == 0 ")) ) {
            	        throw new FailedPredicateException(input, "switchStatement", " defaultClauseCount == 0 ");
            	    }
            	    dbg.location(1491,76);
            	    pushFollow(FOLLOW_defaultClause_in_switchStatement5455);
            	    defaultClause241=defaultClause();

            	    state._fsp--;

            	    stream_defaultClause.add(defaultClause241.getTree());
            	    dbg.location(1491,90);
            	     defaultClauseCount++; 

            	    }
            	    break;
            	case 2 :
            	    dbg.enterAlt(2);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1491:118: caseClause
            	    {
            	    dbg.location(1491,118);
            	    pushFollow(FOLLOW_caseClause_in_switchStatement5461);
            	    caseClause242=caseClause();

            	    state._fsp--;

            	    stream_caseClause.add(caseClause242.getTree());

            	    }
            	    break;

            	default :
            	    break loop64;
                }
            } while (true);
            } finally {dbg.exitSubRule(64);}

            dbg.location(1491,132);
            RBRACE243=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_switchStatement5466);  
            stream_RBRACE.add(RBRACE243);



            // AST REWRITE
            // elements: caseClause, SWITCH, defaultClause, expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1492:2: -> ^( SWITCH expression ( defaultClause )? ( caseClause )* )
            {
                dbg.location(1492,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1492:5: ^( SWITCH expression ( defaultClause )? ( caseClause )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1492,8);
                root_1 = (Object)adaptor.becomeRoot(stream_SWITCH.nextNode(), root_1);

                dbg.location(1492,15);
                adaptor.addChild(root_1, stream_expression.nextTree());
                dbg.location(1492,26);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1492:26: ( defaultClause )?
                if ( stream_defaultClause.hasNext() ) {
                    dbg.location(1492,26);
                    adaptor.addChild(root_1, stream_defaultClause.nextTree());

                }
                stream_defaultClause.reset();
                dbg.location(1492,41);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1492:41: ( caseClause )*
                while ( stream_caseClause.hasNext() ) {
                    dbg.location(1492,41);
                    adaptor.addChild(root_1, stream_caseClause.nextTree());

                }
                stream_caseClause.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1493, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "switchStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "switchStatement"

    public static class caseClause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "caseClause"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1495:1: caseClause : CASE expression COLON ( statement )* ;
    public final ES3Parser.caseClause_return caseClause() throws RecognitionException {
        ES3Parser.caseClause_return retval = new ES3Parser.caseClause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token CASE244=null;
        Token COLON246=null;
        ES3Parser.expression_return expression245 = null;

        ES3Parser.statement_return statement247 = null;


        Object CASE244_tree=null;
        Object COLON246_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "caseClause");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1495, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1496:2: ( CASE expression COLON ( statement )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1496:4: CASE expression COLON ( statement )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1496,8);
            CASE244=(Token)match(input,CASE,FOLLOW_CASE_in_caseClause5494); 
            CASE244_tree = (Object)adaptor.create(CASE244);
            root_0 = (Object)adaptor.becomeRoot(CASE244_tree, root_0);

            dbg.location(1496,10);
            pushFollow(FOLLOW_expression_in_caseClause5497);
            expression245=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression245.getTree());
            dbg.location(1496,26);
            COLON246=(Token)match(input,COLON,FOLLOW_COLON_in_caseClause5499); 
            dbg.location(1496,28);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1496:28: ( statement )*
            try { dbg.enterSubRule(65);

            loop65:
            do {
                int alt65=2;
                try { dbg.enterDecision(65, decisionCanBacktrack[65]);

                int LA65_0 = input.LA(1);

                if ( ((LA65_0>=NULL && LA65_0<=BREAK)||LA65_0==CONTINUE||(LA65_0>=DELETE && LA65_0<=DO)||(LA65_0>=FOR && LA65_0<=IF)||(LA65_0>=NEW && LA65_0<=WITH)||LA65_0==LBRACE||LA65_0==LPAREN||LA65_0==LBRACK||LA65_0==SEMIC||(LA65_0>=ADD && LA65_0<=SUB)||(LA65_0>=INC && LA65_0<=DEC)||(LA65_0>=NOT && LA65_0<=INV)||(LA65_0>=Identifier && LA65_0<=StringLiteral)||LA65_0==RegularExpressionLiteral||(LA65_0>=DecimalLiteral && LA65_0<=HexIntegerLiteral)) ) {
                    alt65=1;
                }


                } finally {dbg.exitDecision(65);}

                switch (alt65) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1496:28: statement
            	    {
            	    dbg.location(1496,28);
            	    pushFollow(FOLLOW_statement_in_caseClause5502);
            	    statement247=statement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, statement247.getTree());

            	    }
            	    break;

            	default :
            	    break loop65;
                }
            } while (true);
            } finally {dbg.exitSubRule(65);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1497, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "caseClause");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "caseClause"

    public static class defaultClause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "defaultClause"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1499:1: defaultClause : DEFAULT COLON ( statement )* ;
    public final ES3Parser.defaultClause_return defaultClause() throws RecognitionException {
        ES3Parser.defaultClause_return retval = new ES3Parser.defaultClause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DEFAULT248=null;
        Token COLON249=null;
        ES3Parser.statement_return statement250 = null;


        Object DEFAULT248_tree=null;
        Object COLON249_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "defaultClause");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1499, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1500:2: ( DEFAULT COLON ( statement )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1500:4: DEFAULT COLON ( statement )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1500,11);
            DEFAULT248=(Token)match(input,DEFAULT,FOLLOW_DEFAULT_in_defaultClause5515); 
            DEFAULT248_tree = (Object)adaptor.create(DEFAULT248);
            root_0 = (Object)adaptor.becomeRoot(DEFAULT248_tree, root_0);

            dbg.location(1500,18);
            COLON249=(Token)match(input,COLON,FOLLOW_COLON_in_defaultClause5518); 
            dbg.location(1500,20);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1500:20: ( statement )*
            try { dbg.enterSubRule(66);

            loop66:
            do {
                int alt66=2;
                try { dbg.enterDecision(66, decisionCanBacktrack[66]);

                int LA66_0 = input.LA(1);

                if ( ((LA66_0>=NULL && LA66_0<=BREAK)||LA66_0==CONTINUE||(LA66_0>=DELETE && LA66_0<=DO)||(LA66_0>=FOR && LA66_0<=IF)||(LA66_0>=NEW && LA66_0<=WITH)||LA66_0==LBRACE||LA66_0==LPAREN||LA66_0==LBRACK||LA66_0==SEMIC||(LA66_0>=ADD && LA66_0<=SUB)||(LA66_0>=INC && LA66_0<=DEC)||(LA66_0>=NOT && LA66_0<=INV)||(LA66_0>=Identifier && LA66_0<=StringLiteral)||LA66_0==RegularExpressionLiteral||(LA66_0>=DecimalLiteral && LA66_0<=HexIntegerLiteral)) ) {
                    alt66=1;
                }


                } finally {dbg.exitDecision(66);}

                switch (alt66) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1500:20: statement
            	    {
            	    dbg.location(1500,20);
            	    pushFollow(FOLLOW_statement_in_defaultClause5521);
            	    statement250=statement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, statement250.getTree());

            	    }
            	    break;

            	default :
            	    break loop66;
                }
            } while (true);
            } finally {dbg.exitSubRule(66);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1501, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "defaultClause");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "defaultClause"

    public static class labelledStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "labelledStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1507:1: labelledStatement : Identifier COLON statement -> ^( LABELLED Identifier statement ) ;
    public final ES3Parser.labelledStatement_return labelledStatement() throws RecognitionException {
        ES3Parser.labelledStatement_return retval = new ES3Parser.labelledStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token Identifier251=null;
        Token COLON252=null;
        ES3Parser.statement_return statement253 = null;


        Object Identifier251_tree=null;
        Object COLON252_tree=null;
        RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
        RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");
        RewriteRuleSubtreeStream stream_statement=new RewriteRuleSubtreeStream(adaptor,"rule statement");
        try { dbg.enterRule(getGrammarFileName(), "labelledStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1507, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1508:2: ( Identifier COLON statement -> ^( LABELLED Identifier statement ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1508:4: Identifier COLON statement
            {
            dbg.location(1508,4);
            Identifier251=(Token)match(input,Identifier,FOLLOW_Identifier_in_labelledStatement5538);  
            stream_Identifier.add(Identifier251);

            dbg.location(1508,15);
            COLON252=(Token)match(input,COLON,FOLLOW_COLON_in_labelledStatement5540);  
            stream_COLON.add(COLON252);

            dbg.location(1508,21);
            pushFollow(FOLLOW_statement_in_labelledStatement5542);
            statement253=statement();

            state._fsp--;

            stream_statement.add(statement253.getTree());


            // AST REWRITE
            // elements: statement, Identifier
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1509:2: -> ^( LABELLED Identifier statement )
            {
                dbg.location(1509,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1509:5: ^( LABELLED Identifier statement )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1509,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LABELLED, "LABELLED"), root_1);

                dbg.location(1509,17);
                adaptor.addChild(root_1, stream_Identifier.nextNode());
                dbg.location(1509,28);
                adaptor.addChild(root_1, stream_statement.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1510, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "labelledStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "labelledStatement"

    public static class throwStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "throwStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1531:1: throwStatement : THROW expression semic ;
    public final ES3Parser.throwStatement_return throwStatement() throws RecognitionException {
        ES3Parser.throwStatement_return retval = new ES3Parser.throwStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token THROW254=null;
        ES3Parser.expression_return expression255 = null;

        ES3Parser.semic_return semic256 = null;


        Object THROW254_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "throwStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1531, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1532:2: ( THROW expression semic )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1532:4: THROW expression semic
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1532,9);
            THROW254=(Token)match(input,THROW,FOLLOW_THROW_in_throwStatement5573); 
            THROW254_tree = (Object)adaptor.create(THROW254);
            root_0 = (Object)adaptor.becomeRoot(THROW254_tree, root_0);

            dbg.location(1532,11);
             promoteEOL(null); 
            dbg.location(1532,33);
            pushFollow(FOLLOW_expression_in_throwStatement5578);
            expression255=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression255.getTree());
            dbg.location(1532,49);
            pushFollow(FOLLOW_semic_in_throwStatement5580);
            semic256=semic();

            state._fsp--;


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1533, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "throwStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "throwStatement"

    public static class tryStatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "tryStatement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1539:1: tryStatement : TRY block ( catchClause ( finallyClause )? | finallyClause ) ;
    public final ES3Parser.tryStatement_return tryStatement() throws RecognitionException {
        ES3Parser.tryStatement_return retval = new ES3Parser.tryStatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token TRY257=null;
        ES3Parser.block_return block258 = null;

        ES3Parser.catchClause_return catchClause259 = null;

        ES3Parser.finallyClause_return finallyClause260 = null;

        ES3Parser.finallyClause_return finallyClause261 = null;


        Object TRY257_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "tryStatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1539, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1540:2: ( TRY block ( catchClause ( finallyClause )? | finallyClause ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1540:4: TRY block ( catchClause ( finallyClause )? | finallyClause )
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1540,7);
            TRY257=(Token)match(input,TRY,FOLLOW_TRY_in_tryStatement5597); 
            TRY257_tree = (Object)adaptor.create(TRY257);
            root_0 = (Object)adaptor.becomeRoot(TRY257_tree, root_0);

            dbg.location(1540,9);
            pushFollow(FOLLOW_block_in_tryStatement5600);
            block258=block();

            state._fsp--;

            adaptor.addChild(root_0, block258.getTree());
            dbg.location(1540,15);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1540:15: ( catchClause ( finallyClause )? | finallyClause )
            int alt68=2;
            try { dbg.enterSubRule(68);
            try { dbg.enterDecision(68, decisionCanBacktrack[68]);

            int LA68_0 = input.LA(1);

            if ( (LA68_0==CATCH) ) {
                alt68=1;
            }
            else if ( (LA68_0==FINALLY) ) {
                alt68=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(68);}

            switch (alt68) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1540:17: catchClause ( finallyClause )?
                    {
                    dbg.location(1540,17);
                    pushFollow(FOLLOW_catchClause_in_tryStatement5604);
                    catchClause259=catchClause();

                    state._fsp--;

                    adaptor.addChild(root_0, catchClause259.getTree());
                    dbg.location(1540,29);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1540:29: ( finallyClause )?
                    int alt67=2;
                    try { dbg.enterSubRule(67);
                    try { dbg.enterDecision(67, decisionCanBacktrack[67]);

                    int LA67_0 = input.LA(1);

                    if ( (LA67_0==FINALLY) ) {
                        alt67=1;
                    }
                    } finally {dbg.exitDecision(67);}

                    switch (alt67) {
                        case 1 :
                            dbg.enterAlt(1);

                            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1540:29: finallyClause
                            {
                            dbg.location(1540,29);
                            pushFollow(FOLLOW_finallyClause_in_tryStatement5606);
                            finallyClause260=finallyClause();

                            state._fsp--;

                            adaptor.addChild(root_0, finallyClause260.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(67);}


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1540:46: finallyClause
                    {
                    dbg.location(1540,46);
                    pushFollow(FOLLOW_finallyClause_in_tryStatement5611);
                    finallyClause261=finallyClause();

                    state._fsp--;

                    adaptor.addChild(root_0, finallyClause261.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(68);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1541, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "tryStatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "tryStatement"

    public static class catchClause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "catchClause"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1543:1: catchClause : CATCH LPAREN Identifier RPAREN block ;
    public final ES3Parser.catchClause_return catchClause() throws RecognitionException {
        ES3Parser.catchClause_return retval = new ES3Parser.catchClause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token CATCH262=null;
        Token LPAREN263=null;
        Token Identifier264=null;
        Token RPAREN265=null;
        ES3Parser.block_return block266 = null;


        Object CATCH262_tree=null;
        Object LPAREN263_tree=null;
        Object Identifier264_tree=null;
        Object RPAREN265_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "catchClause");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1543, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1544:2: ( CATCH LPAREN Identifier RPAREN block )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1544:4: CATCH LPAREN Identifier RPAREN block
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1544,9);
            CATCH262=(Token)match(input,CATCH,FOLLOW_CATCH_in_catchClause5625); 
            CATCH262_tree = (Object)adaptor.create(CATCH262);
            root_0 = (Object)adaptor.becomeRoot(CATCH262_tree, root_0);

            dbg.location(1544,17);
            LPAREN263=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_catchClause5628); 
            dbg.location(1544,19);
            Identifier264=(Token)match(input,Identifier,FOLLOW_Identifier_in_catchClause5631); 
            Identifier264_tree = (Object)adaptor.create(Identifier264);
            adaptor.addChild(root_0, Identifier264_tree);

            dbg.location(1544,36);
            RPAREN265=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_catchClause5633); 
            dbg.location(1544,38);
            pushFollow(FOLLOW_block_in_catchClause5636);
            block266=block();

            state._fsp--;

            adaptor.addChild(root_0, block266.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1545, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "catchClause");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "catchClause"

    public static class finallyClause_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "finallyClause"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1547:1: finallyClause : FINALLY block ;
    public final ES3Parser.finallyClause_return finallyClause() throws RecognitionException {
        ES3Parser.finallyClause_return retval = new ES3Parser.finallyClause_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FINALLY267=null;
        ES3Parser.block_return block268 = null;


        Object FINALLY267_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "finallyClause");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1547, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1548:2: ( FINALLY block )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1548:4: FINALLY block
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1548,11);
            FINALLY267=(Token)match(input,FINALLY,FOLLOW_FINALLY_in_finallyClause5648); 
            FINALLY267_tree = (Object)adaptor.create(FINALLY267);
            root_0 = (Object)adaptor.becomeRoot(FINALLY267_tree, root_0);

            dbg.location(1548,13);
            pushFollow(FOLLOW_block_in_finallyClause5651);
            block268=block();

            state._fsp--;

            adaptor.addChild(root_0, block268.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1549, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "finallyClause");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "finallyClause"

    public static class functionDeclaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionDeclaration"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1561:1: functionDeclaration : FUNCTION name= Identifier formalParameterList functionBody -> ^( FUNCTION $name formalParameterList functionBody ) ;
    public final ES3Parser.functionDeclaration_return functionDeclaration() throws RecognitionException {
        ES3Parser.functionDeclaration_return retval = new ES3Parser.functionDeclaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token FUNCTION269=null;
        ES3Parser.formalParameterList_return formalParameterList270 = null;

        ES3Parser.functionBody_return functionBody271 = null;


        Object name_tree=null;
        Object FUNCTION269_tree=null;
        RewriteRuleTokenStream stream_FUNCTION=new RewriteRuleTokenStream(adaptor,"token FUNCTION");
        RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");
        RewriteRuleSubtreeStream stream_functionBody=new RewriteRuleSubtreeStream(adaptor,"rule functionBody");
        RewriteRuleSubtreeStream stream_formalParameterList=new RewriteRuleSubtreeStream(adaptor,"rule formalParameterList");
        try { dbg.enterRule(getGrammarFileName(), "functionDeclaration");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1561, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1562:2: ( FUNCTION name= Identifier formalParameterList functionBody -> ^( FUNCTION $name formalParameterList functionBody ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1562:4: FUNCTION name= Identifier formalParameterList functionBody
            {
            dbg.location(1562,4);
            FUNCTION269=(Token)match(input,FUNCTION,FOLLOW_FUNCTION_in_functionDeclaration5672);  
            stream_FUNCTION.add(FUNCTION269);

            dbg.location(1562,17);
            name=(Token)match(input,Identifier,FOLLOW_Identifier_in_functionDeclaration5676);  
            stream_Identifier.add(name);

            dbg.location(1562,29);
            pushFollow(FOLLOW_formalParameterList_in_functionDeclaration5678);
            formalParameterList270=formalParameterList();

            state._fsp--;

            stream_formalParameterList.add(formalParameterList270.getTree());
            dbg.location(1562,49);
            pushFollow(FOLLOW_functionBody_in_functionDeclaration5680);
            functionBody271=functionBody();

            state._fsp--;

            stream_functionBody.add(functionBody271.getTree());


            // AST REWRITE
            // elements: name, functionBody, formalParameterList, FUNCTION
            // token labels: name
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1563:2: -> ^( FUNCTION $name formalParameterList functionBody )
            {
                dbg.location(1563,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1563:5: ^( FUNCTION $name formalParameterList functionBody )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1563,8);
                root_1 = (Object)adaptor.becomeRoot(stream_FUNCTION.nextNode(), root_1);

                dbg.location(1563,17);
                adaptor.addChild(root_1, stream_name.nextNode());
                dbg.location(1563,23);
                adaptor.addChild(root_1, stream_formalParameterList.nextTree());
                dbg.location(1563,43);
                adaptor.addChild(root_1, stream_functionBody.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1564, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "functionDeclaration");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "functionDeclaration"

    public static class functionExpression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionExpression"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1566:1: functionExpression : FUNCTION (name= Identifier )? formalParameterList functionBody -> ^( FUNCTION ( $name)? formalParameterList functionBody ) ;
    public final ES3Parser.functionExpression_return functionExpression() throws RecognitionException {
        ES3Parser.functionExpression_return retval = new ES3Parser.functionExpression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token name=null;
        Token FUNCTION272=null;
        ES3Parser.formalParameterList_return formalParameterList273 = null;

        ES3Parser.functionBody_return functionBody274 = null;


        Object name_tree=null;
        Object FUNCTION272_tree=null;
        RewriteRuleTokenStream stream_FUNCTION=new RewriteRuleTokenStream(adaptor,"token FUNCTION");
        RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");
        RewriteRuleSubtreeStream stream_functionBody=new RewriteRuleSubtreeStream(adaptor,"rule functionBody");
        RewriteRuleSubtreeStream stream_formalParameterList=new RewriteRuleSubtreeStream(adaptor,"rule formalParameterList");
        try { dbg.enterRule(getGrammarFileName(), "functionExpression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1566, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1567:2: ( FUNCTION (name= Identifier )? formalParameterList functionBody -> ^( FUNCTION ( $name)? formalParameterList functionBody ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1567:4: FUNCTION (name= Identifier )? formalParameterList functionBody
            {
            dbg.location(1567,4);
            FUNCTION272=(Token)match(input,FUNCTION,FOLLOW_FUNCTION_in_functionExpression5707);  
            stream_FUNCTION.add(FUNCTION272);

            dbg.location(1567,17);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1567:17: (name= Identifier )?
            int alt69=2;
            try { dbg.enterSubRule(69);
            try { dbg.enterDecision(69, decisionCanBacktrack[69]);

            int LA69_0 = input.LA(1);

            if ( (LA69_0==Identifier) ) {
                alt69=1;
            }
            } finally {dbg.exitDecision(69);}

            switch (alt69) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1567:17: name= Identifier
                    {
                    dbg.location(1567,17);
                    name=(Token)match(input,Identifier,FOLLOW_Identifier_in_functionExpression5711);  
                    stream_Identifier.add(name);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(69);}

            dbg.location(1567,30);
            pushFollow(FOLLOW_formalParameterList_in_functionExpression5714);
            formalParameterList273=formalParameterList();

            state._fsp--;

            stream_formalParameterList.add(formalParameterList273.getTree());
            dbg.location(1567,50);
            pushFollow(FOLLOW_functionBody_in_functionExpression5716);
            functionBody274=functionBody();

            state._fsp--;

            stream_functionBody.add(functionBody274.getTree());


            // AST REWRITE
            // elements: functionBody, formalParameterList, name, FUNCTION
            // token labels: name
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1568:2: -> ^( FUNCTION ( $name)? formalParameterList functionBody )
            {
                dbg.location(1568,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1568:5: ^( FUNCTION ( $name)? formalParameterList functionBody )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1568,8);
                root_1 = (Object)adaptor.becomeRoot(stream_FUNCTION.nextNode(), root_1);

                dbg.location(1568,17);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1568:17: ( $name)?
                if ( stream_name.hasNext() ) {
                    dbg.location(1568,17);
                    adaptor.addChild(root_1, stream_name.nextNode());

                }
                stream_name.reset();
                dbg.location(1568,24);
                adaptor.addChild(root_1, stream_formalParameterList.nextTree());
                dbg.location(1568,44);
                adaptor.addChild(root_1, stream_functionBody.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1569, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "functionExpression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "functionExpression"

    public static class formalParameterList_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "formalParameterList"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1571:1: formalParameterList : LPAREN ( Identifier ( COMMA Identifier )* )? RPAREN -> ^( ARGS ( Identifier )* ) ;
    public final ES3Parser.formalParameterList_return formalParameterList() throws RecognitionException {
        ES3Parser.formalParameterList_return retval = new ES3Parser.formalParameterList_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LPAREN275=null;
        Token Identifier276=null;
        Token COMMA277=null;
        Token Identifier278=null;
        Token RPAREN279=null;

        Object LPAREN275_tree=null;
        Object Identifier276_tree=null;
        Object COMMA277_tree=null;
        Object Identifier278_tree=null;
        Object RPAREN279_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_Identifier=new RewriteRuleTokenStream(adaptor,"token Identifier");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");

        try { dbg.enterRule(getGrammarFileName(), "formalParameterList");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1571, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1572:2: ( LPAREN ( Identifier ( COMMA Identifier )* )? RPAREN -> ^( ARGS ( Identifier )* ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1572:4: LPAREN ( Identifier ( COMMA Identifier )* )? RPAREN
            {
            dbg.location(1572,4);
            LPAREN275=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_formalParameterList5744);  
            stream_LPAREN.add(LPAREN275);

            dbg.location(1572,11);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1572:11: ( Identifier ( COMMA Identifier )* )?
            int alt71=2;
            try { dbg.enterSubRule(71);
            try { dbg.enterDecision(71, decisionCanBacktrack[71]);

            int LA71_0 = input.LA(1);

            if ( (LA71_0==Identifier) ) {
                alt71=1;
            }
            } finally {dbg.exitDecision(71);}

            switch (alt71) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1572:13: Identifier ( COMMA Identifier )*
                    {
                    dbg.location(1572,13);
                    Identifier276=(Token)match(input,Identifier,FOLLOW_Identifier_in_formalParameterList5748);  
                    stream_Identifier.add(Identifier276);

                    dbg.location(1572,24);
                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1572:24: ( COMMA Identifier )*
                    try { dbg.enterSubRule(70);

                    loop70:
                    do {
                        int alt70=2;
                        try { dbg.enterDecision(70, decisionCanBacktrack[70]);

                        int LA70_0 = input.LA(1);

                        if ( (LA70_0==COMMA) ) {
                            alt70=1;
                        }


                        } finally {dbg.exitDecision(70);}

                        switch (alt70) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1572:26: COMMA Identifier
                    	    {
                    	    dbg.location(1572,26);
                    	    COMMA277=(Token)match(input,COMMA,FOLLOW_COMMA_in_formalParameterList5752);  
                    	    stream_COMMA.add(COMMA277);

                    	    dbg.location(1572,32);
                    	    Identifier278=(Token)match(input,Identifier,FOLLOW_Identifier_in_formalParameterList5754);  
                    	    stream_Identifier.add(Identifier278);


                    	    }
                    	    break;

                    	default :
                    	    break loop70;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(70);}


                    }
                    break;

            }
            } finally {dbg.exitSubRule(71);}

            dbg.location(1572,49);
            RPAREN279=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_formalParameterList5762);  
            stream_RPAREN.add(RPAREN279);



            // AST REWRITE
            // elements: Identifier
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1573:2: -> ^( ARGS ( Identifier )* )
            {
                dbg.location(1573,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1573:5: ^( ARGS ( Identifier )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1573,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ARGS, "ARGS"), root_1);

                dbg.location(1573,13);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1573:13: ( Identifier )*
                while ( stream_Identifier.hasNext() ) {
                    dbg.location(1573,13);
                    adaptor.addChild(root_1, stream_Identifier.nextNode());

                }
                stream_Identifier.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1574, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "formalParameterList");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "formalParameterList"

    public static class functionBody_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "functionBody"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1576:1: functionBody : lb= LBRACE ( sourceElement )* RBRACE -> ^( BLOCK[$lb, \"BLOCK\"] ( sourceElement )* ) ;
    public final ES3Parser.functionBody_return functionBody() throws RecognitionException {
        ES3Parser.functionBody_return retval = new ES3Parser.functionBody_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token lb=null;
        Token RBRACE281=null;
        ES3Parser.sourceElement_return sourceElement280 = null;


        Object lb_tree=null;
        Object RBRACE281_tree=null;
        RewriteRuleTokenStream stream_RBRACE=new RewriteRuleTokenStream(adaptor,"token RBRACE");
        RewriteRuleTokenStream stream_LBRACE=new RewriteRuleTokenStream(adaptor,"token LBRACE");
        RewriteRuleSubtreeStream stream_sourceElement=new RewriteRuleSubtreeStream(adaptor,"rule sourceElement");
        try { dbg.enterRule(getGrammarFileName(), "functionBody");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1576, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1577:2: (lb= LBRACE ( sourceElement )* RBRACE -> ^( BLOCK[$lb, \"BLOCK\"] ( sourceElement )* ) )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1577:4: lb= LBRACE ( sourceElement )* RBRACE
            {
            dbg.location(1577,6);
            lb=(Token)match(input,LBRACE,FOLLOW_LBRACE_in_functionBody5787);  
            stream_LBRACE.add(lb);

            dbg.location(1577,14);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1577:14: ( sourceElement )*
            try { dbg.enterSubRule(72);

            loop72:
            do {
                int alt72=2;
                try { dbg.enterDecision(72, decisionCanBacktrack[72]);

                int LA72_0 = input.LA(1);

                if ( ((LA72_0>=NULL && LA72_0<=BREAK)||LA72_0==CONTINUE||(LA72_0>=DELETE && LA72_0<=DO)||(LA72_0>=FOR && LA72_0<=IF)||(LA72_0>=NEW && LA72_0<=WITH)||LA72_0==LBRACE||LA72_0==LPAREN||LA72_0==LBRACK||LA72_0==SEMIC||(LA72_0>=ADD && LA72_0<=SUB)||(LA72_0>=INC && LA72_0<=DEC)||(LA72_0>=NOT && LA72_0<=INV)||(LA72_0>=Identifier && LA72_0<=StringLiteral)||LA72_0==RegularExpressionLiteral||(LA72_0>=DecimalLiteral && LA72_0<=HexIntegerLiteral)) ) {
                    alt72=1;
                }


                } finally {dbg.exitDecision(72);}

                switch (alt72) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1577:14: sourceElement
            	    {
            	    dbg.location(1577,14);
            	    pushFollow(FOLLOW_sourceElement_in_functionBody5789);
            	    sourceElement280=sourceElement();

            	    state._fsp--;

            	    stream_sourceElement.add(sourceElement280.getTree());

            	    }
            	    break;

            	default :
            	    break loop72;
                }
            } while (true);
            } finally {dbg.exitSubRule(72);}

            dbg.location(1577,29);
            RBRACE281=(Token)match(input,RBRACE,FOLLOW_RBRACE_in_functionBody5792);  
            stream_RBRACE.add(RBRACE281);



            // AST REWRITE
            // elements: sourceElement
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 1578:2: -> ^( BLOCK[$lb, \"BLOCK\"] ( sourceElement )* )
            {
                dbg.location(1578,5);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1578:5: ^( BLOCK[$lb, \"BLOCK\"] ( sourceElement )* )
                {
                Object root_1 = (Object)adaptor.nil();
                dbg.location(1578,8);
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BLOCK, lb, "BLOCK"), root_1);

                dbg.location(1578,28);
                // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1578:28: ( sourceElement )*
                while ( stream_sourceElement.hasNext() ) {
                    dbg.location(1578,28);
                    adaptor.addChild(root_1, stream_sourceElement.nextTree());

                }
                stream_sourceElement.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1579, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "functionBody");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "functionBody"

    public static class program_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "program"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1585:1: program : ( sourceElement )* ;
    public final ES3Parser.program_return program() throws RecognitionException {
        ES3Parser.program_return retval = new ES3Parser.program_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.sourceElement_return sourceElement282 = null;



        try { dbg.enterRule(getGrammarFileName(), "program");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1585, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1586:2: ( ( sourceElement )* )
            dbg.enterAlt(1);

            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1586:4: ( sourceElement )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(1586,4);
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1586:4: ( sourceElement )*
            try { dbg.enterSubRule(73);

            loop73:
            do {
                int alt73=2;
                try { dbg.enterDecision(73, decisionCanBacktrack[73]);

                int LA73_0 = input.LA(1);

                if ( ((LA73_0>=NULL && LA73_0<=BREAK)||LA73_0==CONTINUE||(LA73_0>=DELETE && LA73_0<=DO)||(LA73_0>=FOR && LA73_0<=IF)||(LA73_0>=NEW && LA73_0<=WITH)||LA73_0==LBRACE||LA73_0==LPAREN||LA73_0==LBRACK||LA73_0==SEMIC||(LA73_0>=ADD && LA73_0<=SUB)||(LA73_0>=INC && LA73_0<=DEC)||(LA73_0>=NOT && LA73_0<=INV)||(LA73_0>=Identifier && LA73_0<=StringLiteral)||LA73_0==RegularExpressionLiteral||(LA73_0>=DecimalLiteral && LA73_0<=HexIntegerLiteral)) ) {
                    alt73=1;
                }


                } finally {dbg.exitDecision(73);}

                switch (alt73) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1586:4: sourceElement
            	    {
            	    dbg.location(1586,4);
            	    pushFollow(FOLLOW_sourceElement_in_program5821);
            	    sourceElement282=sourceElement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, sourceElement282.getTree());

            	    }
            	    break;

            	default :
            	    break loop73;
                }
            } while (true);
            } finally {dbg.exitSubRule(73);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1587, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "program");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "program"

    public static class sourceElement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sourceElement"
    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1594:1: sourceElement options {k=1; } : ({...}? functionDeclaration | statement );
    public final ES3Parser.sourceElement_return sourceElement() throws RecognitionException {
        ES3Parser.sourceElement_return retval = new ES3Parser.sourceElement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ES3Parser.functionDeclaration_return functionDeclaration283 = null;

        ES3Parser.statement_return statement284 = null;



        try { dbg.enterRule(getGrammarFileName(), "sourceElement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(1594, 1);

        try {
            // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1599:2: ({...}? functionDeclaration | statement )
            int alt74=2;
            try { dbg.enterDecision(74, decisionCanBacktrack[74]);

            try {
                isCyclicDecision = true;
                alt74 = dfa74.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(74);}

            switch (alt74) {
                case 1 :
                    dbg.enterAlt(1);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1599:4: {...}? functionDeclaration
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1599,4);
                    if ( !(evalPredicate( input.LA(1) == FUNCTION ," input.LA(1) == FUNCTION ")) ) {
                        throw new FailedPredicateException(input, "sourceElement", " input.LA(1) == FUNCTION ");
                    }
                    dbg.location(1599,33);
                    pushFollow(FOLLOW_functionDeclaration_in_sourceElement5850);
                    functionDeclaration283=functionDeclaration();

                    state._fsp--;

                    adaptor.addChild(root_0, functionDeclaration283.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // /Users/porcelli/Documents/dev/dyn.js/src/main/antlr3/org/dynjs/parser/ES3.g:1600:4: statement
                    {
                    root_0 = (Object)adaptor.nil();

                    dbg.location(1600,4);
                    pushFollow(FOLLOW_statement_in_sourceElement5855);
                    statement284=statement();

                    state._fsp--;

                    adaptor.addChild(root_0, statement284.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(1601, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "sourceElement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "sourceElement"

    // Delegated rules


    protected DFA43 dfa43 = new DFA43(this);
    protected DFA44 dfa44 = new DFA44(this);
    protected DFA74 dfa74 = new DFA74(this);
    static final String DFA43_eotS =
        "\45\uffff";
    static final String DFA43_eofS =
        "\45\uffff";
    static final String DFA43_minS =
        "\1\4\2\0\42\uffff";
    static final String DFA43_maxS =
        "\1\u00a2\2\0\42\uffff";
    static final String DFA43_acceptS =
        "\3\uffff\1\3\37\uffff\1\1\1\2";
    static final String DFA43_specialS =
        "\1\uffff\1\0\1\1\42\uffff}>";
    static final String[] DFA43_transitionS = {
            "\4\3\2\uffff\1\3\1\uffff\2\3\2\uffff\3\3\2\uffff\13\3\37\uffff"+
            "\1\1\1\uffff\1\3\1\uffff\1\3\2\uffff\1\3\11\uffff\2\3\2\uffff"+
            "\2\3\6\uffff\2\3\67\uffff\1\2\1\3\5\uffff\1\3\3\uffff\3\3",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "1222:1: statement options {k=1; } : ({...}? block | printStatement | statementTail );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA43_1 = input.LA(1);

                         
                        int index43_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (evalPredicate( input.LA(1) == LBRACE ," input.LA(1) == LBRACE ")) ) {s = 35;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index43_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA43_2 = input.LA(1);

                         
                        int index43_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (evalPredicate((validateIdentifierKey(DynJSSoftKeywords.PRINT)),"(validateIdentifierKey(DynJSSoftKeywords.PRINT))")) ) {s = 36;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index43_2);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 43, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA44_eotS =
        "\17\uffff";
    static final String DFA44_eofS =
        "\4\uffff\1\3\12\uffff";
    static final String DFA44_minS =
        "\1\4\3\uffff\1\23\12\uffff";
    static final String DFA44_maxS =
        "\1\u00a2\3\uffff\1\u0093\12\uffff";
    static final String DFA44_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\13\1\14"+
        "\1\15\1\12";
    static final String DFA44_specialS =
        "\17\uffff}>";
    static final String[] DFA44_transitionS = {
            "\3\3\1\10\2\uffff\1\7\1\uffff\1\3\1\6\2\uffff\1\6\1\3\1\5\2"+
            "\uffff\1\3\1\11\1\13\1\3\1\14\1\15\1\3\1\1\1\3\1\6\1\12\37\uffff"+
            "\1\3\1\uffff\1\3\1\uffff\1\3\2\uffff\1\2\11\uffff\2\3\2\uffff"+
            "\2\3\6\uffff\2\3\67\uffff\1\4\1\3\5\uffff\1\3\3\uffff\3\3",
            "",
            "",
            "",
            "\2\3\53\uffff\2\3\1\uffff\1\3\1\uffff\27\3\2\uffff\3\3\1\16"+
            "\15\3\43\uffff\2\3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA44_eot = DFA.unpackEncodedString(DFA44_eotS);
    static final short[] DFA44_eof = DFA.unpackEncodedString(DFA44_eofS);
    static final char[] DFA44_min = DFA.unpackEncodedStringToUnsignedChars(DFA44_minS);
    static final char[] DFA44_max = DFA.unpackEncodedStringToUnsignedChars(DFA44_maxS);
    static final short[] DFA44_accept = DFA.unpackEncodedString(DFA44_acceptS);
    static final short[] DFA44_special = DFA.unpackEncodedString(DFA44_specialS);
    static final short[][] DFA44_transition;

    static {
        int numStates = DFA44_transitionS.length;
        DFA44_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA44_transition[i] = DFA.unpackEncodedString(DFA44_transitionS[i]);
        }
    }

    class DFA44 extends DFA {

        public DFA44(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 44;
            this.eot = DFA44_eot;
            this.eof = DFA44_eof;
            this.min = DFA44_min;
            this.max = DFA44_max;
            this.accept = DFA44_accept;
            this.special = DFA44_special;
            this.transition = DFA44_transition;
        }
        public String getDescription() {
            return "1232:1: statementTail : ( variableStatement | emptyStatement | expressionStatement | ifStatement | iterationStatement | continueStatement | breakStatement | returnStatement | withStatement | labelledStatement | switchStatement | throwStatement | tryStatement );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
    }
    static final String DFA74_eotS =
        "\44\uffff";
    static final String DFA74_eofS =
        "\44\uffff";
    static final String DFA74_minS =
        "\1\4\1\0\42\uffff";
    static final String DFA74_maxS =
        "\1\u00a2\1\0\42\uffff";
    static final String DFA74_acceptS =
        "\2\uffff\1\2\40\uffff\1\1";
    static final String DFA74_specialS =
        "\1\uffff\1\0\42\uffff}>";
    static final String[] DFA74_transitionS = {
            "\4\2\2\uffff\1\2\1\uffff\2\2\2\uffff\1\2\1\1\1\2\2\uffff\13"+
            "\2\37\uffff\1\2\1\uffff\1\2\1\uffff\1\2\2\uffff\1\2\11\uffff"+
            "\2\2\2\uffff\2\2\6\uffff\2\2\67\uffff\2\2\5\uffff\1\2\3\uffff"+
            "\3\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA74_eot = DFA.unpackEncodedString(DFA74_eotS);
    static final short[] DFA74_eof = DFA.unpackEncodedString(DFA74_eofS);
    static final char[] DFA74_min = DFA.unpackEncodedStringToUnsignedChars(DFA74_minS);
    static final char[] DFA74_max = DFA.unpackEncodedStringToUnsignedChars(DFA74_maxS);
    static final short[] DFA74_accept = DFA.unpackEncodedString(DFA74_acceptS);
    static final short[] DFA74_special = DFA.unpackEncodedString(DFA74_specialS);
    static final short[][] DFA74_transition;

    static {
        int numStates = DFA74_transitionS.length;
        DFA74_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA74_transition[i] = DFA.unpackEncodedString(DFA74_transitionS[i]);
        }
    }

    class DFA74 extends DFA {

        public DFA74(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 74;
            this.eot = DFA74_eot;
            this.eof = DFA74_eof;
            this.min = DFA74_min;
            this.max = DFA74_max;
            this.accept = DFA74_accept;
            this.special = DFA74_special;
            this.transition = DFA74_transition;
        }
        public String getDescription() {
            return "1594:1: sourceElement options {k=1; } : ({...}? functionDeclaration | statement );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA74_1 = input.LA(1);

                         
                        int index74_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (evalPredicate( input.LA(1) == FUNCTION ," input.LA(1) == FUNCTION ")) ) {s = 35;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index74_1);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 74, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_reservedWord_in_token1751 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_token1756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_punctuator_in_token1761 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numericLiteral_in_token1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_token1771 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_keyword_in_reservedWord1784 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_futureReservedWord_in_reservedWord1789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_reservedWord1794 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanLiteral_in_reservedWord1799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_keyword0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_futureReservedWord0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_punctuator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal2480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanLiteral_in_literal2485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numericLiteral_in_literal2490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_literal2495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RegularExpressionLiteral_in_literal2500 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_booleanLiteral0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_numericLiteral0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression3113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_primaryExpression3118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression3123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayLiteral_in_primaryExpression3128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_objectLiteral_in_primaryExpression3133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression3140 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_primaryExpression3142 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression3144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_arrayLiteral3168 = new BitSet(new long[]{0x8000000029221070L,0x000000003033009AL,0x0000000710600000L});
    public static final BitSet FOLLOW_arrayItem_in_arrayLiteral3172 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_COMMA_in_arrayLiteral3176 = new BitSet(new long[]{0x8000000029221070L,0x000000003033009AL,0x0000000710600000L});
    public static final BitSet FOLLOW_arrayItem_in_arrayLiteral3178 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000090L});
    public static final BitSet FOLLOW_RBRACK_in_arrayLiteral3186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_arrayItem3214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_objectLiteral3246 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L,0x0000000700600000L});
    public static final BitSet FOLLOW_nameValuePair_in_objectLiteral3250 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000081L});
    public static final BitSet FOLLOW_COMMA_in_objectLiteral3254 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000700600000L});
    public static final BitSet FOLLOW_nameValuePair_in_objectLiteral3256 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000081L});
    public static final BitSet FOLLOW_RBRACE_in_objectLiteral3264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_propertyName_in_nameValuePair3289 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_COLON_in_nameValuePair3291 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_nameValuePair3293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_propertyName3317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_propertyName3322 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numericLiteral_in_propertyName3327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_memberExpression3345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionExpression_in_memberExpression3350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_newExpression_in_memberExpression3355 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_newExpression3366 = new BitSet(new long[]{0x8000000001220070L,0x000000000000000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_memberExpression_in_newExpression3369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arguments3382 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000EL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_arguments3386 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000084L});
    public static final BitSet FOLLOW_COMMA_in_arguments3390 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_arguments3392 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000084L});
    public static final BitSet FOLLOW_RPAREN_in_arguments3400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberExpression_in_leftHandSideExpression3429 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002AL});
    public static final BitSet FOLLOW_arguments_in_leftHandSideExpression3445 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002AL});
    public static final BitSet FOLLOW_LBRACK_in_leftHandSideExpression3466 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_leftHandSideExpression3468 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_RBRACK_in_leftHandSideExpression3470 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002AL});
    public static final BitSet FOLLOW_DOT_in_leftHandSideExpression3489 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_Identifier_in_leftHandSideExpression3491 = new BitSet(new long[]{0x0000000000000002L,0x000000000000002AL});
    public static final BitSet FOLLOW_leftHandSideExpression_in_postfixExpression3526 = new BitSet(new long[]{0x0000000000000002L,0x0000000000300000L});
    public static final BitSet FOLLOW_postfixOperator_in_postfixExpression3532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INC_in_postfixOperator3550 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEC_in_postfixOperator3559 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression3576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryOperator_in_unaryExpression3581 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression3584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DELETE_in_unaryOperator3596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VOID_in_unaryOperator3601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPEOF_in_unaryOperator3606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INC_in_unaryOperator3611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEC_in_unaryOperator3616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ADD_in_unaryOperator3623 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryOperator3632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INV_in_unaryOperator3639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_unaryOperator3644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression3659 = new BitSet(new long[]{0x0000000000000002L,0x00002000000C0000L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression3663 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression3678 = new BitSet(new long[]{0x0000000000000002L,0x00002000000C0000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression3696 = new BitSet(new long[]{0x0000000000000002L,0x0000000000030000L});
    public static final BitSet FOLLOW_set_in_additiveExpression3700 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression3711 = new BitSet(new long[]{0x0000000000000002L,0x0000000000030000L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression3730 = new BitSet(new long[]{0x0000000000000002L,0x0000000001C00000L});
    public static final BitSet FOLLOW_set_in_shiftExpression3734 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression3749 = new BitSet(new long[]{0x0000000000000002L,0x0000000001C00000L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression3768 = new BitSet(new long[]{0x0000000000180002L,0x0000000000000F00L});
    public static final BitSet FOLLOW_set_in_relationalExpression3772 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression3799 = new BitSet(new long[]{0x0000000000180002L,0x0000000000000F00L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpressionNoIn3813 = new BitSet(new long[]{0x0000000000100002L,0x0000000000000F00L});
    public static final BitSet FOLLOW_set_in_relationalExpressionNoIn3817 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpressionNoIn3840 = new BitSet(new long[]{0x0000000000100002L,0x0000000000000F00L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression3859 = new BitSet(new long[]{0x0000000000000002L,0x000000000000F000L});
    public static final BitSet FOLLOW_set_in_equalityExpression3863 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression3882 = new BitSet(new long[]{0x0000000000000002L,0x000000000000F000L});
    public static final BitSet FOLLOW_relationalExpressionNoIn_in_equalityExpressionNoIn3896 = new BitSet(new long[]{0x0000000000000002L,0x000000000000F000L});
    public static final BitSet FOLLOW_set_in_equalityExpressionNoIn3900 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_relationalExpressionNoIn_in_equalityExpressionNoIn3919 = new BitSet(new long[]{0x0000000000000002L,0x000000000000F000L});
    public static final BitSet FOLLOW_equalityExpression_in_bitwiseANDExpression3939 = new BitSet(new long[]{0x0000000000000002L,0x0000000002000000L});
    public static final BitSet FOLLOW_AND_in_bitwiseANDExpression3943 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_equalityExpression_in_bitwiseANDExpression3946 = new BitSet(new long[]{0x0000000000000002L,0x0000000002000000L});
    public static final BitSet FOLLOW_equalityExpressionNoIn_in_bitwiseANDExpressionNoIn3960 = new BitSet(new long[]{0x0000000000000002L,0x0000000002000000L});
    public static final BitSet FOLLOW_AND_in_bitwiseANDExpressionNoIn3964 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_equalityExpressionNoIn_in_bitwiseANDExpressionNoIn3967 = new BitSet(new long[]{0x0000000000000002L,0x0000000002000000L});
    public static final BitSet FOLLOW_bitwiseANDExpression_in_bitwiseXORExpression3983 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_XOR_in_bitwiseXORExpression3987 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_bitwiseANDExpression_in_bitwiseXORExpression3990 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_bitwiseANDExpressionNoIn_in_bitwiseXORExpressionNoIn4006 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_XOR_in_bitwiseXORExpressionNoIn4010 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_bitwiseANDExpressionNoIn_in_bitwiseXORExpressionNoIn4013 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_bitwiseXORExpression_in_bitwiseORExpression4028 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_OR_in_bitwiseORExpression4032 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_bitwiseXORExpression_in_bitwiseORExpression4035 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_bitwiseXORExpressionNoIn_in_bitwiseORExpressionNoIn4050 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_OR_in_bitwiseORExpressionNoIn4054 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_bitwiseXORExpressionNoIn_in_bitwiseORExpressionNoIn4057 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
    public static final BitSet FOLLOW_bitwiseORExpression_in_logicalANDExpression4076 = new BitSet(new long[]{0x0000000000000002L,0x0000000040000000L});
    public static final BitSet FOLLOW_LAND_in_logicalANDExpression4080 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_bitwiseORExpression_in_logicalANDExpression4083 = new BitSet(new long[]{0x0000000000000002L,0x0000000040000000L});
    public static final BitSet FOLLOW_bitwiseORExpressionNoIn_in_logicalANDExpressionNoIn4097 = new BitSet(new long[]{0x0000000000000002L,0x0000000040000000L});
    public static final BitSet FOLLOW_LAND_in_logicalANDExpressionNoIn4101 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_bitwiseORExpressionNoIn_in_logicalANDExpressionNoIn4104 = new BitSet(new long[]{0x0000000000000002L,0x0000000040000000L});
    public static final BitSet FOLLOW_logicalANDExpression_in_logicalORExpression4119 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_LOR_in_logicalORExpression4123 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_logicalANDExpression_in_logicalORExpression4126 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_logicalANDExpressionNoIn_in_logicalORExpressionNoIn4141 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_LOR_in_logicalORExpressionNoIn4145 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_logicalANDExpressionNoIn_in_logicalORExpressionNoIn4148 = new BitSet(new long[]{0x0000000000000002L,0x0000000080000000L});
    public static final BitSet FOLLOW_logicalORExpression_in_conditionalExpression4167 = new BitSet(new long[]{0x0000000000000002L,0x0000000100000000L});
    public static final BitSet FOLLOW_QUE_in_conditionalExpression4171 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_conditionalExpression4174 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_COLON_in_conditionalExpression4176 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_conditionalExpression4179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_logicalORExpressionNoIn_in_conditionalExpressionNoIn4193 = new BitSet(new long[]{0x0000000000000002L,0x0000000100000000L});
    public static final BitSet FOLLOW_QUE_in_conditionalExpressionNoIn4197 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpressionNoIn_in_conditionalExpressionNoIn4200 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_COLON_in_conditionalExpressionNoIn4202 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpressionNoIn_in_conditionalExpressionNoIn4205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_assignmentExpression4233 = new BitSet(new long[]{0x0000000000000002L,0x00005FFC00000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentExpression4240 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_assignmentExpression4243 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_assignmentOperator0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpressionNoIn_in_assignmentExpressionNoIn4320 = new BitSet(new long[]{0x0000000000000002L,0x00005FFC00000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_assignmentExpressionNoIn4327 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpressionNoIn_in_assignmentExpressionNoIn4330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentExpression_in_expression4352 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_COMMA_in_expression4356 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_expression4360 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_assignmentExpressionNoIn_in_expressionNoIn4397 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_COMMA_in_expressionNoIn4401 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpressionNoIn_in_expressionNoIn4405 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_SEMIC_in_semic4456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EOF_in_semic4461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RBRACE_in_semic4466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EOL_in_semic4473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MultiLineComment_in_semic4477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_statement4506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_printStatement_in_statement4511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementTail_in_statement4516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableStatement_in_statementTail4528 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptyStatement_in_statementTail4533 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionStatement_in_statementTail4538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_statementTail4543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_iterationStatement_in_statementTail4548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_continueStatement_in_statementTail4553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_breakStatement_in_statementTail4558 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_returnStatement_in_statementTail4563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_withStatement_in_statementTail4568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelledStatement_in_statementTail4573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchStatement_in_statementTail4578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_throwStatement_in_statementTail4583 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tryStatement_in_statementTail4588 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block4603 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004BL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_block4605 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004BL,0x0000000710600000L});
    public static final BitSet FOLLOW_RBRACE_in_block4608 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_print_key_in_printStatement4636 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_printStatement4639 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_printStatement4642 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_printStatement4644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_print_key4662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_variableStatement4685 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_variableDeclaration_in_variableStatement4687 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_COMMA_in_variableStatement4691 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_variableDeclaration_in_variableStatement4693 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_semic_in_variableStatement4698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_variableDeclaration4721 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
    public static final BitSet FOLLOW_ASSIGN_in_variableDeclaration4725 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpression_in_variableDeclaration4728 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_variableDeclarationNoIn4743 = new BitSet(new long[]{0x0000000000000002L,0x0000000400000000L});
    public static final BitSet FOLLOW_ASSIGN_in_variableDeclarationNoIn4747 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_assignmentExpressionNoIn_in_variableDeclarationNoIn4750 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMIC_in_emptyStatement4769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionStatement4788 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_semic_in_expressionStatement4790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifStatement4808 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_ifStatement4810 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_ifStatement4812 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_ifStatement4814 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_ifStatement4816 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_ELSE_in_ifStatement4822 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_ifStatement4824 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doStatement_in_iterationStatement4857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_whileStatement_in_iterationStatement4862 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forStatement_in_iterationStatement4867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_doStatement4879 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_doStatement4881 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_WHILE_in_doStatement4883 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_doStatement4885 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_doStatement4887 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_doStatement4889 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_semic_in_doStatement4891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_whileStatement4916 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_whileStatement4919 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_whileStatement4922 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_whileStatement4924 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_whileStatement4927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forStatement4940 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_forStatement4943 = new BitSet(new long[]{0x8000000039221070L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_forControl_in_forStatement4946 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_forStatement4948 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_forStatement4951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forControlVar_in_forControl4962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forControlExpression_in_forControl4967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forControlSemic_in_forControl4972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_forControlVar4983 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_variableDeclarationNoIn_in_forControlVar4985 = new BitSet(new long[]{0x0000000000080000L,0x00000000000000C0L});
    public static final BitSet FOLLOW_IN_in_forControlVar4997 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlVar4999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMA_in_forControlVar5045 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_variableDeclarationNoIn_in_forControlVar5047 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C0L});
    public static final BitSet FOLLOW_SEMIC_in_forControlVar5052 = new BitSet(new long[]{0x8000000029221070L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlVar5056 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_SEMIC_in_forControlVar5059 = new BitSet(new long[]{0x8000000029221072L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlVar5063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionNoIn_in_forControlExpression5129 = new BitSet(new long[]{0x0000000000080000L,0x0000000000000040L});
    public static final BitSet FOLLOW_IN_in_forControlExpression5144 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlExpression5148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMIC_in_forControlExpression5194 = new BitSet(new long[]{0x8000000029221070L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlExpression5198 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_SEMIC_in_forControlExpression5201 = new BitSet(new long[]{0x8000000029221072L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlExpression5205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMIC_in_forControlSemic5264 = new BitSet(new long[]{0x8000000029221070L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlSemic5268 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_SEMIC_in_forControlSemic5271 = new BitSet(new long[]{0x8000000029221072L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_forControlSemic5275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_continueStatement5329 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000002C0000L});
    public static final BitSet FOLLOW_Identifier_in_continueStatement5334 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_semic_in_continueStatement5337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_breakStatement5356 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000002C0000L});
    public static final BitSet FOLLOW_Identifier_in_breakStatement5361 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_semic_in_breakStatement5364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_returnStatement5383 = new BitSet(new long[]{0x8000000029221070L,0x00000000303300CBL,0x00000007106C0000L});
    public static final BitSet FOLLOW_expression_in_returnStatement5388 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_semic_in_returnStatement5391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_withStatement5408 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_withStatement5411 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_withStatement5414 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_withStatement5416 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_withStatement5419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SWITCH_in_switchStatement5440 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_switchStatement5442 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_switchStatement5444 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_switchStatement5446 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_LBRACE_in_switchStatement5448 = new BitSet(new long[]{0x0000000000000900L,0x0000000000000001L});
    public static final BitSet FOLLOW_defaultClause_in_switchStatement5455 = new BitSet(new long[]{0x0000000000000900L,0x0000000000000001L});
    public static final BitSet FOLLOW_caseClause_in_switchStatement5461 = new BitSet(new long[]{0x0000000000000900L,0x0000000000000001L});
    public static final BitSet FOLLOW_RBRACE_in_switchStatement5466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CASE_in_caseClause5494 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_caseClause5497 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_COLON_in_caseClause5499 = new BitSet(new long[]{0x80000000FFE734F2L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_caseClause5502 = new BitSet(new long[]{0x80000000FFE734F2L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_DEFAULT_in_defaultClause5515 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_COLON_in_defaultClause5518 = new BitSet(new long[]{0x80000000FFE734F2L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_defaultClause5521 = new BitSet(new long[]{0x80000000FFE734F2L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_Identifier_in_labelledStatement5538 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_COLON_in_labelledStatement5540 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_statement_in_labelledStatement5542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_throwStatement5573 = new BitSet(new long[]{0x8000000029221070L,0x000000003033000AL,0x0000000710600000L});
    public static final BitSet FOLLOW_expression_in_throwStatement5578 = new BitSet(new long[]{0x0000000000000000L,0x00000000000000C1L,0x00000000000C0000L});
    public static final BitSet FOLLOW_semic_in_throwStatement5580 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_tryStatement5597 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_block_in_tryStatement5600 = new BitSet(new long[]{0x0000000000008200L});
    public static final BitSet FOLLOW_catchClause_in_tryStatement5604 = new BitSet(new long[]{0x0000000000008202L});
    public static final BitSet FOLLOW_finallyClause_in_tryStatement5606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_finallyClause_in_tryStatement5611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause5625 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause5628 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_Identifier_in_catchClause5631 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause5633 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause5636 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FINALLY_in_finallyClause5648 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_block_in_finallyClause5651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionDeclaration5672 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_Identifier_in_functionDeclaration5676 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameterList_in_functionDeclaration5678 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_functionBody_in_functionDeclaration5680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_functionExpression5707 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L,0x0000000000200000L});
    public static final BitSet FOLLOW_Identifier_in_functionExpression5711 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameterList_in_functionExpression5714 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_functionBody_in_functionExpression5716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameterList5744 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L,0x0000000000200000L});
    public static final BitSet FOLLOW_Identifier_in_formalParameterList5748 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000084L});
    public static final BitSet FOLLOW_COMMA_in_formalParameterList5752 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_Identifier_in_formalParameterList5754 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000084L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameterList5762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_functionBody5787 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004BL,0x0000000710600000L});
    public static final BitSet FOLLOW_sourceElement_in_functionBody5789 = new BitSet(new long[]{0x80000000FFE734F0L,0x000000003033004BL,0x0000000710600000L});
    public static final BitSet FOLLOW_RBRACE_in_functionBody5792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sourceElement_in_program5821 = new BitSet(new long[]{0x80000000FFE734F2L,0x000000003033004AL,0x0000000710600000L});
    public static final BitSet FOLLOW_functionDeclaration_in_sourceElement5850 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_sourceElement5855 = new BitSet(new long[]{0x0000000000000002L});

}