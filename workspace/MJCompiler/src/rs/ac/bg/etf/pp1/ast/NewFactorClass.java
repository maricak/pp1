// generated with ast extension for cup
// version 0.8
// 2/1/2019 19:23:59


package rs.ac.bg.etf.pp1.ast;

public class NewFactorClass extends NewFactor {

    private Type Type;
    private OptionalArraySelector OptionalArraySelector;

    public NewFactorClass (Type Type, OptionalArraySelector OptionalArraySelector) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.OptionalArraySelector=OptionalArraySelector;
        if(OptionalArraySelector!=null) OptionalArraySelector.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public OptionalArraySelector getOptionalArraySelector() {
        return OptionalArraySelector;
    }

    public void setOptionalArraySelector(OptionalArraySelector OptionalArraySelector) {
        this.OptionalArraySelector=OptionalArraySelector;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(OptionalArraySelector!=null) OptionalArraySelector.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalArraySelector!=null) OptionalArraySelector.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalArraySelector!=null) OptionalArraySelector.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NewFactorClass(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalArraySelector!=null)
            buffer.append(OptionalArraySelector.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NewFactorClass]");
        return buffer.toString();
    }
}
