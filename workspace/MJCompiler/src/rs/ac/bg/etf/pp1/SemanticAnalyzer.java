package rs.ac.bg.etf.pp1;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.mysymboltable.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

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

    // region deklaracije konstanti
    private LinkedList<Constant> constants = new LinkedList<>();

    public void visit(ConstantDecl constantDecl) {
        for (Constant constant : constants) {
            if (MyTable.existsInCurrentScope(constant.getName())) {
                report_error("Ime " + constant.getName() + " vec postoji u trenutnom opsegu", constantDecl);
            } else if (!constant.getObj().getType().equals(constantDecl.getType().struct)) {
                report_error("Neslaganje u tipu konstante i tipu dodeljene vrednosti", constant.getLine());
            } else {
                report_info("Deklarisana konstanta " + constant.getName() + " sa vrednosu " + constant.getValue(),
                        constantDecl);
                MyTable.insertConstant(Obj.Con, constant.getName(), constant.getObj().getType(), constant.getValue());
            }
        }
        constants.clear();
    }

    public void visit(AssignConstant assignConstant) {
        constants.add(new Constant(assignConstant.getConstValue().constant.getObj(),
                assignConstant.getConstValue().constant.getValue(), assignConstant.getConstName(),
                assignConstant.getLine()));
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

    public void visit(VariableDecl variableDecl) {
        if (currentClass != null && currentMethod == null) {
            for (Variable variable : variables) {
                variable.setStruct(variableDecl.getType().struct);
                if (MyTable.existsInCurrentScope(variable.getName())) {
                    report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
                } else {
                    report_info("Deklarisana polje klase promenljiva " + variable.getName(), variable.getLine());
                    if (variable.isArray()) {
                        MyTable.insert(Obj.Fld, variable.getName(), new MyStruct(MyStruct.Array, variable.getStruct()));
                    } else {
                        MyTable.insert(Obj.Fld, variable.getName(), variable.getStruct());
                    }
                }
            }
        } else {
            for (Variable variable : variables) {
                variable.setStruct(variableDecl.getType().struct);
                if (MyTable.existsInCurrentScope(variable.getName())) {
                    report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
                } else {
                    report_info("Deklarisana promenljiva " + variable.getName(), variable.getLine());
                    if (variable.isArray()) {
                        MyTable.insert(Obj.Var, variable.getName(), new MyStruct(MyStruct.Array, variable.getStruct()));
                    } else {
                        MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
                    }
                }
            }
        }
        variables.clear();
    }

    public void visit(VariableList variableList) {
        if (variableList.getVarName().variable != null) {
            variables.add(new Variable(variableList.getVarName().variable));
        }
    }

    public void visit(VariableListEnd variableListEnd) {
        if (variableListEnd.getVarName().variable != null) {
            variables.add(new Variable(variableListEnd.getVarName().variable));
        }
    }

    public void visit(VariableName variableName) {
        variableName.variable = new Variable(variableName.getVarName(),
                variableName.getOptionalBrackets() instanceof Brackets, variableName.getLine());
    }

    // endregion

    // region deklaracija klase
    // pocetak klase
    private MyStruct currentClass = null;

    public void visit(ClassStart classStart) {
        if (MyTable.existsInCurrentScope(classStart.getClassName())) {
            report_error("Ime " + classStart.getClassName() + " vec postoji u trenutnom opsegu", classStart);
            currentClass = new MyStruct(MyStruct.None);
        } else {
            report_info("Zapoceta klasa " + classStart.getClassName(), classStart);
            currentClass = new MyStruct(MyStruct.Class);
            MyTable.insert(Obj.Type, classStart.getClassName(), currentClass);
            classStart.struct = currentClass;
        }
        MyTable.openScope();
    }

    public void visit(ClassVarDeclList classVarDeclList) {
        if (currentClass != null) {
            MyTable.chainLocalSymbols(currentClass);
        }
    }

    // kraj klase
    public void visit(ClassDecl classDecl) {
        currentClass = null;
        MyTable.closeScope();
    }
    // endregion

    // region deklaracije metoda
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
        if (variable == null) {
            //report_error("OPORAVAK Greska u formalnim parametrima", formalPar);
            return;
        }
        variable.setStruct(formPar.getType().struct);

        if (MyTable.existsInCurrentScope(variable.getName())) {
            report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
        } else {
            Obj newFormalParameter;
            report_info("Formalni parametar " + variable.getName(), variable.getLine());
            if (variable.isArray()) {
                newFormalParameter = MyTable.insert(Obj.Var, variable.getName(),
                        new MyStruct(MyStruct.Array, variable.getStruct()));
            } else {
                newFormalParameter = MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
            }
            numberOfParameters++;
            newFormalParameter.setFpPos(numberOfParameters);
        }
    }

    public void visit(ReturnT returnT) {
        returnT.struct = returnT.getType().struct;
    }

    public void visit(ReturnVoid returnVoid) {
        returnVoid.struct = Tab.noType;
    }

    // endregion

    // region standard functions
    public void visit(StandardFunctionChr standardFunctionChr) {
        Struct struct = standardFunctionChr.getExpr().struct;
        if (struct.getKind() != Struct.Int) {
            report_error("Argument mora biti tipa int", standardFunctionChr);
        }
    }

    public void visit(StandardFunctionOrd standardFunctionOrd) {
        Struct struct = standardFunctionOrd.getExpr().struct;
        if (struct.getKind() != Struct.Char) {
            report_error("Argument mora biti tipa char", standardFunctionOrd);
        }
    }

    public void visit(StandardFunctionLen standardFunctionLen) {
        Struct struct = standardFunctionLen.getExpr().struct;
        if (struct.getKind() != Struct.Array) {
            report_error("Argument mora biti niz", standardFunctionLen);
        }
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

    // region statement
    public void visit(Statement statement) {
        System.err.println("statement" + statement);
    }

    public void visit(DesignatorStatement designatorStatement) {
        System.err.println("statement -> designatorStatemenet");
        System.out.println(designatorStatement);
    }

    public void visit(StatementReturn statementReturn) {
        if (currentMethod == null) {
            report_error("Return ne sme da postoji van funkcije ili metode", statementReturn);
        } else {
            isReturned = true;
            if (currentMethod.getType().getKind() != Struct.None) {
                report_error("Void metoda " + currentMethod.getName() + " ne sme imati return", statementReturn);
            }
        }
    }

    public void visit(StatementReturnExpr statementReturnExpr) {
        if (currentMethod == null) {
            report_error("Return ne sme da postoji van funkcije ili metode", statementReturnExpr);
        } else {
            isReturned = true;
            // provera osnovne klase i interfejsa
            if (!currentMethod.getType().compatibleWith(statementReturnExpr.getExpr().struct)) {
                report_error("Povratni tip metode " + currentMethod.getName()
                        + " nije kompatibilan sa tipom povratne vrednosti", statementReturnExpr);
            }
        }
    }

    public void visit(StatementRead statementRead) {
        if (isLeftValue(statementRead.getDesignator())) {
            Struct struct = statementRead.getDesignator().obj.getType();
            if (struct.getKind() != Struct.Int && struct.getKind() != Struct.Char
                    && struct.getKind() != MyStruct.Bool) {
                report_error("Argument mora biti tipa int, char ili bool", statementRead);
            }
        } else {
            report_error("Argument funkcije read mora biti promenljiva, polje klase ili element niza", statementRead);
        }
    }

    public void visit(StatementPrint statementPrint) {
        Struct struct = statementPrint.getExpr().struct;
        if (struct.getKind() != Struct.Int && struct.getKind() != Struct.Char && struct.getKind() != MyStruct.Bool) {
            report_error("Argument funkcije print mora biti tipa int, char ili bool", statementPrint);
        }
    }
    // endregion

    // region designator statement
    public void visit(DesignatorAssign designatorAssign) {
        // System.err.println(designatorAssign);
        if (designatorAssign.getExpr() instanceof ExprERR) {
            report_error("OPORAVAK Greska kod dodele vrednosti", designatorAssign);
            return;
        }
        if (isLeftValue(designatorAssign.getDesignator())) {
            Struct left = designatorAssign.getDesignator().obj.getType();
            Struct right = designatorAssign.getExpr().struct;
            if (left.equals(right)) {
                report_info("Dodela", designatorAssign);
                return;
            }
            if (left.isRefType() && right == MyTable.nullType) {
                report_info("Dodela", designatorAssign);

                return;
            }
            // proveriti interfejs i izvedene klase
            report_error("Tipovi u dodeli nisu kompatiblini", designatorAssign);
        } else {
            report_error("Kod dodele vrednosti leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorAssign);
        }
    }

    public void visit(DesignatorStandardFunction designatorStandardFunction) {
        // System.out.println(designatorStandardFunction);
    }

    public void visit(DesignatorIncrement designatorIncrement) {
        if (isLeftValue(designatorIncrement.getDesignator())) {
            if (designatorIncrement.getDesignator().obj.getKind() != MyStruct.Int) {
                report_error("Samo se ceo broj moze inkrementirati", designatorIncrement);
            }
        } else {
            report_error("Kod inkrementiranja leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorIncrement);
        }
    }

    public void visit(DesignatorDecrement designatorDecrement) {
        if (isLeftValue(designatorDecrement.getDesignator())) {
            if (designatorDecrement.getDesignator().obj.getKind() != MyStruct.Int) {
                report_error("Samo se ceo broj moze dekrementirati", designatorDecrement);
            }
        } else {
            report_error("Kod dekrementiranja leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorDecrement);
        }

    }
    // endregion

    // region expr
    public void visit(ExprTerm exprTerm) {
        exprTerm.struct = exprTerm.getTerm().struct;
    }

    public void visit(ExprNegativeTerm exprNegativeTerm) {
        if (exprNegativeTerm.getTerm().struct != MyTable.intType) {
            report_error("Negacija moze da se primeni samo na cele brojeve", exprNegativeTerm);
            exprNegativeTerm.struct = MyTable.noType;
        } else {
            exprNegativeTerm.struct = exprNegativeTerm.getTerm().struct;
        }
    }

    public void visit(ExprAddop exprAddop) {
        if (exprAddop.getExpr().struct.compatibleWith(exprAddop.getTerm().struct)
                && exprAddop.getExpr().struct == MyTable.intType) {
            exprAddop.struct = exprAddop.getTerm().struct;
        } else {
            report_error("Operacija radi samo nad celim brojevima", exprAddop);
            exprAddop.struct = MyTable.noType;
        }
    }

    public void visit(ExprERR exprErr) {
        report_error("Greska u izrazu", exprErr);
        exprErr.struct = MyTable.noType;
    }

    // endregion

    // region term
    public void visit(TermFactor termFactor) {
        termFactor.struct = termFactor.getFactor().struct;
    }

    public void visit(TermMulop termMulop) {
        if (termMulop.getFactor().struct == MyTable.intType && termMulop.getTerm().struct == MyTable.intType) {
            termMulop.struct = termMulop.getFactor().struct;
        } else {
            report_error("Operacija radi samo nad celim brojevima", termMulop);
            termMulop.struct = MyTable.noType;
        }
    }
    // endregion

    // region factor
    public void visit(FactorDesignator factorDesignator) {
        factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
    }

    public void visit(FactorStandardFunction factorStandardFunction) {
        // ??
        System.out.println(factorStandardFunction);

    }

    public void visit(FactorConst factorConst) {
        factorConst.struct = factorConst.getConstValue().constant.getObj().getType();
    }

    public void visit(FactorNewObj factorNewObj) {
        if (factorNewObj.getType().struct.getKind() != MyStruct.Class) {
            report_error("Sa new moze da se napravi samo objekat klasnog tipa", factorNewObj);
            factorNewObj.struct = MyTable.noType;
        } else {
            factorNewObj.struct = factorNewObj.getType().struct;
        }
    }

    public void visit(FactorNewArray factorNewArray) {
        if (factorNewArray.getExpr().struct != MyTable.intType) {
            report_error("Velicina niza mora biti int", factorNewArray);
            factorNewArray.struct = MyTable.noType;
        } else {
            factorNewArray.struct = factorNewArray.getType().struct;
        }
    }

    public void visit(FactorNull factorNull) {
        factorNull.struct = MyTable.nullType;
    }

    public void visit(FactorExpression factorExpression) {
        factorExpression.struct = factorExpression.getExpr().struct;
    }
    // endregion

    // region designator
    public void visit(DesignatorName designatorName) {
        Obj obj = MyTable.find(designatorName.getName());
        if (obj == MyTable.noObj) {
            report_error("Ime " + designatorName.getName() + " nije deklarisano", designatorName);
            designatorName.obj = MyTable.noObj;
        } else {
            designatorName.obj = obj;
        }
    }

    public void visit(DesignatorPointAccess designatorPointAccess) {
        if (designatorPointAccess.getDesignator().obj != MyTable.noObj) {
            Struct struct = designatorPointAccess.getDesignator().obj.getType();
            if (struct.getKind() != MyStruct.Class && struct.getKind() != MyStruct.Enum) {
                report_error("Moze se pristupati samo poljima klasnih tipova i enumeratora", designatorPointAccess);
                designatorPointAccess.obj = MyTable.noObj;
            } else {
                Obj elem = struct.getMembers().searchKey(designatorPointAccess.getName());
                if (elem == null) {
                    report_error("Ne postoji ime" + designatorPointAccess.getName(), designatorPointAccess);
                    designatorPointAccess.obj = MyTable.noObj;
                } else {
                    designatorPointAccess.obj = elem;
                }
            }
        } else {
            designatorPointAccess.obj = MyTable.noObj;
        }
    }

    public void visit(DesignatorArrayAccess designatorArrayAccess) {
        Obj obj = designatorArrayAccess.getDesignator().obj;
        if (obj != MyTable.noObj) {
            String arrayName = designatorArrayAccess.getDesignator().obj.getName();
            if (obj.getType().getKind() != MyStruct.Array) {
                report_error("Promenljiva " + arrayName + " nije niz", designatorArrayAccess);
            } else if (designatorArrayAccess.getExpr().struct.getKind() != MyStruct.Int) {
                report_error("Za indeksiranje niza mora da se koristi int", designatorArrayAccess);
            } else {
                designatorArrayAccess.obj = new Obj(Obj.Elem, arrayName, obj.getType().getElemType());
            }
        } else {
            designatorArrayAccess.obj = MyTable.noObj;
        }
    }

    public boolean isLeftValue(Designator designator) {
        if (designator.obj == MyTable.noObj) {
            return false;
        }
        if (designator.obj.getKind() == Obj.Fld) {
            if (designator.obj.getType().getKind() == MyStruct.Array) {
                return false;
            }
            return true;
        }
        if (designator.obj.getKind() == Obj.Elem) {
            return true;
        }
        if (designator.obj.getKind() == Obj.Var && designator.obj.getType().getKind() != MyStruct.None) {
            return true;
        }
        return false;
    }
    // endregion

}
