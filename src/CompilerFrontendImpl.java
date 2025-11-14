public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * LPAREN: \(
     * RPAREN: \)
     * WHITE_SPACE (' '||\n||\r||\t)*
     */
    @Override
    protected void init_lexer() {
        lex = new LexerImpl();

        // NUM: [0-9]*\.[0-9]+
        AutomatonImpl num = new AutomatonImpl();
        // states: 0 = start (before any digits or dot)
        //         1 = after the dot
        //         2 = after at least one digit following the dot (accepting)
        num.addState(0, true, false);
        num.addState(1, false, false);
        num.addState(2, false, true);
        for (char c = '0'; c <= '9'; c++) {
            // leading digits (the [0-9]* part)
            num.addTransition(0, c, 0);
            // digits after the dot (the [0-9]+ part)
            num.addTransition(2, c, 2);
        }
        num.addTransition(0, '.', 1);
        num.addTransition(1, '0', 2);
        num.addTransition(1, '1', 2);
        num.addTransition(1, '2', 2);
        num.addTransition(1, '3', 2);
        num.addTransition(1, '4', 2);
        num.addTransition(1, '5', 2);
        num.addTransition(1, '6', 2);
        num.addTransition(1, '7', 2);
        num.addTransition(1, '8', 2);
        num.addTransition(1, '9', 2);
        lex.add_automaton(TokenType.NUM, num);

        // PLUS: \+
        AutomatonImpl plus = new AutomatonImpl();
        plus.addState(0, true, false);
        plus.addState(1, false, true);
        plus.addTransition(0, '+', 1);
        lex.add_automaton(TokenType.PLUS, plus);

        // MINUS: -
        AutomatonImpl minus = new AutomatonImpl();
        minus.addState(0, true, false);
        minus.addState(1, false, true);
        minus.addTransition(0, '-', 1);
        lex.add_automaton(TokenType.MINUS, minus);

        // TIMES: \*
        AutomatonImpl times = new AutomatonImpl();
        times.addState(0, true, false);
        times.addState(1, false, true);
        times.addTransition(0, '*', 1);
        lex.add_automaton(TokenType.TIMES, times);

        // DIV: /
        AutomatonImpl div = new AutomatonImpl();
        div.addState(0, true, false);
        div.addState(1, false, true);
        div.addTransition(0, '/', 1);
        lex.add_automaton(TokenType.DIV, div);

        // LPAREN: \(
        AutomatonImpl lparen = new AutomatonImpl();
        lparen.addState(0, true, false);
        lparen.addState(1, false, true);
        lparen.addTransition(0, '(', 1);
        lex.add_automaton(TokenType.LPAREN, lparen);

        // RPAREN: \)
        AutomatonImpl rparen = new AutomatonImpl();
        rparen.addState(0, true, false);
        rparen.addState(1, false, true);
        rparen.addTransition(0, ')', 1);
        lex.add_automaton(TokenType.RPAREN, rparen);

        // WHITE_SPACE: (' '||\n||\r||\t)*
        AutomatonImpl ws = new AutomatonImpl();
        // Accept the empty string and any sequence of whitespace characters.
        ws.addState(0, true, true);
        ws.addTransition(0, ' ', 0);
        ws.addTransition(0, '\n', 0);
        ws.addTransition(0, '\r', 0);
        ws.addTransition(0, '\t', 0);
        lex.add_automaton(TokenType.WHITE_SPACE, ws);
    }

}
