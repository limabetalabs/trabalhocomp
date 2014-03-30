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
public class AndExpr implements ClassGenC {

    private ArrayList<ArithExpr> arithExpr;

    public AndExpr(ArrayList<ArithExpr> arithExpr) {
        this.arithExpr = arithExpr;
    }

    public String getType() {
        return arithExpr.get(0).getType();
    }

    @Override
    public void genC(PW pw) {
        arithExpr.get(0).genC(pw);
        for (ArithExpr aE : arithExpr) {
            if (arithExpr.get(0) != aE) {
                pw.print(" && ");
                aE.genC(pw);
            }
        }

    }
}
