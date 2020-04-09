package fg;
import nasm.*;
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

		for (NasmInst inst : nasm.listeInst) {
			in.put(inst, new IntSet(nasm.getTempCounter()));
			out.put(inst, new IntSet(nasm.getTempCounter()));
			use.put(inst, new IntSet(nasm.getTempCounter()));
			def.put(inst, new IntSet(nasm.getTempCounter()));

			if (inst.srcUse)
				add(inst.source, use.get(inst));
			if (inst.destUse)
				add(inst.destination, use.get(inst));
			if (inst.destDef)
				add(inst.destination, def.get(inst));
		}
    }

    public void add(NasmOperand operand, IntSet intSet) {
		if (operand.isGeneralRegister())
			intSet.add(((NasmRegister) operand).val);

		if (operand instanceof NasmAddress) {
			NasmAddress addr = (NasmAddress) operand;
			if (addr.base.isGeneralRegister())
				intSet.add(((NasmRegister) addr.base).val);
			if (addr.offset != null && addr.offset.isGeneralRegister())
				intSet.add(((NasmRegister) addr.offset).val);
		}
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

    
