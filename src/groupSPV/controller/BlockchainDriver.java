package groupSPV.controller;

import java.io.File;

import org.bitcoinj.utils.BriefLogFormatter;

import groupSPV.model.CustomKit;
import groupSPV.view.CommandLineDisplay;

/** BlockchainDriver is a driver class to initiate the downloading the Blockchain and display it.
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class BlockchainDriver {

	/** Default save location for any OS. */
	private static final String saveLocation = getSystemPath() + "SeniorProject_Bitcoin_Client\\";

	/** Main method.
	 * @param args Arguments not utilized. */
	public static void main(String[] args) {
		BriefLogFormatter.init(); // Sets logging format, default for now

		CustomKit kit = new CustomKit(new File(saveLocation));

		System.out.println("Downloading in progress...");
		kit.startAndWait();
		System.out.println("Downloading complete. Current height: " + kit.getHeight());
		
		CommandLineDisplay cld = new CommandLineDisplay(kit);
		cld.display(5);
	}
	
	/** Returns User's full 'AppData' path if Windows, blank string if not.
	 * @return 'AppData' full path. */
	private static String getSystemPath() {
		if (System.getenv("appdata") == null) {
			return "";
		} else {
			return System.getenv("appdata") + "\\";
		}
	}
}