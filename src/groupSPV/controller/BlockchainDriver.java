package groupSPV.controller;

import java.io.File;

import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet.BalanceType;

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
		System.out.println("Balance: " + wc.getBalance(null).toFriendlyString());
		System.out.println("Available : " + wc.getBalance(BalanceType.AVAILABLE).toFriendlyString());
		System.out.println("Estimated: " + wc.getBalance(BalanceType.ESTIMATED).toFriendlyString());
		System.out.println("Estimated spendable: " + wc.getBalance(BalanceType.ESTIMATED_SPENDABLE).toFriendlyString());

		/*try {
			wc.sendBitcoin("mrJMoMskRKXm3YxyR6koBbggqb2KcfTsRY", ".5");
			System.out.println("Sent successfully");
		} catch (Exception e) {
			System.out.println(":(");
			e.printStackTrace();
		}*/
		// TODO Integrate Wallet GUI here (Or with BlockchainGUI if combined)
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