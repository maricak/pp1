package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Obj;

public class Variable {

	String name;
	Obj obj;
	boolean array;
	int line;
	
	
	public Variable(String name, Obj obj, boolean array, int line) {	
		this.name = name;
		this.obj = obj;
		this.array = array;
		this.line = line;
	}
	
	public Variable(String name, boolean array, int line) {	
		this.name = name;
		this.array = array;
		this.line = line;
	}
	
	public Variable(Variable other) {
		this.name = new String(other.name);
		this.array = other.array;
		this.obj = other.obj;
		this.line = other.line;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Obj getObj() {
		return obj;
	}
	public void setObj(Obj obj) {
		this.obj = obj;
	}
	public boolean isArray() {
		return array;
	}
	public void setArray(boolean array) {
		this.array = array;
	}
	

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "Variable [name=" + (name != null ? name : "") + ", obj=" + (obj != null ? obj.getName() : "") + ", array=" + array +  ", line" + line + "]";
	}
	
	
	
	
	
}
