package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {
	public SemanticAnalyzer(Struct bs) {
		boolStruct = bs;
	}

	// registri opste namene da olaksam sebi zivot
	private Obj reg0;
	private Obj reg1;
	private Obj reg2;

	public Obj getReg0() {
		return reg0;
	}

	public Obj getReg1() {
		return reg1;
	}

	public Obj getReg2() {
		return reg2;
	}

	private boolean errorDetected = false;

	public boolean passed() {
		return !errorDetected;
	}

	private int nVars;

	public int getnVars() {
		return nVars;
	}

	private Logger log = Logger.getLogger(getClass());

	private Struct boolStruct;

	// TYPE
	private Struct currentType = null;
	// CONST
	private Struct currentConstType;
	private int constValue;
	// CLASS
	private Obj currentClass = null;
	// METHOD
	private Obj currentMethod = null;
	// MAIN
	private boolean mainFlag = false;
	// FOR
	private boolean inFor = false;
	// PARAMS
	private Stack<List<Obj>> actParsStack = new Stack<>();
	private HashMap<Obj, List<Struct>> methodFormPars = new HashMap<>();
	// STRINGS
	private static final String nonMutableDst = "Destinacija nije promenljiva, element niza ili polje unutar objekta";
	private final static String nonPrintable = "Samo tipovi int bool i char mogu da se citaju/ispisuju";

	// HELPERS
	private void inForCheck(String op, SyntaxNode line) {
		if (!inFor) {
			report_error(op + " se moze naci samo u for petlji", line); // ili switch-u
		}
	}

	private boolean checkArithmeticsOperands(Obj left, Obj right, String op, SyntaxNode line) {
		if (!isInt(right)) {
			report_error("Desni operand operacija " + op + " mora biti tipa int", line);
			return false;
		}
		if (!isInt(left)) {
			report_error("Levi operand operacija " + op + " mora biti tipa int", line);
			return false;
		}
		return true;
	}

	private void checkCall(Obj method, SyntaxNode lin) {
		if (method.getKind() != Obj.Meth) {
			report_error("Poziv nececga sto nije funkcija (metod)", lin);
			return;
		}
		List<Obj> currentParams = actParsStack.empty() ? new ArrayList<Obj>() : actParsStack.pop();

		int formalCount = method.getLevel();
		int actualCount = currentParams.size();
		if (formalCount != actualCount) {
			report_error("Broj formalnih i stvarnih parametara se razlikuje", lin);
			currentParams = null;
			return;
		}
		List<Struct> formPars = methodFormPars.get(method);
		if (formPars == null) {
			formPars = new ArrayList<>();
			switch (method.getName()) {
			case "chr":
				formPars.add(Tab.intType);
				break;
			case "ord":
				formPars.add(Tab.charType);
				break;
			case "len":
				formPars.add(new Struct(Struct.Array, Tab.noType));
				break;
			}

		}
		for (int i = 0; i < formalCount; i++) {
			Struct actualType = currentParams.get(i).getType();
			Struct formalType = formPars.get(i);
			if (!actualType.assignableTo(formalType)) {
				report_error("Neispravan tip parametra u pozivu metode", lin);
				currentParams = null;
				return;
			}
		}
		currentParams = null;
	}

	private void checkBoolCondition(Obj obj, String op, SyntaxNode line) {
		if (!obj.getType().equals(boolStruct)) {
			report_error("Condition u " + op + " nije tipa bool", line);
		}
	}

	private boolean checkConditionOperands(Obj left, Obj right, String op, SyntaxNode line) {
		if (!right.getType().equals(boolStruct)) {
			report_error("Desni operand operacija " + op + " mora biti tipa bool", line);
			return true;
		}
		if (!left.getType().equals(boolStruct)) {
			report_error("Levi operand operacija " + op + " mora biti tipa bool", line);
			return true;
		}
		return false;
	}
	
	private boolean isDefined(String name, SyntaxNode line) {
		if (Tab.find(name) != Tab.noObj) {
			report_error("Simbol sa imenom " + name + " vec definisan na istom nivou", line);
			return true;
		}
		return false;
	}

	private boolean checkCurrentType(SyntaxNode line) {
		if (currentType == null) {
			report_error("Deklaracija bez tipa/ nepostojeci tip ", line);
			return true;
		}
		return false;
	}

	private static boolean isMutable(Obj obj) {
		return obj.getKind() == Obj.Elem || obj.getKind() == Obj.Fld || obj.getKind() == Obj.Var;
	}

	private boolean isPrintable(Struct type) {
		return type.equals(Tab.charType) || type.equals(Tab.intType) || type.equals(boolStruct);
	}

	private boolean isInt(Obj obj) {
		return obj.getType().getKind() == Struct.Int;
		// ovo je staro sa enumom
		// return obj.getType().getKind() == Struct.Int || (obj.getType().getKind() ==
		// Struct.Enum && (obj.getKind() == Obj.Fld || obj.getKind() == Obj.Var ||
		// obj.getKind() == Obj.Elem));
	}

	private Obj createRelop(String name) {
		return new Obj(Obj.NO_VALUE, name, Tab.noType);
	}

	private void addParam(String name, Struct type, SyntaxNode line) {
		if (currentMethod == null) {
			report_error("Dodavanje parametra gde ne treba parametar", null);
			return;
		}
		Obj previos = Tab.find(name);
		if (previos.getKind() == Obj.Var && previos.getLevel() == 1) {
			report_error("Parametar sa imenom " + name + " vec definisan ", null);
			return;
		}
		Obj current = Tab.insert(Obj.Var, name, type);
		currentMethod.setLevel(currentMethod.getLevel() + 1);
		current.setFpPos(currentMethod.getLevel());
		methodFormPars.computeIfAbsent(currentMethod, k -> new ArrayList<>()).add(type);
		report_info("\tDodat novi formalni parametar imena " + name, line);
	}

	private void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		msg.insert(0, "SEMANTICKA GRESKA: ");
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	private void report_info(String message, SyntaxNode info) {
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
		if (checkCurrentType(constDecl)) {
			return;
		}
		if (isDefined(constDecl.getConstName(), constDecl)) {
			return;
		}
		if (!currentConstType.assignableTo(currentType)) {
			report_error("Pogresan tip konstante ", constDecl);
			return;
		}
		Obj newConst = Tab.insert(Obj.Con, constDecl.getConstName(), currentConstType);
		newConst.setAdr(constValue);
		report_info("DEFINICIJA konstante " + newConst.getName(), constDecl);

	}

	// VAR
	public void visit(SingleVarDecl varDecl) {
		if (checkCurrentType(varDecl)) {
			return;
		}
		if (isDefined(varDecl.getVarName(), varDecl)) {
			return;
		}
		Struct varType = varDecl.getOptionalSquares().obj.getType();
		Obj newVar = Tab.insert(Obj.Var, varDecl.getVarName(), varType);
		if(currentMethod == null) report_info("DEKLARACIJA GLOBALNA promenljiva " + newVar.getName(), varDecl);
		else report_info("DEKLARACIJA LOKALNA promenljiva " + newVar.getName(), varDecl);
	}

	public void visit(YesOptionalSquares squares) {
		if (checkCurrentType(squares)) {
			squares.obj = Tab.noObj;
			return;
		}
		squares.obj = new Obj(Obj.Var, "array", new Struct(Struct.Array, currentType));
	}

	public void visit(NoOptionalSquares noSquares) {
		if (checkCurrentType(noSquares)) {
			noSquares.obj = Tab.noObj;
			return;
		}
		noSquares.obj = new Obj(Obj.Var, "var", currentType);
	}

	public void visit(NoErrorVarDecl varDecl) {
		currentType = null;
	}

	// CLASS
	public void visit(ClassName className) {
		if (isDefined(className.getClassName(), className)) {
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
		if (isDefined(methodName.getMethodName(), methodName)) {
			return;
		}
		if (currentType != null) {
			currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), currentType);
		} else {
			currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), Tab.noType);
		}
		currentMethod.setLevel(0);
		if (methodName.getMethodName().equals("main")) {
			if (currentMethod.getType() != Tab.noType) {
				report_error("Metod main nije void tipa ", methodName);
				return;
			}
			mainFlag = true;
		}
		currentType = null;
		Tab.openScope();
		if (currentClass != null) {
			addParam("this", currentClass.getType(), methodName);
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
		addParam(param.getParamName(), param.getOptionalSquares().obj.getType(), param);
		currentType = null;
	}

	// TYPE
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
			currentType = null;
			type.obj = Tab.noObj;
			return;
		}
		if (Obj.Type == typeNode.getKind()) {
			type.obj = new Obj(Obj.Type, "type", typeNode.getType());
			currentType = typeNode.getType();
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
		if (!isMutable(obj)) {
			report_error(nonMutableDst, design);
		}
		if (!design.getExpr().obj.getType().assignableTo(obj.getType())) {
			report_error("Nedodeljiv tip u dodeli vrednosti", design);
		}
	}

	public void visit(IncDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		if (!isMutable(obj)) {
			report_error(nonMutableDst, design);
		}
		if (!isInt(obj)) {
			report_error("Samo tip int se moze inkrementirati", design);
		}
	}

	public void visit(DecDesignatorStatement design) {
		Obj obj = design.getDesignator().obj;
		if (!isMutable(obj)) {
			report_error(nonMutableDst, design);
		}
		if (!isInt(obj)) {
			report_error("Samo tip int se moze dekrementirati", design);
		}
	}

	public void visit(CallDesignatorStatement design) {
		checkCall(design.getDesignator().obj, design);
		report_info("POZIV FUNKCIJE " + design.getDesignator().obj.getName(), design);
	}

	// IF statement
	public void visit(IfStatementIf stmt) {
		checkBoolCondition(stmt.getCondition().obj, "IF", stmt);

	}

	public void visit(ElseStatementIf stmt) {
		checkBoolCondition(stmt.getCondition().obj, "IF", stmt);
	}

	// BREAK statement
	public void visit(StatementBreak stmt) {
		inForCheck("break", stmt);
	}

	// CONTINUE statement
	public void visit(StatementContinue stmt) {
		inForCheck("continue", stmt);
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
		if (currentMethod == null) {
			report_error("return van tela metode ", returnExpr);
			return;
		}
		if (currentMethod.getType() != returnExpr.getExpr().obj.getType()) {
			report_error("return vraca pogresan tip ", returnExpr);
			return;
		}
		if (!returnExpr.getExpr().obj.getType().assignableTo(currentMethod.getType())) {
			report_error("tip izraza nekompatibilan sa deklaracijom metoda", returnExpr);
			return;
		}
	}

	// READ statement
	public void visit(StatementRead stmnt) {
		Obj obj = stmnt.getDesignator().obj;
		Struct type = obj.getType();
		if (!isMutable(obj)) {
			report_error(nonMutableDst, stmnt);
			return;
		}
		if (!isPrintable(type)) {
			report_error(nonPrintable, stmnt);
			return;
		}

	}

	// PRINT statement
	public void visit(ExprStatementPrint stmnt) {
		Struct type = stmnt.getExpr().obj.getType();
		if (!isPrintable(type)) {
			report_error(nonPrintable, stmnt);
			return;
		}
	}

	public void visit(ExprCommaNumberStatementPrint stmnt) {
		Struct type = stmnt.getExpr().obj.getType();
		if (!isPrintable(type)) {
			report_error(nonPrintable, stmnt);
			return;
		}
	}

	// FOR statement
	public void visit(StatementFor stmnt) {
		inFor = false;
	}

	public void visit(YesForCond cond) {
		checkBoolCondition(cond.getCondition().obj, "FOR", cond);
	}

	public void visit(YesForInit for_init) {
		inFor = true;
	}

	public void visit(NoForInit for_init) {
		inFor = true;
	}

	// ACT PARS
	public void visit(SingleActParsList param) {
		actParsStack.peek().add(param.getExpr().obj);
	}

	public void visit(ListActParsList param) {
		actParsStack.peek().add(param.getExpr().obj);
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
		if (checkConditionOperands(cond.getCondFactList().obj, cond.getCondFact().obj, "AND", cond)) {
			cond.obj = Tab.noObj;
			return;
		}
		cond.obj = new Obj(Obj.Var, "cond", boolStruct);
	}

	public void visit(SingleCondFactList cond) {
		cond.obj = cond.getCondFact().obj;
	}

	public void visit(ListCondTermList cond) {
		if (checkConditionOperands(cond.getCondTermList().obj, cond.getCondTerm().obj, "OR", cond)) {
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
		eq.obj = createRelop("eq");
	}

	public void visit(NEQRelop neq) {
		neq.obj = createRelop("neq");
	}

	public void visit(GRelop g) {
		g.obj = createRelop("g");
	}

	public void visit(GERelop ge) {
		ge.obj = createRelop("ge");
	}

	public void visit(LRelop l) {
		l.obj = createRelop("l");
	}

	public void visit(LERelop le) {
		le.obj = createRelop("le");
	}

	// Expr arith
	public void visit(TermExprArithmetic expr) {
		expr.obj = expr.getTerm().obj;
	}

	public void visit(MinusTermExprArithmetic expr) {
		if (!isInt(expr.getTerm().obj)) {
			report_error("Term mora biti tipa int (za negaciju)", expr);
			expr.obj = Tab.noObj;
		}
		expr.obj = new Obj(Obj.Var, "expr", Tab.intType);

	}

	public void visit(ListTermExprArithmetic expr) {
		if (!checkArithmeticsOperands(expr.getExprArithmetic().obj, expr.getTerm().obj, " + i -", expr)) {
			expr.obj = Tab.noObj;
			return;
		}
		expr.obj = new Obj(Obj.Var, "expr", Tab.intType);
	}

	public void visit(ArithExpr expr) {
		expr.obj = expr.getExprArithmetic().obj;
	}

	// Expr tern
	public void visit(ExprTernary expr) {
		if (!expr.getExpr().obj.getType().compatibleWith(expr.getExpr1().obj.getType())) {
			report_error("Vrednosti u ternarnome operatoru nisu istog tipa", expr);
			expr.obj = Tab.noObj;
			return;
		}
		expr.obj = expr.getExpr().obj;
	}

	public void visit(TernExpr expr) {
		expr.obj = expr.getExprTernary().obj;
	}

	// TERM
	public void visit(SingleFactorTerm term) {
		term.obj = term.getFactor().obj;
	}

	public void visit(ListFactorTerm term) {
		if (!checkArithmeticsOperands(term.getFactor().obj, term.getTerm().obj, " * , / i % ", term)) {
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
			factor.obj = Tab.noObj;
			return;
		}
		factor.obj = new Obj(Obj.Var, "call", calledFunction.getType());
		report_info("POZIV FUNKCIJE " + factor.getDesignator().obj.getName(), factor);
	}

	public void visit(CallStart dummy) {
		actParsStack.push(new ArrayList<Obj>());
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
		if (checkCurrentType(factor)) {
			factor.obj = Tab.noObj;
			return;
		}
		if (!isInt(factor.getExpr().obj)) {
			report_error("Niz mora biti inicijalizovan celobrojnim izrazom", factor);
			factor.obj = Tab.noObj;
			return;
		}
		factor.obj = new Obj(Obj.Var, "array", new Struct(Struct.Array, currentType));
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
		if (!(obj.getType().getKind() == Struct.Class)) {
			report_error("Pristup polju identifikatora koji nije klasa", design); // ili enum
		}
		// TODO
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
		report_info("PRISTUP elementu niza " + obj.getName(), design);
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

	// MAP
	public void visit(MapDesignatorStatement stmt) {
		Obj ident = Tab.find(stmt.getId());
		if (ident == Tab.noObj) {
			report_error("identifikator u map nije pronadjen u tabeli simobola", stmt);
			return;
		}
		if (ident.getKind() != Obj.Var) {
			report_error("ident u map mora biti promenljiva", stmt);
			return;
		}

		if (!stmt.getDesignator().obj.getType().getElemType()
				.equals(stmt.getDesignator1().obj.getType().getElemType())) {
			report_error("nizovi u map moraju biti istog tipa", stmt);
			return;
		}
		if (!stmt.getDesignator().obj.getType().getElemType().equals(ident.getType())) {
			report_error("niz i ident nisu istog tipa u map", stmt);
			return;
		}
		if (stmt.getDesignator().obj.getType().getKind() != Struct.Array) {
			report_error("izraz sa map se mora dodeliti nizu", stmt);
			return;
		}
		if (stmt.getDesignator1().obj.getType().getKind() != Struct.Array) {
			report_error(".map se moze pozvati samo nad nizom", stmt);
			return;
		}

	}

}
