
package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.mysymboltable.MyTable;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {

    // region metode
    public void visit(MethodStart methodStart) {
        Obj method = methodStart.obj;
        if (methodStart.getMethodName().equals("main")) {
            Code.mainPc = Code.pc;
        }
        method.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(method.getLevel());
        Code.put(method.getLocalSymbols().size());
    }

    public void visit(MethodDecl methodDecl) {
        Obj obj = methodDecl.getMethodStart().obj;
        if (obj.getType() == MyTable.noType) {
            Code.put(Code.exit);
            Code.put(Code.return_);
        } else {
            Code.put(Code.trap);
            Code.put(1);
        }
    }

    public void visit(StatementReturn statementReturn) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(StatementReturnExpr statementReturnExpr) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }
    // endregion

    // region konstante
    public void visit(ConstValueInt constValueInt) {
        Code.loadConst(constValueInt.getValue());
    }

    public void visit(ConstValueBool constValueBool) {
        Code.loadConst(constValueBool.getValue());
    }

    public void visit(ConstValueChar constValueChar) {
        Code.loadConst(constValueChar.getValue());
    }
    // endregion

    // region print i read
    public void visit(StatementPrint statementPrint) {
        if (statementPrint.getExpr().struct.getKind() == Struct.Int
                || statementPrint.getExpr().struct.getKind() == Struct.Bool) {
            Code.put(Code.print);
        } else {
            Code.put(Code.bprint);
        }
    }

    public void visit(PrintParameter printParameter) {
        Code.loadConst(printParameter.getValue());
    }

    public void visit(PrintParameterNO printParameterNO) {
        Code.put(Code.const_5);
    }

    public void visit(StatementRead statementRead) {
        if (statementRead.getDesignator().obj.getType().getKind() == Struct.Char) {
            Code.put(Code.bread);
        } else {
            Code.put(Code.read);
        }
        Code.store(statementRead.getDesignator().obj);
    }
    // endregion

    // region poziv funkcije
    public void visit(StandardFunctionLen standardFunctionLen) {
        Code.put(Code.arraylength);
    }

    public void visit(DesignatorFunctionCall designatorFunctionCall) {
        callFunction(designatorFunctionCall.getDesignator());
    }

    public void visit(FactorFunctionCall factorFunctionCall) {
        callFunction(factorFunctionCall.getDesignator());
    }

    private void callFunction(Designator designator) {
        int offset = designator.obj.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);
    }
    // endregion

    // region aritmeticke operacije
    public void visit(TermMulop termMulop) {
        Mulop operation = termMulop.getMulop();
        if (operation instanceof Multiplie) {
            Code.put(Code.mul);
        } else if (operation instanceof Divide) {
            Code.put(Code.div);
        } else if (operation instanceof Modulo) {
            Code.put(Code.rem);
        }
    }

    public void visit(ExprAddop exprAddop) {
        Addop operation = exprAddop.getAddop();
        if (operation instanceof Add) {
            Code.put(Code.add);
        } else if (operation instanceof Subtract) {
            Code.put(Code.sub);
        }
    }

    public void visit(ExprNegativeTerm exprNegativeTerm) {
        Code.put(Code.neg);
    }

    public void visit(DesignatorIncrement designatorIncrement) {
        Code.load(designatorIncrement.getDesignator().obj);
        Code.put(Code.const_1);
        Code.put(Code.add);
        Code.store(designatorIncrement.getDesignator().obj);
    }

    public void visit(DesignatorDecrement designatorDecrement) {
        Code.load(designatorDecrement.getDesignator().obj);
        Code.put(Code.const_1);
        Code.put(Code.sub);
        Code.store(designatorDecrement.getDesignator().obj);
    }
    // endregion

    // region alokacija
    public void visit(FactorNewArray factorNewArray) {
        Code.put(Code.newarray);
        if (factorNewArray.getType().struct.getKind() == Struct.Char) {
            Code.put(0);
        } else {
            Code.put(1);
        }
    }

    public void visit(FactorNewObj factorNewObj) {

    }

    // endregion

    // region operandi
    public void visit(DesignatorAssign designatorAssign) {
        Code.store(designatorAssign.getDesignator().obj);
    }

    public void visit(FactorDesignator factorDesignator) {
        Code.load(factorDesignator.getDesignator().obj);
    }

    public void visit(DesignatorName designatorName) {
        if (designatorName.getParent() instanceof DesignatorArrayAccess) {
            // kod pristupa nizu prvo posatviti adresu niza pa tek onda indeksa
            DesignatorArrayAccess designatorArrayAccess = (DesignatorArrayAccess) designatorName.getParent();
            Code.load(designatorArrayAccess.getDesignator().obj);
        }
    }

    public void visit(DesignatorPointAccess designatorPointAccess) {
        Struct struct = designatorPointAccess.obj.getType();
        // u semantickoj analizi se prosledjuje enum, ovde treba proslediti bas enum
        // konstantu
        if (struct.getKind() == Struct.Enum) {
            Obj elem = struct.getMembers().stream().filter(e -> e.getName().equals(designatorPointAccess.getName()))
                    .findFirst().orElse(null);
            designatorPointAccess.obj = elem;
        }
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
        // bezuslovni skok na update statement
        Code.putJump(forUpdateStmntStack.peek());

        // postaviti adresu za continue skokove
        continueStack.peek().forEach(adr -> Code.fixup(adr));

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