/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author bruno
 */
public class VarArgsList implements ClassGenC {

    private ArrayList<Fpdef> Fpdef;
    private ArrayList<Test> test;

    public VarArgsList(ArrayList<Fpdef> fpdef, ArrayList<Test> test) {
        this.Fpdef = fpdef;
        this.test = test;
    }

    public void genC(PW pw) {
        Iterator<Fpdef> it = this.Fpdef.iterator();
        Iterator<Test> it2 = this.test.iterator();
        while (it.hasNext()) {
            Fpdef s = it.next();
            s.genC(pw);
            Test s2 = it2.next();
            if (s2 != null) {
                pw.print(" = ");
                s2.genC(pw);
            }
            if (it.hasNext()) {
                pw.print(", ");
            }
        }
    }

}
