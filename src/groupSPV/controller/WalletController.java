package groupSPV.controller;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;
import org.bitcoinj.wallet.Wallet.SendResult;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;

import com.google.common.util.concurrent.MoreExecutors;

import groupSPV.model.Friend;
import groupSPV.model.User;
import groupSPV.view.WalletGUI;

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
	private NetworkParameters params;
	private User user;
	//private String path;
	
	/***
	 * @author Francis Fasola
	 * Constructs a new wallet controller object.
	 * @param params The network the wallet runs on.
	 */
	public WalletController (Wallet wallet, User user) {
		this.wallet = wallet;
		this.params = wallet.getParams();
		this.user = user;
		//this.path = Utils.getUserPath(user) + "friend.ser";
		addEventListeners();
	}
	
	/**
	 * Only used for internal class testing.
	 * @param params
	 */
	public WalletController (NetworkParameters params) {
		wallet = new Wallet(params);
		this.params = params;
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
		return type == null ? this.wallet.getBalance() : this.wallet.getBalance(type);		
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
	 * @return The pending transactions in the wallet.
	 */
	public Collection<Transaction> getPendingTransactions() {
		return this.wallet.getPendingTransactions();
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
	public void addExistingKeys(String... keys) {
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
	public void addNewKey() {
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
	 * @param peers The peer group the wallet is apart of. 
	 * @param address String representation of the public address.
	 * @param amount The amount of Bitcoin to send (uses {@link Coin.parseCoin}). 
	 * @return True if transaction was successful, else false.
	 * @throws {@link InsufficientMoneyException}
	 * @throws {@link ExecutionException}
	 * @throws {@link InterruptedException}
	 */
	@SuppressWarnings("deprecation")
	public void sendBitcoin(String address, String amount) 
				throws InsufficientMoneyException, ExecutionException, InterruptedException {
		Address destinationAddress = Address.fromBase58(params, address);
		SendRequest request = SendRequest.to(destinationAddress, Coin.parseCoin(amount));
		SendResult result = wallet.sendCoins(request);
		result.broadcastComplete.addListener(() -> {
			System.out.println("Coins were sent. Transaction hash: " + result.tx.getHashAsString());
		}, MoreExecutors.sameThreadExecutor());
	}
	
	/**
	 * @author Francis Fasola
	 * When coins are received this will report information; used as an event listner.
	 * 
	 * @param wallet The wallet receiving the coins.
	 * @param tx The transaction sending the coins.
	 * @param prevBalance The previous balance of the wallet.
	 * @param newBalance The new balance of the wallet.
	 */
	private void coinsRecevied(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
		System.out.println("****** COINS RECEIVED ******");
		System.out.println("HASH: " + tx.getHashAsString());
		System.out.println("RECEIVED: " + tx.getValue(wallet) + " . NEW BALANCE: " + wallet.getBalance());
		System.out.println("PREVIOUS BALANCE: " + prevBalance.value + " NEW BALANCE: " + newBalance.value);
		System.out.println("VERSION: " + tx.getVersion());
		Coin value = tx.getValueSentToMe(wallet);
		System.out.println("Received transaction for " + value.toFriendlyString() + ": " + tx);
	}
	
	/***
	 * @author Francis Fasola
	 * Adds the event listeners to the wallet.
	 */
	private void addEventListeners() {
		//onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance)
		this.wallet.addCoinsReceivedEventListener(this::coinsRecevied);
		//			onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance)
		this.wallet.addCoinsSentEventListener(new WalletCoinsSentEventListener() {
			
			@Override
			public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
				JOptionPane.showMessageDialog(null, "RECEIVED: " + tx.getValue(wallet).toFriendlyString() + " . NEW BALANCE: " 
						+ wallet.getBalance().toFriendlyString(), "****** COINS SENT ******", JOptionPane.OK_OPTION);	
				WalletGUI.lblCurrentBalanceAmount.setText(wallet.getBalance(BalanceType.AVAILABLE).toFriendlyString());
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
	
	/***
	 * Returns the list of Friend Objects for the user.
	 * @author Francis Fasola
	 * @return List of Friend Objects.
	 */
	public List<Friend> getFriendsKeys() {
		return this.user.getFriendKeys();
	}
	
	/***
	 * Adds a key to the list of friend keys.
	 * @author Francis Fasola
	 * 
	 * @param name Alias for the given key.
	 * @param key Public key of the alias.
	 */
	public void addFriend(String name, String key) {
		this.user.getFriendKeys().add(new Friend(name, key));
	}

// Potentially not needed, use LoginList.save() when want to save.
//	/**
//	 * @author Francis Fasola
//	 * Loads the user into the WalletController.
//	 * 
//	 * @throws IOException If the file is not found.
//	 * @throws Exception Any other errors.
//	 */
//	public void loadUser () throws IOException, Exception {
//		try {
//			ObjectInputStream stream = new ObjectInputStream(new FileInputStream(path));
//			Object o = stream.readObject();
//			if (o instanceof User)
//				user = (User)o;
//			else {
//				stream.close();
//				throw new Exception("File does not contain friend key data!"); 
//			}
//			stream.close();
//		} catch (FileNotFoundException e) {
//			throw new IOException("ERROR with file.", e);
//		}catch (IOException e) {
//			e.printStackTrace();
//			throw new IOException("ERROR with file.", e);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception("Unkown error; friendKeys not loaded.", e);
//		}
//	}
//	
//	/***
//	 * Saves the current user.
//	 * @author Francis Fasola
//	 * 
//	 * @throws IOException If the file path is not valid.
//	 * @throws Exception Any other exceptions.
//	 */
//	public void saveUser() throws IOException, Exception {		
//		File file = new File(path);
//		try {
//			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
//			stream.writeObject(user);
//			stream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new IOException("ERROR with file.", e);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception("Unkown error; friendKeys not saved!", e);
//		}
//	}
}