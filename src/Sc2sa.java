import sa.*;
import sc.analysis.DepthFirstAdapter;
import sc.node.*;

public class Sc2sa extends DepthFirstAdapter {

    private SaNode returnValue;

    @Override
    public void caseStart(Start node) {
        super.caseStart(node);
    }

    @Override
    public void caseADecvarldecfoncProgramme(ADecvarldecfoncProgramme node) {
        SaLDec op1;
        SaLDec op2;

        node.getOptdecvar().apply(this);
        op1 = (SaLDec) returnValue;
        node.getListedecfonc().apply(this);
        op2 = (SaLDec) returnValue;
        returnValue = new SaProg(op1, op2);

    }

    @Override
    public void caseALdecfoncProgramme(ALdecfoncProgramme node) {
        super.caseALdecfoncProgramme(node);
    }

    @Override
    public void caseAOptdecvar(AOptdecvar node) {
        super.caseAOptdecvar(node);

    }

    @Override
    public void caseADecvarldecvarListedecvar(ADecvarldecvarListedecvar node) {
        SaDec op1;
        SaLDec op2;

        node.getDecvar().apply(this);
        op1 = (SaDec) returnValue;
        node.getListedecvarbis().apply(this);
        op2 = (SaLDec) returnValue;

        returnValue = new SaLDec(op1, op2);
    }

    @Override
    public void caseADecvarListedecvar(ADecvarListedecvar node) {
        SaDec op1;

        node.getDecvar().apply(this);
        op1 = (SaDec) returnValue;

        returnValue = new SaLDec(op1, null);
    }

    @Override
    public void caseADecvarldecvarListedecvarbis(ADecvarldecvarListedecvarbis node) {
        SaDec op1;
        SaLDec op2;

        node.getDecvar().apply(this);
        op1 = (SaDec) returnValue;
        node.getListedecvarbis().apply(this);
        op2 = (SaLDec) returnValue;

        returnValue = new SaLDec(op1, op2);
    }

    @Override
    public void caseADecvarListedecvarbis(ADecvarListedecvarbis node) {
        SaDec op1;
        SaLDec op2;

        node.getDecvar().apply(this);
        op1 = (SaDec) returnValue;

        returnValue = new SaLDec(op1, null);
    }

    @Override
    public void caseADecvarentierDecvar(ADecvarentierDecvar node) {
        returnValue = new SaDecVar(node.getIdentif().getText());
        super.caseADecvarentierDecvar(node);
    }

    @Override
    public void caseADecvartableauDecvar(ADecvartableauDecvar node) {
        returnValue = new SaDecTab(node.getIdentif().getText(), node.getNombre().getLine());
        super.caseADecvartableauDecvar(node);
    }

    @Override
    public void caseALdecfoncrecListedecfonc(ALdecfoncrecListedecfonc node) {
        SaDec op1;
        SaLDec op2;

        node.getDecfonc().apply(this);
        op1 = (SaDec) returnValue;
        node.getListedecfonc().apply(this);
        op2 = (SaLDec) returnValue;

        returnValue = new SaLDec(op1, op2);
    }

    @Override
    public void caseALdecfoncfinalListedecfonc(ALdecfoncfinalListedecfonc node) {
        returnValue = null;
    }

    @Override
    public void caseADecvarinstrDecfonc(ADecvarinstrDecfonc node) {
        String name;
        SaLDec parametres;
        SaLDec variables;
        SaInstBloc inst;

        name = node.getIdentif().getText();

        node.getListeparam().apply(this);
        parametres = (SaLDec) returnValue;
        node.getOptdecvar().apply(this);
        variables = (SaLDec) returnValue;
        node.getInstrbloc().apply(this);
        inst = (SaInstBloc) returnValue;

        returnValue = new SaDecFonc(name, parametres, variables, inst);
    }

    @Override
    public void caseAInstrDecfonc(AInstrDecfonc node) {
        String name;
        SaLDec parametres;

        SaInstBloc inst;

        name = node.getIdentif().getText();

        node.getListeparam().apply(this);
        parametres = (SaLDec) returnValue;

        node.getInstrbloc().apply(this);
        inst = (SaInstBloc) returnValue;

        returnValue = new SaDecFonc(name, parametres, null, inst);
    }

    @Override
    public void caseASansparamListeparam(ASansparamListeparam node) {
        returnValue = null;
    }

    @Override
    public void caseAAvecparamListeparam(AAvecparamListeparam node) {
        super.caseAAvecparamListeparam(node);
    }

    @Override
    public void caseAInstraffectInstr(AInstraffectInstr node) {
        super.caseAInstraffectInstr(node);
    }

    @Override
    public void caseAInstrblocInstr(AInstrblocInstr node) {
        super.caseAInstrblocInstr(node);
    }

    @Override
    public void caseAInstrsiInstr(AInstrsiInstr node) {
        super.caseAInstrsiInstr(node);
    }

