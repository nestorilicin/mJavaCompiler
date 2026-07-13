// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class AbstractClassSingleGlobalDecl extends SingleGlobalDecl {

    private AbstractClassDecl AbstractClassDecl;

    public AbstractClassSingleGlobalDecl (AbstractClassDecl AbstractClassDecl) {
        this.AbstractClassDecl=AbstractClassDecl;
        if(AbstractClassDecl!=null) AbstractClassDecl.setParent(this);
    }

    public AbstractClassDecl getAbstractClassDecl() {
        return AbstractClassDecl;
    }

    public void setAbstractClassDecl(AbstractClassDecl AbstractClassDecl) {
        this.AbstractClassDecl=AbstractClassDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbstractClassDecl!=null) AbstractClassDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbstractClassDecl!=null) AbstractClassDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbstractClassDecl!=null) AbstractClassDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbstractClassSingleGlobalDecl(\n");

        if(AbstractClassDecl!=null)
            buffer.append(AbstractClassDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbstractClassSingleGlobalDecl]");
        return buffer.toString();
    }
}
