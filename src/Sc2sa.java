import sa.*;
import sc.analysis.DepthFirstAdapter;
import sc.node.*;

public class Sc2sa extends DepthFirstAdapter {

    private SaNode returnValue;

    public void caseAProgramme(AProgramme node) {
        super.caseAProgramme(node);
    }

    public void caseAOuExpression(AOuExpression node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getEt().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpOr(op1, op2);
    }

    public void caseAEtExpression(AEtExpression node) {
        // je sais pas
    }

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
        // je sais pas
    }

    public void caseAInfInfequal(AInfInfequal node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getInfequal().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMoinsplus().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpInf(op1, op2);
    }

    public void caseAEqInfequal(AEqInfequal node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getInfequal().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMoinsplus().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpEqual(op1, op2);
    }

    public void caseASubInfequal(ASubInfequal node) {
        // je sais pas
    }

    public void caseAAddMoinsplus(AAddMoinsplus node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMoinsplus().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMultdiv().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpAdd(op1, op2);
    }

    public void caseASousMoinsplus(ASousMoinsplus node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMoinsplus().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getMultdiv().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpSub(op1, op2);
    }

    public void caseAMultdivMoinsplus(AMultdivMoinsplus node) {

        // j'sais pas
    }

    public void caseAMultMultdiv(AMultMultdiv node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMultdiv().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getNon().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpMult(op1, op2);
    }

    public void caseADivMultdiv(ADivMultdiv node) {
        SaExp op1 = null;
        SaExp op2 = null;

        node.getMultdiv().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getNon().apply(this);
        op2 = (SaExp) this.returnValue;
        this.returnValue = new SaExpDiv(op1, op2);
    }

    public void caseANonMultdiv(ANonMultdiv node) {
        // je sais pas
    }

    public void caseANoNon(ANoNon node) {
        SaExp op1 = null;

        node.getNon().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpNot(op1);
    }

    public void caseAParNon(AParNon node) {
        // je sais pas
    }

    public void caseAParParentheses(AParParentheses node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        //this.returnValue = new SaLExp()
    }

    public void caseANbParentheses(ANbParentheses node) {
        node.getNombre().apply(this);
        this.returnValue = new SaExpInt(node.getNombre().getLine());
    }

    public void caseAIdParentheses(AIdParentheses node) {
        node.getIdentificateur().apply(this);
        this.returnValue = new SaExpVar(new SaVarSimple(node.getIdentificateur().getText()));
    }

    public void caseATabParentheses(ATabParentheses node) {
        node.getTableau().apply(this);
        this.returnValue = new SaExpVar(new SaVarSimple(node.getTableau().toString()));
        // surement pas ca
    }

    public void caseAFctParentheses(AFctParentheses node) {
        // je sais pas
    }

    public void caseAListListexpression(AListListexpression node) {
        SaExp op1 = null;
        SaLExp op2 = null;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getListexpression().apply(this);
        op2 = (SaLExp) this.returnValue;
        this.returnValue = new SaLExp(op1, op2);
    }

    public void caseAUniqueListexpression(AUniqueListexpression node) {
        SaExp op1 = null;
        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaLExp(op1, null);
        //pas sur
    }

    public void caseAListglodeclarGlobdeclaration(AListglodeclarGlobdeclaration node) {

        SaLDec op1 = null;

        node.getListedeclaration().apply(this);
        op1 = (SaLDec) this.returnValue;
        //this.returnValue = new SaLDec(op1)
        // je sais pas
    }

    public void caseAVideTableau(AVideTableau node) {
        SaDecVar op1;

        node.getIdentificateur().apply(this);
        op1 = (SaDecVar)this.returnValue;
        this.returnValue = new SaDecTab(op1.getNom(),0);
    }

    public void caseANonvideTableau(ANonvideTableau node) {
        SaDecVar op1;

        node.getIdentificateur().apply(this);
        op1 = (SaDecVar)this.returnValue;
        node.getNombre().apply(this);
        this.returnValue = new SaDecTab(op1.getNom(),node.getNombre().getLine());
        //pas sur
    }

    public void caseAVarDeclaration(AVarDeclaration node) {
        SaDecVar op1;

        node.getIdentificateur().apply(this);
        op1 = (SaDecVar) this.returnValue;
        this.returnValue = new SaDecVar(op1.getNom());
    }

    public void caseATabDeclaration(ATabDeclaration node) {
        SaDecTab op1;

        node.getTableau().apply(this);
        op1 = (SaDecTab) this.returnValue;
        this.returnValue = new SaDecTab(op1.getNom(),op1.getTaille());
    }

    public void caseAListListedeclaration(AListListedeclaration node) {
        SaDec op2;
        SaLDec op1;

        node.getDeclaration().apply(this);
        op2 = (SaDec) this.returnValue;
        node.getListedeclaration().apply(this);
        op1 = (SaLDec) this.returnValue;
        this.returnValue = new SaLDec(op2,op1);
    }

    public void caseADeclarListedeclaration(ADeclarListedeclaration node) {
        SaDec op1;

        node.getDeclaration().apply(this);
        op1 = (SaDec) this.returnValue;
        this.returnValue = new SaDecVar(op1.getNom());
        //pas sur
    }

    public void caseAListedeclaration(AListedeclaration node) {
        super.caseAListedeclaration(node);
    }

    public void caseAFctecrire(AFctecrire node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaInstEcriture(op1);
    }

    public void caseAFctlire(AFctlire node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaExpLire();
    }

    public void caseAExpFctretour(AExpFctretour node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaInstRetour(op1);
    }

    public void caseAIdFctretour(AIdFctretour node) {
        SaExp op1;

        node.getIdentificateur().apply(this);
        op1 = (SaExp) this.returnValue;
        this.returnValue = new SaInstRetour(op1);
    }

    public void caseAEcrireFonction(AEcrireFonction node) {
        // je sais pas
    }

    public void caseALireFonction(ALireFonction node) {
        // je sais pas
    }

    public void caseAFctFonction(AFctFonction node) {
        SaDec op1;
        SaLExp op2;

        node.getIdentificateur().apply(this);
        op1 = (SaDec) this.returnValue;
        node.getListexpression().apply(this);
        op2 = (SaLExp) this.returnValue;

        this.returnValue = new SaAppel(op1.getNom(),op2);
    }

    public void caseASi(ASi node) {
        SaExp op1;
        SaLInst op2;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getListinstruction().apply(this);
        op2 = (SaLInst) this.returnValue;

        this.returnValue = new SaInstSi(op1,new SaInstBloc(op2),null);


    }

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

    public void caseASiCondition(ASiCondition node) {
        //je sais pas
    }

    public void caseASinonCondition(ASinonCondition node) {
        // je sais toujours pas
    }

    public void caseABoucle(ABoucle node) {
        SaExp op1;
        SaLInst op2;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;
        node.getListinstruction().apply(this);
        op2 = (SaLInst) this.returnValue;

        this.returnValue = new SaInstTantQue(op1,new SaInstBloc(op2));
    }

    public void caseAConditionInstruction(AConditionInstruction node) {
        //chepoi
    }

    public void caseABoucleInstruction(ABoucleInstruction node) {
        //chepoi2
    }

    public void caseARetourInstruction(ARetourInstruction node) {
        //chepoi3
    }

    public void caseAAssignInstruction(AAssignInstruction node) {
        SaExp op1;

        node.getExpression().apply(this);
        op1 = (SaExp) this.returnValue;

        //this.returnValue = new SaInstAffect()
        // Peut être refaire ce morceau de la grammaire
    }

    public void caseAListinstrListinstruction(AListinstrListinstruction node) {
        SaInst op1;
        SaLInst op2;

        node.getInstruction().apply(this);
        op1 = (SaInst) this.returnValue;
        node.getListinstruction().apply(this);
        op2 = (SaLInst) this.returnValue;

        this.returnValue = new SaLInst(op1,op2);
    }

    public void caseAInstrListinstruction(AInstrListinstruction node) {

        node.getInstruction().apply(this);
        //rien trouvé d'autre
    }

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

    public void caseAListfctListefonction(AListfctListefonction node) {
        //Chepoi jpp


    }

    public void caseAFctListefonction(AFctListefonction node) {


        node.getDeclarefonction().apply(this);
        //dpelsiger etupalsiger

    }

}
