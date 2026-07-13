// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ReadSingleStatement extends SingleStatement {

    private StatementRead StatementRead;

    public ReadSingleStatement (StatementRead StatementRead) {
        this.StatementRead=StatementRead;
        if(StatementRead!=null) StatementRead.setParent(this);
    }

    public StatementRead getStatementRead() {
        return StatementRead;
    }

    public void setStatementRead(StatementRead StatementRead) {
        this.StatementRead=StatementRead;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementRead!=null) StatementRead.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementRead!=null) StatementRead.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementRead!=null) StatementRead.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReadSingleStatement(\n");

        if(StatementRead!=null)
            buffer.append(StatementRead.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReadSingleStatement]");
        return buffer.toString();
    }
}
