package groupSPV.model;

import java.io.Serializable;

/** Simple friend class for contact list.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class Friend implements Serializable {

	private static final long serialVersionUID = 7469938326904935397L;
	
	/** Friend name and key strings. */
	private String name, key;
	
	/** Creates a Friend object.
	 * @param name Name of friend.
	 * @param key Key of friend. */
	public Friend(String name, String key) {
		this.name = name;
		this.key = key;
	}
	
	/** Get the name of Friend.
	 * @return String. */
	public String getName() {
		return name;
	}
	
	/** Get the key of Friend.
	 * @return String. */
	public String getKey() {
		return key;
	}
	
	/** Sets the name of Friend.
	 * @param name Friend name. */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Sets the key of Friend.
	 * @param name Friend key. */
	public void setKey(String key) {
		this.key = key;
	}
}