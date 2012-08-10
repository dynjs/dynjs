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
	    {  $value = executor.expressionStatement( $expression.value);   }
	| printStatement
        { $value = $printStatement.value; }
	| ifStatement
        { $value = $ifStatement.value; }
	| doStatement
        { $value = $doStatement.value; }
	| whileStatement
        { $value = $whileStatement.value; }
	| forStatement
        { $value = $forStatement.value; }
	| continueStatement
        { $value = $continueStatement.value; }
	| breakStatement
        { $value = $breakStatement.value; }
	| returnStatement
        { $value = $returnStatement.value; }
	| withStatement
        { $value = $withStatement.value; }
	| labelledStatement
        { $value = $labelledStatement.value; }
	| switchStatement
        { $value = $switchStatement.value; }
	| throwStatement
        { $value = $throwStatement.value; }
	| tryStatement
        { $value = $tryStatement.value; }
	;

block returns [Statement value]
@init { List<Statement> blockContent = new ArrayList<Statement>(); }
	: ^( BLOCK (st=statement {blockContent.add($st.value);})* )
	{  $value = executor.block($BLOCK, blockContent);  }
	;

printStatement returns [Statement value]
	: ^( SK_PRINT expression )
	{  $value = executor.printStatement($SK_PRINT, $expression.value);  }
	;

