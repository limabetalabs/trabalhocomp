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
public class Parameters implements ClassGenC{

    private final VarArgsList varArgsList;

    public Parameters(VarArgsList varArgsList) {
        this.varArgsList = varArgsList;
    }

    @Override
    public void genC(PW pw) {
        pw.print("(");
        if (varArgsList != null) {
            varArgsList.genC(pw);
        }
        pw.print(")");
    }
}
