// generated with ast extension for cup
// version 0.8
// 3/1/2019 15:5:31


package rs.ac.bg.etf.pp1.ast;

public class ConstantDecl extends ConstDecl {

    private Type Type;
    private AssignConstList AssignConstList;

    public ConstantDecl (Type Type, AssignConstList AssignConstList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.AssignConstList=AssignConstList;
        if(AssignConstList!=null) AssignConstList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public AssignConstList getAssignConstList() {
        return AssignConstList;
    }

    public void setAssignConstList(AssignConstList AssignConstList) {
        this.AssignConstList=AssignConstList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(AssignConstList!=null) AssignConstList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(AssignConstList!=null) AssignConstList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(AssignConstList!=null) AssignConstList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignConstList!=null)
            buffer.append(AssignConstList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantDecl]");
        return buffer.toString();
    }
}