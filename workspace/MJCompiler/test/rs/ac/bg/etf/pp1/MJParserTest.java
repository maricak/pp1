/*package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;*/

package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.mysymboltable.MyTable;
import rs.ac.bg.etf.pp1.util.Log4JUtils;


public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * Logger log = Logger.getLogger(MJParserTest.class);
		 * 
		 * Reader br = null; try { File sourceCode = new File("test/program.mj");
		 * log.info("Compiling source file: " + sourceCode.getAbsolutePath());
		 * 
		 * br = new BufferedReader(new FileReader(sourceCode)); Yylex lexer = new
		 * Yylex(br);
		 * 
		 * MJParser p = new MJParser(lexer); Symbol s = p.parse(); //pocetak parsiranja
		 * 
		 * Program prog = (Program)(s.value); // ispis sintaksnog stabla
		 * log.info(prog.toString("")); log.info("===================================");
		 * 
		 * // ispis prepoznatih programskih konstrukcija //RuleVisitor v = new
		 * RuleVisitor(); //prog.traverseBottomUp(v);
		 * 
		 * //log.info(" Print count calls = " + v.printCallCount);
		 * 
		 * //log.info(" Deklarisanih promenljivih ima = " + v.varDeclCount);
		 * 
		 * } finally { if (br != null) try { br.close(); } catch (IOException e1) {
		 * log.error(e1.getMessage(), e1); } }
		 */
		Logger log = Logger.getLogger(MJParserTest.class);

		/*if (args.length < 1) {
			log.error("Nema dovoljno argumenata! Usage: MJParser <source-file>");
			return;
		}*/
		
		File sourceCode = new File("test/stabla.mj");
		if (!sourceCode.exists()) {
			log.error("Source fajl [" + sourceCode.getAbsolutePath() + "] nije pronadjen!");
			return;
		}
			
		log.info("Prevodjenje source fajla: " + sourceCode.getAbsolutePath());
		
		try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
			Yylex lexer = new Yylex(br);
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        SyntaxNode prog = (SyntaxNode)(s.value);
	        
			MyTable.init(); // Universe scope
			SemanticAnalyzer analyzer = new SemanticAnalyzer();
			prog.traverseBottomUp(analyzer);
	        //log.info("Print calls = " + semanticCheck.printCallCount);
			MyTable.dump();
	        
	        if (!p.errorDetected && analyzer.passed()) {
				/*
				 * File objFile = new File(args[1]); log.info("Generating bytecode file: " +
				 * objFile.getAbsolutePath()); if (objFile.exists()) objFile.delete();
				 * 
				 * // Code generation... CodeGenerator codeGenerator = new CodeGenerator();
				 * prog.traverseBottomUp(codeGenerator); Code.dataSize = semanticCheck.nVars;
				 * Code.mainPc = codeGenerator.getMainPc(); Code.write(new
				 * 	FileOutputStream(objFile)); 
				 */
	        	log.info("Parsiranje uspesno zavrseno!");
				 
	        }
	        else {
	        	log.error("Parsiranje NIJE uspesno zavrseno!");
	        }
		}
	}
}
