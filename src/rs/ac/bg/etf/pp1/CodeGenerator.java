package rs.ac.bg.etf.pp1;

import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.DecDesignatorStatement;
import rs.ac.bg.etf.pp1.ast.DesignatorArray;
import rs.ac.bg.etf.pp1.ast.DesignatorFactor;
import rs.ac.bg.etf.pp1.ast.DivMulop;
import rs.ac.bg.etf.pp1.ast.DotLengthDesignator;
import rs.ac.bg.etf.pp1.ast.EQRelop;
import rs.ac.bg.etf.pp1.ast.EqualDesignatorStatement;
import rs.ac.bg.etf.pp1.ast.ExprCommaNumberStatementPrint;
import rs.ac.bg.etf.pp1.ast.ExprStatementPrint;
import rs.ac.bg.etf.pp1.ast.ExprTernary;
import rs.ac.bg.etf.pp1.ast.FindAnyDesignatorStatement;
import rs.ac.bg.etf.pp1.ast.GERelop;
import rs.ac.bg.etf.pp1.ast.GRelop;
import rs.ac.bg.etf.pp1.ast.IncDesignatorStatement;
import rs.ac.bg.etf.pp1.ast.IndexDesignator;
import rs.ac.bg.etf.pp1.ast.LERelop;
import rs.ac.bg.etf.pp1.ast.LRelop;
import rs.ac.bg.etf.pp1.ast.ListCondFactList;
import rs.ac.bg.etf.pp1.ast.ListCondTermList;
import rs.ac.bg.etf.pp1.ast.ListFactorTerm;
import rs.ac.bg.etf.pp1.ast.ListTermExprArithmetic;
import rs.ac.bg.etf.pp1.ast.MethodName;
import rs.ac.bg.etf.pp1.ast.MinusTermExprArithmetic;
import rs.ac.bg.etf.pp1.ast.MulMulop;
import rs.ac.bg.etf.pp1.ast.NEQRelop;
import rs.ac.bg.etf.pp1.ast.NewFactorArray;
import rs.ac.bg.etf.pp1.ast.NumberFactor;
import rs.ac.bg.etf.pp1.ast.PlusAddop;
import rs.ac.bg.etf.pp1.ast.RelopCondFact;
import rs.ac.bg.etf.pp1.ast.SingleMethodDecl;
import rs.ac.bg.etf.pp1.ast.StatementRead;
import rs.ac.bg.etf.pp1.ast.TernMid;
import rs.ac.bg.etf.pp1.ast.TernStart;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}
	
	private int relopCode;
	

	// METHOD
	public void visit(MethodName meth) {
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

	// READ 
	public void visit(StatementRead stmt) {
		Obj obj = stmt.getDesignator().obj;
		if(obj.getType().getKind() == Struct.Char) Code.put(Code.bread);
		else Code.put(Code.read);
		Code.store(obj);
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
		if (fact.getDesignator() instanceof DotLengthDesignator) {
	        return;
	    }
		if (fact.getDesignator() instanceof IndexDesignator) {
	        Struct elemType = fact.getDesignator().obj.getType();
	        if (elemType == Tab.charType)
	            Code.put(Code.baload);
	        else
	            Code.put(Code.aload);
	    } else {
	        Code.load(fact.getDesignator().obj);
	    }
	}

	public void visit(EqualDesignatorStatement stmt) {
		if (stmt.getDesignator() instanceof IndexDesignator) {
	        Struct elemType = stmt.getDesignator().obj.getType();

	        if (elemType == Tab.charType)
	            Code.put(Code.bastore);
	        else
	            Code.put(Code.astore);
	    } else {
	        Code.store(stmt.getDesignator().obj);
	    }
	}
	
	public void visit(IncDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(obj);
	}
	
	public void visit(DecDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		Code.load(obj);
		Code.loadConst(-1);
		Code.put(Code.add);
		Code.store(obj);
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
	
	// ARRAY
	
	public void visit(NewFactorArray fact) {
		Code.put(Code.newarray);
		if(fact.getType().obj.getType().getKind() == Struct.Char)
			Code.put(0);
		else Code.put(1);
	}
	
	public void visit(DesignatorArray arr) {
	    Code.load(arr.getDesignator().obj);
	}
	
	// LENGTH
	
	public void visit(DotLengthDesignator design) {
		Code.load(design.getDesignator().obj);
		Code.put(Code.arraylength);
		
	}
	
	// TERNARY
	private Stack<Integer> falseFixups = new Stack<>();
	private Stack<Integer> endFixups = new Stack<>();
	public void visit(TernStart dummy) {
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		falseFixups.push(Code.pc - 2);
		// if(cond == 0) goto ELSE
	}
	
	public void visit(TernMid dummy) {
		Code.putJump(0);
		endFixups.push(Code.pc - 2);
		Code.fixup(falseFixups.pop());
		// expr_true; goto end; else:
	}
	public void visit(ExprTernary expr) {
		Code.fixup(endFixups.pop());
	}
	
	// CONDITION
	public void visit(EQRelop relop) {
	    relopCode = Code.eq;
	}

	public void visit(NEQRelop relop) {
	    relopCode = Code.ne;
	}

	public void visit(GRelop relop) {
	    relopCode = Code.gt;
	}

	public void visit(GERelop relop) {
	    relopCode = Code.ge;
	}

	public void visit(LRelop relop) {
	    relopCode = Code.lt;
	}

	public void visit(LERelop relop) {
	    relopCode = Code.le;
	}
	public void visit(RelopCondFact dummy) {

	    int adrFalse;

	    Code.putFalseJump(relopCode, 0);
	    adrFalse = Code.pc - 2;

	    Code.loadConst(1);
	    Code.putJump(0);

	    int adrEnd = Code.pc - 2;

	    Code.fixup(adrFalse);
	    Code.loadConst(0);

	    Code.fixup(adrEnd);
	}
	
	public void visit(ListCondFactList dummy) {
	    Code.put(Code.mul);
	}
	
	public void visit(ListCondTermList node) {

	    Code.put(Code.add);

	    Code.loadConst(0);
	    Code.putFalseJump(Code.ne, 0);

	    int adrFalse = Code.pc - 2;

	    Code.loadConst(1);
	    Code.putJump(0);

	    int adrEnd = Code.pc - 2;

	    Code.fixup(adrFalse);
	    Code.loadConst(0);

	    Code.fixup(adrEnd);
	}
	
	// FIND ANY 
	
	public void visit(FindAnyDesignatorStatement stmt) {
		// TODO
	}
	
	
	
}
