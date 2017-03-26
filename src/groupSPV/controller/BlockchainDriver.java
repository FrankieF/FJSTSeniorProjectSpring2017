package groupSPV.controller;
import java.io.File;

import org.bitcoinj.utils.BriefLogFormatter;

import groupSPV.model.CustomKit;
import groupSPV.model.CustomNBBL;
import groupSPV.view.BlockchainGUI;

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
	 * @param args Argument stating "testnet" will use Bitcoin Test network. */
	public static void main(String[] args) {
		BriefLogFormatter.init(); // Sets logging format, default for now
		
		CustomKit kit = new CustomKit(CustomKit.MAINNET, new File(saveLocation));
		
		if (args.length > 0 && args[0].equals("testnet")) {
			System.out.println("TestNet in use.");
			kit = new CustomKit(CustomKit.TESTNET, new File(saveLocation + "test\\"));
		}
		
		System.out.println("Downloading in progress...");
		kit.startAndWait();
		System.out.println("Downloading complete. Current height: " + kit.getHeight());
		
		BlockchainGUI bcGUI = new BlockchainGUI(kit);
		kit.addNewBestBlockListener(new CustomNBBL(bcGUI));
		
		WalletController wc = kit.getWalletController();
		// TODO Integrate Wallet GUI here (Or with BlockchainGUI if combined)
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