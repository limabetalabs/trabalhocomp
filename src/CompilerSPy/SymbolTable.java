package CompilerSPy;

import java.util.*;
import AST.*;

public class SymbolTable {

    public SymbolTable() {
        globalTable = new Hashtable();
        localTable = new Hashtable();
    }

    //# corrija: value deve ter o tipo usado para representar classes
    // o mesmo se aplica a getInGlobal
    public Object putInGlobal(String key, Object value) {
        return globalTable.put(key, value);
    }

    public Object getInGlobal(String key) {
        return globalTable.get(key);
    }

    public void removeInGlobal(String key) {
        globalTable.remove(key);
    }

    public Object putInLocal(String key, Object value) {
        return (Object) localTable.put(key, value);
    }

    public Object getInLocal(String key) {
        return (Object) localTable.get(key);
    }

    public Object get(String key) {
        // returns the object corresponding to the key. 
        Object result;
        if ((result = localTable.get(key)) != null) {
            // found local identifier
            return result;
        } else {
            // global identifier, if it is in globalTable
            return globalTable.get(key);
        }
    }

    public void removeLocalIdent() {
        // remove all local identifiers from the table
        localTable.clear();
    }

    public Object getGlobal() {
        return globalTable;
    }

    public Object getLocal() {
        return localTable;
    }

//    public void cleanGlobal() {
//        globalTable = new Hashtable();
//    }

    public void cleanLocal() {
        localTable = new Hashtable();
    }

    private Hashtable globalTable, localTable;

    void setLocalObject(Object aux_local) {
        localTable = (Hashtable) aux_local;
    }
}
