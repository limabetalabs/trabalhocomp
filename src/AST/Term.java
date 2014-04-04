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
public class Term implements ClassGenC {

    private ArrayList<Factor> factor;
    private Factor firstFactor;
    private ArrayList<Integer> ops;

    public Term(Factor firstFactor, ArrayList<Integer> ops, ArrayList<Factor> factor) {
        this.factor = factor;
        this.firstFactor = firstFactor;
        this.ops = ops;
    }

    public String getType() {
        return firstFactor.getType();
    }

    @Override
    public void genC(PW pw) {
        int count = 0;
        firstFactor.genC(pw);

        for (int op : ops) {

            if (op == Symbol.MULT) {
                pw.print("*");
            }
            if (op == Symbol.DIV) {
                pw.print("/");
            }
            if (op == Symbol.MOD) {
                pw.print("%");
            }
            if (op == Symbol.FLOORDIV) {
                pw.print("/");
            }
            factor.get(count).genC(pw);
            count++;
        }
    }

    String getName() {
         return firstFactor.getName();
    }
}
