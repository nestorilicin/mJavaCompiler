// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class PrintSingleStatement extends SingleStatement {

    private StatementPrint StatementPrint;

    public PrintSingleStatement (StatementPrint StatementPrint) {
        this.StatementPrint=StatementPrint;
        if(StatementPrint!=null) StatementPrint.setParent(this);
    }

    public StatementPrint getStatementPrint() {
        return StatementPrint;
    }

    public void setStatementPrint(StatementPrint StatementPrint) {
        this.StatementPrint=StatementPrint;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementPrint!=null) StatementPrint.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementPrint!=null) StatementPrint.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementPrint!=null) StatementPrint.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintSingleStatement(\n");

        if(StatementPrint!=null)
            buffer.append(StatementPrint.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintSingleStatement]");
        return buffer.toString();
    }
}
