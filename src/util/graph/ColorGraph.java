package util.graph;

import util.graph.*;
import util.intset.*;
import java.util.*;
import java.io.*;


public class ColorGraph {
    public  Graph          G;
    public  int            R;
    public  int            K;
    private Stack<Integer> pile;
    public  IntSet         enleves;
    public  IntSet         deborde;
    public  int[]          couleur;
    public  Node[]         int2Node;
    static  int            NOCOLOR = -1;

    public ColorGraph(Graph G, int K, int[] phi){
	this.G       = G;
	this.K       = K;
	pile         = new Stack<Integer>(); 
	R            = G.nodeCount();
	couleur      = new int[R];

	enleves      = new IntSet(R);
	deborde        = new IntSet(R);

	int2Node     = G.nodeArray();
	for(int v=0; v < R; v++){
	    int preColor = phi[v];
	    if(preColor >= 0 && preColor < K)
		couleur[v] = phi[v];
	    else
		couleur[v] = NOCOLOR;
	}
    }

    /*-------------------------------------------------------------------------------------------------------------*/
    /* associe une couleur à tous les sommets se trouvant dans la pile */
    /*-------------------------------------------------------------------------------------------------------------*/

    public void selection()
    {
        while(!pile.empty()){
            Integer s = pile.pop();
            enleves.remove(s);
            couleur[s] = choisisCouleur(couleursVoisins(s));
        }
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* récupère les couleurs des voisins de t */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public IntSet couleursVoisins(int t)
    {
        IntSet color = new IntSet(K);
        Node node = int2Node[t];

        NodeList nodes = node.succ();

        while(nodes != null){
            if(couleur[nodes.head.mykey] >=0)
                color.add(couleur[nodes.head.mykey]);

            nodes = nodes.tail;
        }

        return color;
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* recherche une couleur absente de colorSet */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public int choisisCouleur(IntSet colorSet)
    {
        for (int i = 0; i < colorSet.getSize(); i++)
            if(!colorSet.isMember(i)) return i;

        return NOCOLOR;
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /* calcule le nombre de voisins du sommet t */
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public int nbVoisins(int t)
    {
        int nbVoisins = 0;
        Node node = int2Node[t];

        NodeList nodes = node.succ();

        while(nodes != null){
            if(!(enleves.isMember(nodes.head.mykey)))
                nbVoisins++;
            nodes = nodes.tail;
        }
        return nbVoisins;
    }

    /*-------------------------------------------------------------------------------------------------------------*/
    /* simplifie le graphe d'interférence g                                                                        */
    /* la simplification consiste à enlever du graphe les temporaires qui ont moins de k voisins                   */
    /* et à les mettre dans une pile                                                                               */
    /* à la fin du processus, le graphe peut ne pas être vide, il s'agit des temporaires qui ont au moins k voisin */
    /*-------------------------------------------------------------------------------------------------------------*/

    public void simplification()
    {
        boolean isModif = true;

        while(pile.size() != R && isModif) {
            isModif = false;

            for(int i = 0; i < R; i++) {
                if(!(pile.contains(i))) {
                    if (nbVoisins(i) < K) {
                        pile.push(i);
                        enleves.add(i);
                        isModif = true;
                    }
                    else {
                        debordement();
                    }
                }
            }
        }
    }
    
    /*-------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------------------*/
    
    public void debordement()
    {

        while(pile.size() != R){
            int som = 0;
            for (int i =0 ;  i < R ; i++) {
                if(!pile.contains(i)){
                    som = i;
                    break;
                }
            }

            pile.push(som);
            enleves.add(som);
            deborde.add(som);
            simplification();
        }

    }


    /*-------------------------------------------------------------------------------------------------------------*/
    /*-------------------------------------------------------------------------------------------------------------*/

    public void coloration()
    {
	this.simplification();
	this.debordement();
	this.selection();
    }

    void affiche()
    {
	System.out.println("vertex\tcolor");
	for(int i = 0; i < R; i++){
	    System.out.println(i + "\t" + couleur[i]);
	}
    }
    
    

}
