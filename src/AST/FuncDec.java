/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class FuncDec extends CompoundStmt {

    private String name;
    private Parameters parameters;
    private Suite suite;

    public FuncDec(String name, Parameters parameters, Suite suite) {
        this.name = name;
        this.parameters = parameters;
        this.suite = suite;
    }

    @Override
    public void genC(PW pw) {
        pw.print("void * " + this.name + "(");

        this.parameters.genC(pw);
        pw.print("){");
        pw.add();
        this.suite.genC(pw);
        pw.sub();
        pw.println("}");
    }
}
