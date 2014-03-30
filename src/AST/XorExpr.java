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
public class XorExpr implements ClassGenC {

    private ArrayList<AndExpr> andExpr;

    public XorExpr(ArrayList<AndExpr> andExpr) {
        this.andExpr = andExpr;
    }

    public String getType() {
        return andExpr.get(0).getType();
    }

    @Override
    public void genC(PW pw) {
        andExpr.get(0).genC(pw);
        for (AndExpr a : andExpr) {
            if (andExpr.get(0) != a) {
                pw.print(" ^ ");
                a.genC(pw);
            }
        }
    }
}
