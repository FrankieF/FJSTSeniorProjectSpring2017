package groupSPV.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet.BalanceType;

import groupSPV.model.CustomKit;
import groupSPV.view.CommandLineDisplay;

/** BlockchainDriver is a driver class to initiate the downloading the Blockchain and display it.
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class BlockchainDriverTest {

	/** Default save location for any OS. */
	private static final String saveLocation = getSystemPath() + "SeniorProject_Bitcoin_Client\\";
	private static final String saveLocationTest = getSystemPath() + "SeniorProject_Bitcoin_Client\\Test";
	private static boolean isTest = true;

	/** Main method.
	 * @param args Arguments not utilized. */
	public static void main(String[] args) {
		BriefLogFormatter.init(); // Sets logging format, default for now
		
		CustomKit kit = new CustomKit(new File(!isTest ? saveLocation : saveLocationTest));
		System.out.println("Downloading in progress...");
		kit.startAndWait();
		System.out.println("Downloading complete. Current height: " + kit.getHeight());
		CommandLineDisplay cld = new CommandLineDisplay(kit);
		cld.display(5);
		
		WalletController wc = new WalletController(kit.getWalletAppKit().wallet());
		System.out.println("Address: " + wc.getWallet().currentReceiveAddress());
		//wc.addNewKey();
		for (ECKey a : wc.getWallet().getImportedKeys())
			System.out.println(a.toStringWithPrivate(wc.getWallet().getParams()).toString());
		try {
		wc.sendBitcoin(kit.getWalletAppKit().peerGroup(), "n3XdmaMAWc9ULbr6Hsa2ixarBTfd21ToQF", "1");
		} catch (Exception e) {System.out.println("ERROR");
		System.out.println("Available: "+wc.getBalance(BalanceType.AVAILABLE));
		System.out.println("Spendable: "+wc.getBalance(BalanceType.AVAILABLE_SPENDABLE));
		System.out.println("Estimated: "+wc.getBalance(BalanceType.ESTIMATED));
		System.out.println("Estimated Spendable: "+wc.getBalance(BalanceType.ESTIMATED_SPENDABLE));
		}		
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