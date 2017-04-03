package groupSPV.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.wallet.Wallet.BalanceType;

import groupSPV.controller.WalletController;

public class WalletGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private WalletController wc;
	private JTable table;
	private JTable transactionTable;
	private JTable pendingTransactionTable;
	private JScrollPane scrollPane;
	private JScrollPane pendingTransactionScrollPane;
	private JScrollPane transactionScrollPane;
	private JButton sendButton;
	public static JLabel lblCurrentBalanceAmount;
	
	public WalletGUI(WalletController walletController){
		super("Wallet");
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
		
		JLabel lblBitcoinValueAmount = new JLabel("$989.14");
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
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 46, SpringLayout.SOUTH, lblBitcoinValue);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 69, SpringLayout.WEST, getContentPane());
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
		springLayout.putConstraint(SpringLayout.NORTH, transactionScrollPane, 0, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, transactionScrollPane, 53, SpringLayout.EAST, scrollPane);
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
		springLayout.putConstraint(SpringLayout.EAST, transactionScrollPane, -52, SpringLayout.WEST, pendingTransactionScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, pendingTransactionScrollPane, 920, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, pendingTransactionScrollPane, 0, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, pendingTransactionScrollPane, -102, SpringLayout.EAST, getContentPane());
		getContentPane().add(pendingTransactionScrollPane);
		updatePendingTransactionTable();
		
		sendButton = new JButton("Send Bitcoin");
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 111, SpringLayout.EAST, sendButton);
		springLayout.putConstraint(SpringLayout.WEST, sendButton, 79, SpringLayout.EAST, lblBitcoinValueAmount);
		springLayout.putConstraint(SpringLayout.SOUTH, sendButton, 0, SpringLayout.SOUTH, lblBitcoinValue);
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					wc.sendBitcoin("mrJMoMskRKXm3YxyR6koBbggqb2KcfTsRY", ".5");
				} catch (Exception e) { e.printStackTrace(); }				
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
		Iterator<ECKey> it = wc.getKeys().iterator();
		
		while(it.hasNext()){
			
			Object[] row = {it.next().toString()};
			model.addRow(row);
		}
	}
	
	/***
	 * Adds the transactions into the table.
	 * @author Francis Fasola
	 */
	public void updateTransactionTable() {
		DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
		model.setRowCount(0);
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
		Iterator<Transaction> it = wc.getPendingTransactions().iterator();
		while(it.hasNext()) {
			Object[] row = {it.next().toString()};
			model.addRow(row);
		}
	}
}
