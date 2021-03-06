package groupSPV.controller;

import java.io.File;
import java.io.PrintStream;

import org.bitcoinj.utils.BriefLogFormatter;

import groupSPV.model.CustomKit;
import groupSPV.view.LoginGUI;

/** Driver class to initiate the login of the client.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class Driver {

	/** Main method.
	 * @param args Argument stating "testnet" will use Bitcoin Test network. */
	public static void main(String[] args) {
		BriefLogFormatter.init(); // Sets logging format, default for now
		ignoreSELF4JErrors();
		
		if (args.length > 0 && args[0].equals("testnet"))
			Utils.setTestNetwork(CustomKit.TESTNET);
		
		File tmpFile = new File(Utils.getSystemPath());
		if(!tmpFile.exists())
			tmpFile.mkdirs();
		
		new LoginGUI();
	}
	
	/** Ignores errors related to "SELF4J" in System.err.
	 * These errors are generated by bitcoinj dependency that is unused. */
	private static void ignoreSELF4JErrors() {
		PrintStream filterOut = new PrintStream(System.err) {
			public void println(String l) {
				if (! l.startsWith("SLF4J") )
					super.println(l);
			}
		};
		System.setErr(filterOut);
	}
}