import sa.*;
import ts.Ts;
import ts.TsItemFct;
import ts.TsItemVar;

public class Sa2ts extends SaDepthFirstVisitor <Void> {

    private Ts table = new Ts();

    public Sa2ts(SaNode saRoot) {
        saRoot.accept(this);
    }

    @Override
    public Void visit(SaDecTab node) {
        if (/*item.isParam  || */table.variables.containsKey(node.getNom()))
            System.out.println(node.getNom() + " n'existe pas");

        table.addVar(node.getNom(), node.getTaille());

        node.tsItem = new TsItemVar(node.getNom(),node.getTaille());
        return null;
    }

    @Override
    public Void visit(SaDecFonc node) {
        node.getCorps().accept(this);

        if (table.fonctions.containsKey(node.getNom()))
            System.out.println("La fonction " + node.getNom() + " n'existe pas");

        if(node.getParametres() == null){
            table.addFct(node.getNom(), 0, new Ts(), node);
            node.tsItem = new TsItemFct(node.getNom(), 0, table, node);
        }
        else{
            table.addFct(node.getNom(), node.getParametres().length(), new Ts(), node);
            node.tsItem = new TsItemFct(node.getNom(), node.getParametres().length(), table, node);
        }

        return null;
    }

    @Override
    public Void visit(SaDecVar node) {
        if (table.variables.containsKey(node.getNom()))
            System.out.println(node.getNom() + " n'existe pas");

        table.addVar(node.getNom(), 1);
        node.tsItem = new TsItemVar(node.getNom(),1);
        node.tsItem.isParam = true;

        return null;
    }

    @Override
    public Void visit(SaVarSimple node) {
        table.addVar(node.getNom(),1);
        node.tsItem = new TsItemVar(node.getNom(),1);

        return null;
    }

    @Override
    public Void visit(SaAppel node) {

        if (!table.fonctions.containsKey(node.getNom()))
            System.out.println("La fonction " + node.getNom() + " n'existe pas");
        if (table.fonctions.containsKey("main") && table.getFct("main").nbArgs == 0)
            System.out.println("main invalide: trop d'arguments");


        int size;

        if(node.getArguments() != null)
            size = node.getArguments().length();
        else
            size = 0;

        node.tsItem = new TsItemFct(
                node.getNom(),
                size,
                table,
                table.getFct(node.getNom()).saDecFonc);

        return null;
    }

    @Override
    public Void visit(SaVarIndicee node) {
        node.getIndice().accept(this);

        if(table.getVar(node.getNom()) == null)
            System.out.println(node.getNom() + " n'existe pas");

        node.tsItem = new TsItemVar(node.getNom(),1);
        return  null;
    }



    public Ts getTableGlobale() {
        return table;
    }
}