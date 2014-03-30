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
public class Suite implements ClassGenC{

    private SimpleStmt simpleStmt;
    private ArrayList<Stmt> stmt;

    public Suite(SimpleStmt simpleStmt, ArrayList<Stmt> stmt) {
        this.simpleStmt = simpleStmt;
        this.stmt = stmt;
    }

    @Override
    public void genC(PW pw) {
        if (simpleStmt != null) {
            simpleStmt.genC(pw);
        } else {
            pw.print("{");
            pw.print("\n");
            pw.add();
            for (Stmt s : stmt) {
                s.genC(pw);
            }
            pw.sub();
            pw.print("}");
            pw.print("\n");
        }

    }
}
