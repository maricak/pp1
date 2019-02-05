package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.mysymboltable.MyTable;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;

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

        Logger log = Logger.getLogger(MJParserTest.class);

        File sourceCode = new File("test/test301.mj");
        if (!sourceCode.exists()) {
            log.error("Source fajl [" + sourceCode.getAbsolutePath() + "] nije pronadjen!");
            return;
        }

        log.info("Prevodjenje source fajla: " + sourceCode.getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
            Yylex lexer = new Yylex(br);
            MJParser parser = new MJParser(lexer);
            Symbol s = parser.parse(); // pocetak parsiranja
            SyntaxNode program = (SyntaxNode) (s.value);

            MyTable.init(); // Universe scope
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            // System.out.println(((Program)prog).toString());
            program.traverseBottomUp(semanticAnalyzer);
            MyTable.dump();

            if (!parser.errorDetected && semanticAnalyzer.passed()) {

                File objFile = new File("test/program.obj");
                log.info("Generating bytecode file: " + objFile.getAbsolutePath());
                if (objFile.exists())
                    objFile.delete();

                //Code generation... 
                CodeGenerator codeGenerator = new CodeGenerator();
                Code.dataSize = semanticAnalyzer.nVars;      
                
                program.traverseBottomUp(codeGenerator);
                
                Code.write(new FileOutputStream(objFile));
                log.info("Parsiranje uspesno zavrseno!");

            } else {
                log.error("Parsiranje NIJE uspesno zavrseno!");
            }
        }
    }
}
