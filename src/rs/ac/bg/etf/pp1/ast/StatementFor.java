// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class StatementFor implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ForInit ForInit;
    private ForCond ForCond;
    private ForAfter ForAfter;
    private SingleStatement SingleStatement;

    public StatementFor (ForInit ForInit, ForCond ForCond, ForAfter ForAfter, SingleStatement SingleStatement) {
        this.ForInit=ForInit;
        if(ForInit!=null) ForInit.setParent(this);
        this.ForCond=ForCond;
        if(ForCond!=null) ForCond.setParent(this);
        this.ForAfter=ForAfter;
        if(ForAfter!=null) ForAfter.setParent(this);
        this.SingleStatement=SingleStatement;
        if(SingleStatement!=null) SingleStatement.setParent(this);
    }

    public ForInit getForInit() {
        return ForInit;
    }

    public void setForInit(ForInit ForInit) {
        this.ForInit=ForInit;
    }

    public ForCond getForCond() {
        return ForCond;
    }

    public void setForCond(ForCond ForCond) {
        this.ForCond=ForCond;
    }

    public ForAfter getForAfter() {
        return ForAfter;
    }

    public void setForAfter(ForAfter ForAfter) {
        this.ForAfter=ForAfter;
    }

    public SingleStatement getSingleStatement() {
        return SingleStatement;
    }

    public void setSingleStatement(SingleStatement SingleStatement) {
        this.SingleStatement=SingleStatement;
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
        if(ForInit!=null) ForInit.accept(visitor);
        if(ForCond!=null) ForCond.accept(visitor);
        if(ForAfter!=null) ForAfter.accept(visitor);
        if(SingleStatement!=null) SingleStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForInit!=null) ForInit.traverseTopDown(visitor);
        if(ForCond!=null) ForCond.traverseTopDown(visitor);
        if(ForAfter!=null) ForAfter.traverseTopDown(visitor);
        if(SingleStatement!=null) SingleStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForInit!=null) ForInit.traverseBottomUp(visitor);
        if(ForCond!=null) ForCond.traverseBottomUp(visitor);
        if(ForAfter!=null) ForAfter.traverseBottomUp(visitor);
        if(SingleStatement!=null) SingleStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementFor(\n");

        if(ForInit!=null)
            buffer.append(ForInit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForCond!=null)
            buffer.append(ForCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForAfter!=null)
            buffer.append(ForAfter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleStatement!=null)
            buffer.append(SingleStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementFor]");
        return buffer.toString();
    }
}
