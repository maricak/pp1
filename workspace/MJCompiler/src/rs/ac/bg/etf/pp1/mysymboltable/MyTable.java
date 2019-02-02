package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

public class MyTable extends Tab {

	public static final Struct boolType = new Struct(MyStruct.Bool);

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
        System.out.println("Adresa " + value);
		ret.setAdr(value);
		return ret;
	}
	
	public static boolean existsInCurrentScope(String name) {	
		if(currentScope.findSymbol(name) != null)
			return true;
        return false;
    }    
}
