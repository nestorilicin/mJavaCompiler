// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ListGlobalDeclList extends GlobalDeclList {

    private SingleGlobalDecl SingleGlobalDecl;
    private GlobalDeclList GlobalDeclList;

    public ListGlobalDeclList (SingleGlobalDecl SingleGlobalDecl, GlobalDeclList GlobalDeclList) {
        this.SingleGlobalDecl=SingleGlobalDecl;
        if(SingleGlobalDecl!=null) SingleGlobalDecl.setParent(this);
        this.GlobalDeclList=GlobalDeclList;
        if(GlobalDeclList!=null) GlobalDeclList.setParent(this);
    }

    public SingleGlobalDecl getSingleGlobalDecl() {
        return SingleGlobalDecl;
    }

    public void setSingleGlobalDecl(SingleGlobalDecl SingleGlobalDecl) {
        this.SingleGlobalDecl=SingleGlobalDecl;
    }

    public GlobalDeclList getGlobalDeclList() {
        return GlobalDeclList;
    }

    public void setGlobalDeclList(GlobalDeclList GlobalDeclList) {
        this.GlobalDeclList=GlobalDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SingleGlobalDecl!=null) SingleGlobalDecl.accept(visitor);
        if(GlobalDeclList!=null) GlobalDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingleGlobalDecl!=null) SingleGlobalDecl.traverseTopDown(visitor);
        if(GlobalDeclList!=null) GlobalDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingleGlobalDecl!=null) SingleGlobalDecl.traverseBottomUp(visitor);
        if(GlobalDeclList!=null) GlobalDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ListGlobalDeclList(\n");

        if(SingleGlobalDecl!=null)
            buffer.append(SingleGlobalDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalDeclList!=null)
            buffer.append(GlobalDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ListGlobalDeclList]");
        return buffer.toString();
    }
}
