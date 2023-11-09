package app;

import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import java.io.FileNotFoundException;

import ast.Statechart;
import see.*;
import program.*;
import frontend.*;
import translation.*;
import java_cup.runtime.Symbol;
import java.io.PrintWriter;

// Entry point of the application
public class App {
	public static void main (String[] args) {
		OptionGroup grp = new OptionGroup();
		grp
			.addOption(new Option("klee", false, "Translate to C++ for inspection with KLEE"))
			.addOption(new Option("see", false, "Run spec with home grown symbolic engine"));
		grp.setRequired(true);

		OptionGroup p_grp = new OptionGroup();
		p_grp
			.addOption(new Option("nd", "non-determinism", false, "PropertyOfInterest::NON_DETERMINISM"))
			.addOption(new Option("ss", "stuck-specification", false, "PropertyOfInterest::STUCK_SPECIFICATION"));

		Options options = new Options();
		options
			.addOptionGroup(grp)
			.addOptionGroup(p_grp)
			.addRequiredOption("md", "max-depth", true, "Max number of events to simulate over")
			.addRequiredOption("f", "filename", true, "Stabl specification file");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			boolean useKlee = cmd.hasOption("klee");
			int md = 10;
			if (cmd.hasOption("max-depth")) {
				String mds = cmd.getOptionValue("max-depth");
				md = Integer.parseInt(mds);
			}

			String filename = null;
			if (cmd.hasOption("filename")) { // Required option
				filename = cmd.getOptionValue("filename");
			}
			Statechart statechart = null;
			try {
				// Parse
				FrontEnd frontend = new FrontEnd(filename);
				Parser fparser = frontend.getParser();    
				Symbol result = fparser.parse();
				statechart = (Statechart)result.value;

				// typecheck
				Typechecker checker = new Typechecker(statechart);
				checker.typecheck();

				// flatten
				Flattener flattener = new Flattener();
				Statechart flattenedSC = flattener.translate(statechart);
				//write flattened program to file
				PrintWriter pw=new PrintWriter(filename+".flatten", "UTF-8");
				pw.println(flattenedSC);
				pw.flush();
				
				// Translate to Program using s2p
				S2P s2p = new S2P();
				Program program = s2p.translate(flattenedSC);
				PrintWriter pw1=new PrintWriter(filename+".pgm", "UTF-8");
				pw1.println(program);
				pw1.flush();
				
				// Translate to Program using statechart to program translator
				StatechartToProgramTranslator s2p1 = new StatechartToProgramTranslator(flattenedSC);
				Program program1 = s2p1.translate();
				PrintWriter pw2=new PrintWriter(filename+".stbl2pgm", "UTF-8");
				pw2.println(program1);
				pw2.flush();
				
				
				if (useKlee) {
					ProgramToCpp translator = new ProgramToCpp(program);
					translator.translate();
				} else {
					Engine.PropertyOfInterest property = Engine.PropertyOfInterest.NON_DETERMINISM; // Default
					if (cmd.hasOption("ss")) {
						property = Engine.PropertyOfInterest.STUCK_SPECIFICATION;
					}
					Engine engine = new Engine();
					engine.getSEResult(program, md, property);
					//engine.getSEResult(program1, md, property);
				}

			} catch (Exception e) {
				System.out.println(e);
				System.exit(1);
			}

		} catch (ParseException e) {
			System.out.println("Error: " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "app", options );
			System.exit(65);
		}
	}
}
