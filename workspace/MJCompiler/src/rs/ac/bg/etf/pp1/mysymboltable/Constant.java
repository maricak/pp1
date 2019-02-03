package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Obj;

public class Constant {
	
	private Obj obj;
	private int value;
	private String name;
	private int line;
	
	
	public Constant(Obj obj, int value, int line) {	
		this.obj = obj;
		this.value = value;
		this.line = line;
	}

	public Constant(int value, String name, int line) {
		this.value = value;
		this.name = name;
		this.line = line;
	}

	public Constant(Obj obj, int value, String name, int line) {
		this.obj = obj;
		this.value = value;
		this.name = name;
		this.line = line;
    }	
    public Constant(String name, int line) {
        this.name = name;
        this.line = line;
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
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
    }	
    
    
	
	
}
