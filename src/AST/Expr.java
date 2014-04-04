/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author bruno
 */
public class Expr implements ClassGenC {

    private final ArrayList<XorExpr> xorExpr;

    public Expr(ArrayList<XorExpr> xorExpr) {
        this.xorExpr = xorExpr;
    }

    public String getType() {
        return xorExpr.get(0).getType();
    }

    @Override
    public void genC(PW pw) {
        xorExpr.get(0).genC(pw);

        for (XorExpr xE : xorExpr) {
            if (xorExpr.get(0) != xE) {
                pw.print(" || ");
                xE.genC(pw);
            }
        }
    }

    String getName() {
        return xorExpr.get(0).getName();
    }
}
