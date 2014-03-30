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
public class Fpdef implements ClassGenC{
    
    private String name;
    private Fplist fplist;
    
    public Fpdef(String name, Fplist fplist) {
        this.name = name;
        this.fplist = fplist;
    }
    
    @Override
    public void genC(PW pw) {
        if (fplist != null) {
            pw.print("(");
            fplist.genC(pw);
            pw.print(")");
        } else {
            pw.print("_" + this.name);
        }
        
    }
}
