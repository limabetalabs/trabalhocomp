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
public class TargetList implements ClassGenC {

    private ArrayList<Target> target;

    public TargetList(ArrayList<Target> target) {
        this.target = target;
    }

    public int getSize() {
        return this.target.size();
    }

    public Target get(int i) {
        return target.get(i);
    }

    public Target getElement(int i) {
        return target.get(i);
    }

    @Override
    public void genC(PW pw) {
        target.get(0).genC(pw);
        for (Target t : target) {
            if (t != target.get(0)) {
                pw.print(", ");
                t.genC(pw);
            }
        }
    }
}
