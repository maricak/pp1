
package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
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
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(ReturnT returnT) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(ReturnVoid returnVoid) {
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
        if (statementPrint.getExpr().struct.getKind() == Struct.Int) {
            Code.put(Code.print);
        } else {
            Code.put(Code.bprint);
        }
    }

    public void visit(PrintParameter printParameter) {
        Code.loadConst(printParameter.getValue());
    }

    public void visit(PrintParameterNO printParameterNO) {
        Code.put(Code.const_3);
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
    // ???
    public void visit(DesignatorAssign designatorAssign) {
        Code.store(designatorAssign.getDesignator().obj);
    }

    public void visit(FactorDesignator factorDesignator) {
        // ubaci adresu niza
        Code.load(factorDesignator.getDesignator().obj);
    }

    public void visit(DesignatorName designatorName) {
        if(designatorName.getParent() instanceof DesignatorArrayAccess) {
            System.err.println("otac");
            DesignatorArrayAccess designatorArrayAccess = (DesignatorArrayAccess) designatorName.getParent();
            Code.load(designatorArrayAccess.getDesignator().obj);
        }       
    }

    public void visit(DesignatorArrayAccess designatorArrayAccess) {
        
    }
    // public void visit(Designator designator) {
    // SyntaxNode parent = designator.getParent();
    // if(parent instanceof DesignatorArrayAccess) {
    // System.err.println("Adresa niza " +
    // ((DesignatorArrayAccess)parent).getDesignator().obj.getName());
    // Code.load(((DesignatorArrayAccess)parent).getDesignator().obj);
    // }
    // //System.err.println("Adresa podatka " +
    // factorDesignator.getDesignator().obj.getName());
    // }

    // public void visit(DesignatorName designatorName) { // elem designator
    // System.err.println("obj kind " + designatorName.obj.getKind());
    // Code.load(designatorName.obj);
    // }

    public void visit(DesignatorPointAccess designatorPointAccess) {
        Struct struct = designatorPointAccess.obj.getType();
        if (struct.getKind() == Struct.Enum) {
            Obj elem = struct.getMembers().stream().filter(e -> e.getName().equals(designatorPointAccess.getName()))
                    .findFirst().orElse(null);
            designatorPointAccess.obj = elem;
        }
    }
}