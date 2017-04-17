package groupSPV.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.listeners.NewBestBlockListener;
import org.bitcoinj.core.listeners.ReorganizeListener;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;

import groupSPV.controller.WalletController;

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
	
	private WalletController wc;
	
	private User user;
	
	/** List of all current NewBestBlockListeners. */
	private List<NewBestBlockListener> newBestBlockListeners = new ArrayList<NewBestBlockListener>();
	
	/** List of all current ReorganizeListeners. */
	private List<ReorganizeListener> reorganizeListeners = new ArrayList<ReorganizeListener>();

	/** Default file prefix string needed for WalletAppKit. */
	private static final String defaultFilePrefix = "forwarding-service";
	
	/** TestNet file prefix string needed for WalletAppKit. */
	private static final String testFilePrefix = "forwarding-service-testnet";
	
	/** Bitcoin network selection integer. */
	public static final boolean MAINNET = false, TESTNET = true;
	
	/* -----------------------
	 *          SETUP
	 * ----------------------- */
	/** Constructor for creating a CustomKit at a particular saveLocation. Uses default bitcoin network.
	 * @param saveLocation File object of folder location to save client files.
	 * @param User for wallet. */
	public CustomKit(File saveLocation, User user) {
		this(MAINNET, saveLocation, user);
	}
	
	/** Constructor for creating a CustomKit at a particular saveLocation. Uses network ints.
	 * TestNet if 1, MainNet if anything else.
	 * @param network Network selection integer.
	 * @param saveLocation File object of folder location to save client files.
	 * @param User for wallet. */
	public CustomKit(boolean network, File saveLocation, User user) { 
		if (network == TESTNET)
			wak = new WalletAppKit(TestNet3Params.get(), saveLocation, testFilePrefix);
		else
			wak = new WalletAppKit(MainNetParams.get(), saveLocation, defaultFilePrefix);
		this.user = user;
	}
	
	/** Starts downloading of Blockchain, holds until fully downloaded. */
	public void startAndWait() {
		JLabel loadingLabel = new JLabel();
		loadingLabel.setText("Loading...");
		wak.startAsync(); // Start WAK
		loadingLabel.setVisible(true);
		wak.awaitRunning(); // Wait for WAK to fully start
		wc = new WalletController(wak.wallet(), user); // Create WC after done
		loadingLabel.setVisible(false);
	}
	
	/** Stops synchronizing processes, holds until fully stopped. */
	public void stopAndWait() {
		wak.stopAsync(); // Stop WAK
		wak.awaitTerminated(); // Wait for WAK to fully stop
	}
	
	/* -----------------------
	 *      VARIOUS DATA
	 * ----------------------- */
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
	
	/** Returns the WalletController. Null if CustomKit is not started.
	 * @return WalletController. */
	public WalletController getWalletController() {
		return wc;
	}
	
	/* -----------------------
	 *    LISTENER HANDLING
	 * ----------------------- */
	/** Adds a Blockchain NewBlockListener.
	 * @param nbbl NewBestBlockListener to add. */
	public void addNewBestBlockListener(NewBestBlockListener nbbl) {
		newBestBlockListeners.add(nbbl);
		wak.chain().addNewBestBlockListener(nbbl);
	}
	
	/** Removes a Blockchain NewBlockListener.
	 * @param nbbl NewBestBlockListener to remove. */
	public void removeNewBestBlockListener(NewBestBlockListener nbbl) {
		newBestBlockListeners.remove(nbbl);
		wak.chain().removeNewBestBlockListener(nbbl);
	}
	
	/** Removes all Blockchain NewBlockListener(s). */
	public void removeAllNewBestBlockListeners() {
		for(NewBestBlockListener nbbl : newBestBlockListeners)
			wak.chain().removeNewBestBlockListener(nbbl);
		newBestBlockListeners.clear();
	}

	/** Adds a Blockchain ReorganizeListener.
	 * @param rl ReorganizeListener to add. */
	public void addReorganizeListener(ReorganizeListener rl) {
		reorganizeListeners.add(rl);
		wak.chain().addReorganizeListener(rl);
	}
	
	/** Removes a Blockchain ReorganizeListener.
	 * @param rl ReorganizeListener to remove. */
	public void removeReorganizeListener(ReorganizeListener rl) {
		reorganizeListeners.remove(rl);
		wak.chain().removeReorganizeListener(rl);
	}
	
	/** Removes all Blockchain ReorganizeListener(s). */
	public void removeAllReorganizeListener() {
		for(ReorganizeListener rl : reorganizeListeners)
			wak.chain().removeReorganizeListener(rl);
		reorganizeListeners.clear();
	}
	
	/* -----------------------
	 *     FULL NODE/PEER
	 * ----------------------- */
	/** Returns list of all connected Peers.
	 * @return List of connected Peers. */
	private List<Peer> getConnectedPeers() {
		return wak.peerGroup().getConnectedPeers();
	}
	
	/** Returns a currently connected Peer.
	 * @return Connected Peer. */
	protected Peer getConnectedPeer() {
		return getConnectedPeers().get(0);
	}
	
	/** Requests and returns a full Block from supplied Peer.
	 * @param peer Full peer to request Block from.
	 * @param blockHash Block hash to request.
	 * @return Full block.
	 * @throws InterruptedException Thrown if interrupted.
	 * @throws ExecutionException Thrown if error arises. */
	protected Block getFullBlock(Peer peer, Sha256Hash blockHash) throws InterruptedException, ExecutionException {
		return peer.getBlock(blockHash).get();
	}
	
	/** Requests and returns a full Block from Peer from getConnectedPeer().
	 * @param blockHash Block hash to request.
	 * @return Full block.
	 * @throws InterruptedException Thrown if interrupted.
	 * @throws ExecutionException Thrown if error arises. */
	public Block getFullBlock(Sha256Hash blockHash) throws InterruptedException, ExecutionException {
		return getFullBlock(getConnectedPeer(), blockHash);
	}
	
	/** Returns Version of StoredBlock on disk.
	 * @param block block that is in question
	 * @return String Version of the BlockStore. */
	public String getBlockStoreVersion(StoredBlock block) {
		String[] info = block.toString().split("\\r?\\n");
		return info[2].split(":")[1];
	}
	
	/** Returns Vervion of StoredBlock on disk.
	 * @param block block that is in question.
	 * @return String Time of the BlockStore. */
	public String getBlockStoreTime(StoredBlock block) {
		String[] info = block.toString().split("\\r?\\n");
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Integer.valueOf(info[5].
				substring(info[5].indexOf(':') + 1, info[5].indexOf('(')).trim()) * 1000L));
	}
}
