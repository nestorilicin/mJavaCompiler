// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ListEnumDeclList extends EnumDeclList {

    private EnumDeclList EnumDeclList;
    private SingleEnumDecl SingleEnumDecl;

    public ListEnumDeclList (EnumDeclList EnumDeclList, SingleEnumDecl SingleEnumDecl) {
        this.EnumDeclList=EnumDeclList;
        if(EnumDeclList!=null) EnumDeclList.setParent(this);
        this.SingleEnumDecl=SingleEnumDecl;
        if(SingleEnumDecl!=null) SingleEnumDecl.setParent(this);
    }

    public EnumDeclList getEnumDeclList() {
        return EnumDeclList;
    }

    public void setEnumDeclList(EnumDeclList EnumDeclList) {
        this.EnumDeclList=EnumDeclList;
    }

    public SingleEnumDecl getSingleEnumDecl() {
        return SingleEnumDecl;
    }

    public void setSingleEnumDecl(SingleEnumDecl SingleEnumDecl) {
        this.SingleEnumDecl=SingleEnumDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumDeclList!=null) EnumDeclList.accept(visitor);
        if(SingleEnumDecl!=null) SingleEnumDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumDeclList!=null) EnumDeclList.traverseTopDown(visitor);
        if(SingleEnumDecl!=null) SingleEnumDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumDeclList!=null) EnumDeclList.traverseBottomUp(visitor);
        if(SingleEnumDecl!=null) SingleEnumDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ListEnumDeclList(\n");

        if(EnumDeclList!=null)
            buffer.append(EnumDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleEnumDecl!=null)
            buffer.append(SingleEnumDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ListEnumDeclList]");
        return buffer.toString();
    }
}
