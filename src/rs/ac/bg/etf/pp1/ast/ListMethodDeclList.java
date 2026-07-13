// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ListMethodDeclList extends MethodDeclList {

    private MethodDeclList MethodDeclList;
    private SingleMethodDecl SingleMethodDecl;

    public ListMethodDeclList (MethodDeclList MethodDeclList, SingleMethodDecl SingleMethodDecl) {
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
        this.SingleMethodDecl=SingleMethodDecl;
        if(SingleMethodDecl!=null) SingleMethodDecl.setParent(this);
    }

    public MethodDeclList getMethodDeclList() {
        return MethodDeclList;
    }

    public void setMethodDeclList(MethodDeclList MethodDeclList) {
        this.MethodDeclList=MethodDeclList;
    }

    public SingleMethodDecl getSingleMethodDecl() {
        return SingleMethodDecl;
    }

    public void setSingleMethodDecl(SingleMethodDecl SingleMethodDecl) {
        this.SingleMethodDecl=SingleMethodDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
        if(SingleMethodDecl!=null) SingleMethodDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
        if(SingleMethodDecl!=null) SingleMethodDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        if(SingleMethodDecl!=null) SingleMethodDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ListMethodDeclList(\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleMethodDecl!=null)
            buffer.append(SingleMethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ListMethodDeclList]");
        return buffer.toString();
    }
}
