package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class Compiler {
	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	static final String defaultInputFileName = "test/test9_map.mj";
	static Struct boolStruct = new Struct(Struct.Bool);

	public static void tsdump() {
		SymbolTableVisitor visitor = new DumpSymbolTableVisitor();
		Tab.dump(visitor);
	}

	public static void main(String[] args) throws Exception {

		Logger log = Logger.getLogger(Compiler.class);

		Reader br = null;
		String sourceFileName = args.length < 1 ? defaultInputFileName : args[0];
		String outputFileName = args.length < 2 ? sourceFileName.replace(".mj", ".obj") : args[1];
		
		File sourceCode = new File(sourceFileName);
		try {

			log.info("Prevodjenje fajla: " + sourceCode.getAbsolutePath() + '\n');
			// 1. Lex
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);

			// 2. Parse
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse(); // pocetak parsiranja
			if (p.errorDetected) {
				log.error("Greka pri parsiranju, obrada zaustavljena");
				return;
			}
			log.info("Parsiranje uspesno zavrseno");

			Program prog = (Program) (s.value);
			// ispis sintaksnog stabla
			Tab.init();
			Tab.insert(Obj.Type, "bool", boolStruct);
			log.info("Sintaksno stablo: \n");
			log.info(prog.toString(""));
			log.info("\n");

			// 3. Semantic
			
			SemanticAnalyzer v = new SemanticAnalyzer(boolStruct);
			prog.traverseBottomUp(v);

			if (!v.passed()) {
				log.error("Greska pri semantickoj analizi, obrada zaustavljena");
				return;
			}
			
			tsdump();
			
			
			// 4. Code gen
			File objFile = new File(outputFileName);
			if (objFile.exists()) objFile.delete();
			CodeGenerator codeGenerator = new CodeGenerator(v);
			prog.traverseBottomUp(codeGenerator);

			Code.dataSize = v.getnVars();
			Code.mainPc = codeGenerator.getMainPc();
			Code.write(new FileOutputStream(objFile));
			
			log.info("Uspesno generisan izvrsiv fajl : " + outputFileName);
			Properties props = new Properties();
			props.setProperty("objfile", outputFileName);
			try (FileOutputStream fos = new FileOutputStream("build.properties")) {
			    props.store(fos, null);
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