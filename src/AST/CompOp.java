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
public class CompOp implements ClassGenC{

    private int compop;

    public CompOp(int compop) {
        this.compop = compop;
    }

    @Override
    public void genC(PW pw) {
        if (compop == Symbol.LT) {
            pw.print("<");
        }
        if (compop == Symbol.GT) {
            pw.print(">");
        }
        if (compop == Symbol.LE) {
            pw.print("<=");
        }
        if (compop == Symbol.EQ || compop == Symbol.IS) {
            pw.print("==");
        }
        if (compop == Symbol.GE) {
            pw.print(">=");
        }
        if (compop == Symbol.NEQ || compop == Symbol.ISNOT) {
            pw.print("!=");
        }
        
        
        
    }
}
