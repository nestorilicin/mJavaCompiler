package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.DesignatorFactor;
import rs.ac.bg.etf.pp1.ast.DivMulop;
import rs.ac.bg.etf.pp1.ast.EqualDesignatorStatement;
import rs.ac.bg.etf.pp1.ast.ExprCommaNumberStatementPrint;
import rs.ac.bg.etf.pp1.ast.ExprStatementPrint;
import rs.ac.bg.etf.pp1.ast.ListFactorTerm;
import rs.ac.bg.etf.pp1.ast.ListTermExprArithmetic;
import rs.ac.bg.etf.pp1.ast.MethodName;
import rs.ac.bg.etf.pp1.ast.MinusTermExprArithmetic;
import rs.ac.bg.etf.pp1.ast.MulMulop;
import rs.ac.bg.etf.pp1.ast.NumberFactor;
import rs.ac.bg.etf.pp1.ast.PlusAddop;
import rs.ac.bg.etf.pp1.ast.SingleMethodDecl;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}

	// METHOD
	public void visit(MethodName meth) {
		System.out.println("Method: " + meth.getMethodName());

		if ("main".equalsIgnoreCase(meth.getMethodName())) {
			mainPc = Code.pc;
		}
		meth.obj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(0);
		Code.put(meth.obj.getLocalSymbols().size());
	}

	public void visit(SingleMethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	// PRINT

	public void visit(ExprStatementPrint stmt) {
		Code.loadConst(5);
		if (stmt.getExpr().obj.getType() == Tab.charType)
			Code.put(Code.bprint);
		else
			Code.put(Code.print);
	}

	public void visit(ExprCommaNumberStatementPrint stmt) {
		Code.loadConst(stmt.getN2());
		if (stmt.getExpr().obj.getType() == Tab.charType)
			// char
			Code.put(Code.bprint);
		else
			// int i bool
			Code.put(Code.print);
	}

	// FACTOR

	public void visit(NumberFactor fact) {
		Code.loadConst(fact.getN1());
	}

	public void visit(CharFactor fact) {
		Code.loadConst(fact.getC1());
	}

	public void visit(BoolFactor fact) {
		Code.loadConst(fact.getB1() ? 1 : 0);
	}

	// DESIGNATOR
	public void visit(DesignatorFactor fact) {
		Code.load(fact.getDesignator().obj);
	}

	public void visit(EqualDesignatorStatement stmt) {
		Code.store(stmt.getDesignator().obj);
	}
	
	// ARITHMETICS
	
	public void visit(ListTermExprArithmetic expr){
		if(expr.getAddop() instanceof PlusAddop) {
			Code.put(Code.add);
		}
		else {
			Code.put(Code.sub);
		}
	}
	
	public void visit(MinusTermExprArithmetic expr) {
		Code.put(Code.neg);
	}
	
	public void visit(ListFactorTerm term) {
		if(term.getMulop() instanceof MulMulop) {
			Code.put(Code.mul);
		}
		else if (term.getMulop() instanceof DivMulop) {
			Code.put(Code.div);
		}
		else {
			Code.put(Code.rem);
		}
	}
}