    @Override
    public void caseAInstrtantqueInstr(AInstrtantqueInstr node) {
        super.caseAInstrtantqueInstr(node);
    }

    @Override
    public void caseAInstrappelInstr(AInstrappelInstr node) {
        super.caseAInstrappelInstr(node);
    }

    @Override
    public void caseAInstrretourInstr(AInstrretourInstr node) {
        super.caseAInstrretourInstr(node);
    }

    @Override
    public void caseAInstrecritureInstr(AInstrecritureInstr node) {
        super.caseAInstrecritureInstr(node);
    }

    @Override
    public void caseAInstrvideInstr(AInstrvideInstr node) {
        super.caseAInstrvideInstr(node);
    }

    @Override
    public void caseAInstraffect(AInstraffect node) {
        SaVar var;
        SaExp exp;

        node.getVar().apply(this);
        var = (SaVar) returnValue;
        node.getExp().apply(this);
        exp = (SaExp) returnValue;

        returnValue = new SaInstAffect(var, exp);
    }

    @Override
    public void caseAInstrbloc(AInstrbloc node) {
        SaLInst inst;

        node.getListeinst().apply(this);
        inst = (SaLInst) returnValue;

        returnValue = new SaInstBloc(inst);
    }

    @Override
    public void caseALinstrecListeinst(ALinstrecListeinst node) {
        SaLInst lInst;
        SaInst inst;

        node.getInstr().apply(this);
        inst = (SaInst) returnValue;
        node.getListeinst().apply(this);
        lInst = (SaLInst) returnValue;

        returnValue = new SaLInst(inst, lInst);
    }

    @Override
    public void caseALinstfinalListeinst(ALinstfinalListeinst node) {
        returnValue = null;
    }

    @Override
    public void caseAAvecsinonInstrsi(AAvecsinonInstrsi node) {
        SaExp exp;
        SaInst siInst;
        SaInst sinonInst;

        node.getExp().apply(this);
        exp = (SaExp) returnValue;
        node.getInstrbloc().apply(this);
        siInst = (SaInst) returnValue;
        node.getInstrsinon().apply(this);
        sinonInst = (SaInst) returnValue;

        returnValue = new SaInstSi(exp, siInst, sinonInst);
    }

    @Override
    public void caseASanssinonInstrsi(ASanssinonInstrsi node) {
        SaExp exp;
        SaInst siInst;

        node.getExp().apply(this);
        exp = (SaExp) returnValue;
        node.getInstrbloc().apply(this);
        siInst = (SaInst) returnValue;

        returnValue = new SaInstSi(exp, siInst, null);
    }

    @Override
    public void caseAInstrsinon(AInstrsinon node) {
        super.caseAInstrsinon(node);
    }

    @Override
    public void caseAInstrtantque(AInstrtantque node) {
        SaExp test;
        SaInst faire;

        node.getExp().apply(this);
        test = (SaExp) returnValue;
        node.getInstrbloc().apply(this);
        faire = (SaInst) returnValue;

        returnValue = new SaInstTantQue(test, faire);
    }

    @Override
    public void caseAInstrappel(AInstrappel node) {
        super.caseAInstrappel(node);
    }

    @Override
    public void caseAInstrretour(AInstrretour node) {
        SaExp exp;

        node.getExp().apply(this);
        exp = (SaExp) returnValue;

        returnValue = new SaInstRetour(exp);
    }

    @Override
    public void caseAInstrecriture(AInstrecriture node) {
        SaExp exp;

        node.getExp().apply(this);
        exp = (SaExp) returnValue;
        returnValue = new SaInstEcriture(exp);
    }

    @Override
    public void caseAInstrvide(AInstrvide node) {
        returnValue = null;
    }

    @Override
    public void caseAOuExp(AOuExp node) {
        SaExp exp1;
        SaExp exp2;

        node.getExp().apply(this);
        exp1 = (SaExp) returnValue;
        node.getExp1().apply(this);
        exp2 = (SaExp) returnValue;

        returnValue = new SaExpOr(exp1,exp2);

    }

    @Override
    public void caseAExp1Exp(AExp1Exp node) {
        super.caseAExp1Exp(node);
    }

    @Override
    public void caseAEtExp1(AEtExp1 node) {
        SaExp exp1;
        SaExp exp2;

        node.getExp1().apply(this);
        exp1 = (SaExp) returnValue;
        node.getExp2().apply(this);
        exp2 = (SaExp) returnValue;

        returnValue = new SaExpAnd(exp1,exp2);
    }

    @Override
    public void caseAExp2Exp1(AExp2Exp1 node) {
        super.caseAExp2Exp1(node);
    }

    @Override
    public void caseAInfExp2(AInfExp2 node) {
        SaExp exp1;
        SaExp exp2;

        node.getExp2().apply(this);
        exp1 = (SaExp) returnValue;
        node.getExp3().apply(this);
        exp2 = (SaExp) returnValue;

        returnValue = new SaExpInf(exp1,exp2);
    }

