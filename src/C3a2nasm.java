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
        return null;
    }

    @Override
    public NasmOperand visit(C3aInstJumpIfNotEqual inst) {
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
        return null;
    }

    @Override
    public NasmOperand visit(C3aFunction oper) {
        return null;
    }

    public Nasm getNasm() {
        return nasm;
    }
}
