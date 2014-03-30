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
public class ExprStmt extends SmallStmt {

    private TargetList targetlist;
    private AugAssign augassign;
    private ListMaker listmaker;

    public ExprStmt(TargetList targetlist, AugAssign augassign, ListMaker listmaker) {
        this.targetlist = targetlist;
        this.augassign = augassign;
        this.listmaker = listmaker;
    }

    @Override
    public void genC(PW pw) {
        targetlist.genC(pw);
        augassign.genC(pw);
        listmaker.genC(pw);

    }
}
