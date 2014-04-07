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
public class ClassDef extends CompoundStmt {

    private String name;
    private ArrayList<Atom> atom;
    private Suite suite;

    public ClassDef(String name, ArrayList<Atom> atom, Suite suite) {
        this.name = name;
        this.atom = atom;
        this.suite = suite;
    }

    public ClassDef() {
        this.name = null;
        this.atom = null;
        this.suite = null;
    }

    public void genC(PW pw) {
        if (this.name != null && this.atom != null && this.suite != null) {
            pw.println("typedef struct _" + this.name);
            if (this.atom != null) {
                Iterator<Atom> it = this.atom.iterator();
                while (it.hasNext()) {
                    Atom s = it.next();
                    s.genC(pw);
                }
            }
            this.suite.genC(pw);
            pw.println("_"+this.name + ";");
        }
    }
}
