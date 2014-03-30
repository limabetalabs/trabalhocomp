/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class FuncDec extends CompoundStmt{
    private int name;
    private Parameters parameters;
    private Suite suite;
    public FuncDec(int name, Parameters parameters, Suite suite){
        this.name = name;
        this.parameters = parameters;
        this.suite = suite;
    }

    @Override
    public void genC(PW pw) {
        pw.print("void " + name + "(");
        parameters.genC(pw);
        pw.print("){");
        
        suite.genC(pw);
        
        pw.println("}");
    }
}
