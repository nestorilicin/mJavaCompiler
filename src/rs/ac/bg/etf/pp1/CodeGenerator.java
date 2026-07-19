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
	public CodeGenerator(SemanticAnalyzer semAnal) {
		// pomocne promenljive
		this.reg0 = semAnal.getReg0();
		this.reg1 = semAnal.getReg1();
		this.reg2 = semAnal.getReg2();

		// ugradjena imena
		Obj chr = Tab.find("chr");
		Obj ord = Tab.find("ord");
		Obj len = Tab.find("len");

		chr.setAdr(Code.pc);
		Code.put(Code.return_);
		ord.setAdr(Code.pc);
		Code.put(Code.return_);
		len.setAdr(Code.pc);
		Code.put(Code.arraylength);
		Code.put(Code.return_);
	}

	private int mainPc;

	public int getMainPc() {
		return mainPc;
	}

	private int relopCode;

	private Obj reg0, reg1, reg2;

	// CONDITION
	private List<Integer> falseAddr; // ifFalse -> else
	private List<Integer> trueAddr; // ifTrue -> then
	private Stack<List<Integer>> falseStack = new Stack<>(); // za ugnjezdavanje
	private Stack<List<Integer>> trueStack = new Stack<>(); // za ugnjezdavanje

	private int skipAdr = -1; // goto END
	private Stack<Integer> skipStack = new Stack<>(); // ugnjezdavanje

	private boolean firstCond = true; // prvi uslov od mnogih

	// FOR
	private Stack<Integer> forBodyStack = new Stack<>();
	private Stack<Integer> forAfterStack = new Stack<>();

	private int forBodyAddr;
	private int forAfterAddr;
	private int forCondAddr;

	private Stack<List<Integer>> continueStack = new Stack<>();
	private List<Integer> continueAddr;

	private Stack<List<Integer>> breakStack = new Stack<>();
	private List<Integer> breakAddr;

	// MAP
	private int mapLoopStart = -1;
	private int mapExitJump = -1;
	private Obj mapDst = null;
	private Obj mapSrc = null;

	private boolean insideMap = false;
	private String mapVarName;

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
		// stiglo se do kraja metode a nije pozvan return
		// ako je void okej
		// ako nije void trebalo bi da ima return, runtime greska trap 1
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

	// RETURN
	public void visit(NoExprStatementReturn stmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		// expr stek prazan
	}

	public void visit(ExprStatementReturn stmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		// izracunata vrednost na expr steku
	}

	// METHOD CALL
	public void visit(CallDesignatorStatement stmt) {
		Obj obj = stmt.getDesignator().obj;
		if (obj.getFpPos() == 0) {
			// funkcija
			Code.put(Code.call);
			Code.put2(obj.getAdr() - Code.pc + 1);
		} else {
			// TODO klase
		}
	}

	public void visit(CallFactor fact) {
		Obj obj = fact.getDesignator().obj;
		if (obj.getFpPos() == 0) {
			// funkcija
			Code.put(Code.call);
			Code.put2(obj.getAdr() - Code.pc + 1);
		} else {
			// TODO klase
		}
	}

	// ARRAY

	public void visit(NewFactorArray fact) {
		// expr stek : arraysize
		Code.put(Code.newarray);
		if (fact.getType().obj.getType().getKind() == Struct.Char)
			Code.put(0);
		else
			Code.put(1);
		// expr stek : arrayaddr
	}

	public void visit(DesignatorArray arr) {
		Code.load(arr.getDesignator().obj);
		// expr stek: array addr
	}

	// LENGTH

	public void visit(DotLengthDesignator design) {
		Code.load(design.getDesignator().obj);
		// expr stek : arrayaddr
		Code.put(Code.arraylength);
		// expr stek : len(arrayaddr)
	}

	// CONDITION

	private void newContextCondition() {
		// helper za otvaranje novog konteksta
		if (firstCond) {
			firstCond = false;
			falseStack.push(falseAddr);
			trueStack.push(trueAddr);
			falseAddr = new ArrayList<Integer>();
			trueAddr = new ArrayList<Integer>();
		}
	}

	public void visit(SingleCondTermList dummy) {
		// poslednji u nizu ORova mimi oro najjace
		newContextCondition();

		falseAddr.add(Code.pc - 2);
	}

	public void visit(RelopCondFact dummy) {
		newContextCondition();

		// expr stek: op1 op2
		Code.putFalseJump(relopCode, 0);

		falseAddr.add(Code.pc - 2);
	}

	public void visit(NoRelopCondFact dummy) {
		newContextCondition();

		// expr stek: vrednost
		Code.put(Code.const_1);
		// expr stek: vrednost 1
		Code.putFalseJump(Code.eq, 0);
		// if(vrednost != 1) goto ELSE
		falseAddr.add(Code.pc - 2);
	}

	public void visit(OrSign dummy) {
		// levi u oru je tacan, ostali se ne racunaju
		Code.putJump(0);
		trueAddr.add(Code.pc - 2);
		// levu u oru je netacan, izracunava se sledeci
		for (int adr : falseAddr)
			Code.fixup(adr);
		falseAddr = new ArrayList<Integer>();
	}

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

	// IF/ELSE

	public void visit(IfStart dummy) {
		// novi if push starog
		skipStack.push(skipAdr);
	}

	public void visit(IfEnd dummy) {
		// THEN:
		firstCond = true;
		// OR short-circuit gadjaju ovde
		for (int adr : trueAddr)
			Code.fixup(adr);
	}

	public void visit(ElseStart dummy) {
		// THEN: goto END
		Code.putJump(0);
		skipAdr = Code.pc - 2;
		// ifFalse gadjaju ovde
		for (int adr : falseAddr)
			Code.fixup(adr);
	}

	public void visit(IfStatementIf dummy) {
		// if bez else
		// cond ifFalse goto END
		// THEN: stmt
		// END:
		// ifFalse gadjaju ovde
		for (int adr : falseAddr)
			Code.fixup(adr);
		// kraj skida se kontekst
		falseAddr = falseStack.pop();
		trueAddr = trueStack.pop();
		skipAdr = skipStack.pop();
	}

	public void visit(ElseStatementIf dummy) {
		// if sa else
		// goto END
		Code.fixup(skipAdr);
		// kraj skida se kontekst
		falseAddr = falseStack.pop();
		trueAddr = trueStack.pop();
		skipAdr = skipStack.pop();
	}

	// FOR

	public void visit(ForCondStart dummy) {
		// cuvanje konteksta zbog ugnjezdavanja
		forBodyStack.push(forBodyAddr);
		forAfterStack.push(forAfterAddr);
		continueStack.push(continueAddr);
		breakStack.push(breakAddr);
		// CONDITION:
		forCondAddr = Code.pc;

	}

	public void visit(ForCondEnd dummy) {
		firstCond = true;

		// goto BODY:
		Code.putJump(0);
		forBodyAddr = Code.pc - 2;
		// AFTER:
		forAfterAddr = Code.pc;
		continueAddr = new ArrayList<>();
		breakAddr = new ArrayList<>();
	}

	public void visit(StatementFor stmt) {
		// continue -> AFTER
		for (int adr : continueAddr)
			Code.fixup(adr);
		// BODY -> AFTER
		Code.putJump(forAfterAddr);
		// break -> END
		for (int adr : breakAddr)
			Code.fixup(adr);
		// false -> END
		for (int adr : falseAddr)
			Code.fixup(adr);

		// skidanje konteksta za for i za condition
		forBodyAddr = forBodyStack.pop();
		forAfterAddr = forAfterStack.pop();
		continueAddr = continueStack.pop();
		breakAddr = breakStack.pop();
		falseAddr = falseStack.pop();
		trueAddr = trueStack.pop();
	}

	public void visit(ForBodyStart dummy) {
		// AFTER -> CONDTION
		Code.putJump(forCondAddr);
		// fixup BODY
		Code.fixup(forBodyAddr);
		// or fixup
		for (int adr : trueAddr)
			Code.fixup(adr);
	}

	public void visit(NoForCond dummy) {
		// bez uslova, samo se otvara novi kontekst
		falseStack.push(falseAddr);
		trueStack.push(trueAddr);
		falseAddr = new ArrayList<>();
		trueAddr = new ArrayList<>();
	}

	// BREAK
	public void visit(StatementBreak stmt) {
		// break -> END
		Code.putJump(0);
		breakAddr.add(Code.pc - 2);
	}

	// CONTINUE
	public void visit(StatementContinue stmt) {
		// continue -> AFTER
		Code.putJump(0);
		continueAddr.add(Code.pc - 2);
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

	// TERNARY

	public void visit(TernStart dummy) {
		skipStack.push(skipAdr);
		firstCond = true;
		for (int adr : trueAddr)
			Code.fixup(adr);
	}

	public void visit(TernMid dummy) {
		Code.putJump(0);
		skipAdr = Code.pc - 2;
		for (int adr : falseAddr)
			Code.fixup(adr);
		// expr_true; goto end; else:
	}

	public void visit(TernExpr expr) {
		Code.fixup(skipAdr);
		falseAddr = falseStack.pop();
		trueAddr = trueStack.pop();
		skipAdr = skipStack.pop();
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
		// za map
		if (insideMap && fact.getDesignator().obj.getName().equals(mapVarName)) {
			Code.load(mapSrc); 
			Code.load(reg0); 
			// stek : srcAddr index
			if (mapSrc.getType().getElemType() == Tab.charType)
				Code.put(Code.baload);
			else
				Code.put(Code.aload);
			// arr[index] = expr(mapVar)
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
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2); // za nizove
		}
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(obj);
	}

	public void visit(DecDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		if (obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2); // za nizove
		}
		Code.load(obj);
		Code.loadConst(-1);
		Code.put(Code.add);
		Code.store(obj);
	}

	// FIND ANY
	// findAny(dst, arr, value)
	// result = 0
	// index = 0
	//
	// LOOP:
	// if(index >= arr.length) goto END
	//
	// if(arr[index] == value) {
	//	     result = 1
	//	     goto END
	// }
	//
	// index++
	// goto LOOP
	//
	// END:
	// dst = result
	public void visit(FindAnyDesignatorStatement stmt) {
		Obj arr = stmt.getDesignator1().obj;
		// expr stack : value
		Code.store(reg0); // REG0 = search value
		Code.loadConst(0);
		Code.store(reg1); // REG1 = 0 index
		Code.loadConst(0);
		Code.store(reg2); // REG2 = 0 return value
		int loop = Code.pc; // LOOP
		Code.load(reg1); // stek: index
		Code.load(arr); // stek: index arrayaddr
		Code.put(Code.arraylength); // stek: index len(arr)
		Code.putFalseJump(Code.lt, 0); // if(index >= arr.length) goto END
		int exit = Code.pc - 2;
		Code.load(arr); // stek : arrayaddr
		Code.load(reg1); // stek : arrayaddr index
		if (arr.getType().getElemType() == Tab.charType)
			Code.put(Code.baload);
		else
			Code.put(Code.aload);
		// stek arr[index]
		Code.load(reg0); // stek arr[index] value
		Code.putFalseJump(Code.eq, 0); // if(arr[index] != value) goto index++
		int loopContinue = Code.pc - 2; 
		Code.loadConst(1);
		Code.store(reg2); // result = 1
		Code.putJump(0);
		int end = Code.pc - 2;
		Code.fixup(loopContinue);
		Code.load(reg1);
		Code.loadConst(1);
		Code.put(Code.add); 
		Code.store(reg1); // index++
		Code.putJump(loop); // ponovo telo
		Code.fixup(exit); // EXIT
		Code.fixup(end); // END
		Code.load(reg2); // dst = result
		Code.store(stmt.getDesignator().obj);

	}

	// MAP

	// reg0 index
	// reg1 len
	
	// dst = map(src, expr)
	//
	// dst = new T[src.length]
	//
	// LOOP:
	// if(index >= len) goto END
	//
	// elem = src[index]
	//
	// result = <korisnicki izraz>
	//
	// dst[index] = result
	//
	// index++
	// goto LOOP
	//
	// END:
	public void visit(MapStart ms) {
		MapDesignatorStatement stmt = (MapDesignatorStatement) ms.getParent();
		mapDst = stmt.getDesignator().obj;
		mapSrc = stmt.getDesignator1().obj;

		mapVarName = stmt.getId();
		insideMap = true;
		// index = 0
		Code.loadConst(0);
		Code.store(reg0);	// REG0 = index
		// len = src.length
		Code.load(mapSrc);
		Code.put(Code.arraylength);
		Code.store(reg1);	// REG1 = len

		// alokacija
		Code.load(reg1);
		Code.put(Code.newarray);

		if (mapDst.getType().getElemType() == Tab.charType)
			Code.put(0);
		else
			Code.put(1);

		Code.store(mapDst);
		// loop:
		mapLoopStart = Code.pc;
		// if(index >= len) goto end
		Code.load(reg0);
		Code.load(reg1);
		Code.putFalseJump(Code.lt, 0);
		mapExitJump = Code.pc - 2;

	}

	public void visit(MapEnd me) {
		// stek: result expresiona
		Code.store(reg2); 	// REG2 = rezultat korisnickog izraza
		
		Code.load(mapDst);
		Code.load(reg0);
		Code.load(reg2);

		// stek: dst index result
		if (mapSrc.getType().getElemType() == Tab.charType)
			Code.put(Code.bastore);
		else
			Code.put(Code.astore);
		
		// index++
		Code.load(reg0);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(reg0);
		
		// goto loop
		Code.putJump(mapLoopStart);
		// end:
		Code.fixup(mapExitJump);
		insideMap = false;
		mapVarName = null;
		mapLoopStart = -1;
		mapExitJump = -1;
		mapDst = null;
		mapSrc = null;

	}

}
