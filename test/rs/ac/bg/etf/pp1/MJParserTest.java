package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	static Struct boolStruct = new Struct(Struct.Bool);
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(MJParserTest.class);
		
		Reader br = null;
		try {
			String sourceFileName = "program";
			File sourceCode = new File("test/" + sourceFileName + ".mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);

			MJParser p = new MJParser(lexer);
			Symbol s = p.parse(); // pocetak parsiranja

			Program prog = (Program) (s.value);
			Tab.init();
			Tab.insert(Obj.Type, "bool", boolStruct);
			// ispis sintaksnog stabla
			//log.info(prog.toString(""));
			log.info("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer(boolStruct);
			prog.traverseBottomUp(v);
			NonDumbSymbolTableVisitor visitor = new NonDumbSymbolTableVisitor();
			log.info("===================================");
			
			Tab.dump(visitor);
			
			if (!p.errorDetected && v.passed()) {
				log.info("Parsiranje uspesno zavrseno!");
				File objFile = new File("test/program.obj");
				if(objFile.exists()) objFile.delete();
				CodeGenerator codeGenerator = new CodeGenerator(v.reg0, v.reg1, v.reg2, v.reg3);
				prog.traverseBottomUp(codeGenerator);
				
				Code.dataSize = v.nVars;
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
				
				
			} else {
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}

		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
		}

	}

}