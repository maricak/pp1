package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Struct;

public class Variable {

    String name;
    Struct struct;
    boolean array;
    int line;

    public Variable(String name, Struct struct, boolean array, int line) {
        this.name = name;
        this.struct = struct;
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
        this.struct = other.struct;
        this.line = other.line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Struct getStruct() {
        return struct;
    }

    public void setStruct(Struct struct) {
        this.struct = struct;
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
        return "Variable [name=" + (name != null ? name : "") + ", struct=" + (struct != null ? struct.getKind() : "")
                + ", array=" + array + ", line" + line + "]";
    }

}
