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
public class SimpleStmt implements ClassGenC {

    private ArrayList<SmallStmt> smallstmt;

    public SimpleStmt(ArrayList<SmallStmt> smallstmt) {
        this.smallstmt = smallstmt;
    }

    @Override
    public void genC(PW pw) {
        smallstmt.get(0).genC(pw);
        pw.print(";");
        for (SmallStmt s : smallstmt) {
            if (s != smallstmt.get(0)) {
                s.genC(pw);
                pw.print(";");
            }
        }
        pw.println("");
    }
}
