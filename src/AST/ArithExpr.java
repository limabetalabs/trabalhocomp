/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;
import java.util.ArrayList;

/**
 *
 * @author bruno
 */
public class ArithExpr implements ClassGenC {

    private ArrayList<Term> term;
    private Term firstTerm;
    private ArrayList<Integer> op;

    public ArithExpr(Term firstTerm, ArrayList<Integer> op, ArrayList<Term> term) {
        this.firstTerm = firstTerm;
        this.term = term;
        this.op = op;
    }

    public String getType() {
        return firstTerm.getType();
    }

    @Override
    public void genC(PW pw) {
        firstTerm.genC(pw);
        int count = 0;
        for (int i : op) {
            if (i == Symbol.PLUS) {
                pw.print("+");
            }
            if (i == Symbol.MINUS) {
                pw.print("-");
            }
            term.get(count).genC(pw);
            count++;
        }

    }
}
