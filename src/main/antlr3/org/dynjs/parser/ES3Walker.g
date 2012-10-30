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

import org.dynjs.parser.ast.*;
}

@members {

    Program result = null;
    ASTFactory astFactory = null;

    public void setASTFactory(ASTFactory astFactory){
        this.astFactory = astFactory;
    }
    
    public Program getResult(){
        return result;
    }
}


/*
Note: functionDeclaration is reachable via statement->expression as functionExpression and functionDeclaration are combined.
*/
program
@init { 
  List<Statement> blockContent = new ArrayList<Statement>(); 
}
        : ^(p=PROGRAM 
             (st=statement 
               {blockContent.add($st.value);} )* ) 
        {   result = astFactory.program( ((JavascriptTree)p).isStrict(), blockContent);   }
	;

statement returns [Statement value] 
	: block
        {  $value = $block.value;   }
	| variableDeclaration
	    {  $value = $variableDeclaration.value;   }
	    /*
	| functionDeclaration
	    {  $value = astFactory.expressionStatement( $functionDeclaration.value );   }
	    */
	| expression
	    {  $value = astFactory.expressionStatement( $expression.value);   }
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
	{  $value = astFactory.block($BLOCK, blockContent);  }
	;

printStatement returns [Statement value]
	: ^( SK_PRINT expression )
	{  $value = astFactory.printStatement($SK_PRINT, $expression.value);  }
	;

variableDeclaration returns [VariableDeclarationStatement value]
@init { List<VariableDeclaration> decls = new ArrayList<VariableDeclaration>(); }
	: ^( VAR
	      ( id=Identifier
	{   decls.add( astFactory.variableDeclaration($id, $id.text, null) ); }
	      | ^( ASSIGN id=Identifier expr )
	{   decls.add( astFactory.variableDeclaration($id, $id.text, $expr.value) );   }
	      )+
	   ) { $value = astFactory.variableDeclarationStatement( $VAR, decls ); }
	;

ifStatement returns [Statement value]
	: ^( IF vbool=expression vthen=statement velse=statement? )
    { $value = astFactory.ifStatement($IF, $vbool.value, $vthen.value, $velse.value); }
	;

doStatement returns [Statement value]
	: ^( DO vloop=statement vbool=expression )
    { $value = astFactory.doStatement($DO, $vbool.value, $vloop.value); }
	;

whileStatement returns [Statement value]
	: ^( WHILE vbool=expression vloop=statement )
    { $value = astFactory.whileStatement($WHILE, $vbool.value, $vloop.value); }
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
            $value = astFactory.forStepVar($FORSTEP, $stepVar.value, $stepOpt2.value, $stepOpt3.value, $statement.value);
        } else if (isStep && !isVar) {
            $value = astFactory.forStepExpr($FORSTEP, $stepOpt1.value, $stepOpt2.value, $stepOpt3.value, $statement.value);
        } else if (isIter && isVar) {
            $value = astFactory.forIterVar($FORITER, $iterVar.value, $iterExpr2.value, $statement.value);
        } else if (isIter && !isVar) {
            $value = astFactory.forIterExpr($FORITER, $iterExpr1.value, $iterExpr2.value, $statement.value);
        }
    }
    ;

exprOptClause returns [Expression value]
	: ^( EXPR expression? )
	{ $value = $expression.value; }
	;

exprClause returns [Expression value]
	: ^( EXPR expression )
	{ $value = $expression.value; }
	;

continueStatement returns [Statement value]
	: ^( CONTINUE Identifier? )
    { $value = astFactory.continueStatement($CONTINUE, $Identifier.text); }
	;

breakStatement returns [Statement value]
	: ^( BREAK Identifier? )
    { $value = astFactory.breakStatement($BREAK, $Identifier.text); }
	;

returnStatement returns [Statement value]
	: ^( RETURN expression? )
    { $value = astFactory.returnStatement($RETURN, $expression.value); }
	;

withStatement returns [Statement value]
	: ^( WITH expression statement )
    { $value = astFactory.withStatement($WITH, $expression.value, $statement.value); }
	;

labelledStatement returns [Statement value]
	: ^( LABELLED Identifier statement )
    { $value = astFactory.labelledStatement($Identifier.text, $statement.value); }
	;

switchStatement returns [Statement value]
@init { List<CaseClause> caseClauses = new ArrayList<CaseClause>(); }
	: ^( SWITCH expression (cc=caseClause{ caseClauses.add( $cc.value); } |dc=defaultClause { caseClauses.add( $dc.value); } )* )
    { $value = astFactory.switchStatement($SWITCH, $expression.value, caseClauses ); }
	;

