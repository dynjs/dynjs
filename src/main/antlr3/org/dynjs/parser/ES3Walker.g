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

*/

tree grammar ES3Walker ;

options
{
	ASTLabelType = CommonTree ;
	tokenVocab = ES3 ;
}

@header {
package org.dynjs.parser;

}

@members {

    List<Statement> result = null;
    Executor executor = null;

    public void setExecutor(Executor executor){
        this.executor = executor;
    }
    
    public List<Statement> getResult(){
        return result;
    }
}


/*
Note: functionDeclaration is reachable via statement->expression as functionExpression and functionDeclaration are combined.
*/
program
@init { List<Statement> blockContent = new ArrayList<Statement>(); }
        : (st=statement {blockContent.add($st.value);})*
        {   result = executor.program(blockContent);   }
	;

statement returns [Statement value] 
	: block
        {  $value = $block.value;   }
	| variableDeclaration
	    {  $value = $variableDeclaration.value;   }
	| expression
	    {  $value = $expression.value;   }
	| printStatement
        { $value = $printStatement.value; }
	| ifStatement
	| doStatement
	| whileStatement
	| forStatement
	| continueStatement
	| breakStatement
	| returnStatement
        { $value = $returnStatement.value; }
	| withStatement
	| labelledStatement
	| switchStatement
	| throwStatement
	| tryStatement
	;

block returns [Statement value]
@init { List<Statement> blockContent = new ArrayList<Statement>(); }
	: ^( BLOCK (st=statement {blockContent.add($st.value);})* )
	{  $value = executor.block(blockContent);  }
	;

printStatement returns [Statement value]
	: ^( SK_PRINT expression )
	{  $value = executor.printStatement($expression.value);  }
	;

variableDeclaration returns [Statement value]
	: ^( VAR
	      ( id=Identifier
	{   $value = executor.declareVar($id);   }
	      | ^( ASSIGN id=Identifier expr )
	{   $value = executor.declareVar($id, $expr.value);   }
	      )+
	   )
	;

ifStatement returns [Statement value]
	: ^( IF vbool=expression vthen=statement velse=statement? )
    { $value = executor.ifStatement($vbool.value, $vthen.value, $velse.value); }
	;

doStatement returns [Statement value]
	: ^( DO vloop=statement vbool=expression )
    { $value = executor.doStatement($vbool.value, $vloop.value); }
	;

whileStatement returns [Statement value]
	: ^( WHILE vbool=expression vloop=statement )
    { $value = executor.whileStatement($vbool.value, $vloop.value); }
	;

forStatement
	: ^( FOR
         (   ^( FORSTEP ( exprOptClause | variableDeclaration ) exprOptClause exprOptClause )
         |   ^( FORITER ( exprClause | variableDeclaration ) exprClause )
         )
         statement )
    ;

exprOptClause
	: ^( EXPR expression? )
	;

exprClause
	: ^( EXPR expression )
	;

continueStatement
	: ^( CONTINUE Identifier? )
	;

breakStatement
	: ^( BREAK Identifier? )
	;

returnStatement returns [Statement value]
	: ^( RETURN ex=expression? )
    { $value = executor.returnStatement($ex.value); }
	;

withStatement
	: ^( WITH expression statement )
	;

labelledStatement
	: ^( LABELLED Identifier statement )
	;

switchStatement
	: ^( SWITCH expression defaultClause? caseClause* )
	;

defaultClause
	: ^( DEFAULT statement* )
	;

caseClause
	: ^( CASE expression statement* )
	;

throwStatement
	: ^( THROW expression )
	;

tryStatement
	: ^( TRY block catchClause? finallyClause? )
	;
	
catchClause
	: ^( CATCH Identifier block )
	;
	
finallyClause
	: ^( FINALLY block )
	;

expression returns [Statement value]
	: expr
	{ $value = $expr.value; }
	| ^( CEXPR expr+ )
	;

