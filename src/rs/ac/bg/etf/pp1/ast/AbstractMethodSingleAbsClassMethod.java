// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class AbstractMethodSingleAbsClassMethod extends SingleAbsClassMethod {

    private SingleAbstractMethodDecl SingleAbstractMethodDecl;

    public AbstractMethodSingleAbsClassMethod (SingleAbstractMethodDecl SingleAbstractMethodDecl) {
        this.SingleAbstractMethodDecl=SingleAbstractMethodDecl;
        if(SingleAbstractMethodDecl!=null) SingleAbstractMethodDecl.setParent(this);
    }

    public SingleAbstractMethodDecl getSingleAbstractMethodDecl() {
        return SingleAbstractMethodDecl;
    }

    public void setSingleAbstractMethodDecl(SingleAbstractMethodDecl SingleAbstractMethodDecl) {
        this.SingleAbstractMethodDecl=SingleAbstractMethodDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SingleAbstractMethodDecl!=null) SingleAbstractMethodDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingleAbstractMethodDecl!=null) SingleAbstractMethodDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingleAbstractMethodDecl!=null) SingleAbstractMethodDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbstractMethodSingleAbsClassMethod(\n");

        if(SingleAbstractMethodDecl!=null)
            buffer.append(SingleAbstractMethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbstractMethodSingleAbsClassMethod]");
        return buffer.toString();
    }
}
