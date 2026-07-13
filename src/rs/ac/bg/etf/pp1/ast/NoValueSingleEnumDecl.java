// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class NoValueSingleEnumDecl extends SingleEnumDecl {

    private String constName;

    public NoValueSingleEnumDecl (String constName) {
        this.constName=constName;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
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
        buffer.append("NoValueSingleEnumDecl(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NoValueSingleEnumDecl]");
        return buffer.toString();
    }
}
