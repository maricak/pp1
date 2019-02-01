// generated with ast extension for cup
// version 0.8
// 1/1/2019 21:25:12


package rs.ac.bg.etf.pp1.ast;

public class FromParamsClass extends FormPars {

    private Type Type;
    private String I2;
    private OptionalBrackets OptionalBrackets;
    private TypeIdentBracketsList TypeIdentBracketsList;

    public FromParamsClass (Type Type, String I2, OptionalBrackets OptionalBrackets, TypeIdentBracketsList TypeIdentBracketsList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I2=I2;
        this.OptionalBrackets=OptionalBrackets;
        if(OptionalBrackets!=null) OptionalBrackets.setParent(this);
        this.TypeIdentBracketsList=TypeIdentBracketsList;
        if(TypeIdentBracketsList!=null) TypeIdentBracketsList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public OptionalBrackets getOptionalBrackets() {
        return OptionalBrackets;
    }

    public void setOptionalBrackets(OptionalBrackets OptionalBrackets) {
        this.OptionalBrackets=OptionalBrackets;
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
        if(OptionalBrackets!=null) OptionalBrackets.accept(visitor);
        if(TypeIdentBracketsList!=null) TypeIdentBracketsList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalBrackets!=null) OptionalBrackets.traverseTopDown(visitor);
        if(TypeIdentBracketsList!=null) TypeIdentBracketsList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalBrackets!=null) OptionalBrackets.traverseBottomUp(visitor);
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

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(OptionalBrackets!=null)
            buffer.append(OptionalBrackets.toString("  "+tab));
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
