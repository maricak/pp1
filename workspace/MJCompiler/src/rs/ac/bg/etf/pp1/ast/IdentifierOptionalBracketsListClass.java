// generated with ast extension for cup
// version 0.8
// 1/1/2019 21:25:12


package rs.ac.bg.etf.pp1.ast;

public class IdentifierOptionalBracketsListClass extends IdentOptionalBracketsList {

    private IdentOptionalBracketsList IdentOptionalBracketsList;
    private String I2;
    private OptionalBrackets OptionalBrackets;

    public IdentifierOptionalBracketsListClass (IdentOptionalBracketsList IdentOptionalBracketsList, String I2, OptionalBrackets OptionalBrackets) {
        this.IdentOptionalBracketsList=IdentOptionalBracketsList;
        if(IdentOptionalBracketsList!=null) IdentOptionalBracketsList.setParent(this);
        this.I2=I2;
        this.OptionalBrackets=OptionalBrackets;
        if(OptionalBrackets!=null) OptionalBrackets.setParent(this);
    }

    public IdentOptionalBracketsList getIdentOptionalBracketsList() {
        return IdentOptionalBracketsList;
    }

    public void setIdentOptionalBracketsList(IdentOptionalBracketsList IdentOptionalBracketsList) {
        this.IdentOptionalBracketsList=IdentOptionalBracketsList;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IdentOptionalBracketsList!=null) IdentOptionalBracketsList.accept(visitor);
        if(OptionalBrackets!=null) OptionalBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IdentOptionalBracketsList!=null) IdentOptionalBracketsList.traverseTopDown(visitor);
        if(OptionalBrackets!=null) OptionalBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IdentOptionalBracketsList!=null) IdentOptionalBracketsList.traverseBottomUp(visitor);
        if(OptionalBrackets!=null) OptionalBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IdentifierOptionalBracketsListClass(\n");

        if(IdentOptionalBracketsList!=null)
            buffer.append(IdentOptionalBracketsList.toString("  "+tab));
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

        buffer.append(tab);
        buffer.append(") [IdentifierOptionalBracketsListClass]");
        return buffer.toString();
    }
}
