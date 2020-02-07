import sa.*;
import sc.analysis.DepthFirstAdapter;
import sc.node.*;

public class Sc2sa2 extends DepthFirstAdapter {

    private SaNode returnValue;

    @Override
    public void caseStart(Start node) {
        super.caseStart(node);
    }

    @Override
    public void caseAProgramme(AProgramme node) {
        SaLDec vars;
        SaLDec fonctions;

        node.getGlobdeclaration().apply(this);
        vars = (SaLDec) this.returnValue;
        node.getListefonction().apply(this);
        fonctions = (SaLDec) this.returnValue;
        this.returnValue = new SaProg(vars, fonctions);
    }

    @Override
    public void caseAOuExpression(AOuExpression node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getEt().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpOr(op1, op2);
    }

    @Override
    public void caseAEtExpression(AEtExpression node) {
        super.caseAEtExpression(node);
    }

    @Override
    public void caseAEtEt(AEtEt node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getEt().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getInfequal().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpAdd(op1, op2);
    }

    public void caseAInfEt(AInfEt node) {
        super.caseAInfEt(node);
    }

    @Override
    public void caseAInfInfequal(AInfInfequal node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getInfequal().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMoinsplus().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpInf(op1, op2);
    }

    @Override
    public void caseAEqInfequal(AEqInfequal node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getInfequal().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMoinsplus().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpEqual(op1, op2);
    }

    @Override
    public void caseASubInfequal(ASubInfequal node) {
        super.caseASubInfequal(node);
    }

    @Override
    public void caseAAddMoinsplus(AAddMoinsplus node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMoinsplus().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMultdiv().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpAdd(op1, op2);
    }

    @Override
    public void caseASousMoinsplus(ASousMoinsplus node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMoinsplus().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMultdiv().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpSub(op1, op2);
    }

    @Override
    public void caseAMultdivMoinsplus(AMultdivMoinsplus node) {

        super.caseAMultdivMoinsplus(node);
    }

    @Override
    public void caseAMultMultdiv(AMultMultdiv node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMultdiv().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getNon().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpMult(op1, op2);
    }

    @Override
    public void caseADivMultdiv(ADivMultdiv node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMultdiv().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getNon().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpDiv(op1, op2);
    }

    @Override
    public void caseANonMultdiv(ANonMultdiv node) {
        super.caseANonMultdiv(node);
    }

    public void caseANoNon(ANoNon node) {
        SaExp op1 = null;

        node.getNon().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpNot(op1);
    }

    @Override
    public void caseAParNon(AParNon node) {
        super.caseAParNon(node);
    }

    @Override
    public void caseAParParentheses(AParParentheses node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        //this.returnValue = new SaLExp()
    }

    @Override
    public void caseANbParentheses(ANbParentheses node) {
        node.getNombre().apply(this);
        this.returnValue = new SaExpInt(node.getNombre().getLine());
    }

    @Override
    public void caseAIdParentheses(AIdParentheses node) {
        node.getIdentificateur().apply(this);
        this.returnValue = new SaExpVar(new SaVarSimple(node.getIdentificateur().getText()));
    }

    @Override
    public void caseATabParentheses(ATabParentheses node) {
        node.getTableau().apply(this);
        this.returnValue = new SaExpVar(new SaVarSimple(node.getTableau().toString()));
        // surement pas ca
    }

    @Override
    public void caseAFctParentheses(AFctParentheses node) {
        super.caseAFctParentheses(node);
    }

    @Override
    public void caseAListListexpression(AListListexpression node) {
        SaExp op1 = null;
        SaLExp op2 = null;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getListexpression().apply(this);
        op2 = (SaLExp) this.returnValue;
        this.returnValue = new SaLExp(op1, op2);
    }

    @Override
    public void caseAUniqueListexpression(AUniqueListexpression node) {
        SaExp op1 = null;
        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaLExp(op1, null);
        //pas sur
    }

    @Override
    public void caseAListglodeclarGlobdeclaration(AListglodeclarGlobdeclaration node) {

        SaLDec op1 = null;

        node.getListedeclaration().apply(this);
        op1 = (SaLDec) this.returnValue;
        //this.returnValue = new SaLDec(op1)
        // je sais pas
    }

    @Override
    public void caseAVideTableau(AVideTableau node) {
        SaDecVar op1;

        node.getIdentificateur().apply(this);
        op1 = (SaDecVar)this.returnValue;
        this.returnValue = new SaDecTab(op1.getNom(),0);
    }

    @Override
    public void caseANonvideTableau(ANonvideTableau node) {
        SaDecVar op1;

        node.getIdentificateur().apply(this);
        op1 = (SaDecVar)this.returnValue;
        node.getNombre().apply(this);
        this.returnValue = new SaDecTab(op1.getNom(),node.getNombre().getLine());
        //pas sur
    }

    @Override
    public void caseAVarDeclaration(AVarDeclaration node) {
        SaDecVar op1;

        node.getIdentificateur().apply(this);
        op1 = (SaDecVar) this.returnValue;
        this.returnValue = new SaDecVar(op1.getNom());
    }