defaultClause returns [DefaultCaseClause value]
@init { List<Statement> statements = new ArrayList<Statement>(); }
	: ^( DEFAULT (statement { statements.add($statement.value); } )* )
    { $value = astFactory.switchDefaultClause($DEFAULT, statements); }
	;

caseClause returns [CaseClause value]
@init { List<Statement> statements = new ArrayList<Statement>(); }
	: ^( CASE expression (statement { statements.add($statement.value); } )* )
    { $value = astFactory.switchCaseClause($CASE, $expression.value, statements); }
	;

throwStatement returns [Statement value]
	: ^( THROW expression )
    { $value = astFactory.throwStatement($THROW, $expression.value); }
	;

tryStatement returns [TryStatement value]
	: ^( TRY block catchClause? finallyClause? )
    { $value = astFactory.tryStatement($TRY, $block.value, $catchClause.value, $finallyClause.value); }
	;
	
catchClause returns [CatchClause value]
	: ^( CATCH Identifier block )
    { $value = astFactory.tryCatchClause($CATCH, $Identifier.text, $block.value); }
	;
	
finallyClause returns [Statement value]
	: ^( FINALLY block )
    { $value = astFactory.tryFinallyClause($FINALLY, $block.value); }
	;

expression returns [Expression value]
@init { List<Expression> exprList = new ArrayList<Expression>(); }
	: expr
	{ $value = $expr.value; }
	| ^( CEXPR (expr {exprList.add($expr.value);})+ )
    { $value = astFactory.exprList(exprList);   }
	;

