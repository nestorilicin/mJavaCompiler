// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ElseStatementIf extends StatementIf {

    private Condition Condition;
    private SingleStatement SingleStatement;
    private SingleStatement SingleStatement1;

    public ElseStatementIf (Condition Condition, SingleStatement SingleStatement, SingleStatement SingleStatement1) {
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.SingleStatement=SingleStatement;
        if(SingleStatement!=null) SingleStatement.setParent(this);
        this.SingleStatement1=SingleStatement1;
        if(SingleStatement1!=null) SingleStatement1.setParent(this);
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public SingleStatement getSingleStatement() {
        return SingleStatement;
    }

    public void setSingleStatement(SingleStatement SingleStatement) {
        this.SingleStatement=SingleStatement;
    }

    public SingleStatement getSingleStatement1() {
        return SingleStatement1;
    }

    public void setSingleStatement1(SingleStatement SingleStatement1) {
        this.SingleStatement1=SingleStatement1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Condition!=null) Condition.accept(visitor);
        if(SingleStatement!=null) SingleStatement.accept(visitor);
        if(SingleStatement1!=null) SingleStatement1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(SingleStatement!=null) SingleStatement.traverseTopDown(visitor);
        if(SingleStatement1!=null) SingleStatement1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(SingleStatement!=null) SingleStatement.traverseBottomUp(visitor);
        if(SingleStatement1!=null) SingleStatement1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElseStatementIf(\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleStatement!=null)
            buffer.append(SingleStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleStatement1!=null)
            buffer.append(SingleStatement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ElseStatementIf]");
        return buffer.toString();
    }
}
