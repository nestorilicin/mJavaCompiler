// generated with ast extension for cup
// version 0.8
// 23/1/2026 0:7:21


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private ClassName ClassName;
    private ClassExtends ClassExtends;
    private MultipleVarDecl MultipleVarDecl;
    private ClassOptionalMethods ClassOptionalMethods;

    public ClassDecl (ClassName ClassName, ClassExtends ClassExtends, MultipleVarDecl MultipleVarDecl, ClassOptionalMethods ClassOptionalMethods) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.ClassExtends=ClassExtends;
        if(ClassExtends!=null) ClassExtends.setParent(this);
        this.MultipleVarDecl=MultipleVarDecl;
        if(MultipleVarDecl!=null) MultipleVarDecl.setParent(this);
        this.ClassOptionalMethods=ClassOptionalMethods;
        if(ClassOptionalMethods!=null) ClassOptionalMethods.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public ClassExtends getClassExtends() {
        return ClassExtends;
    }

    public void setClassExtends(ClassExtends ClassExtends) {
        this.ClassExtends=ClassExtends;
    }

    public MultipleVarDecl getMultipleVarDecl() {
        return MultipleVarDecl;
    }

    public void setMultipleVarDecl(MultipleVarDecl MultipleVarDecl) {
        this.MultipleVarDecl=MultipleVarDecl;
    }

    public ClassOptionalMethods getClassOptionalMethods() {
        return ClassOptionalMethods;
    }

    public void setClassOptionalMethods(ClassOptionalMethods ClassOptionalMethods) {
        this.ClassOptionalMethods=ClassOptionalMethods;
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
        if(ClassName!=null) ClassName.accept(visitor);
        if(ClassExtends!=null) ClassExtends.accept(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.accept(visitor);
        if(ClassOptionalMethods!=null) ClassOptionalMethods.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(ClassExtends!=null) ClassExtends.traverseTopDown(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.traverseTopDown(visitor);
        if(ClassOptionalMethods!=null) ClassOptionalMethods.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(ClassExtends!=null) ClassExtends.traverseBottomUp(visitor);
        if(MultipleVarDecl!=null) MultipleVarDecl.traverseBottomUp(visitor);
        if(ClassOptionalMethods!=null) ClassOptionalMethods.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassExtends!=null)
            buffer.append(ClassExtends.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MultipleVarDecl!=null)
            buffer.append(MultipleVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassOptionalMethods!=null)
            buffer.append(ClassOptionalMethods.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}
