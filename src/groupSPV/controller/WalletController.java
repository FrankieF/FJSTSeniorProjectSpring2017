package groupSPV.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import org.bitcoinj.core.listeners.TransactionConfidenceEventListener;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;
import org.bitcoinj.wallet.Wallet.SendResult;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;

import com.google.common.util.concurrent.MoreExecutors;

import groupSPV.model.Friend;
import groupSPV.model.User;
import groupSPV.view.WalletGUI;

/** Interfaces between the client and the wallet by wrapping the wallet methods.
 * @author Frankie Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva  */
public class WalletController {
	/** Current Wallet object. */
	private Wallet wallet;
	/** Current NetworkParameter object. */
	private NetworkParameters params;
	/** Current User object. */
	private User user;

	/** Constructs a new wallet controller object.
	 * @param wallet The current wallet.
	 * @param user The current user.
	 * @author Francis Fasola */
	public WalletController (Wallet wallet, User user) {
		this.wallet = wallet;
		params = wallet.getParams();
		this.user = user;
		loadUser();		
		addEventListeners();
	}
	
	/** Only used for internal class testing.
	 * @param params Network Parameters. */
	public WalletController (NetworkParameters params) {
		wallet = new Wallet(params);
		this.params = params;
		addEventListeners();
	}
	
	/** Get the Wallet object.
	 * @author Francis Fasola
	 * @return The wallet. */
	public Wallet getWallet() {
		return this.wallet;
	}
	
	/** Returns The balance of the specified by the type passed in. The type
	 * can be left null to receive available balance of the wallet. 
	 * @author Francis Fasola
	 * @param type BalanceType to retrieve.
	 * @return The balance of the wallet. */
	public Coin getBalance(Wallet.BalanceType type) {
		return type == null ? this.wallet.getBalance() : this.wallet.getBalance(type);		
	}
	
	/** Adds an address to the wallet.
	 * @author Francis Fasola
	 * @param address The address to add to the wallet. */
	public void addAddress (Address address) {
		this.wallet.addWatchedAddress(address);
	}
	
	/** Allows a user to change the password to their wallet.
	 * @author Francis Fasola
	 * @param newPassword The new password.
	 * @param oldPassword The old password. */
	public void changePassword (CharSequence newPassword, CharSequence oldPassword) {
		this.wallet.changeEncryptionPassword(oldPassword, newPassword);
	}
	
	/** Gets a list of transactions starting with the most recent.
	 * @author Francis Fasola
	 * @return List of transactions. */
	public List<Transaction> getTransactions() {
		return this.wallet.getTransactionsByTime();
	}
	
	/** Gets a list of all pending transactions.
	 * @author Francis Fasola
	 * @return The pending transactions in the wallet. */
	public Collection<Transaction> getPendingTransactions() {
		return this.wallet.getPendingTransactions();
	}
	
	/** Returns a list of transaction outputs that represent the unspent amount in the wallet.
	 * @author Francis Fasola
	 * @return List of TransactionOutput objects. */
	public List<TransactionOutput> getUnspent() {
		return this.wallet.getUnspents();
	}
	
	/** Returns the description of the wallet.
	 * @author Francis Fasola
	 * @return Description as string. */
	public String getDescription() {
		return this.wallet.getDescription();
	}
	
	/** Allows the user to change the description of the wallet.
	 * @author Francis Fasola
	 * @param desc The new description of the wallet. */
	public void setDescription(String desc) {
		this.wallet.setDescription(desc);
	}
	
	/** Get the wallet version.
	 * @author Francis Fasola
	 * @return The version of the wallet. */
	public int getVersion () {
		return this.wallet.getVersion();
	}
	
	/** Adds multiple pre-existing keys to the wallet.
	 * @author Francis Fasola
	 * @param keys String representation of the private key(s). */
	public void addExistingKeys(String... keys) {
		for (String key : keys) {
			DumpedPrivateKey _key = DumpedPrivateKey.fromBase58(params, key);
			this.wallet.importKey(_key.getKey());	
		}
	}
	
	/** Creates a new ECKey (Eliptic Curve) and adds it to the wallet.
	 * @author Francis Fasola */
	public void addNewKey() {
		ECKey key = new ECKey();
		this.wallet.importKey(key);
		System.out.println("Public key: " + key.toAddress(params));
		System.out.println("Private key: " + key.getPrivateKeyEncoded(params));
	}
	
	/** Gets user keys.
	 * @author Francis Fasola
	 * @return The list of keys imported into the wallet. */
	public List<ECKey> getKeys() {
		return this.wallet.getImportedKeys();
	}
	
