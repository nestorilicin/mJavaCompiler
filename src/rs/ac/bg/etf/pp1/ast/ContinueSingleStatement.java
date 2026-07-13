// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ContinueSingleStatement extends SingleStatement {

    private StatementContinue StatementContinue;

    public ContinueSingleStatement (StatementContinue StatementContinue) {
        this.StatementContinue=StatementContinue;
        if(StatementContinue!=null) StatementContinue.setParent(this);
    }

    public StatementContinue getStatementContinue() {
        return StatementContinue;
    }

    public void setStatementContinue(StatementContinue StatementContinue) {
        this.StatementContinue=StatementContinue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementContinue!=null) StatementContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementContinue!=null) StatementContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementContinue!=null) StatementContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ContinueSingleStatement(\n");

        if(StatementContinue!=null)
            buffer.append(StatementContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ContinueSingleStatement]");
        return buffer.toString();
    }
}
