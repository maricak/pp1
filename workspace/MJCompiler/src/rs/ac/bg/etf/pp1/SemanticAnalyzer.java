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

    public int nVars;

    private MyTableDumpVisitor tdv = new MyTableDumpVisitor();

    // region report
    Logger log = Logger.getLogger(getClass());
    boolean errorDetected = false;

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder("SEMANTICKA GRESKA!: [");
        msg.append(message).append("]");
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
        StringBuilder msg = new StringBuilder("DETEKCIJA: [");
        msg.append(message).append("]");
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.info(msg.toString());
    }

    public void report_info(String message, int line) {
        StringBuilder msg = new StringBuilder("DETEKCIJA: [");
        msg.append(message).append("]");
        msg.append(" na liniji ").append(line);
        log.info(msg.toString());
    }

    // endregion

    // region program
    // pocetak programa
    public void visit(ProgramStart programStart) {
        // novi Obj cvor za program
        programStart.obj = MyTable.insert(Obj.Prog, programStart.getProgramName(), MyTable.noType);
        // otvaranje novog opsega
        MyTable.openScope();
    }

    // kraj programa
    public void visit(Program program) {
        nVars = MyTable.currentScope.getLocals().symbols().size();
        // svi simboli su lokalni za obj Program
        MyTable.chainLocalSymbols(program.getProgramStart().obj);
        MyTable.closeScope();
        if (!foundMain)
            report_error("Nije deklarisana funkcija main", null);
    }
    // endregion

    // region deklaracije konstanti
    // konstante istog tipa koje su deklarisane zajedno
    private LinkedList<Constant> constants = new LinkedList<>();

    // kraj deklaracije konstanti
    public void visit(ConstantDecl constantDecl) {
        for (Constant constant : constants) {
            // da li ime postoji u opsegu
            if (MyTable.existsInCurrentScope(constant.getName())) {
                report_error("Ime " + constant.getName() + " vec postoji u trenutnom opsegu", constantDecl);
                // da li dodeljena vrednost odgovara tipu
            } else if (!MyTable.equivalent(constant.getObj().getType(), constantDecl.getType().struct)) {
                report_error("Neslaganje u tipu konstante i tipu dodeljene vrednosti", constant.getLine());
            } else {
                // novu konstantu dodati u trenutni opseg
                report_info("Deklarisana konstanta " + constant.getName() + "=" + constant.getValue(), constantDecl);
                MyTable.insertConstant(Obj.Con, constant.getName(), constant.getObj().getType(), constant.getValue());
            }
        }
        constants.clear();
    }

    // nova konstanta
    public void visit(ConstantAssign constantAssign) {
        constants.add(new Constant(constantAssign.getConstValue().constant.getObj(),
                constantAssign.getConstValue().constant.getValue(), constantAssign.getConstName(),
                constantAssign.getLine()));
    }

    // int vrednost konstante
    public void visit(ConstValueInt constValueInt) {
        constValueInt.constant = new Constant(MyTable.find("int"), constValueInt.getValue(), constValueInt.getLine());
    }

    // bool vrednost konstante
    public void visit(ConstValueBool constValueBool) {
        constValueBool.constant = new Constant(MyTable.find("bool"), constValueBool.getValue(),
                constValueBool.getLine());
    }

    // char vrednost konstate
    public void visit(ConstValueChar constValueChar) {
        constValueChar.constant = new Constant(MyTable.find("char"), constValueChar.getValue(),
                constValueChar.getLine());
    }

    // endregion

    // region deklaracije promenljivih
    // lista promenljivih istog tipa koje s deklarisane zajedno
    private LinkedList<Variable> variables = new LinkedList<>();

    // kraj jedne deklaracije
    public void visit(VariableDecl variableDecl) {
        for (Variable variable : variables) {
            variable.setStruct(variableDecl.getType().struct);
            // da li ime vec postoji u trenutnom opsegu
            if (MyTable.existsInCurrentScope(variable.getName())) {
                report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
            } else {
                // ako je promenljiva niz postaviti struct tipa array
                if (variable.isArray()) {
                    if (arrayTypes.get(variable.getStruct()) == null) {
                        arrayTypes.put(variable.getStruct(), new Struct(Struct.Array, variable.getStruct()));
                    }
                    variable.setStruct(arrayTypes.get(variable.getStruct()));
                }
                // promenljive su ustvari polja klase
                if (currentClass != null && currentMethod == null) {
                    report_info("Deklarisano polje klase " + variable.getName(), variable.getLine());
                    MyTable.insert(Obj.Fld, variable.getName(), variable.getStruct());
                } else {
                    if (currentMethod != null) {
                        // lokalna promenljiva
                        report_info("Deklarisana lokalna promenljiva " + variable.getName(), variable.getLine());
                    } else {
                        // globalna proenljiva
                        report_info("Deklarisana globalna promenljiva " + variable.getName(), variable.getLine());
                    }
                    MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
                }
            }
        }
        variables.clear();
    }

    // dodavanje deklarisane promenljive u niz
    public void visit(VariableList variableList) {
        if (variableList.getVarName().variable != null) {
            variables.add(new Variable(variableList.getVarName().variable));
        }
    }

    // dodavanje deklarisane promenljive u niz
    public void visit(VariableListEnd variableListEnd) {
        if (variableListEnd.getVarName().variable != null) {
            variables.add(new Variable(variableListEnd.getVarName().variable));
        }
    }

    // deklaracija jedne promenljive
    public void visit(VariableName variableName) {
        variableName.variable = new Variable(variableName.getVarName(),
                variableName.getOptionalBrackets() instanceof Brackets, variableName.getLine());
    }

    // endregion

    // region deklaracija klase

    // klasa koja se trenutno deklarise
    private Struct currentClass = null;
    // spisak metoda nasledjenih iz osnovne klase (ime metode -> da li je
    // impementirana)
    private HashMap<String, Boolean> baseMethods = new HashMap<>();
    // spisak metoda nasledjenih iz interfejsa (ime metode -> da li je
    // impementirana)
    private HashMap<String, Boolean> interfaceMethods = new HashMap<>();

    // pocetak klase
    public void visit(ClassStart classStart) {
        // da li ime vec postoji
        if (MyTable.existsInCurrentScope(classStart.getClassName())) {
            report_error("Ime klase " + classStart.getClassName() + " vec postoji u trenutnom opsegu", classStart);
            currentClass = new Struct(Struct.None);
        } else {
            // nova klasa, pravi se obj cvor
            // report_info("Zapoceta klasa " + classStart.getClassName(), classStart);
            currentClass = new Struct(Struct.Class);
            MyTable.insert(Obj.Type, classStart.getClassName(), currentClass);
            classStart.struct = currentClass;
        }
        // otvaranje novog opsega za klasu
        MyTable.openScope();
        MyTable.insert(Obj.Fld, "vtp", MyTable.intType);
    }

    // extends
    public void visit(ExtendsType extendsType) {
        Struct baseClass = extendsType.getType().struct;
        // osnovni tip mora biti klasa
        if (baseClass.getKind() != Struct.Class) {
            report_error("Moze se izvoditi samo iz klasa", extendsType);
        } else {
            // postavljanje pokazivaca na tip snovne klase
            currentClass.setElementType(extendsType.getType().struct);
            // dodavanje nasledjenih polja - izvedena klasa ima sva polja kao i osnovna
            for (Obj field : extendsType.getType().struct.getMembers()) {
                if (field.getKind() == Obj.Fld) {
                    Obj newField = new Obj(field.getKind(), field.getName(), field.getType(), field.getAdr(),
                            field.getLevel());
                    // dodavanje u opseg klase
                    MyTable.currentScope.addToLocals(newField);
                }
            }

            // interfejsi osnovne klase su interfejsi i izvedene klase
            if(baseClass.getImplementedInterfaces() != null) {
                baseClass.getImplementedInterfaces().forEach(i -> currentClass.addImplementedInterface(i));
            }
        }
    }

    // implements dodavanje jednog interfejsa
    public void visit(Interfaces interfaces) {
        Struct type = interfaces.getType().struct;
        // moze se implementirati samo interfejs
        if (type.getKind() != Struct.Interface) {
            report_error("Mogu se implementirati samo interfejsi", interfaces);
        } else if (currentClass != null) {
            // provera da li je ime interfejsa u listi jednistveno
            if (currentClass.getImplementedInterfaces().stream().filter(s -> s == type).findFirst()
                    .orElse(null) != null) {
                report_error("Interfejsi u listi moraju biti jedinstveni", interfaces);
            } else {
                // dodavanje interfejsa u struct cvor klase
                currentClass.addImplementedInterface(type);
            }
        }
    }

    // implements dodavanje jednog interfejsa
    public void visit(Interface interfacee) {
        Struct type = interfacee.getType().struct;
        // moze se implementirati samo interfejs
        if (type.getKind() != Struct.Interface) {
            report_error("Mogu se implementirati samo interfejsi", interfacee);
        } else if (currentClass != null) {
            // provera da li je ime interfejsa u listi jednistveno
            if (currentClass.getImplementedInterfaces().stream().filter(s -> s == type).findFirst()
                    .orElse(null) != null) {
                report_error("Interfejsi u listi moraju biti jedinstveni", interfacee);
            } else {
                // dodavanje interfejsa u struct cvor klase
                currentClass.addImplementedInterface(type);
            }
        }
    }

    // kraj deklaracjie polja
    public void visit(ClassVarDeclList classVarDeclList) {
        if (currentClass == null) {
            return;
        }
        // polja klase se vezuju u struct cvor klase
        MyTable.chainLocalSymbols(currentClass);
        // dodavanje metoda osnovne klase
        if (currentClass.getElemType() != null) {
            for (Obj field : currentClass.getElemType().getMembers()) {
                if (field.getKind() == Obj.Meth) {
                    // nova nasledjena metoda koja nije implementirana
                    baseMethods.put(field.getName(), false);
                    Obj newMethod = new Obj(field.getKind(), field.getName(), field.getType(), field.getAdr(),
                            field.getLevel());
                    newMethod.setFpPos(field.getFpPos());
                    // kopiranje parametara metode
                    if (!field.getLocalSymbols().isEmpty()) {
                        Scope tempScope = new Scope(null);
                        for (Obj localVar : field.getLocalSymbols()) {
                            Obj newLocalVar;
                            // this parametar se menja
                            if (localVar.getName() != "this") {
                                newLocalVar = new Obj(localVar.getKind(), localVar.getName(), localVar.getType(),
                                        localVar.getAdr(), localVar.getLevel());
                            } else {
                                newLocalVar = new Obj(localVar.getKind(), localVar.getName(), currentClass,
                                        localVar.getAdr(), localVar.getLevel());
                            }
                            tempScope.addToLocals(newLocalVar);
                        }
                        // parametri se dodaju u opseg metode
                        newMethod.setLocals(tempScope.getLocals());
                    }
                    // metoda se dodaje u opseg klase
                    MyTable.currentScope.addToLocals(newMethod);
                }
            }
        }
        // dodavanje metoda iz interfejsa
        if (currentClass.getImplementedInterfaces() != null && !currentClass.getImplementedInterfaces().isEmpty()) {
            for (Struct interfacee : currentClass.getImplementedInterfaces()) {
                // dodavanje nasledjenih metoda
                for (Obj interfaceMethod : interfacee.getMembers()) {
                    // da li postoji metoda istog imena od drugog interfejsa ili osnovne klase
                    Obj other = MyTable.currentScope.findSymbol(interfaceMethod.getName());
                    if (other != null) {
                        // parametri poronadjene metode
                        LinkedList<Obj> otherLocalsList = new LinkedList<>(other.getLocalSymbols());
                        // ako je metoda naslejdena iz osnovne klase ukloniti this
                        if (otherLocalsList.peekFirst() != null
                                && otherLocalsList.peekFirst().getName().equals("this")) {
                            otherLocalsList.removeFirst();
                        }
                        // ako postoji proveriti da li je povratna vrednost ista
                        if (!other.getType().equals(interfaceMethod.getType())) {
                            report_error("Nasledjene metode istog imena (" + interfaceMethod.getName()
                                    + ") moraju imati istu povratnu vrednost", classVarDeclList);
                        }
                        // prveriti da li je broj parametara isti
                        if (interfaceMethod.getLevel() != otherLocalsList.size()) {
                            report_error("Nasledjene metode istog imena " + interfaceMethod.getName()
                                    + " imaju razlicit broj parametara", classVarDeclList);
                        } else if (interfaceMethod.getLevel() != 0) {
                            // proveriti da li su parametri istog tipa
                            Obj[] locals = (Obj[]) interfaceMethod.getLocalSymbols().toArray();
                            Obj[] otherLocals = (Obj[]) otherLocalsList.toArray();
                            for (int i = 0; i < locals.length; i++) {
                                if (!locals[i].getType().equals(otherLocals[i].getType())) {
                                    report_error((i + 1) + ". parametar metode " + interfaceMethod.getName()
                                            + " nije isti u svim nasledjenim metodama tog", classVarDeclList);
                                }
                            }
                        }
                    } else {
                        // ne postoji metoda drugog interfejsa ili osnovne klase
                        // kopiramo metodu iz trenutnog interfejsa
                        Obj newMethod = new Obj(interfaceMethod.getKind(), interfaceMethod.getName(),
                                interfaceMethod.getType(), interfaceMethod.getAdr(), interfaceMethod.getLevel());
                        newMethod.setFpPos(interfaceMethod.getFpPos());
                        // kopiramo i sve parametre
                        if (!interfaceMethod.getLocalSymbols().isEmpty()) {
                            Scope tempScope = new Scope(null);
                            for (Obj localVar : interfaceMethod.getLocalSymbols()) {
                                Obj newLocalVar = new Obj(localVar.getKind(), localVar.getName(), localVar.getType(),
                                        localVar.getAdr(), localVar.getLevel());
                                tempScope.addToLocals(newLocalVar);
                            }
                            // parametri metode
                            newMethod.setLocals(tempScope.getLocals());
                        }
                        // nova metoda je dodata u metode klase
                        MyTable.currentScope.addToLocals(newMethod);
                        // nova metoda nasledjena od interfejsa i jos uvek nije implementirana
                        interfaceMethods.put(interfaceMethod.getName(), false);
                    }
                }
            }
        }
    }

    // kraj klase
    public void visit(ClassDecl classDecl) {

        // provera da li su svi interfejsi implementirani
        interfaceMethods.forEach((k, v) -> {
            if (v == false) {
                report_error("Klasa " + classDecl.getClassStart().getClassName() + " nije implementirala metodu " + k
                        + " nasledjenu od interfejsa", classDecl);
                // izbaciti iz opsega
                MyTable.currentScope.getLocals().deleteKey(k);
            }
        });

        // resetujemo spisak metoda
        baseMethods.clear();
        interfaceMethods.clear();

        // zatvaramo opseg za klasu
        currentClass = null;
        MyTable.closeScope();
    }
    // endregion

    // region deklaracija enuma

    // sve konstante enumeratora koji se trenutno deklarise
    private LinkedList<Constant> enums = new LinkedList<>();

    // pocetak enuma
    public void visit(EnumStart enumStart) {
        // da li ime vec postoji
        if (MyTable.existsInCurrentScope(enumStart.getEnumName())) {
            report_error("Ime " + enumStart.getEnumName() + " vec postoji u trenutnom opsegu", enumStart);
        } else {
            // report_info("Zapocet enum " + enumStart.getEnumName(), enumStart);
            // kreianje obj cvora za enum
            enumStart.obj = MyTable.insert(Obj.Type, enumStart.getEnumName(), new Struct(Struct.Enum));
        }
        // otvaranje novog opsega
        MyTable.openScope();
    }

    // deklaracija enum konstante bez eksplicitne vresnosti
    public void visit(EnumName enumName) {
        // nova konstanta
        Constant newEnum = new Constant(enumName.getEnumName(), enumName.getLine());
        // provera da li je imejedinstveno
        if (enums.stream().filter(e -> e.getName().equals(newEnum.getName())).findFirst().orElse(null) != null) {
            report_error("Enum " + newEnum.getName() + " vec postoji", enumName);
        } else {
            // postaviti vrednost
            Constant previous = enums.peekLast();
            if (previous == null) {
                // 0 ako je prva kosntanta
                newEnum.setValue(0);
            } else {
                // za jedan vecu od prethodne konstatne
                newEnum.setValue(previous.getValue() + 1);
            }
            // report_info("Dodat enum: " + newEnum.getName() + " sa vrednoscu: " +
            // newEnum.getValue(), enumName);
            // dodavanje u listu enuma
            enums.addLast(newEnum);
        }
    }

    // deklaracija enum konstatne sa eksplicitnom vrednoscu
    public void visit(EnumNameAssign enumNameAssign) {
        // nova konstanta
        Constant newEnum = new Constant(enumNameAssign.getValue(), enumNameAssign.getEnumName(),
                enumNameAssign.getLine());
        // provera da li je ime jedinstveno
        if (enums.stream().filter(e -> e.getName().equals(newEnum.getName())).findFirst().orElse(null) != null) {
            report_error("Enum " + newEnum.getName() + " vec postoji", enumNameAssign);
            // provera da li je vrednost jedinstvema
        } else if (enums.stream().filter(e -> e.getValue() == newEnum.getValue()).findFirst().orElse(null) != null) {
            report_error("Vrednost " + newEnum.getValue() + " je vec dodeljena", enumNameAssign);
        } else {
            // report_info("Dodat enum: " + newEnum.getName() + " sa vrednoscu: " +
            // newEnum.getValue(), enumNameAssign);
            // dodavanje u listu enuma
            enums.addLast(newEnum);
        }
    }

    // kraj enuma
    public void visit(EnumDecl enumDecl) {
        // sve deklarisane konstatne ubaciti u opseg enuma
        for (Constant enumeration : enums) {
            MyTable.insertConstant(Obj.Con, enumeration.getName(), MyTable.intType, enumeration.getValue());
        }
        enums.clear();
        // ulancavanje konstanti
        if (enumDecl.getEnumStart().obj != null) {
            MyTable.chainLocalSymbols(enumDecl.getEnumStart().obj.getType());
        }
        // zatvaranje opsega
        MyTable.closeScope();
    }

    // endregion

    // region deklaracije interfejsa

    // pocetak interfejsa
    public void visit(InterfaceStart interfaceStart) {
        // provera da li je ime vec deklarisano
        if (MyTable.existsInCurrentScope(interfaceStart.getInterfaceName())) {
            report_error("Ime " + interfaceStart.getInterfaceName() + " vec postoji u trenutnom opsegu",
                    interfaceStart);
        } else {
            // report_info("Zapocet interfejs " + interfaceStart.getInterfaceName(),
            // interfaceStart);
            // kreiranje obj cvora za interfejs
            interfaceStart.obj = MyTable.insert(Obj.Type, interfaceStart.getInterfaceName(),
                    new Struct(Struct.Interface));
        }
        // otvaranje opsega za interfejs
        MyTable.openScope();
    }

    // pocetak metode interfejsa
    public void visit(InterfaceMethodStart interfaceMethodStart) {
        // provera da li je ime metode jedinstveno u interfejsu
        if (MyTable.existsInCurrentScope(interfaceMethodStart.getMethodName())) {
            report_error("Ime " + interfaceMethodStart.getMethodName() + " vec postoji u opsegu interfejsa",
                    interfaceMethodStart);
        } else {
            // report_info("Zapoceta metoda interfejsa " +
            // interfaceMethodStart.getMethodName(), interfaceMethodStart);
            // novi ob cvor za metodu
            interfaceMethodStart.obj = MyTable.insert(Obj.Meth, interfaceMethodStart.getMethodName(),
                    interfaceMethodStart.getReturnType().struct);
        }
        numberOfParameters = 0;
        // otvaranje opsega za metodu
        MyTable.openScope();
    }

    // kraj metode interfejsa
    public void visit(InterfaceMethodDecl interfaceMethodDecl) {
        Obj method = interfaceMethodDecl.getInterfaceMethodStart().obj;
        if (method != null) {
            // uvezivanje parametara metode i postavljanje broja parametara
            MyTable.chainLocalSymbols(method);
            method.setLevel(numberOfParameters);
        }
        numberOfParameters = 0;
        // report_info("Zavrsena metoda " +
        // interfaceMethodDecl.getInterfaceMethodStart().getMethodName(), null);
        // zatvaranje opsega za metodu interfejsa
        MyTable.closeScope();
    }

    // kraj interfejsa
    public void visit(InterfaceDecl interfaceDecl) {
        if (interfaceDecl.getInterfaceStart().obj != null) {
            // uvezivanje metoda interfejsa
            MyTable.chainLocalSymbols(interfaceDecl.getInterfaceStart().obj.getType());
        }
        // zatvaranje opsega interfejsa
        MyTable.closeScope();
    }

    // endregion

    // region deklaracije metoda

    // metoda koja se trenutno deklarise
    private Obj currentMethod = null;
    // trenutni broj paramentara metode
    private int numberOfParameters = 0;
    // da li postoji return naredba
    private boolean isReturned = false;
    // da li je pronadjena main metoda
    private boolean foundMain = false;
    // parametri metode iz osnovne klase koja se redefinise
    private ArrayList<Obj> baseMethodVars;
    // broj parametara metode koja se redefinise
    private int baseNumberOfParameters = 0;
    // da li se dogodila greska prilikom redefinicije
    // private boolean overrideError = false;
    // da li je metoda nasledjena iz interfejsa
    private int interfaceMethod = 0;

    // pocetak metode
    public void visit(MethodStart methodStart) {
        Obj baseMethod = MyTable.currentScope.findSymbol(methodStart.getMethodName());
        if (baseMethod != null && currentClass != null) {
            // ova metoda redefinise neku metodu klase
            // da li se redefinise metoda iz osnovne klase
            if (baseMethods.get(methodStart.getMethodName()) != null) {
                // provera da li je metoda vec redefinisana
                if (baseMethods.get(methodStart.getMethodName())) {
                    report_error("Nasledjena metoda " + methodStart.getMethodName() + "je vec implementirana",
                            methodStart);
                }
                // metoda ne redefinise metod iz interfejsa
                interfaceMethod = 0;
            }
            // da li redefinise metodu iz nekog interfejsa
            if (interfaceMethods.get(methodStart.getMethodName()) != null) {
                // provera da li je metoda vec redefinisana
                if (interfaceMethods.get(methodStart.getMethodName())) {
                    report_error("Nasledjena metoda " + methodStart.getMethodName() + "je vec implementirana",
                            methodStart);
                }
                // metoda redefinise metodu iz interfejsa
                interfaceMethod = 1;
            }
            // provera da li redefinicija metode ima odgovarajuci povratni tip
            if (!baseMethod.getType().equals(methodStart.getReturnType().struct)) {
                report_error("Prilikom redefinisanja metode " + methodStart.getMethodName()
                        + " povratni tip se sme da se menja", methodStart);
            } else {
                // promenljive metode koja se redefinise
                baseMethodVars = new ArrayList<>(baseMethod.getLocalSymbols());
                baseNumberOfParameters = baseMethod.getLevel();
                // trenutna metoda za sada nema nijedan parametar
                currentMethod = baseMethod;
                currentMethod.setLocals(null);
                methodStart.obj = currentMethod;
            }
            // ime vec postoji u opsegu a ne radi se o redefiniciji
        } else if (baseMethod != null) {
            report_error("Ime " + methodStart.getMethodName() + " vec postoji u trenutnom opsegu", methodStart);
            currentMethod = new Obj(Obj.Meth, methodStart.getMethodName(), MyTable.noType);
        } else {
            // report_info("Zapoceta metoda " + methodStart.getMethodName(), methodStart);
            // pravi se novi obj cvor za metodu
            currentMethod = MyTable.insert(Obj.Meth, methodStart.getMethodName(), methodStart.getReturnType().struct);
            methodStart.obj = currentMethod;
        }
        // resetovanje
        numberOfParameters = 0;
        isReturned = false;

        // novi opseg za metodu
        MyTable.openScope();

        if (currentClass != null) {
            // ako je metoda unutar klase dodati implicitan parametar this
            Obj t = MyTable.insert(Obj.Var, "this", currentClass);
            t.setFpPos(1);
            numberOfParameters++;
        }
    }

    // kraj metode
    public void visit(MethodDecl methodDecl) {
        if (currentMethod != null) {
            // provera da li je pronadjena return naredba
            if (!isReturned && currentMethod.getType() != MyTable.noType) {
                report_error("Metoda " + methodDecl.getMethodStart().getMethodName() + " nema return naredbu", null);
            }
            // provera da li je pronadjena min metoda
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
        // postaviti true ako je metoda uspesna redefinicija
        baseMethods.replace(methodDecl.getMethodStart().getMethodName(), false, true);
        interfaceMethods.replace(methodDecl.getMethodStart().getMethodName(), false, true);

        // resetovanje
        isReturned = false;
        numberOfParameters = 0;
        baseNumberOfParameters = 0;
        currentMethod = null;
        baseMethodVars = null;
        // overrideError = false;
        // report_info("Zavrsena metoda " + methodDecl.getMethodStart().getMethodName(),
        // null);

        // zatvaranje opsga za metodu
        MyTable.closeScope();
    }

    // formalni parametar metode
    public void visit(FormPar formPar) {
        Variable variable = formPar.getVarName().variable;
        // postoji greska u parametru
        if (variable == null) {
            // report_error("OPORAVAK Greska u formalnim parametrima", formalPar);
            return;
        }
        variable.setStruct(formPar.getType().struct);
        // ako je parametar tipa niz postaviti struct tipa Array
        if (variable.isArray()) {
            if (arrayTypes.get(variable.getStruct()) == null) {
                arrayTypes.put(variable.getStruct(), new Struct(Struct.Array, variable.getStruct()));
            }
            variable.setStruct(arrayTypes.get(variable.getStruct()));
        }
        // ako je parametar tipe Enum postaviti mu struct tipa Int
        // if (variable.getStruct().getKind() == Struct.Enum) {
        // variable.setStruct(MyTable.intType);
        // }
        // provera da li je ime parametra jedinstveno
        if (MyTable.existsInCurrentScope(variable.getName())) {
            report_error("Ime " + variable.getName() + " vec postoji u trenutnom opsegu", variable.getLine());
        } else {
            if (baseMethodVars != null) {
                // u toku je redefinicija metode
                // provera da li je broj redefinicije veci nego u originalnoj metodi
                // interfaceMethod se koristi da se izbegne this parametar -- interfejs metode
                // ga nemaju
                if (baseNumberOfParameters + interfaceMethod <= numberOfParameters) {
                    report_error("Broj parametara u redefinisanoj metodi " + currentMethod.getName()
                            + " je veci nego u originalnoj", formPar);
                    // dogodila se greska
                    // overrideError = true;
                    return;
                    // proverit da li tip parametra u redefiniciji odgovara tipu parametra u
                    // originalnoj metodi
                } else if (!MyTable.equivalent(baseMethodVars.get(numberOfParameters - interfaceMethod).getType(),
                        variable.getStruct())) {
                    report_error(
                            numberOfParameters + ". parametar ne odgovara po tipu parametru metode iz osnovne klase",
                            formPar);
                    // dogodila se greska
                    // overrideError = true;
                    return;
                }
            }
            // obj cvor za parametar
            Obj newFormalParameter = MyTable.insert(Obj.Var, variable.getName(), variable.getStruct());
            // report_info("Formalni parametar " + variable.getName(), variable.getLine());

            // uvecava se broj parametara
            numberOfParameters++;
            // postavljanje pozicije parametra unutar metode
            newFormalParameter.setFpPos(numberOfParameters);
        }
    }

    // kraj formalnih parametara
    public void visit(MethodFormParsEnd methodFormParsEnd) {
        if (currentMethod != null && baseMethodVars != null) {
            // ako je kraj redefinicije proveriti broj parametara osnovne i izvenden klase
            // interfaceMethod se koristi da se zaobidje this parametar ako treba
            if (baseNumberOfParameters + interfaceMethod != numberOfParameters) {
                report_error("Broj parametara u redefinisanoj metodi je manji nego u originalnoj", methodFormParsEnd);
                // dogodila se greska
                // overrideError = true;
            }
            // ako se dogodila greska vratiti parametre iz originalne metode radi manje
            // greska u nastavku
            // if (overrideError) {
            // Scope tempScope = new Scope(null);
            // for (Obj o : baseMethodVars) {
            // tempScope.addToLocals(o);
            // }
            // currentMethod.setLocals(tempScope.getLocals());
            // }
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

    // chr
    public void visit(StandardFunctionChr standardFunctionChr) {
        Struct struct = standardFunctionChr.getExpr().struct;
        // argument mora biti tipa int
        if (struct.getKind() != Struct.Int) {
            report_error("Argument mora biti tipa int", standardFunctionChr);
        } else {
            tdv.visitObjNode(MyTable.find("chr"));
            report_info("Poziv funkcije chr" + tdv.getOutput(), standardFunctionChr);
        }
        standardFunctionChr.struct = MyTable.find("chr").getType();
    }

    // ord
    public void visit(StandardFunctionOrd standardFunctionOrd) {
        Struct struct = standardFunctionOrd.getExpr().struct;
        // argument mora biti tipa char
        if (struct.getKind() != Struct.Char) {
            report_error("Argument mora biti tipa char", standardFunctionOrd);
        } else {
            tdv.visitObjNode(MyTable.find("ord"));
            report_info("Poziv funkcije ord" + tdv.getOutput(), standardFunctionOrd);
        }
        standardFunctionOrd.struct = MyTable.find("ord").getType();
    }

    // len
    public void visit(StandardFunctionLen standardFunctionLen) {
        Struct struct = standardFunctionLen.getExpr().struct;
        // argument mora biti niz
        if (struct.getKind() != Struct.Array) {
            report_error("Argument mora biti niz", standardFunctionLen);
        } else {
            tdv.visitObjNode(MyTable.find("len"));
            report_info("Poziv funkcije len" + tdv.getOutput(), standardFunctionLen);
        }
        standardFunctionLen.struct = MyTable.find("len").getType();
    }
    // endregion

    // region tipovi

    // tip int
    public void visit(TypeInt typeInt) {
        typeInt.struct = MyTable.intType;
    }

    // tip boolean
    public void visit(TypeBool typeBool) {
        typeBool.struct = MyTable.boolType;
    }

    // char
    public void visit(TypeChar typeChar) {
        typeChar.struct = MyTable.charType;
    }

    // korisnicki tip
    public void visit(TypeCustom typeCustom) {
        Obj typeNode = MyTable.find(typeCustom.getTypeName());
        // provera da li je ime tipa deklarisano
        if (typeNode == MyTable.noObj) {
            report_error("Nije pronadjen tip " + typeCustom.getTypeName() + " u tabeli simbola", typeCustom);
            typeCustom.struct = MyTable.noType;
        } else {
            // provera da li je pronadjeno ime zaista ime tipa
            if (typeNode.getKind() == Obj.Type) {
                typeCustom.struct = typeNode.getType();
            } else {
                report_error("Ime " + typeCustom.getTypeName() + " ne predstavlja tip", typeCustom);
                typeCustom.struct = MyTable.noType;
            }
        }
    }
    // endregion

    // region statement

    public void visit(IfCondition ifCondition) {
        if (ifCondition.getCondition().struct != MyTable.boolType) {
            report_error("OPORAVAK greska u if uslovu", ifCondition);
        }
    }

    // broj trenutno zapocetih petlji
    private int loop = 0;

    // kraj for petlje
    public void visit(StatementFor statementFor) {
        // izlazak iz petlje
        loop--;
    }

    // pocetak for petlje
    public void visit(ForStart forStart) {
        // ulazak u petlju
        loop++;
    }

    // break naredba
    public void visit(StatementBreak statementBreak) {
        // provera da li je naredba unutar petlje
        if (loop <= 0) {
            report_error("Break moze da se koristi samo unutar petlje", statementBreak);
        }
    }

    // continue naredba
    public void visit(StatementContinue statementContinue) {
        // provera da li je naredba unutar petlje
        if (loop <= 0) {
            report_error("Continue moze da se koristi samo unutar petlje", statementContinue);
        }
    }

    // return naredba bez povratne vrednosti
    public void visit(StatementReturn statementReturn) {
        // provera da li je return naredba unutar tela metode/funckije
        if (currentMethod == null) {
            report_error("Return ne sme da postoji van funkcije ili metode", statementReturn);
        } else {
            // trenutna metoda/funckija ima return naredbu
            isReturned = true;
            // provera da li trenutna metoda zahteva povratnu vrednot u return naredbi
            if (currentMethod.getType().getKind() != Struct.None) {
                report_error("Void metoda " + currentMethod.getName() + " ne sme imati return", statementReturn);
            }
        }
    }

    // return naredba sa povratnom vrednoscu
    public void visit(StatementReturnExpr statementReturnExpr) {
        // provera da li je return naredba unutar tela metode/funkcije
        if (currentMethod == null) {
            report_error("Return ne sme da postoji van funkcije ili metode", statementReturnExpr);
        } else {
            isReturned = true;
            // provera da li povratna vrednost ofgovara povratnom tipu trenutne
            // metode/funckije
            if (!MyTable.assignable(currentMethod.getType(), statementReturnExpr.getExpr().struct)) {
                report_error("Povratni tip metode " + currentMethod.getName()
                        + " nije kompatibilan sa tipom povratne vrednosti", statementReturnExpr);
            }
        }
    }

    // read naredba
    public void visit(StatementRead statementRead) {
        // provera da li je argument promenljiva, element niza ili polje objekta
        if (isLeftValue(statementRead.getDesignator())) {
            Struct struct = statementRead.getDesignator().obj.getType();
            // provera da li je tip argumenta char, int ili bool
            if (struct.getKind() != Struct.Int && struct.getKind() != Struct.Char && struct.getKind() != Struct.Bool) {
                report_error("Argument mora biti tipa int, char ili bool", statementRead);
            } else {
                report_info("Poziv funkcije read", statementRead);
            }
        } else {
            report_error("Argument funkcije read mora biti promenljiva, polje klase ili element niza", statementRead);
        }
    }

    // print naredba
    public void visit(StatementPrint statementPrint) {
        Struct struct = statementPrint.getExpr().struct;
        // provera da li je prvi argument odgovarajuceg tipa
        if (struct.getKind() != Struct.Int && struct.getKind() != Struct.Char && struct.getKind() != Struct.Bool) {
            report_error("Argument funkcije print mora biti tipa int, char ili bool", statementPrint);
        } else {
            report_info("Poziv funkcije print", statementPrint);
        }
    }
    // endregion

    // region designator statement

    // dodela vrednosti
    public void visit(DesignatorAssign designatorAssign) {
        // da li se dogodila greska u izrazu sa desne strane
        if (designatorAssign.getExpr() instanceof ExprERR) {
            report_error("OPORAVAK Greska kod dodele vrednosti", designatorAssign);
            return;
        }
        // provera da li se levoj strani moze dodeliti vrednost
        if (isLeftValue(designatorAssign.getDesignator())) {
            Struct left = designatorAssign.getDesignator().obj.getType();
            Struct right = designatorAssign.getExpr().struct;
            // provera da li su tipovi leve i desne strane odgovarajuci
            if (MyTable.assignable(left, right)) {
                return;
            }
            report_error("Tipovi u dodeli nisu kompatiblini", designatorAssign);
        } else {
            report_error("Kod dodele vrednosti leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorAssign);
        }
    }

    // poziv funkcije
    public void visit(DesignatorFunctionCall designatorFunctionCall) {
        Obj method = designatorFunctionCall.getDesignator().obj;
        // argumenti funkcije/metode koja se poziva
        LinkedList<Struct> arguments = functionCallArguments.pop();
        // provera da li je ime metoda
        if (method.getKind() != Obj.Meth) {
            if (method != MyTable.noObj)
                report_error(method.getName() + " nije ime metode ni funkcije", designatorFunctionCall);
            return;
        } else {
            // parametri funkcije/metode koja se poziva
            LinkedList<Obj> parameters = new LinkedList<>(method.getLocalSymbols());
            int numberOfParameters = method.getLevel();
            // ukloniti this ako treba
            if (parameters.peekFirst() != null && parameters.peekFirst().getName().equals("this")) {
                parameters.removeFirst();
                numberOfParameters--;
            }
            // provera da li je broj argumenata i broj parametara isti
            if (arguments.size() != numberOfParameters) {
                report_error("Netacan broj argumenata za poziv " + method.getName(), designatorFunctionCall);
            } else {
                // provera da li se tipovi arumenata slazu sa tipovima parametara
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
                    tdv.visitObjNode(method);
                    if (designatorFunctionCall.getDesignator() instanceof DesignatorName) {
                        report_info("Poziv funkcije " + method.getName() + " " + tdv.getOutput(),
                                designatorFunctionCall);
                    } else {
                        report_info("Poziv metode " + method.getName() + " " + tdv.getOutput(), designatorFunctionCall);
                    }
                }
            }
        }
    }

    // inkrementiranje
    public void visit(DesignatorIncrement designatorIncrement) {
        // provera da li se operand moze inkrementirati
        if (isLeftValue(designatorIncrement.getDesignator())) {
            Struct struct = designatorIncrement.getDesignator().obj.getType();
            // provera da li je operand ceo broj
            if (struct.getKind() != Struct.Int) {
                report_error("Samo se ceo broj moze inkrementirati", designatorIncrement);
            }
        } else {
            report_error("Kod inkrementiranja leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorIncrement);
        }
    }

    // dekrementiranje
    public void visit(DesignatorDecrement designatorDecrement) {
        // provera da li operand moze da se dekrementira
        if (isLeftValue(designatorDecrement.getDesignator())) {
            Struct struct = designatorDecrement.getDesignator().obj.getType();
            // provera da li je operand ceo broj
            if (struct.getKind() != Struct.Int) {
                report_error("Samo se ceo broj moze dekrementirati", designatorDecrement);
            }
        } else {
            report_error("Kod dekrementiranja leva strana mora biti promenljiva, polje klase ili element niza",
                    designatorDecrement);
        }
    }
    // endregion

    // region act pars

    // stak poziva funckija. element steka je lista argumenta funkcije
    private Stack<LinkedList<Struct>> functionCallArguments = new Stack<>();

    // jedan argument
    public void visit(ActParameter actParameter) {
        // dodavanje argumenta u listu funckije na vrhu steka
        if (!functionCallArguments.empty()) {
            functionCallArguments.peek().add(actParameter.getExpr().struct);
        }
    }

    public void visit(ActParameters actParameters) {
        // dodavanje argumenta u listu funckije na vrhu steka
        if (!functionCallArguments.empty()) {
            functionCallArguments.peek().add(actParameters.getExpr().struct);
        }
    }
    // endregion

    // region condition
    public void visit(Condition condition) {
        // provera da li je izraz tipa bool
        if (condition.getCondTerm().struct != MyTable.boolType
                || condition.getCondTermList().struct != MyTable.boolType) {
            report_error("Uslov mora biti bool tipa", condition);
            condition.struct = MyTable.noType;
        } else {
            condition.struct = MyTable.boolType;
        }
    }
    // endregion

    // region condition term
    public void visit(ConditionTermList conditionTermList) {
        // provera da li je iyray tipa bool
        if (conditionTermList.getCondTermList().struct != MyTable.boolType) {
            conditionTermList.struct = MyTable.noType;
        } else if (conditionTermList.getCondTerm().struct != MyTable.boolType) {
            conditionTermList.struct = MyTable.noType;
        } else {
            conditionTermList.struct = MyTable.boolType;
        }
    }

    public void visit(ConditionTermListNO conditionTermListNO) {
        // radi izbegavanja gresaka
        conditionTermListNO.struct = MyTable.boolType;
    }

    public void visit(CondTerm condTerm) {
        // provera da li je izraz tipa bool
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
        // provera da li je izraz tipa bool
        if (conditionFactList.getCondFactList().struct != MyTable.boolType) {
            conditionFactList.struct = MyTable.noType;
        } else if (conditionFactList.getCondFact().struct != MyTable.boolType) {
            conditionFactList.struct = MyTable.noType;
        } else {
            conditionFactList.struct = MyTable.boolType;
        }
    }

    public void visit(ConditionFactListNO conditionFactListNO) {
        // radi izbegavanja gresaka
        conditionFactListNO.struct = MyTable.boolType;
    }

    public void visit(CondFactExpr condFactExpr) {
        // prosledjivanje tipa izraza uz stablo
        condFactExpr.struct = condFactExpr.getExpr().struct;
    }

    // compare
    public void visit(CondFactCompare condFactCompare) {
        Struct struct1 = condFactCompare.getExpr().struct;
        Struct struct2 = condFactCompare.getExpr1().struct;
        // prrovera da li su opera ndi odgovarajuceg tipa
        if (!MyTable.compatible(struct1, struct2)) {
            report_error("Operandi nisu kompatibilni", condFactCompare);
            // provera da li su korisnicki tipove porede na manje, vece
        } else if ((struct1.isRefType() || struct2.isRefType()) && !(condFactCompare.getRelop() instanceof Equal)
                && !(condFactCompare.getRelop() instanceof NotEqual)) {
            report_error("Nizovi i objekti se mogu porediti samo sa != i ==", condFactCompare);
        }
        condFactCompare.struct = MyTable.boolType;
    }

    // endregion

    // region expr

    public void visit(ExprTerm exprTerm) {
        // prosledjivanje tipa uz stablo
        exprTerm.struct = exprTerm.getTerm().struct;
    }

    public void visit(ExprNegativeTerm exprNegativeTerm) {
        // provera da li izraz moze da se negira
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

        // provera da li su operandi celi brojevi
        if ((left.getKind() == Struct.Int && right.getKind() == Struct.Int)
                || (left.getKind() == Struct.Int && right.getKind() == Struct.Enum)
                || (left.getKind() == Struct.Enum && right.getKind() == Struct.Int)
                || (left.getKind() == Struct.Enum && right.getKind() == Struct.Enum)) {
            exprAddop.struct = MyTable.intType;
        } else {
            report_error("Operacija radi samo nad celim brojevima", exprAddop);
            exprAddop.struct = MyTable.noType;
        }
    }

    public void visit(ExprERR exprErr) {
        // prosledjivanje tipa da ne bi ostao null
        report_error("Greska u izrazu", exprErr.getParent());
        exprErr.struct = MyTable.noType;
    }

    // endregion

    // region term
    public void visit(TermFactor termFactor) {
        // prosledjivanje tipa
        termFactor.struct = termFactor.getFactor().struct;
    }

    public void visit(TermMulop termMulop) {
        Struct left = termMulop.getFactor().struct;
        Struct right = termMulop.getTerm().struct;

        // provera da li su operandi celi brojevi
        if ((left.getKind() == Struct.Int && right.getKind() == Struct.Int)
                || (left.getKind() == Struct.Int && right.getKind() == Struct.Enum)
                || (left.getKind() == Struct.Enum && right.getKind() == Struct.Int)
                || (left.getKind() == Struct.Enum && right.getKind() == Struct.Enum)) {
            termMulop.struct = MyTable.intType;
        } else {
            report_error("Operacija radi samo nad celim brojevima", termMulop);
            termMulop.struct = MyTable.noType;
        }
    }
    // endregion

    // region factor
    public void visit(FactorDesignator factorDesignator) {
        // proslednjivanje tipa
        factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
    }

    public void visit(FactorStandardFunction factorStandardFunction) {
        // prosledjivanje tipa
        factorStandardFunction.struct = factorStandardFunction.getStandardFunction().struct;
    }

    // poziv funkcije
    public void visit(FactorFunctionCall factorFunctionCall) {
        Obj method = factorFunctionCall.getDesignator().obj;
        // argumenti za poziv funkcije/metode
        LinkedList<Struct> arguments = functionCallArguments.pop();
        // provera da li je ime metoda/funckija
        if (method.getKind() != Obj.Meth) {
            if (method != MyTable.noObj)
                report_error(method.getName() + " nije ime metode ni funkcije", factorFunctionCall);
            factorFunctionCall.struct = MyTable.noType;
            return;
        } else {
            factorFunctionCall.struct = method.getType();
            // parametri funkcije/metode
            LinkedList<Obj> parameters = new LinkedList<>(method.getLocalSymbols());
            int numberOfParameters = method.getLevel();
            // sklanjanje this ako treba
            if (parameters.peekFirst() != null && parameters.peekFirst().getName().equals("this")) {
                parameters.removeFirst();
                numberOfParameters--;
            }
            // provera da li je broj argumenata isti kao i broj parametara
            if (arguments.size() != numberOfParameters) {
                report_error("Netacan broj argumenata za poziv " + method.getName(), factorFunctionCall);
            } else {
                // provera da li tip svakog od argumenata odgovara tipu parametra
                boolean incompatible = false;
                for (int i = 0; i < numberOfParameters; i++) {
                    Struct argument = arguments.remove();
                    Struct parameter = parameters.remove().getType();
                    if (!MyTable.assignable(parameter, argument)) {
                        // greska
                        report_error((i + 1) + ". argument ne moze da se dodeli odgovarajucem parametru",
                                factorFunctionCall);
                        incompatible = true;
                    }
                }
                if (incompatible) {
                    tdv.visitObjNode(method);
                    if (factorFunctionCall.getDesignator() instanceof DesignatorName) {
                        report_info("Poziv funkcije " + tdv.getOutput(), factorFunctionCall);
                    } else {
                        report_info("Poziv metode " + tdv.getOutput(), factorFunctionCall);
                    }
                }
            }
        }
    }

    public void visit(FactorConst factorConst) {
        // prosledjivanje tipa
        factorConst.struct = factorConst.getConstValue().constant.getObj().getType();
    }

    public void visit(FactorNewObj factorNewObj) {
        // privera da li se kreira klasni tip
        if (factorNewObj.getType().struct.getKind() != Struct.Class) {
            report_error("Sa new moze da se napravi samo objekat klasnog tipa", factorNewObj);
            factorNewObj.struct = MyTable.noType;
        } else {
            // prosledjivanje tipa
            factorNewObj.struct = factorNewObj.getType().struct;
            tdv.visitObjNode(MyTable.find(((TypeCustom) factorNewObj.getType()).getTypeName()));
            report_info("Pravljenje objekta tipa " + ((TypeCustom) factorNewObj.getType()).getTypeName() + " "
                    + tdv.getOutput(), factorNewObj);
        }
    }

    // stuct objekti za tipove Array
    private HashMap<Struct, Struct> arrayTypes = new HashMap<>();

    public void visit(FactorNewArray factorNewArray) {
        // provera da li je izraz za velicinu niza tipa int ili enum
        if (factorNewArray.getExpr().struct != MyTable.intType
                && factorNewArray.getExpr().struct.getKind() != Struct.Enum) {
            report_error("Velicina niza mora biti int ili enum", factorNewArray);
            factorNewArray.struct = MyTable.noType;
        } else {
            if (arrayTypes.get(factorNewArray.getType().struct) == null) {
                arrayTypes.put(factorNewArray.getType().struct,
                        new Struct(Struct.Array, factorNewArray.getType().struct));
            }
            // prosledjivanje tipa
            factorNewArray.struct = arrayTypes.get(factorNewArray.getType().struct);
        }
    }

    public void visit(FactorNull factorNull) {
        // prosledjivanje tipa
        factorNull.struct = MyTable.nullType;
    }

    public void visit(FactorExpression factorExpression) {
        // prosledjivanje tipa
        factorExpression.struct = factorExpression.getExpr().struct;
    }
    // endregion

    // region designator
    public void visit(DesignatorName designatorName) {
        // provera da li zapocinje poziv funkcije
        if (designatorName.getParent() instanceof FactorFunctionCall
                || designatorName.getParent() instanceof DesignatorFunctionCall) {
            functionCallArguments.push(new LinkedList<>());
        }
        // provera da li je ime postoji
        Obj obj = MyTable.find(designatorName.getName());
        if (obj == MyTable.noObj) {
            report_error("Ime " + designatorName.getName() + " nije deklarisano", designatorName);
            designatorName.obj = MyTable.noObj;
        } else {
            designatorName.obj = obj;
            detection(designatorName);
        }
    }

    public void visit(DesignatorPointAccess designatorPointAccess) {
        // provera da li zapocinje poziv funkcije
        if (designatorPointAccess.getParent() instanceof FactorFunctionCall
                || designatorPointAccess.getParent() instanceof DesignatorFunctionCall) {
            functionCallArguments.push(new LinkedList<>());
        }

        if (designatorPointAccess.getDesignator().obj != MyTable.noObj) {
            Struct struct = designatorPointAccess.getDesignator().obj.getType();
            // provera da li je sa leve strane tacke klasa, enum ili metoda
            if (struct.getKind() != Struct.Class && struct.getKind() != Struct.Interface
                    && struct.getKind() != Struct.Enum) {
                report_error("Moze se pristupati samo poljima klasnih tipova i enumeratora", designatorPointAccess);
                designatorPointAccess.obj = MyTable.noObj;
            } else {
                Obj elem;
                // provera da li postoji ime u opsegu
                elem = struct.getMembers().stream().filter(e -> e.getName().equals(designatorPointAccess.getName()))
                        .findFirst().orElse(null);
                if (elem == null) {
                    report_error("Ne postoji ime " + designatorPointAccess.getName(), designatorPointAccess);
                    designatorPointAccess.obj = MyTable.noObj;
                } else {
                    // prosledjivanje tipa
                    if (struct.getKind() == Struct.Enum) {
                        // za enum prosledi tip enum
                        designatorPointAccess.obj = designatorPointAccess.getDesignator().obj;
                    } else {
                        // za klase i interfejse prosledi tip polja ili metode
                        designatorPointAccess.obj = elem;
                    }
                    tdv.visitObjNode(elem);
                    if (struct.getKind() == Struct.Class || struct.getKind() == Struct.Interface) {
                        if (elem.getKind() == Obj.Meth) {
                            report_info("Poziv metode " + elem.getName() + " " + tdv.getOutput(),
                                    designatorPointAccess);
                        } else {
                            report_info("Pristup polju " + elem.getName() + " " + tdv.getOutput(),
                                    designatorPointAccess);
                        }
                    } else {
                        report_info("Pristup enum konstanti " + elem.getName() + tdv.getOutput(),
                                designatorPointAccess);
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
            // provera da li je u pitanju niz
            if (obj.getType().getKind() != Struct.Array) {
                report_error("Promenljiva " + arrayName + " nije niz", designatorArrayAccess);
                designatorArrayAccess.obj = MyTable.noObj;
                // provera da li je indeks tipa int
            } else if (designatorArrayAccess.getExpr().struct.getKind() != Struct.Int
                    && designatorArrayAccess.getExpr().struct.getKind() != Struct.Enum) {
                report_error("Za indeksiranje niza mora da se koristi int ili enum", designatorArrayAccess);
                designatorArrayAccess.obj = MyTable.noObj;
            } else {
                // prosledjivanje tipa
                designatorArrayAccess.obj = new Obj(Obj.Elem, "elem" + arrayName, obj.getType().getElemType());
                tdv.visitObjNode(obj);
                report_info("Pristup elementu niza " + obj.getName() + tdv.getOutput(), designatorArrayAccess);
            }
        } else {
            designatorArrayAccess.obj = MyTable.noObj;
        }
    }
    // endregion

    public boolean isLeftValue(Designator designator) {
        if (designator.obj == MyTable.noObj) {
            return false;
        }
        if (designator.obj.getKind() == Obj.Fld) {
            // if (designator.obj.getType().getKind() == Struct.Array) {
            // return false;
            // }
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

    public void detection(DesignatorName designator) {
        tdv.visitObjNode(designator.obj);
        switch (designator.obj.getKind()) {
        case Obj.Con:
            report_info("Upotreba globalne konstante " + designator.getName() + tdv.getOutput(), designator);
            break;
        case Obj.Meth:
            if (currentClass != null && currentClass.getMembers().stream()
                    .filter(o -> o.getName().equals(designator.getName())).findFirst().orElse(null) != null) {
                report_info("Poziv metode " + designator.getName() + tdv.getOutput(), designator);
            } else {
                report_info("Poziv globalne funkcije " + tdv.getOutput(), designator);
            }
            break;
        case Obj.Var:
            if (designator.obj.getFpPos() > 0 && !designator.getName().equals("this")) {
                report_info("Upotreba formalnog parametra " + designator.getName() + " metode "
                        + currentMethod.getName() + tdv.getOutput(), designator);
            } else if (currentMethod != null && currentMethod.getLocalSymbols().contains(designator.obj)
                    && !designator.getName().equals("this")) {
                report_info("Upotreba lokalne promenljive " + designator.getName() + " metode "
                        + currentMethod.getName() + tdv.getOutput(), designator);
            } else {
                report_info("Upotreba globalne promenljive " + designator.getName() + tdv.getOutput(), designator);
            }
            break;
        }
    }

    public boolean passed() {
        return !errorDetected && foundMain;
    }
}
