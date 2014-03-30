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
public class VarArgsList implements ClassGenC{
    private ArrayList<Fpdef> Fpdef;
    private ArrayList<Test> test;
    public VarArgsList(ArrayList<Fpdef> fpdef, ArrayList<Test> test){
        this.Fpdef = fpdef;
        this.test = test;
    }

    @Override
    public void genC(PW pw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
