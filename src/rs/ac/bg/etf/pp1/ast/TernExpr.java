// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class TernExpr extends Expr {

    private ExprTernary ExprTernary;

    public TernExpr (ExprTernary ExprTernary) {
        this.ExprTernary=ExprTernary;
        if(ExprTernary!=null) ExprTernary.setParent(this);
    }

    public ExprTernary getExprTernary() {
        return ExprTernary;
    }

    public void setExprTernary(ExprTernary ExprTernary) {
        this.ExprTernary=ExprTernary;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprTernary!=null) ExprTernary.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprTernary!=null) ExprTernary.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprTernary!=null) ExprTernary.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TernExpr(\n");

        if(ExprTernary!=null)
            buffer.append(ExprTernary.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TernExpr]");
        return buffer.toString();
    }
}
