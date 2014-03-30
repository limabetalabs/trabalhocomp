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
public class ExprList implements ClassGenC{

    private ArrayList<Expr> exprList;

    @Override
    public void genC(PW pw) {
        for (Expr e : exprList) {
            e.genC(pw);
        }
    }
}
