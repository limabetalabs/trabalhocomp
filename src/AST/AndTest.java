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
public class AndTest implements ClassGenC {

    private final ArrayList<NotTest> notTest;

    public AndTest(ArrayList<NotTest> notTest) {
        this.notTest = notTest;
    }

    public void print() {
        System.out.println("AndTest");
    }

    public String getType() {
        return notTest.get(0).getType();
    }

    @Override
    public void genC(PW pw) {
        notTest.get(0).genC(pw);
        for (NotTest t : notTest) {
            if (t != notTest.get(0)) {
                pw.print(" && ");
                t.genC(pw);
            }
        }
    }
}
