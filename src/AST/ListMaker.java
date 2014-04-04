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
public class ListMaker implements ClassGenC{

    private ArrayList<Test> test;

    public ListMaker(ArrayList<Test> test) {
        this.test = test;
    }

    public int getSize() {
        return this.test.size();
    }
    
    public Test getElement(int i) {
        return test.get(i);
    }
    
    @Override
    public void genC(PW pw) {
        test.get(0).genC(pw);
        for(Test t: test){
            if ( test.get(0) != t){
                pw.print(", ");
                t.genC(pw);
            }
        }
    }
}
