package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

public class MyTable extends Tab {

    public static final Struct boolType = new Struct(Struct.Bool);

    public static void init() {
        // currentLevel inicijalizacija
        Tab.init();

        Scope universe = currentScope = new Scope(null);

        universe.addToLocals(new Obj(Obj.Type, "int", intType));
        universe.addToLocals(new Obj(Obj.Type, "char", charType));
        universe.addToLocals(new Obj(Obj.Type, "bool", boolType));
        universe.addToLocals(new Obj(Obj.Con, "eol", charType, 10, 0));
        universe.addToLocals(new Obj(Obj.Con, "null", nullType, 0, 0));

        universe.addToLocals(chrObj = new Obj(Obj.Meth, "chr", charType, 0, 1));
        {
            openScope();
            currentScope.addToLocals(new Obj(Obj.Var, "i", intType, 0, 1));
            chrObj.setLocals(currentScope.getLocals());
            closeScope();
        }

        universe.addToLocals(ordObj = new Obj(Obj.Meth, "ord", intType, 0, 1));
        {
            openScope();
            currentScope.addToLocals(new Obj(Obj.Var, "ch", charType, 0, 1));
            ordObj.setLocals(currentScope.getLocals());
            closeScope();
        }

        universe.addToLocals(lenObj = new Obj(Obj.Meth, "len", intType, 0, 1));
        {
            openScope();
            currentScope.addToLocals(new Obj(Obj.Var, "arr", new Struct(Struct.Array, noType), 0, 1));
            lenObj.setLocals(currentScope.getLocals());
            closeScope();
        }
    }

    public static void dump() {
        Tab.dump(new MyTableDumpVisitor());
    }

    public static Obj insertConstant(int kind, String name, Struct type, int value) {
        Obj ret = Tab.insert(kind, name, type);
        ret.setAdr(value);
        return ret;
    }

    public static boolean existsInCurrentScope(String name) {
        if (currentScope.findSymbol(name) != null)
            return true;
        return false;
    }

    public static boolean equivalent(Struct s1, Struct s2) {
        if (s1.getKind() == Struct.Array && s2.getKind() == Struct.Array) {
            return equivalent(s1.getElemType(), s2.getElemType());
        }
        return s1 == s2;
    }

    public static boolean refType(Struct s) {
        return s.getKind() == Struct.Array || s.getKind() == Struct.Class || s.getKind() == Struct.Interface;
    }

    public static boolean compatible(Struct s1, Struct s2) {
        if (equivalent(s1, s2)) {
            return true;
        }
        if (refType(s1) && s2 == Tab.nullType) {
            return true;
        }
        if (refType(s2) && s1 == Tab.nullType) {
            return true;
        }
        if(s1.getKind() == Struct.Enum && s2.getKind() == Struct.Int) {
            return true;
        }
        if(s1.getKind() == Struct.Int && s2.getKind() == Struct.Enum) {
            return true;
        }
        return false;
    }

    public static boolean assignable(Struct left, Struct right) {
        if (equivalent(left, right)) {
            return true;
        }
        if (refType(left) && right == Tab.nullType) {
            return true;
        }
        if (right.getKind() == Struct.Class) {
            Struct tmp = right.getElemType();
            while (tmp != null) {
                if(tmp == left) {
                    return true;
                }
                tmp = tmp.getElemType();
            } 
            return true;
        }
        if (left.getKind() == Struct.Int && right.getKind() == Struct.Enum) {
            return true;
        }    
        if (left.getKind() == Struct.Interface) {
        
            Struct tmp = right;
            while (tmp != null) {
                Struct found = tmp.getImplementedInterfaces().stream().filter(s -> s == left).findFirst().orElse(null);
                if (found != null) {
                    return true;
                } 
                tmp = tmp.getElemType();
            }           
        }
        return false;
    }
}
