package groupSPV.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStore;

/** CustomKit is a "Facade" class to the bitcoinj WalletAppKit class.
 * It simplifies working with the WalletAppKit class for our specific project needs.
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class CustomKit {

	/** WalletAppKit object to simplify. */
	private WalletAppKit wak;

	/** Default file prefix string needed for WalletAppKit. */
	private static final String defaultFilePrefix = "forwarding-service";
	
	/** Constructor for creating a CustomKit at a particular saveLocation. Uses default bitcoin network.
	 * @param saveLocation File object of folder location to save client files. */
	public CustomKit(File saveLocation) {
		this(MainNetParams.get(), saveLocation, defaultFilePrefix);
	}

	/** Constructor for creating a CustomKit using particular bitcoinj parameters.
	 * @param params NetworkParameters to use.
	 * @param saveLocation File object of folder location to save client files.
	 * @param filePrefix File prefix needed for WalletAppKit. */
	protected CustomKit(NetworkParameters params, File saveLocation, String filePrefix) {
		wak = new WalletAppKit(params, saveLocation, filePrefix);
	}

	/** Starts downloading of Blockchain, holds until fully downloaded. */
	public void startAndWait() {
		wak.startAsync(); // Start WAK
		wak.awaitRunning(); // Wait for WAK to fully start
	}
	
	/** Get the best known Blockchain height.
	 * @return Blockchain height. */
	public int getHeight() {
		return wak.chain().getBestChainHeight();
	}
	
	/** Returns the stored Blockchain.
	 * This only has Block headers in this instance.
	 * @return BlockChain. */
	private BlockChain getBlockChain() {
		return wak.chain();
	}
	
	/** Returns the last StoredBlock in BlockChain.
	 * @return Last StoredBlock. */
	public StoredBlock getChainHead() {
		return getBlockChain().getChainHead();
	}
	
	/** Returns BlockStore of StoredBlock(s) on disk.
	 * @return BlockStore. */
	public BlockStore getBlockStore() {
		return getBlockChain().getBlockStore();
	}
	
	/** Returns Version of StoredBlock on disk.
	 * @param StoredBlock block that is in question
	 * @return String Version of the BlockStore. */
	public String getBlockStoreVersion(StoredBlock block) {
		String[] info = block.toString().split("\\r?\\n");
		return info[2].split(":")[1];
	}
	
	/** Returns Vervion of StoredBlock on disk.
	 * @param StoredBlock block that is in question
	 * @return 
	 * @return String Time of the BlockStore. */
	public String getBlockStoreTime(StoredBlock block) {
		String[] info = block.toString().split("\\r?\\n");
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Integer.valueOf(info[5].
				substring(info[5].indexOf(':') + 1, info[5].indexOf('(')).trim()) * 1000L));
	}
}