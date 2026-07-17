package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {
	// registri opste namene da olaksam sebi zivot
	public Obj reg0;
	public Obj reg1;
	public Obj reg2;
	public Obj reg3;

	public SemanticAnalyzer(Struct bs) {
		boolStruct = bs;
	}

	boolean errorDetected = false;
	int nVars;
	Logger log = Logger.getLogger(getClass());
	Struct boolStruct;
	// TYPE
	Obj currentType = null;
	// CONST
	private Struct currentConstType;
	private int constValue;

	// CLASS
	private Obj currentClass = null;
	// METHOD
	private Obj currentMethod = null;

	private boolean mainFlag = false;
	// ACTPARS
	private List<Obj> currentParams = null;

	// for
	private boolean inFor = false;

	private boolean isInt(Obj obj) {
		return obj.getType().getKind() == Struct.Int;

		// ovo je staro sa enumom
		/*
		 * return obj.getType().getKind() == Struct.Int || (obj.getType().getKind() ==
		 * Struct.Enum && (obj.getKind() == Obj.Fld || obj.getKind() == Obj.Var ||
		 * obj.getKind() == Obj.Elem));
		 */
	}

	private void checkCall(Obj method, SyntaxNode lin) {
		if (method.getKind() != Obj.Meth) {
			report_error("Poziv nececga sto nije funkcija (metod)", lin);
			return;
		}
		if (currentParams == null) {
			currentParams = new ArrayList<>();
		}
		int formalCount = method.getLevel();
		int actualCount = currentParams.size();
		if (formalCount != actualCount) {
			report_error("Broj formalnih i stvarnih parametara se razlikuje", lin);
			currentParams = null;
			return;
		} else {
			Iterator<Obj> iter = method.getLocalSymbols().iterator();
			for (int i = 0; i < formalCount; i++) {
				Obj formal = iter.next();
				Struct actualType = currentParams.get(i).getType();
				Struct formalType = formal.getType();
				if (!actualType.assignableTo(formalType)) {
					report_error("Neispravan tip parametra, ocekuje se " + formalType.getKind(), lin);
				}
			}
		}
		currentParams = null;
	}

	private boolean addParam(String name, Struct type) {
		if (currentMethod == null) {
			report_error("Dodavanje parametra gde ne treba parametar", null);
			return false;
		}
		Obj previos = Tab.find(name);
		if (previos.getKind() == Obj.Var && previos.getLevel() == 1) {
			report_error("Parametar sa imenom " + name + " vec definisan ", null);
			return false;
		}
		Obj current = Tab.insert(Obj.Var, name, type);
		currentMethod.setLevel(currentMethod.getLevel() + 1);
		current.setFpPos(currentMethod.getLevel());
		return true;

	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		msg.insert(0, "SEMANTICKA GRESKA: ");
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	// PROGRAM
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
		reg0 = Tab.insert(Obj.Var, "_reg0", Tab.intType);
		reg1 = Tab.insert(Obj.Var, "_reg1", Tab.intType);
		reg2 = Tab.insert(Obj.Var, "_reg2", Tab.intType);
		reg3 = Tab.insert(Obj.Var, "_reg3", Tab.intType);

	}

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
		if (!mainFlag) {
			report_error("Program nema main metodu", program);
		}
	}

	// CONST
	public void visit(NumberConstValue numberConstValue) {
		currentConstType = Tab.intType;
		constValue = numberConstValue.getNumberValue();
	}

	public void visit(CharConstValue charConstValue) {
		currentConstType = Tab.charType;
		constValue = charConstValue.getCharValue();
	}

	public void visit(BoolConstValue boolConstValue) {
		currentConstType = boolStruct;
		constValue = boolConstValue.getBoolValue() ? 1 : 0;
	}

	public void visit(SingleConstDecl constDecl) {
		if (currentType == null) {
			report_error("Definicija konstante bez tipa/ nepostojeci tip ", constDecl);
			return;
		}
		Obj newConst = Tab.find(constDecl.getConstName());
		if (newConst != Tab.noObj) {
			report_error("Konstanta " + constDecl.getConstName() + " vec definisana ", constDecl);
			return;
		}
		if (!currentConstType.assignableTo(currentType.getType())) {
			report_error("Pogresan tip konstante ", constDecl);
		} else {
			newConst = Tab.insert(Obj.Con, constDecl.getConstName(), currentConstType);
			newConst.setAdr(constValue);
			report_info("DEFINICIJA konstanta imena " + newConst.getName() + " sa vrednoscu " + newConst.getAdr(),
					constDecl);
		}

	}

	// VAR
	public void visit(YesOptionalSquares squares) {
		if (currentType == null)
			return;
		squares.obj = new Obj(Obj.Var, "array", new Struct(Struct.Array, currentType.getType()));
	}

	public void visit(NoOptionalSquares noSquares) {
		if (currentType == null)
			return;
		noSquares.obj = new Obj(Obj.Var, "var", currentType.getType());
	}

	public void visit(NoErrorVarDecl varDecl) {
		currentType = null;
	}

	public void visit(SingleVarDecl varDecl) {
		if (currentType == null) {
			report_error("Deklaracija varijable bez tipa/nepostojeci tip ", varDecl);
			return;
		}
		Struct varType = varDecl.getOptionalSquares().obj.getType();
		Obj prevVar = Tab.find(varDecl.getVarName());
		Obj newVar = Tab.insert(Obj.Var, varDecl.getVarName(), varType);
		if (newVar == prevVar) {
			report_error("Simbol sa imenom " + varDecl.getVarName() + " vec deklarisan", varDecl);
		} else {
			report_info(
					"DEKLARACIJA promenljiva imena " + varDecl.getVarName() + " sa tipom " + newVar.getType().getKind(),
					varDecl);
		}
		// currentType = null;
	}

	// CLASS
	public void visit(ClassName className) {
		if (Tab.find(className.getClassName()) != Tab.noObj) {
			report_error("Simbol sa imenom " + className.getClassName() + " vec definisan", className);
			return;
		}
		currentClass = Tab.insert(Obj.Type, className.getClassName(), new Struct(Struct.Class));
		Tab.openScope();
		report_info("Dodata klasa imena " + className.getClassName(), className);
	}

	public void visit(ExtendsClassExtends classExtends) {
		String parentClassName = classExtends.getType().getTypeName();
		Obj parentClass = Tab.find(parentClassName);
		if (parentClass == Tab.noObj) {
			report_error("Ne postoji roditeljska klasa " + parentClassName, classExtends);
			return;
		}
		if (parentClass.getKind() != Obj.Type) {
			report_error("Roditeljska klasa " + parentClassName + " nije tipa klase", classExtends);
			return;
		}
		Struct parentClassType = parentClass.getType();
		if (parentClassType == Tab.intType || parentClassType == Tab.charType || parentClassType == boolStruct) {
			report_error("Roditeljska klasa " + parentClassName + " je primitivnog tipa", classExtends);
			return;
		}
		/*
		 * if (parentClassType.getKind() == Struct.Enum) {
		 * report_error("Roditeljska klasa " + parentClassName + " je tipa enum",
		 * classExtends); return; }
		 */
		currentClass.getType().setElementType(parentClassType);
	}

	public void visit(ClassDecl classDecl) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		classDecl.obj = currentClass;
		currentClass = null;
	}

	// ABSTRACT CLASS
	public void visit(AbstractClassDecl classDecl) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		classDecl.obj = currentClass;
		currentClass = null;
	}

	// METHOD
	public void visit(MethodName methodName) {
		if (Tab.find(methodName.getMethodName()) != Tab.noObj) {
			report_error("Simbol sa imenom " + methodName.getMethodName() + " vec definisan ", methodName);
			return;
		}
		if (currentType != null) {
			currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), currentType.getType());
		} else {
			currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), Tab.noType);
		}
		currentMethod.setLevel(0);
		if (methodName.getMethodName().equals("main")) {
			if (currentMethod.getType() != Tab.noType) {
				report_error("Metod main nije void tipa ", methodName);
			}
			mainFlag = true;
		}
		currentType = null;
		Tab.openScope();
		if (currentClass != null) {
			addParam("this", currentClass.getType());
		}
		methodName.obj = currentMethod;
		report_info("DEFINICIJA metoda " + methodName.getMethodName(), methodName);
	}

	public void visit(SingleMethodDecl methodDecl) {
		if (currentMethod == null)
			return;
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		methodDecl.obj = currentMethod;
		currentMethod = null;
	}

	// ABSTRACT METHOD

	// FORM PARS
	public void visit(SingleFormPar param) {
		if (addParam(param.getParamName(), param.getOptionalSquares().obj.getType())) {
			report_info("\tDodat novi formalni parametar imena " + param.getParamName(), param);
		}

		currentType = null;
	}

	// TYPE
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		// System.out.println("SMENA TIP " + typeNode.getKind());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
			currentType = null;
			type.obj = Tab.noObj;
			return;
		}
		if (Obj.Type == typeNode.getKind()) {
			type.obj = new Obj(Obj.Type, "type", typeNode.getType());
			currentType = typeNode;
		} else {
			report_error("Ime " + type.getTypeName() + " ne predstavlja tip", type);
			type.obj = Tab.noObj;
			currentType = null;
		}

	}

	// STATEMENT
	// Designator statement
	public void visit(EqualDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		if (obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld || obj.getKind() == Obj.Var) {
			if (!design.getExpr().obj.getType().assignableTo(obj.getType())) {
				report_error("Nedodeljiv tip u dodeli vrednosti", design);
			}

			// TODO klase
		} else {
			report_error("Destinacija dodele nije promenljiva, element niza ili polje unutar objekta", design);
		}
	}

	public void visit(IncDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		if (obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld || obj.getKind() == Obj.Var) {
			if (!obj.getType().equals(Tab.intType)) {
				report_error("Samo tip int se moze inkrementirati", design);
			}
		} else {
			report_error("Destinacija inkrementa nije promenljiva, element niza ili polje unutar objekta", design);
		}
	}

	public void visit(DecDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		if (obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld || obj.getKind() == Obj.Var) {
			if (!isInt(obj)) {
				report_error("Samo tip int se moze dekrementirati", design);
			}
		} else {
			report_error("Destinacija dekrement nije promenljiva, element niza ili polje unutar objekta", design);
		}
	}

	public void visit(CallDesignatorStatement design) {
		checkCall(design.getDesignator().obj, design);
	}

	// IF statement
	public void visit(IfStatementIf stmnt) {
		if (!stmnt.getCondition().obj.getType().equals(boolStruct)) {
			report_error("Condition u If nije tipa bool", stmnt);
		}
	}

	public void visit(ElseStatementIf stmnt) {
		if (!stmnt.getCondition().obj.getType().equals(boolStruct)) {
			report_error("Condition u If nije tipa bool", stmnt);
		}
	}

	// BREAK statement
	public void visit(StatementBreak stmnt) {
		if (!inFor) { // && !inSwitch
			report_error("break se moze naci samo u for petlji", stmnt); // ili switch-u
		}
	}

	// CONTINUE statement
	public void visit(StatementContinue stmnt) {
		if (!inFor) {
			report_error("continue se moze naci samo u for petlji", stmnt);
		}
	}

	// RETURN statement
	public void visit(NoExprStatementReturn returnExpr) {
		if (currentMethod == null) {
			report_error("return van tela metode ", returnExpr);
			return;
		}
		if (currentMethod.getType() != Tab.noType) {
			report_error("return void metode vraca vrednost ", returnExpr);
			return;
		}
		

	}

	public void visit(ExprStatementReturn returnExpr) {
		// System.out.println("returnExpr: " + returnExpr);
		// System.out.println("returnExpr.getExpr: " + returnExpr.getExpr());
		if (currentMethod == null) {
			report_error("return van tela metode ", returnExpr);
			return;
		}
		if (currentMethod.getType() != returnExpr.getExpr().obj.getType()) {
			report_error("return vraca pogresan tip ", returnExpr);
			return;
		}
		if(!returnExpr.getExpr().obj.getType().assignableTo(currentMethod.getType())) {
			report_error("tip izraza nekompatibilan sa deklaracijom metoda", returnExpr);
		}
	}

	// READ statement
	public void visit(StatementRead stmnt) {
		Obj obj = stmnt.getDesignator().obj;
		Struct type = obj.getType();
		if (obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld || obj.getKind() == Obj.Var) {
			if (!type.equals(Tab.charType) && !type.equals(Tab.intType) && !type.equals(boolStruct)) {
				report_error("Samo tipovi int bool i char mogu da se citaju", stmnt);
			}
		} else {
			report_error("Destinacija read nije promenljiva, element niza ili polje unutar objekta", stmnt);
		}
	}

	// PRINT statement
	public void visit(ExprStatementPrint stmnt) {
		Struct type = stmnt.getExpr().obj.getType();
		if (!type.equals(Tab.charType) && !type.equals(Tab.intType) && !type.equals(boolStruct)) {
			report_error("Samo tipovi int bool i char mogu da se ispisuju", stmnt);
		}
	}

	public void visit(ExprCommaNumberStatementPrint stmnt) {
		Struct type = stmnt.getExpr().obj.getType();
		if (!type.equals(Tab.charType) && !type.equals(Tab.intType) && !type.equals(boolStruct)) {
			report_error("Samo tipovi int bool i char mogu da se ispisuju", stmnt);
		}
	}

	// FOR statement
	public void visit(StatementFor stmnt) {
		inFor = false;
	}

	public void visit(YesForCond for_cond) {
		if (!for_cond.getCondition().obj.getType().equals(boolStruct)) {
			report_error("Uslov u for petlji nije tipa bool", for_cond);
		}
	}

	public void visit(YesForInit for_init) {
		inFor = true;
	}

	public void visit(NoForInit for_init) {
		inFor = true;
	}

	// ACT PARS
	public void visit(SingleActParsList param) {
		if (currentParams == null)
			currentParams = new ArrayList<>();
		currentParams.add(param.getExpr().obj);

	}

	public void visit(ListActParsList params) {
		currentParams.add(params.getExpr().obj);
	}

	// CONDITIONS: Condition CondTerm CondFact Relop
	public void visit(NoRelopCondFact fact) {
		fact.obj = fact.getExprArithmetic().obj;
	}

	public void visit(RelopCondFact fact) {
		Struct op1 = fact.getExprArithmetic().obj.getType();
		Struct op2 = fact.getExprArithmetic1().obj.getType();
		if (!op1.compatibleWith(op2)) {
			report_error("Nekompatibilni tipovi u logickom izrazu", fact);
			fact.obj = Tab.noObj;
			return;
		}
		String relopType = fact.getRelop().obj.getName();
		if (op1.isRefType() && !(relopType.equals("eq") || relopType.equals("neq"))) {
			report_error("Klasni tipovi i nizovi se mogu porediti samo sa == ili !=", fact);
			fact.obj = Tab.noObj;
			return;
		}
		if (op1.equals(boolStruct) && op2.equals(boolStruct) && !(relopType.equals("eq") || relopType.equals("neq"))) {
			report_error("Bool vrednosti se mogu porediti samo sa == ili !=", fact);
			fact.obj = Tab.noObj;
			return;
		}
		fact.obj = new Obj(Obj.Var, "fact", boolStruct);
	}

	public void visit(ListCondFactList cond) {
		if (!cond.getCondFactList().obj.getType().equals(boolStruct)) {
			report_error("Desni operand logickog AND mora biti tipa bool", cond);
			cond.obj = Tab.noObj;
			return;
		}
		if (!cond.getCondFact().obj.getType().equals(boolStruct)) {
			report_error("Operand logickog AND mora biti tipa bool", cond);
			cond.obj = Tab.noObj;
			return;
		}
		cond.obj = new Obj(Obj.Var, "cond", boolStruct);
		;
	}

	public void visit(SingleCondFactList cond) {
		cond.obj = cond.getCondFact().obj;
	}

	public void visit(ListCondTermList cond) {
		if (!cond.getCondTermList().obj.getType().equals(boolStruct)) {
			report_error("Desni operand logickog OR mora biti tipa bool", cond);
			cond.obj = Tab.noObj;
			return;
		}
		if (!cond.getCondTerm().obj.getType().equals(boolStruct)) {
			report_error("Levi operand logickog OR mora biti tipa bool", cond);
			cond.obj = Tab.noObj;
			return;
		}
		cond.obj = new Obj(Obj.Var, "cond", boolStruct);
	}

	public void visit(CondTerm cond) {
		cond.obj = cond.getCondFactList().obj;
	}

	public void visit(SingleCondTermList cond) {
		cond.obj = cond.getCondTerm().obj;
	}

	public void visit(Condition cond) {
		cond.obj = cond.getCondTermList().obj;
	}

	public void visit(EQRelop eq) {
		eq.obj = new Obj(Obj.NO_VALUE, "eq", Tab.noType);
	}

	public void visit(NEQRelop neq) {
		neq.obj = new Obj(Obj.NO_VALUE, "neq", Tab.noType);
	}

	public void visit(GRelop g) {
		g.obj = new Obj(Obj.NO_VALUE, "g", Tab.noType);
	}

	public void visit(GERelop ge) {
		ge.obj = new Obj(Obj.NO_VALUE, "ge", Tab.noType);
	}

	public void visit(LRelop l) {
		l.obj = new Obj(Obj.NO_VALUE, "l", Tab.noType);
	}

	public void visit(LERelop le) {
		le.obj = new Obj(Obj.NO_VALUE, "le", Tab.noType);
	}

	// Expr
	public void visit(TermExprArithmetic expr) {
		expr.obj = expr.getTerm().obj;
	}

	public void visit(MinusTermExprArithmetic expr) {
		if (isInt(expr.getTerm().obj))
			expr.obj = new Obj(Obj.Var, "expr", Tab.intType);
		else {
			report_error("Term mora biti tipa int (za negaciju)", expr);
		}
	}

	public void visit(ListTermExprArithmetic expr) {
		if (!isInt(expr.getExprArithmetic().obj)) {
			report_error("Desni operand operacija + i - mora biti tipa int", expr);
			expr.obj = Tab.noObj;
			return;
		}
		if (!isInt(expr.getTerm().obj)) {
			report_error("Levi operand operacija + i - mora biti tipa int", expr);
			expr.obj = Tab.noObj;
			return;
		}
		expr.obj = new Obj(Obj.Var, "expr", Tab.intType);
	}

	public void visit(ExprTernary expr) {
		if (!expr.getExpr().obj.getType().compatibleWith(expr.getExpr1().obj.getType())) {
			report_error("Vrednosti u ternarnome operatoru nisu istog tipa", expr);
			expr.obj = Tab.noObj;
		}
		expr.obj = expr.getExpr().obj;
	}

	public void visit(TernExpr expr) {
		expr.obj = expr.getExprTernary().obj;
	}

	public void visit(ArithExpr expr) {
		expr.obj = expr.getExprArithmetic().obj;

	}

	// TERM
	public void visit(SingleFactorTerm term) {
		term.obj = term.getFactor().obj;
	}

	public void visit(ListFactorTerm term) {
		if (!isInt(term.getFactor().obj)) {
			report_error("Desni operand operacija *,/ ili % mora biti tipa int", term);
			term.obj = Tab.noObj;
			return;
		}
		if (!isInt(term.getTerm().obj)) {
			report_error("Levi operand operacija *,/ ili % mora biti tipa int", term);
			term.obj = Tab.noObj;
			return;
		}
		term.obj = new Obj(Obj.Var, "term", Tab.intType);
	}

	// FACTOR
	public void visit(DesignatorFactor factor) {
		factor.obj = factor.getDesignator().obj;
	}

	public void visit(CallFactor factor) {
		Obj calledFunction = factor.getDesignator().obj;
		checkCall(calledFunction, factor);
		if (calledFunction.getType() == Tab.noType) {
			report_error("Void metoda se ne može koristiti u izrazu", factor);
		}
		factor.obj = new Obj(Obj.Var, "call", calledFunction.getType());
	}

	public void visit(NumberFactor factor) {
		factor.obj = new Obj(Obj.Var, "factor", Tab.intType);
	}

	public void visit(CharFactor factor) {
		factor.obj = new Obj(Obj.Var, "factor", Tab.charType);
	}

	public void visit(BoolFactor factor) {
		factor.obj = new Obj(Obj.Var, "factor", boolStruct);
	}

	public void visit(NewFactorArray factor) {
		if (currentType == null) {
			factor.getType().obj = Tab.noObj;
			return;
		}
		if (!isInt(factor.getExpr().obj)) {
			report_error("Niz mora biti inicijalizovan celobrojnim izrazom", factor);
			factor.obj = Tab.noObj;
			return;
		}
		factor.obj = new Obj(Obj.Var, "array", new Struct(Struct.Array, currentType.getType()));
		report_info("Inicijalizovan niz " + factor.getType(), factor);
		currentType = null;
	}

	public void visit(NewFactor factor) {
		Obj type = factor.getType().obj;
		if (type == null) {
			factor.obj = Tab.noObj;
			return;
		}
		if (type.getKind() != Struct.Class) {
			report_error("Pokusaj inicijalizacije sa NEW za neklasni tip", factor);
		}
	}

	public void visit(ParentsFactor factor) {
		factor.obj = factor.getExpr().obj;
	}

	// DESIGNATOR (desingerica)
	public void visit(IdentDesignator design) {
		if (currentClass != null) {
			// TODO u deklaraciji klase
		}
		design.obj = Tab.find(design.getDesignatorName());
		if (design.obj == Tab.noObj) {
			report_error("Simbol sa imenom " + design.getDesignatorName() + " nije definisan", design);
		} else {
			report_info("UPOTREBA simbola " + design.getDesignatorName(), design);
		}
	}

	public void visit(DotIdentDesignator design) {
		Obj obj = design.getDesignator().obj;
		if (obj.getType().getKind() == Struct.Class) {
			System.out.println("OBRADA KLASE");
			// TODO
		}
		report_error("Pristup polju identifikatora koji nije klasa", design); // ili enum
	}

	public void visit(DotLengthDesignator design) {
		Obj obj = design.getDesignator().obj;
		if (obj.getType().getKind() != Struct.Array) {
			report_error("Pokusaj poziva length nad necemu sto nije niz", design);
			design.obj = Tab.noObj;
			return;
		}
		design.obj = new Obj(Obj.Con, "length", Tab.intType);
		report_info("UPOTREBA length za niz", design);
	}

	public void visit(DesignatorArray arr) {
		arr.obj = arr.getDesignator().obj;
	}

	public void visit(IndexDesignator design) {
		Obj obj = design.getDesignatorArray().obj;
		if (obj.getType().getKind() != Struct.Array) {
			report_error("Pokusaj indeksiranja nad necemu sto nije niz", design);
			design.obj = Tab.noObj;
			return;
		}
		if (!isInt(design.getExpr().obj)) {
			report_error("Pokusaj indeksiranja necim sto nije int", design);
			design.obj = Tab.noObj;
			return;
		}
		design.obj = new Obj(Obj.Elem, "element", obj.getType().getElemType());
		report_info("UPOTREBA element niza ", design);
	}

	// FIND ANY
	public void visit(FindAnyDesignatorStatement stmt) {
		if (stmt.getDesignator().obj.getType() != boolStruct) {
			report_error("izraz sa findAny moze da se dodeli samo bool tipu", stmt);
			return;
		}
		if (stmt.getDesignator1().obj.getType().getKind() != Struct.Array) {
			report_error("findAny moze da se pozove samo nad tipom niza", stmt);
			return;
		}
	}

	public boolean passed() {
		return !errorDetected;
	}

}
