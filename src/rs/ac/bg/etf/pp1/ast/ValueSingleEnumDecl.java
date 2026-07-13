// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ValueSingleEnumDecl extends SingleEnumDecl {

    private String constName;
    private Integer enumValue;

    public ValueSingleEnumDecl (String constName, Integer enumValue) {
        this.constName=constName;
        this.enumValue=enumValue;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public Integer getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(Integer enumValue) {
        this.enumValue=enumValue;
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
        buffer.append("ValueSingleEnumDecl(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(" "+tab+enumValue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ValueSingleEnumDecl]");
        return buffer.toString();
    }
}
