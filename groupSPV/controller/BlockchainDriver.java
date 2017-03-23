package groupSPV.controller;

import java.io.File;

import org.bitcoinj.utils.BriefLogFormatter;

import groupSPV.model.CustomKit;

/** BlockchainDriver is a driver class to initiate the downloading the Blockchain and display it.
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class BlockchainDriver {

	/** Default save location for any OS. */
	private static final String saveLocation = getSystemPath() + "SeniorProject_Bitcoin_Client\\";
	private CustomKit kit;
	
	/** Main method.
	 * @param args Arguments not utilized. */
	public BlockchainDriver() {
		BriefLogFormatter.init(); // Sets logging format, default for now
		kit = new CustomKit(new File(saveLocation));

	}
	
	public CustomKit getKit(){
		return kit;
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