    @Override
    public void caseAEgalExp2(AEgalExp2 node) {
        SaExp exp1;
        SaExp exp2;

        node.getExp2().apply(this);
        exp1 = (SaExp) returnValue;
        node.getExp3().apply(this);
        exp2 = (SaExp) returnValue;

        returnValue = new SaExpEqual(exp1,exp2);
    }

    @Override
    public void caseAExp3Exp2(AExp3Exp2 node) {
        super.caseAExp3Exp2(node);
    }

    @Override
    public void caseAPlusExp3(APlusExp3 node) {
        SaExp op1 = null;
        SaExp op2 = null;
        node.getExp3().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getExp4().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpAdd(op1, op2);
    }

    @Override
    public void caseAMoinsExp3(AMoinsExp3 node) {
        SaExp op1 = null;
        SaExp op2 = null;
        node.getExp3().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getExp4().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpSub(op1, op2);
    }

    @Override
    public void caseAExp4Exp3(AExp4Exp3 node) {
        super.caseAExp4Exp3(node);
    }

    @Override
    public void caseAFoisExp4(AFoisExp4 node) {
        SaExp exp1;
        SaExp exp2;

        node.getExp4().apply(this);
        exp1 = (SaExp) returnValue;
        node.getExp5().apply(this);
        exp2 = (SaExp) returnValue;

        returnValue = new SaExpMult(exp1,exp2);
    }

    @Override
    public void caseADiviseExp4(ADiviseExp4 node) {
        SaExp exp1;
        SaExp exp2;

        node.getExp4().apply(this);
        exp1 = (SaExp) returnValue;
        node.getExp5().apply(this);
        exp2 = (SaExp) returnValue;

        returnValue = new SaExpDiv(exp1,exp2);
    }

    @Override
    public void caseAExp5Exp4(AExp5Exp4 node) {
        super.caseAExp5Exp4(node);
    }

    @Override
    public void caseANonExp5(ANonExp5 node) {
        SaExp exp1;

        node.getExp5().apply(this);
        exp1 = (SaExp) returnValue;

        returnValue = new SaExpNot(exp1);
    }

    @Override
    public void caseAExp6Exp5(AExp6Exp5 node) {
        super.caseAExp6Exp5(node);
    }

    @Override
    public void caseANombreExp6(ANombreExp6 node) {
        returnValue = new SaExpInt(Integer.parseInt(node.getNombre().getText()));
        super.caseANombreExp6(node);
    }

    @Override
    public void caseAAppelfctExp6(AAppelfctExp6 node) {
        SaAppel appel;

        node.getAppelfct().apply(this);
        appel = (SaAppel) returnValue;

        returnValue = new SaExpAppel(appel);
    }

    @Override
    public void caseAVarExp6(AVarExp6 node) {
        SaVar var;

        node.getVar().apply(this);
        var = (SaVar) returnValue;

        returnValue = new SaExpVar(var);
    }

    @Override
    public void caseAParenthesesExp6(AParenthesesExp6 node) {
        super.caseAParenthesesExp6(node);
    }

    @Override
    public void caseALireExp6(ALireExp6 node) {
        returnValue = new SaExpLire();
        super.caseALireExp6(node);
    }

    @Override
    public void caseAVartabVar(AVartabVar node) {
        SaExp exp;

        node.getExp().apply(this);
        exp = (SaExp) returnValue;

        returnValue = new SaVarIndicee(node.getIdentif().getText(), exp);

    }

    @Override
    public void caseAVarsimpleVar(AVarsimpleVar node) {
        returnValue = new SaVarSimple(node.getIdentif().getText());
        super.caseAVarsimpleVar(node);
    }

    @Override
    public void caseARecursifListeexp(ARecursifListeexp node) {
        SaExp exp;
        SaLExp lexp;

        node.getExp().apply(this);
        exp = (SaExp) returnValue;
        node.getListeexpbis().apply(this);
        lexp = (SaLExp) returnValue;

        returnValue = new SaLExp(exp,lexp);
    }

    @Override
    public void caseAFinalListeexp(AFinalListeexp node) {
        returnValue = null;
    }

    @Override
    public void caseARecursifListeexpbis(ARecursifListeexpbis node) {
        SaExp exp;
        SaLExp lexp;

        node.getExp().apply(this);
        exp = (SaExp) returnValue;
        node.getListeexpbis().apply(this);
        lexp = (SaLExp) returnValue;

        returnValue = new SaLExp(exp,lexp);
    }

    @Override
    public void caseAFinalListeexpbis(AFinalListeexpbis node) {
        returnValue = null;
    }

    @Override
    public void caseAAppelfct(AAppelfct node) {

        SaLExp lexp;

        node.getListeexp().apply(this);
        lexp = (SaLExp) returnValue;

        returnValue = new SaAppel(node.getIdentif().getText(),lexp);
    }

    public SaNode getRoot() {
        return returnValue;
    }

}
