package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	private Symbol new_symbol(int type){
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	private Symbol new_symbol(int type, Object value){
		return new Symbol(type, yyline+1, yycolumn, value);
	}

	
%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%
/* BLANKS */
" "		{ }
"\b"	{ }
"\t"	{ }
"\r\n"	{ }
"\f"	{ }

/* KEYWORDS */
"program"	{ return new_symbol(sym.PROG, yytext()); 		}
"break"		{ return new_symbol(sym.BREAK, yytext()); 		}
																/* "enum"		{ return new_symbol(sym.ENUM, yytext()); 		} */
"class"		{ return new_symbol(sym.CLASS, yytext()); 		}
"abstract" 	{ return new_symbol(sym.ABSTRACT, yytext()); 	}
"else" 		{ return new_symbol(sym.ELSE, yytext()); 		}
"const" 	{ return new_symbol(sym.CONST, yytext()); 		}
"if" 		{ return new_symbol(sym.IF, yytext()); 			}
"new" 		{ return new_symbol(sym.NEW, yytext()); 		}
"print" 	{ return new_symbol(sym.PRINT, yytext()); 		}
"read" 		{ return new_symbol(sym.READ, yytext()); 		}
"return"	{ return new_symbol(sym.RETURN, yytext()); 		}
"void"		{ return new_symbol(sym.VOID, yytext()); 		}
"extends"	{ return new_symbol(sym.EXTENDS, yytext()); 	}
"continue"	{ return new_symbol(sym.CONTINUE, yytext()); 	}
"for"		{ return new_symbol(sym.FOR, yytext()); 		}
"length" 	{ return new_symbol(sym.LENGTH, yytext()); 		}
"findAny" 	{ return new_symbol(sym.FINDANY, yytext());		}
"map"		{ return new_symbol(sym.MAP, yytext());			}
																/* "switch" 	{ return new_symbol(sym.SWITCH, yytext()); 		} */
																/* "case" 		{ return new_symbol(sym.CASE, yytext()); 		} */

/* COMMENTS */
"//"	{yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

/* OPERATORS */
/* INC&DEC */
"++"		{ return new_symbol(sym.INC, yytext());		}
"--"		{ return new_symbol(sym.DEC, yytext());		}
/* ADDOP */
"+" 		{ return new_symbol(sym.PLUS, yytext()); 	}
"-" 		{ return new_symbol(sym.MINUS, yytext()); 	}
/* MULOP */
"*" 		{ return new_symbol(sym.MUL, yytext()); 	}
"/" 		{ return new_symbol(sym.DIV, yytext()); 	}
"%" 		{ return new_symbol(sym.MOD, yytext()); 	}
/* RELOP */
"=="		{ return new_symbol(sym.RELEQ, yytext()); 	}
"!="		{ return new_symbol(sym.RELNEQ, yytext()); 	}
">"			{ return new_symbol(sym.RELG, yytext()); 	}
">="		{ return new_symbol(sym.RELGE, yytext()); 	}
"<"			{ return new_symbol(sym.RELL, yytext()); 	}
"<="		{ return new_symbol(sym.RELLE, yytext()); 	}
/* LOGIC */
"&&"		{ return new_symbol(sym.AND, yytext());	  	}
"||"		{ return new_symbol(sym.OR, yytext());	  	}
/* ASIGN */
"=>"		{ return new_symbol(sym.ARROW, yytext());	}
"="			{ return new_symbol(sym.EQUAL, yytext());	}

/* BRACKETS */
"(" 		{ return new_symbol(sym.LPAREN, yytext()); 	}
")" 		{ return new_symbol(sym.RPAREN, yytext()); 	}
"{" 		{ return new_symbol(sym.LBRACE, yytext()); 	}
"}" 		{ return new_symbol(sym.RBRACE, yytext()); 	}
"["			{ return new_symbol(sym.LSQUARE, yytext());	}
"]"			{ return new_symbol(sym.RSQUARE, yytext());	}
/* SPECIAL SIGNS */
"?"			{ return new_symbol(sym.QMARK, yytext());}
":"			{ return new_symbol(sym.COLON, yytext());	}
";" 		{ return new_symbol(sym.SEMI, yytext()); 	}
"," 		{ return new_symbol(sym.COMMA, yytext()); 	}
"."			{ return new_symbol(sym.DOT, yytext());		}

/* TOKENS */
/* BOOLEAN */
"true"					{ return new_symbol(sym.BOOL, true);						}
"false" 				{ return new_symbol(sym.BOOL, false);						}
/* INTEGER */
[0-9]+			 		{ return new_symbol(sym.NUMBER, Integer.parseInt(yytext()));}
/* CHARS */
['].[']					{ return new_symbol(sym.CHAR, yytext().charAt(1));			}
/* IDS */
[a-zA-Z][a-zA-Z0-9_]*	{ return new_symbol(sym.IDENT, yytext());					}

/* ERROR */
. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + " kolona " + (yycolumn + 1)); }