expr returns [Expression value]
	: leftHandSideExpression
	{ $value = $leftHandSideExpression.value; }

	// Assignment operators
	| ^( ASSIGN l=expr r=expr )
    { $value = astFactory.defineAssOp($ASSIGN, $l.value, $r.value); }
	| ^( MULASS l=expr r=expr )
    { $value = astFactory.defineMulAssOp($MULASS, $l.value, $r.value); }
	| ^( DIVASS l=expr r=expr )
    { $value = astFactory.defineDivAssOp($DIVASS, $l.value, $r.value); }
	| ^( MODASS l=expr r=expr )
    { $value = astFactory.defineModAssOp($MODASS, $l.value, $r.value); }
	| ^( ADDASS l=expr r=expr )
    { $value = astFactory.defineAddAssOp($ADDASS, $l.value, $r.value); }
	| ^( SUBASS l=expr r=expr )
    { $value = astFactory.defineSubAssOp($SUBASS, $l.value, $r.value); }
	| ^( SHLASS l=expr r=expr )
    { $value = astFactory.defineShlAssOp($SHLASS, $l.value, $r.value); }
	| ^( SHRASS l=expr r=expr )
    { $value = astFactory.defineShrAssOp($SHRASS, $l.value, $r.value); }
	| ^( SHUASS l=expr r=expr )
    { $value = astFactory.defineShuAssOp($SHUASS, $l.value, $r.value); }
	| ^( ANDASS l=expr r=expr )
    { $value = astFactory.defineAndAssOp($ANDASS, $l.value, $r.value); }
	| ^( XORASS l=expr r=expr )
    { $value = astFactory.defineXorAssOp($XORASS, $l.value, $r.value); }
	| ^( ORASS l=expr r=expr )
    { $value = astFactory.defineOrAssOp($ORASS, $l.value, $r.value); }

	// Conditional operator
	| ^( QUE ex1=expr ex2=expr ex3=expr )
    { $value = astFactory.defineQueOp($QUE, $ex1.value, $ex2.value, $ex3.value); }

	// Logical operators
	| ^( LOR l=expr r=expr )
    { $value = astFactory.defineLorOp($LOR, $l.value, $r.value); }
	| ^( LAND l=expr r=expr )
    { $value = astFactory.defineLandOp($LAND, $l.value, $r.value); }

	// Binary bitwise operators
	| ^( AND l=expr r=expr )
    { $value = astFactory.defineAndBitOp($AND, $l.value, $r.value); }
	| ^( OR l=expr r=expr )
    { $value = astFactory.defineOrBitOp($OR, $l.value, $r.value); }
	| ^( XOR l=expr r=expr )
    { $value = astFactory.defineXorBitOp($XOR, $l.value, $r.value); }

	// Equality operators
	| ^( EQ l=expr r=expr )
    { $value = astFactory.defineEqOp($EQ, $l.value, $r.value); }
	| ^( NEQ l=expr r=expr )
    { $value = astFactory.defineNEqOp($NEQ, $l.value, $r.value); }
	| ^( SAME l=expr r=expr )
    { $value = astFactory.defineSameOp($SAME, $l.value, $r.value); }
	| ^( NSAME l=expr r=expr )
    { $value = astFactory.defineNSameOp($NSAME, $l.value, $r.value); }

	// Relational operator
	| ^( LT l=expr r=expr )
    { $value = astFactory.defineLtRelOp($LT, $l.value, $r.value); }
	| ^( GT l=expr r=expr )
    { $value = astFactory.defineGtRelOp($GT, $l.value, $r.value); }
	| ^( LTE l=expr r=expr )
    { $value = astFactory.defineLteRelOp($LTE, $l.value, $r.value); }
	| ^( GTE l=expr r=expr )
    { $value = astFactory.defineGteRelOp($GTE, $l.value, $r.value); }
	| ^( INSTANCEOF l=expr r=expr )
    { $value = astFactory.defineInstanceOfRelOp($INSTANCEOF, $l.value, $r.value); }
	| ^( IN l=expr r=expr )
    { $value = astFactory.defineInRelOp($IN, $l.value, $r.value); }

	// Bitwise shift operator
	| ^( SHL l=expr r=expr )
    { $value = astFactory.defineShlOp($SHL, $l.value, $r.value); }
	| ^( SHR l=expr r=expr )
    { $value = astFactory.defineShrOp($SHR, $l.value, $r.value); }
	| ^( SHU l=expr r=expr )
    { $value = astFactory.defineShuOp($SHU, $l.value, $r.value); }

	// Additive operators
	| ^( ADD l=expr r=expr )
    { $value = astFactory.defineAddOp($ADD, $l.value, $r.value); }
	| ^( SUB l=expr r=expr )
    { $value = astFactory.defineSubOp($SUB, $l.value, $r.value); }

	// Multipiclative operators
	| ^( MUL l=expr r=expr )
    { $value = astFactory.defineMulOp($MUL, $l.value, $r.value); }
	| ^( DIV l=expr r=expr )
    { $value = astFactory.defineDivOp($DIV, $l.value, $r.value); }
	| ^( MOD l=expr r=expr )
    { $value = astFactory.defineModOp($MOD, $l.value, $r.value); }

	// Unary operator
	| ^( DELETE ex=expr )
    { $value = astFactory.defineDeleteOp($DELETE, $ex.value); }
	| ^( VOID ex=expr )
    { $value = astFactory.defineVoidOp($VOID, $ex.value); }
	| ^( TYPEOF ex=expr )
    { $value = astFactory.defineTypeOfOp($TYPEOF, $ex.value); }
	| ^( INC ex=expr )
    { $value = astFactory.definePreIncOp($INC, $ex.value); }
	| ^( DEC ex=expr )
    { $value = astFactory.definePreDecOp($DEC, $ex.value); }
	| ^( POS ex=expr )
    { $value = astFactory.definePosOp($POS, $ex.value); }
	| ^( NEG ex=expr )
    { $value = astFactory.defineNegOp($NEG, $ex.value); }
	| ^( INV ex=expr )
    { $value = astFactory.defineInvOp($INV, $ex.value); }
	| ^( NOT ex=expr )
    { $value = astFactory.defineNotOp($NOT, $ex.value); }

	// Postfix operators
	| ^( PINC ex=expr )
    { $value = astFactory.definePostIncOp($PINC, $ex.value); }
	| ^( PDEC ex=expr )
    { $value = astFactory.definePostDecOp($PDEC, $ex.value); }
	;

leftHandSideExpression returns [Expression value]
	: primaryExpression
	{ $value = $primaryExpression.value;  }
	| newExpression
	{ $value = $newExpression.value;  }
	| functionDescriptor
	{ $value = astFactory.functionExpression( $functionDescriptor.value );  }
	| callExpression
	{ $value = $callExpression.value;  }
	| memberExpression
	{ $value = $memberExpression.value;  }
	;

newExpression returns [NewOperatorExpression value]
@init { List<Expression> args = new ArrayList<Expression>(); }
	: ^( NEW leftHandSideExpression ( ^( ARGS (expr { args.add($expr.value); } )* ) )? )
	{ $value = astFactory.newOperatorExpression($NEW, $leftHandSideExpression.value, args); }
	;

