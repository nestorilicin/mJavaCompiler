// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ListAbsClassMethodList extends AbsClassMethodList {

    private AbsClassMethodList AbsClassMethodList;
    private SingleAbsClassMethod SingleAbsClassMethod;

    public ListAbsClassMethodList (AbsClassMethodList AbsClassMethodList, SingleAbsClassMethod SingleAbsClassMethod) {
        this.AbsClassMethodList=AbsClassMethodList;
        if(AbsClassMethodList!=null) AbsClassMethodList.setParent(this);
        this.SingleAbsClassMethod=SingleAbsClassMethod;
        if(SingleAbsClassMethod!=null) SingleAbsClassMethod.setParent(this);
    }

    public AbsClassMethodList getAbsClassMethodList() {
        return AbsClassMethodList;
    }

    public void setAbsClassMethodList(AbsClassMethodList AbsClassMethodList) {
        this.AbsClassMethodList=AbsClassMethodList;
    }

    public SingleAbsClassMethod getSingleAbsClassMethod() {
        return SingleAbsClassMethod;
    }

    public void setSingleAbsClassMethod(SingleAbsClassMethod SingleAbsClassMethod) {
        this.SingleAbsClassMethod=SingleAbsClassMethod;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbsClassMethodList!=null) AbsClassMethodList.accept(visitor);
        if(SingleAbsClassMethod!=null) SingleAbsClassMethod.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbsClassMethodList!=null) AbsClassMethodList.traverseTopDown(visitor);
        if(SingleAbsClassMethod!=null) SingleAbsClassMethod.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbsClassMethodList!=null) AbsClassMethodList.traverseBottomUp(visitor);
        if(SingleAbsClassMethod!=null) SingleAbsClassMethod.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ListAbsClassMethodList(\n");

        if(AbsClassMethodList!=null)
            buffer.append(AbsClassMethodList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleAbsClassMethod!=null)
            buffer.append(SingleAbsClassMethod.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ListAbsClassMethodList]");
        return buffer.toString();
    }
}
