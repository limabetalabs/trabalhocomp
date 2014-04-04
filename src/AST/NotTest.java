/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class NotTest implements ClassGenC {

    private final NotTest notTest;
    private final Comparison comparison;

    public NotTest(NotTest notTest, Comparison comparison) {
        this.notTest = notTest;
        this.comparison = comparison;
    }

    public String getType() {
        if (notTest != null) {
            return notTest.getType();
        } else {
            return comparison.getType();
        }
    }

    @Override
    public void genC(PW pw) {
        if (comparison != null) {
            comparison.genC(pw);
        } else {
            pw.print("!");
            notTest.genC(pw);
        }
    }

    String getName() {
        if (notTest != null) {
            return notTest.getName();
        } else {
            return comparison.getName();
        }
    }
}
