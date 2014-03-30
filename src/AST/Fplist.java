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
public class Fplist implements ClassGenC{

    private ArrayList<Fpdef> fpdef;

    public Fplist(ArrayList<Fpdef> fpdef) {
        this.fpdef = fpdef;
    }

    @Override
    public void genC(PW pw) {
        for(Fpdef f : fpdef){
            pw.print(", ");
            f.genC(pw);
        }
        pw.print(";");
    }
}
