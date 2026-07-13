// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class BoolConstValue extends ConstValue {

    private Boolean boolValue;

    public BoolConstValue (Boolean boolValue) {
        this.boolValue=boolValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue=boolValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BoolConstValue(\n");

        buffer.append(" "+tab+boolValue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolConstValue]");
        return buffer.toString();
    }
}
