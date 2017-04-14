package groupSPV.controller;

import java.io.File;

import org.bitcoinj.utils.BriefLogFormatter;

import groupSPV.model.CustomKit;
import groupSPV.view.LoginGUI;

/** BlockchainDriver is a driver class to initiate the downloading the Blockchain and display it.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class BlockchainDriver {

	/** Default save location for any OS on a particular Bitcoin Network. */
	protected static String networkSaveLocation = getSystemPath() + "SeniorProject_Bitcoin_Client\\";
	
	protected static int currentNetwork;
	
	/** Default TestNet Folder. */
	private static final String testNetFolder = "test\\";

	/** Main method.
	 * @param args Argument stating "testnet" will use Bitcoin Test network. */
	public static void main(String[] args) {
		BriefLogFormatter.init(); // Sets logging format, default for now

		currentNetwork = CustomKit.MAINNET;
		
		if (args.length > 0 && args[0].equals("testnet")) {
			System.out.println("TESTNET IN USE!!!");
			networkSaveLocation += testNetFolder;
			currentNetwork = CustomKit.TESTNET;
		}
		
		File tmpFile = new File(networkSaveLocation);
		if(!tmpFile.exists())
			tmpFile.mkdirs();
		
		new LoginGUI();
		
		/*ConversionRate.update(); // Must call update when you want to update. Value stored for multiple conversions.
		BigCoin testCoins = new BigCoin(Long.parseLong("1000000000")); // 10 BTC
		System.out.println(ConversionRate.convert(testCoins)); // 10 BTC to USD*/
	}

	/** Returns User's full 'AppData' path if Windows, blank string if not.
	 *  @return 'AppData' full path. */
	private static String getSystemPath() {
		String appData = System.getenv("appdata");
		
		if (appData == null) return "";
		return appData + "\\";
	}
}