expr returns [Statement value]
	: leftHandSideExpression
	{ $value = $leftHandSideExpression.value; }

	// Assignment operators
	| ^( ASSIGN l=expr r=expr )
    { $value = executor.defineAssOp($l.value, $r.value); }
	| ^( MULASS l=expr r=expr )
    { $value = executor.defineMulAssOp($l.value, $r.value); }
	| ^( DIVASS l=expr r=expr )
    { $value = executor.defineDivAssOp($l.value, $r.value); }
	| ^( MODASS l=expr r=expr )
    { $value = executor.defineModAssOp($l.value, $r.value); }
	| ^( ADDASS l=expr r=expr )
    { $value = executor.defineAddAssOp($l.value, $r.value); }
	| ^( SUBASS l=expr r=expr )
    { $value = executor.defineSubAssOp($l.value, $r.value); }
	| ^( SHLASS l=expr r=expr )
    { $value = executor.defineShlAssOp($l.value, $r.value); }
	| ^( SHRASS l=expr r=expr )
    { $value = executor.defineShrAssOp($l.value, $r.value); }
	| ^( SHUASS l=expr r=expr )
    { $value = executor.defineShuAssOp($l.value, $r.value); }
	| ^( ANDASS l=expr r=expr )
    { $value = executor.defineAndAssOp($l.value, $r.value); }
	| ^( XORASS l=expr r=expr )
    { $value = executor.defineXorAssOp($l.value, $r.value); }
	| ^( ORASS l=expr r=expr )
    { $value = executor.defineOrAssOp($l.value, $r.value); }

	// Conditional operator
	| ^( QUE ex1=expr ex2=expr ex3=expr )
    { $value = executor.defineQueOp($ex1.value, $ex2.value, $ex3.value); }

	// Logical operators
	| ^( LOR l=expr r=expr )
    { $value = executor.defineLorOp($l.value, $r.value); }
	| ^( LAND l=expr r=expr )
    { $value = executor.defineLandOp($l.value, $r.value); }

	// Binary bitwise operators
	| ^( AND l=expr r=expr )
    { $value = executor.defineAndBitOp($l.value, $r.value); }
	| ^( OR l=expr r=expr )
    { $value = executor.defineOrBitOp($l.value, $r.value); }
	| ^( XOR l=expr r=expr )
    { $value = executor.defineXorBitOp($l.value, $r.value); }

	// Equality operators
	| ^( EQ l=expr r=expr )
    { $value = executor.defineEqOp($l.value, $r.value); }
	| ^( NEQ l=expr r=expr )
    { $value = executor.defineNEqOp($l.value, $r.value); }
	| ^( SAME l=expr r=expr )
    { $value = executor.defineSameOp($l.value, $r.value); }
	| ^( NSAME l=expr r=expr )
    { $value = executor.defineNSameOp($l.value, $r.value); }

	// Relational operator
	| ^( LT l=expr r=expr )
    { $value = executor.defineLtRelOp($l.value, $r.value); }
	| ^( GT l=expr r=expr )
    { $value = executor.defineGtRelOp($l.value, $r.value); }
	| ^( LTE l=expr r=expr )
    { $value = executor.defineLteRelOp($l.value, $r.value); }
	| ^( GTE l=expr r=expr )
    { $value = executor.defineGteRelOp($l.value, $r.value); }
	| ^( INSTANCEOF l=expr r=expr )
    { $value = executor.defineInstanceOfRelOp($l.value, $r.value); }
	| ^( IN l=expr r=expr )
    { $value = executor.defineInRelOp($l.value, $r.value); }

	// Bitwise shift operator
	| ^( SHL l=expr r=expr )
    { $value = executor.defineShlOp($l.value, $r.value); }
	| ^( SHR l=expr r=expr )
    { $value = executor.defineShrOp($l.value, $r.value); }
	| ^( SHU l=expr r=expr )
    { $value = executor.defineShuOp($l.value, $r.value); }

	// Additive operators
	| ^( ADD l=expr r=expr )
    { $value = executor.defineAddOp($l.value, $r.value); }
	| ^( SUB l=expr r=expr )
    { $value = executor.defineSubOp($l.value, $r.value); }

	// Multipiclative operators
	| ^( MUL l=expr r=expr )
    { $value = executor.defineMulOp($l.value, $r.value); }
	| ^( DIV l=expr r=expr )
    { $value = executor.defineDivOp($l.value, $r.value); }
	| ^( MOD l=expr r=expr )
    { $value = executor.defineModOp($l.value, $r.value); }

	// Unary operator
	| ^( DELETE ex=expr )
    { $value = executor.defineDeleteOp($ex.value); }
	| ^( VOID ex=expr )
    { $value = executor.defineVoidOp($ex.value); }
	| ^( TYPEOF ex=expr )
    { $value = executor.defineTypeOfOp($ex.value); }
	| ^( INC ex=expr )
    { $value = executor.defineIncOp($ex.value); }
	| ^( DEC ex=expr )
    { $value = executor.defineDecOp($ex.value); }
	| ^( POS ex=expr )
    { $value = executor.definePosOp($ex.value); }
	| ^( NEG ex=expr )
    { $value = executor.defineNegOp($ex.value); }
	| ^( INV ex=expr )
    { $value = executor.defineInvOp($ex.value); }
	| ^( NOT ex=expr )
    { $value = executor.defineNotOp($ex.value); }

	// Postfix operators
	| ^( PINC ex=expr )
    { $value = executor.definePIncOp($ex.value); }
	| ^( PDEC ex=expr )
    { $value = executor.definePDecOp($ex.value); }
	;