    @Override
    public void caseATabDeclaration(ATabDeclaration node) {
        SaDecTab op1;

        node.getTableau().apply(this);
        op1 = (SaDecTab) this.returnValue;
        this.returnValue = new SaDecTab(op1.getNom(),op1.getTaille());
    }

    @Override
    public void caseAListListedeclaration(AListListedeclaration node) {
        SaDec op2;
        SaLDec op1;

        node.getDeclaration().apply(this);
        op2 = (SaDec) this.returnValue;
        node.getListedeclaration().apply(this);
        op1 = (SaLDec) this.returnValue;
        this.returnValue = new SaLDec(op2,op1);
    }

    @Override
    public void caseADeclarListedeclaration(ADeclarListedeclaration node) {
        SaDec op1;

        node.getDeclaration().apply(this);
        op1 = (SaDec) this.returnValue;
        this.returnValue = new SaDecVar(op1.getNom());
        //pas sur
    }

    @Override
    public void caseAListedeclaration(AListedeclaration node) {
        super.caseAListedeclaration(node);
    }

    @Override
    public void caseAFctecrire(AFctecrire node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaInstEcriture(op1);
    }

    @Override
    public void caseAFctlire(AFctlire node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpLire();
    }

    @Override
    public void caseAExpFctretour(AExpFctretour node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaInstRetour(op1);
    }

    @Override
    public void caseAIdFctretour(AIdFctretour node) {
        SaExp op1;

        node.getIdentificateur().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaInstRetour(op1);
    }

    @Override
    public void caseAEcrireFonction(AEcrireFonction node) {
        super.caseAEcrireFonction(node);
    }

    @Override
    public void caseALireFonction(ALireFonction node) {
        super.caseALireFonction(node);
    }

    @Override
    public void caseAFctFonction(AFctFonction node) {
        SaDec op1;
        SaLExp op2;

        node.getIdentificateur().apply(this);
        op1 = (SaDec) this.returnValue;
        node.getListexpression().apply(this);
        op2 = (SaLExp) this.returnValue;

        this.returnValue = new SaAppel(op1.getNom(),op2);
    }

    @Override
    public void caseASi(ASi node) {
        SaExp op1;
        SaLInst op2;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getListinstruction().apply(this);
        op2 = (SaLInst) this.returnValue;

        this.returnValue = new SaInstSi(op1,new SaInstBloc(op2),null);


    }

    @Override
    public void caseASinon(ASinon node) {
        SaExp op1;
        SaLInst op2;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getListinstruction().apply(this);
        op2 = (SaLInst) this.returnValue;
        //this.returnValue = new SaInstSi(op1,op2,null);
        // C'est complexe
    }

    @Override
    public void caseASiCondition(ASiCondition node) {
        super.caseASiCondition(node);
    }

    @Override
    public void caseASinonCondition(ASinonCondition node) {
        super.caseASinonCondition(node);
    }

    @Override
    public void caseABoucle(ABoucle node) {
        SaExp op1;
        SaLInst op2;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getListinstruction().apply(this);
        op2 = (SaLInst) this.returnValue;

        this.returnValue = new SaInstTantQue(op1,new SaInstBloc(op2));
    }

    @Override
    public void caseAConditionInstruction(AConditionInstruction node) {
        super.caseAConditionInstruction(node);
    }

    @Override
    public void caseABoucleInstruction(ABoucleInstruction node) {
        super.caseABoucleInstruction(node);
    }

    @Override
    public void caseARetourInstruction(ARetourInstruction node) {
        super.caseARetourInstruction(node);
    }

    @Override
    public void caseAAssignInstruction(AAssignInstruction node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;

        //this.returnValue = new SaInstAffect()
        // Peut être refaire ce morceau de la grammaire
    }

    @Override
    public void caseAListinstrListinstruction(AListinstrListinstruction node) {
        SaInst op1;
        SaLInst op2;

        node.getInstruction().apply(this);
        op1 = (SaInst) this.returnValue;
        node.getListinstruction().apply(this);
        op2 = (SaLInst) this.returnValue;

        this.returnValue = new SaLInst(op1,op2);
    }

    @Override
    public void caseAInstrListinstruction(AInstrListinstruction node) {
        super.caseAInstrListinstruction(node);
    }

    @Override
    public void caseADeclarefonction(ADeclarefonction node) {
        SaDec op1;
        SaLDec op2;
        SaLDec op3;
        SaLInst op4;

        node.getIdentificateur().apply(this);
        op1 = (SaDec) this.returnValue;
        node.getListdclr1().apply(this);
        op2 = (SaLDec) this.returnValue;
        node.getListdclr2().apply(this);
        op3 = (SaLDec) this.returnValue;
        node.getListinstruction().apply(this);
        op4 = (SaLInst) this.returnValue;

        this.returnValue = new SaDecFonc(op1.getNom(),op2,op3,new SaInstBloc(op4));
    }

    @Override
    public void caseAListfctListefonction(AListfctListefonction node) {
        super.caseAListfctListefonction(node);
    }

    @Override
    public void caseAFctListefonction(AFctListefonction node) {


        node.getDeclarefonction().apply(this);
        //dpelsiger etupalsiger

    }

    public SaNode getRoot() {
        return returnValue;
    }
}