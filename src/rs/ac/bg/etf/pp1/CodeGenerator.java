package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
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

	private Obj reg0, reg1, reg2, reg3;

	public CodeGenerator(Obj reg0, Obj reg1, Obj reg2, Obj reg3) {
		this.reg0 = reg0;
		this.reg1 = reg1;
		this.reg2 = reg2;
		this.reg3 = reg3;
		Obj chr = Tab.find("chr");
		Obj ord = Tab.find("ord");
		Obj len = Tab.find("len");
		// chr() function
		chr.setAdr(Code.pc);
		Code.put(Code.return_);
		// ord() function
		ord.setAdr(Code.pc);
		Code.put(Code.return_);
		// len() function
		len.setAdr(Code.pc);
		Code.put(Code.arraylength);
		Code.put(Code.return_);
	}

	// METHOD
	public void visit(MethodName meth) {
		if ("main".equalsIgnoreCase(meth.getMethodName())) {
			mainPc = Code.pc;
		}
		meth.obj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(meth.obj.getLevel());
		Code.put(meth.obj.getLocalSymbols().size());
	}

	public void visit(SingleMethodDecl methodDecl) {
		if (methodDecl.obj.getType() == Tab.noType) {
			Code.put(Code.exit);
			Code.put(Code.return_);
		} else {
			// sa prezentacije predavanje
			// izaziva runtime gresku ako se stiglo do ovde a nije return bio
			Code.put(Code.trap);
			Code.put(1);
		}
	}

	public void visit(NoExprStatementReturn stmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(ExprStatementReturn stmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(CallDesignatorStatement stmt) {
		Obj obj = stmt.getDesignator().obj;
		if (obj.getFpPos() == 0) {
			// function
			Code.put(Code.call);
			Code.put2(obj.getAdr() - Code.pc + 1);
		} else {
			// TODO klase
		}
	}

	public void visit(CallFactor fact) {
		Obj obj = fact.getDesignator().obj;
		if (obj.getFpPos() == 0) {
			// function
			Code.put(Code.call);
			Code.put2(obj.getAdr() - Code.pc + 1);
		} else {
			// TODO klase
		}
	}

	// PRINT

	public void visit(ExprStatementPrint stmt) {
		Code.loadConst(1);
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
		if (obj.getType().getKind() == Struct.Char)
			Code.put(Code.bread);
		else
			Code.put(Code.read);
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

	public void visit(ListTermExprArithmetic expr) {
		if (expr.getAddop() instanceof PlusAddop) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}

	public void visit(MinusTermExprArithmetic expr) {
		Code.put(Code.neg);
	}

	public void visit(ListFactorTerm term) {
		if (term.getMulop() instanceof MulMulop) {
			Code.put(Code.mul);
		} else if (term.getMulop() instanceof DivMulop) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}
	}

	// ARRAY

	public void visit(NewFactorArray fact) {
		Code.put(Code.newarray);
		if (fact.getType().obj.getType().getKind() == Struct.Char)
			Code.put(0);
		else
			Code.put(1);
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
	private Stack<Integer> ternaryFalseFixups = new Stack<>();
	private Stack<Integer> ternaryEndFixups = new Stack<>();

	public void visit(TernStart dummy) {
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		ternaryFalseFixups.push(Code.pc - 2);
		// if(cond == 0) goto ELSE
	}

	public void visit(TernMid dummy) {
		Code.putJump(0);
		ternaryEndFixups.push(Code.pc - 2);
		Code.fixup(ternaryFalseFixups.pop());
		// expr_true; goto end; else:
	}

	public void visit(ExprTernary expr) {
		Code.fixup(ternaryEndFixups.pop());
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
		Obj arr = stmt.getDesignator1().obj;
		// expr stack : value

		Code.store(reg0); // REG0 = search value

		Code.loadConst(0);
		Code.store(reg1); // REG1 = 0 index

		Code.loadConst(0);
		Code.store(reg2); // REG2 = 0 return value

		int loop = Code.pc;

		Code.load(reg1);
		Code.load(arr);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.lt, 0);
		int exit = Code.pc - 2;

		Code.load(arr);
		Code.load(reg1);
		if (arr.getType().getElemType() == Tab.charType)
			Code.put(Code.baload);
		else
			Code.put(Code.aload);

		Code.load(reg0);

		Code.putFalseJump(Code.eq, 0);
		int next = Code.pc - 2;

		Code.loadConst(1);
		Code.store(reg2);

		Code.putJump(0);
		int end = Code.pc - 2;

		Code.fixup(next);

		Code.load(reg1);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(reg1);

		Code.putJump(loop);

		Code.fixup(exit);

		Code.fixup(end);

		Code.load(reg2);
		Code.store(stmt.getDesignator().obj);

	}

	// IF/ELSE
	private Stack<Integer> ifFalseFixups = new Stack<>();
	private Stack<Integer> ifEndFixups = new Stack<>();

	public void visit(IfStart dummy) {
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		ifFalseFixups.push(Code.pc - 2);
		// if(cond == 0) goto ELSE
	}

	public void visit(ElseStart dummy) {
		Code.putJump(0);
		ifEndFixups.push(Code.pc - 2);
		Code.fixup(ifFalseFixups.pop());
		// expr_true; goto end; else:
	}

	public void visit(IfStatementIf dummy) {
		Code.fixup(ifFalseFixups.pop());
	}

	public void visit(ElseStatementIf dummy) {
		Code.fixup(ifEndFixups.pop());
	}

	// FOR
	private Stack<Integer> forBodyStack = new Stack<>();
	private Stack<Integer> forAfterStack = new Stack<>();
	private Stack<Integer> forExitStack = new Stack<>();
	
	private int forBodyAddr;
	private int forAfterAddr;
	private int forCondAddr;
	private int forExitAddr;
	
	private Stack<List<Integer>> continueStack = new Stack<>();
	private List<Integer> continueAddr;
	
	private Stack<List<Integer>> breakStack = new Stack<>();
	private List<Integer> breakAddr;
	
	public void visit(ForCondStart dummy) {
		forBodyStack.push(forBodyAddr);
		forAfterStack.push(forAfterAddr);
		forExitStack.push(forExitAddr);
		forCondAddr = Code.pc;
		
		continueStack.push(continueAddr);
		breakStack.push(breakAddr);

		
	}
	public void visit(ForCondEnd dummy) {
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		forExitAddr = Code.pc - 2;
		Code.putJump(0);
		forBodyAddr = Code.pc - 2;
		forAfterAddr = Code.pc;
		continueAddr = new ArrayList<Integer>();
		breakAddr = new ArrayList<Integer>();
	}
	public void visit(StatementFor stmt) {
		for(int addr : continueAddr) Code.fixup(addr);
		Code.putJump(forAfterAddr);
		for(int addr : breakAddr) Code.fixup(addr);
		Code.fixup(forExitAddr);
		forBodyAddr = forBodyStack.pop();
		forAfterAddr = forAfterStack.pop();
		forExitAddr = forExitStack.pop();
		continueAddr = continueStack.pop();
		breakAddr = breakStack.pop();
	}
	
	public void visit(ForBodyStart dummy) {
		Code.putJump(forCondAddr);
		Code.fixup(forBodyAddr);
	}
	
	public void visit(StatementContinue stmt) {
		Code.putJump(0);
		continueAddr.add(Code.pc - 2);
	}
	public void visit(StatementBreak stmt) {
		Code.putJump(0);
		breakAddr.add(Code.pc -2);
	}
}
