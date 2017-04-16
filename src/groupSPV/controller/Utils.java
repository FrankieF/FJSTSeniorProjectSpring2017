package groupSPV.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;

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
	
	/** Default path name, unrelated to appdata folder. */
	private static final String defaultPath = "SeniorProject_Bitcoin_Client\\";
	
	/** Current network. */
	private static boolean currentNetwork = CustomKit.MAINNET;
	
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
	
	/** Returns full path based on supplied user and current network.
	 * @param user Supplied User.
	 * @return Full path. */
	public static String getUserPath(User user) {
		return getSystemPath(user.getUsername() + "\\");
	}
	
	/** Set if test network.
	 * @param testNetwork Boolean. */
	public static void setTestNetwork(boolean testNetwork) {
		currentNetwork = testNetwork;
	}
	
	/** Determines if test network.
	 * @return Boolean. */
	public static boolean isTestNetwork() {
		return currentNetwork;
	}
	
	/** Sets the given window location to the center of the User's screen.
	 * @param window Window to center. */
	public static void setWindowCenterOfScreen(Window window) {
		Dimension userScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(userScreenSize.width/2-window.getSize().width/2, userScreenSize.height/2-window.getSize().height/2);
	}
	
	/** Returns a formatted BigDecimal with the minimum nubmer of decimal places specified without rounding.
	 * 
	 * Usage:	Utils.setMinimumDecimalPlaces(new BigDecimal("5"), 2) --> 2.00
	 * 			Utils.setMinimumDecimalPlaces(new BigDecimal("5.0675"), 2) --> 5.0675
	 * 
	 * @param bigDecimal BigDecimal to format.
	 * @param minDecimalPlaces Minimum number of decimal places.
	 * @return Formatted BigDecimal. */
	public static BigDecimal setMinimumDecimalPlaces(BigDecimal bigDecimal, int minDecimalPlaces) {
		return bigDecimal.setScale(Math.max(minDecimalPlaces, bigDecimal.scale()));
	}

	/** Sends String to User's clipboard.
	 * @param string String to send to clipboard. */
	public static void sendToClipboard(String string) {
		StringSelection selection = new StringSelection(string);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
}