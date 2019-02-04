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
                // } else if
                // (!constant.getObj().getType().equals(constantDecl.getType().struct)) {
            } else if (!MyTable.equivalent(constant.getObj().getType(), constantDecl.getType().struct)) {
                report_error("Neslaganje u tipu konstante i tipu dodeljene vrednosti", constant.getLine());
            } else {
                report_info("Deklarisana konstanta " + constant.getName() + " sa vrednosu " + constant.getValue(),
                        constantDecl);
                MyTable.insertConstant(Obj.Con, constant.getName(), constant.getObj().getType(), constant.getValue());
            }
        }
        constants.clear();

    }

    public void visit(ConstantAssign constantAssign) {
        constants.add(new Constant(constantAssign.getConstValue().constant.getObj(),
                constantAssign.getConstValue().constant.getValue(), constantAssign.getConstName(),
                constantAssign.getLine()));
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
                    MyTable.insert(Obj.Fld, variable.getName(), variable.getStruct());
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
    private HashMap<String, Boolean> baseMethods = new HashMap<>();
    private HashMap<String, Boolean> interfaceMethods = new HashMap<>();

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

    // kraj deklaracjie polja
    public void visit(ClassVarDeclList classVarDeclList) {
        if (currentClass != null) {
            MyTable.chainLocalSymbols(currentClass);
            // dodavanje nasledjenih metoda
            if (currentClass.getElemType() != null) {
                for (Obj field : currentClass.getElemType().getMembers()) {
                    if (field.getKind() == Obj.Meth) {
                        baseMethods.put(field.getName(), false);
                        Obj newMethod = new Obj(field.getKind(), field.getName(), field.getType(), field.getAdr(),
                                field.getLevel());
                        newMethod.setFpPos(field.getFpPos());
                        if (!field.getLocalSymbols().isEmpty()) {
                            Scope tempScope = new Scope(null);
                            for (Obj localVar : field.getLocalSymbols()) {
                                Obj newLocalVar = new Obj(localVar.getKind(), localVar.getName(), localVar.getType(),
                                        localVar.getAdr(), localVar.getLevel());
                                tempScope.addToLocals(newLocalVar);
                            }
                            newMethod.setLocals(tempScope.getLocals());
                        }
                        MyTable.currentScope.addToLocals(newMethod);
                    }
                }
            }
        }
    }

    // kraj klase
    public void visit(ClassDecl classDecl) {
        baseMethods.forEach((k, v) -> System.err.println(k + " " + v));
        baseMethods.clear();

        // proveriti da li su sve implementirane
        interfaceMethods.forEach((k, v) -> {
            System.err.println(k + " " + v);
            if (v == false) {
                report_error("Interfejs metoda " + k + " nije implementirana", classDecl);
                MyTable.currentScope.getLocals().deleteKey(k);
            }
        });
        interfaceMethods.clear();
        
        currentClass = null;
        MyTable.closeScope();
    }

    // region implements

    public void visit(Interfaces interfaces) {
        Struct type = interfaces.getType().struct;
        if (type.getKind() != Struct.Interface) {
            report_error("Mogu se implementirati samo interfejsi", interfaces);
        } else if (currentClass != null) {
            if (currentClass.getImplementedInterfaces().stream().filter(s -> s == type).findFirst()
                    .orElse(null) != null) {
                report_error("Interfejsi u listi moraju biti jedinstveni", interfaces);
            }
            // proveriti da li je duplikat
            currentClass.getImplementedInterfaces().add(type);
        }
    }

    public void visit(Interface interfacee) {
        Struct type = interfacee.getType().struct;
        if (type.getKind() != Struct.Interface) {
            report_error("Mogu se implementirati samo interfejsi", interfacee);
        } else if (currentClass != null) {
            if (currentClass.getImplementedInterfaces().stream().filter(s -> s == type).findFirst()
                    .orElse(null) != null) {
                report_error("Interfejsi u listi moraju biti jedinstveni", interfacee);
            }
            // proveriti da li je duplikat
            currentClass.getImplementedInterfaces().add(type);
        }
    }

    public void visit(ImplementsList implementsList) {
        if (currentClass != null && currentClass.getImplementedInterfaces() != null) {
            for (Struct interfacee : currentClass.getImplementedInterfaces()) {
                // dodavanje nasledjenih metoda
                for (Obj interfaceMethod : interfacee.getMembers()) {
                    Obj other = MyTable.currentScope.findSymbol(interfaceMethod.getName());
                    if (other != null) {
                        // proveriti da li su parametri isti
                        if (!other.getType().equals(interfaceMethod.getType())) {
                            report_error(
                                    "Metode istog imena  (" + interfaceMethod.getName()
                                            + ") razlicith interfejsa moraju imati istu povratnu vrednost",
                                    implementsList);
                        }
                        if (interfaceMethod.getLevel() != other.getLevel()) {
                            report_error(
                                    "Metode " + interfaceMethod.getName()
                                            + " u dva razlicita interfejsa imaju razlicit broj parametara",
                                    implementsList);
                        } else {
                            if (interfaceMethod.getLevel() != 0) {
                                Obj[] locals = (Obj[]) interfaceMethod.getLocalSymbols().toArray();
                                Obj[] otherLocals = (Obj[]) other.getLocalSymbols().toArray();
                                for (int i = 0; i < locals.length; i++) {
                                    if (!locals[i].getType().equals(otherLocals[i].getType())) {
                                        report_error((i + 1) + ". parametar metode " + interfaceMethod.getName()
                                                + " nije isti u svim interfejsima", implementsList);
                                    }
                                }
                            }
                        }
                    } else {
                        Obj newMethod = new Obj(interfaceMethod.getKind(), interfaceMethod.getName(),
                                interfaceMethod.getType(), interfaceMethod.getAdr(), interfaceMethod.getLevel());
                        newMethod.setFpPos(interfaceMethod.getFpPos());
                        if (!interfaceMethod.getLocalSymbols().isEmpty()) {
                            Scope tempScope = new Scope(null);
                            for (Obj localVar : interfaceMethod.getLocalSymbols()) {
                                Obj newLocalVar = new Obj(localVar.getKind(), localVar.getName(), localVar.getType(),
                                        localVar.getAdr(), localVar.getLevel());
                                tempScope.addToLocals(newLocalVar);
                            }
                            newMethod.setLocals(tempScope.getLocals());
                        }
                        MyTable.currentScope.addToLocals(newMethod);
                        interfaceMethods.put(interfaceMethod.getName(), false);
                    }
                }
            }
        }

    }

    // endregion

    // region extends
    public void visit(ExtendsType extendsType) {
        if (extendsType.getType().struct.getKind() != Struct.Class) {
            report_error("Moze se izvoditi samo iz klasa", extendsType);
        } else {
            currentClass.setElementType(extendsType.getType().struct);
            // dodavanje nasledjenih polja
            for (Obj field : extendsType.getType().struct.getMembers()) {
                if (field.getKind() == Obj.Fld) {
                    Obj newField = new Obj(field.getKind(), field.getName(), field.getType(), field.getAdr(),
                            field.getLevel());
                    MyTable.currentScope.addToLocals(newField);
                }
            }
        }
    }
    // endregion

    // endregion

    // region deklaracija enuma
    // pocetak enuma
    public void visit(EnumStart enumStart) {
        if (MyTable.existsInCurrentScope(enumStart.getEnumName())) {
            report_error("Ime " + enumStart.getEnumName() + " vec postoji u trenutnom opsegu", enumStart);
        } else {
            report_info("Zapocet enum " + enumStart.getEnumName(), enumStart);
            enumStart.obj = MyTable.insert(Obj.Type, enumStart.getEnumName(), new Struct(Struct.Enum));
        }
        MyTable.openScope();
    }

    private LinkedList<Constant> enums = new LinkedList<>();

    public void visit(EnumName enumName) {
        Constant newEnum = new Constant(enumName.getEnumName(), enumName.getLine());
        if (enums.stream().filter(e -> e.getName().equals(newEnum.getName())).findFirst().orElse(null) != null) {
            report_error("Enum " + newEnum.getName() + " vec postoji", enumName);
        } else {
            Constant previous = enums.peekLast();
            if (previous == null) {
                newEnum.setValue(0);
            } else {
                newEnum.setValue(previous.getValue() + 1);
            }
            report_info("Dodat enum: " + newEnum.getName() + " sa vrednoscu: " + newEnum.getValue(), enumName);
            enums.addLast(newEnum);
        }
    }

    public void visit(EnumNameAssign enumNameAssign) {
        Constant newEnum = new Constant(enumNameAssign.getValue(), enumNameAssign.getEnumName(),
                enumNameAssign.getLine());
        if (enums.stream().filter(e -> e.getName().equals(newEnum.getName())).findFirst().orElse(null) != null) {
            report_error("Enum " + newEnum.getName() + " vec postoji", enumNameAssign);
        } else if (enums.stream().filter(e -> e.getValue() == newEnum.getValue()).findFirst().orElse(null) != null) {
            report_error("Vrednost " + newEnum.getValue() + " je vec dodeljena", enumNameAssign);
        } else {
            report_info("Dodat enum: " + newEnum.getName() + " sa vrednoscu: " + newEnum.getValue(), enumNameAssign);
            enums.addLast(newEnum);
        }
    }

    // kraj enuma
    public void visit(EnumDecl enumDecl) {
        for (Constant enumeration : enums) {
            MyTable.insertConstant(Obj.Con, enumeration.getName(), MyTable.intType, enumeration.getValue());
        }
        enums.clear();
        if (enumDecl.getEnumStart().obj != null) {
            MyTable.chainLocalSymbols(enumDecl.getEnumStart().obj.getType());
        }
        MyTable.closeScope();
    }

    // endregion

    // region deklaracije interfejsa

    // pocetak interfejsa
    public void visit(InterfaceStart interfaceStart) {
        if (MyTable.existsInCurrentScope(interfaceStart.getInterfaceName())) {
            report_error("Ime " + interfaceStart.getInterfaceName() + " vec postoji u trenutnom opsegu",
                    interfaceStart);
        } else {
            report_info("Zapocet interfejs " + interfaceStart.getInterfaceName(), interfaceStart);
            interfaceStart.obj = MyTable.insert(Obj.Type, interfaceStart.getInterfaceName(),
                    new Struct(Struct.Interface));
        }
        MyTable.openScope();
    }

    // kraj interfejsa
    public void visit(InterfaceDecl interfaceDecl) {
        if (interfaceDecl.getInterfaceStart().obj != null) {
            MyTable.chainLocalSymbols(interfaceDecl.getInterfaceStart().obj.getType());
        }
        MyTable.closeScope();
    }

    // pocetak metode interfejsa
    public void visit(InterfaceMethodStart interfaceMethodStart) {
        if (MyTable.existsInCurrentScope(interfaceMethodStart.getMethodName())) {
            report_error("Ime " + interfaceMethodStart.getMethodName() + " vec postoji u trenutnom opsegu",
                    interfaceMethodStart);
        } else {
            report_info("Zapoceta metoda interfejsa " + interfaceMethodStart.getMethodName(), interfaceMethodStart);
            interfaceMethodStart.obj = MyTable.insert(Obj.Meth, interfaceMethodStart.getMethodName(),
                    interfaceMethodStart.getReturnType().struct);
        }
        numberOfParameters = 0;
        MyTable.openScope();
    }

    // kraj metode interfejsa
    public void visit(InterfaceMethodDecl interfaceMethodDecl) {
        Obj method = interfaceMethodDecl.getInterfaceMethodStart().obj;
        if (method != null) {
            MyTable.chainLocalSymbols(method);
            method.setLevel(numberOfParameters);
        }
        numberOfParameters = 0;
        report_info("Zavrsena metoda " + interfaceMethodDecl.getInterfaceMethodStart().getMethodName(), null);
        MyTable.closeScope();
    }
    // endregion

    // region deklaracije metoda
    private Obj currentMethod = null;
    private int numberOfParameters = 0;
    private boolean isReturned = false;
    private boolean foundMain = false;
    private ArrayList<Obj> baseMethodVars;
    private boolean overrideError = false;

    // pocetak metode
    public void visit(MethodStart methodStart) {
        Obj baseMethod = MyTable.currentScope.findSymbol(methodStart.getMethodName());
        if (baseMethod != null && currentClass != null) {
            // proveri redefiniciju
            if (baseMethods.get(methodStart.getMethodName()) != null && baseMethods.get(methodStart.getMethodName())) {
                report_error("Inherited method " + methodStart.getMethodName() + "was already implemented",
                        methodStart);
            }
            if (interfaceMethods.get(methodStart.getMethodName()) != null
                    && interfaceMethods.get(methodStart.getMethodName())) {
                report_error("Inherited method " + methodStart.getMethodName() + "was already implemented",
                        methodStart);
            }
            if (!baseMethod.getType().equals(methodStart.getReturnType().struct)) {
                report_error("Prilikom redefinisanja metode" + methodStart.getMethodName()
                        + " povratni tip se sme da se menja", methodStart);
            } else {
                baseMethodVars = new ArrayList<>(baseMethod.getLocalSymbols());
                currentMethod = baseMethod;
                currentMethod.setLocals(null);
                methodStart.obj = currentMethod;
            }
        } else if (baseMethod != null) {
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
        baseMethods.replace(methodDecl.getMethodStart().getMethodName(), false, true);
        interfaceMethods.replace(methodDecl.getMethodStart().getMethodName(), false, true);
        isReturned = false;
        numberOfParameters = 0;
        currentMethod = null;
        baseMethodVars = null;
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
            if (baseMethodVars != null) {
                if (baseMethodVars.size() <= numberOfParameters) {
                    report_error("Broj parametara u redefinisanoj metodi je veci nego u originalnoj", formPar);
                    overrideError = true;
                    return;
                } else if (!baseMethodVars.get(numberOfParameters).getType().equals(variable.getStruct())) {
                    report_error(
                            numberOfParameters + ". parametar ne odgovara po tipu parametru metode iz osnovne klase",
                            formPar);
                    overrideError = true;
                    return;
                }
            }
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

    // kraj formalnih parametara
    public void visit(MethodFormParsEnd methodFormParsEnd) {
        if (currentMethod != null && baseMethodVars != null) {
            if (overrideError || currentMethod.getLevel() > 1) {
                if (currentMethod.getLevel() != numberOfParameters) {
                    report_error("Broj parametara u redefinisanoj metodi je manji nego u originalnoj",
                            methodFormParsEnd);
                }
                Scope tempScope = new Scope(null);
                for (Obj o : baseMethodVars) {
                    tempScope.addToLocals(o);
                }
                currentMethod.setLocals(tempScope.getLocals());
            }
        }
    }

    // kraj pomenljivih
    public void visit(MethodVarsEnd methodVarsEnd) {
        if (currentMethod != null) {
            MyTable.chainLocalSymbols(currentMethod);
            currentMethod.setLevel(numberOfParameters);
        }
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
    public void visit(DesignatorStatement designatorStatement) {
    }

    private int loop = 0;

    public void visit(StatementFor statementFor) {
        loop--;
    }

    public void visit(ForStart forStart) {
        loop++;
    }

    public void visit(StatementBreak statementBreak) {
        if (loop <= 0) {
            report_error("Break moze da se koristi samo unutar petlje", statementBreak);
        }
    }

    public void visit(StatementContinue statementContinue) {
        if (loop <= 0) {
            report_error("Continue moze da se koristi samo unutar petlje", statementContinue);
        }
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
            if (!MyTable.equivalent(currentMethod.getType(), statementReturnExpr.getExpr().struct)) {
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
        if (designatorAssign.getExpr() instanceof ExprERR) {
            report_error("OPORAVAK Greska kod dodele vrednosti", designatorAssign);
            return;
        }
        if (isLeftValue(designatorAssign.getDesignator())) {
            Struct left = designatorAssign.getDesignator().obj.getType();
            Struct right = designatorAssign.getExpr().struct;
            if (MyTable.assignable(left, right)) {
                report_info("Dodela", designatorAssign);
                // if (left.getKind() == Struct.Interface) {
                // left.setElementType(right);
                // }
                return;
            }
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
                report_error("Netacan broj argumenata za poziv " + method.getName(), designatorFunctionCall);
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
                    if (!MyTable.assignable(parameter, argument)) {
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
        if (!MyTable.compatible(struct1, struct2)) {
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
        Struct left = exprAddop.getExpr().struct;
        Struct right = exprAddop.getTerm().struct;

        int leftKind = left.getKind();
        int rightKind = right.getKind();

        if ((leftKind == Struct.Int && rightKind == Struct.Int) || (leftKind == Struct.Int && rightKind == Struct.Enum)
                || (leftKind == Struct.Enum && rightKind == Struct.Int)) {
            exprAddop.struct = MyTable.intType;
        } else {
            report_error("Operacija radi samo nad celim brojevima", exprAddop);
            exprAddop.struct = MyTable.noType;
        }
    }

    public void visit(ExprERR exprErr) {
        report_error("Greska u izrazu", exprErr.getParent());
        exprErr.struct = MyTable.noType;
    }

    // endregion

    // region term
    public void visit(TermFactor termFactor) {
        termFactor.struct = termFactor.getFactor().struct;
    }

    public void visit(TermMulop termMulop) {
        Struct left = termMulop.getFactor().struct;
        Struct right = termMulop.getTerm().struct;

        int leftKind = left.getKind();
        int rightKind = right.getKind();

        if ((leftKind == Struct.Int && rightKind == Struct.Int) || (leftKind == Struct.Int && rightKind == Struct.Enum)
                || (leftKind == Struct.Enum && rightKind == Struct.Int)) {
            termMulop.struct = MyTable.intType;

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
                    if (!MyTable.assignable(parameter, argument)) {
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
            if (struct.getKind() != Struct.Class && struct.getKind() != Struct.Interface
                    && struct.getKind() != Struct.Enum) {
                report_error("Moze se pristupati samo poljima klasnih tipova i enumeratora", designatorPointAccess);
                designatorPointAccess.obj = MyTable.noObj;
            } else {
                Obj elem;
                elem = struct.getMembers().stream().filter(e -> e.getName().equals(designatorPointAccess.getName()))
                        .findFirst().orElse(null);
                if (elem == null) {
                    report_error("Ne postoji ime " + designatorPointAccess.getName(), designatorPointAccess);
                    designatorPointAccess.obj = MyTable.noObj;
                } else {
                    if (struct.getKind() == Struct.Class || struct.getKind() == Struct.Interface) {
                        designatorPointAccess.obj = elem;
                    } else {
                        designatorPointAccess.obj = designatorPointAccess.getDesignator().obj;
                    }
                }
            }
        } else {
            designatorPointAccess.obj = MyTable.noObj;
        }
    }

    public void visit(DesignatorArrayAccess designatorArrayAccess) {
        Obj obj = designatorArrayAccess.getDesignator().obj;
        if (obj != MyTable.noObj) {
            String arrayName = obj.getName();
            if (obj.getType().getKind() != Struct.Array) {
                report_error("Promenljiva " + arrayName + " nije niz", designatorArrayAccess);
                designatorArrayAccess.obj = MyTable.noObj;
            } else if (designatorArrayAccess.getExpr().struct.getKind() != Struct.Int
                    && designatorArrayAccess.getExpr().struct.getKind() != Struct.Enum) {
                report_error("Za indeksiranje niza mora da se koristi int ili enum", designatorArrayAccess);
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
