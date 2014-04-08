/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import CompilerSPy.SymbolTable;

/**
 *
 * @author bruno
 */
public class Atom implements ClassGenC {

    private ListMaker listMaker;
    private String name;
    private String name2;
    private Integer number;
    private String str;
    private SymbolTable globais;

    public Atom(ListMaker listMaker, String name, Integer number, String str, String name2, SymbolTable globais) {
        this.listMaker = listMaker;
        this.name = name;
        this.name2 = name2;
        this.number = number;
        this.str = str;
        this.globais = globais;
    }

    public String getType() {
        if (this.name != null || this.name2 != null) {
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
            if (globais.getInGlobal(this.name) == "class") {
                pw.print("new _" + this.name + "()");
            } else {
                pw.print("_" + this.name);
            }
        } else if (this.number != null) {
            pw.print(String.valueOf(this.number));
        } else if (this.str != null) {
            pw.print('"'+this.str+'"');
        } else if (this.listMaker != null) {
            pw.print("(");
            this.listMaker.genC(pw);
            pw.print(")");
        }
    }

    String getName() {
        if (this.name != null || this.name2 != null) {
            if (this.notEmpty(this.name2)) {
                return this.name2;
            } else {
                return this.name;
            }
        } else if (this.number != null) {
            return Integer.toString(this.number);
        } else if (this.str != null) {
            return this.str;
        } else {
            return "undefined";
        }
    }

    public static boolean notEmpty(String s) {
        return (s != null && s.length() > 0);
    }
}
