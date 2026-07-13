// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class StatementSwitch implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Expr Expr;
    private SwitchCaseList SwitchCaseList;

    public StatementSwitch (Expr Expr, SwitchCaseList SwitchCaseList) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.SwitchCaseList=SwitchCaseList;
        if(SwitchCaseList!=null) SwitchCaseList.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public SwitchCaseList getSwitchCaseList() {
        return SwitchCaseList;
    }

    public void setSwitchCaseList(SwitchCaseList SwitchCaseList) {
        this.SwitchCaseList=SwitchCaseList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(SwitchCaseList!=null) SwitchCaseList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(SwitchCaseList!=null) SwitchCaseList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(SwitchCaseList!=null) SwitchCaseList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementSwitch(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchCaseList!=null)
            buffer.append(SwitchCaseList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementSwitch]");
        return buffer.toString();
    }
}
