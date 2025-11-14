public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */

    @Override
    public Expr do_parse() throws Exception {
        Expr e = parseT();
        // After parsing a T, we should have consumed all tokens.
        if (tokens != null) {
            throw new Exception("Parsing error: extra tokens remaining");
        }
        return e;
    }

    // T -> F AddOp T | F
    private Expr parseT() throws Exception {
        Expr left = parseF();
        if (tokens != null &&
            (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0))) {
            Token op = parseAddOp();
            Expr right = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(left, right);
            } else {
                return new MinusExpr(left, right);
            }
        }
        return left;
    }

    // F -> Lit MulOp F | Lit
    private Expr parseF() throws Exception {
        Expr left = parseLit();
        if (tokens != null &&
            (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0))) {
            Token op = parseMulOp();
            Expr right = parseF();
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            } else {
                return new DivExpr(left, right);
            }
        }
        return left;
    }

    // Lit -> NUM | LPAREN T RPAREN
    private Expr parseLit() throws Exception {
        if (tokens == null) {
            throw new Exception("Parsing error: unexpected end of input in Lit");
        }

        if (peek(TokenType.NUM, 0)) {
            Token numTok = consume(TokenType.NUM);
            float value = Float.parseFloat(numTok.lexeme);
            return new FloatExpr(value);
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr inside = parseT();
            if (!peek(TokenType.RPAREN, 0)) {
                throw new Exception("Parsing error: expected ')'");
            }
            consume(TokenType.RPAREN);
            return inside;
        } else {
            throw new Exception("Parsing error in Lit on token: " + tokens.elem.lexeme);
        }
    }

    // AddOp -> PLUS | MINUS
    private Token parseAddOp() throws Exception {
        if (tokens == null) {
            throw new Exception("Parsing error: unexpected end of input in AddOp");
        }
        if (peek(TokenType.PLUS, 0)) {
            return consume(TokenType.PLUS);
        } else if (peek(TokenType.MINUS, 0)) {
            return consume(TokenType.MINUS);
        } else {
            throw new Exception("Parsing error in AddOp on token: " + tokens.elem.lexeme);
        }
    }

    // MulOp -> TIMES | DIV
    private Token parseMulOp() throws Exception {
        if (tokens == null) {
            throw new Exception("Parsing error: unexpected end of input in MulOp");
        }
        if (peek(TokenType.TIMES, 0)) {
            return consume(TokenType.TIMES);
        } else if (peek(TokenType.DIV, 0)) {
            return consume(TokenType.DIV);
        } else {
            throw new Exception("Parsing error in MulOp on token: " + tokens.elem.lexeme);
        }
    }

}
