// generated with ast extension for cup
// version 0.8
// 1/1/2019 23:41:19


package rs.ac.bg.etf.pp1.ast;

public class AssignConstantList extends AssignConstList {

    private AssignConstList AssignConstList;
    private String constName;
    private ConstValue ConstValue;

    public AssignConstantList (AssignConstList AssignConstList, String constName, ConstValue ConstValue) {
        this.AssignConstList=AssignConstList;
        if(AssignConstList!=null) AssignConstList.setParent(this);
        this.constName=constName;
        this.ConstValue=ConstValue;
        if(ConstValue!=null) ConstValue.setParent(this);
    }

    public AssignConstList getAssignConstList() {
        return AssignConstList;
    }

    public void setAssignConstList(AssignConstList AssignConstList) {
        this.AssignConstList=AssignConstList;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public ConstValue getConstValue() {
        return ConstValue;
    }

    public void setConstValue(ConstValue ConstValue) {
        this.ConstValue=ConstValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AssignConstList!=null) AssignConstList.accept(visitor);
        if(ConstValue!=null) ConstValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AssignConstList!=null) AssignConstList.traverseTopDown(visitor);
        if(ConstValue!=null) ConstValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AssignConstList!=null) AssignConstList.traverseBottomUp(visitor);
        if(ConstValue!=null) ConstValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignConstantList(\n");

        if(AssignConstList!=null)
            buffer.append(AssignConstList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(ConstValue!=null)
            buffer.append(ConstValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignConstantList]");
        return buffer.toString();
    }
}
