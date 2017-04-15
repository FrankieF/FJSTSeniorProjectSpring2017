package groupSPV.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


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
		setWindowCenterOfScreen(this);
		setVisible(true);
	}
	
	/** Sets the given window location to the center of the User's screen.
	 * @param window Window to center. */
	private static void setWindowCenterOfScreen(Window window) {
		Dimension userScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(userScreenSize.width/2-window.getSize().width/2, userScreenSize.height/2-window.getSize().height/2);
	}
}