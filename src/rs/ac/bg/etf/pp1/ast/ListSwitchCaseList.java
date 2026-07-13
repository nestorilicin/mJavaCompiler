// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ListSwitchCaseList extends SwitchCaseList {

    private SwitchCaseList SwitchCaseList;
    private SingleCase SingleCase;

    public ListSwitchCaseList (SwitchCaseList SwitchCaseList, SingleCase SingleCase) {
        this.SwitchCaseList=SwitchCaseList;
        if(SwitchCaseList!=null) SwitchCaseList.setParent(this);
        this.SingleCase=SingleCase;
        if(SingleCase!=null) SingleCase.setParent(this);
    }

    public SwitchCaseList getSwitchCaseList() {
        return SwitchCaseList;
    }

    public void setSwitchCaseList(SwitchCaseList SwitchCaseList) {
        this.SwitchCaseList=SwitchCaseList;
    }

    public SingleCase getSingleCase() {
        return SingleCase;
    }

    public void setSingleCase(SingleCase SingleCase) {
        this.SingleCase=SingleCase;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SwitchCaseList!=null) SwitchCaseList.accept(visitor);
        if(SingleCase!=null) SingleCase.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchCaseList!=null) SwitchCaseList.traverseTopDown(visitor);
        if(SingleCase!=null) SingleCase.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchCaseList!=null) SwitchCaseList.traverseBottomUp(visitor);
        if(SingleCase!=null) SingleCase.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ListSwitchCaseList(\n");

        if(SwitchCaseList!=null)
            buffer.append(SwitchCaseList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleCase!=null)
            buffer.append(SingleCase.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ListSwitchCaseList]");
        return buffer.toString();
    }
}
