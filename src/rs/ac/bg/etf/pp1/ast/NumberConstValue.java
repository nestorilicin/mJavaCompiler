// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class NumberConstValue extends ConstValue {

    private Integer numberValue;

    public NumberConstValue (Integer numberValue) {
        this.numberValue=numberValue;
    }

    public Integer getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(Integer numberValue) {
        this.numberValue=numberValue;
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
        buffer.append("NumberConstValue(\n");

        buffer.append(" "+tab+numberValue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NumberConstValue]");
        return buffer.toString();
    }
}
