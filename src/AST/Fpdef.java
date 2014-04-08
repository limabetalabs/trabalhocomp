/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import AST.PW;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author betalabs
 */
public class Fpdef {

    private String id;
    private List<Fpdef> list;

    public Fpdef(String id) {
        this.list = null;
        this.id = id;
    }

    public Fpdef(List<Fpdef> list) {
        this.id = null;
        this.list = list;
    }

    public void genC(PW pw) {
        if (id == null) {
            Iterator<Fpdef> it = this.list.iterator();
            it.next();
            while (it.hasNext()) {
                Fpdef s = it.next();
                s.genC(pw);

                if (it.hasNext()) {
                    pw.print(",");
                }
            }
        } else {
            pw.print(id);
        }
    }
}
