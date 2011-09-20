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
	| printStatement
        { $value = $printStatement.value; }
	| ifStatement
	| doStatement
	| whileStatement
	| forStatement
	| continueStatement
	| breakStatement
	| returnStatement
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

ifStatement
	: ^( IF expression statement+ )
	;

doStatement
	: ^( DO statement expression )
	;

whileStatement
	: ^( WHILE expression statement )
	;

forStatement
	: ^(
	FOR 
	(
		^( FORSTEP ( exprOptClause | variableDeclaration ) exprOptClause exprOptClause )
		| ^( FORITER ( exprClause | variableDeclaration ) exprClause )
	)
	statement
	);

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

returnStatement
	: ^( RETURN expression? )
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
	| ^( PAREXPR e=expr )
	{ $value = $e.value; }

	// Assignment operators
	| ^( ASSIGN expr expr )
	| ^( MULASS expr expr )
	| ^( DIVASS expr expr )
	| ^( MODASS expr expr )
	| ^( ADDASS expr expr )
	| ^( SUBASS expr expr )
	| ^( SHLASS expr expr )
	| ^( SHRASS expr expr )
	| ^( SHUASS expr expr )
	| ^( ANDASS expr expr )
	| ^( XORASS expr expr )
	| ^( ORASS expr expr )
	
	// Conditional operator
	| ^( QUE expr expr expr )
	
	// Logical operators
	| ^( LOR expr expr )
	| ^( LAND expr expr )
	
	// Binary bitwise operators
	| ^( AND expr expr )
	| ^( OR expr expr )
	| ^( XOR expr expr )
	
	// Equality operators
	| ^( EQ expr expr )
	| ^( NEQ expr expr )
	| ^( SAME expr expr )
	| ^( NSAME expr expr )
	
	// Relational operator
	| ^( LT expr expr )
	| ^( GT expr expr )
	| ^( LTE expr expr )
	| ^( GTE expr expr )
	| ^( INSTANCEOF expr expr )
	| ^( IN expr expr )
	
	// Bitwise shift operator
	| ^( SHL expr expr )
	| ^( SHR expr expr )
	| ^( SHU expr expr )
	
	// Additive operators
	| ^( ADD l=expr r=expr )
    { $value = executor.defineAddOp($l.value, $r.value); }
	| ^( SUB l=expr r=expr )
    { $value = executor.defineSubOp($l.value, $r.value); }

	// Multipiclative operators
	| ^( MUL l=expr r=expr )
    { $value = executor.defineMulOp($l.value, $r.value); }
	| ^( DIV expr expr )
	| ^( MOD expr expr )
	
	// Unary operator
	| ^( DELETE expr )
	| ^( VOID expr )
	| ^( TYPEOF expr )
	| ^( INC expr )
	| ^( DEC expr )
	| ^( POS expr )
	| ^( NEG expr )
	| ^( INV expr )
	| ^( NOT expr )
	
	// Postfix operators
	| ^( PINC expr )
	| ^( PDEC expr )
	;

leftHandSideExpression returns [Statement value]
	: primaryExpression
	{ $value = $primaryExpression.value;  }
	| newExpression
	| functionDeclaration
	| callExpression
	| memberExpression
	;

newExpression
	: ^( NEW leftHandSideExpression )
	;

functionDeclaration
	: ^( FUNCTION Identifier? ^( ARGS Identifier* ) block )
	;

callExpression
	: ^( CALL leftHandSideExpression ^( ARGS expr* ) )
	;
	
memberExpression
	: ^( BYINDEX leftHandSideExpression expression )
	| ^( BYFIELD leftHandSideExpression Identifier )
	;

primaryExpression returns [Statement value]
	: Identifier
	| literal
	{ $value = $literal.value;  }
	;

literal returns [Statement value]
	: THIS
	| NULL
	| booleanLiteral
	| numericLiteral
	{ $value = $numericLiteral.value;  }
	| StringLiteral
	{ $value = executor.defineStringLiteral($StringLiteral.text);  }
	| RegularExpressionLiteral
	| arrayLiteral
	| objectLiteral
	;

booleanLiteral
	: TRUE
	| FALSE
	;

numericLiteral returns [Statement value]
	: DecimalLiteral
	{ $value = executor.defineNumberLiteral($DecimalLiteral.text);  }
	| OctalIntegerLiteral
	{ $value = executor.defineOctalLiteral($OctalIntegerLiteral.text);  }
	| HexIntegerLiteral
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
