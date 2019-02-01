package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Obj;

public class ConstValue {
	
	private Obj obj;
	private int value;
	private String name;
	
	
	public ConstValue(Obj obj, int value) {	
		this.obj = obj;
		this.value = value;
	}

	public ConstValue(int value, String name) {
		super();
		this.value = value;
		this.name = name;
	}

	public ConstValue(Obj obj, int value, String name) {
		this.obj = obj;
		this.value = value;
		this.name = name;
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
}
