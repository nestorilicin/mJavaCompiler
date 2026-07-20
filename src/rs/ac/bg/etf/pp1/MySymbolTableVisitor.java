package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class MySymbolTableVisitor extends DumpSymbolTableVisitor {
	protected StringBuilder output = new StringBuilder();

	private final String indent = "    ";
	private int indentLevel = 0;

	private void printIndent() {
		for (int i = 0; i < indentLevel; i++)
			output.append(indent);
	}

	private void incIndent() {
		indentLevel++;
	}

	private void decIndent() {
		if (indentLevel > 0)
			indentLevel--;
	}

	@Override
	public void visitObjNode(Obj obj) {

		switch (obj.getKind()) {
		case Obj.Prog:
			output.append("Program ").append(obj.getName()).append(" {\n");
			incIndent();
			for (Obj local : obj.getLocalSymbols()) {
				printIndent();
				local.accept(this);
				output.append("\n");
			}
			decIndent();
			output.append("}");
			break;

		case Obj.Var:
			output.append("var ").append(obj.getName());
			if ("this".equals(obj.getName())) {
				output.append(" : this");
			} else {
				output.append(" : ");
				obj.getType().accept(this);
			}
			output.append(" [adr=").append(obj.getAdr()).append(", level=").append(obj.getLevel()).append("]");
			break;

		case Obj.Con:
			output.append("const ").append(obj.getName()).append(" : ");
			obj.getType().accept(this);
			output.append(" = ").append(obj.getAdr());
			break;

		case Obj.Type:
			output.append("type ").append(obj.getName()).append(" = ");
			obj.getType().accept(this);
			break;

		case Obj.Fld:
			output.append("field ").append(obj.getName()).append(" : ");
			obj.getType().accept(this);
			output.append(" [adr=").append(obj.getAdr()).append("]");
			break;

		case Obj.Meth:
			if(obj.getFpPos() == 1) output.append("abstract ");
			output.append("method ").append(obj.getName()).append(" : ");
			obj.getType().accept(this);
			output.append(" (adr=").append(obj.getAdr()).append(", level=").append(obj.getLevel()).append(")")
					.append(" {\n");
			incIndent();
			output.append("\n");
			printIndent();
			output.append("Parameters:\n");
			incIndent();
			for (Obj local : obj.getLocalSymbols()) {
				if (local.getFpPos() >= 0) {
					printIndent();
					local.accept(this);
					output.append("\n");
				}
			}
			decIndent();
			printIndent();
			output.append("Locals:\n");
			incIndent();
			for (Obj local : obj.getLocalSymbols()) {
				if (local.getFpPos() < 0) {
					printIndent();
					local.accept(this);
					output.append("\n");
				}
			}
			decIndent();
			decIndent();
			printIndent();
			output.append("}");
			break;
		}
	}

	@Override
	public void visitScopeNode(Scope scope) {
		for (Obj obj : scope.values()) {
			printIndent();
			obj.accept(this);
			output.append("\n");
		}
	}
	@Override
	public void visitStructNode(Struct struct) {
		switch (struct.getKind()) {
		case Struct.None:
			output.append("noType");
			break;
		case Struct.Int:
			output.append("int");
			break;
		case Struct.Char:
			output.append("char");
			break;
		case Struct.Array:
			output.append("array[");
			struct.getElemType().accept(this);
			output.append("]");
			break;
		case Struct.Class:
			output.append("class {\n");
			incIndent();
			for (Obj member : struct.getMembers()) {
				printIndent();
				member.accept(this);
				output.append("\n");
			}
			decIndent();
			printIndent();
			output.append("}");
			break;
		default:
			output.append("unknown");
		}
	}

	public String getOutput() {
		return output.toString();
	}
}
