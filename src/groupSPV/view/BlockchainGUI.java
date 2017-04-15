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

	private static final long serialVersionUID = 6385080842879973690L;
	
	/** Main JTable. */
	private JTable table;
	
	/** Main JScrollPane. */
	private JScrollPane scrollPane;
	
	/** Current CustomKit. */
	private CustomKit kit;

	/** Creates and displays the BlockchainGUI.
	 * @param kit CustomKit to utilize. */
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

		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 50, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 50, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -50, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -50, SpringLayout.EAST, getContentPane());

		JButton btnOpenWallet = new JButton("Wallet");
		btnOpenWallet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new WalletGUI(kit.getWalletController());
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
	 * @throws BlockStoreException Thrown if BlockStore does not have previous block.  */
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
