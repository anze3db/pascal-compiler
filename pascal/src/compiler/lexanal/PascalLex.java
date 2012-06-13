/* The following code was generated by JFlex 1.4.3 on 6/13/12 6:19 PM */

package compiler.lexanal;

import java.io.*;

import compiler.report.*;
import compiler.synanal.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 6/13/12 6:19 PM from the specification file
 * <tt>pascal.jflex</tt>
 */
public class PascalLex implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int COMMENT = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\1\1\1\25\0\1\1\6\0\1\47\1\10\1\12\1\17"+
    "\1\14\1\5\1\21\1\6\1\0\12\50\1\3\1\13\1\16\1\4"+
    "\1\15\2\0\32\51\1\7\1\0\1\11\1\20\1\51\1\0\1\40"+
    "\1\46\1\33\1\26\1\22\1\27\1\37\1\42\1\35\2\51\1\23"+
    "\1\41\1\25\1\30\1\36\1\51\1\31\1\24\1\34\1\32\1\44"+
    "\1\45\1\51\1\43\1\51\1\2\1\0\1\52\uff82\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7"+
    "\1\10\1\11\1\12\1\13\1\14\1\15\1\16\1\17"+
    "\1\20\1\21\1\22\17\23\1\1\1\24\1\2\1\25"+
    "\1\26\1\27\1\30\1\31\1\32\1\33\4\23\1\34"+
    "\4\23\1\35\1\36\3\23\1\37\4\23\1\40\7\23"+
    "\2\0\1\23\1\41\1\42\1\43\1\44\1\45\12\23"+
    "\1\46\1\23\1\47\3\23\1\50\1\51\3\23\1\52"+
    "\1\53\1\54\1\55\11\23\1\56\3\23\1\57\1\60"+
    "\1\61\2\23\1\62\5\23\1\63\1\23\1\64\1\65"+
    "\1\66\1\23\1\67";

  private static int [] zzUnpackAction() {
    int [] result = new int[136];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\53\0\126\0\201\0\126\0\254\0\126\0\126"+
    "\0\327\0\126\0\126\0\126\0\126\0\126\0\126\0\u0102"+
    "\0\u012d\0\126\0\126\0\126\0\u0158\0\u0183\0\u01ae\0\u01d9"+
    "\0\u0204\0\u022f\0\u025a\0\u0285\0\u02b0\0\u02db\0\u0306\0\u0331"+
    "\0\u035c\0\u0387\0\u03b2\0\u03dd\0\u0408\0\126\0\126\0\126"+
    "\0\126\0\126\0\126\0\126\0\126\0\u0433\0\u045e\0\u0489"+
    "\0\u04b4\0\u0183\0\u04df\0\u050a\0\u0535\0\u0560\0\u0183\0\u0183"+
    "\0\u058b\0\u05b6\0\u05e1\0\u0183\0\u060c\0\u0637\0\u0662\0\u068d"+
    "\0\u0183\0\u06b8\0\u06e3\0\u070e\0\u0739\0\u0764\0\u078f\0\u07ba"+
    "\0\u07e5\0\u0810\0\u083b\0\u0183\0\u0183\0\u0183\0\u0183\0\u0183"+
    "\0\u0866\0\u0891\0\u08bc\0\u08e7\0\u0912\0\u093d\0\u0968\0\u0993"+
    "\0\u09be\0\u09e9\0\u0183\0\u0a14\0\u0183\0\u0a3f\0\u0a6a\0\u0a95"+
    "\0\126\0\u0183\0\u0ac0\0\u0aeb\0\u0b16\0\u0183\0\u0183\0\u0183"+
    "\0\u0183\0\u0b41\0\u0b6c\0\u0b97\0\u0bc2\0\u0bed\0\u0c18\0\u0c43"+
    "\0\u0c6e\0\u0c99\0\u0183\0\u0cc4\0\u0cef\0\u0d1a\0\u0183\0\u0183"+
    "\0\u0183\0\u0d45\0\u0d70\0\u0183\0\u0d9b\0\u0dc6\0\u0df1\0\u0e1c"+
    "\0\u0e47\0\u0183\0\u0e72\0\u0183\0\u0183\0\u0183\0\u0e9d\0\u0183";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[136];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12"+
    "\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22"+
    "\1\23\1\24\1\25\2\26\1\27\1\30\1\31\1\32"+
    "\1\33\1\26\1\34\1\35\1\36\1\37\1\26\1\40"+
    "\3\26\1\41\1\42\1\43\1\44\1\45\1\26\1\3"+
    "\2\46\1\47\47\46\1\50\54\0\1\4\55\0\1\51"+
    "\54\0\1\52\50\0\1\53\52\0\1\54\10\0\1\55"+
    "\57\0\1\26\1\56\1\26\1\57\21\26\1\0\2\26"+
    "\23\0\25\26\1\0\2\26\23\0\6\26\1\60\4\26"+
    "\1\61\11\26\1\0\2\26\23\0\6\26\1\62\4\26"+
    "\1\63\11\26\1\0\2\26\23\0\6\26\1\64\1\26"+
    "\1\65\5\26\1\66\6\26\1\0\2\26\23\0\5\26"+
    "\1\67\1\26\1\70\15\26\1\0\2\26\23\0\1\71"+
    "\24\26\1\0\2\26\23\0\6\26\1\72\11\26\1\73"+
    "\4\26\1\0\2\26\23\0\6\26\1\74\1\75\10\26"+
    "\1\76\1\77\3\26\1\0\2\26\23\0\3\26\1\100"+
    "\1\26\1\101\17\26\1\0\2\26\23\0\7\26\1\102"+
    "\15\26\1\0\2\26\23\0\3\26\1\103\3\26\1\104"+
    "\15\26\1\0\2\26\23\0\16\26\1\105\6\26\1\0"+
    "\2\26\23\0\20\26\1\106\4\26\1\0\2\26\23\0"+
    "\1\107\5\26\1\110\16\26\1\0\2\26\1\0\47\111"+
    "\1\112\3\111\50\0\1\45\24\0\2\26\1\113\22\26"+
    "\1\0\2\26\23\0\4\26\1\114\20\26\1\0\2\26"+
    "\23\0\12\26\1\115\12\26\1\0\2\26\23\0\1\26"+
    "\1\116\23\26\1\0\2\26\23\0\22\26\1\117\2\26"+
    "\1\0\2\26\23\0\7\26\1\120\15\26\1\0\2\26"+
    "\23\0\3\26\1\121\21\26\1\0\2\26\23\0\1\26"+
    "\1\122\23\26\1\0\2\26\23\0\11\26\1\123\13\26"+
    "\1\0\2\26\23\0\3\26\1\124\21\26\1\0\2\26"+
    "\23\0\16\26\1\125\6\26\1\0\2\26\23\0\10\26"+
    "\1\126\14\26\1\0\2\26\23\0\1\127\24\26\1\0"+
    "\2\26\23\0\14\26\1\130\10\26\1\0\2\26\23\0"+
    "\12\26\1\131\12\26\1\0\2\26\23\0\6\26\1\132"+
    "\16\26\1\0\2\26\23\0\4\26\1\133\20\26\1\0"+
    "\2\26\23\0\7\26\1\134\15\26\1\0\2\26\23\0"+
    "\7\26\1\135\15\26\1\0\2\26\23\0\13\26\1\136"+
    "\11\26\1\0\2\26\23\0\15\26\1\137\7\26\1\0"+
    "\2\26\23\0\6\26\1\140\16\26\1\0\2\26\50\0"+
    "\1\141\52\0\1\111\25\0\1\142\24\26\1\0\2\26"+
    "\23\0\11\26\1\143\13\26\1\0\2\26\23\0\2\26"+
    "\1\126\22\26\1\0\2\26\23\0\6\26\1\144\16\26"+
    "\1\0\2\26\23\0\2\26\1\145\22\26\1\0\2\26"+
    "\23\0\7\26\1\146\15\26\1\0\2\26\23\0\1\147"+
    "\24\26\1\0\2\26\23\0\3\26\1\150\21\26\1\0"+
    "\2\26\23\0\1\151\24\26\1\0\2\26\23\0\1\152"+
    "\24\26\1\0\2\26\23\0\11\26\1\153\3\26\1\154"+
    "\7\26\1\0\2\26\23\0\16\26\1\155\6\26\1\0"+
    "\2\26\23\0\1\26\1\156\23\26\1\0\2\26\23\0"+
    "\13\26\1\157\11\26\1\0\2\26\23\0\1\26\1\160"+
    "\23\26\1\0\2\26\23\0\12\26\1\161\12\26\1\0"+
    "\2\26\23\0\7\26\1\162\15\26\1\0\2\26\23\0"+
    "\12\26\1\163\12\26\1\0\2\26\23\0\15\26\1\164"+
    "\7\26\1\0\2\26\23\0\1\165\24\26\1\0\2\26"+
    "\23\0\7\26\1\166\15\26\1\0\2\26\23\0\21\26"+
    "\1\167\3\26\1\0\2\26\23\0\1\170\24\26\1\0"+
    "\2\26\23\0\3\26\1\171\21\26\1\0\2\26\23\0"+
    "\1\172\24\26\1\0\2\26\23\0\13\26\1\173\11\26"+
    "\1\0\2\26\23\0\4\26\1\174\20\26\1\0\2\26"+
    "\23\0\1\175\24\26\1\0\2\26\23\0\4\26\1\176"+
    "\20\26\1\0\2\26\23\0\16\26\1\177\6\26\1\0"+
    "\2\26\23\0\16\26\1\200\6\26\1\0\2\26\23\0"+
    "\6\26\1\201\16\26\1\0\2\26\23\0\7\26\1\202"+
    "\15\26\1\0\2\26\23\0\10\26\1\203\14\26\1\0"+
    "\2\26\23\0\17\26\1\204\5\26\1\0\2\26\23\0"+
    "\3\26\1\205\21\26\1\0\2\26\23\0\3\26\1\206"+
    "\21\26\1\0\2\26\23\0\7\26\1\207\15\26\1\0"+
    "\2\26\23\0\1\210\24\26\1\0\2\26\1\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[3784];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\1\1\1\11\1\1\2\11\1\1\6\11"+
    "\2\1\3\11\21\1\10\11\33\1\2\0\26\1\1\11"+
    "\47\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[136];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
	int nesting = 0, lbracketStartLine = 0, lbracketStartColumn = 0;
	
    private PascalSym sym(int type) {
        return new PascalSym(type, yyline + 1, yycolumn + 1, yytext());
    }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public PascalLex(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public PascalLex(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 116) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
    	if(nesting > 0) Report.warning("Unclosed comment "+yytext(), lbracketStartLine + 1, lbracketStartColumn + 1);
  yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public PascalSym next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 18: 
          { return sym(PascalTok.SUB);
          }
        case 56: break;
        case 30: 
          { return sym(PascalTok.OR);
          }
        case 57: break;
        case 9: 
          { return sym(PascalTok.LPARENTHESIS);
          }
        case 58: break;
        case 27: 
          { return sym(PascalTok.NEQ);
          }
        case 59: break;
        case 39: 
          { return sym(PascalTok.VAR);
          }
        case 60: break;
        case 14: 
          { return sym(PascalTok.GTH);
          }
        case 61: break;
        case 24: 
          { return sym(PascalTok.DOTS);
          }
        case 62: break;
        case 37: 
          { return sym(PascalTok.FOR);
          }
        case 63: break;
        case 46: 
          { return sym(PascalTok.CONST);
          }
        case 64: break;
        case 35: 
          { return sym(PascalTok.NIL);
          }
        case 65: break;
        case 12: 
          { return sym(PascalTok.SEMIC);
          }
        case 66: break;
        case 6: 
          { return sym(PascalTok.COMMA);
          }
        case 67: break;
        case 22: 
          { nesting--; if (nesting == 0) yybegin(YYINITIAL);
          }
        case 68: break;
        case 54: 
          { return sym(PascalTok.FUNCTION);
          }
        case 69: break;
        case 29: 
          { return sym(PascalTok.OF);
          }
        case 70: break;
        case 23: 
          { return sym(PascalTok.ASSIGN);
          }
        case 71: break;
        case 31: 
          { return sym(PascalTok.TO);
          }
        case 72: break;
        case 11: 
          { return sym(PascalTok.RPARENTHESIS);
          }
        case 73: break;
        case 50: 
          { return sym(PascalTok.RECORD);
          }
        case 74: break;
        case 16: 
          { return sym(PascalTok.MUL);
          }
        case 75: break;
        case 17: 
          { return sym(PascalTok.PTR);
          }
        case 76: break;
        case 7: 
          { return sym(PascalTok.DOT);
          }
        case 77: break;
        case 49: 
          { return sym(PascalTok.BEGIN);
          }
        case 78: break;
        case 26: 
          { return sym(PascalTok.LEQ);
          }
        case 79: break;
        case 52: 
          { return sym(PascalTok.PROGRAM);
          }
        case 80: break;
        case 20: 
          { return sym(PascalTok.INT_CONST);
          }
        case 81: break;
        case 8: 
          { return sym(PascalTok.LBRACKET);
          }
        case 82: break;
        case 45: 
          { return sym(PascalTok.TYPE);
          }
        case 83: break;
        case 13: 
          { return sym(PascalTok.ADD);
          }
        case 84: break;
        case 33: 
          { return sym(PascalTok.END);
          }
        case 85: break;
        case 47: 
          { return sym(PascalTok.ARRAY);
          }
        case 86: break;
        case 41: 
          { return sym(PascalTok.ELSE);
          }
        case 87: break;
        case 51: 
          { return sym(PascalTok.INT);
          }
        case 88: break;
        case 3: 
          { nesting = 1; lbracketStartLine = yyline; lbracketStartColumn = yycolumn; yybegin(COMMENT);
          }
        case 89: break;
        case 1: 
          { Report.warning("Unknown character: "+yytext(), yyline + 1, yycolumn + 1);
          }
        case 90: break;
        case 43: 
          { return sym(PascalTok.BOOL_CONST);
          }
        case 91: break;
        case 44: 
          { return sym(PascalTok.THEN);
          }
        case 92: break;
        case 15: 
          { return sym(PascalTok.LTH);
          }
        case 93: break;
        case 38: 
          { return sym(PascalTok.AND);
          }
        case 94: break;
        case 34: 
          { return sym(PascalTok.NOT);
          }
        case 95: break;
        case 28: 
          { return sym(PascalTok.DO);
          }
        case 96: break;
        case 36: 
          { return sym(PascalTok.DIV);
          }
        case 97: break;
        case 21: 
          { nesting++;
          }
        case 98: break;
        case 4: 
          { return sym(PascalTok.COLON);
          }
        case 99: break;
        case 19: 
          { return sym(PascalTok.IDENTIFIER);
          }
        case 100: break;
        case 40: 
          { return sym(PascalTok.CHAR_CONST);
          }
        case 101: break;
        case 32: 
          { return sym(PascalTok.IF);
          }
        case 102: break;
        case 42: 
          { return sym(PascalTok.CHAR);
          }
        case 103: break;
        case 10: 
          { return sym(PascalTok.RBRACKET);
          }
        case 104: break;
        case 53: 
          { return sym(PascalTok.BOOL);
          }
        case 105: break;
        case 48: 
          { return sym(PascalTok.WHILE);
          }
        case 106: break;
        case 55: 
          { return sym(PascalTok.PROCEDURE);
          }
        case 107: break;
        case 25: 
          { return sym(PascalTok.GEQ);
          }
        case 108: break;
        case 2: 
          { 
          }
        case 109: break;
        case 5: 
          { return sym(PascalTok.EQU);
          }
        case 110: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              {     return new PascalSym(PascalTok.EOF);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
