/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author bruno
 */
public class Target implements ClassGenC {

    private String name, tipo;

    public Target(String name, String tipo) {
        this.name = name;
        this.tipo = tipo;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return tipo;
    }

    public void setType(String type) {
        this.tipo = type;
    }

    @Override
    public void genC(PW pw) {
        pw.print("_" + this.name);
    }
}
