package rs.ac.bg.etf.pp1;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.mysymboltable.*;
import rs.ac.bg.etf.pp1.mysymboltable.Constant;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder("SINTAKSNA GRESKA!: [");
		msg.append(message);
		msg.append("]");
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_error(String message, int line) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder("SINTAKSNA GRESKA!: [");
		msg.append(message);
		msg.append("]");
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

	public void report_info(String message, int line) {
		StringBuilder msg = new StringBuilder(message);
		msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean passed() {
		if (!foundMain)
			report_error("Nije deklarisana funkcija main", null);
		return !errorDetected && foundMain;
	}

	private Obj programObj;

	// program
	public void visit(ProgramStart programStart) {
		programObj = MyTable.insert(Obj.Prog, programStart.getProgramName(), MyTable.noType);
		MyTable.openScope();
	}

	public void visit(Program program) {
		MyTable.chainLocalSymbols(programObj);
		MyTable.closeScope();
	}

	// deklaracije simbolickih konstanti
	private LinkedList<Constant> constants = new LinkedList<>();

	public void visit(ConstDecl constDecl) {
		for (Constant constant : constants) {
			if (MyTable.existsInCurrentScope(constant.getName())) {
				report_error("Konstanta " + constant.getName() + " je vec deklarisana u trenutnom opsegu", constDecl);
			} else if (!constant.getObj().getType().equals(constDecl.getType().obj.getType())) {
				report_error("Konstanti " + constant.getName() + " koja je tipa " + constDecl.getType().obj.getName()
						+ " ne moze se dodeliti vrednost " + constant.getValue() + " koja je tipa "
						+ constant.getObj().getName(), constDecl);
			} else {
				report_info("Deklarisana konstanta " + constant.getName() + " tipa " + constDecl.getType().obj.getName()
						+ " vrednost=" + constant.getValue(), constDecl);
				MyTable.insertConstant(Obj.Con, constant.getName(), constant.getObj().getType(), constant.getValue());
			}
		}
		constants.clear();
	}

	/*public void visit(AssignConstantList assignConstantList) {
		constants.add(new Constant(assignConstantList.getConstValue().constant.getObj(),
				assignConstantList.getConstValue().constant.getValue(), assignConstantList.getConstName(),
				assignConstantList.getLine()));
	}

	public void visit(AssignConstantListEnd assignConstantListEnd) {
		constants.add(new Constant(assignConstantListEnd.getConstValue().constant.getObj(),
				assignConstantListEnd.getConstValue().constant.getValue(), assignConstantListEnd.getConstName(),
				assignConstantListEnd.getLine()));
	}
	*/
	public void visit(AssignConst assignConst) {
		constants.add(new Constant(assignConst.getConstValue().constant.getObj(),
				assignConst.getConstValue().constant.getValue(), assignConst.getConstName(),
				assignConst.getLine()));
	}

	public void visit(ConstValueInt constValueInt) {
		constValueInt.constant = new Constant(MyTable.find("int"), constValueInt.getValue(), constValueInt.getLine());
	}

	public void visit(ConstValueBool constValueBool) {
		constValueBool.constant = new Constant(MyTable.find("bool"), constValueBool.getValue(),
				constValueBool.getLine());
	}

	public void visit(ConstValueChar constValueChar) {
		constValueChar.constant = new Constant(MyTable.find("char"), constValueChar.getValue(),
				constValueChar.getLine());
	}

	private LinkedList<Variable> variables = new LinkedList<>();

	// MOZE U METODU VISIT VARDECL
	// deklaracije globalnih promenljivih
	public void visit(DeclarationVar declarationVar) {
		for (Variable variable : variables) {
			if (MyTable.existsInCurrentScope(variable.getName())) {
				report_error("Globalna Promenljiva " + variable.getName() + " je vec deklarisana u trenutnom opsegu",
						variable.getLine());
			} else {
				report_info("Deklarisana globalna promenljiva " + variable.getName() + " tipa "
						+ variable.getObj().getName() + (variable.isArray() ? "[]" : ""), variable.getLine());
				if (variable.isArray()) {
					MyTable.insert(Obj.Var, variable.getName(), new Struct(Struct.Array, variable.getObj().getType()));
				} else {
					MyTable.insert(Obj.Var, variable.getName(), variable.getObj().getType());
				}
			}
		}
		variables.clear();
	}

	private boolean insideClass = false;

	public void visit(VarDecl varDecl) {
		if (insideClass) {
			for (Variable variable : variables) {
				variable.setObj(varDecl.getType().obj);
				if (MyTable.existsInCurrentScope(variable.getName())) {
					report_error("Polje klase " + variable.getName() + " je vec deklarisana u trenutnom opsegu",
							variable.getLine());
				} else {
					report_info(
							"Deklarisana polje klase promenljiva " + variable.getName() + " tipa "
									+ variable.getObj().getName() + (variable.isArray() ? "[]" : ""),
							variable.getLine());
					if (variable.isArray()) {
						MyTable.insert(Obj.Fld, variable.getName(),
								new Struct(Struct.Array, variable.getObj().getType()));
					} else {
						MyTable.insert(Obj.Fld, variable.getName(), variable.getObj().getType());
					}
				}
			}
		} else {
			for (Variable variable : variables) {
				variable.setObj(varDecl.getType().obj);
				if (MyTable.existsInCurrentScope(variable.getName())) {
					report_error("Lokalna Promenljiva " + variable.getName() + " je vec deklarisana u trenutnom opsegu",
							variable.getLine());
				} else {
					report_info(
							"Deklarisana lokalna promenljiva " + variable.getName() + " tipa "
									+ variable.getObj().getName() + (variable.isArray() ? "[]" : ""),
							variable.getLine());
					if (variable.isArray()) {
						MyTable.insert(Obj.Var, variable.getName(),
								new Struct(Struct.Array, variable.getObj().getType()));
					} else {
						MyTable.insert(Obj.Var, variable.getName(), variable.getObj().getType());
					}
				}
			}
		}
		variables.clear();
	}

	public void visit(VariableList variableList) {
		variables.add(new Variable(variableList.getVarName().variable));
	}

	public void visit(VariableListEnd variableListEnd) {
		variables.add(new Variable(variableListEnd.getVarName().variable));
	}

	public void visit(VarName varName) {
		varName.variable = new Variable(varName.getVarName(), varName.getOptionalBrackets() instanceof Brackets,
				varName.getLine());
	}

	private Obj methodObj;
	private int numberOfParameters = 0;
	private boolean isReturned = false;
	private boolean foundMain = false;

	// deklaracije metoda
	// pocetak metode
	public void visit(MethodStart methodStart) {
		if (MyTable.existsInCurrentScope(methodStart.getMethodName())) {
			report_error("Metoda " + methodStart.getMethodName() + " vec postoji u trenutnom opsegu", methodStart);
		} else {
			report_info("Zapoceta metoda " + methodStart.getMethodName(), methodStart);
			methodObj = MyTable.insert(Obj.Meth, methodStart.getMethodName(),
					methodStart.getReturnType().obj.getType());
			numberOfParameters = 0;
			isReturned = false;
			MyTable.openScope();
		}
	}

	// kraj metode
	public void visit(MethodDecl methodDecl) {
		if (methodObj.getType() != MyTable.noType && !isReturned) {
			report_error("Metoda " + methodDecl.getMethodStart().getMethodName() + " nema return naredbu", methodDecl);
		}
		if (methodObj.getType() == MyTable.noType && methodDecl.getMethodStart().getMethodName().equals("main")
				&& numberOfParameters == 0) {
			foundMain = true;
		}
		report_info("Zavrsena metoda " + methodDecl.getMethodStart().getMethodName(), methodDecl);
		methodObj.setLevel(numberOfParameters);
		MyTable.chainLocalSymbols(methodObj);
		MyTable.closeScope();
	}

	// formalni parametri
	public void visit(FormPar formPar) {
		Variable variable = formPar.getVarName().variable;
		variable.setObj(formPar.getType().obj);

		if (MyTable.existsInCurrentScope(variable.getName())) {
			report_error("Formalni parametar " + variable.getName() + " je vec deklarisana u trenutnom opsegu",
					variable.getLine());
		} else {
			report_info("Formalni parametar " + variable.getName() + " tipa " + variable.getObj().getName()
					+ (variable.isArray() ? "[]" : ""), variable.getLine());
			if (variable.isArray()) {
				MyTable.insert(Obj.Var, variable.getName(), new Struct(Struct.Array, variable.getObj().getType()));
			} else {
				MyTable.insert(Obj.Var, variable.getName(), variable.getObj().getType());
			}
			numberOfParameters++;
		}
	}

	// return tip
	public void visit(ReturnT returnT) {
		returnT.obj = returnT.getType().obj;
	}

	public void visit(ReturnVoid returnVoid) {
		returnVoid.obj = MyTable.noObj;
	}

	// tipovi
	public void visit(TypeInt typeInt) {
		typeInt.obj = MyTable.find("int");
	}

	public void visit(TypeBool typeBool) {
		typeBool.obj = MyTable.find("bool");
	}

	public void visit(TypeChar typeChar) {
		typeChar.obj = MyTable.find("char");
	}

	public void visit(TypeCustom typeCustom) {
		Obj typeNode = MyTable.find(typeCustom.getTypeName());
		if (typeNode == MyTable.noObj) {
			report_error("Nije pronadjen tip " + typeCustom.getTypeName() + " u tabeli simbola! ", typeCustom);
			typeCustom.obj = MyTable.noObj;
		} else {
			if (typeNode.getKind() == Obj.Type) {
				typeCustom.obj = typeNode;
			} else {
				report_error("Greska: Ime " + typeCustom.getTypeName() + " ne predstavlja tip!", typeCustom);
				typeCustom.obj = MyTable.noObj;
			}
		}
	}
}
