// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class SwitchSingleStatement extends SingleStatement {

    private StatementSwitch StatementSwitch;

    public SwitchSingleStatement (StatementSwitch StatementSwitch) {
        this.StatementSwitch=StatementSwitch;
        if(StatementSwitch!=null) StatementSwitch.setParent(this);
    }

    public StatementSwitch getStatementSwitch() {
        return StatementSwitch;
    }

    public void setStatementSwitch(StatementSwitch StatementSwitch) {
        this.StatementSwitch=StatementSwitch;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementSwitch!=null) StatementSwitch.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementSwitch!=null) StatementSwitch.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementSwitch!=null) StatementSwitch.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SwitchSingleStatement(\n");

        if(StatementSwitch!=null)
            buffer.append(StatementSwitch.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SwitchSingleStatement]");
        return buffer.toString();
    }
}
