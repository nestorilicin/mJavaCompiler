// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class RelopCondFact extends CondFact {

    private ExprArithmetic ExprArithmetic;
    private Relop Relop;
    private ExprArithmetic ExprArithmetic1;

    public RelopCondFact (ExprArithmetic ExprArithmetic, Relop Relop, ExprArithmetic ExprArithmetic1) {
        this.ExprArithmetic=ExprArithmetic;
        if(ExprArithmetic!=null) ExprArithmetic.setParent(this);
        this.Relop=Relop;
        if(Relop!=null) Relop.setParent(this);
        this.ExprArithmetic1=ExprArithmetic1;
        if(ExprArithmetic1!=null) ExprArithmetic1.setParent(this);
    }

    public ExprArithmetic getExprArithmetic() {
        return ExprArithmetic;
    }

    public void setExprArithmetic(ExprArithmetic ExprArithmetic) {
        this.ExprArithmetic=ExprArithmetic;
    }

    public Relop getRelop() {
        return Relop;
    }

    public void setRelop(Relop Relop) {
        this.Relop=Relop;
    }

    public ExprArithmetic getExprArithmetic1() {
        return ExprArithmetic1;
    }

    public void setExprArithmetic1(ExprArithmetic ExprArithmetic1) {
        this.ExprArithmetic1=ExprArithmetic1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprArithmetic!=null) ExprArithmetic.accept(visitor);
        if(Relop!=null) Relop.accept(visitor);
        if(ExprArithmetic1!=null) ExprArithmetic1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprArithmetic!=null) ExprArithmetic.traverseTopDown(visitor);
        if(Relop!=null) Relop.traverseTopDown(visitor);
        if(ExprArithmetic1!=null) ExprArithmetic1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprArithmetic!=null) ExprArithmetic.traverseBottomUp(visitor);
        if(Relop!=null) Relop.traverseBottomUp(visitor);
        if(ExprArithmetic1!=null) ExprArithmetic1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RelopCondFact(\n");

        if(ExprArithmetic!=null)
            buffer.append(ExprArithmetic.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Relop!=null)
            buffer.append(Relop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprArithmetic1!=null)
            buffer.append(ExprArithmetic1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RelopCondFact]");
        return buffer.toString();
    }
}
