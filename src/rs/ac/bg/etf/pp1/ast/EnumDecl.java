// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class EnumDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private EnumName EnumName;
    private SingleEnumDecl SingleEnumDecl;
    private EnumDeclList EnumDeclList;

    public EnumDecl (EnumName EnumName, SingleEnumDecl SingleEnumDecl, EnumDeclList EnumDeclList) {
        this.EnumName=EnumName;
        if(EnumName!=null) EnumName.setParent(this);
        this.SingleEnumDecl=SingleEnumDecl;
        if(SingleEnumDecl!=null) SingleEnumDecl.setParent(this);
        this.EnumDeclList=EnumDeclList;
        if(EnumDeclList!=null) EnumDeclList.setParent(this);
    }

    public EnumName getEnumName() {
        return EnumName;
    }

    public void setEnumName(EnumName EnumName) {
        this.EnumName=EnumName;
    }

    public SingleEnumDecl getSingleEnumDecl() {
        return SingleEnumDecl;
    }

    public void setSingleEnumDecl(SingleEnumDecl SingleEnumDecl) {
        this.SingleEnumDecl=SingleEnumDecl;
    }

    public EnumDeclList getEnumDeclList() {
        return EnumDeclList;
    }

    public void setEnumDeclList(EnumDeclList EnumDeclList) {
        this.EnumDeclList=EnumDeclList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumName!=null) EnumName.accept(visitor);
        if(SingleEnumDecl!=null) SingleEnumDecl.accept(visitor);
        if(EnumDeclList!=null) EnumDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumName!=null) EnumName.traverseTopDown(visitor);
        if(SingleEnumDecl!=null) SingleEnumDecl.traverseTopDown(visitor);
        if(EnumDeclList!=null) EnumDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumName!=null) EnumName.traverseBottomUp(visitor);
        if(SingleEnumDecl!=null) SingleEnumDecl.traverseBottomUp(visitor);
        if(EnumDeclList!=null) EnumDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDecl(\n");

        if(EnumName!=null)
            buffer.append(EnumName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleEnumDecl!=null)
            buffer.append(SingleEnumDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumDeclList!=null)
            buffer.append(EnumDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDecl]");
        return buffer.toString();
    }
}
