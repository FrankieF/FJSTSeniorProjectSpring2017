package groupSPV.controller;

import java.io.File;

import groupSPV.model.CustomKit;
import groupSPV.model.CustomNBBL;
import groupSPV.model.User;
import groupSPV.view.BlockchainGUI;

public class AfterLogin {

	private static String userSaveLocation;
	
	public static void startGUI(User user) {
		userSaveLocation = BlockchainDriver.networkSaveLocation + user.getUsername() + "\\";
		CustomKit kit = new CustomKit(BlockchainDriver.currentNetwork, new File(userSaveLocation));
		kit.startAndWait();

		BlockchainGUI bcGUI = new BlockchainGUI(kit);
		kit.addNewBestBlockListener(new CustomNBBL(bcGUI));
	}
}