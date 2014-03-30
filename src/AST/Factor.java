/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import Lexer.Symbol;

/**
 *
 * @author bruno
 */
public class Factor implements ClassGenC{

    private Factor factor;
    private Atom atom;
    private int op;

    public Factor(int op, Factor factor, Atom atom) {
        this.factor = factor;
        this.op = op;
        this.atom = atom;
    }

    @Override
    public void genC(PW pw) {
        if (atom != null) {
            atom.genC(pw);
        } else {
            if (op == Symbol.INVERTION) {
                pw.print("~");
            }
            if (op == Symbol.PLUS) {
                pw.print("+");
            }
            if (op == Symbol.MINUS) {
                pw.print("-");
            }
            factor.genC(pw);
        }

    }

    public String getType() {
       if (this.factor != null) {
           return this.factor.getType();
       } else {
           return this.atom.getType();
       }
   }
}
