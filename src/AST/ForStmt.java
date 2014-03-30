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
public class ForStmt extends CompoundStmt {

    private Atom atom;
    private Suite doPart;
    private Suite elsePart;
    private ArrayList<Expr> expr;

    public ForStmt(ArrayList<Expr> expr, Atom atom, Suite doPart, Suite elsePart) {
        this.expr = expr;
        this.atom = atom;
        this.doPart = doPart;
        this.elsePart = elsePart;
    }

    @Override
    public void genC(PW pw) {
        pw.print("for( int _" +atom +"= 0; _" +atom +" < " + expr.size() + "; _" +atom +"++){");
        
        doPart.genC(pw);
        
        pw.print("}");
        
        if (elsePart != null)
            elsePart.genC(pw);
    }
}
