// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class IfSingleStatement extends SingleStatement {

    private StatementIf StatementIf;

    public IfSingleStatement (StatementIf StatementIf) {
        this.StatementIf=StatementIf;
        if(StatementIf!=null) StatementIf.setParent(this);
    }

    public StatementIf getStatementIf() {
        return StatementIf;
    }

    public void setStatementIf(StatementIf StatementIf) {
        this.StatementIf=StatementIf;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementIf!=null) StatementIf.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementIf!=null) StatementIf.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementIf!=null) StatementIf.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfSingleStatement(\n");

        if(StatementIf!=null)
            buffer.append(StatementIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfSingleStatement]");
        return buffer.toString();
    }
}
