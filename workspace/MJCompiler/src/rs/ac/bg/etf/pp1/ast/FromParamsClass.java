// generated with ast extension for cup
// version 0.8
// 1/1/2019 23:41:19


package rs.ac.bg.etf.pp1.ast;

public class FromParamsClass extends FormPars {

    private Type Type;
    private VarName VarName;
    private TypeIdentBracketsList TypeIdentBracketsList;

    public FromParamsClass (Type Type, VarName VarName, TypeIdentBracketsList TypeIdentBracketsList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarName=VarName;
        if(VarName!=null) VarName.setParent(this);
        this.TypeIdentBracketsList=TypeIdentBracketsList;
        if(TypeIdentBracketsList!=null) TypeIdentBracketsList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarName getVarName() {
        return VarName;
    }

    public void setVarName(VarName VarName) {
        this.VarName=VarName;
    }

    public TypeIdentBracketsList getTypeIdentBracketsList() {
        return TypeIdentBracketsList;
    }

    public void setTypeIdentBracketsList(TypeIdentBracketsList TypeIdentBracketsList) {
        this.TypeIdentBracketsList=TypeIdentBracketsList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(VarName!=null) VarName.accept(visitor);
        if(TypeIdentBracketsList!=null) TypeIdentBracketsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarName!=null) VarName.traverseTopDown(visitor);
        if(TypeIdentBracketsList!=null) TypeIdentBracketsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarName!=null) VarName.traverseBottomUp(visitor);
        if(TypeIdentBracketsList!=null) TypeIdentBracketsList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FromParamsClass(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarName!=null)
            buffer.append(VarName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TypeIdentBracketsList!=null)
            buffer.append(TypeIdentBracketsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FromParamsClass]");
        return buffer.toString();
    }
}
