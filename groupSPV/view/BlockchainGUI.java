package groupSPV.view;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.store.BlockStoreException;

import groupSPV.controller.BlockchainDriver;
import groupSPV.model.CustomKit;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BlockchainGUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane scrollPane;
	private BlockchainDriver driver;
	private JButton btnNewButton;
	
	public BlockchainGUI() {
		super("Blockchain");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		driver = new BlockchainDriver();
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Block", "Hash", "Time", "Version"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
		
		
		btnNewButton = new JButton("Update Table");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Need to add some type of waiting feature
				CustomKit kit = driver.getKit();
				kit.startAndWait();
				try {
					updateTable(table, kit);
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
			
	}
	
	/**
	 * Updates table with given map from query and table
	 * @param Map<Item, Inventory> map
	 * @param JTable table
	 * @throws BlockStoreException 
	 */
	private static void updateTable(JTable table, CustomKit kit) throws BlockStoreException{
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		StoredBlock working = kit.getChainHead();
		StoredBlock previous = working.getPrev(kit.getBlockStore());
		for(int i = 0; i < 20; i++){
			kit.getBlockStoreVersion(working);
			Object[] row = {Integer.toString(working.getHeight()),working.getHeader().getHashAsString(),
					kit.getBlockStoreTime(working).trim(),kit.getBlockStoreVersion(working).trim()};
			working = previous; 
			previous = working.getPrev(kit.getBlockStore());
			model.addRow(row);
		}
	}
}
