package groupSPV.view;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import groupSPV.controller.Utils;


/** JFrame to spawn while waiting for Blockchain to synchronize.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class SyncGUI extends JFrame {

	private static final long serialVersionUID = -9191920674195749323L;

	// TODO Fix JLabel showing up
	public SyncGUI(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Makes it not closable.

		JLabel label = new JLabel();
		label.setFont(new Font("Tahoma", 0, 14));
		label.setText("Downloading/Synchronizing Bitcoin Block Chain");
		
		JPanel panel = new JPanel();
		panel.add(label);
		
		add(panel);

		pack();
		Utils.setWindowCenterOfScreen(this);
		setVisible(true);
	}
}