leftHandSideExpression returns [Statement value]
	: primaryExpression
	{ $value = $primaryExpression.value;  }
	| newExpression
	{ $value = $newExpression.value;  }
	| functionDeclaration
	{ $value = $functionDeclaration.value;  }
	| callExpression
	| memberExpression
	;

newExpression returns [Statement value]
	: ^( NEW leftHandSideExpression )
	{ $value = executor.executeNew($leftHandSideExpression.value); }
	;

functionDeclaration returns [Statement value]
@init { List<String> args = new ArrayList<String>(); }
	: ^( FUNCTION id=Identifier? ^( ARGS (ai=Identifier {args.add($ai.text);})* ) block)
	{ $value = executor.defineFunction($id.text, args, $block.value); }
	;

callExpression
	: ^( CALL leftHandSideExpression ^( ARGS expr* ) )
	;
	
memberExpression returns [Statement value]
	: ^( BYINDEX leftHandSideExpression expression)
	{ $value = executor.defineByIndex($leftHandSideExpression.value, $expression.value); }

	| ^( BYFIELD leftHandSideExpression Identifier )
	{ $value = executor.resolveByField($leftHandSideExpression.value, $Identifier.text); }
	;

primaryExpression returns [Statement value]
	: id=Identifier
	{ $value = executor.resolveIdentifier($id); }
	| literal
	{ $value = $literal.value;  }
	;

literal returns [Statement value]
	: THIS
	{ $value = executor.defineThisLiteral();  }
	| NULL
	{ $value = executor.defineNullLiteral();  }
	| booleanLiteral
	{ $value = $booleanLiteral.value;  }
	| numericLiteral
	{ $value = $numericLiteral.value;  }
	| StringLiteral
	{ $value = executor.defineStringLiteral($StringLiteral.text);  }
	| RegularExpressionLiteral
	{ $value = executor.defineRegExLiteral($RegularExpressionLiteral.text);  }
	| arrayLiteral
	| objectLiteral
	;

booleanLiteral returns [Statement value]
	: TRUE
	{ $value = executor.defineTrueLiteral();  }
	| FALSE
	{ $value = executor.defineFalseLiteral();  }
	;

numericLiteral returns [Statement value]
	: DecimalLiteral
	{ $value = executor.defineNumberLiteral($DecimalLiteral.text);  }
	| OctalIntegerLiteral
	{ $value = executor.defineOctalLiteral($OctalIntegerLiteral.text);  }
	| HexIntegerLiteral
	{ $value = executor.defineHexaLiteral($HexIntegerLiteral.text);  }
	;

arrayLiteral
	: ^( ARRAY ( ^( ITEM expr? ) )* )
	;

objectLiteral
	: ^( OBJECT ( ^( NAMEDVALUE propertyName expr) )* )
	;

propertyName
	: Identifier
	| StringLiteral
	| numericLiteral
	;
