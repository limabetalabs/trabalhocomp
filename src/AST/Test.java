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
public class Test implements ClassGenC {

    private OrTest orTest;
    private OrTest ifOrTest;
    private Test elseTest;

    public Test(OrTest orTest, OrTest ifOrTest, Test elseTest) {
        this.orTest = orTest;
        this.ifOrTest = ifOrTest;
        this.elseTest = elseTest;
    }

    public String getType() {
        return orTest.getType();
    }

    public void genC(PW pw) {
        orTest.genC(pw);
        if (ifOrTest != null) {
            pw.print("if ( ");
            ifOrTest.genC(pw);
            pw.print(" )");
            if (elseTest != null) {
                pw.print("else");
                elseTest.genC(pw);
            }
        }
    }
}
