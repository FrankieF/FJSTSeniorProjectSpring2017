package groupSPV.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import groupSPV.model.CustomKit;
import groupSPV.model.User;

/**
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva
 * 
 * This class acts as a utility class for the entire project. Methods that are used
 * in more than one place can be placed here as static methods to avoid duplicating code.
 */
public class Utils {
	
	private static final String defaultPath = "SeniorProject_Bitcoin_Client\\";
	private static int currentNetwork = CustomKit.MAINNET;
	
	/** Returns User's full 'AppData' path if Windows, blank string if not.
	 *  @return 'AppData' full path. */
	public static String getSystemPath() {
		String tempPath;
		if (System.getenv("appdata") == null) {
			tempPath = defaultPath;
		} else {
			tempPath = System.getenv("appdata") + "\\" + defaultPath;
		}
		return tempPath + (currentNetwork==CustomKit.TESTNET ? "test\\" : "");
	}

	/** Returns User's full 'AppData' path if Windows, blank string if not.
	 * @param path The path to the file inside of the SeniorProject_Bitcoin_Client directory.
	 *  @return 'AppData' full path. */
	public static String getSystemPath(String path) {
		return getSystemPath() + path;
	}
	
	public static String getUserPath(User user) {
		return getSystemPath(user.getUsername() + "\\");
	}
	
	public static void setNetwork(int network) {
		currentNetwork = network;
	}
	
	public static int getNetwork() {
		return currentNetwork;
	}
	
	/** Sets the given window location to the center of the User's screen.
	 * @param window Window to center. */
	public static void setWindowCenterOfScreen(Window window) {
		Dimension userScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(userScreenSize.width/2-window.getSize().width/2, userScreenSize.height/2-window.getSize().height/2);
	}
}
