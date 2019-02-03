package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.mysymboltable.*;

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
                        if (arrayTypes.get(variable.getStruct()) == null) {
                            arrayTypes.put(variable.getStruct(), new Struct(Struct.Array, variable.getStruct()));
                        }
                        variable.setStruct(arrayTypes.get(variable.getStruct()));
                    }
                    MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
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
                        if (arrayTypes.get(variable.getStruct()) == null) {
                            arrayTypes.put(variable.getStruct(), new Struct(Struct.Array, variable.getStruct()));
                        }
                        variable.setStruct(arrayTypes.get(variable.getStruct()));
                    }
                    MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
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
    private Struct currentClass = null;

    public void visit(ClassStart classStart) {
        if (MyTable.existsInCurrentScope(classStart.getClassName())) {
            report_error("Ime " + classStart.getClassName() + " vec postoji u trenutnom opsegu", classStart);
            currentClass = new Struct(Struct.None);
        } else {
            report_info("Zapoceta klasa " + classStart.getClassName(), classStart);
            currentClass = new Struct(Struct.Class);
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
                report_error("Metoda " + methodDecl.getMethodStart().getMethodName() + " nema return naredbu", null);
            }
            if (currentMethod.getName().equals("main")) {
                if (numberOfParameters != 0) {
                    report_error("Metoda main ne sme imati argumente", null);
                } else if (currentMethod.getType() != MyTable.noType) {
                    report_error("Metoda main nmora biti tipa void", null);
                } else {
                    foundMain = true;
                }
            }
        }
        isReturned = false;
        numberOfParameters = 0;
        currentMethod = null;
        report_info("Zavrsena metoda " + methodDecl.getMethodStart().getMethodName(), null);
        MyTable.closeScope();
    }

    // formalni parametri
    public void visit(FormPar formPar) {
        Variable variable = formPar.getVarName().variable;
        if (variable == null) {
            // report_error("OPORAVAK Greska u formalnim parametrima", formalPar);
            return;
        }
        variable.setStruct(formPar.getType().struct);

        if (MyTable.existsInCurrentScope(variable.getName())) {
            report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
        } else {
            Obj newFormalParameter;
            report_info("Formalni parametar " + variable.getName(), variable.getLine());
            if (variable.isArray()) {
                if (arrayTypes.get(variable.getStruct()) == null) {
                    arrayTypes.put(variable.getStruct(), new Struct(Struct.Array, variable.getStruct()));
                }
                variable.setStruct(arrayTypes.get(variable.getStruct()));
            }
            newFormalParameter = MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());

            numberOfParameters++;
            newFormalParameter.setFpPos(numberOfParameters);
        }
    }

    // kraj pomenljivih
    public void visit(MethodVarsEnd methodVarsEnd) {
        MyTable.chainLocalSymbols(currentMethod);
        currentMethod.setLevel(numberOfParameters);
    }

    public void visit(ReturnT returnT) {
        returnT.struct = returnT.getType().struct;
    }

    public void visit(ReturnVoid returnVoid) {
        returnVoid.struct = Tab.noType;
    }

    // endregion

    // region standardne funkcije
    public void visit(StandardFunctionChr standardFunctionChr) {
        Struct struct = standardFunctionChr.getExpr().struct;
        if (struct.getKind() != Struct.Int) {
            report_error("Argument mora biti tipa int", standardFunctionChr);
        }
        standardFunctionChr.struct = MyTable.find("chr").getType();
    }

    public void visit(StandardFunctionOrd standardFunctionOrd) {
        Struct struct = standardFunctionOrd.getExpr().struct;
        if (struct.getKind() != Struct.Char) {
            report_error("Argument mora biti tipa char", standardFunctionOrd);
        }
        standardFunctionOrd.struct = MyTable.find("ord").getType();
    }

    public void visit(StandardFunctionLen standardFunctionLen) {
        Struct struct = standardFunctionLen.getExpr().struct;
        if (struct.getKind() != Struct.Array) {
            report_error("Argument mora biti niz", standardFunctionLen);
        }
        standardFunctionLen.struct = MyTable.find("len").getType();
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
            if (struct.getKind() != Struct.Int && struct.getKind() != Struct.Char && struct.getKind() != Struct.Bool) {
                report_error("Argument mora biti tipa int, char ili bool", statementRead);
            }
        } else {
            report_error("Argument funkcije read mora biti promenljiva, polje klase ili element niza", statementRead);
        }
    }

    public void visit(StatementPrint statementPrint) {
        Struct struct = statementPrint.getExpr().struct;
        if (struct.getKind() != Struct.Int && struct.getKind() != Struct.Char && struct.getKind() != Struct.Bool) {
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

    public void visit(DesignatorFunctionCall designatorFunctionCall) {
        Obj method = designatorFunctionCall.getDesignator().obj;
        LinkedList<Struct> arguments = functionCallArguments.pop();
        if (method.getKind() != Obj.Meth) {
            if (method != MyTable.noObj)
                report_error(method.getName() + " nije ime metode ni funkcije", designatorFunctionCall);
            return;
        } else {
            LinkedList<Obj> parameters = new LinkedList<>(method.getLocalSymbols());
            int numberOfParameters = method.getLevel();
            if (numberOfParameters != parameters.size()) {
                System.err.println("GRESKA PARAMTERI");
            }
            if (parameters.peekFirst() != null && parameters.peekFirst().getName().equals("this")) {
                parameters.removeFirst();
                numberOfParameters--;
            }

            if (arguments.size() != numberOfParameters) {
                report_error("Netacan broj argumenata za poziv " + method.getName(), designatorFunctionCall);
            } else {
                boolean incompatible = false;
                for (int i = 0; i < numberOfParameters; i++) {
                    Struct argument = arguments.remove();
                    Struct parameter = parameters.remove().getType();
                    if (!argument.assignableTo(parameter)) {
                        report_error((i + 1) + ". argument ne moze da se dodeli odgovarajucem parametru",
                                designatorFunctionCall);
                        incompatible = true;
                    }
                }
                if (!incompatible) {
                    if (designatorFunctionCall.getDesignator() instanceof DesignatorName) {
                        report_info("Poziv funkcije " + method.getName(), designatorFunctionCall);
                    } else {
                        report_info("Poziv metode " + method.getName(), designatorFunctionCall);
                    }
                }
            }
        }
    }

    public void visit(DesignatorIncrement designatorIncrement) {
        if (isLeftValue(designatorIncrement.getDesignator())) {
            Struct struct = designatorIncrement.getDesignator().obj.getType();
            if (struct.getKind() == Struct.Array && struct.getElemType().getKind() != Struct.Int) {
                report_error("Samo se ceo broj moze inkrementirati", designatorIncrement);
            } else if (struct.getKind() != Struct.Int) {
                report_error("Samo se ceo broj moze inkrementirati", designatorIncrement);
            }
        } else {
            report_error("Kod inkrementiranja leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorIncrement);
        }
    }

    public void visit(DesignatorDecrement designatorDecrement) {
        if (isLeftValue(designatorDecrement.getDesignator())) {
            Struct struct = designatorDecrement.getDesignator().obj.getType();
            if (struct.getKind() == Struct.Array && struct.getElemType().getKind() != Struct.Int) {
                report_error("Samo se ceo broj moze dekrementirati", designatorDecrement);
            } else if (struct.getKind() != Struct.Int) {
                report_error("Samo se ceo broj moze dekrementirati", designatorDecrement);
            }
        } else {
            report_error("Kod dekrementiranja leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorDecrement);
        }
    }
    // endregion

    // region act pars
    private Stack<LinkedList<Struct>> functionCallArguments = new Stack<>();

    public void visit(ActParameter actParameter) {
        functionCallArguments.peek().add(actParameter.getExpr().struct);
    }

    public void visit(ActParameters actParameters) {
        functionCallArguments.peek().add(actParameters.getExpr().struct);
    }
    // endregion

    // region condition
    public void visit(Condition condition) {
        if (condition.getCondTerm().struct != MyTable.boolType
                || condition.getCondTermList().struct != MyTable.boolType) {
            report_error("Uslov mora biti bool tipa", condition);
        }
    }
    // endregion

    // region condition term
    public void visit(ConditionTermList conditionTermList) {
        if (conditionTermList.getCondTermList().struct != MyTable.boolType) {
            conditionTermList.struct = MyTable.noType;
        } else if (conditionTermList.getCondTerm().struct != MyTable.boolType) {
            conditionTermList.struct = MyTable.noType;
        } else {
            conditionTermList.struct = MyTable.boolType;
        }
    }

    public void visit(ConditionTermListNO conditionTermListNO) {
        conditionTermListNO.struct = MyTable.boolType;
    }

    public void visit(CondTerm condTerm) {
        if (condTerm.getCondFact().struct != MyTable.boolType) {
            condTerm.struct = MyTable.noType;
        } else if (condTerm.getCondFactList().struct != MyTable.boolType) {
            condTerm.struct = MyTable.noType;
        } else {
            condTerm.struct = MyTable.boolType;
        }
    }
    // endregion

    // region condition fact
    public void visit(ConditionFactList conditionFactList) {
        if (conditionFactList.getCondFactList().struct != MyTable.boolType) {
            conditionFactList.struct = MyTable.noType;
        } else if (conditionFactList.getCondFact().struct != MyTable.boolType) {
            conditionFactList.struct = MyTable.noType;
        } else {
            conditionFactList.struct = MyTable.boolType;
        }
    }

    public void visit(ConditionFactListNO conditionFactListNO) {
        conditionFactListNO.struct = MyTable.boolType;
    }

    public void visit(CondFactExpr condFactExpr) {
        condFactExpr.struct = condFactExpr.getExpr().struct;
    }

    public void visit(CondFactCompare condFactCompare) {
        Struct struct1 = condFactCompare.getExpr().struct;
        Struct struct2 = condFactCompare.getExpr1().struct;
        if (!struct1.compatibleWith(struct2)) {
            report_error("Operandi nisu kompatibilni", condFactCompare);
        } else if ((struct1.isRefType() || struct2.isRefType()) && !(condFactCompare.getRelop() instanceof Equal)
                && !(condFactCompare.getRelop() instanceof NotEqual)) {
            report_error("Nizovi i objekti se mogu porediti samo sa != i ==", condFactCompare);
        }
        condFactCompare.struct = MyTable.boolType;
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
        factorStandardFunction.struct = factorStandardFunction.getStandardFunction().struct;

    }

    public void visit(FactorFunctionCall factorFunctionCall) {
        Obj method = factorFunctionCall.getDesignator().obj;
        LinkedList<Struct> arguments = functionCallArguments.pop();
        if (method.getKind() != Obj.Meth) {
            report_error(method.getName() + " nije ime metode ni funkcije", factorFunctionCall);
            factorFunctionCall.struct = MyTable.noType;
            return;
        } else {
            factorFunctionCall.struct = method.getType();
            LinkedList<Obj> parameters = new LinkedList<>(method.getLocalSymbols());
            int numberOfParameters = method.getLevel();
            if (parameters.peekFirst() != null && parameters.peekFirst().getName().equals("this")) {
                parameters.removeFirst();
                numberOfParameters--;
            }
            if (arguments.size() != numberOfParameters) {
                report_error("Netacan broj argumenata za poziv " + method.getName(), factorFunctionCall);
            } else {
                boolean incompatible = false;
                for (int i = 0; i < numberOfParameters; i++) {
                    Struct argument = arguments.remove();
                    Struct parameter = parameters.remove().getType();
                    if (!argument.assignableTo(parameter)) {
                        report_error((i + 1) + ". argument ne moze da se dodeli odgovarajucem parametru",
                                factorFunctionCall);
                        incompatible = true;
                    }
                }
                if (!incompatible) {
                    if (factorFunctionCall.getDesignator() instanceof DesignatorName) {
                        report_info("Poziv funkcije " + method.getName(), factorFunctionCall);
                    } else {
                        report_info("Poziv metode " + method.getName(), factorFunctionCall);
                    }
                }
            }
        }
    }

    public void visit(FactorConst factorConst) {
        factorConst.struct = factorConst.getConstValue().constant.getObj().getType();
    }

    public void visit(FactorNewObj factorNewObj) {
        if (factorNewObj.getType().struct.getKind() != Struct.Class) {
            report_error("Sa new moze da se napravi samo objekat klasnog tipa", factorNewObj);
            factorNewObj.struct = MyTable.noType;
        } else {
            factorNewObj.struct = factorNewObj.getType().struct;
        }
    }

    private HashMap<Struct, Struct> arrayTypes = new HashMap<>();

    public void visit(FactorNewArray factorNewArray) {
        if (factorNewArray.getExpr().struct != MyTable.intType) {
            report_error("Velicina niza mora biti int", factorNewArray);
            factorNewArray.struct = MyTable.noType;
        } else {
            if (arrayTypes.get(factorNewArray.getType().struct) == null) {
                arrayTypes.put(factorNewArray.getType().struct,
                        new Struct(Struct.Array, factorNewArray.getType().struct));
            }
            factorNewArray.struct = arrayTypes.get(factorNewArray.getType().struct);
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
        if (designatorName.getParent() instanceof FactorFunctionCall
                || designatorName.getParent() instanceof DesignatorFunctionCall) {
            functionCallArguments.push(new LinkedList<>());
        }
        // System.err.println("Ime " + designatorName.getName());
        Obj obj = MyTable.find(designatorName.getName());
        if (obj == MyTable.noObj) {
            report_error("Ime " + designatorName.getName() + " nije deklarisano", designatorName);
            designatorName.obj = MyTable.noObj;
        } else {
            designatorName.obj = obj;
        }
    }

    public void visit(DesignatorPointAccess designatorPointAccess) {
        if (designatorPointAccess.getParent() instanceof FactorFunctionCall
                || designatorPointAccess.getParent() instanceof DesignatorFunctionCall) {
            functionCallArguments.push(new LinkedList<>());
        }

        if (designatorPointAccess.getDesignator().obj != MyTable.noObj) {
            Struct struct = designatorPointAccess.getDesignator().obj.getType();
            if (struct.getKind() != Struct.Class && struct.getKind() != Struct.Enum) {
                report_error("Moze se pristupati samo poljima klasnih tipova i enumeratora", designatorPointAccess);
                designatorPointAccess.obj = MyTable.noObj;
            } else {
                Obj elem = struct.getMembers().stream().filter(e -> e.getName().equals(designatorPointAccess.getName()))
                        .findFirst().orElse(null);
                if (elem == null) {
                    report_error("Ne postoji ime " + designatorPointAccess.getName(), designatorPointAccess);
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
            if (obj.getType().getKind() != Struct.Array) {
                report_error("Promenljiva " + arrayName + " nije niz", designatorArrayAccess);
                designatorArrayAccess.obj = MyTable.noObj;
            } else if (designatorArrayAccess.getExpr().struct.getKind() != Struct.Int) {
                report_error("Za indeksiranje niza mora da se koristi int", designatorArrayAccess);
                designatorArrayAccess.obj = MyTable.noObj;
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
            if (designator.obj.getType().getKind() == Struct.Array) {
                return false;
            }
            return true;
        }
        if (designator.obj.getKind() == Obj.Elem) {
            return true;
        }
        if (designator.obj.getKind() == Obj.Var && designator.obj.getType().getKind() != Struct.None) {
            return true;
        }
        return false;
    }

    // endregion

}
