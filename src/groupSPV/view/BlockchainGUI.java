package groupSPV.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.store.BlockStoreException;

import groupSPV.model.CustomKit;

public class BlockchainGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnNewButton;
	private CustomKit kit;

	public BlockchainGUI(CustomKit kit) {
		super("Blockchain");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		this.kit = kit;

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Block", "Hash", "Time", "Version" }) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);

		// TODO Remove this button, not needed with CustomNBBL.java -James
		btnNewButton = new JButton("Update Table");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					updateTable();
				} catch (BlockStoreException e) {
					e.printStackTrace();
				}
			}
		});

		getContentPane().add(btnNewButton);

		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 50, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 50, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -50, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -50, SpringLayout.EAST, getContentPane());

		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton, 0, SpringLayout.EAST, scrollPane);

		JButton btnOpenWallet = new JButton("Wallet Login");
		btnOpenWallet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new WalletLoginGUI(kit.getWalletController());
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnOpenWallet, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnOpenWallet, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnOpenWallet);

		try {
			updateTable(); // Initial call to populate table
		} catch (BlockStoreException bse) {
			System.err.println(bse.getMessage());
		}

		setVisible(true);
	}

	/** Updates table
	 * @throws BlockStoreException */
	public void updateTable() throws BlockStoreException {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		StoredBlock working = kit.getChainHead();
		StoredBlock previous = working.getPrev(kit.getBlockStore());
		for (int i = 0; i < 200; i++) {
			kit.getBlockStoreVersion(working);
			Object[] row = { Integer.toString(working.getHeight()), working.getHeader().getHashAsString(),
					kit.getBlockStoreTime(working).trim(), kit.getBlockStoreVersion(working).trim() };
			working = previous;
			previous = working.getPrev(kit.getBlockStore());
			model.addRow(row);
		}
	}
}
