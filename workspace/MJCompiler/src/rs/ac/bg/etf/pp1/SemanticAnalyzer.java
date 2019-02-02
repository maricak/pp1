package rs.ac.bg.etf.pp1;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.mysymboltable.*;
import rs.etf.pp1.symboltable.Tab;
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
            } else if (!constant.getObj().getType().equals(constDecl.getType().struct)) {
                report_error("Neslaganje u tipu konstante i tipu dodeljene vrednosti", constant.getLine());
            } else {
                report_info("Deklarisana konstanta " + constant.getName() + " sa vrednosu " + constant.getValue(),
                        constDecl);
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

    // MOZE U METODU VISIT VARDECL I JESTE TAMO OVO NE raDI
    // deklaracije globalnih promenljivih
    public void visit(DeclarationVar declarationVar) {
        report_info("Nisam ovde", null);
    }

    public void visit(VarDecl varDecl) {
        if (currentClass != null && currentMethod == null) {
            for (Variable variable : variables) {
                variable.setStruct(varDecl.getType().struct);
                if (MyTable.existsInCurrentScope(variable.getName())) {
                    report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
                } else {
                    report_info("Deklarisana polje klase promenljiva " + variable.getName(), variable.getLine());
                    if (variable.isArray()) {
                        MyTable.insert(Obj.Fld, variable.getName(), new MyStruct(Struct.Array, variable.getStruct()));
                    } else {
                        MyTable.insert(Obj.Fld, variable.getName(), variable.getStruct());
                    }
                }
            }
        } else {
            for (Variable variable : variables) {
                variable.setStruct(varDecl.getType().struct);
                if (MyTable.existsInCurrentScope(variable.getName())) {
                    report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
                } else {
                    report_info("Deklarisana promenljiva " + variable.getName(), variable.getLine());
                    if (variable.isArray()) {
                        MyTable.insert(Obj.Var, variable.getName(), new MyStruct(Struct.Array, variable.getStruct()));
                    } else {
                        MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
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
        if (MyTable.existsInCurrentScope(classStart.getClassName())) {
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
        if (currentClass != null) {
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
            currentMethod = MyTable.insert(Obj.Meth, methodStart.getMethodName(), methodStart.getReturnType().struct);
            methodStart.obj = currentMethod;
        }
        numberOfParameters = 0;
        isReturned = false;
        MyTable.openScope();

        if (currentClass != null) {
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
        variable.setStruct(formPar.getType().struct);

        if (MyTable.existsInCurrentScope(variable.getName())) {
            report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
        } else {
            Obj newFormalParameter;
            report_info("Formalni parametar " + variable.getName(), variable.getLine());
            if (variable.isArray()) {
                newFormalParameter = MyTable.insert(Obj.Var, variable.getName(),
                        new MyStruct(Struct.Array, variable.getStruct()));
            } else {
                newFormalParameter = MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
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
        returnT.struct = returnT.getType().struct;
    }

    public void visit(ReturnVoid returnVoid) {
        returnVoid.struct = Tab.noType;
    }

    // endregion

    // region tipovi
    public void visit(TypeInt typeInt) {
        typeInt.struct = MyTable.intType;
    }

    public void visit(TypeBool typeBool) {
        typeBool.struct = MyTable.boolType;
    }

    public void visit(TypeChar typeChar) {
        typeChar.struct = MyTable.charType;
    }

    public void visit(TypeCustom typeCustom) {
        Obj typeNode = MyTable.find(typeCustom.getTypeName());
        if (typeNode == MyTable.noObj) {
            report_error("Nije pronadjen tip " + typeCustom.getTypeName() + " u tabeli simbola! ", typeCustom);
            typeCustom.struct = MyTable.noType;
        } else {
            if (typeNode.getKind() == Obj.Type) {
                typeCustom.struct = typeNode.getType();
            } else {
                report_error("Greska: Ime " + typeCustom.getTypeName() + " ne predstavlja tip!", typeCustom);
                typeCustom.struct = MyTable.noType;
            }
        }
    }
    // endregion

    // region expr
    public void visit(ExprTerm exprTerm) {
        exprTerm.struct = exprTerm.getTerm().struct;
        report_info("Expression -> term", exprTerm);
    }

    public void visit(ExprNegativeTerm exprNegativeTerm) {
        if (exprNegativeTerm.getTerm().struct != MyTable.intType) {
            report_error("Negacija moze da se primeni samo na cele brojeve", exprNegativeTerm);
            exprNegativeTerm.struct = MyTable.noType;
        } else {
            exprNegativeTerm.struct = exprNegativeTerm.getTerm().struct;
            report_info("Expression -> negative term", exprNegativeTerm);
        }
    }

    public void visit(ExprAddop exprAddop) {
        if (exprAddop.getExpr().struct.compatibleWith(exprAddop.getTerm().struct)
                && exprAddop.getExpr().struct == MyTable.intType) {
            exprAddop.struct = exprAddop.getTerm().struct;
            report_info("Expression -> sabiranje", exprAddop);
        } else {
            report_error("Operacija radi samo nad celim brojevima", exprAddop);
        }
    }

    // endregion

    // region term
    public void visit(TermFactor termFactor) {
        termFactor.struct = termFactor.getFactor().struct;
        report_info("term -> factor", termFactor);
    }

    public void visit(TermMulop termMulop) {
        if (termMulop.getFactor().struct == MyTable.intType && termMulop.getTerm().struct == MyTable.intType) {
            termMulop.struct = termMulop.getFactor().struct;
            report_info("term -> mnozenje", termMulop);
        } else {
            report_error("Operacija radi samo nad celim brojevima", termMulop);
            termMulop.struct = MyTable.noType;
        }
    }
    // endregion

    // region factor

    public void visit(FactorConst factorConst) {
        factorConst.struct = factorConst.getConstValue().constant.getObj().getType();
        report_info("factor -> constanta", factorConst);
    }

    public void visit(FactorNewObj factorNewObj) {
        if (factorNewObj.getType().struct.getKind() != Struct.Class) {
            report_error("Sa new moze da se napravi samo objekat klasnog tipa", factorNewObj);
            factorNewObj.struct = MyTable.noType;
        } else {
            factorNewObj.struct = factorNewObj.getType().struct;
            report_info("factor -> new obj", factorNewObj);
        }
    }

    public void visit(FactorNewArray factorNewArray) {
        if (factorNewArray.getExpr().struct != MyTable.intType) {
            report_error("Velicina niza mora biti int", factorNewArray);
            factorNewArray.struct = MyTable.noType;
        } else {
            factorNewArray.struct = factorNewArray.getType().struct;
            report_info("factor -> new array", factorNewArray);
        }
    }

    public void visit(FactorNull factorNull) {
        factorNull.struct = MyTable.nullType;
        report_info("factor -> null", factorNull);
    }

    public void visit(FactorExpression factorExpression) {
        factorExpression.struct = factorExpression.getExpr().struct;
        report_info("factor -> expr u zagradama", factorExpression);
    }
    // endregion

    
}
