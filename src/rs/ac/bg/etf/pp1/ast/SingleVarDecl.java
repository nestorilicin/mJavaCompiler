// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class SingleVarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String varName;
    private OptionalSquares OptionalSquares;

    public SingleVarDecl (String varName, OptionalSquares OptionalSquares) {
        this.varName=varName;
        this.OptionalSquares=OptionalSquares;
        if(OptionalSquares!=null) OptionalSquares.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public OptionalSquares getOptionalSquares() {
        return OptionalSquares;
    }

    public void setOptionalSquares(OptionalSquares OptionalSquares) {
        this.OptionalSquares=OptionalSquares;
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
        if(OptionalSquares!=null) OptionalSquares.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalSquares!=null) OptionalSquares.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalSquares!=null) OptionalSquares.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleVarDecl(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(OptionalSquares!=null)
            buffer.append(OptionalSquares.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleVarDecl]");
        return buffer.toString();
    }
}
