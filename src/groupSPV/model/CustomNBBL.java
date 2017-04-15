package groupSPV.model;

import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.VerificationException;
import org.bitcoinj.core.listeners.NewBestBlockListener;
import org.bitcoinj.store.BlockStoreException;

import groupSPV.view.BlockchainGUI;

/** Custom NewBestBlock Listener to update Blockchain GUI.
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class CustomNBBL implements NewBestBlockListener {
	private BlockchainGUI bcg;
	
	public CustomNBBL(BlockchainGUI bcg) {
		this.bcg = bcg;
	}

	@Override
	public void notifyNewBestBlock(StoredBlock block) throws VerificationException {
		try {
			bcg.updateTable();
			BlockchainGUI.hideBtnMoreInfo();
		} catch (BlockStoreException e) {
			System.err.println(e.getMessage());
		}
	}
}
