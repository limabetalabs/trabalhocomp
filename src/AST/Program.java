package AST;

import CompilerSPy.SymbolTable;
import java.util.*;

public class Program {

    public Program(ArrayList<Stmt> listStmt, SymbolTable variaveis, ArrayList<ClassDef> classes, ArrayList<FuncDec> funcoes) {
        this.listStmt = listStmt;
        this.variaveis = variaveis;
        this.classes = classes;
        this.funcoes = funcoes;
    }

    public void genC(PW pw) {

        pw.println("#include <stdio.h>");
        pw.println("#include <stdlib.h>");
        pw.println("#include <malloc.h>");
        pw.println("#include <string.h>\n\n");

        // Get a set of all the entries (key - value pairs) contained in the Hashtable
        Hashtable hs = (Hashtable) variaveis.getGlobal();
        Set entrySet = hs.entrySet();

        // Obtain an Iterator for the entries Set
        Iterator it = entrySet.iterator();

        if (funcoes != null) {
            pw.println("");
            for (FuncDec fc : funcoes) {
                fc.genC(pw);
            }
        }

        if (classes != null) {
            pw.println("");
            for (ClassDef cd : classes) {
                cd.genC(pw);
            }
            pw.println("");
        }

        // Iterate through Hashtable entries
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getValue() != "class" && !entry.getKey().toString().toLowerCase().contains("->")) {
                if (entry.getValue() == "func") {
                    pw.println("typedef void (* " + entry.getKey() + ")();");
                } else if (entry.getValue() != "int" && entry.getValue() != "String" && entry.getValue() != "string" && entry.getValue() != "float") {
                    pw.println("_" + entry.getValue() + " *_" + entry.getKey() + ";");
                } else {
                    pw.println(entry.getValue() + " _" + entry.getKey() + ";");
                }
            }
        }
        pw.println("");

        pw.println("int main(){");

        pw.add();
        for (Stmt s : listStmt) {
            s.genC(pw);
        }

        pw.println("\n\n");
        pw.println("return 0;");
        pw.sub();
        pw.println("}");

    }
    private ArrayList<Stmt> listStmt;
    private SymbolTable variaveis;
    private ArrayList<ClassDef> classes;
    private final ArrayList<FuncDec> funcoes;

}
