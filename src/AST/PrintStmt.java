/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author JoãoOtávio
 */
public class PrintStmt extends SmallStmt {

    private ArrayList<Test> test;

    public PrintStmt(ArrayList<Test> test) {
        this.test = test;
    }

    @Override
    public void genC(PW pw) {
        pw.print("printf(");//precisa verificar os tipos
        test.get(0).genC(pw);
        for (Test t : test) {
            if (t != test.get(0)) {
            pw.print(", ");
            t.genC(pw);
            }
        }
        pw.print(")");
    }
}
