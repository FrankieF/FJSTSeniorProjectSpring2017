package groupSPV.view;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;

/** ExtendedBlockchain is a popup window that is displayed upon clicking
 * the more info button in the BlockchainGUI
*
* @author Frank Fasola
* @author James Donnell
* @author Spencer Escalante
* @author Trevor Silva */
public class ExtendedBlockchain extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Block fullBlock;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel lblDifficulty;
	private JLabel lblNonce;
	private JLabel lblMessageSize;
	private JLabel lblMerkleRoot;
	private JLabel lblWork;
	private JLabel lblDPW;
	private JLabel lblN;
	private JLabel lblMS;
	private JLabel lblMR;
	private JLabel lblW;
	
	public ExtendedBlockchain(int blockNumber, Block b) {
		super("Block: " + blockNumber);
		fullBlock = b;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(new Rectangle(new Dimension(550,525)));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		
		/**************************************
		/	      Setup of the table	   	  /
		/		  with scrollpane			  /
		/*************************************/
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Transactoin Hash" }) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		scrollPane = new JScrollPane(table);
		
		/**************************************
		/	      Setup of the JPanel   	  /
		/									  /
		/*************************************/		
		lblDifficulty = new JLabel("Difficulty of the Proof of Work: ");
		lblNonce = new JLabel("Nonce:");
		lblMessageSize = new JLabel("Message Size:");
		lblMerkleRoot = new JLabel("Merkle Root:");
		lblWork = new JLabel("Work:");
		lblDPW = new JLabel(String.valueOf(fullBlock.getDifficultyTarget()));
		lblN = new JLabel(String.valueOf(fullBlock.getNonce()));
		lblMS = new JLabel(String.valueOf(fullBlock.getMessageSize()));
		lblMR = new JLabel(String.valueOf(fullBlock.getMerkleRoot().toString()));
		lblW = new JLabel(String.valueOf(fullBlock.getWork())  + " tries");
		
		/**************************************
		/	      Add Everything to 	   	  /
		/		     contentpane			  /
		/*************************************/
		getContentPane().add(scrollPane);
		getContentPane().add(lblDifficulty);
		getContentPane().add(lblNonce);
		getContentPane().add(lblMessageSize);
		getContentPane().add(lblMerkleRoot);
		getContentPane().add(lblWork);
		getContentPane().add(lblDPW);
		getContentPane().add(lblN);
		getContentPane().add(lblMS);
		getContentPane().add(lblMR);
		getContentPane().add(lblW);
		
		update();		

		/**************************************
		/	      Constraint setup		   	  /
		/									  /
		/*************************************/
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 150, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, getContentPane());

		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, lblDifficulty);
		springLayout.putConstraint(SpringLayout.NORTH, lblDifficulty, 34, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblDifficulty, 10, SpringLayout.WEST, getContentPane());

		springLayout.putConstraint(SpringLayout.NORTH, lblNonce, 6, SpringLayout.SOUTH, lblDifficulty);
		springLayout.putConstraint(SpringLayout.WEST, lblNonce, 0, SpringLayout.WEST, lblDifficulty);

		springLayout.putConstraint(SpringLayout.NORTH, lblMessageSize, 6, SpringLayout.SOUTH, lblNonce);
		springLayout.putConstraint(SpringLayout.WEST, lblMessageSize, 0, SpringLayout.WEST, lblDifficulty);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblMerkleRoot, 6, SpringLayout.SOUTH, lblMessageSize);
		springLayout.putConstraint(SpringLayout.WEST, lblMerkleRoot, 0, SpringLayout.WEST, lblDifficulty);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblWork, 6, SpringLayout.SOUTH, lblMerkleRoot);
		springLayout.putConstraint(SpringLayout.WEST, lblWork, 0, SpringLayout.WEST, lblDifficulty);

		springLayout.putConstraint(SpringLayout.WEST, lblDPW, 6, SpringLayout.EAST, lblDifficulty);
		springLayout.putConstraint(SpringLayout.SOUTH, lblDPW, 0, SpringLayout.SOUTH, lblDifficulty);

		springLayout.putConstraint(SpringLayout.NORTH, lblN, 6, SpringLayout.SOUTH, lblDifficulty);
		springLayout.putConstraint(SpringLayout.WEST, lblN, 6, SpringLayout.EAST, lblNonce);

		springLayout.putConstraint(SpringLayout.WEST, lblMS, 6, SpringLayout.EAST, lblMessageSize);
		springLayout.putConstraint(SpringLayout.SOUTH, lblMS, 0, SpringLayout.SOUTH, lblMessageSize);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblMR, 0, SpringLayout.NORTH, lblMerkleRoot);
		springLayout.putConstraint(SpringLayout.WEST, lblMR, 6, SpringLayout.EAST, lblMerkleRoot);
		
		springLayout.putConstraint(SpringLayout.WEST, lblW, 6, SpringLayout.EAST, lblWork);
		springLayout.putConstraint(SpringLayout.SOUTH, lblW, 0, SpringLayout.SOUTH, lblWork);
	}
	
	/**
	 * Updates the table that contains Transaction Hashes
	 */
	public void update(){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (Transaction t : fullBlock.getTransactions()) {
			Object[] row = {t.getHashAsString()};
			model.addRow(row);
		}
	}
}
