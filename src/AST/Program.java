package AST;

import CompilerSPy.SymbolTable;
import java.util.*;

public class Program {

    public Program(ArrayList<Stmt> listStmt, SymbolTable variaveis) {
        this.listStmt = listStmt;
        this.variaveis = variaveis;
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

        // Iterate through Hashtable entries
        while (it.hasNext()) {
            pw.println("int _" + it.toString() + ";");
        }

//       for (String s : variaveis2){
//           pw.println("int _" + s + ";");
//       }
       
        pw.print("\n\n");
        pw.println("int main(){");
        

        for (Stmt s : listStmt) {
            s.genC(pw);
        }

        pw.print("\n\n");
        pw.println("return 0;");
        pw.println("}");


    }
    private ArrayList<Stmt> listStmt;
    private SymbolTable variaveis;
}