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

    @Override
    public Void visit(SaDecTab node) {
        TsItemVar item = node.tsItem;

        if(item.isParam || table.variables.containsKey(item.identif))
            return null;

        table.addVar(item.identif,item.taille);

        return super.visit(node);
    }

    @Override
    public Void visit(SaDecFonc node) {
        TsItemFct item = node.tsItem;

        if(table.fonctions.containsKey(item.identif))
            return null;
        table.addFct(item.identif,item.nbArgs,item.getTable(),item.saDecFonc);
        return super.visit(node);
    }

    @Override
    public Void visit(SaDecVar node) {
        TsItemVar item = node.tsItem;
        if(item.portee.variables.containsKey(item.identif))
            return null;

        table.addVar(item.identif,item.taille);
        return super.visit(node);
    }

    @Override
    public Void visit(SaVarSimple node) {
        TsItemVar item = node.tsItem;

        if(item.portee.variables.containsKey(item.identif))
           return super.visit(node);

        for(TsItemFct fct : item.portee.fonctions.values()){
            if(fct.getTable().variables.containsKey(item.identif))
                return super.visit(node);
        }

        if(table.variables.containsKey(item.identif))
            return super.visit(node);

        return null;
    }

    @Override
    public Void visit(SaAppel node) {
        TsItemFct item = node.tsItem;
        if(!table.fonctions.containsKey(item.identif))
            return null;
        if(item.nbArgs != table.getFct(item.identif).nbArgs)
            return null;
        if(table.fonctions.containsKey("main") && table.getFct("main").nbArgs == 0)
            return super.visit(node);

        return null;
    }

    @Override
    public Void visit(SaVarIndicee node) {
        TsItemVar item = node.tsItem;

        if(node.getIndice() == null)
            return null;

        if(item.portee.variables.containsKey(item.identif))
            return super.visit(node);

        for(TsItemFct fct : item.portee.fonctions.values()){
            if(fct.getTable().variables.containsKey(item.identif))
                return super.visit(node);
        }

        if(table.variables.containsKey(item.identif))
            return super.visit(node);

        return null;
    }
}