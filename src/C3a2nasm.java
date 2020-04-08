import c3a.*;
import nasm.*;
import ts.Ts;
import ts.TsItemFct;

public class C3a2nasm implements C3aVisitor<NasmOperand> {

    private C3a c3a;
    private Nasm nasm;
    private Ts tableGlobale;
    private TsItemFct currentFct;

    public C3a2nasm(C3a c3a, Ts table) {
        this.nasm = new Nasm(table);
        this.c3a = c3a;
        this.tableGlobale = table;

        head();
        for (int i=0; this.c3a.listeInst.size() > i; i++) {
            this.c3a.listeInst.get(i).accept(this);
        }
    }

    private void head(){
        NasmCall callMain = new NasmCall(null,new NasmLabel("main"),"");
        nasm.ajouteInst(callMain);

        NasmRegister registerEBX = nasm.newRegister();
        registerEBX.colorRegister(Nasm.REG_EBX);
        NasmMov moveEBX = new NasmMov(null,registerEBX, new NasmConstant(0), " valeur de retour du programme");
        nasm.ajouteInst(moveEBX);

        NasmRegister registerEAX = nasm.newRegister();
        registerEAX.colorRegister(Nasm.REG_EAX);
        NasmMov moveEAX = new NasmMov(null,registerEAX, new NasmConstant(1), "");
        nasm.ajouteInst(moveEAX);

        nasm.ajouteInst(new NasmInt(null, ""));
    }

