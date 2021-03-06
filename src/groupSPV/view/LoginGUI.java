package groupSPV.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import groupSPV.controller.LoginList;
import groupSPV.controller.Utils;
import groupSPV.model.User;

/**
 *
 * @author Spencer
 */
public class LoginGUI extends JFrame {

	private static final long serialVersionUID = -251358090366870208L;
	
	/** JLabel for user information. */
	private JLabel userLabel, passwordLabel, welcomeLabel;
	/** JPasswordField for obscured password input. */
	private JPasswordField passwordField;
	/** JTextField for username input. */
	private JTextField userField;
	/** JButton for user interaction. */
	private JButton loginBtn, registerBtn;

	/** Creates new form WalletLoginGUI */
	public LoginGUI() {
		/* Set Nimbus look and feel. */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println(e.getMessage());
		}
		initComponents();
		Utils.setWindowCenterOfScreen(this);
		setVisible(true);
	}

	/** This method is called from within the constructor to initialize the form. */
	private void initComponents() {
		userLabel = new JLabel();
		passwordLabel = new JLabel();
		userField = new JTextField();
		loginBtn = new JButton();
		registerBtn = new JButton();
		passwordField = new JPasswordField();
		welcomeLabel = new JLabel();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Client Login" + (Utils.isTestNetwork() ? " (TESTNET)" : ""));

		userLabel.setText("Username:");

		passwordLabel.setText("Password:");

		loginBtn.setText("Login");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				loginBtnActionPerformed(evt);
			}
		});

		registerBtn.setText("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				registerBtnActionPerformed(evt);
			}
		});

		welcomeLabel.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
		welcomeLabel.setText("Welcome");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(35, 35, 35)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(welcomeLabel, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(userLabel).addComponent(passwordLabel))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(passwordField, GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addComponent(registerBtn)
												.addGap(21, 21, 21).addComponent(loginBtn))
										.addComponent(userField))))
				.addContainerGap(54, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(42, 42, 42)
						.addComponent(welcomeLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(userLabel)
								.addComponent(userField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(passwordLabel)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(loginBtn)
								.addComponent(registerBtn))
						.addGap(36, 36, 36)));

		userField.getAccessibleContext().setAccessibleName("usernameTextField");
		userField.getAccessibleContext().setAccessibleDescription("");

		getRootPane().setDefaultButton(loginBtn);

		pack();
	}

	/** Verifies the User or displays an error.
	 * @param evt Not-utilized ActionEvent. */
	private void loginBtnActionPerformed(ActionEvent evt) {
		User user;
		if ((user = LoginList.verifyUser(userField.getText(), new String(passwordField.getPassword()))) != null) {
			this.dispose();
			Utils.startGUI(user);
		} else {
			JOptionPane.showMessageDialog(null, "Either Username or Password is incorrect", "Login Error",
					JOptionPane.ERROR_MESSAGE);
			userField.setText("");
			passwordField.setText("");
		}
	}

	/** Opens the RegisterGUI.
	 * @param evt Not-utilized ActionEvent. */
	private void registerBtnActionPerformed(ActionEvent evt) {
		new RegisterGUI();
	}
}
