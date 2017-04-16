package groupSPV.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.wallet.Wallet.BalanceType;

import groupSPV.controller.ConversionRate;
import groupSPV.controller.WalletController;
import groupSPV.model.Friend;

import javax.swing.JScrollBar;
import javax.swing.JTextPane;

public class WalletGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private WalletController wc;
	private JTable table;
	private JTable transactionTable;
	private JTable pendingTransactionTable;
	private JTable friendTable;
	private JScrollPane scrollPane;
	private JScrollPane pendingTransactionScrollPane;
	private JScrollPane transactionScrollPane;
	private JScrollPane friendScrollPane;
	private JButton sendButton;
	private JButton addFriendButton;
	public static JLabel lblCurrentBalanceAmount;
	private JTextPane addressPane;
	private JTextPane amountPane;
	private JLabel addressLabel, amountLabel;
	private JLabel friendNameLabel;
	private JLabel publicKeyLabel;
	private JTextPane nameTextPane;
	private JTextPane publicKeyTextPane;
	private JTextPane keyPane;
	private JLabel keyLabel;
	private JButton addKeyButton;
	private JButton randomKeyButton;
	
	public WalletGUI(WalletController walletController){
		super(walletController.getUser().getUsername() + "'s Wallet.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		setBounds(400, 400, 600, 443);
		
		wc = walletController;
		
		JLabel lblCurrentBalance = new JLabel("Current Balance:");
		getContentPane().add(lblCurrentBalance);
		
		lblCurrentBalanceAmount = new JLabel(wc.getBalance(BalanceType.AVAILABLE).toFriendlyString());
		getContentPane().add(lblCurrentBalanceAmount);
		
		JLabel lblBitcoinValue = new JLabel("Bitcoin Value:");
		getContentPane().add(lblBitcoinValue);
		
		ConversionRate rate = new ConversionRate();
		rate.update();
		JLabel lblBitcoinValueAmount = new JLabel("$"+rate.getConversionRate().toPlainString());
		getContentPane().add(lblBitcoinValueAmount);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {},
			new String[] {"Keys"}) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		scrollPane = new JScrollPane(table);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 117, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 198, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -371, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -796, SpringLayout.EAST, getContentPane());
		getContentPane().add(scrollPane);
		updateTable();
		
		transactionTable = new JTable();
		transactionTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"Transactions"}) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		transactionScrollPane = new JScrollPane(transactionTable);
		springLayout.putConstraint(SpringLayout.NORTH, transactionScrollPane, 50, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, transactionScrollPane, 198, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, transactionScrollPane, -64, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(transactionScrollPane);
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
		springLayout.putConstraint(SpringLayout.EAST, transactionScrollPane, -178, SpringLayout.WEST, pendingTransactionScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, pendingTransactionScrollPane, 0, SpringLayout.NORTH, transactionScrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, pendingTransactionScrollPane, 0, SpringLayout.SOUTH, transactionScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, pendingTransactionScrollPane, -258, SpringLayout.EAST, getContentPane());
		getContentPane().add(pendingTransactionScrollPane);
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
		springLayout.putConstraint(SpringLayout.WEST, pendingTransactionScrollPane, 0, SpringLayout.WEST, friendScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, friendScrollPane, 103, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, friendScrollPane, 178, SpringLayout.EAST, scrollPane);
		springLayout.putConstraint(SpringLayout.SOUTH, friendScrollPane, 0, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, friendScrollPane, -258, SpringLayout.EAST, getContentPane());
		getContentPane().add(friendScrollPane);
		updateFriendTable();
		
		sendButton = new JButton("Send Bitcoin");
		springLayout.putConstraint(SpringLayout.NORTH, sendButton, 88, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, sendButton, -6, SpringLayout.NORTH, scrollPane);
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
		getContentPane().add(sendButton);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblCurrentBalance, 31, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblCurrentBalance, 23, SpringLayout.WEST, getContentPane());
		
		springLayout.putConstraint(SpringLayout.NORTH, lblCurrentBalanceAmount, 0, SpringLayout.NORTH, lblCurrentBalance);
		springLayout.putConstraint(SpringLayout.WEST, lblCurrentBalanceAmount, 6, SpringLayout.EAST, lblCurrentBalance);

		springLayout.putConstraint(SpringLayout.NORTH, lblBitcoinValue, 12, SpringLayout.SOUTH, lblCurrentBalance);
		springLayout.putConstraint(SpringLayout.WEST, lblBitcoinValue, 0, SpringLayout.WEST, lblCurrentBalance);

		springLayout.putConstraint(SpringLayout.WEST, lblBitcoinValueAmount, 21, SpringLayout.EAST, lblBitcoinValue);
		springLayout.putConstraint(SpringLayout.SOUTH, lblBitcoinValueAmount, 0, SpringLayout.SOUTH, lblBitcoinValue);
		
		addressPane = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, addressPane, 57, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, addressPane, 202, SpringLayout.EAST, lblBitcoinValueAmount);
		springLayout.putConstraint(SpringLayout.SOUTH, addressPane, -628, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, addressPane, 0, SpringLayout.EAST, scrollPane);
		getContentPane().add(addressPane);
		
		amountPane = new JTextPane();
		springLayout.putConstraint(SpringLayout.WEST, sendButton, 26, SpringLayout.EAST, amountPane);
		springLayout.putConstraint(SpringLayout.NORTH, amountPane, 11, SpringLayout.SOUTH, addressPane);
		springLayout.putConstraint(SpringLayout.WEST, amountPane, 0, SpringLayout.WEST, addressPane);
		springLayout.putConstraint(SpringLayout.SOUTH, amountPane, -6, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, amountPane, -124, SpringLayout.EAST, scrollPane);
		getContentPane().add(amountPane);
		
		addressLabel = new JLabel("Address");
		springLayout.putConstraint(SpringLayout.NORTH, addressLabel, 0, SpringLayout.NORTH, lblBitcoinValue);
		getContentPane().add(addressLabel);
		
		amountLabel = new JLabel("Amount");
		springLayout.putConstraint(SpringLayout.EAST, addressLabel, 0, SpringLayout.EAST, amountLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, amountLabel, -6, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, amountLabel, -12, SpringLayout.WEST, amountPane);
		getContentPane().add(amountLabel);
		
		friendNameLabel = new JLabel("Name");
		springLayout.putConstraint(SpringLayout.NORTH, friendNameLabel, 0, SpringLayout.NORTH, friendScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, friendNameLabel, 6, SpringLayout.EAST, friendScrollPane);
		getContentPane().add(friendNameLabel);
		
		publicKeyLabel = new JLabel("Public Key");
		springLayout.putConstraint(SpringLayout.NORTH, publicKeyLabel, 17, SpringLayout.SOUTH, friendNameLabel);
		springLayout.putConstraint(SpringLayout.WEST, publicKeyLabel, 6, SpringLayout.EAST, friendScrollPane);
		getContentPane().add(publicKeyLabel);
		
		nameTextPane = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, nameTextPane, 40, SpringLayout.NORTH, lblBitcoinValue);
		springLayout.putConstraint(SpringLayout.SOUTH, nameTextPane, 0, SpringLayout.SOUTH, friendNameLabel);
		springLayout.putConstraint(SpringLayout.EAST, nameTextPane, -37, SpringLayout.EAST, getContentPane());
		getContentPane().add(nameTextPane);
		
		publicKeyTextPane = new JTextPane();
		springLayout.putConstraint(SpringLayout.WEST, nameTextPane, 0, SpringLayout.WEST, publicKeyTextPane);
		springLayout.putConstraint(SpringLayout.NORTH, publicKeyTextPane, 68, SpringLayout.NORTH, lblBitcoinValue);
		springLayout.putConstraint(SpringLayout.WEST, publicKeyTextPane, 6, SpringLayout.EAST, publicKeyLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, publicKeyTextPane, 0, SpringLayout.SOUTH, publicKeyLabel);
		springLayout.putConstraint(SpringLayout.EAST, publicKeyTextPane, -37, SpringLayout.EAST, getContentPane());
		getContentPane().add(publicKeyTextPane);
		
		addFriendButton = new JButton("Add Friend");
		springLayout.putConstraint(SpringLayout.NORTH, addFriendButton, 12, SpringLayout.SOUTH, publicKeyTextPane);
		springLayout.putConstraint(SpringLayout.EAST, addFriendButton, -77, SpringLayout.EAST, getContentPane());
		getContentPane().add(addFriendButton);
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
		
		keyPane = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, keyPane, 46, SpringLayout.SOUTH, lblBitcoinValue);
		springLayout.putConstraint(SpringLayout.WEST, keyPane, 34, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, keyPane, 140, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, keyPane, -2, SpringLayout.WEST, scrollPane);
		getContentPane().add(keyPane);
		
		keyLabel = new JLabel("Key");
		springLayout.putConstraint(SpringLayout.NORTH, keyLabel, 7, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, keyLabel, -6, SpringLayout.WEST, keyPane);
		getContentPane().add(keyLabel);
		
		addKeyButton = new JButton("Add Key");
		springLayout.putConstraint(SpringLayout.NORTH, addKeyButton, 6, SpringLayout.SOUTH, keyPane);
		springLayout.putConstraint(SpringLayout.EAST, addKeyButton, -62, SpringLayout.WEST, scrollPane);
		getContentPane().add(addKeyButton);
		addKeyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!keyPane.getText().isEmpty())
					try {
						wc.addExistingKeys(keyPane.getText());
						updateTable();
					} catch (Exception e) { 
						JOptionPane.showMessageDialog(null, "The key entered is not valid.", "****** Inavlid Key ******", JOptionPane.ERROR_MESSAGE);
					}
				else
					JOptionPane.showMessageDialog(null, "Enter your private key.", 
												"****** No Key ******", JOptionPane.OK_OPTION);
			}});
		randomKeyButton = new JButton("Add Random Key");
		springLayout.putConstraint(SpringLayout.NORTH, randomKeyButton, 14, SpringLayout.SOUTH, addKeyButton);
		springLayout.putConstraint(SpringLayout.EAST, randomKeyButton, -43, SpringLayout.WEST, scrollPane);
		getContentPane().add(randomKeyButton);
		randomKeyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					wc.addNewKey();
					updateTable();
				} catch (Exception e) { 
					JOptionPane.showMessageDialog(null, "The key entered is not valid.", "****** Inavlid Key ******", JOptionPane.ERROR_MESSAGE);
				}
			}});
		
		//pack();
		setVisible(true);
	}
	
	/**
	 * Updates table
	 * @throws BlockStoreException
	 */
	public void updateTable(){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		if (wc.getKeys().isEmpty()) {
			Object[] row = {"No keys."};
			model.addRow(row);
		} else {
			Iterator<ECKey> it = wc.getKeys().iterator();
			while(it.hasNext()){
				
				Object[] row = {it.next().toString()};
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
