package compiler.lexanal;

import compiler.report.*;
import compiler.synanal.*;

%%


%class	  PascalLex
%public

%line
%column

/* Vzpostavimo zdruzljivost z orodjem Java Cup.
 * To bi lahko naredili tudi z ukazom %cup,
 * a v tem primeru ne bi mogli uporabiti razreda compiler.lexanal.PascalSym
 * namesto razreda java_cup.runtime.Symbol za opis osnovnih simbolov. */
%cupsym	 compiler.synanal.PascalTok
%implements java_cup.runtime.Scanner
%function   next_token
%type	   PascalSym
%eofval{
	return new PascalSym(PascalTok.EOF);
%eofval}
%eofclose

%state COMMENT, YYINITIAL

%{
	int nesting = 0, lbracketStartLine = 0, lbracketStartColumn = 0;
	
	private PascalSym sym(int type) {
		return new PascalSym(type, yyline + 1, yycolumn + 1, yytext());
	}
%}

%eof{
	if(nesting > 0) Report.warning("Unclosed comment "+yytext(), lbracketStartLine + 1, lbracketStartColumn + 1);
%eof}

%%
<YYINITIAL> {


	[ \n\t]+						{ }
	
	// nesting:
	
	"{"  							{ nesting = 1; lbracketStartLine = yyline; lbracketStartColumn = yycolumn; yybegin(COMMENT); }
	
	// other symbols:
	
	":=" 							{ return sym(PascalTok.ASSIGN); }
	":" 							{ return sym(PascalTok.COLON); }
	"," 							{ return sym(PascalTok.COMMA); }
	"." 							{ return sym(PascalTok.DOT); }
	".." 							{ return sym(PascalTok.DOTS); }
	"[" 							{ return sym(PascalTok.LBRACKET); }
	"(" 							{ return sym(PascalTok.LPARENTHESIS); }
	"]" 							{ return sym(PascalTok.RBRACKET); }
	")" 							{ return sym(PascalTok.RPARENTHESIS); }
	";" 							{ return sym(PascalTok.SEMIC); }
	"+" 							{ return sym(PascalTok.ADD); }
	"=" 							{ return sym(PascalTok.EQU); }
	">=" 							{ return sym(PascalTok.GEQ); }
	">" 							{ return sym(PascalTok.GTH); }
	"<" 							{ return sym(PascalTok.LTH); }
	"<=" 							{ return sym(PascalTok.LEQ); }
	"*" 							{ return sym(PascalTok.MUL); }
	"<>" 							{ return sym(PascalTok.NEQ); }
	"^" 							{ return sym(PascalTok.PTR); }
	"-" 							{ return sym(PascalTok.SUB); }
	
	
	"else" 							{ return sym(PascalTok.ELSE); }
	"end"							{ return sym(PascalTok.END); }
	"for"							{ return sym(PascalTok.FOR); }
	"function"						{ return sym(PascalTok.FUNCTION); }
	"if"							{ return sym(PascalTok.IF); }
	"nil"							{ return sym(PascalTok.NIL); }
	"not"							{ return sym(PascalTok.NOT); }
	"of"							{ return sym(PascalTok.OF); }
	"or"							{ return sym(PascalTok.OR); }
	"procedure"						{ return sym(PascalTok.PROCEDURE); }
	"program"						{ return sym(PascalTok.PROGRAM); }
	"record"						{ return sym(PascalTok.RECORD); }
	"then"							{ return sym(PascalTok.THEN); }
	"to" 							{ return sym(PascalTok.TO); }
	"type"							{ return sym(PascalTok.TYPE); }
	"var"							{ return sym(PascalTok.VAR); }
	"while"							{ return sym(PascalTok.WHILE); }
	"do"							{ return sym(PascalTok.DO); }
	"div"							{ return sym(PascalTok.DIV); }
	"const"							{ return sym(PascalTok.CONST); }
	"begin"							{ return sym(PascalTok.BEGIN); }
	"array"							{ return sym(PascalTok.ARRAY); }
	"and"							{ return sym(PascalTok.AND); }
	"repeat"                        { return sym(PascalTok.REPEAT); }
	"until"                         { return sym(PascalTok.UNTIL); }
	
	"integer"						{ return sym(PascalTok.INT); }
	"boolean"						{ return sym(PascalTok.BOOL); }
	"char"							{ return sym(PascalTok.CHAR); }
	
	// konstante atomarnih podatkovnih tipov:
	"true"|"false"					{ return sym(PascalTok.BOOL_CONST); }
	\'([^\']|\'\')\'				{ return sym(PascalTok.CHAR_CONST); } //System.out.println(yytext());  
	[0-9]+							{ return sym(PascalTok.INT_CONST); }  
	
	// imena programov, konstant, tipov, spremenljivk in podprogramov:
	
	[A-Za-z_][A-Za-z_0-9]*			{ return sym(PascalTok.IDENTIFIER); }
	
	.								{ Report.warning("Unknown character: "+yytext(), yyline + 1, yycolumn + 1); }
}

<COMMENT> {
	"{"							{ nesting++; }
	"}"							{ nesting--; if (nesting == 0) yybegin(YYINITIAL); }
	.|\n						{ }
}