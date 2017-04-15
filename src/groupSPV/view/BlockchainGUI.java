package groupSPV.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.store.BlockStoreException;

import groupSPV.model.CustomKit;

/** BlockchainGUI is the user interface that appears after login
 * that displays block number, hash, time, and version with
 * options to open the user's wallet of extend a block's info
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class BlockchainGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnOpenWallet;
	private static JButton btnMoreInfo;
	private CustomKit kit;

	public BlockchainGUI(CustomKit kit) {
		super("Blockchain");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		this.kit = kit;

		
		/**************************************
		/	      Setup of the table	   	  /
		/									  /
		/*************************************/
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Block", "Hash", "Time", "Version"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMaxWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(650);
		table.getColumnModel().getColumn(1).setMaxWidth(650);
		scrollPane = new JScrollPane(table);
		
		/**************************************
		/	      Setup of the JButtons   	  /
		/									  /
		/*************************************/
		btnOpenWallet = new JButton("Wallet");
		
		btnMoreInfo = new JButton("More Info");
		btnMoreInfo.setVisible(false);
		
		/**************************************
		/	      Adding Listeners to    	  /
		/		  components				  /
		/*************************************/
		btnMoreInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExtendedBlockchain ebc;
				try {
					ebc = new ExtendedBlockchain(Integer.valueOf(table.getModel().getValueAt(table.getSelectedRow(),0).toString()),
							kit.getFullBlock(Sha256Hash.wrap(table.getModel().getValueAt(table.getSelectedRow(),1).toString())));
					ebc.setVisible(true);
				} catch (NumberFormatException | InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnMoreInfo.setVisible(true);
			}
		});
		
		btnOpenWallet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new WalletGUI(kit.getWalletController());
			}
		});
		
		/**************************************
		/	     Adding components to 	   	  /
		/		  	contentpane				  /
		/*************************************/
		getContentPane().add(scrollPane);
		getContentPane().add(btnMoreInfo);
		getContentPane().add(btnOpenWallet);
		
		try {
			updateTable(); // Initial call to populate table
		} catch (BlockStoreException bse) {
			System.err.println(bse.getMessage());
		}
		
		/**************************************
		/	      	Setup of constraints   	  /
		/		  							  /
		/*************************************/
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 50, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 50, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -50, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -50, SpringLayout.EAST, getContentPane());

		springLayout.putConstraint(SpringLayout.NORTH, btnOpenWallet, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnOpenWallet, -10, SpringLayout.EAST, getContentPane());

		springLayout.putConstraint(SpringLayout.SOUTH, btnMoreInfo, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnMoreInfo, 70, SpringLayout.WEST, scrollPane);
		

		setVisible(true);
	}

	/** Updates table for blocks
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
	
	/** Hides the button after selecting a block to expand
	 */
	public static void hideBtnMoreInfo(){
		btnMoreInfo.setVisible(false);
	}
}
