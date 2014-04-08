/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class WhileStmt extends CompoundStmt {

    private Test test;
    private Suite doPart;
    private Suite elsePart;

    public WhileStmt(Test test, Suite doPart, Suite elsePart) {
        this.test = test;
        this.doPart = doPart;
        this.elsePart = elsePart;
    }

    @Override
    public void genC(PW pw) {
        if (elsePart == null) {
            pw.print("while(");
            test.genC(pw);
            pw.print(")\n");
            doPart.genC(pw);
        } else {
            pw.print("if (");
            test.genC(pw);
            pw.print(") {\n");
            pw.print("\twhile(");
            test.genC(pw);
            pw.print(")");
            doPart.genC(pw);
            pw.print("} else {\n");
            elsePart.genC(pw);
            pw.print("}");
        }
    }
}
