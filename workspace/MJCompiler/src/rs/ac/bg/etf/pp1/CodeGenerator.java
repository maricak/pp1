
package rs.ac.bg.etf.pp1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.mysymboltable.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {

    private Struct currentClass;
    private Obj currentMethod;

    private HashMap<String, Integer> vtpHashMap = new HashMap<>();
    private VirtualTable virtualTable = new VirtualTable();

    // region klase

    public void visit(ClassStart classStart) {
        currentClass = classStart.struct;

        if (currentClass.getElemType() != null) {
            for (Obj parentMethod : currentClass.getElemType().getMembers()) {
                if (parentMethod.getKind() == Obj.Meth) {
                    // prepisate adrese iz roditeljske klase
                    Obj currentMethod = currentClass.getMembersTable().searchKey(parentMethod.getName());
                    currentMethod.setAdr(parentMethod.getAdr());
                }
            }
        }
        vtpHashMap.put(classStart.getClassName(), Code.dataSize);
    }

    public void visit(ClassDecl classDecl) {
        for (Obj method : classDecl.getClassStart().struct.getMembers()) {
            // sve metode ubaciti u tabelu
            if (method.getKind() == Obj.Meth) {
                virtualTable.addFunctionEntry(method.getName(), method.getAdr());
            }
        }
        virtualTable.addTableTerminator();
        currentClass = null;
    }
    // endregion

    // region metode
    public void visit(MethodStart methodStart) {
        // inicijalizovanje main pc
        Obj method = methodStart.obj;
        if (methodStart.getMethodName().equals("main")) {
            Code.mainPc = Code.pc;
        }
        // pozivanje funckcije
        method.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(method.getLevel());
        Code.put(method.getLocalSymbols().size());

        currentMethod = method;

        if (methodStart.getMethodName().equals("main")) {
            virtualTable.copyTable();
        }
    }

    // kraj metode
    public void visit(MethodDecl methodDecl) {
        Obj obj = methodDecl.getMethodStart().obj;
        // return ako nije bila naredba return
        if (obj.getType() == MyTable.noType) {
            Code.put(Code.exit);
            Code.put(Code.return_);
            // runtime error, nema return
        } else {
            Code.put(Code.trap);
            Code.put(1);
        }
        currentMethod = null;
    }

    public void visit(StatementReturn statementReturn) {
        // instrukcije za kraj metode
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(StatementReturnExpr statementReturnExpr) {
        // instrukcije za kraj metode
        Code.put(Code.exit);
        Code.put(Code.return_);
    }
    // endregion

    // region konstante
    public void visit(ConstValueInt constValueInt) {
        // ucitavanje vrednosti na stek
        Code.loadConst(constValueInt.getValue());
    }

    public void visit(ConstValueBool constValueBool) {
        // ucitavanje vrednosti na stek
        Code.loadConst(constValueBool.getValue());
    }

    public void visit(ConstValueChar constValueChar) {
        // ucitavanje vrednosti na stek
        Code.loadConst(constValueChar.getValue());
    }
    // endregion

    // region print i read
    public void visit(StatementPrint statementPrint) {
        if (statementPrint.getExpr().struct.getKind() == Struct.Int
                || statementPrint.getExpr().struct.getKind() == Struct.Bool) {
            Code.put(Code.print);
        } else {
            // char
            Code.put(Code.bprint);
        }
    }

    public void visit(PrintParameter printParameter) {
        // argument za sirinu ispisa
        Code.loadConst(printParameter.getValue());
    }

    public void visit(PrintParameterNO printParameterNO) {
        // ako nije naveden arugment sirina je 5
        Code.put(Code.const_5);
    }

    public void visit(StatementRead statementRead) {
        if (statementRead.getDesignator().obj.getType().getKind() == Struct.Char) {
            // char
            Code.put(Code.bread);
        } else {
            Code.put(Code.read);
        }
        // vrednost se cuva u promenljivu
        Code.store(statementRead.getDesignator().obj);
    }
    // endregion

    // region poziv funkcije
    public void visit(StandardFunctionLen standardFunctionLen) {
        // duzina niza
        Code.put(Code.arraylength);
    }

    public void visit(DesignatorFunctionCall designatorFunctionCall) {
        // poziv funkcije
        callFunction(designatorFunctionCall.getDesignator());
    }

    public void visit(FactorFunctionCall factorFunctionCall) {
        // poziv funckije
        callFunction(factorFunctionCall.getDesignator());
    }

    public void visit(CallStart callStart) {
        Designator designator = null;
        if (callStart.getParent() instanceof DesignatorFunctionCall) {
            designator = ((DesignatorFunctionCall) callStart.getParent()).getDesignator();
        }
        if (callStart.getParent() instanceof FactorFunctionCall) {
            designator = ((FactorFunctionCall) callStart.getParent()).getDesignator();
        }

        if (!(designator instanceof DesignatorPointAccess) // nije pristup preko objekta
                && currentClass != null // kod je unutar klase
                && currentMethod != null // kod je unutar metode
                && !currentMethod.getLocalSymbols().contains(designator.obj) // ime nije lokalna promenljiva ili
                                                                             // parametar
                && currentClass.getMembers().contains(designator.obj)) // ime je polje klase
        {
            Code.put(Code.load);
            Code.put(0);
        }
    }

    private void callFunction(Designator designator) {
        // poziv funkcije --> skok

        if (designator instanceof DesignatorPointAccess) {
            // this je element niza
            designator.traverseBottomUp(this);
            // get vtp
            Code.put(Code.getfield);
            Code.put2(0);
            Code.put(Code.invokevirtual);
            // argument za invoke virtual
            String name = designator.obj.getName();
            for (int i = 0; i < name.length(); i++) {
                Code.put4(name.charAt(i));
            }
            Code.put4(-1);
        } else if (currentClass != null && currentClass.getMembers().contains(designator.obj)) {

            // this
            Code.put(Code.load);
            Code.put(0);

            // get vtp
            Code.put(Code.getfield);
            Code.put2(0);
            Code.put(Code.invokevirtual);
            // argument za invoke virtual
            String name = designator.obj.getName();
            for (int i = 0; i < name.length(); i++) {
                Code.put4(name.charAt(i));
            }
            Code.put4(-1);
        } else {
            int offset = designator.obj.getAdr() - Code.pc;
            Code.put(Code.call);
            Code.put2(offset);
        }
    }
    // endregion

    // region aritmeticke operacije
    public void visit(TermMulop termMulop) {
        Mulop operation = termMulop.getMulop();
        if (operation instanceof Multiplie) {
            // mnozenje
            Code.put(Code.mul);
        } else if (operation instanceof Divide) {
            // deljenje
            Code.put(Code.div);
        } else if (operation instanceof Modulo) {
            // moduo
            Code.put(Code.rem);
        }
    }

    public void visit(ExprAddop exprAddop) {
        Addop operation = exprAddop.getAddop();
        if (operation instanceof Add) {
            // sabiranje
            Code.put(Code.add);
        } else if (operation instanceof Subtract) {
            // oduzimanje
            Code.put(Code.sub);
        }
    }

    public void visit(ExprNegativeTerm exprNegativeTerm) {
        // negacija
        Code.put(Code.neg);
    }

    public void visit(DesignatorIncrement designatorIncrement) {
        // argument se sabira sa 1

        if (designatorIncrement.getDesignator() instanceof DesignatorArrayAccess) {
            designatorIncrement.getDesignator().traverseBottomUp(this);
        }

        Code.load(designatorIncrement.getDesignator().obj);
        Code.put(Code.const_1);
        Code.put(Code.add);
        Code.store(designatorIncrement.getDesignator().obj);
    }

    public void visit(DesignatorDecrement designatorDecrement) {
        // od argumenta se oduzima jedan

        if (designatorDecrement.getDesignator() instanceof DesignatorArrayAccess) {
            designatorDecrement.getDesignator().traverseBottomUp(this);
        }

        Code.load(designatorDecrement.getDesignator().obj);
        Code.put(Code.const_1);
        Code.put(Code.sub);

        Code.store(designatorDecrement.getDesignator().obj);
    }
    // endregion

    // region alokacija
    public void visit(FactorNewArray factorNewArray) {
        // pravlenje niza
        Code.put(Code.newarray);
        if (factorNewArray.getType().struct.getKind() == Struct.Char) {
            Code.put(0);
        } else {
            Code.put(1);
        }
    }

    public void visit(FactorNewObj factorNewObj) {
        Code.put(Code.new_);
        Code.put2(factorNewObj.struct.getNumberOfFields() * 4);
        Code.put(Code.dup);
        Code.loadConst(vtpHashMap.get(((TypeCustom) factorNewObj.getType()).getTypeName())); // pokazivac na tabel
                                                                                             // virtuelnih funckija
        Code.put(Code.putfield);
        Code.put2(0);
    }

    // endregion

    // region operandi
    // public void visit(DesignatorAssign designatorAssign) {
    // Designator designator = designatorAssign.getDesignator();

    // // ucitavanje vrednosti u promenljivu
    // Code.store(designator.obj);
    // }

    // public void visit(Assignop assignop) {
    // Designator designator = ((DesignatorAssign)
    // assignop.getParent()).getDesignator();

    // if (!(designator instanceof DesignatorPointAccess) // nije pristup preko
    // objekta
    // && currentClass != null // kod je unutar klase
    // && currentMethod != null // kod je unutar metode
    // && !currentMethod.getLocalSymbols().contains(designator.obj) // ime nije
    // lokalna promenljiva ili
    // // parametar
    // && currentClass.getMembers().contains(designator.obj)) // ime je polje klase
    // {
    // Code.put(Code.load);
    // Code.put(0);
    // }
    // }

    // public void visit(FactorDesignator factorDesignator) {
    // Designator designator = factorDesignator.getDesignator();

    // if (!(designator instanceof DesignatorPointAccess) // nije pristup preko
    // objekta
    // && currentClass != null // kod je unutar klase
    // && currentMethod != null // kod je unutar metode
    // && !currentMethod.getLocalSymbols().contains(designator.obj) // ime nije
    // lokalna promenljiva ili
    // // parametar
    // && currentClass.getMembers().contains(designator.obj)) // ime je polje klase
    // {
    // Code.put(Code.load);
    // Code.put(0);
    // }

    // if (!(designator instanceof DesignatorArrayAccess)) {
    // // vrednost ide na stek
    // if (designator.obj.getKind() != Obj.Elem)
    // Code.load(designator.obj);
    // }
    // }

    // public void visit(DesignatorName designatorName) {

    // if (designatorName.getParent() instanceof DesignatorArrayAccess) {
    // // kod pristupa nizu prvo postaviti adresu niza pa tek onda indeks
    // DesignatorArrayAccess designatorArrayAccess = (DesignatorArrayAccess)
    // designatorName.getParent();
    // Code.load(designatorArrayAccess.getDesignator().obj);
    // }
    // if (designatorName.getParent() instanceof DesignatorPointAccess) {
    // DesignatorPointAccess designatorPointAccess = (DesignatorPointAccess)
    // designatorName.getParent();
    // if (designatorPointAccess.getDesignator().obj.getType().getKind() !=
    // Struct.Enum)
    // // kod pristupa klasi na stek staviti prvo objekat
    // Code.load(designatorPointAccess.getDesignator().obj);
    // }

    // }

    // public void visit(DesignatorArrayAccess designatorArrayAccess) {
    // if (designatorArrayAccess.getParent() instanceof DesignatorAssign)
    // return;
    // if (designatorArrayAccess.getParent() instanceof StatementRead)
    // return;
    // // ucitavanje polja niza cim argumenti budu na steku osim u slicaju dodele
    // // vrednosti
    // Code.load(designatorArrayAccess.obj);
    // }

    // public void visit(DesignatorPointAccess designatorPointAccess) {
    // Obj obj = designatorPointAccess.obj;
    // // u semantickoj analizi se prosledjuje enum, ovde treba proslediti bas enum
    // // konstantu
    // if (obj.getKind() == Obj.Type && obj.getType().getKind() == Struct.Enum) {
    // Obj elem = obj.getType().getMembers().stream()
    // .filter(e ->
    // e.getName().equals(designatorPointAccess.getName())).findFirst().orElse(null);
    // designatorPointAccess.obj = elem;
    // }
    // if (designatorPointAccess.getParent() instanceof DesignatorArrayAccess
    // /* || designatorPointAccess.getParent() instanceof DesignatorPointAccess */)
    // {
    // Code.load(obj);
    // }
    // }
    // endregion

    // region operandi

    public void visit(DesignatorEnd designatorEnd) {
        Designator designator = null;
        if (designatorEnd.getParent() instanceof FactorDesignator) {
            designator = ((FactorDesignator) designatorEnd.getParent()).getDesignator();
        }
        if (designatorEnd.getParent() instanceof DesignatorPointAccess) {
            DesignatorPointAccess designatorPointAccess = (DesignatorPointAccess) designatorEnd.getParent();
            designator = designatorPointAccess.getDesignator();

            Obj obj = designatorPointAccess.obj;
            // u semantickoj analizi se prosledjuje enum, ovde treba proslediti bas enum
            // konstantu
            if (obj.getKind() == Obj.Type && obj.getType().getKind() == Struct.Enum) {
                Obj elem = obj.getType().getMembers().stream()
                        .filter(e -> e.getName().equals(designatorPointAccess.getName())).findFirst().orElse(null);
                designator.obj = elem;
                designatorPointAccess.obj = elem;
                return;
            }
        }
        if (designatorEnd.getParent() instanceof DesignatorArrayAccess) {
            designator = ((DesignatorArrayAccess) designatorEnd.getParent()).getDesignator();
        }

        if (!(designator instanceof DesignatorPointAccess) && currentClass != null && currentMethod != null
                && !currentMethod.getLocalSymbols().contains(designator.obj)
                && currentClass.getMembers().contains(designator.obj)) {
            Code.put(Code.load);
            Code.put(0);
        }
        Code.load(designator.obj);
    }

    public void visit(Assignop assignop) {
        Designator designator = ((DesignatorAssign) assignop.getParent()).getDesignator();

        if (!(designator instanceof DesignatorPointAccess) && currentClass != null && currentMethod != null
                && !currentMethod.getLocalSymbols().contains(designator.obj)
                && currentClass.getMembers().contains(designator.obj)) {
            Code.put(Code.load);
            Code.put(0);
        }
    }

    public void visit(DesignatorAssign designatorAssign) {
        Code.store(designatorAssign.getDesignator().obj);
    }
    // endregion

    // region condition
    Stack<LinkedList<Integer>> fixupTrueStack = new Stack<>();
    Stack<LinkedList<Integer>> fixupFalseStack = new Stack<>();

    public void visit(OrStart orStart) {
        // ako je prethodno tacno skok na if deo, preskacu se ostali or uslovi
        Code.putJump(0);
        fixupTrueStack.peek().add(Code.pc - 2);
        // svi pre or operatora ako nisu tacni skacu na proveru narednog or uslova
        fixupFalseStack.peek().forEach(addr -> Code.fixup(addr));
        fixupFalseStack.peek().clear();
    }

    public void visit(CondFactExpr condFactExpr) {
        // pojedinacni izrazi -- poredi se sa true
        Code.put(Code.const_1);
        Code.putFalseJump(Code.eq, 0);
        fixupFalseStack.peek().add(Code.pc - 2);
    }

    public void visit(CondFactCompare condFactCompare) {
        // pojedinacni izrazi -- poredjenje
        if (condFactCompare.getRelop() instanceof Equal) {
            Code.putFalseJump(Code.eq, 0);
        } else if (condFactCompare.getRelop() instanceof NotEqual) {
            Code.putFalseJump(Code.ne, 0);
        } else if (condFactCompare.getRelop() instanceof Greater) {
            Code.putFalseJump(Code.gt, 0);
        } else if (condFactCompare.getRelop() instanceof GreaterEqual) {
            Code.putFalseJump(Code.ge, 0);
        } else if (condFactCompare.getRelop() instanceof Less) {
            Code.putFalseJump(Code.lt, 0);
        } else if (condFactCompare.getRelop() instanceof LessEqual) {
            Code.putFalseJump(Code.le, 0);
        }
        fixupFalseStack.peek().add(Code.pc - 2);
    }
    // endregion

    // region if

    public void visit(StatementIfElse statementIfElse) {
        // postavljanje adrese za preskakanje else bloka - samo jedna adresa
        LinkedList<Integer> fix = fixupTrueStack.pop();
        fix.forEach(adr -> Code.fixup(adr));
        fixupFalseStack.pop();
    }

    public void visit(ElseStart elseStart) {
        // skok za preskakanje else bloka
        Code.putJump(0);
        fixupTrueStack.peek().add(Code.pc - 2);
        // postaviti adresu za false/else skokove, iza if bloka
        LinkedList<Integer> fix = fixupFalseStack.peek();
        fix.forEach(adr -> Code.fixup(adr));
    }

    public void visit(StatementIf statementIf) {
        // postaviti adresu za false skokove iza if bloka
        LinkedList<Integer> fix = fixupFalseStack.pop();
        fix.forEach(adr -> Code.fixup(adr));
        fixupTrueStack.pop();
    }

    public void visit(IfCondition ifCondition) {
        // postaviti adresu za true skokove
        LinkedList<Integer> fix = fixupTrueStack.peek();
        fix.forEach(adr -> Code.fixup(adr));
        fix.clear();
    }

    public void visit(IfStart ifStart) {
        fixupFalseStack.push(new LinkedList<>());
        fixupTrueStack.push(new LinkedList<>());
    }

    // endregion

    // region for
    Stack<Integer> forCheckConditionStack = new Stack<>();
    Stack<Integer> forUpdateStmntStack = new Stack<>();
    Stack<LinkedList<Integer>> breakStack = new Stack<>();
    Stack<LinkedList<Integer>> continueStack = new Stack<>();

    public void visit(ForStart forStart) {
        fixupFalseStack.push(new LinkedList<>());
        fixupTrueStack.push(new LinkedList<>());

        breakStack.push(new LinkedList<>());
        continueStack.push(new LinkedList<>());
    }

    public void visit(ForInitStatement forInitStatemet) {
        // adresa za povratak na proveru uslova
        forCheckConditionStack.push(Code.pc);
    }

    public void visit(ForInitStatementNO forInitStatementNO) {
        // adresa za povratak na proveru uslova
        forCheckConditionStack.push(Code.pc);
    }

    public void visit(ForCondition forCondition) {
        // preskakanje update statement
        Code.putJump(0);
        fixupTrueStack.peek().add(Code.pc - 2);
        // adresa za update statement
        forUpdateStmntStack.push(Code.pc);
    }

    public void visit(ForConditionNO forConditionNO) {
        // preskakanje update statement
        Code.putJump(0);
        fixupTrueStack.peek().add(Code.pc - 2);
        // adresa za update statement
        forUpdateStmntStack.push(Code.pc);
    }

    public void visit(ForUpdateStatement forUpdateStatement) {
        // povratak na proveru uslova
        Code.putJump(forCheckConditionStack.peek());
        // postaviti adresu za true skokove
        LinkedList<Integer> fix = fixupTrueStack.peek();
        fix.forEach(adr -> Code.fixup(adr));
        fix.clear();
    }

    public void visit(ForUpdateStatementNO forUpdateStatementNO) {
        // povratak na proveru uslova
        Code.putJump(forCheckConditionStack.peek());
        // postaviti adresu za true skokove
        LinkedList<Integer> fix = fixupTrueStack.peek();
        fix.forEach(adr -> Code.fixup(adr));
        fix.clear();
    }

    public void visit(ForBody forBody) {

        // postaviti adresu za continue skokove
        continueStack.peek().forEach(adr -> Code.fixup(adr));
        // bezuslovni skok na update statement
        Code.putJump(forUpdateStmntStack.peek());

        // postaviti adresu za false skokove iza for bloka
        LinkedList<Integer> fix = fixupFalseStack.pop();
        fix.forEach(adr -> Code.fixup(adr));
        fixupTrueStack.pop();

        // postaviti adresu za break skokove iza for bloka
        breakStack.peek().forEach(adr -> Code.fixup(adr));
    }

    public void visit(ForStatement forStatement) {
        forCheckConditionStack.pop();
        forUpdateStmntStack.pop();
        continueStack.pop();
        breakStack.pop();
    }

    public void visit(StatementBreak statementBreak) {
        Code.putJump(0);
        breakStack.peek().add(Code.pc - 2);
    }

    public void visit(StatementContinue statementContinue) {
        Code.putJump(0);
        continueStack.peek().add(Code.pc - 2);
    }
    // endregion
}