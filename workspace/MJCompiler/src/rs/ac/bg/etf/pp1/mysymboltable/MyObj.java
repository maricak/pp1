package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class MyObj extends Obj{
	
	public static final int Intrf = 7;

	public MyObj(int kind, String name, Struct type) {
		super(kind, name, type);
	}

	public MyObj(int kind, String name, Struct type, int adr, int level) {
		super(kind, name, type, adr, level);
	}
}
