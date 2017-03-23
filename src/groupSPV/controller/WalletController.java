package groupSPV.controller;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.bitcoinj.core.*;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.AbstractWalletEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;

/**
 * @author Frankie Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva
 * Interfaces between the client and the wallet by wrapping the wallet methods.
 */
public class WalletController
{
	private Wallet wallet;

	/***
	 * @author Francis Fasola
	 * Constructs a new wallet controller object.
	 * @param params The network the wallet runs on.
	 */
	public WalletController (Wallet wallet) {
		this.wallet = wallet;
		addEventListeners();
	}
	
	/**
	 * Only used for internal class testing.
	 * @param params
	 */
	private WalletController (NetworkParameters params) {
		wallet = new Wallet(params);
		addEventListeners();
	}
	
	/**
	 * @author Francis Fasola
	 * @return The wallet.
	 */
	public Wallet getWallet() {
		return this.wallet;
	}
	
	/**
	 * @author Francis Fasola
	 * Returns The balance of the specified by the type passed in. The type
	 * can be left null to receive available balance of the wallet. 
	 * @return The balance of the wallet.
	 */
	public Coin getBalance(Wallet.BalanceType type) {
		return type.equals(null) ? this.wallet.getBalance() : this.wallet.getBalance(type);		
	}
	
	/***
	 * @author Francis Fasola
	 * Adds an address to the wallet.
	 * @param address The address to add to the wallet.
	 */
	public void addAddress (Address address) {
		this.wallet.addWatchedAddress(address);
	}
	
	/***
	 * @author Francis Fasola
	 * Allows a user to change the password to their wallet.
	 * 
	 * @param newPassword The new password.
	 * @param oldPassword The old password.
	 */
	public void changePassword (CharSequence newPassword, CharSequence oldPassword) {
		this.wallet.changeEncryptionPassword(oldPassword, newPassword);
	}
	
	/***
	 * @author Francis Fasola
	 * Returns a list of transactions starting with the most recent.
	 */
	public List<Transaction> getTransactions() {
		return this.wallet.getTransactionsByTime();
	}
	
	/***
	 * @author Francis Fasola
	 * Returns a list of transaction outputs that represent the unspent amount in the wallet.
	 */
	public List<TransactionOutput> getUnspent() {
		return this.wallet.getUnspents();
	}
	
	/***
	 * @author Francis Fasola
	 * Returns the description of the wallet.
	 */
	public String getDescription() {
		return this.wallet.getDescription();
	}
	
	/***
	 * @author Francis Fasola
	 * Allows the user to change the description of the wallet.
	 * @param desc The new description of the wallet.
	 */
	public void setDescription(String desc) {
		this.wallet.setDescription(desc);
	}
	
	/**
	 * @author Francis Fasola
	 * @return The version of the wallet.
	 */
	public int getVersion () {
		return this.wallet.getVersion();
	}
	
	/**
	 * @author Francis Fasola
	 * Adds multiple pre-existing keys to the wallet.
	 * 
	 * @param params The network parameters of the client.
	 * @param key String representation of the private key(s).
	 */
	public void addExistingKeys(NetworkParameters params, String... keys) {
		for (String key : keys) {
			DumpedPrivateKey _key = DumpedPrivateKey.fromBase58(params, key);
			this.wallet.importKey(_key.getKey());
		}
	}
	
	/**
	 * @author Francis Fasola
	 * Creates a new ECKey (Eliptic Curve) and adds it to the wallet.
	 * 
	 * @param params Network parameters for the wallet.
	 */
	public void addNewKey(NetworkParameters params) {
		ECKey key = new ECKey();
		this.wallet.importKey(key);
		System.out.println("Public key: " + key.toAddress(params));
		System.out.println("Private key: " + key.getPrivateKeyEncoded(params));
	}
	
	/**
	 * @author Francis Fasola
	 * @return The list of keys imported into the wallet.
	 */
	public List<ECKey> getKeys() {
		return this.wallet.getImportedKeys();
	}
	
	/**
	 * @author Francis Fasola
	 * Sends Bitcoin to the specified bitcoin address.
	 * 
	 * @param params The network parameters.
	 * @param peers The peer group the wallet is apart of. 
	 * @param address String representation of the public address.
	 * @param amount The amount of Bitcoin to send (uses {@link Coin.parseCoin}). 
	 * @return True if transaction was successful, else false.
	 * @throws {@link InsufficientMoneyException}
	 * @throws {@link ExecutionException}
	 * @throws {@link InterruptedException}
	 */
	public boolean sendBitcoin(NetworkParameters params, PeerGroup peers, String address, String amount) 
				throws InsufficientMoneyException, ExecutionException, InterruptedException {
		System.out.println("Entered send function");
		Address destinationAddress = Address.fromBase58(params, address);
		Wallet.SendResult result = this.wallet.sendCoins(peers, destinationAddress, Coin.parseCoin(amount));
		if(result != null) {
			result.broadcastComplete.get();
			System.out.println("The money was sent!");
			return true;
		}
		else {
			System.out.println("Something went wrong sending the money.");
			return false;
		}
	}
	
	/***
	 * @author Francis Fasola
	 * Adds the event listeners to the wallet.
	 */
	@SuppressWarnings("deprecation")
	private void addEventListeners() {
		//onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance)
		this.wallet.addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
			System.out.println("****** COINS RECEIVED ******");
			System.out.println("HASH: " + tx.getHashAsString());
			System.out.println("RECEIVED: " + tx.getValue(wallet) + " . NEW BALANCE: " + wallet.getBalance());
			System.out.println("PREVIOUS BALANCE: " + prevBalance.value + " NEW BALANCE: " + newBalance.value);
			System.out.println("VERSION: " + tx.getVersion());
		});
		//			onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance)
		this.wallet.addCoinsSentEventListener(new WalletCoinsSentEventListener() {
			
			@Override
			public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
				System.out.println("****** COINS SENT ******");
				System.out.println("RECEIVED: " + tx.getValue(wallet) + " . NEW BALANCE: " + wallet.getBalance());				
			}
		});
		// 			onTransactionConfidenceChanged(Wallet wallet, Transaction tx)
		/*this.wallet.addTransactionConfidenceEventListener((wallet, tx) -> {
			System.out.println("***** CONFIDENCE CHANGED *****");
			System.out.println("HASH: " + tx.getHashAsString());
			TransactionConfidence c = tx.getConfidence();
			System.out.println("NEW BLOCK DEPTH: " + c.getDepthInBlocks());
		});*/
		// 			onKeysAdded(List<ECKey> keys)
		this.wallet.addKeyChainEventListener(e -> {
			System.out.println("KEY ADDED: " + e.toString());
		});
	}
	
}