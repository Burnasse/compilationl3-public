import sa.*;
import ts.Ts;
import ts.TsItemFct;
import ts.TsItemVar;

public class Sa2ts <T> extends SaDepthFirstVisitor<T> {
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
    public T visit(SaDecTab node) {
        System.out.println("1");

        if (/*item.isParam  || */table.variables.containsKey(node.getNom()))
            return super.visit(node);

        table.addVar(node.getNom(), node.getTaille());

        TsItemVar itemVar = new TsItemVar(node.getNom(),node.getTaille());
        return (T) itemVar;
    }

    @Override
    public T visit(SaDecFonc node) {

        if (table.fonctions.containsKey(node.getNom()))
            return super.visit(node);

        TsItemFct fct = new TsItemFct(node.getNom(),1,table,node);

        table.addFct(node.getNom(), 1, table, node);

        return (T) fct;
    }

    @Override
    public T visit(SaDecVar node) {
        System.out.println("2");
        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);

        table.addVar(node.getNom(), 1);


        TsItemVar itemVar = new TsItemVar(node.getNom(),1);
        return (T) itemVar;
    }

    @Override
    public T visit(SaVarSimple node) {
        System.out.println("3");

        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);

        for (TsItemFct fct : table.fonctions.values()) {
            if (fct.getTable().variables.containsKey(node.getNom()))
                return super.visit(node);
        }

        if (table.variables.containsKey(node.getNom()))
            return super.visit(node);

        TsItemVar itemVar = new TsItemVar(node.getNom(),1);
        return (T) itemVar;
    }

    @Override
    public T visit(SaAppel node) {
        System.out.println("4");
        if (!table.fonctions.containsKey(node.getNom()))
            return super.visit(node);
        if (node.getArguments().length() != table.getFct(node.getNom()).nbArgs)
            return super.visit(node);
        if (table.fonctions.containsKey("main") && table.getFct("main").nbArgs == 0)
            return super.visit(node);

        return super.visit(node);
    }

    @Override
    public T visit(SaVarIndicee node) {
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

        TsItemVar itemVar = new TsItemVar(node.getNom(),1);
        return (T) itemVar;
    }

    public Ts getTableGlobale() {
        return table;
    }
}