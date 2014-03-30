/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class Atom implements ClassGenC {

    private ListMaker listMaker;
    private String name;
    private Integer number;
    private String str;

    public Atom(ListMaker listMaker, String name, Integer number, String str) {
        this.listMaker = listMaker;
        this.name = name;
        this.number = number;
        this.str = str;
    }

    public String getType() {
        if (this.name != null) {
            return "name";
        } else if (this.number != null) {
            return "int";
        } else if (this.str != null) {
            return "string";
        } else {
            return "undefined";
        }
    }

    public void genC(PW pw) {
        if (this.name != null) {
            pw.print("_" + this.name);
        } else if (this.number != null) {
            pw.print(String.valueOf(this.number));
        } else if (this.str != null) {
            pw.print(this.str);
        } else if (this.listMaker != null) {
            pw.print("(");
            this.listMaker.genC(pw);
            pw.print(")");
        }
    }
}
