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
        for (Test t : test) {
            pw.print(" , ");
            t.genC(pw);
        }
        pw.print(");");
    }
}
