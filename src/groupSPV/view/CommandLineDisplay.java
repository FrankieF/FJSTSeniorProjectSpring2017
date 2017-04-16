package groupSPV.view;

import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.store.BlockStoreException;

import groupSPV.model.CustomKit;

/** CommandLineDisplay is an object used to display the Blockchain via CLI.
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class CommandLineDisplay {

	private CustomKit kit;
	
	private static final String NEWLINE = "\n", TAB = "\t";
	
	public CommandLineDisplay(CustomKit kit) {
		this.kit = kit;
	}
	
	public void display() {
		System.out.println(this.toString());
	}
	
	public void display(int blocksToDisplay) {
		System.out.println(this.toString(blocksToDisplay));
	}
	
	public String toString() {
		return toString(10);
	}

	public String toString(int blocksToDisplay) {
		String result = NEWLINE + "Block" + TAB + "Hash" + NEWLINE;
		try {
			StoredBlock working = kit.getChainHead();
			StoredBlock previous = working.getPrev(kit.getBlockStore());
			
			for (int i = 0; i < blocksToDisplay; i++) {
				result += Integer.toString(working.getHeight()) + TAB + working.getHeader().getHashAsString() + NEWLINE;
			
				working = previous; 
				previous = working.getPrev(kit.getBlockStore());
			}
			
			return result;
		} catch (BlockStoreException bse) {
			return result + "BlockStoreException Reached...";
		}
	}
}