/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.io.PrintWriter;

/**
 *
 * @author JoãoOtávio
 */
public class Stmt {
    private SimpleStmt simplestmt;
    private CompoundStmt compoundstmt;

    public Stmt(SimpleStmt simplestmt, CompoundStmt compoundstmt) {
        this.simplestmt = simplestmt;
        this.compoundstmt = compoundstmt;
    }

    void genC(PW pw) {
        if(simplestmt != null){
            simplestmt.genC(pw);
        }
        else
            compoundstmt.genC(pw);
    }
}