variableDeclaration returns [VariableDeclarationStatement value]
@init { List<VariableDeclarationExpression> decls = new ArrayList<VariableDeclarationExpression>(); }
	: ^( VAR
	      ( id=Identifier
	{   decls.add( executor.variableDeclarationExpression($id, $id.text, null) );   }
	      | ^( ASSIGN id=Identifier expr )
	{   decls.add( executor.variableDeclarationExpression($id, $ASSIGN.text, $expr.value);   }
	      )+
	   ) { $value = execute.variableDeclarationStatement( $VAR, decls ); }
	;

ifStatement returns [Statement value]
	: ^( IF vbool=expression vthen=statement velse=statement? )
    { $value = executor.ifStatement($IF, $vbool.value, $vthen.value, $velse.value); }
	;

doStatement returns [Statement value]
	: ^( DO vloop=statement vbool=expression )
    { $value = executor.doStatement($DO, $vbool.value, $vloop.value); }
	;

whileStatement returns [Statement value]
	: ^( WHILE vbool=expression vloop=statement )
    { $value = executor.whileStatement($WHILE, $vbool.value, $vloop.value); }
	;

forStatement returns [Statement value]
@init { boolean isStep = false; boolean isIter = false; boolean isVar = false; }
	: ^( FOR
         (   ^( FORSTEP ( stepOpt1=exprOptClause | stepVar=variableDeclaration  {isVar = true;} ) stepOpt2=exprOptClause stepOpt3=exprOptClause )
         {  isStep = true;   }
         |   ^( FORITER ( iterExpr1=exprClause | iterVar=variableDeclaration  {isVar = true;} ) iterExpr2=exprClause )
         {  isIter = true;   }
         )
         statement )
    {
        if (isStep && isVar) {
            $value = executor.forStepVar($FORSTEP, $stepVar.value, $stepOpt2.value, $stepOpt3.value, $statement.value);
        } else if (isStep && !isVar) {
            $value = executor.forStepExpr($FORSTEP, $stepOpt1.value, $stepOpt2.value, $stepOpt3.value, $statement.value);
        } else if (isIter && isVar) {
            $value = executor.forIterVar($FORITER, $iterVar.value, $iterExpr2.value, $statement.value);
        } else if (isStep && !isVar) {
            $value = executor.forIterExpr($FORITER, $iterExpr1.value, $iterExpr2.value, $statement.value);
        }
    }
    ;

exprOptClause returns [Statement value]
	: ^( EXPR expression? )
	{ $value = $expression.value; }
	;

exprClause returns [Statement value]
	: ^( EXPR expression )
	{ $value = $expression.value; }
	;

continueStatement returns [Statement value]
	: ^( CONTINUE Identifier? )
    { $value = executor.continueStatement($CONTINUE, $Identifier.text); }
	;

breakStatement returns [Statement value]
	: ^( BREAK Identifier? )
    { $value = executor.breakStatement($BREAK, $Identifier.text); }
	;

returnStatement returns [Statement value]
	: ^( RETURN expression? )
    { $value = executor.returnStatement($RETURN, $expression.value); }
	;

withStatement returns [Statement value]
	: ^( WITH expression statement )
    { $value = executor.withStatement($WITH, $expression.value, $statement.value); }
	;

labelledStatement returns [Statement value]
	: ^( LABELLED Identifier statement )
    { $value = executor.labelledStatement($LABELLED, $Identifier.text, $statement.value); }
	;

switchStatement returns [Statement value]
@init { List<Statement> cases = new ArrayList<Statement>(); }
	: ^( SWITCH expression defaultClause? (caseClause { cases.add($caseClause.value); } )* )
    { $value = executor.switchStatement($SWITCH, $expression.value, $defaultClause.value, cases); }
	;

defaultClause returns [Statement value]
@init { List<Statement> statements = new ArrayList<Statement>(); }
	: ^( DEFAULT (statement { statements.add($statement.value); } )* )
    { $value = executor.switchDefaultClause($DEFAULT, statements); }
	;

caseClause returns [Statement value]
@init { List<Statement> statements = new ArrayList<Statement>(); }
	: ^( CASE expression (statement { statements.add($statement.value); } )* )
    { $value = executor.switchCaseClause($CASE, $expression.value, statements); }
	;

throwStatement returns [Statement value]
	: ^( THROW expression )
    { $value = executor.throwStatement($THROW, $expression.value); }
	;

tryStatement returns [Statement value]
	: ^( TRY block catchClause? finallyClause? )
    { $value = executor.tryStatement($TRY, $block.value, $catchClause.value, $finallyClause.value); }
	;
	
catchClause returns [Statement value]
	: ^( CATCH Identifier block )
    { $value = executor.tryCatchClause($CATCH, $Identifier.text, $block.value); }
	;
	
finallyClause returns [Statement value]
	: ^( FINALLY block )
    { $value = executor.tryFinallyClause($FINALLY, $block.value); }
	;

expression returns [Statement value]
@init { List<Statement> exprList = new ArrayList<Statement>(); }
	: expr
	{ $value = $expr.value; }
	| ^( CEXPR (expr {exprList.add($expr.value);})+ )
    { $value = executor.exprListStatement(exprList);   }
	;

expr returns [Statement value]
	: leftHandSideExpression
	{ $value = $leftHandSideExpression.value; }

	// Assignment operators
	| ^( ASSIGN l=expr r=expr )
    { $value = executor.defineAssOp($ASSIGN, $l.value, $r.value); }
	| ^( MULASS l=expr r=expr )
    { $value = executor.defineMulAssOp($MULASS, $l.value, $r.value); }
	| ^( DIVASS l=expr r=expr )
    { $value = executor.defineDivAssOp($DIVASS, $l.value, $r.value); }
	| ^( MODASS l=expr r=expr )
    { $value = executor.defineModAssOp($MODASS, $l.value, $r.value); }
	| ^( ADDASS l=expr r=expr )
    { $value = executor.defineAddAssOp($ADDASS, $l.value, $r.value); }
	| ^( SUBASS l=expr r=expr )
    { $value = executor.defineSubAssOp($SUBASS, $l.value, $r.value); }
	| ^( SHLASS l=expr r=expr )
    { $value = executor.defineShlAssOp($SHLASS, $l.value, $r.value); }
	| ^( SHRASS l=expr r=expr )
    { $value = executor.defineShrAssOp($SHRASS, $l.value, $r.value); }
	| ^( SHUASS l=expr r=expr )
    { $value = executor.defineShuAssOp($SHUASS, $l.value, $r.value); }
	| ^( ANDASS l=expr r=expr )
    { $value = executor.defineAndAssOp($ANDASS, $l.value, $r.value); }
	| ^( XORASS l=expr r=expr )
    { $value = executor.defineXorAssOp($XORASS, $l.value, $r.value); }
	| ^( ORASS l=expr r=expr )
    { $value = executor.defineOrAssOp($ORASS, $l.value, $r.value); }

	// Conditional operator
	| ^( QUE ex1=expr ex2=expr ex3=expr )
    { $value = executor.defineQueOp($QUE, $ex1.value, $ex2.value, $ex3.value); }

	// Logical operators
	| ^( LOR l=expr r=expr )
    { $value = executor.defineLorOp($LOR, $l.value, $r.value); }
	| ^( LAND l=expr r=expr )
    { $value = executor.defineLandOp($LAND, $l.value, $r.value); }

	// Binary bitwise operators
	| ^( AND l=expr r=expr )
    { $value = executor.defineAndBitOp($AND, $l.value, $r.value); }
	| ^( OR l=expr r=expr )
    { $value = executor.defineOrBitOp($OR, $l.value, $r.value); }
	| ^( XOR l=expr r=expr )
    { $value = executor.defineXorBitOp($XOR, $l.value, $r.value); }

	// Equality operators
	| ^( EQ l=expr r=expr )
    { $value = executor.defineEqOp($EQ, $l.value, $r.value); }
	| ^( NEQ l=expr r=expr )
    { $value = executor.defineNEqOp($NEQ, $l.value, $r.value); }
	| ^( SAME l=expr r=expr )
    { $value = executor.defineSameOp($SAME, $l.value, $r.value); }
	| ^( NSAME l=expr r=expr )
    { $value = executor.defineNSameOp($NSAME, $l.value, $r.value); }

	// Relational operator
	| ^( LT l=expr r=expr )
    { $value = executor.defineLtRelOp($LT, $l.value, $r.value); }
	| ^( GT l=expr r=expr )
    { $value = executor.defineGtRelOp($GT, $l.value, $r.value); }
	| ^( LTE l=expr r=expr )
    { $value = executor.defineLteRelOp($LTE, $l.value, $r.value); }
	| ^( GTE l=expr r=expr )
    { $value = executor.defineGteRelOp($GTE, $l.value, $r.value); }
	| ^( INSTANCEOF l=expr r=expr )
    { $value = executor.defineInstanceOfRelOp($INSTANCEOF, $l.value, $r.value); }
	| ^( IN l=expr r=expr )
    { $value = executor.defineInRelOp($IN, $l.value, $r.value); }

	// Bitwise shift operator
	| ^( SHL l=expr r=expr )
    { $value = executor.defineShlOp($SHL, $l.value, $r.value); }
	| ^( SHR l=expr r=expr )
    { $value = executor.defineShrOp($SHR, $l.value, $r.value); }
	| ^( SHU l=expr r=expr )
    { $value = executor.defineShuOp($SHU, $l.value, $r.value); }

	// Additive operators
	| ^( ADD l=expr r=expr )
    { $value = executor.defineAddOp($ADD, $l.value, $r.value); }
	| ^( SUB l=expr r=expr )
    { $value = executor.defineSubOp($SUB, $l.value, $r.value); }

	// Multipiclative operators
	| ^( MUL l=expr r=expr )
    { $value = executor.defineMulOp($MUL, $l.value, $r.value); }
	| ^( DIV l=expr r=expr )
    { $value = executor.defineDivOp($DIV, $l.value, $r.value); }
	| ^( MOD l=expr r=expr )
    { $value = executor.defineModOp($MOD, $l.value, $r.value); }

	// Unary operator
	| ^( DELETE ex=expr )
    { $value = executor.defineDeleteOp($DELETE, $ex.value); }
	| ^( VOID ex=expr )
    { $value = executor.defineVoidOp($VOID, $ex.value); }
	| ^( TYPEOF ex=expr )
    { $value = executor.defineTypeOfOp($TYPEOF, $ex.value); }
	| ^( INC ex=expr )
    { $value = executor.defineIncOp($INC, $ex.value); }
	| ^( DEC ex=expr )
    { $value = executor.defineDecOp($DEC, $ex.value); }
	| ^( POS ex=expr )
    { $value = executor.definePosOp($POS, $ex.value); }
	| ^( NEG ex=expr )
    { $value = executor.defineNegOp($NEG, $ex.value); }
	| ^( INV ex=expr )
    { $value = executor.defineInvOp($INV, $ex.value); }
	| ^( NOT ex=expr )
    { $value = executor.defineNotOp($NOT, $ex.value); }

	// Postfix operators
	| ^( PINC ex=expr )
    { $value = executor.definePIncOp($PINC, $ex.value); }
	| ^( PDEC ex=expr )
    { $value = executor.definePDecOp($PDEC, $ex.value); }
	;

leftHandSideExpression returns [Statement value]
	: primaryExpression
	{ $value = $primaryExpression.value;  }
	| newExpression
	{ $value = $newExpression.value;  }
	| functionDeclaration
	{ $value = $functionDeclaration.value;  }
	| callExpression
	{ $value = $callExpression.value;  }
	| memberExpression
	{ $value = $memberExpression.value;  }
	;

newExpression returns [Statement value]
	: ^( NEW leftHandSideExpression )
	{ $value = executor.executeNew($NEW, $leftHandSideExpression.value); }
	;

functionDeclaration returns [Statement value]
@init { List<String> args = new ArrayList<String>(); }
	: ^( FUNCTION id=Identifier? ^( ARGS (ai=Identifier {args.add($ai.text);})* ) block)
	{ $value = executor.defineFunction($FUNCTION, $id.text, args, $block.value); }
	;

callExpression returns [Expression value]
@init { List<Statement> args = new ArrayList<Statement>(); }
	: ^( CALL leftHandSideExpression ^( ARGS (expr { args.add($expr.value); } )* ) )
	{ $value = executor.resolveCallExpr($CALL, $leftHandSideExpression.value, args);  }
	;
	
memberExpression returns [Expression value]
	: ^( BYINDEX leftHandSideExpression expression)
	{ $value = executor.resolveByIndex($BYINDEX, $leftHandSideExpression.value, $expression.value); }
	| ^( BYFIELD leftHandSideExpression Identifier )
	{ $value = executor.resolveByField($BYFIELD, $leftHandSideExpression.value, $Identifier, $Identifier.text); }
	;

primaryExpression returns [Expression value]
	: id=Identifier
	{ $value = executor.resolveIdentifier($id, $id.text); }
	| literal
	{ $value = $literal.value;  }
	;

literal returns [Expression value]
	: THIS
	{ $value = executor.defineThisLiteral($THIS);  }
	| NULL
	{ $value = executor.defineNullLiteral($NULL);  }
	| booleanLiteral
	{ $value = $booleanLiteral.value;  }
	| numericLiteral
	{ $value = $numericLiteral.value;  }
	| StringLiteral
	{ $value = executor.defineStringLiteral($StringLiteral, $StringLiteral.text);  }
	| RegularExpressionLiteral
	{ $value = executor.defineRegExLiteral($RegularExpressionLiteral);  }
	| arrayLiteral
	{ $value = $arrayLiteral.value;  }
	| objectLiteral
	{ $value = $objectLiteral.value;  }
	;

booleanLiteral returns [Expression value]
	: TRUE
	{ $value = executor.defineTrueLiteral($TRUE);  }
	| FALSE
	{ $value = executor.defineFalseLiteral($FALSE);  }
	;

numericLiteral returns [Expression value]
	: DecimalLiteral
	{ $value = executor.defineNumberLiteral($DecimalLiteral, $DecimalLiteral.text, 10);  }
	| OctalIntegerLiteral
	{ $value = executor.defineNumberLiteral($OctalIntegerLiteral, $OctalIntegerLiteral.text, 8);  }
	| HexIntegerLiteral
	{ $value = executor.defineNumberLiteral($HexIntegerLiteral, $HexIntegerLiteral.text, 16);  }
	;

arrayLiteral returns [Expression value]
@init { List<Statement> exprs = new ArrayList<Statement>(); }
	: ^( ARRAY ( ^( ITEM expr? { exprs.add($expr.value); } ) )* )
	{ $value = executor.arrayLiteral($ARRAY, exprs);  }
	;

objectLiteral returns [Expression value]
@init { List<Statement> namedValues = new ArrayList<Statement>(); }
	: ^( OBJECT
	    ( ^( NAMEDVALUE propertyName expr
	       { final Statement st = executor.namedValue($NAMEDVALUE, $propertyName.value, $expr.value); namedValues.add(st); }
	       ) )* )
	{ $value = executor.objectValue($OBJECT, namedValues);  }
	;

propertyName returns [Statement value]
	: Identifier
	{ $value = executor.propertyNameId($Identifier, $Identifier.text);  }
	| StringLiteral
	{ $value = executor.propertyNameString($StringLiteral, $StringLiteral.text);  }
	| numericLiteral
	{ $value = executor.propertyNameNumeric($numericLiteral.value);  }
	;
