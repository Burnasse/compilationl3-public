import c3a.*;
import sa.*;

public class Sa2c3a extends SaDepthFirstVisitor<C3aOperand> {

    private C3a c3a;

    public Sa2c3a(SaNode root) {
        c3a = new C3a();
        root.accept(this);
    }

    @Override
    public C3aOperand visit(SaProg node) {
        return super.visit(node);
    }

    @Override
    public C3aOperand visit(SaDecTab node) {
        return new C3aVar(node.tsItem, new C3aTemp(node.getTaille()));
    }

    @Override
    public C3aOperand visit(SaExp node) {
        return super.visit(node);
    }

    @Override
    public C3aOperand visit(SaExpInt node) {
        return new C3aConstant(node.getVal());
    }

    @Override
    public C3aOperand visit(SaExpVar node) {

        return node.getVar().accept(this);
    }

    @Override
    public C3aOperand visit(SaInstEcriture node) {
        C3aOperand op = node.getArg().accept(this);
        c3a.ajouteInst(new C3aInstWrite(op, ""));

        return null;
    }

    @Override
    public C3aOperand visit(SaInstTantQue node) {
        C3aLabel label0 = c3a.newAutoLabel();
        C3aLabel label1 = c3a.newAutoLabel();

        c3a.addLabelToNextInst(label0);
        C3aOperand op1 = node.getTest().accept(this);
        c3a.ajouteInst(new C3aInstJumpIfEqual(op1, c3a.False, label1,""));

        node.getFaire().accept(this);
        c3a.ajouteInst(new C3aInstJump(label0,""));
        c3a.addLabelToNextInst(label1);

        return null;
    }

    @Override
    public C3aOperand visit(SaLInst node) {
        return super.visit(node);
    }

    @Override
    public C3aOperand visit(SaDecFonc node) {
        c3a.ajouteInst(new C3aInstFBegin(node.tsItem, "entree fonction"));
        node.getCorps().accept(this);
        c3a.ajouteInst(new C3aInstFEnd(""));
        return null;
    }

    @Override
    public C3aOperand visit(SaDecVar node) {
        return null;
    }

    @Override
    public C3aOperand visit(SaInstAffect node) {
        C3aOperand result = node.getLhs().accept(this);
        C3aOperand op1 = node.getRhs().accept(this);
        C3aInstAffect aInstAffect = new C3aInstAffect(op1, result, "");
        c3a.ajouteInst(aInstAffect);

        return null;
    }

    @Override
    public C3aOperand visit(SaLDec node) {
        return super.visit(node);
    }

    @Override
    public C3aOperand visit(SaVarSimple node) {
        return new C3aVar(node.tsItem, null);
    }

    @Override
    public C3aOperand visit(SaAppel node) {
        if(node.getArguments() != null)
            node.getArguments().accept(this);
        c3a.ajouteInst(new C3aInstCall(new C3aFunction(node.tsItem),null,""));
        return super.visit(node);
    }

    @Override
    public C3aOperand visit(SaExpAppel node) {
        if(node.getVal().getArguments() != null)
            node.getVal().getArguments().accept(this);
        C3aTemp temp = c3a.newTemp();
        c3a.ajouteInst(new C3aInstCall(new C3aFunction(node.getVal().tsItem),temp,""));
        return temp;
    }

    @Override
    public C3aOperand visit(SaExpAdd node) {
        C3aTemp temp = c3a.newTemp();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);