	/** Gets the User.
	 * @return Current user. */
	public User getUser() {
		return this.user;
	}
	
	/** Sends Bitcoin to the specified bitcoin address.
	 * @author Francis Fasola
	 * @param address String representation of the public address.
	 * @param amount The amount of Bitcoin to send.
	 * @throws InsufficientMoneyException Not enough money.
	 * @throws ExecutionException Error during execution.
	 * @throws InterruptedException Error during execution. */
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
	
	/** When coins are received this will report information; used as an event listener.
	 * @author Francis Fasola
	 * @param wallet The wallet receiving the coins.
	 * @param tx The transaction sending the coins.
	 * @param prevBalance The previous balance of the wallet.
	 * @param newBalance The new balance of the wallet. */
	private void coinsRecevied(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
		Coin value = tx.getValueSentToMe(wallet);
		JOptionPane.showMessageDialog(null, "Coins receive.\nHASH: " + tx.getHashAsString() + 
									"\nRECEIVED: " + tx.getValue(wallet) + "\nPREVIOUS BALANCE: " + 
									prevBalance.value + " NEW BALANCE: " + newBalance.value +
									"VERSION: " + tx.getVersion() + "Received transaction for " + 
									value.toFriendlyString() + ": " + tx);
	}
	
	/** Adds the event listeners to the wallet.
	 * @author Francis Fasola */
	private void addEventListeners() {
		this.wallet.addCoinsReceivedEventListener(this::coinsRecevied);
		
		this.wallet.addCoinsSentEventListener(new WalletCoinsSentEventListener() {
			@Override
			public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
				JOptionPane.showMessageDialog(null, "RECEIVED: " + tx.getValue(wallet).toFriendlyString() + " . NEW BALANCE: " 
						+ wallet.getBalance().toFriendlyString(), "****** COINS SENT ******", JOptionPane.OK_OPTION);	
			}
		});

		this.wallet.addKeyChainEventListener(e -> {
			JOptionPane.showMessageDialog(null, "Key added.", "", JOptionPane.OK_OPTION);
		});
	}
	
	/** Add required listeners for WalletGUI updating.
	 * @param walletGUI WalletGUI to update. */
	public void addListenersForGUI(WalletGUI walletGUI) {
		getWallet().addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
			@Override
			public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
				walletGUI.updatePendingTransactionTable();
				walletGUI.updateTransactionTable();
				walletGUI.updateBalance(wallet.getBalance(BalanceType.AVAILABLE));
			}
		});
		
		getWallet().addCoinsSentEventListener(new WalletCoinsSentEventListener() {
			@Override
			public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
				walletGUI.updatePendingTransactionTable();
				walletGUI.updateTransactionTable();
				walletGUI.updateBalance(wallet.getBalance(BalanceType.AVAILABLE));
			}
		});
		
		getWallet().addTransactionConfidenceEventListener(new TransactionConfidenceEventListener() {
			@Override
			public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
				walletGUI.updatePendingTransactionTable();
				walletGUI.updateTransactionTable();
				walletGUI.updateBalance(wallet.getBalance(BalanceType.AVAILABLE));
			}
		});
	}
	
	/** Returns the list of Friend Objects for the user.
	 * @author Francis Fasola
	 * @return List of Friend Objects. */
	public List<Friend> getFriendsKeys() {
		return this.user.getFriendKeys();
	}
	
	/**  Adds a key to the list of friend keys.
	 * @author Francis Fasola
	 * @param name Alias for the given key.
	 * @param key Public key of the alias. */
	public void addFriend(String name, String key) {
		this.user.getFriendKeys().add(new Friend(name, key));
		saveUser();
	}
	
	/** Loads the user into the WalletController.
	 * @author Francis Fasola */
	public void loadUser() {
		ObjectInputStream stream = null;
		File file = new File(Utils.getSystemPath(user.getUsername() + "\\friends.ser"));
		if (file.exists()) {
			try {
				stream = new ObjectInputStream(new FileInputStream(file));
				Object o = stream.readObject();
				if (o instanceof User)
					user = (User)o;
				else 
					throw new Exception("File does not contain user data and may be corrupt!"); 
				stream.close();
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
	
	/** Saves the current user.
	 * @author Francis Fasola */
	public void saveUser() {		
		File file = new File(Utils.getSystemPath(user.getUsername() + "\\friends.ser"));
		try {
			if (!file.exists())
				file.createNewFile();
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
			stream.writeObject(user);
			stream.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}