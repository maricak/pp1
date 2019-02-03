package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class MyStruct extends Struct {

    public static final int Bool = 5;
    public static final int Interface = 6;
    public static final int Enum = 7;

    private int myKind;

	public MyStruct(int kind) {
        super(kind);
        myKind = kind;
	}

	public MyStruct(int kind, Struct elemType) {
        super(kind, elemType);
        myKind = kind;        
	}

	public MyStruct(int kind, SymbolDataStructure members) {
        super(kind, members);
        myKind = kind;
    }
    
    @Override
    public boolean isRefType() {
		return super.isRefType() || myKind == Interface;
    }
    
    @Override
    public boolean assignableTo(Struct dest) {
        return super.assignableTo(dest);
        // || provera za interfejse i klase
    }

    
}