functionDescriptor returns [FunctionDescriptor value]
@init { List<String> args = new ArrayList<String>(); }
	: ^( FUNCTION id=Identifier? ^( ARGS (ai=Identifier {args.add($ai.text);})* ) block)
	{ $value = astFactory.functionDescriptor($FUNCTION, $id.text, args, $block.value); }
	;

callExpression returns [Expression value]
@init { List<Expression> args = new ArrayList<Expression>(); }
	: ^( CALL expression ^( ARGS (expr { args.add($expr.value); } )* ) )
	{ $value = astFactory.resolveCallExpr($CALL, $expression.value, args);  }
	;
	
memberExpression returns [Expression value]
	: ^( BYINDEX leftHandSideExpression expression)
	{ $value = astFactory.memberExpression($BYINDEX, $leftHandSideExpression.value, $expression.value); }
	| ^( BYFIELD leftHandSideExpression Identifier )
	{ $value = astFactory.memberExpression($BYFIELD, $leftHandSideExpression.value, astFactory.defineStringLiteral( $Identifier, $Identifier.text ) ); }
	;

primaryExpression returns [Expression value]
	: id=Identifier
	{ $value = astFactory.resolveIdentifier($id, $id.text); }
	| literal
	{ $value = $literal.value;  }
	;

literal returns [Expression value]
	: THIS
	{ $value = astFactory.defineThisLiteral($THIS);  }
	| NULL
	{ $value = astFactory.defineNullLiteral($NULL);  }
	| booleanLiteral
	{ $value = $booleanLiteral.value;  }
	| numericLiteral
	{ $value = $numericLiteral.value;  }
	| StringLiteral
	{ $value = astFactory.defineStringLiteral($StringLiteral, $StringLiteral.text);  }
	| RegularExpressionLiteral
	{ $value = astFactory.defineRegExLiteral($RegularExpressionLiteral, $RegularExpressionLiteral.text);  }
	| arrayLiteral
	{ $value = $arrayLiteral.value;  }
	| objectLiteral
	{ $value = $objectLiteral.value;  }
	;

booleanLiteral returns [Expression value]
	: TRUE
	{ $value = astFactory.defineTrueLiteral($TRUE);  }
	| FALSE
	{ $value = astFactory.defineFalseLiteral($FALSE);  }
	;

numericLiteral returns [Expression value]
	: DecimalLiteral
	{ $value = astFactory.defineNumberLiteral($DecimalLiteral, $DecimalLiteral.text, 10);  }
	| OctalIntegerLiteral
	{ $value = astFactory.defineNumberLiteral($OctalIntegerLiteral, $OctalIntegerLiteral.text, 8);  }
	| HexIntegerLiteral
	{ $value = astFactory.defineNumberLiteral($HexIntegerLiteral, $HexIntegerLiteral.text, 16);  }
	;

arrayLiteral returns [Expression value]
@init { List<Expression> exprs = new ArrayList<Expression>(); }
	: ^( ARRAY ( ^( ITEM expr? { exprs.add($expr.value); } ) )* )
	{ $value = astFactory.arrayLiteral($ARRAY, exprs);  }
	;

objectLiteral returns [Expression value]
@init { List<PropertyAssignment> propAssignments = new ArrayList<PropertyAssignment>(); }
	: ^( OBJECT
	    ( ^( NAMEDVALUE pn1=propertyName expr
	       { final NamedValue st = astFactory.namedValue($NAMEDVALUE, $pn1.value, $expr.value); propAssignments.add(st); }
	       ) 
	       |
	      ^( PROPERTYGET pn2=propertyName b1=block )
	       { final PropertyGet pg = astFactory.propertyGet($PROPERTYGET, $pn2.value, $b1.value ); propAssignments.add(pg); }
	      |
	      ^( PROPERTYSET pn3=propertyName Identifier b2=block )
	       { final PropertySet ps = astFactory.propertySet($PROPERTYSET, $pn3.value, $Identifier.text, $b2.value ); propAssignments.add(ps); }
	    )* )
	{ $value = astFactory.objectValue($OBJECT, propAssignments);  }
	;

propertyName returns [String value]
	: Identifier
	{ $value = $Identifier.text;  }
	| StringLiteral
	{ $value = $StringLiteral.text;  }
	| numericLiteral
	{ $value = ((NumberLiteralExpression)$numericLiteral.value).getText();  }
	;
