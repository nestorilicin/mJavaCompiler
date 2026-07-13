// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class BreakSingleStatement extends SingleStatement {

    private StatementBreak StatementBreak;

    public BreakSingleStatement (StatementBreak StatementBreak) {
        this.StatementBreak=StatementBreak;
        if(StatementBreak!=null) StatementBreak.setParent(this);
    }

    public StatementBreak getStatementBreak() {
        return StatementBreak;
    }

    public void setStatementBreak(StatementBreak StatementBreak) {
        this.StatementBreak=StatementBreak;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementBreak!=null) StatementBreak.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementBreak!=null) StatementBreak.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementBreak!=null) StatementBreak.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BreakSingleStatement(\n");

        if(StatementBreak!=null)
            buffer.append(StatementBreak.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BreakSingleStatement]");
        return buffer.toString();
    }
}
