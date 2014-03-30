/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class ContinueStmt extends FlowStmt {

    public void genC(PW pw) {
        pw.print("continue;");
    }
}
