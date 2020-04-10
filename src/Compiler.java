import sc.parser.*;
import sc.lexer.*;
import sc.node.*;
import java.io.*;
import sa.*;
import ts.*;
import c3a.*;
import nasm.*;
import fg.*;
import ig.*;

public class Compiler
{
	public static void main(String[] args)
	{
		PushbackReader br = null;
		String baseName = null;
		int RegisterNb = 4;
		try {
			if (0 < args.length) {
				br = new PushbackReader(new FileReader(args[0]));
				baseName = removeSuffix(args[0], ".l");
			}
			else{
				System.out.println("il manque un argument");
				System.exit(1);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Create a Parser instance.
			Parser p = new Parser(new Lexer(br));
			// Parse the input.
			System.out.print("[BUILD SC] ");
			Start tree = p.parse();

			System.out.println("[PRINT SC]");
			tree.apply(new Sc2Xml(baseName));

			System.out.print("[BUILD SA] ");
			Sc2sa sc2sa = new Sc2sa();
			tree.apply(sc2sa);
			SaNode saRoot = sc2sa.getRoot();

			System.out.println("[PRINT SA]");
			new Sa2Xml(saRoot, baseName);

			System.out.print("[BUILD TS] ");
			Ts table = new Sa2ts(saRoot).getTableGlobale();

			System.out.println("[PRINT TS]");
			table.afficheTout(baseName);

			System.out.print("[EXEC SA] ");
			SaEval saEval = new SaEval(saRoot, table);

			System.out.println("[SA OUT]");
			saEval.affiche(baseName);

			System.out.print("[BUILD C3A] ");
			C3a c3a = new Sa2c3a(saRoot).getC3a();

			System.out.print("[PRINT C3A] ");
			c3a.affiche(baseName);

			System.out.println("[C3A OUT]");
			C3aEval c3aEval = new C3aEval(c3a, table);
			c3aEval.affiche(baseName);

			System.out.print("[BUILD PRE NASM] ");
			Nasm nasm = new C3a2nasm(c3a, table).getNasm();
			System.out.println("[PRINT PRE NASM] ");
			nasm.affichePre(baseName);

			System.out.print("[BUILD FG] ");
			Fg fg = new Fg(nasm);
			System.out.print("[PRINT FG] ");
			fg.affiche(baseName);

			System.out.println("[SOLVE FG]");
			FgSolution fgSolution = new FgSolution(nasm, fg);
			fgSolution.affiche(baseName);

			System.out.print("[BUILD IG] ");
			Ig ig = new Ig(fgSolution);

			System.out.print("[PRINT IG] ");
			ig.affiche(baseName);

			System.out.println("[ALLOCATE REGISTERS]");
			ig.allocateRegisters();

			System.out.println("[PRINT NASM]");
			nasm.affiche(baseName);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}


	public static String removeSuffix(final String s, final String suffix)
	{
		if (s != null && suffix != null && s.endsWith(suffix)){
			return s.substring(0, s.length() - suffix.length());
		}
		return s;
	}

}