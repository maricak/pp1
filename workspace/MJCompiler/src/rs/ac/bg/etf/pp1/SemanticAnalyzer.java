package rs.ac.bg.etf.pp1;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.mysymboltable.*;
import rs.ac.bg.etf.pp1.mysymboltable.Constant;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

    boolean errorDetected = false;

    // region report
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
    // endregion
    
    public boolean passed() {
        return !errorDetected && foundMain;
    }

    // region program
    public void visit(ProgramStart programStart) {
        programStart.obj = MyTable.insert(Obj.Prog, programStart.getProgramName(), MyTable.noType);
        MyTable.openScope();
    }

    public void visit(Program program) {
        MyTable.chainLocalSymbols(program.getProgramStart().obj);
        MyTable.closeScope();
        if (!foundMain)
            report_error("Nije deklarisana funkcija main", null);
    }
    // endregion

    // region deklaracije simbolickih konstanti
    private LinkedList<Constant> constants = new LinkedList<>();

    public void visit(ConstDecl constDecl) {
        for (Constant constant : constants) {
            if (MyTable.existsInCurrentScope(constant.getName())) {
                report_error("Ime " + constant.getName() + " vec postoji u trenutnom opsegu", constDecl);
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

    public void visit(AssignConst assignConst) {
        constants.add(new Constant(assignConst.getConstValue().constant.getObj(),
                assignConst.getConstValue().constant.getValue(), assignConst.getConstName(), assignConst.getLine()));
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

    // endregion

    // region deklaracije promenljivih
    private LinkedList<Variable> variables = new LinkedList<>();

    // MOZE U METODU VISIT VARDECL
    // deklaracije globalnih promenljivih
    public void visit(DeclarationVar declarationVar) {
        for (Variable variable : variables) {
            if (MyTable.existsInCurrentScope(variable.getName())) {
                report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu",
                        variable.getLine());
            } else {
                report_info("Deklarisana globalna promenljiva " + variable.getName() + " tipa "
                        + variable.getObj().getName() + (variable.isArray() ? "[]" : ""), variable.getLine());
                if (variable.isArray()) {
                    MyTable.insert(Obj.Var, variable.getName(), new MyStruct(Struct.Array, variable.getObj().getType()));
                } else {
                    MyTable.insert(Obj.Var, variable.getName(), variable.getObj().getType());
                }
            }
        }
        variables.clear();
    }

    public void visit(VarDecl varDecl) {
        if (currentClass != null && currentMethod == null) {
            for (Variable variable : variables) {
                variable.setObj(varDecl.getType().obj);
                if (MyTable.existsInCurrentScope(variable.getName())) {
                    report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu",
                            variable.getLine());
                } else {
                    report_info(
                            "Deklarisana polje klase promenljiva " + variable.getName() + " tipa "
                                    + variable.getObj().getName() + (variable.isArray() ? "[]" : ""),
                            variable.getLine());
                    if (variable.isArray()) {
                        MyTable.insert(Obj.Fld, variable.getName(),
                                new MyStruct(Struct.Array, variable.getObj().getType()));
                    } else {
                        MyTable.insert(Obj.Fld, variable.getName(), variable.getObj().getType());
                    }
                }
            }          
        } else {
            for (Variable variable : variables) {
                variable.setObj(varDecl.getType().obj);
                if (MyTable.existsInCurrentScope(variable.getName())) {
                    report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu",
                            variable.getLine());
                } else {
                    report_info(
                            "Deklarisana lokalna promenljiva " + variable.getName() + " tipa "
                                    + variable.getObj().getName() + (variable.isArray() ? "[]" : ""),
                            variable.getLine());
                    if (variable.isArray()) {
                        MyTable.insert(Obj.Var, variable.getName(),
                                new MyStruct(Struct.Array, variable.getObj().getType()));
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

    // endregion

    // region deklaracija klase
    // pocetak klase
    private MyStruct currentClass = null;

    public void visit(ClassStart classStart) {
        if(MyTable.existsInCurrentScope(classStart.getClassName())) {
            report_error("Ime " + classStart.getClassName() + " vec postoji u trenutnom opsegu", classStart);
            currentClass = new MyStruct(Struct.None);
        } else {
            report_info("Zapoceta klasa " + classStart.getClassName(), classStart);
            currentClass = new MyStruct(Struct.Class); 
            MyTable.insert(Obj.Type, classStart.getClassName(), currentClass);
            classStart.struct = currentClass;
        }
        MyTable.openScope();
    }
  
    public void visit(ClassVarDeclList classVarDeclList) {
        if(currentClass != null) {
            MyTable.chainLocalSymbols(currentClass);
            System.out.println("klasa kraj polja");
        }
    }
    // kraj klase
    public void visit(ClassDecl classDecl) {
        System.out.println("klasa kraj opsega");
        currentClass = null;
        MyTable.closeScope();
    }
    // endregion

    // region deklaracije metoda i return tip
    private Obj currentMethod = null;
    private int numberOfParameters = 0;
    private boolean isReturned = false;
    private boolean foundMain = false;

    // pocetak metode
    public void visit(MethodStart methodStart) {
        if (MyTable.existsInCurrentScope(methodStart.getMethodName())) {
            report_error("Ime " + methodStart.getMethodName() + " vec postoji u trenutnom opsegu", methodStart);
            currentMethod = new Obj(Obj.Meth, methodStart.getMethodName(), MyTable.noType);
        } else {
            report_info("Zapoceta metoda " + methodStart.getMethodName(), methodStart);
            currentMethod = MyTable.insert(Obj.Meth, methodStart.getMethodName(),
                    methodStart.getReturnType().obj.getType());
            methodStart.obj = currentMethod;
        }
        numberOfParameters = 0;
        isReturned = false;
        MyTable.openScope();

        if(currentClass != null) {
            Obj t = MyTable.insert(Obj.Var, "this", currentClass);
            t.setFpPos(1);
            numberOfParameters++;
        }
    }

    // kraj metode
    public void visit(MethodDecl methodDecl) {
        if (currentMethod != null) {
            if (!isReturned && currentMethod.getType() != MyTable.noType) {
                report_error("Metoda " + methodDecl.getMethodStart().getMethodName() + " nema return naredbu",
                        methodDecl);
            }
            if (currentMethod.getName().equals("main")) {
                if (numberOfParameters != 0) {
                    report_error("Metoda main ne sme imati argumente", methodDecl);
                } else if (currentMethod.getType() != MyTable.noType) {
                    report_error("Metoda main nmora biti tipa void", methodDecl);
                } else {
                    foundMain = true;
                }
            }
            MyTable.chainLocalSymbols(currentMethod);
            currentMethod.setLevel(numberOfParameters);
        }
        isReturned = false;
        numberOfParameters = 0;
        currentMethod = null;
        report_info("Zavrsena metoda " + methodDecl.getMethodStart().getMethodName(), methodDecl);    
        MyTable.closeScope();
    }

    // formalni parametri
    public void visit(FormPar formPar) {
        Variable variable = formPar.getVarName().variable;
        variable.setObj(formPar.getType().obj);

        if (MyTable.existsInCurrentScope(variable.getName())) {
            report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu",
                    variable.getLine());
        } else {
            Obj newFormalParameter;
            report_info("Formalni parametar " + variable.getName() + " tipa " + variable.getObj().getName()
                    + (variable.isArray() ? "[]" : ""), variable.getLine());
            if (variable.isArray()) {
                newFormalParameter = MyTable.insert(Obj.Var, variable.getName(), new MyStruct(Struct.Array, variable.getObj().getType()));
            } else {
                newFormalParameter = MyTable.insert(Obj.Var, variable.getName(), variable.getObj().getType());
            }
            numberOfParameters++;
            newFormalParameter.setFpPos(numberOfParameters);
        }
    }

    // return tip
    // public void visit(ReturnType returnType) {
    // if(currentMethod == MyTable.noObj) {
    // report_error("Return ne sme da postoji van funkcije ili metode", returnType);
    // } else {
    // isReturned = true;
    // if(!currentMethod.getType().compatibleWith(returnType.obj.getType())) {
    // report_error("Povratni tip metode " + currentMethod.getName() + " se neslaze
    // sa tipom u naredbi return", returnType);
    // }
    // }
    // }
    public void visit(ReturnT returnT) {
        returnT.obj = returnT.getType().obj;
    }

    public void visit(ReturnVoid returnVoid) {
        returnVoid.obj = new Obj(Obj.Type, "void", MyTable.noType);
    }

    // endregion

    // region tipovi
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
    // endregion

}
