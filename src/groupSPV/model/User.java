package groupSPV.model;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/** User object for usernames and hashed passwords.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class User implements Serializable {

	/** Serial Version UID. */
	private static final long serialVersionUID = 5171950477886229899L;

	/** Stored username and hashed password. */
	private String username, hashedPassword;
	
	private List<Friend> friendKeys = new ArrayList<Friend>();
	
	/** Create a new user, store username and hash given password.
	 * @param username Username.
	 * @param password Password before hashing. */
	public User (String username, String password) {
		this.username = username;
		this.hashedPassword = hashPassword(password);
	}
	
	/** Verifies if password is correct.
	 * @param potentialPassword Password before hashing.
	 * @return boolean. */
	public boolean verifyUser(String potentialPassword) {
		String potentialHashed = hashPassword(potentialPassword);
		if(hashedPassword.equals(potentialHashed)) return true; // Correct password
		else return false; // Wrong password
	}
	
	@Override
	public String toString() {
		return getUsername();
	}
	
	public String getUsername() {
		return this.username;
	}
	
	/** Hash a password using Java SHA-256.
	 * @param original Original password.
	 * @return Hashed password. */
	private static String hashPassword(String original) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(original.getBytes(Charset.forName("UTF-8")));
			byte byteData[] = md.digest();
			//convert the byte to hex format
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1)
	   	     		hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// Should never happen.
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Friend> getFriendKeys() {
		return this.friendKeys;
	}
	
	/***
	 * Adds a key to the list of friend keys.
	 * @author Francis Fasola
	 * 
	 * @param name Alias for the given key.
	 * @param key Public key of the alias.
	 */
	public void addFriend(String name, String key) {
		this.friendKeys.add(new Friend(name, key));
	}
}