// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ForSingleStatement extends SingleStatement {

    private StatementFor StatementFor;

    public ForSingleStatement (StatementFor StatementFor) {
        this.StatementFor=StatementFor;
        if(StatementFor!=null) StatementFor.setParent(this);
    }

    public StatementFor getStatementFor() {
        return StatementFor;
    }

    public void setStatementFor(StatementFor StatementFor) {
        this.StatementFor=StatementFor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementFor!=null) StatementFor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementFor!=null) StatementFor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementFor!=null) StatementFor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForSingleStatement(\n");

        if(StatementFor!=null)
            buffer.append(StatementFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForSingleStatement]");
        return buffer.toString();
    }
}
