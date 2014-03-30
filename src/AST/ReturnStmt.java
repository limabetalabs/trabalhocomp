/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class ReturnStmt extends FlowStmt {

    private Test test;

    public ReturnStmt(Test test) {
        this.test = test;
    }

    @Override
    public void genC(PW pw) {
        pw.print("return ");
        test.genC(pw);
    }
}
