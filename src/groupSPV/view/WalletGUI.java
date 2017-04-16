package groupSPV.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.wallet.Wallet.BalanceType;

import groupSPV.controller.ConversionRate;
import groupSPV.controller.WalletController;
import groupSPV.model.Friend;

public class WalletGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private WalletController wc;
	private JTable keyTable;
	private JTable transactionTable;
	private JTable pendingTransactionTable;
	private JTable friendTable;
	private JScrollPane keyScrollPane;
	private JScrollPane pendingTransactionScrollPane;
	private JScrollPane transactionScrollPane;
	private JScrollPane friendScrollPane;
	private JButton sendButton;
	private JButton addFriendButton;
	private static JLabel lblBitcoinValueAmount;
	private static JLabel lblCurrentBalanceAmount;
	private JTextPane addressPane;
	private JTextPane amountPane;
	private JTextPane nameTextPane;
	private JTextPane publicKeyTextPane;
	private JTextPane keyPane;
	private JButton addKeyButton;
	private JButton randomKeyButton;
	
	public WalletGUI(WalletController walletController){
		super(walletController.getUser().getUsername() + "'s Wallet.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		setBounds(400, 400, 600, 443);
		
		/* -----------------------
		 * Component Initialization
		 * ----------------------- */
		wc = walletController;
		
		JLabel lblCurrentBalance = new JLabel("Current Balance:");
		lblCurrentBalanceAmount = new JLabel();
		JLabel lblBitcoinValue = new JLabel("Bitcoin Value:");
		lblBitcoinValueAmount = new JLabel();
		updateBalance(wc.getBalance(BalanceType.AVAILABLE));
		
		addressPane = new JTextPane();
		JLabel addressLabel = new JLabel("Address");
		amountPane = new JTextPane();
		JLabel amountLabel = new JLabel("Amount");
		sendButton = new JButton("Send Bitcoin");
		
		JLabel keyLabel = new JLabel("Key");
		keyPane = new JTextPane();
		addKeyButton = new JButton("Add Key");
		randomKeyButton = new JButton("Add Random Key");
		
		JLabel friendNameLabel = new JLabel("Name");
		nameTextPane = new JTextPane();
		JLabel publicKeyLabel = new JLabel("Public Key");
		publicKeyTextPane = new JTextPane();
		addFriendButton = new JButton("Add Friend");
		
		/* --------------------
		 * Table Initialization
		 * -------------------- */
		keyTable = new JTable();
		keyTable.setModel(new DefaultTableModel(new Object[][] {},
			new String[] {"Keys"}) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		keyScrollPane = new JScrollPane(keyTable);
		updateKeyTable();
		
		transactionTable = new JTable();
		transactionTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"Transactions"}) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		transactionScrollPane = new JScrollPane(transactionTable);
		updateTransactionTable();
		
		pendingTransactionTable = new JTable();
		pendingTransactionTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"Pending Transactions"}) {
			private static final long serialVersionUID = 1l;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		pendingTransactionScrollPane = new JScrollPane(pendingTransactionTable);
		updatePendingTransactionTable();
		
		friendTable = new JTable();
		friendTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"Friend Keys"}) {
			private static final long serialVersionUID = 1l;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		friendScrollPane = new JScrollPane(friendTable);
		updateFriendTable();
		
		
		/* ------------------
		 * Layout Constraints
		 * ------------------ */
		springLayout.putConstraint(SpringLayout.NORTH, keyScrollPane, 117, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, keyScrollPane, 198, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, keyScrollPane, -371, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, keyScrollPane, -796, SpringLayout.EAST, getContentPane());
		
		springLayout.putConstraint(SpringLayout.NORTH, transactionScrollPane, 50, SpringLayout.SOUTH, keyScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, transactionScrollPane, 198, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, transactionScrollPane, -64, SpringLayout.SOUTH, getContentPane());
		
		springLayout.putConstraint(SpringLayout.EAST, transactionScrollPane, -178, SpringLayout.WEST, pendingTransactionScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, pendingTransactionScrollPane, 0, SpringLayout.NORTH, transactionScrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, pendingTransactionScrollPane, 0, SpringLayout.SOUTH, transactionScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, pendingTransactionScrollPane, -258, SpringLayout.EAST, getContentPane());
		
		springLayout.putConstraint(SpringLayout.WEST, pendingTransactionScrollPane, 0, SpringLayout.WEST, friendScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, friendScrollPane, 0, SpringLayout.NORTH, keyScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, friendScrollPane, 178, SpringLayout.EAST, keyScrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, friendScrollPane, 0, SpringLayout.SOUTH, keyScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, friendScrollPane, -258, SpringLayout.EAST, getContentPane());
		
		springLayout.putConstraint(SpringLayout.NORTH, lblCurrentBalance, 31, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblCurrentBalance, 23, SpringLayout.WEST, getContentPane());
		
		springLayout.putConstraint(SpringLayout.NORTH, lblCurrentBalanceAmount, 0, SpringLayout.NORTH, lblCurrentBalance);
		springLayout.putConstraint(SpringLayout.WEST, lblCurrentBalanceAmount, 6, SpringLayout.EAST, lblCurrentBalance);

		springLayout.putConstraint(SpringLayout.NORTH, lblBitcoinValue, 12, SpringLayout.SOUTH, lblCurrentBalance);
		springLayout.putConstraint(SpringLayout.WEST, lblBitcoinValue, 0, SpringLayout.WEST, lblCurrentBalance);

		springLayout.putConstraint(SpringLayout.WEST, lblBitcoinValueAmount, 21, SpringLayout.EAST, lblBitcoinValue);
		springLayout.putConstraint(SpringLayout.SOUTH, lblBitcoinValueAmount, 0, SpringLayout.SOUTH, lblBitcoinValue);
		
		springLayout.putConstraint(SpringLayout.NORTH, addressPane, 31, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, addressPane, 202, SpringLayout.EAST, lblCurrentBalanceAmount);
		springLayout.putConstraint(SpringLayout.EAST, addressPane, 0, SpringLayout.EAST, keyScrollPane);
		
		springLayout.putConstraint(SpringLayout.NORTH, sendButton, 0, SpringLayout.NORTH, amountPane);
		springLayout.putConstraint(SpringLayout.EAST, sendButton, 0, SpringLayout.EAST, keyScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, sendButton, -100, SpringLayout.EAST, keyScrollPane);
		
		springLayout.putConstraint(SpringLayout.EAST, amountPane, -26, SpringLayout.WEST, sendButton);
		springLayout.putConstraint(SpringLayout.NORTH, amountPane, 11, SpringLayout.SOUTH, addressPane);
		springLayout.putConstraint(SpringLayout.WEST, amountPane, 0, SpringLayout.WEST, addressPane);
		
		springLayout.putConstraint(SpringLayout.NORTH, addressLabel, 0, SpringLayout.NORTH, addressPane);
		springLayout.putConstraint(SpringLayout.EAST, addressLabel, 0, SpringLayout.EAST, amountLabel);
		
		springLayout.putConstraint(SpringLayout.NORTH, amountLabel, 0, SpringLayout.NORTH, amountPane);
		springLayout.putConstraint(SpringLayout.EAST, amountLabel, -12, SpringLayout.WEST, amountPane);
		
		
		springLayout.putConstraint(SpringLayout.NORTH, friendNameLabel, 0, SpringLayout.NORTH, friendScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, friendNameLabel, 6, SpringLayout.EAST, friendScrollPane);
		
		springLayout.putConstraint(SpringLayout.NORTH, publicKeyLabel, 17, SpringLayout.SOUTH, friendNameLabel);
		springLayout.putConstraint(SpringLayout.WEST, publicKeyLabel, 0, SpringLayout.WEST, friendNameLabel);
		
		springLayout.putConstraint(SpringLayout.NORTH, nameTextPane, 0, SpringLayout.NORTH, friendScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, nameTextPane, -37, SpringLayout.EAST, getContentPane());
		
		springLayout.putConstraint(SpringLayout.WEST, nameTextPane, 0, SpringLayout.WEST, publicKeyTextPane);
		springLayout.putConstraint(SpringLayout.NORTH, publicKeyTextPane, 12, SpringLayout.SOUTH, nameTextPane);
		springLayout.putConstraint(SpringLayout.WEST, publicKeyTextPane, 6, SpringLayout.EAST, publicKeyLabel);
		springLayout.putConstraint(SpringLayout.EAST, publicKeyTextPane, 0, SpringLayout.EAST, nameTextPane);
		
		springLayout.putConstraint(SpringLayout.NORTH, addFriendButton, 12, SpringLayout.SOUTH, publicKeyTextPane);
		springLayout.putConstraint(SpringLayout.EAST, addFriendButton, -77, SpringLayout.EAST, getContentPane());
		
		springLayout.putConstraint(SpringLayout.NORTH, keyPane, 46, SpringLayout.SOUTH, lblBitcoinValue);
		springLayout.putConstraint(SpringLayout.WEST, keyPane, 34, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, keyPane, 140, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, keyPane, -2, SpringLayout.WEST, keyScrollPane);
		
		springLayout.putConstraint(SpringLayout.NORTH, keyLabel, 7, SpringLayout.NORTH, keyScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, keyLabel, -6, SpringLayout.WEST, keyPane);
		
		springLayout.putConstraint(SpringLayout.NORTH, addKeyButton, 6, SpringLayout.SOUTH, keyPane);
		springLayout.putConstraint(SpringLayout.EAST, addKeyButton, -62, SpringLayout.WEST, keyScrollPane);
		
		springLayout.putConstraint(SpringLayout.NORTH, randomKeyButton, 14, SpringLayout.SOUTH, addKeyButton);
		springLayout.putConstraint(SpringLayout.EAST, randomKeyButton, -43, SpringLayout.WEST, keyScrollPane);
		
		
		/* -------------------
		 * Component Placement
		 * ------------------- */
		getContentPane().add(lblCurrentBalance);
		getContentPane().add(lblCurrentBalanceAmount);
		getContentPane().add(lblBitcoinValue);
		getContentPane().add(lblBitcoinValueAmount);
		
		getContentPane().add(addressPane);
		getContentPane().add(addressLabel);
		getContentPane().add(amountPane);
		getContentPane().add(amountLabel);
		getContentPane().add(sendButton);
		
		getContentPane().add(keyLabel);
		getContentPane().add(keyPane);
		getContentPane().add(addKeyButton);
		getContentPane().add(randomKeyButton);
		
		getContentPane().add(friendNameLabel);
		getContentPane().add(nameTextPane);
		getContentPane().add(publicKeyLabel);
		getContentPane().add(publicKeyTextPane);
		getContentPane().add(addFriendButton);
		
		getContentPane().add(keyScrollPane);
		getContentPane().add(transactionScrollPane);
		getContentPane().add(pendingTransactionScrollPane);
		getContentPane().add(friendScrollPane);
		
	
		/* ----------------
		 * Button Listeners
		 * ---------------- */
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!addressPane.getText().isEmpty() && !amountPane.getText().isEmpty())
					try {
						wc.sendBitcoin(addressPane.getText(), amountPane.getText());
					} catch (Exception e) { 
						JOptionPane.showMessageDialog(null, "The address or amount is not in the correct format.", "****** Invalid Input ******", JOptionPane.ERROR_MESSAGE);
					}
				else
					JOptionPane.showMessageDialog(null, "Select an address to send to..", 
												"****** No Address selected ******", JOptionPane.OK_OPTION);
			}});
		
		addFriendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!nameTextPane.getText().isEmpty() && !publicKeyTextPane.getText().isEmpty())
					try {
						wc.addFriend(nameTextPane.getText(), publicKeyTextPane.getText());
						updateFriendTable();
					} catch (Exception e) { 
						JOptionPane.showMessageDialog(null, "The address is not a valid address.", "****** Inavlid Addres ******", JOptionPane.ERROR_MESSAGE);
					}
				else
					JOptionPane.showMessageDialog(null, "Enter a name and address for the friend.", 
												"****** No Friend Information ******", JOptionPane.OK_OPTION);
			}});
		
		addKeyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!keyPane.getText().isEmpty())
					try {
						wc.addExistingKeys(keyPane.getText());
						updateKeyTable();
					} catch (Exception e) { 
						JOptionPane.showMessageDialog(null, "The key entered is not valid.", "****** Inavlid Key ******", JOptionPane.ERROR_MESSAGE);
					}
				else
					JOptionPane.showMessageDialog(null, "Enter your private key.", 
												"****** No Key ******", JOptionPane.OK_OPTION);
			}});
		
		randomKeyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					wc.addNewKey();
					updateKeyTable();
				} catch (Exception e) { 
					JOptionPane.showMessageDialog(null, "The key entered is not valid.", "****** Inavlid Key ******", JOptionPane.ERROR_MESSAGE);
				}
			}});
		
		setVisible(true);
	}
	
	/* ----------------------
	 * Update Functions
	 * ---------------------- */
	
	public static void updateBalance(Coin availableBalance) {
		ConversionRate.update(); // Get most recent exchange rate.
		lblCurrentBalanceAmount.setText(availableBalance.toFriendlyString());
		lblBitcoinValueAmount.setText("$"+ConversionRate.convert(availableBalance).setScale(2, RoundingMode.HALF_UP));
	}

	/**
	 * Updates user keys table
	 * @throws BlockStoreException
	 */
	public void updateKeyTable(){
		DefaultTableModel model = (DefaultTableModel) keyTable.getModel();
		model.setRowCount(0);
		if (wc.getKeys().isEmpty()) {
			Object[] row = {"No keys."};
			model.addRow(row);
		} else {
			Iterator<ECKey> it = wc.getKeys().iterator();
			while(it.hasNext()){
				ECKey currentKey = it.next();
				//ssssssssssssssSystem.out.println(currentKey);
				Object[] row = {currentKey.toAddress(wc.getWallet().getParams())};
				model.addRow(row);
			}
		}
	}
	
	/***
	 * Adds the transactions into the table.
	 * @author Francis Fasola
	 */
	public void updateTransactionTable() {
		DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
		model.setRowCount(0);
		if (wc.getTransactions().isEmpty()) {
			Object[] row = {"No transactions."};
			model.addRow(row);
		}
		Iterator<Transaction> it = wc.getTransactions().iterator();
		while(it.hasNext()) {
			Object[] row = {it.next().toString()};
			model.addRow(row);
		}
	}
	
	/***
	 * Adds the pending transactions into the table.
	 * @author Francis Fasola
	 */
	public void updatePendingTransactionTable() {
		DefaultTableModel model = (DefaultTableModel) pendingTransactionTable.getModel();
		model.setRowCount(0);
		if (wc.getPendingTransactions().isEmpty()) {
			Object[] row = {"No pending transactions."};
			model.addRow(row);
		}
		else {
			Iterator<Transaction> it = wc.getPendingTransactions().iterator();
			while(it.hasNext()) {
				Object[] row = {it.next().toString()};
				model.addRow(row);
			}
		}
	}
	
	/***
	 * Adds the pending transactions into the table.
	 * @author Francis Fasola
	 */
	public void updateFriendTable() {
		DefaultTableModel model = (DefaultTableModel) friendTable.getModel();
		model.setRowCount(0);
		if (wc.getFriendsKeys().isEmpty()) {
			Object[] row = {"No friend keys."};
			model.addRow(row);
		}
		else {
			Iterator<Friend> it = wc.getFriendsKeys().iterator();
			while(it.hasNext()) {
				Object[] row = {it.next().toString()};
				model.addRow(row);
			}
		}
	}
}