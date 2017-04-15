package groupSPV.Utils;

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
}