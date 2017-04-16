package groupSPV.controller;

import java.io.File;

import javax.swing.JFrame;

import groupSPV.model.CustomKit;
import groupSPV.model.CustomNBBL;
import groupSPV.model.User;
import groupSPV.view.BlockchainGUI;
import groupSPV.view.SyncGUI;

/** Helper class to kick off functionality after user login.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class AfterLogin {

	/** Save location for logged in user. */
	private static String userSaveLocation;
	
	/** Starts BlockchainGUI with particular user.
	 * @param user User to start GUI with. */
	public static void startGUI(User user) {
		userSaveLocation = BlockchainDriver.networkSaveLocation + user.getUsername() + "\\";
		
		JFrame syncScreen = new SyncGUI("Updating blockchain for " + user.getUsername() + " ...");
		CustomKit kit = new CustomKit(BlockchainDriver.currentNetwork, new File(userSaveLocation));
		kit.startAndWait(user);
		syncScreen.dispose();
		
		BlockchainGUI bcGUI = new BlockchainGUI(kit);
		kit.addNewBestBlockListener(new CustomNBBL(bcGUI));
	}
}