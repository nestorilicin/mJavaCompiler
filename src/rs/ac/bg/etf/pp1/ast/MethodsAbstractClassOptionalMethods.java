// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class MethodsAbstractClassOptionalMethods extends AbstractClassOptionalMethods {

    private AbsClassMethodList AbsClassMethodList;

    public MethodsAbstractClassOptionalMethods (AbsClassMethodList AbsClassMethodList) {
        this.AbsClassMethodList=AbsClassMethodList;
        if(AbsClassMethodList!=null) AbsClassMethodList.setParent(this);
    }

    public AbsClassMethodList getAbsClassMethodList() {
        return AbsClassMethodList;
    }

    public void setAbsClassMethodList(AbsClassMethodList AbsClassMethodList) {
        this.AbsClassMethodList=AbsClassMethodList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbsClassMethodList!=null) AbsClassMethodList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbsClassMethodList!=null) AbsClassMethodList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbsClassMethodList!=null) AbsClassMethodList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodsAbstractClassOptionalMethods(\n");

        if(AbsClassMethodList!=null)
            buffer.append(AbsClassMethodList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodsAbstractClassOptionalMethods]");
        return buffer.toString();
    }
}