        c3a.ajouteInst(new C3aInstAdd(op1, op2, temp , ""));

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpSub node) {
        C3aTemp temp = c3a.newTemp();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);

        c3a.ajouteInst(new C3aInstSub(op1, op2, temp , ""));

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpMult node) {
        C3aTemp temp = c3a.newTemp();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);


        c3a.ajouteInst(new C3aInstMult(op1, op2, temp , ""));

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpDiv node) {
        C3aTemp temp = c3a.newTemp();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);

        c3a.ajouteInst(new C3aInstDiv(op1, op2, temp , ""));

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpInf node) {
        C3aTemp temp = c3a.newTemp();
        C3aLabel label = c3a.newAutoLabel();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);

        c3a.ajouteInst(new C3aInstAffect(c3a.True, temp,""));
        c3a.ajouteInst(new C3aInstJumpIfLess(op1,op2,label,""));
        c3a.ajouteInst(new C3aInstAffect(c3a.False, temp,""));

        c3a.addLabelToNextInst(label);

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpEqual node) {
        C3aTemp temp = c3a.newTemp();
        C3aLabel label = c3a.newAutoLabel();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);

        c3a.ajouteInst(new C3aInstAffect(c3a.True, temp,""));
        c3a.ajouteInst(new C3aInstJumpIfEqual(op1,op2,label,""));
        c3a.ajouteInst(new C3aInstAffect(c3a.False, temp,""));

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpAnd node) {
        C3aTemp temp = c3a.newTemp();
        C3aLabel label0 = c3a.newAutoLabel();
        C3aLabel label1 = c3a.newAutoLabel();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);

        c3a.ajouteInst(new C3aInstJumpIfEqual(op1,c3a.False,label1,""));
        c3a.ajouteInst(new C3aInstJumpIfEqual(op2,c3a.False,label1,""));

        c3a.ajouteInst(new C3aInstAffect(c3a.True, temp,""));

        c3a.ajouteInst(new C3aInstJump(label0,""));

        c3a.addLabelToNextInst(label1);
        c3a.ajouteInst(new C3aInstAffect(c3a.False, temp,""));
        c3a.addLabelToNextInst(label0);

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpOr node) {
        C3aTemp temp = c3a.newTemp();
        C3aLabel label0 = c3a.newAutoLabel();
        C3aLabel label1 = c3a.newAutoLabel();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);

        c3a.ajouteInst(new C3aInstJumpIfNotEqual(op1,c3a.False,label1,""));
        c3a.ajouteInst(new C3aInstJumpIfNotEqual(op2,c3a.False,label1,""));

        c3a.ajouteInst(new C3aInstAffect(c3a.False, temp,""));

        c3a.ajouteInst(new C3aInstJump(label0,""));

        c3a.addLabelToNextInst(label1);
        c3a.ajouteInst(new C3aInstAffect(c3a.True, temp,""));
        c3a.addLabelToNextInst(label0);

        return temp;
    }

    @Override
    public C3aOperand visit(SaExpNot node) {
        C3aLabel label = c3a.newAutoLabel();

        C3aOperand op1 = node.getOp1().accept(this);
        C3aOperand op2 = node.getOp2().accept(this);

        c3a.ajouteInst(new C3aInstJumpIfNotEqual(op1,op2,label,""));

        return op1;
    }

    @Override
    public C3aOperand visit(SaExpLire node) {
        C3aTemp temp = c3a.newTemp();
        c3a.ajouteInst(new C3aInstRead(temp,""));
        return temp;
    }

    @Override
    public C3aOperand visit(SaInstBloc node) {
        return super.visit(node);
    }

    @Override
    public C3aOperand visit(SaInstSi node) {
        C3aLabel label = c3a.newAutoLabel();

        C3aOperand op1 = node.getTest().accept(this);
        c3a.ajouteInst(new C3aInstJumpIfEqual(op1,c3a.False, label, ""));

        node.getAlors().accept(this);

        if(node.getSinon() != null){
            C3aLabel label1 = c3a.newAutoLabel();
            c3a.ajouteInst(new C3aInstJump(label1,""));
            c3a.addLabelToNextInst(label);
            node.getSinon().accept(this);
            c3a.addLabelToNextInst(label1);
        }
        else
            c3a.addLabelToNextInst(label);

        return null;
    }

    @Override
    public C3aOperand visit(SaInstRetour node) {
        C3aOperand op = node.getVal().accept(this);
        c3a.ajouteInst(new C3aInstReturn(op,""));
        return null;
    }

    @Override
    public C3aOperand visit(SaLExp node) {
        if(node.getQueue() != null)
            node.getQueue().accept(this);

        C3aOperand op1 = node.getTete().accept(this);
        c3a.ajouteInst(new C3aInstParam(op1,""));
        return null;
    }

    @Override
    public C3aOperand visit(SaVarIndicee node) {
        C3aOperand op1 = node.getIndice().accept(this);
        return new C3aVar(node.tsItem, op1);
    }

    public C3a getC3a() {
        return c3a;
    }
}
