package ig;

import fg.*;
import nasm.*;
import util.graph.*;
import util.intset.*;

import java.util.*;
import java.io.*;

public class Ig {
    public Graph graph;
    public ColorGraph colorGraph;
    public FgSolution fgs;
    public int regNb;
    public Nasm nasm;
    public Node int2Node[];
    public int[] color;


    public Ig(FgSolution fgs) {
        this.fgs = fgs;
        this.graph = new Graph();
        this.nasm = fgs.nasm;
        this.regNb = this.nasm.getTempCounter();
        this.int2Node = new Node[regNb];

        this.construction();
        color = getPrecoloredTemporaries();
        colorGraph = new ColorGraph(graph, 3, color);
        color = colorGraph.couleur;
        this.allocateRegisters();
    }


    public void construction() {
        for(int i = 0; i < regNb; i++) {
            int2Node[i] = graph.newNode();
        }
        for (IntSet currentValue : fgs.in.values()) {
            for (int i = 0; i < regNb; i++) {
                for (int j = i + 1; j < regNb; j++) {
                    if (currentValue.isMember(i) && currentValue.isMember(j))
                        graph.addNOEdge(int2Node[i], int2Node[j]);
                }
            }
        }

        for (IntSet currentValue : fgs.out.values()) {
            for (int i = 0; i < regNb; i++) {
                for (int j = i + 1; j < regNb; j++) {
                    if (currentValue.isMember(i) && currentValue.isMember(j))
                        graph.addEdge(int2Node[i], int2Node[j]);
                }
            }
        }

    }

    public int[] getPrecoloredTemporaries() {

        int[] color = new int[regNb];
        for (NasmInst currentInst : nasm.listeInst) {
            if (currentInst.destination != null && currentInst.destination.isGeneralRegister()) {
                NasmRegister reg = (NasmRegister) currentInst.destination;
                if(reg.color != -3)
                    color[reg.val] = reg.color;
            }
            if (currentInst.source != null && currentInst.source.isGeneralRegister()) {
                NasmRegister reg = (NasmRegister) currentInst.source;
                if(reg.color != -3)
                    color[reg.val] = reg.color;
            }
        }

        return color;
    }


    public void allocateRegisters() {
        for (NasmInst currentInst : nasm.listeInst) {
            if (currentInst.destination != null && currentInst.destination.isGeneralRegister()) {
                NasmRegister reg = (NasmRegister) currentInst.destination;
                if(reg.color != -3)
                reg.colorRegister(color[reg.val]);
            }
            if (currentInst.source != null && currentInst.source.isGeneralRegister()) {
                NasmRegister reg = (NasmRegister) currentInst.source;
                if(reg.color != -3)
                reg.colorRegister(color[reg.val]);
            }
        }
    }


    public void affiche(String baseFileName) {
        String fileName;
        PrintStream out = System.out;

        if (baseFileName != null) {
            try {
                baseFileName = baseFileName;
                fileName = baseFileName + ".ig";
                out = new PrintStream(fileName);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        for (int i = 0; i < regNb; i++) {
            Node n = this.int2Node[i];
            out.print(n + " : ( ");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                out.print(q.head.toString());
                out.print(" ");
            }
            out.println(")");
        }
    }
}
	    
    

    
    
