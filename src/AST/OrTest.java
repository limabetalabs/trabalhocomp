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
public class OrTest implements ClassGenC {

    private final ArrayList<AndTest> andTest;

    public OrTest(ArrayList<AndTest> andTest) {
        this.andTest = andTest;
    }

    public String getType() {
        return andTest.get(0).getType();
    }

    @Override
    public void genC(PW pw) {
        andTest.get(0).genC(pw);
        for (AndTest t : andTest) {
            if (t != andTest.get(0)) {
                pw.print(" || ");
                t.genC(pw);
            }
        }
    }
}
