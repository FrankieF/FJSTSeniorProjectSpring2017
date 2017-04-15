package groupSPV.controller;

import java.io.File;

import javax.swing.JFrame;

import groupSPV.Utils.Utils;
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

	/** Starts BlockchainGUI with particular user.
	 * @param user User to start GUI with. */
	public static void startGUI(User user) {
		JFrame syncScreen = new SyncGUI("Updating blockchain for " + user.getUsername() + " ...");
		
		CustomKit kit = new CustomKit(Utils.getNetwork(), new File(Utils.getUserPath(user)), user);
		kit.startAndWait();
		
		syncScreen.dispose();
		
		BlockchainGUI bcGUI = new BlockchainGUI(kit);
		kit.addNewBestBlockListener(new CustomNBBL(bcGUI));
	}
}