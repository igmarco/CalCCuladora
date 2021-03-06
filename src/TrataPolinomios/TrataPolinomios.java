/* Generated By:JavaCC: Do not edit this line. TrataPolinomios.java */
package TrataPolinomios;
import java.util.*;

class Monomio
{
  public Double coeficiente;

  public Integer grado;

  public Monomio(Double c, Integer g)
  {
    this.coeficiente = c;
    this.grado = g;
  }
}

;

public class TrataPolinomios implements TrataPolinomiosConstants {
  public static void main(String args []) throws ParseException
  {
    TrataPolinomios parser = new TrataPolinomios(System.in);
    while (true)
    {
      System.out.println("Reading from standard input...");
      System.out.print("Enter an expression like \u005c"5x^2+3x^3-2+x;\u005c" :");
      try
      {
        switch (TrataPolinomios.one_line())
        {
          case 0 :
          System.out.println("OK.");
          break;
          case 1 :
          System.out.println("Goodbye.");
          break;
          default :
          break;
        }
      }
      catch (Exception e)
      {
        System.out.println("NOK.");
        System.out.println(e.getMessage());
        TrataPolinomios.ReInit(System.in);
      }
      catch (Error e)
      {
        System.out.println("Oops.");
        System.out.println(e.getMessage());
        break;
      }
    }
  }

  static final public int one_line() throws ParseException {
 ArrayList < Double > polArr;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PLUS:
    case MINUS:
    case TINDEPENDIENTE:
    case ENTERO:
    case 11:
      polArr = polinomio();
      jj_consume_token(11);
    for(Double coef : polArr) System.out.println("> " + coef);
    {if (true) return 0;}
      break;
//      jj_consume_token(11);
//    {if (true) return 1;}
//      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public ArrayList < Double > polinomio() throws ParseException {
  Monomio mon;
  HashMap < Integer, Double > pol = new HashMap < Integer, Double > ();
  ArrayList < Double > polArr = new ArrayList < Double > ();
  boolean resta = false;
    mon = monomio();
    pol.put(mon.grado, mon.coeficiente);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
        break;
      case MINUS:
        jj_consume_token(MINUS);
        resta = true;
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      mon = monomio();
      if (resta) mon.coeficiente = - mon.coeficiente;
      resta = false;
      if (pol.containsKey(mon.grado)) pol.replace(mon.grado, pol.get(mon.grado) + mon.coeficiente);
      else pol.put(mon.grado, mon.coeficiente);
    }
    Set < Integer > grados = pol.keySet();
    Integer maximo = 0;
    for (Integer g : grados) if (g > maximo) maximo = g;
    for (Integer grado = 0; grado <= maximo; grado++) polArr.add(pol.containsKey(grado) ? pol.get(grado) : (Integer) 0);
    {if (true) return polArr;}
    throw new Error("Missing return statement in function");
  }

  static final public Monomio monomio() throws ParseException {
  Token gradoToken, coefEnteroToken, coefDecimalesToken;
  Double coefDecimales = 0.0;
  Monomio mon = new Monomio
  (
    (Double) 1.0, (Integer) 0
  )
  ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ENTERO:
      coefEnteroToken = jj_consume_token(ENTERO);
      mon.coeficiente = (Double) (0.0 + Integer.parseInt(coefEnteroToken.image));
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 12:
        jj_consume_token(12);
        coefDecimalesToken = jj_consume_token(ENTERO);
        coefDecimales = Double.parseDouble(coefDecimalesToken.image);
        while (coefDecimales > 1) coefDecimales = coefDecimales / 10;
        mon.coeficiente = mon.coeficiente + coefDecimales;
        break;
      default:
        jj_la1[3] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TINDEPENDIENTE:
      jj_consume_token(TINDEPENDIENTE);
      mon.grado = 1;
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ELEVADO:
        jj_consume_token(ELEVADO);
        gradoToken = jj_consume_token(ENTERO);
        mon.grado = Integer.parseInt(gradoToken.image);
        break;
      default:
        jj_la1[5] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    {if (true) return mon;}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public TrataPolinomiosTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[7];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xb60,0x60,0x60,0x1000,0x200,0x80,0x100,};
   }

  /** Constructor with InputStream. */
  public TrataPolinomios(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public TrataPolinomios(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new TrataPolinomiosTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public TrataPolinomios(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TrataPolinomiosTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public TrataPolinomios(TrataPolinomiosTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(TrataPolinomiosTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[13];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 7; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 13; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
