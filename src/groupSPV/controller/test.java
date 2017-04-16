/**
 * 
 */
package groupSPV.controller;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;

/**
 * @author Frankie Fasola
 *
 */
public class test {

	private JFrame frame;
	private JTable table;
	private JTable table_1;
	private JTable table_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test window = new test();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public test() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table = new JTable();
		frame.getContentPane().add(table, BorderLayout.NORTH);
		
		table_1 = new JTable();
		frame.getContentPane().add(table_1, BorderLayout.WEST);
		
		table_2 = new JTable();
		frame.getContentPane().add(table_2, BorderLayout.EAST);
	}

}