    @Override
    public NasmOperand visit(C3aInstAdd inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper1 = inst.op1.accept(this);
        NasmOperand oper2 = inst.op2.accept(this);
        NasmOperand dest = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label, dest, oper1, ""));
        nasm.ajouteInst(new NasmAdd(null, dest, oper2, ""));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstCall inst) {
        NasmRegister nasmRegister = new NasmRegister(Nasm.REG_ESP);
        nasmRegister.colorRegister(Nasm.REG_ESP);

        NasmSub nasmSub = new NasmSub(null, nasmRegister, new NasmConstant(4), " allocation mémoire pour la valeur de retour");
        nasm.ajouteInst(nasmSub);

        NasmLabel oper = (NasmLabel) inst.op1.accept(this);
        NasmCall nasmCall = new NasmCall(null, oper, "");
        nasm.ajouteInst(nasmCall);

        if (inst.result != null) {
            NasmPop nasmPop = new NasmPop(null, inst.result.accept(this) , " récupération de la valeur de retour");
            nasm.ajouteInst(nasmPop);
        }
        if (tableGlobale.getFct(oper.val).nbArgs > 0) {
            NasmAdd nasmAdd = new NasmAdd(
                    null,
                    nasmRegister,
                    new NasmConstant(inst.op1.val.nbArgs * 4),
                    " désallocation des arguments");
            nasm.ajouteInst(nasmAdd);
        }
        
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstFBegin inst) {
        NasmRegister registerEBP = new NasmRegister(Nasm.REG_EBP);
        registerEBP.colorRegister(Nasm.REG_EBP);

        NasmRegister registerESP = new NasmRegister(Nasm.REG_ESP);
        registerESP.colorRegister(Nasm.REG_ESP);

        NasmPush callFct = new NasmPush(new NasmLabel(inst.val.identif),registerEBP," sauvegarde la valeur de ebp");
        nasm.ajouteInst(callFct);

        NasmMov moveEBP = new NasmMov(null, registerEBP, registerESP, " nouvelle valeur de ebp");
        nasm.ajouteInst(moveEBP);

        currentFct = inst.val;

        NasmSub subEsp = new NasmSub(null, registerESP, new NasmConstant(inst.val.nbArgs*4), " allocation des variables locales");
        nasm.ajouteInst(subEsp);

        return null;
    }

    @Override
    public NasmOperand visit(C3aInst inst) {
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJumpIfLess inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper = inst.op1.accept(this);
        NasmOperand oper2 = inst.op2.accept(this);
        NasmOperand result = inst.result.accept(this);

        if(oper.getClass() != NasmRegister.class){
            NasmRegister nasmRegister = nasm.newRegister();
            nasm.ajouteInst(new NasmMov(label, nasmRegister, oper, " JumpIfLess 1"));
            nasm.ajouteInst( new NasmCmp(null, nasmRegister, oper2, " on passe par un registre temporaire"));
        }
        else
            nasm.ajouteInst(new NasmCmp(label,oper,oper2," JumpIfLess 1"));

        nasm.ajouteInst(new NasmJl(null, result, " JumpIfLess 2"));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstMult inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper1 = inst.op1.accept(this);
        NasmOperand oper2 = inst.op2.accept(this);
        NasmOperand dest = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label, dest, oper1, ""));
        nasm.ajouteInst(new NasmMul(null, dest, oper2, ""));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstRead inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand dest = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label, dest, null, " Read"));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstSub inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper1 = inst.op1.accept(this);
        NasmOperand oper2 = inst.op2.accept(this);
        NasmOperand dest = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label, dest, oper1, ""));
        nasm.ajouteInst(new NasmSub(null, dest, oper2, ""));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstAffect inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper = inst.op1.accept(this);
        NasmOperand dest = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label, dest, oper, " Affect"));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstDiv inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper1 = inst.op1.accept(this);
        NasmOperand oper2 = inst.op2.accept(this);
        NasmOperand dest = inst.result.accept(this);
        nasm.ajouteInst(new NasmMov(label, dest, oper1, ""));
        nasm.ajouteInst(new NasmDiv( dest, oper2, ""));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstFEnd inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        NasmRegister registerESP = new NasmRegister(Nasm.REG_ESP);
        registerESP.colorRegister(Nasm.REG_ESP);

        NasmRegister registerEBP = new NasmRegister(Nasm.REG_EBP);
        registerEBP.colorRegister(Nasm.REG_EBP);

        int allocVar = tableGlobale.getFct(currentFct.identif).getTable().nbVar()*4;
        NasmConstant constant = new NasmConstant(allocVar);

        nasm.ajouteInst(new NasmAdd(label, registerESP, constant," désallocation des variables locales"));
        nasm.ajouteInst(new NasmPop(null, registerEBP, " restaure la valeur de ebp"));
        nasm.ajouteInst(new NasmRet(null,""));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJumpIfEqual inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper = inst.op1.accept(this);
        NasmOperand oper2 = inst.op2.accept(this);
        NasmOperand result = inst.result.accept(this);

        if(oper.getClass() != NasmRegister.class){
            NasmRegister nasmRegister = nasm.newRegister();
            nasm.ajouteInst(new NasmMov(label, nasmRegister, oper, " JumpIfEqual 1"));
            nasm.ajouteInst( new NasmCmp(null, nasmRegister, oper2, " on passe par un registre temporaire"));
        }
        else
            nasm.ajouteInst(new NasmCmp(label,oper,oper2," JumIfEqual 1"));

        nasm.ajouteInst(new NasmJe(null, result, " JumpIfEqual 2"));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJumpIfNotEqual inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper = inst.op1.accept(this);
        NasmOperand oper2 = inst.op2.accept(this);
        NasmOperand result = inst.result.accept(this);

        if(oper.getClass() != NasmRegister.class) {
            NasmRegister nasmRegister = nasm.newRegister();
            nasm.ajouteInst(new NasmMov(label, nasmRegister, oper, " jumpIfNotEqual 1"));
            nasm.ajouteInst(new NasmCmp(null, nasmRegister, oper2, " on passe par un registre temporaire"));
        }
        else
            nasm.ajouteInst(new NasmCmp(label,oper,oper2," jumpIfNotEqual 1"));

        nasm.ajouteInst(new NasmJne(null, result, " jumpIfNotEqual 2"));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJump inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand result = inst.result.accept(this);
        nasm.ajouteInst(new NasmJmp(label, result, ""));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstParam inst) {
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;
        NasmOperand oper = inst.op1.accept(this);
        nasm.ajouteInst(new NasmPush(label, oper,""));

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstReturn inst) {
        NasmOperand op = inst.op1.accept(this);

        NasmRegister register = new NasmRegister(Nasm.REG_EBP);
        register.colorRegister(Nasm.REG_EBP);

        NasmAddress addr = new NasmAddress(register, '+',new NasmConstant(2));
        NasmMov nasmMov = new NasmMov(null,addr , op, " ecriture de la valeur de retour");
        nasm.ajouteInst(nasmMov);

        return null;
    }

    @Override
    public NasmOperand visit(C3aInstWrite inst) {
        NasmOperand oper = inst.op1.accept(this);
        NasmOperand label = (inst.label != null) ? inst.label.accept(this) : null;

        NasmRegister register = nasm.newRegister();
        register.colorRegister(Nasm.REG_EAX);

        nasm.ajouteInst(new NasmMov(label, register, oper, " Write 1"));

        NasmCall nasmCall = new NasmCall(null, new NasmLabel("iprintLF"), " Write 2");
        nasm.ajouteInst(nasmCall);

        return null;
    }

    @Override
    public NasmOperand visit(C3aConstant oper) {
        return new NasmConstant(oper.val);
    }

    @Override
    public NasmOperand visit(C3aLabel oper) {
        return new NasmLabel(oper.toString());
    }

    @Override
    public NasmOperand visit(C3aTemp oper) {
        NasmRegister nasmRegister = nasm.newRegister();
        nasmRegister.val = oper.num;

        return nasmRegister;
    }

    @Override
    public NasmOperand visit(C3aVar oper) {

        if (tableGlobale.getVar(oper.item.identif) != null) {
            NasmLabel label = new NasmLabel(oper.item.identif);
            if (oper.index != null)
                return new NasmAddress(label, '+', oper.index.accept(this));
            else
                return new NasmAddress(label);
        }
        else if (tableGlobale.getFct(currentFct.identif).getTable().getVar(oper.item.identif).isParam) {
            NasmRegister nasmRegister = new NasmRegister(Nasm.REG_EBP);
            nasmRegister.colorRegister(Nasm.REG_EBP);
            NasmConstant offset = new NasmConstant( 2 + tableGlobale.getFct(currentFct.identif).getNbArgs() - oper.item.adresse);
            return new NasmAddress(nasmRegister, '+', offset);
        }
        else {
            NasmRegister nasmRegister = new NasmRegister(Nasm.REG_EBP);
            nasmRegister.colorRegister(Nasm.REG_EBP);
            return new NasmAddress(nasmRegister, '-', new NasmConstant(oper.item.adresse + oper.item.taille));
        }
    }

    @Override
    public NasmOperand visit(C3aFunction oper) {
        return new NasmLabel(oper.val.identif);
    }

    public Nasm getNasm() {
        return nasm;
    }
}
