package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.mysymboltable.*;
import rs.ac.bg.etf.pp1.mysymboltable.ConstValue;
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

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean passed() {
		return errorDetected == false;
	}

	// simbolicke konstante
	private LinkedList<ConstValue> constants = new LinkedList();
	
	public void visit(ConstDecl constDecl) {
		constants.addFirst(new ConstValue(constDecl.getConstValue().constvalue.getObj(), 
				constDecl.getConstValue().constvalue.getValue(), 
				constDecl.getConstName()));
		/*for (ConstValue constant : constants) {
			report_info("----Konstanta " + constant.getName() + "vrednost" + constant.getValue(), constDecl);
		}*/
		
		for (ConstValue constant : constants) {
			if (MyTable.existsInCurrentScope(constant.getName())) {
				report_error("Konstanta " + constant.getName() + " je vec deklarisana u trenutnom opsegu", constDecl);
			} else if (!constant.getObj().getType().equals(constDecl.getType().obj.getType())) {
				report_error("Konstanti " + constant.getName() + " koja je tipa " + constDecl.getType().obj.getName()
						+ " ne moze se dodeliti vrednost " + constant.getValue()
						+ " koja je tipa " + constant.getObj().getName(), constDecl);
			} else {
				report_info("Deklarisana konstanta " + constant.getName() + " tipa " + constDecl.getType().obj.getName()
								+ " vrednost=" + constant.getValue(), constDecl);
				MyTable.insertConstant(Obj.Var, constant.getName(), constant.getObj().getType(),
						constant.getValue());
			}
		}
		constants.clear();
	}
	
	public void visit(AssignConstantList assignConstantList) {		
		constants.addFirst(new ConstValue(assignConstantList.getConstValue().constvalue.getObj(), 
				assignConstantList.getConstValue().constvalue.getValue(), 
				assignConstantList.getConstName()));
	}

	public void visit(ConstValueInt constValueInt) {
		constValueInt.constvalue = new ConstValue(MyTable.find("int"), constValueInt.getValue());	
	}

	public void visit(ConstValueBool constValueBool) {
		constValueBool.constvalue= new ConstValue(MyTable.find("bool"), constValueBool.getValue());	
	}

	public void visit(ConstValueChar constValueChar) {
		constValueChar.constvalue = new ConstValue(MyTable.find("char"), constValueChar.getValue());			
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
