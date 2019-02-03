package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class MyTableDumpVisitor extends DumpSymbolTableVisitor {

    @Override
    public void visitStructNode(Struct structToVisit) {

        switch (structToVisit.getKind()) {
        case Struct.None:
            output.append("notype");
            break;
        case Struct.Int:
            output.append("int");
            break;
        case Struct.Char:
            output.append("char");
            break;
        case Struct.Bool:
            output.append("bool");
            break;
        case Struct.Array:
            output.append("Arr of ");

            switch (structToVisit.getElemType().getKind()) {
            case Struct.None:
                output.append("notype");
                break;
            case Struct.Int:
                output.append("int");
                break;
            case Struct.Char:
                output.append("char");
                break;
            case Struct.Bool:
                output.append("bool");
                break;
            case Struct.Class:
                output.append("Class");
                break;
            case Struct.Interface:
                output.append("Interface");
                break;
            case Struct.Enum:
                output.append("Enum");
                break;
            }
            break;
        case Struct.Class:
            output.append("Class [");
            for (Obj obj : structToVisit.getMembers()) {
                obj.accept(this);
            }
            output.append("]");
            break;
        case Struct.Interface:
            output.append("Interface[");
            for (Obj obj : structToVisit.getMembers()) {
                obj.accept(this);
            }
            output.append("]");
            break;        
        case Struct.Enum:
            output.append("Enum[");
            for (Obj obj : structToVisit.getMembers()) {
                obj.accept(this);
            }
            output.append("]");
            break;
        }    
    }
}