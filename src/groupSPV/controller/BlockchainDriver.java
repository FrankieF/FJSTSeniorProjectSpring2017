package groupSPV.controller;

import java.io.File;

import org.bitcoinj.utils.BriefLogFormatter;

import groupSPV.model.CustomKit;
import groupSPV.model.CustomNBBL;
import groupSPV.view.BlockchainGUI;

/** BlockchainDriver is a driver class to initiate the downloading the Blockchain and display it.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class BlockchainDriver {

	/** Default save location for any OS. */
	protected static final String saveLocation = getSystemPath() + "SeniorProject_Bitcoin_Client\\";

	/** Main method.
	 * @param args Argument stating "testnet" will use Bitcoin Test network. */
	public static void main(String[] args) {
		BriefLogFormatter.init(); // Sets logging format, default for now

		CustomKit kit = new CustomKit(CustomKit.MAINNET, new File(saveLocation));
		if (args.length > 0 && args[0].equals("testnet")) {
			System.out.println("TestNet in use.");
			kit = new CustomKit(CustomKit.TESTNET, new File(saveLocation + "test\\"));
		}
		
		kit.startAndWait();

		BlockchainGUI bcGUI = new BlockchainGUI(kit);
		kit.addNewBestBlockListener(new CustomNBBL(bcGUI));
		
		/*LoginList.addUser("test", "test");
		System.out.println(LoginList.verifyUser("test", "test1")); //Should fail with bad password*/
		
		/*ConversionRate.update(); // Must call update when you want to update. Value stored for multiple conversions.
		BigCoin testCoins = new BigCoin(Long.parseLong("1000000000")); // 10 BTC
		System.out.println(ConversionRate.convert(testCoins)); // 10 BTC to USD*/
	}

	/** Returns User's full 'AppData' path if Windows, blank string if not.
	 *  @return 'AppData' full path. */
	private static String getSystemPath() {
		if (System.getenv("appdata") == null) {
			return "";
		} else {
			return System.getenv("appdata") + "\\";
		}
	}
}