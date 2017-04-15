/**
 * 
 */
package groupSPV.Utils;

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
	
	/** Returns User's full 'AppData' path if Windows, blank string if not.
	 *  @return 'AppData' full path. */
	public static String getSystemPath() {
		if (System.getenv("appdata") == null) {
			return "SeniorProject_Bitcoin_Client\\";
		} else {
			return System.getenv("appdata") + "SeniorProject_Bitcoin_Client\\";
		}
	}

	/** Returns User's full 'AppData' path if Windows, blank string if not.
	 * @param path The path to the file inside of the SeniorProject_Bitcoin_Client directory.
	 *  @return 'AppData' full path. */
	public static String getSystemPath(String path) {
		if (System.getenv("appdata") == null) {
			return "SeniorProject_Bitcoin_Client\\" + path;
		} else {
			return System.getenv("appdata") + "SeniorProject_Bitcoin_Client\\" + path;
		}
	}
}
