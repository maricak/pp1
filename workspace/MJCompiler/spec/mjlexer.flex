package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{
	private Symbol createSymbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	 return createSymbol(sym.EOF, "eof");
%eofval}

%%

// Whitespace
[\t\r\n \b\f]+ { }

// Types
"int"       { return createSymbol(sym.INT, yytext()); }
"bool"      { return createSymbol(sym.BOOL, yytext()); }
"char"      { return createSymbol(sym.CHAR, yytext()); }

// Null
"null"      { return createSymbol(sym.NULL, yytext()); }

// Operators

// Arithmetic
"+"         { return createSymbol(sym.ADD, yytext()); }
"-"         { return createSymbol(sym.SUBTRACT, yytext()); }
"*"         { return createSymbol(sym.MULTIPLY, yytext()); }
"/"         { return createSymbol(sym.DIVIDE, yytext()); }
"%"         { return createSymbol(sym.MODULO, yytext()); }

// Relational
"=="        { return createSymbol(sym.EQUAL, yytext()); }
"!="        { return createSymbol(sym.NOT_EQUAL, yytext()); }
">"         { return createSymbol(sym.GREATER, yytext()); }
">="        { return createSymbol(sym.GREATER_EQUAL, yytext()); }
"<"         { return createSymbol(sym.LESS, yytext()); }
"<="        { return createSymbol(sym.LESS_EQUAL, yytext()); }

// Logical
"&&"        { return createSymbol(sym.AND, yytext()); }
"||"        { return createSymbol(sym.OR, yytext()); }

// Assignment
"="         { return createSymbol(sym.ASSIGN, yytext()); }

// Increment and decrement
"++"        { return createSymbol(sym.INCREMENT, yytext()); }
"--"        { return createSymbol(sym.DECREMENT, yytext()); }

// Separators
";"         { return createSymbol(sym.SEPARATOR, yytext()); }
","         { return createSymbol(sym.COMMA, yytext()); }
"."         { return createSymbol(sym.POINT, yytext()); }

// Enclosures
"("         { return createSymbol(sym.LEFT_PARENTHESIS, yytext()); }
")"         { return createSymbol(sym.RIGHT_PARENTHESIS, yytext()); }
"["         { return createSymbol(sym.LEFT_BRACKET, yytext()); }
"]"         { return createSymbol(sym.RIGHT_BRACKET, yytext()); }
"{"         { return createSymbol(sym.LEFT_BRACE, yytext()); }
"}"         { return createSymbol(sym.RIGHT_BRACE, yytext()); }

// Keywords
"program"   { return createSymbol(sym.PROGRAM, yytext()); }
"class"     { return createSymbol(sym.CLASS, yytext()); }
"extends"   { return createSymbol(sym.EXTENDS, yytext()); }
"interface" {return createSymbol(sym.INTERFACE, yytext()); }
"implements" {return createSymbol(sym.IMPLEMENTS, yytext()); }
"enum"        { return createSymbol(sym.ENUM, yytext()); }
"if"        { return createSymbol(sym.IF, yytext()); }
"else"      { return createSymbol(sym.ELSE, yytext()); }
"new"       { return createSymbol(sym.NEW, yytext()); }
"for"        { return createSymbol(sym.FOR, yytext()); }
"return"    { return createSymbol(sym.RETURN, yytext()); }
"break"     { return createSymbol(sym.BREAK, yytext()); }
"continue"  { return createSymbol(sym.CONTINUE, yytext()); }
"print"     { return createSymbol(sym.PRINT, yytext()); }
"read"      { return createSymbol(sym.READ, yytext()); }
"void"      { return createSymbol(sym.VOID, yytext()); }
"const"		{ return createSymbol(sym.CONST, yytext()); }

//"chr" 		{ return createSymbol(sym.CHR, yytext()); }
//"ord" 		{ return createSymbol(sym.ORD, yytext()); }
//"len" 		{ return createSymbol(sym.LEN, yytext()); }

// Constants
[0-9]+                  { return createSymbol(sym.INT_VALUE, new Integer (yytext())); }
"'"[ -~]"'"             { return createSymbol(sym.CHAR_VALUE, yytext().charAt(1)); }
"true"                  { return createSymbol(sym.BOOL_VALUE, true); }
"false"                 { return createSymbol(sym.BOOL_VALUE, false); }

// Identifiers
[a-zA-Z][_a-zA-Z0-9]* 	{ return createSymbol(sym.IDENTIFIER, yytext()); }

// Comments
"//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r"   { yybegin(YYINITIAL); }
<COMMENT> "\n"   { yybegin(YYINITIAL); }

// Everything else is a syntax error
. { return createSymbol(sym.ERROR, "Line " + (yyline + 1) + ", syntax error: '" + yytext() + "'"); }