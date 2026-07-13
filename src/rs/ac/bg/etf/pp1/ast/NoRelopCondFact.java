// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class NoRelopCondFact extends CondFact {

    private ExprArithmetic ExprArithmetic;

    public NoRelopCondFact (ExprArithmetic ExprArithmetic) {
        this.ExprArithmetic=ExprArithmetic;
        if(ExprArithmetic!=null) ExprArithmetic.setParent(this);
    }

    public ExprArithmetic getExprArithmetic() {
        return ExprArithmetic;
    }

    public void setExprArithmetic(ExprArithmetic ExprArithmetic) {
        this.ExprArithmetic=ExprArithmetic;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprArithmetic!=null) ExprArithmetic.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprArithmetic!=null) ExprArithmetic.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprArithmetic!=null) ExprArithmetic.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoRelopCondFact(\n");

        if(ExprArithmetic!=null)
            buffer.append(ExprArithmetic.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NoRelopCondFact]");
        return buffer.toString();
    }
}
