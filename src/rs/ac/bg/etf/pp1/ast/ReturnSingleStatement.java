// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ReturnSingleStatement extends SingleStatement {

    private StatementReturn StatementReturn;

    public ReturnSingleStatement (StatementReturn StatementReturn) {
        this.StatementReturn=StatementReturn;
        if(StatementReturn!=null) StatementReturn.setParent(this);
    }

    public StatementReturn getStatementReturn() {
        return StatementReturn;
    }

    public void setStatementReturn(StatementReturn StatementReturn) {
        this.StatementReturn=StatementReturn;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementReturn!=null) StatementReturn.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementReturn!=null) StatementReturn.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementReturn!=null) StatementReturn.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnSingleStatement(\n");

        if(StatementReturn!=null)
            buffer.append(StatementReturn.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnSingleStatement]");
        return buffer.toString();
    }
}
