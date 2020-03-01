import sa.*;
import ts.Ts;
import ts.TsItemFct;
import ts.TsItemVar;

public class Sa2ts extends SaDepthFirstVisitor <Void> {
    /*
    Hash ->
            fonctions -> noms variables | TsItemVar
                         OU
            variables -> noms fonctions | TsItemFct


     adrVarCourants -> taille occupé var locales (pour tables locales)
     adrArgCourant -> taille occupé paramètre fonction (pour tables locales)
    */

    Ts table = new Ts();



    public Sa2ts(SaNode saRoot) {
        saRoot.accept(this);
    }

    @Override
    public Void visit(SaDecTab node) {
        System.out.println("1");

        if (/*item.isParam  || */table.variables.containsKey(node.getNom()))
            return super.visit(node);

        table.addVar(node.getNom(), node.getTaille());

        TsItemVar itemVar = new TsItemVar(node.getNom(),node.getTaille());
        return null;
    }

    @Override
    public Void visit(SaDecFonc node) {
        System.out.println("salutm");

        node.getCorps().accept(this);

        if (table.fonctions.containsKey(node.getNom()))
            return super.visit(node);



        if(node.getParametres() == null)
            table.addFct(node.getNom(), 0, new Ts(), node);
        else
            table.addFct(node.getNom(), node.getParametres().length(), new Ts(), node);




        return null;
    }

    @Override
    public Void visit(SaDecVar node) {
        System.out.println("2");
        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);

        table.addVar(node.getNom(), 1);

        return null;
    }

    @Override
    public Void visit(SaVarSimple node) {
        System.out.println("3");

        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);

        for (TsItemFct fct : table.fonctions.values()) {
            if (fct.getTable().variables.containsKey(node.getNom()))
                return super.visit(node);
        }

        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);

        table.addVar(node.getNom(),1);

        return null;
    }

    @Override
    public Void visit(SaAppel node) {
        System.out.println("4");

        if (!table.fonctions.containsKey(node.getNom()))
            return super.visit(node);
        if (node.getArguments().length() != table.getFct(node.getNom()).nbArgs)
            return super.visit(node);
        if (table.fonctions.containsKey("main") && table.getFct("main").nbArgs == 0)
            return super.visit(node);

        return null;
    }

    @Override
    public Void visit(SaVarIndicee node) {
        System.out.println("5");

        if (node.getIndice() == null)
            return super.visit(node);

        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);

        for (TsItemFct fct : table.fonctions.values()) {
            if (fct.getTable().variables.containsKey(node.getNom()))
                return super.visit(node);
        }

        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);


        return  null;
    }

    public Ts getTableGlobale() {
        return table;
    }
}