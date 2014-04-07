/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import CompilerSPy.SymbolTable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bruno
 */
public class Target implements ClassGenC {

    private String var1, var2, tipo;
    private SymbolTable globais;

    public Target(String var1, String var2, String tipo, SymbolTable globais) {
        this.var1 = var1;
        this.var2 = var2;
        this.tipo = tipo;
        this.globais = globais;
    }

    public Target(String var1, String tipo, SymbolTable globais) {
        this.var1 = var1;
        this.tipo = tipo;
        this.globais = globais;
    }

    public String getName() {
        if (this.notEmpty(var2)) {
            return var1 + "->" + var2;
        } else {
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
        String s = null;
        if (this.var2 != null) {
            s = this.var1 + "->" + this.var2;
        } else {
            s = this.var1;
        }
        if (globais.getInGlobal(this.var1) == null) {
            pw.print(this.tipo + " _" + s);
        } else {
            pw.print("_" + s);
        }
    }

    public static boolean notEmpty(String s) {
        return (s != null && s.length() > 0);
    }
}
