package compiler.semanal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import compiler.abstree.tree.AbsProgram;
import compiler.abstree.tree.AbsTree;
import compiler.lexanal.PascalLex;
import compiler.report.Report;
import compiler.report.XML;
import compiler.synanal.PascalSyn;

public class Main {

	/** Abstraktno sintaksno drevo prevajanega programa. */
	public static AbsTree absTree = null;

	/**
	 * Izvede prevajanje do faze semanticne analize.
	 */
	public static void exec() {
		/* Odpremo vhodno in izhodno datoteko. */
		FileReader srcFile = null;
		String srcName = compiler.Main.prgName + ".pascal";
		try {
			srcFile = new FileReader(srcName);
		} catch (FileNotFoundException _) {
			Report.error("Source file '" + srcName + "' cannot be opened.", 1);
		}
		PrintStream xml = XML.open("semanal");

		PascalLex lexer = new PascalLex(srcFile);
		PascalSyn parser = new PascalSyn(lexer);
		AbsProgram program = null;
		try {
			program = (AbsProgram) (parser.parse().value);
		} catch (Exception ex) {
			Report.error("Uncaught syntax error.", 1);
		}
		SemNameResolver nameResolver = new SemNameResolver();
		SemTypeChecker typeChecker = new SemTypeChecker();
		ConstEvaluator constEvaluator = new ConstEvaluator();
		program.accept(nameResolver);
		program.accept(constEvaluator);
		program.accept(typeChecker);
		program.accept(new SemPrintXML(xml));

		/* Zapremo obe datoteki. */
		XML.close("semanal", xml);
		try {
			srcFile.close();
		} catch (IOException _) {
			Report.error("Source file '" + srcName + "' cannot be closed.", 1);
		}
		
		if (nameResolver.error || typeChecker.error) {
			Report.error("Too many errors.", 1);
		}
	}
}
