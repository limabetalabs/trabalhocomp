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

    private String var1, var2, tipo;

    public Target(String var1, String var2, String tipo) {
        this.var1 = var1;
        this.var2 = var2;
        this.tipo = tipo;
    }

    public Target(String var1, String tipo) {
        this.var1 = var1;
        this.tipo = tipo;
    }

    public String getName() {
        if (this.notEmpty(var2)) {
            return var2;
        }else{
            return var1;
        }
    }

    public String getType() {
        return tipo;
    }

    public void setType(String type) {
        this.tipo = type;
    }

    @Override
    public void genC(PW pw) {
        pw.print("_" + this.var1);
    }

    public static boolean notEmpty(String s) {
        return (s != null && s.length() > 0);
    }
}
