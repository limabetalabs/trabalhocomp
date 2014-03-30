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
public class AugAssign {

    private int aug;

    public AugAssign(int augassign) {
        this.aug = augassign;
    }

    public void genC(PW pw) {
        if (aug == Symbol.DIVASSIGN) {
            pw.print("/=");
        }
        if (aug == Symbol.MODASSIGN) {
            pw.print("%=");
        }
        if (aug == Symbol.MINUSASSIGN) {
            pw.print("-=");
        }
        if (aug == Symbol.ASSIGN) {
            pw.print("=");
        }
        if (aug == Symbol.PLUSASSIGN) {
            pw.print("+=");
        }
        if (aug == Symbol.MULTIASSIGN) {
            pw.print("*=");
        }
        if (aug == Symbol.ORASSIGN) {
            pw.print("|=");
        }
        if (aug == Symbol.XORASSIGN) {
            pw.print("^=");
        }
        if (aug == Symbol.ANDASSIGN) {
            pw.print("&=");
        }
    }
}
