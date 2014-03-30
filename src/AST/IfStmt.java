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
public class IfStmt extends CompoundStmt {

    private Test test;
    private Suite thenPart;
    private ArrayList<Test> elifTest;
    private ArrayList<Suite> elifSuite;
    private Suite elsePart;

    public IfStmt(Test test, Suite thenPart, ArrayList<Test> elifTest, ArrayList<Suite> elifSuite, Suite elsePart) {
        this.test = test;
        this.thenPart = thenPart;
        this.elifTest = elifTest;
        this.elifSuite = elifSuite;
        this.elsePart = elsePart;
    }

    @Override
    public void genC(PW pw) {
        // if_stmt: 'if' test ':' suite ('elif' test ':' suite)* ['else' ':' suite]
        
        pw.print("if ("  );
        test.genC(pw);
        pw.print("){");
        
        thenPart.genC(pw);
        
        pw.println("}");
        
        if ( elifTest != null){
            for ( int i = 0; i < elifTest.size() ; i++ ){
                pw.print("else if (");
                elifTest.get(i).genC(pw);
                elifSuite.get(0).genC(pw);
                pw.println("}");
            }
        }
        
        if ( elsePart != null){
            pw.print("else{");
            elsePart.genC(pw);
            pw.println("}");
        }
    }
}
