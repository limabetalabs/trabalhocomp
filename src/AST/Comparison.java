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
public class Comparison implements ClassGenC {

    private ArrayList<CompOp> compOp;
    private ArrayList<Expr> expr;
    private Expr firstExpr;

    public Comparison(Expr firstExpr, ArrayList<CompOp> compOp, ArrayList<Expr> expr) {
        this.compOp = compOp;
        this.expr = expr;
        this.firstExpr = firstExpr;
    }

    public String getType() {
        return firstExpr.getType();
    }

    @Override
    public void genC(PW pw) {

        firstExpr.genC(pw);

        for (int i = 0; i < compOp.size(); i++) {
            compOp.get(i).genC(pw);
            expr.get(i).genC(pw);
        }


    }
}
