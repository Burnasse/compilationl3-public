package fg;
import nasm.*;
import util.graph.Node;
import util.graph.NodeList;
import util.intset.*;
import java.io.*;
import java.util.*;

public class FgSolution{
    int iterNum = 0;
    public Nasm nasm;
    Fg fg;
    public Map< NasmInst, IntSet> use;
    public Map< NasmInst, IntSet> def;
    public Map< NasmInst, IntSet> in;
    public Map< NasmInst, IntSet> out;
    
    public FgSolution(Nasm nasm, Fg fg){
	this.nasm = nasm;
	this.fg = fg;
	this.use = new HashMap< NasmInst, IntSet>();
	this.def = new HashMap< NasmInst, IntSet>();
	this.in =  new HashMap< NasmInst, IntSet>();
	this.out = new HashMap< NasmInst, IntSet>();

	initialize();
	createIO();
    }

    private void initialize(){
		for (NasmInst inst : nasm.listeInst) {
			IntSet useIntSet = new IntSet(20);
			IntSet defIntSet = new IntSet(20);

			in.put(inst, new IntSet(20));
			out.put(inst, new IntSet(20));

			if(inst.srcDef && inst.srcUse && inst.source.isGeneralRegister())
				useIntSet.add(((NasmRegister)inst.source).val);

			if(inst.srcDef && inst.source instanceof NasmAddress){
				NasmAddress addr = (NasmAddress) inst.source;

				if(addr.isGeneralRegister())
					useIntSet.add(((NasmRegister)addr.offset).val);
			}

			if(inst.destDef && inst.destUse && inst.destination.isGeneralRegister())
				useIntSet.add(((NasmRegister)inst.destination).val);

			if(inst.destDef && inst.destination.isGeneralRegister())
				defIntSet.add(((NasmRegister)inst.destination).val);

			use.put(inst,useIntSet);
			def.put(inst,defIntSet);
		}
	}

	private void createIO(){

		Map< NasmInst, IntSet> inP = new HashMap<>(in.size());
		Map< NasmInst, IntSet> outP = new HashMap<>(out.size());

		for(NasmInst inst : nasm.listeInst){
			inP.put(inst, new IntSet(20));
			outP.put(inst, new IntSet(20));
		}

		iterNum = -1;
		boolean test;

		do {
			iterNum++;

			for(NasmInst s : nasm.listeInst) {

				inP.replace(s, in.get(s));
				outP.replace(s, out.get(s));

				IntSet useS = use.get(s).copy();
				IntSet outS = out.get(s).copy();
				IntSet defS = def.get(s).copy();

				in.replace(s, useS.union(outS.minus(defS)));

				Node nodes = fg.inst2Node.get(s);
				NodeList succ = nodes.succ();

				while(succ != null) {
					NasmInst succInst = fg.node2Inst.get(succ.head);
					out.replace(succInst, out.get(succInst).union(in.get(s)));
					succ = succ.tail;
				}
			}

			test = true;

			for(NasmInst s : nasm.listeInst) {
				if(!(in.get(s).equal(inP.get(s))))
					test = false;
				if(!(out.get(s).equal(outP.get(s))))
					test = false;
			}
		} while(!test);
	}
    
    public void affiche(String baseFileName){
	String fileName;
	PrintStream out = System.out;
	
	if (baseFileName != null){
	    try {
		baseFileName = baseFileName;
		fileName = baseFileName + ".fgs";
		out = new PrintStream(fileName);
	    }
	    
	    catch (IOException e) {
		System.err.println("Error: " + e.getMessage());
	    }
	}
	
	out.println("iter num = " + iterNum);
	for(NasmInst nasmInst : this.nasm.listeInst){
	    out.println("use = "+ this.use.get(nasmInst) + " def = "+ this.def.get(nasmInst) + "\tin = " + this.in.get(nasmInst) + "\t \tout = " + this.out.get(nasmInst) + "\t \t" + nasmInst);
	}
    }
}

    
