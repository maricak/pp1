package rs.ac.bg.etf.pp1.mysymboltable;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class MyTableDumpVisitor extends DumpSymbolTableVisitor {

    @Override
    public void visitStructNode(Struct structToVisit) {

        // switch (structToVisit.getKind()) {
        // case Struct.None:
        // output.append("notype");
        // break;
        // case Struct.Int:
        // output.append("int");
        // break;
        // case Struct.Char:
        // output.append("char");
        // break;
        // case Struct.Bool:
        // output.append("bool");
        // break;
        // case Struct.Array:
        // output.append("Arr of ");
        // switch (structToVisit.getElemType().getKind()) {
        // case Struct.None:
        // output.append("notype");
        // break;
        // case Struct.Int:
        // output.append("int");
        // break;
        // case Struct.Char:
        // output.append("char");
        // break;
        // case Struct.Bool:
        // output.append("bool");
        // break;
        // case Struct.Class:
        // output.append("Class");
        // break;
        // case Struct.Interface:
        // output.append("Interface");
        // break;
        // case Struct.Enum:
        // output.append("Enum");
        // break;
        // }
        // break;
        // case Struct.Class:
        // output.append("Class [");
        // if (!structToVisit.getMembers().isEmpty()) {
        // output.append("\n");
        // nextIndentationLevel();
        // for (Obj obj : structToVisit.getMembers()) {
        // output.append(currentIndent.toString());
        // obj.accept(this);
        // // output.append("\n");
        // }
        // previousIndentationLevel();
        // output.append(currentIndent.toString());
        // }
        // output.append("]");
        // break;
        // case Struct.Interface:
        // output.append("Interface[");
        // if (!structToVisit.getMembers().isEmpty()) {

        // output.append("\n");
        // nextIndentationLevel();
        // for (Obj obj : structToVisit.getMembers()) {
        // output.append(currentIndent.toString());
        // obj.accept(this);
        // // output.append("\n");
        // }
        // previousIndentationLevel();
        // output.append(currentIndent.toString());
        // }
        // output.append("]");
        // break;
        // case Struct.Enum:
        // output.append("Enum[");
        // if (!structToVisit.getMembers().isEmpty()) {

        // output.append("\n");
        // nextIndentationLevel();
        // for (Obj obj : structToVisit.getMembers()) {
        // output.append(currentIndent.toString());
        // obj.accept(this);
        // // output.append("\n");
        // }
        // previousIndentationLevel();
        // output.append(currentIndent.toString());
        // }
        // output.append("]");
        // break;
        // }

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
            case Struct.Class:
                output.append("Class");
                break;
            case Struct.Enum:
                output.append("Enum");
                break;
            case Struct.Interface:
                output.append("Interface");
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
        case Struct.Enum:
            output.append("Enum [");
            for (Obj obj : structToVisit.getMembers()) {
                obj.accept(this);
            }
            output.append("]");
            break;
        case Struct.Interface:
            output.append("Interface [");
            for (Obj obj : structToVisit.getMembers()) {
                obj.accept(this);
            }
            output.append("]");
            break;
        }

    }

    @Override
    public void visitObjNode(Obj objToVisit) {
        // // output.append("[");
        // switch (objToVisit.getKind()) {
        // case Obj.Con:
        // output.append("Con ");
        // break;
        // case Obj.Var:
        // output.append("Var ");
        // break;
        // case Obj.Type:
        // output.append("Type ");
        // break;
        // case Obj.Meth:
        // output.append("Meth ");
        // break;
        // case Obj.Fld:
        // output.append("Fld ");
        // break;
        // case Obj.Prog:
        // output.append("Prog ");
        // break;
        // }

        // output.append(objToVisit.getName());
        // output.append(": ");

        // if ((Obj.Var == objToVisit.getKind()) &&
        // "this".equalsIgnoreCase(objToVisit.getName()))
        // output.append("");
        // else
        // objToVisit.getType().accept(this);

        // output.append(", ");
        // output.append(objToVisit.getAdr());
        // output.append(", ");
        // output.append(objToVisit.getLevel() + " ");

        // switch (objToVisit.getKind()) {
        // case Obj.Con:
        // break;
        // case Obj.Var:
        // break;
        // case Obj.Type:
        // break;
        // case Obj.Meth:
        // if (!objToVisit.getLocalSymbols().isEmpty()) {
        // output.append("\n");
        // nextIndentationLevel();
        // for (Obj obj : objToVisit.getLocalSymbols()) {
        // output.append(currentIndent.toString());
        // obj.accept(this);
        // }
        // previousIndentationLevel();
        // }
        // break;
        // case Obj.Fld:
        // break;
        // case Obj.Prog:
        // if (!objToVisit.getLocalSymbols().isEmpty()) {
        // output.append("\n");
        // nextIndentationLevel();
        // for (Obj obj : objToVisit.getLocalSymbols()) {
        // output.append(currentIndent.toString());
        // obj.accept(this);
        // }
        // previousIndentationLevel();
        // }
        // break;
        // }
        // if (objToVisit.getLocalSymbols().isEmpty()) {
        // output.append("\n");
        // }

        // output.append("[");
        switch (objToVisit.getKind()) {
        case Obj.Con:
            output.append("Con ");
            break;
        case Obj.Var:
            output.append("Var ");
            break;
        case Obj.Type:
            output.append("Type ");
            break;
        case Obj.Meth:
            output.append("Meth ");
            break;
        case Obj.Fld:
            output.append("Fld ");
            break;
        case Obj.Prog:
            output.append("Prog ");
            break;
        }

        output.append(objToVisit.getName());
        output.append(": ");

        if ((Obj.Var == objToVisit.getKind()) && "this".equalsIgnoreCase(objToVisit.getName()))
            output.append("");
        else
            objToVisit.getType().accept(this);

        output.append(", ");
        output.append(objToVisit.getAdr());
        output.append(", ");
        output.append(objToVisit.getLevel() + " ");

        if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
            output.append("\n");
            nextIndentationLevel();
        }

        for (Obj o : objToVisit.getLocalSymbols()) {
            output.append(currentIndent.toString());
            o.accept(this);
            output.append("\n");
        }

        if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth)
            previousIndentationLevel();

        // output.append("]");

    }

    @Override
    public String getOutput() {
        String ret = super.getOutput();
        output = new StringBuilder();
        return ret;
    }
}