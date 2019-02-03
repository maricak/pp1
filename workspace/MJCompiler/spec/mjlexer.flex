package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;
import org.apache.log4j.*;

%%

%{
	private Symbol createSymbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

    Logger log = Logger.getLogger(getClass());   

    public void report_error(String message, int line) {
    	StringBuilder msg = new StringBuilder("LEKSICKA GRESKA! [");
        msg.append(message).append(" na liniji ").append(line).append("]");
        log.error(msg.toString());
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
"int"                   { return createSymbol(sym.INT, yytext()); }
"bool"                  { return createSymbol(sym.BOOL, yytext()); }
"char"                  { return createSymbol(sym.CHAR, yytext()); }

// Null
"null"                  { return createSymbol(sym.NULL, yytext()); }

// Operators
"+"                     { return createSymbol(sym.ADD, yytext()); }
"-"                     { return createSymbol(sym.SUBTRACT, yytext()); }
"*"                     { return createSymbol(sym.MULTIPLY, yytext()); }
"/"                     { return createSymbol(sym.DIVIDE, yytext()); }
"%"                     { return createSymbol(sym.MODULO, yytext()); }
"=="                    { return createSymbol(sym.EQUAL, yytext()); }
"!="                    { return createSymbol(sym.NOT_EQUAL, yytext()); }
">"                     { return createSymbol(sym.GREATER, yytext()); }
">="                    { return createSymbol(sym.GREATER_EQUAL, yytext()); }
"<"                     { return createSymbol(sym.LESS, yytext()); }
"<="                    { return createSymbol(sym.LESS_EQUAL, yytext()); }
"&&"                    { return createSymbol(sym.AND, yytext()); }
"||"                    { return createSymbol(sym.OR, yytext()); }
"="                     { return createSymbol(sym.ASSIGN, yytext()); }
"++"                    { return createSymbol(sym.INCREMENT, yytext()); }
"--"                    { return createSymbol(sym.DECREMENT, yytext()); }

// Separators
";"                     { return createSymbol(sym.SEPARATOR, yytext()); }
","                     { return createSymbol(sym.COMMA, yytext()); }
"."                     { return createSymbol(sym.POINT, yytext()); }

// Enclosures
"("                     { return createSymbol(sym.LEFT_PARENTHESIS, yytext()); }
")"                     { return createSymbol(sym.RIGHT_PARENTHESIS, yytext()); }
"["                     { return createSymbol(sym.LEFT_BRACKET, yytext()); }
"]"                     { return createSymbol(sym.RIGHT_BRACKET, yytext()); }
"{"                     { return createSymbol(sym.LEFT_BRACE, yytext()); }
"}"                     { return createSymbol(sym.RIGHT_BRACE, yytext()); }

// Keywords
"program"               { return createSymbol(sym.PROGRAM, yytext()); }
"class"                 { return createSymbol(sym.CLASS, yytext()); }
"extends"               { return createSymbol(sym.EXTENDS, yytext()); }
"interface"             { return createSymbol(sym.INTERFACE, yytext()); }
"implements"            { return createSymbol(sym.IMPLEMENTS, yytext()); }
"enum"                  { return createSymbol(sym.ENUM, yytext()); }
"if"                    { return createSymbol(sym.IF, yytext()); }
"else"                  { return createSymbol(sym.ELSE, yytext()); }
"new"                   { return createSymbol(sym.NEW, yytext()); }
"for"                   { return createSymbol(sym.FOR, yytext()); }
"return"                { return createSymbol(sym.RETURN, yytext()); }
"break"                 { return createSymbol(sym.BREAK, yytext()); }
"continue"              { return createSymbol(sym.CONTINUE, yytext()); }
"print"                 { return createSymbol(sym.PRINT, yytext()); }
"read"                  { return createSymbol(sym.READ, yytext()); }
"void"                  { return createSymbol(sym.VOID, yytext()); }
"const"		            { return createSymbol(sym.CONST, yytext()); }

"chr" 		            { return createSymbol(sym.CHR, yytext()); }
"ord" 		            { return createSymbol(sym.ORD, yytext()); }
"len" 		            { return createSymbol(sym.LEN, yytext()); }

// Constants
[0-9]+                  { return createSymbol(sym.INT_VALUE, new Integer(yytext())); }
"'"[ -~]"'"             { return createSymbol(sym.CHAR_VALUE, new Integer(yytext().charAt(1))); }
"true"                  { return createSymbol(sym.BOOL_VALUE, new Integer(1)); }
"false"                 { return createSymbol(sym.BOOL_VALUE, new Integer(0)); }

// Identifiers
[a-zA-Z][_a-zA-Z0-9]* 	{ return createSymbol(sym.IDENTIFIER, yytext()); }

// Comments
"//" 		            { yybegin(COMMENT); }
<COMMENT> .             { yybegin(COMMENT); }
<COMMENT> "\r"          { yybegin(YYINITIAL); }
<COMMENT> "\n"          { yybegin(YYINITIAL); }

// Everything else is a syntax error
. { report_error(yytext(), yyline + 1); }