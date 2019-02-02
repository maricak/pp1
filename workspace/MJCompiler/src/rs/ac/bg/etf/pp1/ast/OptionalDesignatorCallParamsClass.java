// generated with ast extension for cup
// version 0.8
// 2/1/2019 2:21:53


package rs.ac.bg.etf.pp1.ast;

public class OptionalDesignatorCallParamsClass extends OptionalDesignatorCallParams {

    private DesignatorCallParams DesignatorCallParams;

    public OptionalDesignatorCallParamsClass (DesignatorCallParams DesignatorCallParams) {
        this.DesignatorCallParams=DesignatorCallParams;
        if(DesignatorCallParams!=null) DesignatorCallParams.setParent(this);
    }

    public DesignatorCallParams getDesignatorCallParams() {
        return DesignatorCallParams;
    }

    public void setDesignatorCallParams(DesignatorCallParams DesignatorCallParams) {
        this.DesignatorCallParams=DesignatorCallParams;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorCallParams!=null) DesignatorCallParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorCallParams!=null) DesignatorCallParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorCallParams!=null) DesignatorCallParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptionalDesignatorCallParamsClass(\n");

        if(DesignatorCallParams!=null)
            buffer.append(DesignatorCallParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptionalDesignatorCallParamsClass]");
        return buffer.toString();
    }
}
